# Champions

> Mod: **Champions Unofficial** (16 affixes nativos del mod)
> Relacionado: [Dungeons](dungeons.md), [Bosses](bosses.md), [Tokens](tokens.md), [RPG Classes](rpg-classes.md)

## Overview

Mobs con affixes tipo Diablo. Implementado con **Champions Unofficial** (mod, 16 affixes). Dificultad escala por **stage del jugador mas cercano** via post-procesamiento en servo_core.

**Decision (Session 15)**: NO forkeamos Champions. servo_core post-procesa champions ya creados via API publica (`ChampionAttachment`, `IChampion`, `RankManager`).

## Los 16 affixes (nombres reales del mod)

### Ofensivos
| Affix | ID | Efecto | Dificultad |
|-------|-----|--------|------------|
| Hasty | `hasty` | Velocidad de movimiento muy aumentada | Baja |
| Enkindling | `enkindling` | Dispara proyectiles de fuego constantemente | Media |
| Desecrating | `desecrating` | Spawna nubes de dano bajo el objetivo periodicamente | Media |
| Infested | `infested` | Spawna silverfish al atacar y al morir | Alta |
| Wounding | `wounding` | Inflige Wound: -50% curacion recibida, +150% dano recibido | Alta |

### Defensivos
| Affix | ID | Efecto | Dificultad |
|-------|-----|--------|------------|
| Lively | `lively` | Regenera 1 HP/s (5 HP/s fuera de combate) | Baja |
| Dampening | `dampening` | Reduce dano indirecto (proyectiles, explosiones) | Media |
| Reflective | `reflective` | Refleja porcion del dano recibido al atacante | Alta |
| Adaptable | `adaptable` | Reduce dano del mismo tipo cuando se recibe consecutivamente | Alta |
| Shielding | `shielding` | Escudo periodico que absorbe todo el dano | Alta |

### Control
| Affix | ID | Efecto | Dificultad |
|-------|-----|--------|------------|
| Plagued | `plagued` | Infecta criaturas cercanas con Veneno | Baja |
| Arctic | `arctic` | Dispara proyectiles de hielo que ralentizan | Media |
| Knocking | `knocking` | Knockback masivo + Slowness al atacar | Media |
| Magnetic | `magnetic` | Pull periodico hacia si mismo | Media |
| Molten | `molten` | Resistencia a fuego + ataques de fuego + penetracion de armadura | Alta |
| Paralyzing | `paralyzing` | Jaula al objetivo (no puede moverse por segundos) | Alta |

## Desbloqueo por capitulo (pool de affixes)

Affixes se introducen gradualmente. Los faciles primero, los peligrosos despues.

### Overworld (tier max por stage del jugador)
| Player Stage | Max affixes | Pool (acumulativo) | Total |
|--------------|-------------|-------------------|-------|
| Ch1 | 1 | Hasty, Plagued, Lively, Arctic | 4 |
| Ch2 | 1 | + Knocking, Infested | 6 |
| Ch3 | 2 | + Desecrating, Enkindling, Magnetic | 9 |
| Ch4 | 2 | + Reflective, Dampening, Adaptable | 12 |
| Ch5 | 2 | + Shielding, Molten, Paralyzing, Wounding (todos) | 16 |
| Ch6-8 | 3 | 16 (todos) | 16 |

### Nether (tier max por stage del jugador)
| Player Stage | Max affixes | Pool |
|--------------|-------------|------|
| Ch3 (acceso) | 2 | Pool del stage del jugador |
| Ch5+ | 3 | Pool del stage del jugador |

### Dungeons (tier por llave usada, independiente del player stage)
| Tier de llave | Spawn % | Max affixes | Pool |
|---------------|---------|-------------|------|
| Basica | 15% | 1 | Hasty, Plagued, Lively, Arctic, Knocking, Infested |
| Avanzada | 25% | 2 | + Desecrating, Enkindling, Magnetic, Reflective, Dampening |
| Maestra | 30% | 3 | + Adaptable, Shielding, Molten |
| Del Nucleo | 35% | 3 | Todos (16) incl. Paralyzing, Wounding |

## Dificultad por stage del jugador

servo_core post-procesa champions en `EntityJoinLevelEvent` (priority LOWEST) y hace downgrade si el tier excede lo permitido para ese stage.

**Mecanica de post-procesamiento**:
1. Champions procesa el mob en priority NORMAL (asigna rank, affixes, growth)
2. servo_core intercepta en priority LOWEST → lee champion via `ChampionAttachment.getAttachment(entity)`
3. Busca nearest `ServerPlayer` → lee su stage de ProgressiveStages (cache `Map<UUID, Integer>`)
4. Si tier del champion > max permitido para ese stage → downgrade via API publica
5. Sin player cerca → default a Ch1 (restrictivo, seguro)

**Edge case (dos players de diferente stage)**: usa el stage mas alto del player mas cercano.

**Performance**: Solo toca el ~5-15% de mobs que son champions. Cache de stages se actualiza solo con `StageChangeEvent`.

## HP Scaling

```
Champion HP = mob_hp * multiplicador
  1 affix:  * 2.5
  2 affixes: * 3.75
  3 affixes: * 5.0
```

## Drops

- Pepe Coins: 1-3 tokens por kill
- Esencia de Dungeon: 15% en Avanzada, 25% en Maestra (solo en dungeons)
- Mejor loot que mobs normales

## Implementacion pendiente (servo_core, Fase 7)

- [ ] servo_core: `EntityJoinLevelEvent` handler (priority LOWEST) para post-procesar champions
- [ ] servo_core: cache `Map<UUID, Integer>` de player stages, actualizado via `StageChangeEvent`
- [ ] servo_core: logica de downgrade por dimension (Overworld/Nether: stage del player, Dungeons: tier de llave)
- [ ] Champions: config base estatica (spawn rates, pools maximos)
