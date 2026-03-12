# Champions

> Fuente: GDD v2, seccion 3.9
> Mod: **Champions Unofficial** (16 affixes, reemplaza sistema custom de elite mobs del GDD original)
> Relacionado: [Dungeons](dungeons.md), [Bosses](bosses.md), [Tokens](tokens.md), [RPG Classes](rpg-classes.md)

## Overview

Mobs con affixes tipo Diablo. Implementado con **Champions Unofficial** (mod). 16 affixes en 4 categorias, spawn por zona. 6,884 combinaciones posibles.

**NOTA**: El GDD originalmente llamaba esto "Elite Mobs" como sistema custom de servo_core. Decidido en Session 9 usar Champions Unofficial en su lugar.

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

## Spawn rates por zona

| Zona | Chance base | Max affixes |
|------|------------|-------------|
| Overworld | 8% | 1 (Ch1-2), 2 (Ch3+) |
| Nether | 15% | 2 |
| Dungeon Basica | 15% | 1 |
| Dungeon Avanzada | 25% | 2 |
| Dungeon Maestra | 30% | 3 |
| Dungeon Del Nucleo | 35% | 3 + exclusivos (Teleporter, Invisible, Shield) |

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

- [ ] Champions: configurar tiers/ranks por zona
- [ ] Champions config avanzada: Affixes custom via KubeJS si necesitamos mas variedad
