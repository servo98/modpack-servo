package com.servo.dungeons.dungeon;

import com.servo.dungeons.DungeonRegistry;
import com.servo.dungeons.DungeonTier;
import com.servo.dungeons.ServoDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

/**
 * Procedural dungeon generator. Produces a {@link DungeonLayout} using a random-walk
 * algorithm and can place the resulting rooms as blocks in the dungeon dimension.
 */
public class DungeonGenerator {

    /** The four horizontal directions used for room connections. */
    private static final Direction[] HORIZONTALS = {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
    };

    // ==================== Layout Generation ====================

    /**
     * Generate a dungeon layout for the given tier.
     * Deterministic for a given RandomSource seed.
     *
     * @param tier   the dungeon tier (determines room count range)
     * @param random the random source
     * @return a fully populated DungeonLayout
     */
    public static DungeonLayout generate(DungeonTier tier, RandomSource random) {
        int roomCount = tier.minRooms + random.nextInt(tier.maxRooms - tier.minRooms + 1);

        // Grid positions that are part of the dungeon
        Set<Long> occupied = new HashSet<>();
        // Ordered list of grid positions in the order they were added
        List<long[]> orderedCells = new ArrayList<>();

        // Start at (0, 0)
        long startKey = DungeonLayout.packGridPos(0, 0);
        occupied.add(startKey);
        orderedCells.add(new long[]{0, 0});

        // Frontier: cells that have at least one empty neighbor
        List<long[]> frontier = new ArrayList<>();
        frontier.add(new long[]{0, 0});

        // Random walk to fill rooms
        while (orderedCells.size() < roomCount && !frontier.isEmpty()) {
            // Pick a random frontier cell
            int frontierIdx = random.nextInt(frontier.size());
            long[] cell = frontier.get(frontierIdx);
            int cx = (int) cell[0];
            int cz = (int) cell[1];

            // Collect empty neighbors
            List<long[]> emptyNeighbors = new ArrayList<>();
            for (Direction dir : HORIZONTALS) {
                int nx = cx + dir.getStepX();
                int nz = cz + dir.getStepZ();
                if (!occupied.contains(DungeonLayout.packGridPos(nx, nz))) {
                    emptyNeighbors.add(new long[]{nx, nz});
                }
            }

            if (emptyNeighbors.isEmpty()) {
                // This cell has no empty neighbors; remove from frontier
                frontier.remove(frontierIdx);
                continue;
            }

            // Pick a random empty neighbor and add it
            long[] chosen = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
            int nx = (int) chosen[0];
            int nz = (int) chosen[1];
            long newKey = DungeonLayout.packGridPos(nx, nz);
            occupied.add(newKey);
            orderedCells.add(new long[]{nx, nz});

            // The new cell is a potential frontier cell
            frontier.add(new long[]{nx, nz});

            // Re-check if the parent cell still has empty neighbors
            // (optimization: don't remove now, will be caught on next pick if empty)
        }

        // Find the farthest room from entrance using BFS
        long[] farthestCell = findFarthestCell(orderedCells, occupied);

        // Determine connections and room types
        Map<Long, RoomData> rooms = new HashMap<>();
        RoomData entrance = null;
        RoomData bossRoom = null;

        boolean isCoreT = (tier == DungeonTier.CORE);
        long farthestKey = DungeonLayout.packGridPos((int) farthestCell[0], (int) farthestCell[1]);

        for (long[] cell : orderedCells) {
            int gx = (int) cell[0];
            int gz = (int) cell[1];
            long key = DungeonLayout.packGridPos(gx, gz);

            // Determine connections: which neighbors exist?
            EnumSet<Direction> connections = EnumSet.noneOf(Direction.class);
            for (Direction dir : HORIZONTALS) {
                int adjX = gx + dir.getStepX();
                int adjZ = gz + dir.getStepZ();
                if (occupied.contains(DungeonLayout.packGridPos(adjX, adjZ))) {
                    connections.add(dir);
                }
            }

            // Determine room type
            RoomType type;
            if (gx == 0 && gz == 0) {
                type = RoomType.ENTRANCE;
            } else if (isCoreT && key == farthestKey) {
                type = RoomType.BOSS;
            } else {
                type = classifyRoom(connections);
            }

            RoomData room = new RoomData(gx, gz, type, connections);
            rooms.put(key, room);

            if (type == RoomType.ENTRANCE) {
                entrance = room;
            }
            if (type == RoomType.BOSS) {
                bossRoom = room;
            }
        }

        ServoDungeons.LOGGER.info("Generated {} layout: {} rooms (requested {})",
                tier.name, rooms.size(), roomCount);

        return new DungeonLayout(rooms, entrance, bossRoom);
    }

