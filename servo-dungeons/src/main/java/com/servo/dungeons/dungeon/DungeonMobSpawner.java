package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonTier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

/**
 * Handles spawning mobs when a dungeon room is activated.
 * Mob types and counts scale with the dungeon tier and room type.
 */
public class DungeonMobSpawner {

    // Mob pools by difficulty tier
    private static final EntityType<?>[] BASIC_MOBS = {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER
    };
    private static final EntityType<?>[] ADVANCED_MOBS = {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER,
            EntityType.CAVE_SPIDER, EntityType.STRAY, EntityType.HUSK
    };
    private static final EntityType<?>[] MASTER_MOBS = {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CAVE_SPIDER,
            EntityType.WITHER_SKELETON, EntityType.BLAZE, EntityType.PIGLIN_BRUTE
    };
    private static final EntityType<?>[] CORE_MOBS = {
            EntityType.WITHER_SKELETON, EntityType.BLAZE, EntityType.PIGLIN_BRUTE,
            EntityType.VINDICATOR, EntityType.EVOKER
    };

    private DungeonMobSpawner() {
        // Utility class
    }

    /**
     * Spawn mobs in a room based on dungeon tier.
     * Does not spawn mobs in ENTRANCE rooms.
     *
     * @param room          the room to spawn mobs in
     * @param tier          the dungeon tier (determines mob pool and count)
     * @param level         the dungeon dimension's ServerLevel
     * @param dungeonCenter the dungeon instance center (from OffsetAllocator)
     * @param random        random source for mob selection and positioning
     * @return the number of mobs successfully spawned
     */
    public static int spawnInRoom(RoomData room, DungeonTier tier, ServerLevel level,
                                  BlockPos dungeonCenter, RandomSource random) {
        // Don't spawn in ENTRANCE rooms
        if (room.getType() == RoomType.ENTRANCE) return 0;

        // Calculate mob count based on room type and tier
        int baseMobs = switch (room.getType()) {
            case DEAD_END -> 2 + tier.tier;      // 3-6 mobs
            case HALLWAY -> 1 + tier.tier;        // 2-5
            case CORNER -> 2 + tier.tier;         // 3-6
            case T_JUNCTION -> 3 + tier.tier;     // 4-7
            case CROSS -> 4 + tier.tier;          // 5-8
            case BOSS -> 6 + tier.tier * 2;       // 8-14
            default -> 2;
        };

        // Get mob pool for this tier
        EntityType<?>[] mobPool = switch (tier) {
            case BASIC -> BASIC_MOBS;
            case ADVANCED -> ADVANCED_MOBS;
            case MASTER -> MASTER_MOBS;
            case CORE -> CORE_MOBS;
        };

        BlockPos roomOrigin = room.getWorldPos(dungeonCenter);
        int spawned = 0;

        for (int i = 0; i < baseMobs; i++) {
            // Pick random mob type from pool
            EntityType<?> mobType = mobPool[random.nextInt(mobPool.length)];

            // Random position inside room interior (X 2-13, Z 2-13, Y 1)
            int spawnX = 2 + random.nextInt(12);
            int spawnZ = 2 + random.nextInt(12);
            BlockPos spawnPos = roomOrigin.offset(spawnX, 1, spawnZ);

            // Spawn the mob
            Entity entity = mobType.create(level);
            if (entity instanceof Mob mob) {
                mob.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                        random.nextFloat() * 360f, 0f);
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos),
                        MobSpawnType.EVENT, null);
                // Make mob persistent (don't despawn)
                mob.setPersistenceRequired();
                level.addFreshEntity(mob);
                spawned++;
            }
        }

        // TODO: Champion affixes integration (future - requires Champions Unofficial API)
        // For now, just spawn vanilla mobs

        return spawned;
    }
}
