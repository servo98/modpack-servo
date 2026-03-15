package com.servo.delivery.block;

import com.servo.delivery.DeliveryRegistry;
import com.servo.delivery.ServoDelivery;
import com.servo.delivery.data.ChapterDelivery;
import com.servo.delivery.data.DeliveryDataLoader;
import com.servo.delivery.data.DeliverySavedData;
import com.servo.delivery.data.TeamHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.UUID;

/**
 * Core BlockEntity for the Delivery Terminal.
 * Handles: multiblock validation, delivery progress tracking, GeckoLib animations.
 *
 * Delivery progress is stored per-team via {@link DeliverySavedData},
 * so progress survives terminal destruction. The terminal tracks its
 * {@code ownerTeamId} to scope automation (hoppers/funnels) to the correct team.
 */
public class DeliveryTerminalBlockEntity extends net.minecraft.world.level.block.entity.BlockEntity
        implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /** Duration of celebration effects in ticks (5 seconds). */
    private static final int CELEBRATION_DURATION = 100;

    // === Per-terminal state ===
    private boolean formed = false;
    /** Countdown in ticks. >0 means celebration is active. Synced to client for beam rendering. */
    private int celebrationTicks = 0;
    /** Team that owns this terminal. Set when a player interacts. Used for automation context. */
    @Nullable
    private UUID ownerTeamId;

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

    // ==================== Team Resolution ====================

    /**
     * Resolves the team UUID for the given context.
     * If a player is present, resolves via FTB Teams (or player UUID fallback).
     * If no player (automation), uses the stored ownerTeamId.
     */
    @Nullable
    private UUID resolveTeamId(@Nullable Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            UUID teamId = TeamHelper.resolveTeamId(serverPlayer);
            // Update owner on any player interaction
            if (ownerTeamId == null || !ownerTeamId.equals(teamId)) {
                ownerTeamId = teamId;
                setChanged();
            }
            return teamId;
        }
        return ownerTeamId; // automation: use stored owner
    }

    /**
     * Sets the owner team from the player who placed or first interacted with the terminal.
     */
    public void setOwnerFromPlayer(ServerPlayer player) {
        UUID teamId = TeamHelper.resolveTeamId(player);
        if (ownerTeamId == null || !ownerTeamId.equals(teamId)) {
            ownerTeamId = teamId;
            setChanged();
        }
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

        refreshCache(ownerTeamId);
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
     * Checks if the terminal would accept this item (without consuming it).
     * Uses ownerTeamId for automation context.
     */
    public boolean canAcceptItem(ItemStack stack) {
        if (level == null || level.isClientSide() || !formed || ownerTeamId == null) return false;
        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter(ownerTeamId));
        if (chapter == null) return false;
        return chapter.findMatchingRequirement(stack, data.getDeliveredItems(ownerTeamId)) != null;
    }

    /**
     * Attempts to insert an item into the terminal.
     * Called from right-click or from DeliveryPort automation.
     * Does NOT auto-complete the chapter — only the LAUNCH button does that.
     *
     * @return true if the item was accepted and consumed
     */
    public boolean tryInsertItem(ItemStack stack, @Nullable Player player) {
        if (level == null || level.isClientSide() || !formed) return false;

        UUID teamId = resolveTeamId(player);
        if (teamId == null) {
            // Automation with no owner — reject
            return false;
        }

        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter(teamId));
        if (chapter == null) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("servo_delivery.terminal.no_chapter"), true);
            }
            return false;
        }

        // Check if this item matches any requirement
        String matchedReq = chapter.findMatchingRequirement(stack, data.getDeliveredItems(teamId));
        if (matchedReq == null) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("servo_delivery.terminal.not_needed"), true);
            }
            return false;
        }

        // Accept the item
        data.deliverItem(teamId, matchedReq);
        stack.shrink(1);

        // Update cached values for rendering (NO auto-complete)
        refreshCache(teamId);
        syncToClient();

        return true;
    }

    /**
     * Called when the player presses the LAUNCH button in the GUI.
     * Only completes the chapter if all requirements are met.
     */
    public void launchDelivery(ServerPlayer player) {
        if (level == null || level.isClientSide()) return;

        UUID teamId = TeamHelper.resolveTeamId(player);
        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter(teamId));
        if (chapter == null || !chapter.isComplete(data.getDeliveredItems(teamId))) return;

        onChapterComplete(data, teamId);
    }

    private void onChapterComplete(DeliverySavedData data, UUID teamId) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        int completedChapter = data.getCurrentChapter(teamId);

        // Fire event for servo_core to grant stages, trigger quests, etc.
        DeliveryCompleteEvent.fire(serverLevel, worldPosition, completedChapter, teamId);

        // Start celebration effects
        celebrationTicks = CELEBRATION_DURATION;
        spawnCelebrationParticles(serverLevel);
        playCelebrationSound(serverLevel);

        // Advance to next chapter (clears delivered items)
        data.advanceChapter(teamId);

        refreshCache(teamId);
        setChanged();
        syncToClient();
    }

    private void spawnCelebrationParticles(ServerLevel serverLevel) {
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 1.5;
        double z = worldPosition.getZ() + 0.5;

        // Totem-style rising particles (the "epic moment" burst)
        serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                x, y, z, 80, 0.6, 1.0, 0.6, 0.3);

        // Firework sparks for extra flair
        serverLevel.sendParticles(ParticleTypes.FIREWORK,
                x, y + 0.5, z, 40, 0.5, 0.8, 0.5, 0.2);

        // End rod floating particles (lingering glow)
        serverLevel.sendParticles(ParticleTypes.END_ROD,
                x, y, z, 20, 0.3, 2.0, 0.3, 0.05);
    }

    private void playCelebrationSound(ServerLevel serverLevel) {
        serverLevel.playSound(null, worldPosition,
                SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS,
                1.5f, 1.0f);
    }

    /**
     * Refreshes cached chapter/progress values from SavedData for a specific team.
     * Called after modifications and periodically from serverTick.
     */
    private void refreshCache(@Nullable UUID teamId) {
        if (level == null || level.isClientSide() || level.getServer() == null || teamId == null) return;
        DeliverySavedData data = DeliverySavedData.get(level.getServer());
        cachedChapter = data.getCurrentChapter(teamId);
        ChapterDelivery chapter = DeliveryDataLoader.getChapter(cachedChapter);
        cachedProgress = chapter != null ? chapter.getProgressPercent(data.getDeliveredItems(teamId)) : 0;
    }

    // ==================== Screen ====================

    public void openScreen(Player player) {
        if (!formed) {
            player.displayClientMessage(
                    Component.translatable("servo_delivery.terminal.not_formed"), true);
            return;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            UUID teamId = TeamHelper.resolveTeamId(serverPlayer);
            // Update owner on screen open
            if (ownerTeamId == null || !ownerTeamId.equals(teamId)) {
                ownerTeamId = teamId;
                setChanged();
            }

            serverPlayer.openMenu(new net.minecraft.world.MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("block.servo_delivery.delivery_terminal");
                }

                @Override
                public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                        int containerId, net.minecraft.world.entity.player.Inventory inv,
                        Player p) {
                    return new DeliveryTerminalMenu(containerId, inv, DeliveryTerminalBlockEntity.this, teamId);
                }
            }, buf -> {
                DeliveryTerminalMenu temp = new DeliveryTerminalMenu(0, serverPlayer.getInventory(), this, teamId);
                temp.writeOpenData(buf);
            });
        }
    }

    public int getProgressPercent(UUID teamId) {
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            DeliverySavedData data = DeliverySavedData.get(level.getServer());
            ChapterDelivery chapter = DeliveryDataLoader.getChapter(data.getCurrentChapter(teamId));
            return chapter != null ? chapter.getProgressPercent(data.getDeliveredItems(teamId)) : 0;
        }
        return cachedProgress;
    }

    public int getCurrentChapter(UUID teamId) {
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            return DeliverySavedData.get(level.getServer()).getCurrentChapter(teamId);
        }
        return cachedChapter;
    }

    public Map<String, Integer> getDeliveredItems(UUID teamId) {
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            return DeliverySavedData.get(level.getServer()).getDeliveredItems(teamId);
        }
        return Map.of();
    }

    // ==================== Getters ====================

    /** Client-safe: returns cached progress (synced from server via update tag). */
    public int getProgressPercent() { return cachedProgress; }

    /** Client-safe: returns cached chapter (synced from server via update tag). */
    public int getCurrentChapter() { return cachedChapter; }

    public boolean isFormed() { return formed; }

    public boolean isCelebrating() { return celebrationTicks > 0; }

    @Nullable
    public UUID getOwnerTeamId() { return ownerTeamId; }

    // ==================== Server Tick ====================

    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                  DeliveryTerminalBlockEntity be) {
        // Celebration countdown
        if (be.celebrationTicks > 0) {
            be.celebrationTicks--;

            // Ongoing particles during celebration (every 4 ticks)
            if (be.celebrationTicks % 4 == 0 && level instanceof ServerLevel serverLevel) {
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + 0.5;
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        x, y, z, 3, 0.2, 0.5, 0.2, 0.02);
            }

            if (be.celebrationTicks == 0) {
                be.setChanged();
                be.syncToClient();
            }
        }

        // Periodic structure validation (every 2 seconds)
        if (level.getGameTime() % 40 == 0) {
            be.tryFormMultiblock();

            // Refresh cached values from SavedData for client sync
            if (be.formed && be.ownerTeamId != null) {
                int oldChapter = be.cachedChapter;
                int oldProgress = be.cachedProgress;
                be.refreshCache(be.ownerTeamId);
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
        tag.putInt("CelebrationTicks", celebrationTicks);
        if (ownerTeamId != null) {
            tag.putUUID("OwnerTeamId", ownerTeamId);
        }
        // Cache values are saved so clients get correct data on chunk load
        tag.putInt("CachedChapter", cachedChapter);
        tag.putInt("CachedProgress", cachedProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        formed = tag.getBoolean("Formed");
        celebrationTicks = tag.getInt("CelebrationTicks");
        if (tag.hasUUID("OwnerTeamId")) {
            ownerTeamId = tag.getUUID("OwnerTeamId");
        }
        cachedChapter = tag.getInt("CachedChapter");
        if (cachedChapter < 1) cachedChapter = 1;
        cachedProgress = tag.getInt("CachedProgress");
    }

    // ==================== Sync ====================

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        // Refresh cache before syncing to ensure fresh data
        if (level != null && !level.isClientSide()) {
            refreshCache(ownerTeamId);
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
