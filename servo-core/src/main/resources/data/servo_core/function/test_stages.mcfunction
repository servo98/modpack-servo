# Test stage progression — cycles through chapters with feedback
# Usage: /function servo_core:test_stages

tellraw @s {"text":"=== Test de Progression Stages ===","color":"aqua","bold":true}

# Report current stages
kubejs stages list @s

tellraw @s {"text":"Usa estos comandos para testear stages individuales:","color":"gray"}
tellraw @s {"text":"  /kubejs stages add @s servo_ch2","color":"gray"}
tellraw @s {"text":"  /kubejs stages add @s servo_ch3","color":"gray"}
tellraw @s {"text":"  /function servo_core:stage_unlock  (todos)","color":"gray"}
tellraw @s {"text":"  /function servo_core:stage_reset   (solo ch1)","color":"gray"}
