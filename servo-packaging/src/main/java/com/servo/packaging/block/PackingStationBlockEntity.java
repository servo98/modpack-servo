package com.servo.packaging.block;

import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.component.BoxContents;
import com.servo.packaging.item.ShippingBoxItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for the Packing Station.
 *
 * Internal state machine:
 * - EMPTY: no box, no items
 * - BOX_PLACED: open box on station, awaiting items
 * - FILLING: box + some items (not yet full)
 * - SEALED: box is full and sealed, ready for pickup
 *
 * Implements WorldlyContainer for hopper interaction:
 * - Top: accepts Open Box only
 * - Sides: accepts packable items
 * - Bottom: outputs sealed Shipping Box
 */
public class PackingStationBlockEntity extends BlockEntity implements WorldlyContainer {

    public enum State { EMPTY, BOX_PLACED, FILLING, SEALED }

    private State currentState = State.EMPTY;

    // What item is being packed (null if empty/box-only)
    @Nullable
    private ResourceLocation packedItemId;
    private int packedCount;
    private int targetCount;
    private String category = "";

    // The sealed output box (only set in SEALED state)
    private ItemStack outputBox = ItemStack.EMPTY;

    // Slot indices for WorldlyContainer
    private static final int SLOT_BOX_IN = 0;    // top — open box input
    private static final int SLOT_ITEM_IN = 1;   // sides — item input
    private static final int SLOT_OUTPUT = 2;     // bottom — sealed box output
    private static final int[] SLOTS_TOP = {SLOT_BOX_IN};
    private static final int[] SLOTS_SIDES = {SLOT_ITEM_IN};
    private static final int[] SLOTS_BOTTOM = {SLOT_OUTPUT};

    public PackingStationBlockEntity(BlockPos pos, BlockState state) {
        super(PackagingRegistry.PACKING_STATION_BE.get(), pos, state);
    }

    // === Player interaction (right-click) ===

    public ItemInteractionResult handleInteraction(Player player, InteractionHand hand, ItemStack heldItem) {
        return switch (currentState) {
            case EMPTY -> handleEmpty(player, heldItem);
            case BOX_PLACED -> handleBoxPlaced(player, heldItem);
            case FILLING -> handleFilling(player, heldItem);
            case SEALED -> handleSealed(player);
        };
    }

