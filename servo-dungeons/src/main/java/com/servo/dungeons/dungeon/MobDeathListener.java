package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
            ServoDungeons.LOGGER.debug("Room ({},{}) cleared!", gridX, gridZ);

            // Check if entire dungeon is complete
            if (tracker.isAllCleared()) {
                // Place exit portal in the center of the last cleared room
                DungeonLayout layout = instance.getLayout();
                if (layout != null) {
                    RoomData room = layout.getRoom(gridX, gridZ);
                    if (room != null) {
                        BlockPos roomOrigin = room.getWorldPos(instance.getCenter());
                        BlockPos portalPos = roomOrigin.offset(
                                RoomData.ROOM_SIZE / 2,  // center X (8)
                                1,                        // floor + 1
                                RoomData.ROOM_SIZE / 2   // center Z (8)
                        );
                        serverLevel.setBlock(portalPos,
                                DungeonRegistry.EXIT_PORTAL_BLOCK.get().defaultBlockState(), 3);

                        // Notify all players in this dungeon
                        for (UUID playerId : instance.getPlayerIds()) {
                            ServerPlayer p = serverLevel.getServer().getPlayerList().getPlayer(playerId);
                            if (p != null) {
                                p.sendSystemMessage(Component.translatable("message.servo_dungeons.dungeon_complete"));
                            }
                        }
                        ServoDungeons.LOGGER.info("Dungeon {} complete! Exit portal placed.", instance.getId());
                    }
                }
            }
        }
    }
}
