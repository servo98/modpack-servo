# Sistema de Bosses

> Fuente: GDD v2, seccion 3.8
> Implementacion: servo_core custom
> Relacionado: [Dungeons](dungeons.md), [Progression](progression.md), [RPG Classes](rpg-classes.md), [Tokens](tokens.md)

## Overview

8 bosses custom en servo_core, 1 por capitulo. Cada uno tiene fases y mecanicas unicas. Acceso via Boss Key + Boss Altar.

## Los 8 bosses

| Cap | Boss | HP Base | Tema / Mecanica |
|-----|------|---------|-----------------|
| 1 | Guardian del Bosque | 200 | Humanoide de raices, melee + raices + minions |
| 2 | Bestia Glotona | 400 | Devora comida, vomita acido. Darle comida envenenada |
| 3 | Coloso Mecanico | 800 | Construccion de engranajes. TNT en puntos debiles |
| 4 | Locomotora Fantasma | 1,600 | Tren fantasma, pelear encima del tren |
| 5 | El Arquitecto | 3,200 | Entidad digital RS. Hackear nodos |
| 6 | Senor de las Cosechas | 6,400 | Raices que absorben crops. Cortar raices |
| 7 | Nucleo del Dungeon | 12,800 | Cristalino. Arena que cambia de forma |
| 8 | Devorador de Mundos | 25,600 | 4 fases: melee → Create → cocina buff → DPS race |

## Scaling

```
HP base:     HP(ch) = 200 * 2^(ch-1)
HP spawn:    HP * (1 + (players-1) * 0.3)    [fijo al spawnear, 0 ticks]
Dmg base:    dmg(ch) = 5 * 1.35^(ch-1)
Dmg spawn:   dmg * (1 + (players-1) * 0.15)  [fijo al spawnear]
```

Ejemplo: Boss Ch5 con 3 jugadores = 3200 * (1 + 2*0.3) = **5,120 HP**

## Boss Key

- Crafteable, costo escala por capitulo
- Boss Altar: consume Boss Key, teleporta a arena
- Boss Key fragments (drop de dungeon Maestra) reducen costo

## Loot

- Drops individuales por participante (como MMO)
- Pepe Coins: 20-50 tokens por boss
- Material de boss (requerido para Space Elevator delivery)
- Boss Ch6: dropea **Cristal de Boss Ch6** (para Llave del Nucleo)
- Boss Ch7: dropea **Cristal del Nucleo** (para T4)
- Boss final: Unique Jewelry legendarias

## Boss de Dungeon

Separado de bosses de capitulo. Solo aparece en **Dungeon Del Nucleo** (Ch7+).
- Garantiza 1-2 Fragmentos de Cristal del Nucleo
- Garantiza 1 Unique Jewelry
- 25% chance accesorio custom T4