    private ItemInteractionResult handleEmpty(Player player, ItemStack heldItem) {
        // Accept open box
        if (heldItem.is(PackagingRegistry.OPEN_BOX_ITEM.get())) {
            heldItem.shrink(1);
            currentState = State.BOX_PLACED;
            playSound(SoundEvents.WOOL_PLACE);
            setChanged();
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult handleBoxPlaced(Player player, ItemStack heldItem) {
        if (heldItem.isEmpty()) {
            // Empty hand on box-only state: return the open box
            returnOpenBox(player);
            return ItemInteractionResult.SUCCESS;
        }
        // Start filling with this item
        return tryAddItem(player, heldItem);
    }

    private ItemInteractionResult handleFilling(Player player, ItemStack heldItem) {
        if (heldItem.isEmpty()) {
            // Empty hand: cancel, return items + open box
            cancelPacking(player);
            return ItemInteractionResult.SUCCESS;
        }
        // Add more of the same item
        return tryAddItem(player, heldItem);
    }

    private ItemInteractionResult handleSealed(Player player) {
        // Pick up the sealed box
        if (!outputBox.isEmpty()) {
            if (!player.getInventory().add(outputBox.copy())) {
                player.drop(outputBox.copy(), false);
            }
            outputBox = ItemStack.EMPTY;
            currentState = State.EMPTY;
            playSound(SoundEvents.ITEM_PICKUP);
            setChanged();
        }
        return ItemInteractionResult.SUCCESS;
    }

    // === Packing logic ===

    private ItemInteractionResult tryAddItem(Player player, ItemStack heldItem) {
        if (!isPackable(heldItem)) {
            sendActionBar(player, Component.translatable("servo_packaging.packing.not_packable"));
            return ItemInteractionResult.FAIL;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(heldItem.getItem());

        if (currentState == State.BOX_PLACED) {
            // First item determines what goes in the box
            packedItemId = itemId;
            category = detectCategory(heldItem);
            targetCount = getPackSize(heldItem);
            packedCount = 0;
        } else if (!itemId.equals(packedItemId)) {
            sendActionBar(player, Component.translatable("servo_packaging.packing.wrong_type"));
            return ItemInteractionResult.FAIL;
        }

        // Add items (shift-click = fill completely, normal = 1 at a time)
        int toAdd = player.isShiftKeyDown()
                ? Math.min(heldItem.getCount(), targetCount - packedCount)
                : Math.min(1, targetCount - packedCount);

        if (toAdd <= 0) {
            return ItemInteractionResult.FAIL;
        }

        heldItem.shrink(toAdd);
        packedCount += toAdd;
        currentState = State.FILLING;
        playSound(SoundEvents.BUNDLE_INSERT);
        setChanged();

        // Check if box is full
        if (packedCount >= targetCount) {
            sealBox();
            sendActionBar(player, Component.translatable("servo_packaging.packing.sealed"));
        } else {
            sendActionBar(player, Component.translatable("servo_packaging.packing.progress",
                    packedCount, targetCount));
        }

        return ItemInteractionResult.SUCCESS;
    }

    private void sealBox() {
        outputBox = ShippingBoxItem.createBox(packedItemId, packedCount, category);
        packedItemId = null;
        packedCount = 0;
        targetCount = 0;
        category = "";
        currentState = State.SEALED;
        playSound(SoundEvents.BARREL_CLOSE);
        setChanged();
    }

    private void returnOpenBox(Player player) {
        ItemStack openBox = new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get());
        if (!player.getInventory().add(openBox)) {
            player.drop(openBox, false);
        }
        currentState = State.EMPTY;
        setChanged();
    }

    private void cancelPacking(Player player) {
        // Return packed items
        if (packedItemId != null && packedCount > 0) {
            var item = BuiltInRegistries.ITEM.get(packedItemId);
            ItemStack returnStack = new ItemStack(item, packedCount);
            if (!player.getInventory().add(returnStack)) {
                player.drop(returnStack, false);
            }
        }
        // Return open box
        returnOpenBox(player);
        packedItemId = null;
        packedCount = 0;
        targetCount = 0;
        category = "";
        sendActionBar(player, Component.translatable("servo_packaging.packing.cancelled"));
    }

    // === Helpers ===

    private boolean isPackable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // Never pack our own items
        if (stack.is(PackagingRegistry.OPEN_BOX_ITEM.get())) return false;
        if (stack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get())) return false;
        if (stack.is(PackagingRegistry.SHIPPING_BOX_ITEM.get())) return false;
        // Must be in the packable tag
        return stack.is(PackagingRegistry.PACKABLE_TAG);
    }

    private int getPackSize(ItemStack stack) {
        // Check tags for pack size, default to 4
        if (stack.is(PackagingRegistry.PACK_SIZE_16_TAG)) return 16;
        if (stack.is(PackagingRegistry.PACK_SIZE_8_TAG)) return 8;
        if (stack.is(PackagingRegistry.PACK_SIZE_1_TAG)) return 1;
        return 4; // default
    }

    private String detectCategory(ItemStack stack) {
        if (stack.is(PackagingRegistry.CATEGORY_FOOD_TAG)) return "food";
        if (stack.is(PackagingRegistry.CATEGORY_CROPS_TAG)) return "crops";
        if (stack.is(PackagingRegistry.CATEGORY_PROCESSED_TAG)) return "processed";
        if (stack.is(PackagingRegistry.CATEGORY_MAGIC_TAG)) return "magic";
        if (stack.is(PackagingRegistry.CATEGORY_SPECIAL_TAG)) return "special";
        return "general";
    }

    private void playSound(net.minecraft.sounds.SoundEvent sound) {
        if (level != null && !level.isClientSide()) {
            level.playSound(null, worldPosition, sound, SoundSource.BLOCKS, 0.8f, 1.0f);
        }
    }

