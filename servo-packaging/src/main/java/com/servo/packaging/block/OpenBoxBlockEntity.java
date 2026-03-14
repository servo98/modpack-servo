package com.servo.packaging.block;

import com.servo.packaging.PackagingRegistry;
import com.servo.packaging.component.BoxContents;
import com.servo.packaging.item.ShippingBoxItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Block entity for the Open Box placed in the world.
 * Stores items inserted by the player or hoppers.
 * All items must be the same type. Max 4 stacks (up to 16 items total by default).
 */
public class OpenBoxBlockEntity extends BlockEntity implements WorldlyContainer {

    public static final int MAX_SLOTS = 4;
    public static final int ITEMS_PER_BOX = 16;

    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
    private int totalCount = 0;
    @Nullable
    private ResourceLocation packedItemId;
    private String category = "";

    private static final int[] SLOTS_TOP = {0, 1, 2, 3};
    private static final int[] SLOTS_NONE = {};

    public OpenBoxBlockEntity(BlockPos pos, BlockState state) {
        super(PackagingRegistry.OPEN_BOX_BE.get(), pos, state);
    }

    // === Player interaction ===

    /**
     * Try to add the held item to the box. Returns true if successful.
     */
    public boolean tryAddItem(Player player, ItemStack heldItem) {
        if (isSealed()) return false;
        if (heldItem.isEmpty()) return false;
        if (!isPackable(heldItem)) return false;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(heldItem.getItem());

        // First item determines type
        if (packedItemId == null) {
            packedItemId = itemId;
            category = detectCategory(heldItem);
        } else if (!itemId.equals(packedItemId)) {
            return false; // wrong type
        }

        // Always add as many as possible from the held stack
        int toAdd = Math.min(heldItem.getCount(), ITEMS_PER_BOX - totalCount);

        if (toAdd <= 0) return false;

        // Distribute into slots
        int remaining = toAdd;
        for (int i = 0; i < MAX_SLOTS && remaining > 0; i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty()) {
                int put = Math.min(remaining, heldItem.getItem().getDefaultMaxStackSize());
                items.set(i, new ItemStack(heldItem.getItem(), put));
                remaining -= put;
            } else if (ItemStack.isSameItemSameComponents(slot, heldItem)) {
                int canFit = slot.getMaxStackSize() - slot.getCount();
                int put = Math.min(remaining, canFit);
                if (put > 0) {
                    slot.grow(put);
                    remaining -= put;
                }
            }
        }

        int actualAdded = toAdd - remaining;
        heldItem.shrink(actualAdded);
        totalCount += actualAdded;

        playSound(SoundEvents.BUNDLE_INSERT);
        updateBlockState();
        syncToClient();
        setChanged();

        // Auto-seal when full
        if (totalCount >= ITEMS_PER_BOX) {
            seal();
        }

