package com.servo.dungeons.block;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.ServoDungeons;
import com.servo.dungeons.dungeon.DungeonManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * BlockEntity for the Dungeon Pedestal.
 * Stores ritual state and manages the connection to a dungeon instance.
 */
public class DungeonPedestalBlockEntity extends BlockEntity {

    /** Duration of the ritual charging phase in ticks (5 seconds). */
    private static final int RITUAL_DURATION = 100;

    @Nullable
    private DungeonTier activeTier;
    @Nullable
    private UUID dungeonId;
    private int ritualTicks;
    private boolean isActive;

    /** Player who started the ritual (used for teleportation when ritual completes). */
    @Nullable
    private UUID ritualStarterId;

    public DungeonPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(DungeonRegistry.PEDESTAL_BE.get(), pos, state);
    }

    // ==================== Ritual Logic ====================

    /**
     * Start the ritual charging phase.
     */
    public void startRitual(DungeonTier tier, ServerPlayer leader) {
        this.activeTier = tier;
        this.ritualTicks = 0;
        this.ritualStarterId = leader.getUUID();

        // Set pedestal state to CHARGING
        if (level != null) {
            level.setBlock(worldPosition, getBlockState()
                    .setValue(DungeonPedestalBlock.STATE, DungeonPedestalBlock.PedestalState.CHARGING), 3);
            setRunesActive(true);
        }
        setChanged();
    }

    /**
     * Called every server tick while ritual is in progress.
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state, DungeonPedestalBlockEntity be) {
        if (be.isRitualInProgress()) {
            be.ritualTicks++;

            if (be.ritualTicks >= RITUAL_DURATION) {
                be.completeRitual();
            }
        }
    }

    /**
     * Complete the ritual — create dungeon and teleport players.
     */
    private void completeRitual() {
        if (level == null || level.isClientSide() || activeTier == null || ritualStarterId == null) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        ServerPlayer leader = serverLevel.getServer().getPlayerList().getPlayer(ritualStarterId);

        if (leader == null) {
            ServoDungeons.LOGGER.warn("Ritual leader {} not found, aborting ritual", ritualStarterId);
            cancelRitual();
            return;
        }

        DungeonManager manager = DungeonManager.getInstance();
        if (manager == null) {
            cancelRitual();
            return;
        }

        // Start the dungeon
        UUID id = manager.startDungeon(serverLevel, worldPosition, leader, activeTier);
        if (id != null) {
            this.dungeonId = id;
            this.isActive = true;

            // Set pedestal state to ACTIVE
            level.setBlock(worldPosition, getBlockState()
                    .setValue(DungeonPedestalBlock.STATE, DungeonPedestalBlock.PedestalState.ACTIVE), 3);

            // Place beam blocks above pedestal for re-entry
            placeBeam();

            leader.sendSystemMessage(Component.translatable("message.servo_dungeons.dungeon_entered"));
        } else {
            cancelRitual();
        }

        // Reset ritual state
        this.ritualTicks = 0;
        this.ritualStarterId = null;
        setChanged();
    }

    /**
     * Cancel a ritual in progress and reset state.
     */
    private void cancelRitual() {
        this.ritualTicks = 0;
        this.ritualStarterId = null;
        this.activeTier = null;

        if (level != null) {
            level.setBlock(worldPosition, getBlockState()
                    .setValue(DungeonPedestalBlock.STATE, DungeonPedestalBlock.PedestalState.INACTIVE), 3);
            setRunesActive(false);
        }
        setChanged();
    }

    /**
     * Reset the pedestal when a dungeon ends.
     */
    public void resetAfterDungeon() {
        this.isActive = false;
        this.dungeonId = null;
        this.activeTier = null;

        if (level != null) {
            removeBeam();
            level.setBlock(worldPosition, getBlockState()
                    .setValue(DungeonPedestalBlock.STATE, DungeonPedestalBlock.PedestalState.INACTIVE), 3);
            setRunesActive(false);
        }
        setChanged();
    }

    // ==================== Multiblock Validation ====================

    /**
     * Check that 4 adjacent blocks (N/S/E/W) are DungeonRuneBlocks.
     */
    public boolean validateMultiblock() {
        if (level == null) return false;

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos runePos = worldPosition.relative(dir);
            if (!(level.getBlockState(runePos).getBlock() instanceof DungeonRuneBlock)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the ACTIVE blockstate on all 4 adjacent rune blocks.
     */
    public void setRunesActive(boolean active) {
        if (level == null) return;

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos runePos = worldPosition.relative(dir);
            BlockState runeState = level.getBlockState(runePos);
            if (runeState.getBlock() instanceof DungeonRuneBlock) {
                level.setBlock(runePos, runeState.setValue(DungeonRuneBlock.ACTIVE, active), 3);
            }
        }
    }

    // ==================== Beam Management ====================

    /**
     * Place beam blocks (Y+1 to Y+4) above the pedestal for dungeon entry.
     */
    private void placeBeam() {
        if (level == null) return;
        for (int y = 1; y <= 4; y++) {
            BlockPos beamPos = worldPosition.above(y);
            level.setBlock(beamPos, DungeonRegistry.BEAM_BLOCK.get().defaultBlockState(), 3);
            BlockEntity be = level.getBlockEntity(beamPos);
            if (be instanceof DungeonBeamBlockEntity beamBE) {
                beamBE.setAltarPos(worldPosition);
            }
        }
    }

    /**
     * Remove beam blocks above the pedestal when the dungeon ends.
     */
    private void removeBeam() {
        if (level == null) return;
        for (int y = 1; y <= 4; y++) {
            BlockPos beamPos = worldPosition.above(y);
            if (level.getBlockState(beamPos).getBlock() instanceof DungeonBeamBlock) {
                level.setBlock(beamPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    // ==================== State Queries ====================

    public boolean isActive() {
        return isActive;
    }

    public boolean isRitualInProgress() {
        return ritualTicks > 0 && ritualStarterId != null;
    }

    @Nullable
    public DungeonTier getActiveTier() {
        return activeTier;
    }

    @Nullable
    public UUID getDungeonId() {
        return dungeonId;
    }

    // ==================== Serialization ====================

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("IsActive", isActive);
        tag.putInt("RitualTicks", ritualTicks);

        if (activeTier != null) {
            tag.putString("ActiveTier", activeTier.name());
        }
        if (dungeonId != null) {
            tag.putUUID("DungeonId", dungeonId);
        }
        if (ritualStarterId != null) {
            tag.putUUID("RitualStarter", ritualStarterId);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("IsActive");
        ritualTicks = tag.getInt("RitualTicks");

        if (tag.contains("ActiveTier")) {
            try {
                activeTier = DungeonTier.valueOf(tag.getString("ActiveTier"));
            } catch (IllegalArgumentException e) {
                activeTier = null;
            }
        }
        if (tag.hasUUID("DungeonId")) {
            dungeonId = tag.getUUID("DungeonId");
        }
        if (tag.hasUUID("RitualStarter")) {
            ritualStarterId = tag.getUUID("RitualStarter");
        }
    }
}
