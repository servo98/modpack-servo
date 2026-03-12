# Champions

> Fuente: GDD v2, seccion 3.9
> Mod: **Champions Unofficial** (16 affixes, reemplaza sistema custom del GDD original)
> Relacionado: [Dungeons](dungeons.md), [Bosses](bosses.md), [Tokens](tokens.md), [RPG Classes](rpg-classes.md)

## Overview

Mobs con affixes tipo Diablo. Implementado con **Champions Unofficial** (mod). 15 affixes en 4 categorias, spawn escala por world stage. 6,884 combinaciones posibles.

**NOTA**: El GDD originalmente planeaba esto como sistema custom de servo_core. Decidido en Session 9 usar Champions Unofficial en su lugar.

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

## Spawn rates por world stage

Dificultad escala con el **world stage** (capitulo mas alto completado por cualquier team). servo_core guarda esto en world saved data y ajusta la config de Champions.

### Overworld
| World Stage | Spawn % | Max affixes | Pool |
|-------------|---------|-------------|------|
| Ch1 | 5% | 1 | Toxico, Veloz, Gelido, Tanque |
| Ch2 | 8% | 1 | + Explosivo, Regenerador |
| Ch3 | 8% | 2 | + Gravitante, Vampiro |
| Ch4 | 10% | 2 | + Berserker, Reflector |
| Ch5 | 10% | 2 | + Invocador, Cegador (12 regulares completos) |
| Ch6-8 | 12% | 2 | 12 regulares |

### Nether
| World Stage | Spawn % | Max affixes | Pool |
|-------------|---------|-------------|------|
| Ch3 (acceso) | 15% | 2 | Pool del world stage actual |
| Ch5+ | 18% | 3 | Pool del world stage actual |

### Dungeons (siempre mas dificil que overworld)
| Tier | Spawn % | Max affixes | Pool |
|------|---------|-------------|------|
| Basica | 15% | 1 | Pool del world stage actual |
| Avanzada | 25% | 2 | Pool del world stage actual |
| Maestra | 30% | 3 | Pool del world stage actual |
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

## Configuracion pendiente

- [ ] servo_core: implementar world stage (saved data global, max chapter completado)
- [ ] servo_core: listener que actualice config de Champions al cambiar world stage
- [ ] Champions: config base por area (overworld/nether/dungeon tiers)
- [ ] Champions config avanzada: Affixes custom via KubeJS si necesitamos mas variedad
