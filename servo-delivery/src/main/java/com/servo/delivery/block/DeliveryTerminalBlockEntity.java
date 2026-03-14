package com.servo.delivery.block;

import com.servo.delivery.DeliveryRegistry;
import com.servo.delivery.ServoDelivery;
import com.servo.delivery.data.ChapterDelivery;
import com.servo.delivery.data.DeliveryDataLoader;
import com.servo.delivery.data.DeliverySavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Map;

/**
 * Core BlockEntity for the Delivery Terminal.
 * Handles: multiblock validation, delivery progress tracking, GeckoLib animations.
 *
 * Delivery progress is stored globally via {@link DeliverySavedData},
 * so all terminals share the same state and progress survives terminal destruction.
 */
public class DeliveryTerminalBlockEntity extends net.minecraft.world.level.block.entity.BlockEntity
        implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // === Per-terminal state ===
    private boolean formed = false;
    private boolean celebrationTriggered = false;

    // === Cached from SavedData for client-side rendering ===
    private int cachedChapter = 1;
    private int cachedProgress = 0;

    public DeliveryTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(DeliveryRegistry.TERMINAL_BE.get(), pos, state);
    }

    // ==================== GeckoLib Animation ====================

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // No Blockbench animations — all visual effects are done via BER
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ==================== Multiblock ====================

    /**
     * Attempts to form the multiblock. Called when terminal is placed or
     * when a neighboring structure block is placed.
     */
    public void tryFormMultiblock() {
        if (level == null || level.isClientSide()) return;

        Direction facing = getBlockState().getValue(DeliveryTerminalBlock.FACING);
        boolean valid = DeliveryTerminalBlock.validateStructure(level, worldPosition, facing);

        if (valid && !formed) {
            formMultiblock(facing);
        } else if (!valid && formed) {
            breakMultiblock();
        }
    }

    private void formMultiblock(Direction facing) {
        formed = true;
        level.setBlock(worldPosition, getBlockState().setValue(DeliveryTerminalBlock.FORMED, true), 3);

        // Tell all slave blocks where the master is + hide them
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        setSlaveFormed(worldPosition.relative(left), true);      // left port
        setSlaveFormed(worldPosition.relative(right), true);     // right port
        setSlaveFormed(worldPosition.below(), true);             // center base
        setSlaveFormed(worldPosition.below().relative(left), true);  // left base
        setSlaveFormed(worldPosition.below().relative(right), true); // right base
        setSlaveFormed(worldPosition.above(), true);             // antenna

        refreshCache();
        setChanged();
        syncToClient();

        ServoDelivery.LOGGER.info("Delivery Terminal formed at {}", worldPosition);
    }

    private void breakMultiblock() {
        formed = false;
        unformSlaves();
        level.setBlock(worldPosition, getBlockState().setValue(DeliveryTerminalBlock.FORMED, false), 3);
        setChanged();
        syncToClient();
        ServoDelivery.LOGGER.info("Delivery Terminal broken at {}", worldPosition);
    }

    /**
     * Resets all slave blocks to FORMED=false (visible again).
     * Called from breakMultiblock() and from DeliveryTerminalBlock.onRemove().
     */
    public void unformSlaves() {
        Direction facing = getBlockState().getValue(DeliveryTerminalBlock.FACING);
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        setSlaveFormed(worldPosition.relative(left), false);
        setSlaveFormed(worldPosition.relative(right), false);
        setSlaveFormed(worldPosition.below(), false);
        setSlaveFormed(worldPosition.below().relative(left), false);
        setSlaveFormed(worldPosition.below().relative(right), false);
        setSlaveFormed(worldPosition.above(), false);
    }

    private void setSlaveFormed(BlockPos slavePos, boolean formedState) {
        if (level.getBlockEntity(slavePos) instanceof SlaveBlockEntity slave) {
            slave.setMasterPos(formedState ? worldPosition : null);
        }
        // Set FORMED blockstate on slave to hide/show its vanilla model
        BlockState slaveState = level.getBlockState(slavePos);
        if (slaveState.hasProperty(DeliveryPortBlock.FORMED)) {
            level.setBlock(slavePos, slaveState.setValue(DeliveryPortBlock.FORMED, formedState), 3);
        } else if (slaveState.hasProperty(ElevatorBaseBlock.FORMED)) {
            level.setBlock(slavePos, slaveState.setValue(ElevatorBaseBlock.FORMED, formedState), 3);
        } else if (slaveState.hasProperty(ElevatorAntennaBlock.FORMED)) {
            level.setBlock(slavePos, slaveState.setValue(ElevatorAntennaBlock.FORMED, formedState), 3);
        }
    }

    // ==================== Delivery Logic ====================

    /**
     * Attempts to insert an item into the terminal.
     * Called from right-click or from DeliveryPort automation.
     * Does NOT auto-complete the chapter — only the LAUNCH button does that.
     *
     * @return true if the item was accepted and consumed
     */
    public boolean tryInsertItem(ItemStack stack, @Nullable Player player) {
        if (level == null || level.isClientSide() || !formed) return false;

        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter());
        if (chapter == null) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("servo_delivery.terminal.no_chapter"), true);
            }
            return false;
        }

        // Check if this item matches any requirement
        String matchedReq = chapter.findMatchingRequirement(stack, data.getDeliveredItems());
        if (matchedReq == null) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("servo_delivery.terminal.not_needed"), true);
            }
            return false;
        }

        // Accept the item
        data.deliverItem(matchedReq);
        stack.shrink(1);

        // Update cached values for rendering (NO auto-complete)
        refreshCache();
        syncToClient();

        return true;
    }

    /**
     * Called when the player presses the LAUNCH button in the GUI.
     * Only completes the chapter if all requirements are met.
     */
    public void launchDelivery() {
        if (level == null || level.isClientSide()) return;

        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter());
        if (chapter == null || !chapter.isComplete(data.getDeliveredItems())) return;

        onChapterComplete(data);
    }

    private void onChapterComplete(DeliverySavedData data) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        celebrationTriggered = true;

        // Fire event for servo_core to grant stages, trigger quests, etc.
        DeliveryCompleteEvent.fire(serverLevel, worldPosition, data.getCurrentChapter());

        // Advance to next chapter (clears delivered items)
        data.advanceChapter();

        refreshCache();
        setChanged();
        syncToClient();
    }

    /**
     * Refreshes cached chapter/progress values from SavedData.
     * Called after modifications and periodically from serverTick.
     */
    private void refreshCache() {
        if (level == null || level.isClientSide() || level.getServer() == null) return;
        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        cachedChapter = data.getCurrentChapter();
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(cachedChapter);
        cachedProgress = chapter != null ? chapter.getProgressPercent(data.getDeliveredItems()) : 0;
    }

    // ==================== Screen ====================

    public void openScreen(Player player) {
        if (!formed) {
            player.displayClientMessage(
                    Component.translatable("servo_delivery.terminal.not_formed"), true);
            return;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            DeliveryTerminalMenu menu = new DeliveryTerminalMenu(0, serverPlayer.getInventory(), this);
            serverPlayer.openMenu(new net.minecraft.world.MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("block.servo_delivery.delivery_terminal");
                }

                @Override
                public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                        int containerId, net.minecraft.world.entity.player.Inventory inv,
                        Player p) {
                    return new DeliveryTerminalMenu(containerId, inv, DeliveryTerminalBlockEntity.this);
                }
            }, buf -> {
                DeliveryTerminalMenu temp = new DeliveryTerminalMenu(0, serverPlayer.getInventory(), this);
                temp.writeOpenData(buf);
            });
        }
    }

    public int getProgressPercent() {
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            DeliverySavedData data = DeliverySavedData.get(level.getServer());
            ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter());
            return chapter != null ? chapter.getProgressPercent(data.getDeliveredItems()) : 0;
        }
        return cachedProgress;
    }

    // ==================== Getters ====================

    public boolean isFormed() { return formed; }

    public int getCurrentChapter() {
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            return DeliverySavedData.get(level.getServer()).getCurrentChapter();
        }
        return cachedChapter;
    }

    public Map<String, Integer> getDeliveredItems() {
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            return DeliverySavedData.get(level.getServer()).getDeliveredItems();
        }
        return Map.of(); // client-side: empty (Menu uses ContainerData for sync)
    }

    // ==================== Server Tick ====================

    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                  DeliveryTerminalBlockEntity be) {
        // Periodic structure validation (every 2 seconds)
        if (level.getGameTime() % 40 == 0) {
            be.tryFormMultiblock();

            // Refresh cached values from SavedData for client sync
            if (be.formed) {
                int oldChapter = be.cachedChapter;
                int oldProgress = be.cachedProgress;
                be.refreshCache();
                if (be.cachedChapter != oldChapter || be.cachedProgress != oldProgress) {
                    be.syncToClient();
                }
            }
        }
    }

    // ==================== Save/Load ====================

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("Formed", formed);
        tag.putBoolean("CelebrationTriggered", celebrationTriggered);
        // Cache values are saved so clients get correct data on chunk load
        tag.putInt("CachedChapter", cachedChapter);
        tag.putInt("CachedProgress", cachedProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        formed = tag.getBoolean("Formed");
        celebrationTriggered = tag.getBoolean("CelebrationTriggered");
        cachedChapter = tag.getInt("CachedChapter");
        if (cachedChapter < 1) cachedChapter = 1;
        cachedProgress = tag.getInt("CachedProgress");
    }

    // ==================== Sync ====================

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        // Refresh cache before syncing to ensure fresh data
        if (level != null && !level.isClientSide()) {
            refreshCache();
        }
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