    /**
     * BFS from (0,0) to find the cell farthest from the entrance.
     */
    private static long[] findFarthestCell(List<long[]> cells, Set<Long> occupied) {
        // Build adjacency via the occupied set
        Map<Long, long[]> cellMap = new HashMap<>();
        for (long[] c : cells) {
            cellMap.put(DungeonLayout.packGridPos((int) c[0], (int) c[1]), c);
        }

        // BFS
        Queue<Long> queue = new ArrayDeque<>();
        Set<Long> visited = new HashSet<>();
        long startKey = DungeonLayout.packGridPos(0, 0);
        queue.add(startKey);
        visited.add(startKey);

        long[] farthest = cells.get(0); // default to entrance

        while (!queue.isEmpty()) {
            Long current = queue.poll();
            int cx = DungeonLayout.unpackX(current);
            int cz = DungeonLayout.unpackZ(current);
            farthest = cellMap.get(current);

            for (Direction dir : HORIZONTALS) {
                int nx = cx + dir.getStepX();
                int nz = cz + dir.getStepZ();
                long neighborKey = DungeonLayout.packGridPos(nx, nz);
                if (occupied.contains(neighborKey) && !visited.contains(neighborKey)) {
                    visited.add(neighborKey);
                    queue.add(neighborKey);
                }
            }
        }

        return farthest;
    }

    /**
     * Classify a room based on its connections.
     */
    private static RoomType classifyRoom(EnumSet<Direction> connections) {
        int count = connections.size();
        return switch (count) {
            case 1 -> RoomType.DEAD_END;
            case 2 -> {
                // Check if connections are in a straight line (N-S or E-W)
                if ((connections.contains(Direction.NORTH) && connections.contains(Direction.SOUTH))
                        || (connections.contains(Direction.EAST) && connections.contains(Direction.WEST))) {
                    yield RoomType.HALLWAY;
                }
                yield RoomType.CORNER;
            }
            case 3 -> RoomType.T_JUNCTION;
            case 4 -> RoomType.CROSS;
            default -> RoomType.DEAD_END; // should not happen
        };
    }

    // ==================== World Placement ====================

    /**
     * Place the generated layout as actual blocks in the dungeon dimension.
     *
     * @param layout       the generated dungeon layout
     * @param dungeonLevel the dungeon dimension's ServerLevel
     * @param center       the dungeon instance center (from OffsetAllocator)
     */
    public static void placeInWorld(DungeonLayout layout, ServerLevel dungeonLevel, BlockPos center) {
        for (RoomData room : layout.getRooms()) {
            placeRoom(room, dungeonLevel, center);
        }
        ServoDungeons.LOGGER.debug("Placed {} rooms in world at center {}",
                layout.getRoomCount(), center);
    }

