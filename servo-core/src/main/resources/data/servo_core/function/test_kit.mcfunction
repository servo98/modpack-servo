# Servo Core test kit — /function servo_core:test_kit
# Items custom registrados en Java (ModRegistry.java)

# Tokens y moneda
give @s servo_core:pepe_coin 64

# Dungeon keys (4 tiers)
give @s servo_core:basic_dungeon_key 4
give @s servo_core:advanced_dungeon_key 4
give @s servo_core:master_dungeon_key 4
give @s servo_core:core_dungeon_key 4

# Materiales
give @s servo_core:dungeon_essence 16
give @s servo_core:core_crystal_fragment 8

# Feedback
tellraw @s {"text":"[Servo Core] Test kit entregado: coins, keys, esencias, cristales","color":"gold"}
