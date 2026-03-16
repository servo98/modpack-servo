# Reset stages to ch1 only (simulates fresh player)
# Usage: /function servo_core:stage_reset

kubejs stages remove @s servo_ch8
kubejs stages remove @s servo_ch7
kubejs stages remove @s servo_ch6
kubejs stages remove @s servo_ch5
kubejs stages remove @s servo_ch4
kubejs stages remove @s servo_ch3
kubejs stages remove @s servo_ch2
kubejs stages add @s servo_ch1
tellraw @s {"text":"[Servo] Stages reseteados a Ch1","color":"yellow"}
