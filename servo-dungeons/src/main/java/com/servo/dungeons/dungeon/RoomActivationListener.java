package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Server-side listener that checks every tick whether a player has entered
 * a LOCKED room, triggering room activation and mob spawning.
 */
public class RoomActivationListener {

    private RoomActivationListener() {
        // Utility class
    }

    /**
     * Called every server tick for levels in the dungeon dimension.
     * Checks if any player has entered a LOCKED room, and if so,
     * activates the room and spawns mobs.
     *
     * @param level the ServerLevel being ticked (must be the dungeon dimension)
     */
    public static void onServerTick(ServerLevel level) {
        if (!level.dimension().equals(DungeonRegistry.DUNGEON_LEVEL_KEY)) return;

        DungeonManager manager = DungeonManager.getInstance();
        if (manager == null || !manager.hasActiveInstances()) return;

        for (ServerPlayer player : level.players()) {
            // Find which dungeon instance this player is in
            DungeonInstance instance = manager.getDungeonForPlayer(player.getUUID());
            if (instance == null || instance.getLayout() == null) continue;

            RoomTracker tracker = instance.getRoomTracker();
            if (tracker == null) continue;

            // Determine which grid cell the player is in
            BlockPos playerPos = player.blockPosition();
            BlockPos center = instance.getCenter();
            int gridX = Math.floorDiv(playerPos.getX() - center.getX(), RoomData.ROOM_SIZE);
            int gridZ = Math.floorDiv(playerPos.getZ() - center.getZ(), RoomData.ROOM_SIZE);

            RoomState state = tracker.getState(gridX, gridZ);
            if (state == RoomState.LOCKED) {
                // Activate room: spawn mobs
                DungeonLayout layout = instance.getLayout();
                RoomData room = layout.getRoom(gridX, gridZ);
                if (room != null) {
                    int spawned = DungeonMobSpawner.spawnInRoom(
                            room, instance.getTier(), level, instance.getCenter(), level.getRandom());
                    tracker.activateRoom(gridX, gridZ);
                    tracker.setLivingMobs(gridX, gridZ, spawned);
                    ServoDungeons.LOGGER.debug("Room ({},{}) activated: {} mobs spawned",
                            gridX, gridZ, spawned);
                }
            }
        }
    }
}
