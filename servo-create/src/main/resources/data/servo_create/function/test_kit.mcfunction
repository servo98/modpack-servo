# Servo Create test kit — /function servo_create:test_kit
# Items para testear Deployer Folding + Basin Compacting

# Materiales de servo_packaging
give @s servo_packaging:flat_cardboard 32
give @s servo_packaging:open_box 8

# Items packables para Basin test
give @s minecraft:cobblestone 64
give @s minecraft:iron_ingot 64
give @s minecraft:wheat 64

# Componentes Create
give @s create:mechanical_press 2
give @s create:basin 2
give @s create:deployer 2
give @s create:belt_connector 16
give @s create:shaft 16
give @s create:cogwheel 8
give @s create:andesite_casing 8
give @s create:creative_motor 2
give @s create:wrench 1
give @s create:brass_funnel 4

# Feedback
tellraw @s {"text":"[Servo Create] Test kit entregado: materiales para deployer + basin testing","color":"yellow"}
