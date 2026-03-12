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
| Boss Chamber | **Re-entry via portal** | Soulbound se queda, consumibles se pierden, portal reabre 15s |

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

## Muerte en dungeon — mecanica completa

Estilo **Dark Souls**: tu tumba queda donde moriste, puedes volver a buscarla.

1. Mueres en dungeon → YIGD crea tumba con tu run loot
2. Soulbound (T2+ equipado) se queda contigo
3. Respawneas en overworld (cama/spawn)
4. Vas al Altar de Dungeon (sigue activo, brillando)
5. Click en pedestal → teleport **gratis** a sala de entrada de la dungeon
6. Peleas de vuelta a tu tumba → recuperas items
7. Si mueres otra vez → tumba anterior suelta items, nueva tumba se forma

**Nota**: No necesitas otra llave. Re-entry es gratis e ilimitado mientras la dungeon este activa.

Ver: [Dungeons - Multiplayer](dungeons.md#muerte-en-dungeon-dark-souls-style) para el flujo completo.

## Multiplayer en dungeon

- Jugador A muere → respawnea en overworld, puede re-entrar gratis por el altar
- Jugador B sigue en dungeon → continua peleando
- Cuando 0 jugadores quedan Y nadie re-entra en 10 min → servo_core limpia chunks
- Tumbas no recogidas se teleportan al altar en overworld (items no se pierden)

## Muerte en Boss Chamber

Diferente al sistema de dungeons. NO hay tumba YIGD.

1. Mueres en Boss Chamber → respawneas en overworld junto al Boss Altar
2. Items Soulbound (T2+ equipado) se quedan contigo
3. Consumibles (comida, pociones, materiales) se pierden
4. El portal se **REABRE automaticamente 15 segundos** → puedes re-entrar
5. Re-entras debilitado (sin consumibles) pero con tu gear
6. El HP del boss NO se recalcula al re-entrar
7. Si **TODOS** los jugadores mueren simultaneamente → Boss Chamber se destruye, hay que usar otra Boss Key

**Diferencia clave con dungeons**: No hay tumba que recuperar. No hay re-entry ilimitado. El portal solo reabre 15s por muerte. La penalizacion es perder consumibles, no loot.

Ver: [Bosses - Muerte y Soulbound](bosses.md#7-muerte-y-soulbound-en-boss-chamber) para detalle completo.

## YIGD en overworld/nether

- Tumba con todos los items
- Compas para encontrar la tumba
- Timer anti-robo (otros jugadores no pueden abrir tu tumba inmediatamente)
