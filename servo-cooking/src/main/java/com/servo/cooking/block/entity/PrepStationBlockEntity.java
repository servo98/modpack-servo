package com.servo.cooking.block.entity;

import com.servo.cooking.CookingRegistry;
import com.servo.cooking.menu.PrepStationMenu;
import com.servo.cooking.recipe.PrepStationRecipe;
import com.servo.cooking.recipe.PrepStationRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Prep Station block entity — cold assembly workstation.
 *
 * 4 input slots + 1 output slot. Recipe matching is instant:
 * whenever inputs change, the output is recalculated.
 * The output slot shows the result; extracting it consumes one of each input.
 *
 * WorldlyContainer for hopper compat:
 * - Top: accepts ingredients into input slots
 * - Bottom/Sides: outputs result
 */
public class PrepStationBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {

    public static final int SLOT_INPUT_0 = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_INPUT_2 = 2;
    public static final int SLOT_INPUT_3 = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int SLOT_COUNT = 5;
    public static final int INPUT_SLOTS = 4;

    private static final int[] SLOTS_TOP = {SLOT_INPUT_0, SLOT_INPUT_1, SLOT_INPUT_2, SLOT_INPUT_3};
    private static final int[] SLOTS_BOTTOM_SIDE = {SLOT_OUTPUT};

    private final ItemStack[] items = new ItemStack[SLOT_COUNT];

    public PrepStationBlockEntity(BlockPos pos, BlockState state) {
        super(CookingRegistry.PREP_STATION_BE.get(), pos, state);
        for (int i = 0; i < SLOT_COUNT; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    // === MenuProvider ===

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.servo_cooking.prep_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new PrepStationMenu(containerId, playerInv, this);
    }

    // === Recipe matching ===

    /**
     * Called whenever inputs change. Recalculates the output based on recipe match.
     */
    public void updateRecipeOutput() {
        if (level == null || level.isClientSide()) return;

        PrepStationRecipeInput input = createRecipeInput();
        Optional<RecipeHolder<PrepStationRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(CookingRegistry.PREP_STATION_RECIPE_TYPE.get(), input, level);

        if (recipe.isPresent()) {
            items[SLOT_OUTPUT] = recipe.get().value().assemble(input, level.registryAccess());
        } else {
            items[SLOT_OUTPUT] = ItemStack.EMPTY;
        }
        setChanged();
    }

    /**
     * Consumes one of each input ingredient and recalculates the output.
     * Called when extracting from the output slot.
     */
    private void consumeInputsAndRecalculate() {
        if (level == null || level.isClientSide()) return;

        // Verify the recipe still matches before consuming
        PrepStationRecipeInput input = createRecipeInput();
        Optional<RecipeHolder<PrepStationRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(CookingRegistry.PREP_STATION_RECIPE_TYPE.get(), input, level);

        if (recipe.isPresent()) {
            // Consume one of each non-empty input
            for (int i = 0; i < INPUT_SLOTS; i++) {
                if (!items[i].isEmpty()) {
                    items[i].shrink(1);
                }
            }
            // Recalculate output for remaining items
            updateRecipeOutput();
        }
    }

    private PrepStationRecipeInput createRecipeInput() {
        return new PrepStationRecipeInput(
                items[SLOT_INPUT_0],
                items[SLOT_INPUT_1],
                items[SLOT_INPUT_2],
                items[SLOT_INPUT_3]
        );
    }

    public boolean hasRecipeResult() {
        return !items[SLOT_OUTPUT].isEmpty();
    }

    /** Drop all contents when block is broken. */
    public void dropAllContents(Level level, BlockPos pos) {
        for (int i = 0; i < INPUT_SLOTS; i++) {
            if (!items[i].isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), items[i]);
                items[i] = ItemStack.EMPTY;
            }
        }
        // Don't drop the output — it's computed from inputs
    }

    // === NBT persistence ===

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag list = new ListTag();
        // Only save input slots — output is recomputed on load
        for (int i = 0; i < INPUT_SLOTS; i++) {
            CompoundTag slotTag = new CompoundTag();
            slotTag.putByte("Slot", (byte) i);
            if (!items[i].isEmpty()) {
                items[i].save(registries, slotTag);
            }
            list.add(slotTag);
        }
        tag.put("Items", list);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ListTag list = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < SLOT_COUNT; i++) {
            items[i] = ItemStack.EMPTY;
        }
        for (int i = 0; i < list.size(); i++) {
            CompoundTag slotTag = list.getCompound(i);
            int slot = slotTag.getByte("Slot") & 0xFF;
            if (slot < INPUT_SLOTS) {
                items[slot] = ItemStack.parse(registries, slotTag).orElse(ItemStack.EMPTY);
            }
        }
        // Output will be recalculated on first access / when level is available
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
        return side == Direction.UP ? SLOTS_TOP : SLOTS_BOTTOM_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return slot >= SLOT_INPUT_0 && slot <= SLOT_INPUT_3 && direction == Direction.UP;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_OUTPUT && direction != Direction.UP && hasRecipeResult();
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) return ItemStack.EMPTY;
        return items[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot < 0 || slot >= SLOT_COUNT) return ItemStack.EMPTY;

        if (slot == SLOT_OUTPUT) {
            // Taking from output: produce the result and consume inputs
            if (!items[SLOT_OUTPUT].isEmpty()) {
                ItemStack result = items[SLOT_OUTPUT].copy();
                int taken = Math.min(amount, result.getCount());
                result.setCount(taken);
                // Consume one set of inputs and recalculate
                consumeInputsAndRecalculate();
                return result;
            }
            return ItemStack.EMPTY;
        }

        // Input slots: standard split
        ItemStack result = items[slot].split(amount);
        if (!result.isEmpty()) {
            setChanged();
            updateRecipeOutput();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) return ItemStack.EMPTY;
        ItemStack result = items[slot];
        items[slot] = ItemStack.EMPTY;
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SLOT_COUNT) return;
        items[slot] = stack;
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
        if (slot != SLOT_OUTPUT) {
            updateRecipeOutput();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(this.worldPosition.getX() + 0.5,
                this.worldPosition.getY() + 0.5,
                this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            items[i] = ItemStack.EMPTY;
        }
        setChanged();
    }
}
