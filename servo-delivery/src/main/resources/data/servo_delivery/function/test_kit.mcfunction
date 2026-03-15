# Servo Delivery test kit — /function servo_delivery:test_kit
# Gives all blocks for the multiblock + test shipping boxes

# Multiblock blocks (9 total)
give @s servo_delivery:elevator_base 3
give @s servo_delivery:delivery_port 2
give @s servo_delivery:delivery_terminal 1
give @s servo_delivery:elevator_antenna 1

# Test shipping boxes (Ch1 requirements)
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"farmersdelight:vegetable_soup",count:16,category:"food"}] 4
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"farmersdelight:beef_stew",count:16,category:"food"}] 4
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"farmersdelight:mixed_salad",count:16,category:"food"}] 8
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"minecraft:wheat",count:16,category:"crops"}] 4
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"minecraft:cooked_salmon",count:16,category:"food"}] 2
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"minecraft:iron_pickaxe",count:16,category:"processed"}] 1
give @s servo_packaging:shipping_box[servo_packaging:box_contents={item_id:"minecraft:iron_sword",count:16,category:"processed"}] 1
