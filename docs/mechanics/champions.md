# Champions

> Fuente: GDD v2, seccion 3.9
> Mod: **Champions Unofficial** (16 affixes, reemplaza sistema custom del GDD original)
> Relacionado: [Dungeons](dungeons.md), [Bosses](bosses.md), [Tokens](tokens.md), [RPG Classes](rpg-classes.md)

## Overview

Mobs con affixes tipo Diablo. Implementado con **Champions Unofficial** (mod). 15 affixes documentados en 4 categorias (12 regulares + 3 dungeon exclusivos), dificultad escala por **stage del jugador mas cercano** via post-procesamiento en servo_core.

> Nota: GDD y otros docs dicen "16 affixes" (numero del mod oficial). El affix 16 puede venir de la config base del mod. La documentacion aqui lista los 15 que tenemos asignados explicitamente.

**NOTA**: El GDD originalmente planeaba esto como sistema custom de servo_core. Decidido en Session 9 usar Champions Unofficial en su lugar.

**Decision (Session 15)**: NO forkeamos Champions. servo_core post-procesa champions ya creados via API publica (`ChampionAttachment`, `IChampion`, `RankManager`). Razones: licencia ambigua (LGPL vs GPL), 165 clases de mantenimiento, API publica suficiente.

## Affixes por categoria

### Ofensivos
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Veloz | Speed II, +30% dmg | Particulas azules | Media |
| Explosivo | Explota al morir (sin block dmg) | Particulas fuego | Media |
| Invocador | Spawna 2 copias cada 15s | Particulas ender | Alta |
| Berserker | +50% dmg y speed bajo 30% HP | Ojos rojos | Alta |

### Defensivos
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Tanque | +150% HP, Resistance I | Tamano +20% | Media |
| Vampiro | Se cura 20% del dmg dado | Particulas rojas | Alta |
| Reflector | Devuelve 15% dmg recibido | Escudo visual | Alta |
| Regenerador | Regen II constante | Particulas verdes | Media |

### Control
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Gelido | Slowness II al pegar | Escarcha | Media |
| Toxico | Nube de veneno al morir (5s) | Particulas verdes | Baja |
| Cegador | Blindness 2s al pegar | Particulas oscuras | Alta |
| Gravitante | Pull hacia el mob cada 10s | Particulas moradas | Media |

### Dungeon Exclusivos
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Teleporter | Se teleporta detras del jugador | Flash ender | Muy Alta |
| Invisible | Invisible hasta que ataca | Shimmer | Muy Alta |
| Shield | Escudo que se rompe con 3 hits | Escudo giratorio | Alta |

## Dificultad por stage del jugador

Dificultad escala por el **stage del jugador mas cercano** (per-player via ProgressiveStages). servo_core post-procesa champions en `EntityJoinLevelEvent` (priority LOWEST) y hace downgrade si el tier excede lo permitido para ese stage.

**Mecanica de post-procesamiento**:
1. Champions procesa el mob en priority NORMAL (asigna rank, affixes, growth)
2. servo_core intercepta en priority LOWEST → lee champion via `ChampionAttachment.getAttachment(entity)`
3. Busca nearest `ServerPlayer` → lee su stage de ProgressiveStages (cache `Map<UUID, Integer>`)
4. Si tier del champion > max permitido para ese stage → downgrade via API publica
5. Sin player cerca → default a Ch1 (restrictivo, seguro)

**Edge case (dos players de diferente stage)**: usa el stage mas alto del player mas cercano. Si estas con alguien de Ch5, enfrentas Ch5 (como en un MMO).

**Performance**: Solo toca el ~5-15% de mobs que son champions. Cache de stages se actualiza solo con `StageChangeEvent` (8 veces max por jugador en todo el playthrough).

**Spawn rate**: Champions usa config estatica para % spawn. Al downgrade-ar champions de tier alto a tier bajo, efectivamente se vuelven mobs normales → dificultad "efectiva" baja en chapters tempranos.

### Overworld (tier max por stage del jugador)
| Player Stage | Max tier permitido | Max affixes | Pool |
|--------------|-------------------|-------------|------|
| Ch1 | 1 | 1 | Toxico, Veloz, Gelido, Tanque |
| Ch2 | 1 | 1 | + Explosivo, Regenerador |
| Ch3 | 2 | 2 | + Gravitante, Vampiro |
| Ch4 | 2 | 2 | + Berserker, Reflector |
| Ch5 | 2 | 2 | + Invocador, Cegador (12 regulares completos) |
| Ch6-8 | 3 | 2 | 12 regulares |

### Nether (tier max por stage del jugador)
| Player Stage | Max tier permitido | Max affixes | Pool |
|--------------|-------------------|-------------|------|
| Ch3 (acceso) | 2 | 2 | Pool del stage del jugador |
| Ch5+ | 3 | 3 | Pool del stage del jugador |

### Dungeons (tier por llave usada, independiente del player stage)
| Tier de llave | Spawn % | Max affixes | Pool |
|---------------|---------|-------------|------|
| Basica | 15% | 1 | Pool segun tier de llave |
| Avanzada | 25% | 2 | Pool segun tier de llave |
| Maestra | 30% | 3 | Pool segun tier de llave |
| Del Nucleo | 35% | 3 + exclusivos | Pool completo (15) incl. Teleporter, Invisible, Shield |

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
- [ ] Champions config avanzada: Affixes custom via KubeJS si necesitamos mas variedad
