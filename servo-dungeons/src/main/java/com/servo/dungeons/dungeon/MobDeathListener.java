package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.UUID;

/**
 * Listens for mob death events in the dungeon dimension.
 * Decrements the living mob count for the room where the mob died,
 * and marks the room as CLEARED when all mobs are dead.
 */
public class MobDeathListener {

    private MobDeathListener() {
        // Utility class
    }

    /**
     * Called on {@link LivingDeathEvent}. If the dying entity is in a dungeon room,
     * decrements the living mob count. If count reaches 0, marks room as CLEARED.
     *
     * @param event the living death event
     */
    public static void onMobDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity().level() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(DungeonRegistry.DUNGEON_LEVEL_KEY)) return;
        // Skip player deaths
        if (event.getEntity() instanceof Player) return;

        DungeonManager manager = DungeonManager.getInstance();
        if (manager == null) return;

        BlockPos deathPos = event.getEntity().blockPosition();
        DungeonInstance instance = manager.getDungeonAtPosition(deathPos);
        if (instance == null) return;

        RoomTracker tracker = instance.getRoomTracker();
        if (tracker == null) return;

        BlockPos center = instance.getCenter();
        int gridX = Math.floorDiv(deathPos.getX() - center.getX(), RoomData.ROOM_SIZE);
        int gridZ = Math.floorDiv(deathPos.getZ() - center.getZ(), RoomData.ROOM_SIZE);

        tracker.onMobKilled(gridX, gridZ);

        if (tracker.isCleared(gridX, gridZ)) {
            tracker.markCleared(gridX, gridZ);

            // Remove barrier blocks from the cleared room's doorways
            DungeonLayout layout = instance.getLayout();
            if (layout != null) {
                RoomData clearedRoom = layout.getRoom(gridX, gridZ);
                if (clearedRoom != null) {
                    DungeonBarrierManager.removeBarriers(clearedRoom, serverLevel, instance.getCenter());
                }
            }

            // Notify players that this room is cleared
            for (UUID playerId : instance.getPlayerIds()) {
                ServerPlayer p = serverLevel.getServer().getPlayerList().getPlayer(playerId);
                if (p != null) {
                    p.sendSystemMessage(Component.translatable("message.servo_dungeons.room_cleared"));
                }
            }

            ServoDungeons.LOGGER.debug("Room ({},{}) cleared! Barriers removed.", gridX, gridZ);

            // Check if entire dungeon is complete
            if (tracker.isAllCleared()) {
                onDungeonComplete(instance, serverLevel);
            }
        }
    }

    /**
     * Called when all rooms in a dungeon have been cleared.
     * Places an exit portal in the appropriate room and notifies players.
     *
     * <p>Exit portal placement priority:
     * <ol>
     *   <li>BOSS room (if present)</li>
     *   <li>Last DEAD_END room in the layout</li>
     *   <li>Fallback: entrance room</li>
     * </ol>
     */
    private static void onDungeonComplete(DungeonInstance instance, ServerLevel serverLevel) {
        DungeonLayout layout = instance.getLayout();
        if (layout == null) return;

        // Determine where to place the exit portal
        RoomData portalRoom = layout.getBossRoom();

        // If no boss room, find the last dead-end
        if (portalRoom == null) {
            for (RoomData room : layout.getRooms()) {
                if (room.getType() == RoomType.DEAD_END) {
                    portalRoom = room;
                    // Don't break — we want the last one found
                }
            }
        }

        // Fallback to entrance
        if (portalRoom == null) {
            portalRoom = layout.getEntrance();
        }

        // Place exit portal in the center of the chosen room
        BlockPos roomOrigin = portalRoom.getWorldPos(instance.getCenter());
        BlockPos portalPos = roomOrigin.offset(
                RoomData.ROOM_SIZE / 2,  // center X (8)
                1,                        // floor + 1
                RoomData.ROOM_SIZE / 2   // center Z (8)
        );
        serverLevel.setBlock(portalPos,
                DungeonRegistry.EXIT_PORTAL_BLOCK.get().defaultBlockState(), 3);

        // Also place a 3x3 portal platform for visibility
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue; // center already placed
                BlockPos adjacentPos = portalPos.offset(dx, 0, dz);
                serverLevel.setBlock(adjacentPos,
                        DungeonRegistry.EXIT_PORTAL_BLOCK.get().defaultBlockState(), 3);
            }
        }

        // Notify all players with sound and message
        for (UUID playerId : instance.getPlayerIds()) {
            ServerPlayer p = serverLevel.getServer().getPlayerList().getPlayer(playerId);
            if (p != null) {
                p.sendSystemMessage(Component.translatable("message.servo_dungeons.dungeon_complete"));
                // Play a triumphant sound
                serverLevel.playSound(null, p.blockPosition(),
                        SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS,
                        1.0f, 1.0f);
            }
        }

        ServoDungeons.LOGGER.info("Dungeon {} complete! Exit portal placed at {}.",
                instance.getId(), portalPos);
    }
}
