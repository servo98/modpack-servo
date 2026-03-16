# Servo Dungeons test kit — /function servo_dungeons:test_kit
# Items y bloques para testing del sistema de dungeons

# Dungeon essence + boss drops
give @s servo_dungeons:dungeon_essence 32
give @s servo_dungeons:boss_heart 4
give @s servo_dungeons:boss_crystal 2
give @s servo_dungeons:nether_star_fragment 4

# Dungeon keys (1 de cada tier)
give @s servo_dungeons:dungeon_key_basic 2
give @s servo_dungeons:dungeon_key_advanced 1
give @s servo_dungeons:dungeon_key_master 1
give @s servo_dungeons:dungeon_key_core 1

# Altar blocks
give @s servo_dungeons:dungeon_pedestal 1
give @s servo_dungeons:dungeon_rune 4

# Feedback
tellraw @s {"text":"[Servo Dungeons] Test kit entregado: esencia, boss drops, llaves, altar","color":"dark_purple"}
