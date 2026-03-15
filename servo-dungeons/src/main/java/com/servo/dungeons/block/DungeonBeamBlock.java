package com.servo.dungeons.block;

import com.mojang.serialization.MapCodec;
import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.ServoDungeons;
import com.servo.dungeons.dungeon.DungeonManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Invisible beam block placed above the Dungeon Pedestal when a dungeon is active.
 * Players who walk into the beam are teleported to the dungeon.
 * No collision, no visual model (particles added later), light level 10, indestructible.
 */
public class DungeonBeamBlock extends BaseEntityBlock {

    public static final MapCodec<DungeonBeamBlock> CODEC = simpleCodec(DungeonBeamBlock::new);

    /** Cooldown map: player UUID → last teleport game time. Prevents re-teleport loops. */
    private static final Map<UUID, Long> TELEPORT_COOLDOWNS = new ConcurrentHashMap<>();

    /** Cooldown duration in ticks (2 seconds). */
    private static final int COOLDOWN_TICKS = 40;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public DungeonBeamBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DungeonBeamBlockEntity(pos, state);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;

        if (entity instanceof ServerPlayer serverPlayer) {
            // Check cooldown
            long currentTime = level.getGameTime();
            UUID playerId = serverPlayer.getUUID();
            Long lastTeleport = TELEPORT_COOLDOWNS.get(playerId);
            if (lastTeleport != null && currentTime - lastTeleport < COOLDOWN_TICKS) {
                return;
            }

            // Find the pedestal via stored altar position
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof DungeonBeamBlockEntity beamBE)) return;

            BlockPos altarPos = beamBE.getAltarPos();
            if (altarPos == null) return;

            BlockEntity altarBE = level.getBlockEntity(altarPos);
            if (!(altarBE instanceof DungeonPedestalBlockEntity pedestal)) return;

            UUID dungeonId = pedestal.getDungeonId();
            if (dungeonId == null) return;

            DungeonManager manager = DungeonManager.getInstance();
            if (manager == null || !manager.isActive(dungeonId)) return;

            // Record cooldown and teleport
            TELEPORT_COOLDOWNS.put(playerId, currentTime);
            manager.reenterDungeon(serverPlayer, dungeonId);
            serverPlayer.sendSystemMessage(
                    Component.translatable("message.servo_dungeons.dungeon_entered"));
        }
    }
}