    /**
     * Place a single room as blocks in the world.
     * Creates stone brick walls, floor, ceiling with door openings where connections exist.
     *
     * <p>Room dimensions: 16x16x10 (ROOM_SIZE x ROOM_SIZE x ROOM_HEIGHT)
     * <ul>
     *   <li>Y=0: Floor (full 16x16)</li>
     *   <li>Y=1 to Y=8: Walls on edges, air interior (14x14)</li>
     *   <li>Y=9: Ceiling (full 16x16)</li>
     * </ul>
     */
    private static void placeRoom(RoomData room, ServerLevel dungeonLevel, BlockPos center) {
        BlockPos origin = room.getWorldPos(center);
        int size = RoomData.ROOM_SIZE;   // 16
        int height = RoomData.ROOM_HEIGHT; // 10

        BlockState stoneBricks = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState glowstone = Blocks.GLOWSTONE.defaultBlockState();

        // Choose floor material based on room type
        BlockState floorState = switch (room.getType()) {
            case BOSS -> Blocks.DEEPSLATE_BRICKS.defaultBlockState();
            case DEAD_END -> stoneBricks; // mossy accents added separately
            default -> stoneBricks;
        };

        // === Step 1: Build the full shell (floor, walls, ceiling) ===
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                BlockPos floorPos = origin.offset(x, 0, z);
                // Floor
                dungeonLevel.setBlock(floorPos, floorState, 3);
                // Ceiling
                dungeonLevel.setBlock(origin.offset(x, height - 1, z), stoneBricks, 3);

                // Walls and interior for Y=1 to Y=8
                for (int y = 1; y < height - 1; y++) {
                    BlockPos pos = origin.offset(x, y, z);
                    boolean isWall = (x == 0 || x == size - 1 || z == 0 || z == size - 1);
                    dungeonLevel.setBlock(pos, isWall ? stoneBricks : air, 3);
                }
            }
        }

        // === Step 2: Carve door openings for each connection ===
        for (Direction dir : room.getConnections()) {
            carveDoor(origin, dir, dungeonLevel, size);
        }

        // === Step 3: Lighting — glowstone at ceiling corners ===
        // 4 corners, 2 blocks from each wall, at Y = height - 2 (under ceiling)
        int lightOffset = 2;
        int lightY = height - 2; // Y=8
        dungeonLevel.setBlock(origin.offset(lightOffset, lightY, lightOffset), glowstone, 3);
        dungeonLevel.setBlock(origin.offset(size - 1 - lightOffset, lightY, lightOffset), glowstone, 3);
        dungeonLevel.setBlock(origin.offset(lightOffset, lightY, size - 1 - lightOffset), glowstone, 3);
        dungeonLevel.setBlock(origin.offset(size - 1 - lightOffset, lightY, size - 1 - lightOffset), glowstone, 3);

        // === Step 4: Room-type specific extras ===
        switch (room.getType()) {
            case ENTRANCE -> placeEntranceExtras(room, origin, dungeonLevel);
            case DEAD_END -> placeDeadEndExtras(origin, dungeonLevel);
            case BOSS -> placeBossExtras(origin, dungeonLevel);
            default -> { /* no extras */ }
        }
    }

    /**
     * Carve a 4-wide, 4-tall door opening centered on the given wall.
     * Door X/Z positions: 6, 7, 8, 9 (centered in 16-block wall)
     * Door Y positions: 1, 2, 3, 4
     */
    private static void carveDoor(BlockPos origin, Direction dir, ServerLevel level, int size) {
        BlockState air = Blocks.AIR.defaultBlockState();

        // Door center offsets in the 16-wide room: positions 6,7,8,9
        int doorStart = 6;
        int doorEnd = 9;
        int doorMinY = 1;
        int doorMaxY = 4;

        for (int i = doorStart; i <= doorEnd; i++) {
            for (int y = doorMinY; y <= doorMaxY; y++) {
                BlockPos pos = switch (dir) {
                    case NORTH -> origin.offset(i, y, 0);         // north wall at z=0
                    case SOUTH -> origin.offset(i, y, size - 1);  // south wall at z=15
                    case WEST -> origin.offset(0, y, i);          // west wall at x=0
                    case EAST -> origin.offset(size - 1, y, i);   // east wall at x=15
                    default -> origin; // should never happen (UP/DOWN)
                };
                level.setBlock(pos, air, 3);
            }
        }
    }

    /**
     * Place entrance room extras: Exit Portal block near the wall opposite the first connection.
     */
    private static void placeEntranceExtras(RoomData room, BlockPos origin, ServerLevel level) {
        int size = RoomData.ROOM_SIZE;
        BlockState portalState = DungeonRegistry.EXIT_PORTAL_BLOCK.get().defaultBlockState();

        // Find the first connection direction
        Direction firstConnection = room.getConnections().iterator().next();
        // Place portal opposite the connection (so players see it when they enter)
        Direction portalSide = firstConnection.getOpposite();

        // Position: 1 block inside from the wall on the portal side, centered (X or Z = 7 or 8)
        int centerCoord = size / 2; // 8
        BlockPos portalPos = switch (portalSide) {
            case NORTH -> origin.offset(centerCoord, 1, 1);          // 1 block from north wall
            case SOUTH -> origin.offset(centerCoord, 1, size - 2);   // 1 block from south wall
            case WEST -> origin.offset(1, 1, centerCoord);           // 1 block from west wall
            case EAST -> origin.offset(size - 2, 1, centerCoord);    // 1 block from east wall
            default -> origin.offset(centerCoord, 1, 1);
        };

        level.setBlock(portalPos, portalState, 3);
    }

    /**
     * Place mossy stone brick accents on the floor of dead-end rooms for visual variety.
     */
    private static void placeDeadEndExtras(BlockPos origin, ServerLevel level) {
        BlockState mossy = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        // Scatter some mossy bricks on the interior floor
        // Place at fixed positions for determinism (corners and center of interior)
        int[] offsets = {3, 5, 7, 10, 12};
        for (int x : offsets) {
            for (int z : offsets) {
                // Only place ~40% of positions for a scattered look
                if ((x + z) % 3 == 0) {
                    level.setBlock(origin.offset(x, 0, z), mossy, 3);
                }
            }
        }
    }

    /**
     * Place boss room extras: for now, just visual markers.
     * The deepslate brick floor is already handled in placeRoom().
     */
    private static void placeBossExtras(BlockPos origin, ServerLevel level) {
        // Place extra glowstone along the walls for dramatic lighting
        int size = RoomData.ROOM_SIZE;
        BlockState glowstone = Blocks.GLOWSTONE.defaultBlockState();

        // Mid-wall glowstone at Y=4 on all 4 walls
        int midWallY = 4;
        int mid = size / 2; // 8
        level.setBlock(origin.offset(mid, midWallY, 0), glowstone, 3);       // north
        level.setBlock(origin.offset(mid, midWallY, size - 1), glowstone, 3); // south
        level.setBlock(origin.offset(0, midWallY, mid), glowstone, 3);       // west
        level.setBlock(origin.offset(size - 1, midWallY, mid), glowstone, 3); // east
    }

    // ==================== Cleanup ====================

    /**
     * Clear all blocks placed by a dungeon layout. Sets every block in every room's
     * 16x16x10 volume to air.
     *
     * @param layout       the layout to clear
     * @param dungeonLevel the dungeon dimension
     * @param center       the dungeon instance center
     */
    public static void clearFromWorld(DungeonLayout layout, ServerLevel dungeonLevel, BlockPos center) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (RoomData room : layout.getRooms()) {
            BlockPos origin = room.getWorldPos(center);
            for (int x = 0; x < RoomData.ROOM_SIZE; x++) {
                for (int z = 0; z < RoomData.ROOM_SIZE; z++) {
                    for (int y = 0; y < RoomData.ROOM_HEIGHT; y++) {
                        dungeonLevel.setBlock(origin.offset(x, y, z), air, 3);
                    }
                }
            }
        }
        ServoDungeons.LOGGER.debug("Cleared {} rooms from world at center {}",
                layout.getRoomCount(), center);
    }
}
