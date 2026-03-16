# Unlock all stages for testing (bypasses progression)
# Usage: /function servo_core:stage_unlock
# Note: linear_progression=true means giving ch8 auto-grants ch1-ch7

kubejs stages add @s servo_ch8
tellraw @s {"text":"[Servo] Todos los capitulos desbloqueados (ch1-ch8)","color":"green"}