        return actualAdded > 0;
    }

    /**
     * Remove the last item from the box. Returns the removed stack.
     */
    public ItemStack tryRemoveItem() {
        if (isSealed()) return ItemStack.EMPTY;

        for (int i = MAX_SLOTS - 1; i >= 0; i--) {
            if (!items.get(i).isEmpty()) {
                ItemStack removed = items.get(i).copy();
                removed.setCount(1);
                items.get(i).shrink(1);
                if (items.get(i).isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                }
                totalCount--;
                if (totalCount <= 0) {
                    packedItemId = null;
                    category = "";
                    totalCount = 0;
                }
                playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                updateBlockState();
                syncToClient();
                setChanged();
                return removed;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Seal the box — creates a ShippingBox and replaces the block with air.
     */
    public void seal() {
        if (level == null || level.isClientSide()) return;
        if (packedItemId == null || totalCount <= 0) return;

        // Update blockstate to sealed
        BlockState state = getBlockState();
        level.setBlock(worldPosition, state.setValue(OpenBoxBlock.SEALED, true), 3);

        playSound(SoundEvents.BARREL_CLOSE);
        syncToClient();
        setChanged();
    }

    /**
     * Pick up the sealed box as a ShippingBox item.
     */
    public ItemStack pickUpSealed() {
        if (packedItemId == null) return ItemStack.EMPTY;
        ItemStack box = ShippingBoxItem.createBox(packedItemId, totalCount, category);
        clearContent();
        return box;
    }

    /**
     * Populate this block entity from a ShippingBox's contents (when placing a sealed box).
     */
    public void loadFromBoxContents(BoxContents contents) {
        clearContent();
        packedItemId = contents.itemId();
        category = contents.category();
        totalCount = contents.count();

        // Distribute items into slots
        net.minecraft.world.item.Item item = BuiltInRegistries.ITEM.get(contents.itemId());
        int remaining = contents.count();
        for (int i = 0; i < MAX_SLOTS && remaining > 0; i++) {
            int put = Math.min(remaining, item.getDefaultMaxStackSize());
            items.set(i, new ItemStack(item, put));
            remaining -= put;
        }

        setChanged();
        syncToClient();
    }

    public boolean isSealed() {
        return getBlockState().getValue(OpenBoxBlock.SEALED);
    }

    public boolean isEmpty() {
        return totalCount == 0;
    }

    public int getTotalCount() {
        return totalCount;
    }

    @Nullable
    public ResourceLocation getPackedItemId() {
        return packedItemId;
    }

    public List<ItemStack> getVisibleItems() {
        List<ItemStack> visible = new ArrayList<>();
        for (ItemStack item : items) {
            if (!item.isEmpty()) visible.add(item);
        }
        return visible;
    }

    /** Drop all contents when block is broken. */
    public void dropAllContents() {
        if (level == null) return;
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(),
                        worldPosition.getZ(), item.copy());
            }
        }
    }

    // === Helpers ===

    private boolean isPackable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(PackagingRegistry.OPEN_BOX_ITEM.get())) return false;
        if (stack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get())) return false;
        if (stack.is(PackagingRegistry.SHIPPING_BOX_ITEM.get())) return false;
        return stack.is(PackagingRegistry.PACKABLE_TAG);
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

    private void updateBlockState() {
        if (level == null) return;
        BlockState state = getBlockState();
        int itemCount = Math.min((int) getVisibleItems().size(), 4);
        BlockState newState = state.setValue(OpenBoxBlock.ITEMS_STORED, itemCount);
        if (!state.equals(newState)) {
            level.setBlock(worldPosition, newState, 3);
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private void recalculateTotal() {
        totalCount = 0;
        ResourceLocation firstId = null;
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                totalCount += item.getCount();
                if (firstId == null) {
                    firstId = BuiltInRegistries.ITEM.getKey(item.getItem());
                }
            }
        }
        if (totalCount == 0) {
            packedItemId = null;
            category = "";
        } else if (packedItemId == null) {
            packedItemId = firstId;
        }
    }

    // === NBT persistence ===

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("TotalCount", totalCount);
        tag.putString("Category", category);
        if (packedItemId != null) {
            tag.putString("PackedItemId", packedItemId.toString());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, items, registries);
        totalCount = tag.getInt("TotalCount");
        category = tag.getString("Category");
        if (tag.contains("PackedItemId")) {
            packedItemId = ResourceLocation.parse(tag.getString("PackedItemId"));
        }
    }

    // === Client sync ===

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // === WorldlyContainer ===

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (isSealed()) return SLOTS_NONE;
        return side == Direction.UP ? SLOTS_TOP : SLOTS_NONE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        if (isSealed() || direction != Direction.UP) return false;
        if (!isPackable(stack)) return false;
        if (packedItemId != null && !BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(packedItemId)) {
            return false;
        }
        return totalCount < ITEMS_PER_BOX;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return false; // no extraction from open box via hopper
    }

    @Override
    public int getContainerSize() {
        return MAX_SLOTS;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < MAX_SLOTS ? items.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            recalculateTotal();
            updateBlockState();
            syncToClient();
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (isSealed()) return;
        if (slot < 0 || slot >= MAX_SLOTS) return;

        if (!stack.isEmpty() && isPackable(stack)) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (packedItemId == null) {
                packedItemId = itemId;
                category = detectCategory(stack);
            } else if (!itemId.equals(packedItemId)) {
                return; // wrong type
            }
        }

        items.set(slot, stack);
        recalculateTotal();
        updateBlockState();
        syncToClient();
        setChanged();

        if (totalCount >= ITEMS_PER_BOX) {
            seal();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
        totalCount = 0;
        packedItemId = null;
        category = "";
        setChanged();
    }
}