    private void sendActionBar(Player player, Component message) {
        player.displayClientMessage(message, true);
    }

    /** Drop all contents when block is broken. */
    public void dropAllContents(net.minecraft.world.level.Level level, BlockPos pos) {
        if (!outputBox.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), outputBox);
            outputBox = ItemStack.EMPTY;
        }
        if (packedItemId != null && packedCount > 0) {
            var item = BuiltInRegistries.ITEM.get(packedItemId);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(item, packedCount));
        }
        if (currentState == State.BOX_PLACED || currentState == State.FILLING) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get()));
        }
    }

    // === NBT persistence ===

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("State", currentState.ordinal());
        tag.putInt("PackedCount", packedCount);
        tag.putInt("TargetCount", targetCount);
        tag.putString("Category", category);
        if (packedItemId != null) {
            tag.putString("PackedItemId", packedItemId.toString());
        }
        if (!outputBox.isEmpty()) {
            tag.put("OutputBox", outputBox.save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        currentState = State.values()[tag.getInt("State")];
        packedCount = tag.getInt("PackedCount");
        targetCount = tag.getInt("TargetCount");
        category = tag.getString("Category");
        if (tag.contains("PackedItemId")) {
            packedItemId = ResourceLocation.parse(tag.getString("PackedItemId"));
        }
        if (tag.contains("OutputBox")) {
            outputBox = ItemStack.parse(registries, tag.getCompound("OutputBox")).orElse(ItemStack.EMPTY);
        }
    }

    // === WorldlyContainer (hopper automation) ===

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return switch (side) {
            case UP -> SLOTS_TOP;
            case DOWN -> SLOTS_BOTTOM;
            default -> SLOTS_SIDES;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        if (slot == SLOT_BOX_IN && direction == Direction.UP) {
            return currentState == State.EMPTY && stack.is(PackagingRegistry.OPEN_BOX_ITEM.get());
        }
        if (slot == SLOT_ITEM_IN && direction != Direction.UP && direction != Direction.DOWN) {
            return (currentState == State.BOX_PLACED || currentState == State.FILLING)
                    && isPackable(stack)
                    && (packedItemId == null || BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(packedItemId));
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_OUTPUT && direction == Direction.DOWN && currentState == State.SEALED;
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return currentState == State.EMPTY;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == SLOT_OUTPUT && currentState == State.SEALED) {
            return outputBox;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == SLOT_OUTPUT && currentState == State.SEALED && !outputBox.isEmpty()) {
            ItemStack result = outputBox.split(amount);
            if (outputBox.isEmpty()) {
                currentState = State.EMPTY;
                setChanged();
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == SLOT_OUTPUT && currentState == State.SEALED) {
            ItemStack result = outputBox;
            outputBox = ItemStack.EMPTY;
            currentState = State.EMPTY;
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == SLOT_BOX_IN && stack.is(PackagingRegistry.OPEN_BOX_ITEM.get()) && currentState == State.EMPTY) {
            stack.shrink(1);
            currentState = State.BOX_PLACED;
            playSound(SoundEvents.WOOL_PLACE);
            setChanged();
        } else if (slot == SLOT_ITEM_IN && (currentState == State.BOX_PLACED || currentState == State.FILLING)) {
            // Hopper inserting items
            if (isPackable(stack)) {
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
                if (currentState == State.BOX_PLACED) {
                    packedItemId = itemId;
                    category = detectCategory(stack);
                    targetCount = getPackSize(stack);
                    packedCount = 0;
                    currentState = State.FILLING;
                }
                if (itemId.equals(packedItemId)) {
                    int toAdd = Math.min(stack.getCount(), targetCount - packedCount);
                    packedCount += toAdd;
                    stack.shrink(toAdd);
                    if (packedCount >= targetCount) {
                        sealBox();
                    }
                    setChanged();
                }
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        currentState = State.EMPTY;
        packedItemId = null;
        packedCount = 0;
        targetCount = 0;
        category = "";
        outputBox = ItemStack.EMPTY;
        setChanged();
    }
}
