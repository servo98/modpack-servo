# Master test function — gives ALL test kits from every servo mod
# Usage: /function servo_core:test_all_kits

function servo_packaging:test_kit
function servo_delivery:test_kit
function servo_core:test_kit
function servo_create:test_kit
function servo_dungeons:test_kit
function servo_mart:test_kit

tellraw @s {"text":"=== Todos los test kits entregados ===","color":"green","bold":true}
