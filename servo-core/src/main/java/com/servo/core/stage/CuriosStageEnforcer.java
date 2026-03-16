package com.servo.core.stage;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Supplements ProgressiveStages by enforcing item restrictions in Curios API slots.
 * ProgressiveStages only scans vanilla inventory; this class handles Curios slots.
 *
 * Architecture: This class does NOT import Curios API classes directly.
 * Curios-specific code is in {@link CuriosSlotScanner}, loaded lazily only when
 * the Curios mod is confirmed present.
 */
public class CuriosStageEnforcer {

    private static final Logger LOGGER = LoggerFactory.getLogger("ServoCore/CuriosEnforcer");
    private static final int CHECK_INTERVAL = 20; // ticks (1 second)

    // Item restrictions parsed from ProgressiveStages TOML configs
    // Key: item registry ID (e.g. "jewelry:ruby_ring"), Value: required stage (e.g. "servo_ch3")
    static final Map<String, String> ITEM_TO_STAGE = new HashMap<>();
    // Key: mod ID (e.g. "jewelry"), Value: required stage (e.g. "servo_ch2")
    static final Map<String, String> MOD_TO_STAGE = new HashMap<>();
    // Items explicitly whitelisted (available before their mod's stage)
    static final Set<String> WHITELISTED_ITEMS = new HashSet<>();

    // Stage ordering for comparisons (higher index = later chapter)
    private static final List<String> STAGE_ORDER = List.of(
            "servo_ch1", "servo_ch2", "servo_ch3", "servo_ch4",
            "servo_ch5", "servo_ch6", "servo_ch7", "servo_ch8"
    );

    private static boolean initialized = false;

    /**
     * Call from mod constructor after verifying both curios and progressivestages are loaded.
     */
    public static void init() {
        if (!ModList.get().isLoaded("curios") || !ModList.get().isLoaded("progressivestages")) {
            LOGGER.info("Curios or ProgressiveStages not loaded, enforcer disabled");
            return;
        }

        loadStageConfigs();

        if (ITEM_TO_STAGE.isEmpty() && MOD_TO_STAGE.isEmpty()) {
            LOGGER.warn("No stage restrictions found in configs, enforcer disabled");
            return;
        }

        NeoForge.EVENT_BUS.addListener(CuriosStageEnforcer::onPlayerTick);
        initialized = true;
        LOGGER.info("CuriosStageEnforcer active: {} item locks, {} mod locks, {} whitelisted",
                ITEM_TO_STAGE.size(), MOD_TO_STAGE.size(), WHITELISTED_ITEMS.size());
    }

    /**
     * Parse ProgressiveStages TOML config files to build the item→stage restriction map.
     */
    private static void loadStageConfigs() {
        Path configDir = FMLPaths.CONFIGDIR.get().resolve("ProgressiveStages");
        if (!Files.isDirectory(configDir)) {
            LOGGER.warn("ProgressiveStages config directory not found: {}", configDir);
            return;
        }

        // Process configs in chapter order (ch1→ch8) so later chapters override earlier
        for (String stage : STAGE_ORDER) {
            Path configFile = configDir.resolve(stage + ".toml");
            if (!Files.exists(configFile)) continue;

            try (FileConfig config = FileConfig.of(configFile)) {
                config.load();

                // Read stage ID (verify it matches filename)
                String stageId = config.getOrElse("stage.id", stage);

                // Read mod locks
                List<String> mods = config.getOrElse("locks.mods", Collections.emptyList());
                for (String modId : mods) {
                    MOD_TO_STAGE.put(modId, stageId);
                }

                // Read item locks (specific items override mod locks)
                List<String> items = config.getOrElse("locks.items", Collections.emptyList());
                for (String itemId : items) {
                    ITEM_TO_STAGE.put(itemId, stageId);
                }

                // Read block locks (also count as item restrictions)
                List<String> blocks = config.getOrElse("locks.blocks", Collections.emptyList());
                for (String blockId : blocks) {
                    ITEM_TO_STAGE.putIfAbsent(blockId, stageId);
                }

                // Read whitelisted items
                List<String> unlocked = config.getOrElse("locks.unlocked_items", Collections.emptyList());
                WHITELISTED_ITEMS.addAll(unlocked);

            } catch (Exception e) {
                LOGGER.error("Failed to parse stage config: {}", configFile, e);
            }
        }
    }

    /**
     * Determine the required stage for an item, or null if unrestricted.
     */
    static String getRequiredStage(ItemStack stack) {
        if (stack.isEmpty()) return null;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String itemIdStr = itemId.toString();
        String namespace = itemId.getNamespace();

        // Whitelisted items are always allowed
        if (WHITELISTED_ITEMS.contains(itemIdStr)) return null;

        // Specific item lock takes priority
        String stage = ITEM_TO_STAGE.get(itemIdStr);
        if (stage != null) return stage;

        // Fall back to mod lock
        return MOD_TO_STAGE.get(namespace);
    }

    /**
     * Check if a player has a given stage.
     * Uses reflection to call ProgressiveStages/GameStages API.
     */
    static boolean playerHasStage(ServerPlayer player, String stage) {
        // Try reading from player persistent data (common GameStages format)
        try {
            var persistentData = player.getPersistentData();
            if (persistentData.contains("PlayerStagesData")) {
                // ProgressiveStages format
                var stageData = persistentData.getCompound("PlayerStagesData");
                if (stageData.contains("Stages")) {
                    var stagesList = stageData.getList("Stages", 8); // TAG_STRING = 8
                    for (int i = 0; i < stagesList.size(); i++) {
                        if (stagesList.getString(i).equals(stage)) return true;
                    }
                    return false;
                }
            }
            // Fallback: try common GameStages format
            if (persistentData.contains("GameStageData")) {
                var stageData = persistentData.getCompound("GameStageData");
                if (stageData.contains("Stages")) {
                    var stagesList = stageData.getList("Stages", 8);
                    for (int i = 0; i < stagesList.size(); i++) {
                        if (stagesList.getString(i).equals(stage)) return true;
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Failed to read player stages from persistent data", e);
        }

        // Fallback: try reflection on known stage helper classes
        try {
            // Try net.darkhax.gamestages.GameStageHelper
            Class<?> helperClass = Class.forName("net.darkhax.gamestages.GameStageHelper");
            var method = helperClass.getMethod("hasStage", ServerPlayer.class, String.class);
            return (boolean) method.invoke(null, player, stage);
        } catch (ClassNotFoundException ignored) {
            // GameStages not present
        } catch (Exception e) {
            LOGGER.debug("Failed to call GameStageHelper.hasStage", e);
        }

        // If we can't check stages, assume the player HAS the stage (fail-open)
        LOGGER.debug("Cannot verify stage '{}' for player {}, allowing access", stage, player.getName().getString());
        return true;
    }

    /**
     * Player tick handler — checks Curios slots for restricted items.
     */
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!initialized) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.tickCount % CHECK_INTERVAL != 0) return;

        // Delegate to CuriosSlotScanner (lazy-loaded, contains Curios API imports)
        CuriosSlotScanner.scanAndEnforce(player);
    }
}
