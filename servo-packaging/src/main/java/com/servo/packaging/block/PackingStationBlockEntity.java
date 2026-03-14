package com.servo.packaging.block;

import com.servo.packaging.PackagingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Packing Station — folds flat cardboard into open boxes.
 *
 * State machine: IDLE → FOLDING → DONE
 * - IDLE: waiting for flat_cardboard input
 * - FOLDING: animating fold (40 ticks / 2 seconds)
 * - DONE: open_box ready for pickup in output
 *
 * WorldlyContainer for hopper compat:
 * - Top: accepts flat_cardboard
 * - Bottom/Sides: outputs open_box
 */
public class PackingStationBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {

    public enum State { IDLE, FOLDING, DONE }

    private State currentState = State.IDLE;
    private ItemStack inputSlot = ItemStack.EMPTY;   // flat_cardboard
    private ItemStack outputSlot = ItemStack.EMPTY;  // open_box
    private int foldTicks = 0;
    private static final int FOLD_DURATION = 40; // 2 seconds

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;
    private static final int[] SLOTS_TOP = {SLOT_INPUT};
    private static final int[] SLOTS_BOTTOM_SIDE = {SLOT_OUTPUT};

    /** Syncs foldTicks and foldDuration to the client-side menu for the progress bar. */
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> foldTicks;
                case 1 -> FOLD_DURATION;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) foldTicks = value;
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public PackingStationBlockEntity(BlockPos pos, BlockState state) {
        super(PackagingRegistry.PACKING_STATION_BE.get(), pos, state);
    }

    // === MenuProvider ===

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.servo_packaging.packing_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new PackingStationMenu(containerId, playerInv, this, dataAccess);
    }

    // === Tick ===

    public static void serverTick(Level level, BlockPos pos, BlockState state, PackingStationBlockEntity be) {
        if (be.currentState == State.IDLE) {
            if (canStartFolding(be)) {
                be.currentState = State.FOLDING;
                be.foldTicks = 0;
                be.setChanged();
                be.syncToClient();
            }
        } else if (be.currentState == State.FOLDING) {
            be.foldTicks++;
            if (be.foldTicks >= FOLD_DURATION) {
                // Consume 1 flat_cardboard, produce 1 open_box (stacks if already has some)
                be.inputSlot.shrink(1);
                if (be.outputSlot.isEmpty()) {
                    be.outputSlot = new ItemStack(PackagingRegistry.OPEN_BOX_ITEM.get());
                } else {
                    be.outputSlot.grow(1);
                }
                be.foldTicks = 0;
                level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 0.8f, 1.2f);
                // Continue folding if possible, otherwise go idle
                if (canStartFolding(be)) {
                    be.currentState = State.FOLDING;
                } else {
                    be.currentState = State.IDLE;
                }
                be.setChanged();
                be.syncToClient();
            } else if (be.foldTicks % 10 == 0) {
                be.syncToClient();
            }
        } else if (be.currentState == State.DONE) {
            // Legacy state — transition to IDLE
            be.currentState = State.IDLE;
            be.setChanged();
        }
    }

    private static boolean canStartFolding(PackingStationBlockEntity be) {
        boolean hasInput = !be.inputSlot.isEmpty()
                && be.inputSlot.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get());
        boolean canOutput = be.outputSlot.isEmpty()
                || (be.outputSlot.is(PackagingRegistry.OPEN_BOX_ITEM.get())
                    && be.outputSlot.getCount() < be.outputSlot.getMaxStackSize());
        return hasInput && canOutput;
    }

    // === Getters for renderer ===

    public State getCurrentState() {
        return currentState;
    }

    public float getFoldProgress(float partialTick) {
        if (currentState != State.FOLDING) return currentState == State.DONE ? 1.0f : 0.0f;
        return Math.min(1.0f, (foldTicks + partialTick) / FOLD_DURATION);
    }

    public int getFoldTicks() {
        return foldTicks;
    }

    /** Drop all contents when block is broken. */
    public void dropAllContents(Level level, BlockPos pos) {
        if (!inputSlot.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inputSlot);
            inputSlot = ItemStack.EMPTY;
        }
        if (!outputSlot.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), outputSlot);
            outputSlot = ItemStack.EMPTY;
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // === NBT persistence ===

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("State", currentState.ordinal());
        tag.putInt("FoldTicks", foldTicks);
        if (!inputSlot.isEmpty()) {
            tag.put("Input", inputSlot.save(registries));
        }
        if (!outputSlot.isEmpty()) {
            tag.put("Output", outputSlot.save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        currentState = State.values()[Math.min(tag.getInt("State"), State.values().length - 1)];
        foldTicks = tag.getInt("FoldTicks");
        inputSlot = tag.contains("Input")
                ? ItemStack.parse(registries, tag.getCompound("Input")).orElse(ItemStack.EMPTY)
                : ItemStack.EMPTY;
        outputSlot = tag.contains("Output")
                ? ItemStack.parse(registries, tag.getCompound("Output")).orElse(ItemStack.EMPTY)
                : ItemStack.EMPTY;
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
        return slot == SLOT_INPUT && direction == Direction.UP
                && stack.is(PackagingRegistry.FLAT_CARDBOARD_ITEM.get());
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_OUTPUT && direction != Direction.UP && !outputSlot.isEmpty();
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return inputSlot.isEmpty() && outputSlot.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case SLOT_INPUT -> inputSlot;
            case SLOT_OUTPUT -> outputSlot;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == SLOT_OUTPUT && !outputSlot.isEmpty()) {
            ItemStack result = outputSlot.split(amount);
            setChanged();
            return result;
        }
        if (slot == SLOT_INPUT && !inputSlot.isEmpty()) {
            ItemStack result = inputSlot.split(amount);
            setChanged();
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == SLOT_OUTPUT) {
            ItemStack result = outputSlot;
            outputSlot = ItemStack.EMPTY;
            return result;
        }
        if (slot == SLOT_INPUT) {
            ItemStack result = inputSlot;
            inputSlot = ItemStack.EMPTY;
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == SLOT_INPUT) {
            inputSlot = stack;
            setChanged();
        } else if (slot == SLOT_OUTPUT) {
            outputSlot = stack;
            setChanged();
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
        inputSlot = ItemStack.EMPTY;
        outputSlot = ItemStack.EMPTY;
        currentState = State.IDLE;
        foldTicks = 0;
        setChanged();
    }
}
