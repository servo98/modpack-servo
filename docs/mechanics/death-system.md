# Sistema de Muerte

> Fuente: GDD v2, seccion 3.5 (muerte en dungeons)
> Mods: YIGD (You're in Grave Danger)
> Relacionado: [Dungeons](dungeons.md), [RPG Classes](rpg-classes.md), [Accessories](accessories.md)

## Overview

Comportamiento diferente segun la zona. Simple y claro.

## Comportamiento por zona

| Zona | Al morir | Que pasa |
|------|----------|----------|
| Overworld | YIGD tumba normal | Nada se pierde (tumba con compas) |
| Nether | YIGD tumba normal | Nada se pierde |
| Dungeon | **Roguelike** | Soulbound se queda, loot de la run se pierde |

## Soulbound (NUNCA se pierden)

Items con propiedad Soulbound (automatica en T2+):
- Armor equipada (4 piezas)
- Arma principal equipada
- Rune Pouch equipada
- Accesorios equipados (Curios: rings, necklaces, belt, back, feet)

**Nota**: Items T0-T1 NO son soulbound.

## Items que SI se pierden en dungeon

- Loot recogido en esa run (ores, tokens, drops)
- Comida y pociones
- Bloques y materiales sueltos
- Llaves extra en inventario

## Multiplayer en dungeon

- Jugador A muere → respawnea en overworld con Soulbound items
- Jugador B sigue en dungeon → puede completar solo o morir
- Cuando 0 jugadores quedan → servo_core marca chunks para limpieza

## YIGD en overworld/nether

- Tumba con todos los items
- Compas para encontrar la tumba
- Timer anti-robo (otros jugadores no pueden abrir tu tumba inmediatamente)
