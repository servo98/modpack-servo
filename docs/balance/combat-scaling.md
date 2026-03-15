# Combat Scaling - Modpack Servo

## Decision: RPG Series + custom systems
- RPG Series (Daedelus): armas, armaduras y spells por clase
- Champions con affixes tipo Diablo via **Champions Unofficial** (16 affixes, reemplaza custom servo_core)
- Accesorios: Jewelry (rings/necklaces) + custom servo_core (belt, back, feet, head)
- Max 8 jugadores
- Stats detallados en `rpg-weapon-stats.md`

## Boss Scaling (se fija al spawnear, 0 ticks extra)
- HP: `base * (1 + (players-1) * 0.3)`
- Dmg: `base * (1 + (players-1) * 0.15)`
- NO cambia mid-fight (si alguien se desconecta, queda igual)
- Champions: stats configurados via Champions Unofficial, sin scaling por jugadores

| Jugadores | HP multiplier | Dmg multiplier |
|-----------|--------------|----------------|
| 1 | 1.0x | 1.0x |
| 2 | 1.3x | 1.15x |
| 4 | 1.9x | 1.45x |
| 6 | 2.5x | 1.75x |
| 8 | 3.1x | 2.05x |

## Player DPS by Chapter (Melee - Warrior/Rogue example)

Basado en RPG Series weapon stats reales (ver rpg-weapon-stats.md).
Warrior usa Claymore (slow, high dmg). Rogue usa Dagger (fast, low dmg).
Armor bonuses: Warrior +4-5% dmg/piece, Rogue +2-5% dmg/piece + haste.

| Cap | Warrior Arma        | Base ATK | Armor Bonus | Effective DPS | Rogue Arma       | Base ATK | Effective DPS |
|-----|---------------------|----------|-------------|---------------|------------------|----------|---------------|
| 1   | Vanilla sword       | 6.0      | -           | 9.6           | Flint Dagger T0  | 2.6      | 6.2           |
| 2   | Iron Claymore T1    | 8.3      | +16% (war)  | 9.6           | Iron Dagger T1   | 3.3      | 9.2           |
| 3   | Diamond Claymore T2 | 9.9      | +20% (ber)  | 11.9          | Diamond Dagger T2| 4.0      | 12.5          |
| 4   | Diamond Claymore T2 | 9.9      | +20%        | 11.9          | Diamond Dagger T2| 4.0      | 12.5          |
| 5   | NR Claymore T3      | 11.5     | +20%        | 13.8          | NR Dagger T3     | 4.7      | 14.7          |
| 6   | NR Claymore T3      | 11.5     | +20%+tough  | 13.8          | NR Dagger T3     | 4.7      | 14.7          |
| 7   | NR Claymore T3+ench | ~13.5    | +20%        | 16.2          | NR Dagger+ench   | ~6.7     | 18.5          |
| 8   | Custom T4 Claymore  | 13.0     | +20%        | 15.6          | Custom T4 Dagger | 5.5      | 17.2          |

## Player DPS by Chapter (Mage - Wizard example)

Spell DPS sustained = spell_power * coefficient / cast_time (rotacion optima, single-target).
Spells bypasean armor del boss (solo aplica spell resistance, que es 0 por defecto).

| Cap | Weapon           | Base SP | Armor Bonus | Effective SP | Mejor rotacion sustained | Spell DPS |
|-----|------------------|---------|-------------|--------------|--------------------------|-----------|
| 1   | Novice Wand T0   | 3.0     | -           | 3.0          | fireball (0.8/1.5s)      | 1.6       |
| 2   | -                | -       | -           | -            | (melee only Ch2)         | -         |
| 3   | Frost Staff T2   | 6.0     | +100% (robe)| 12.0         | frostbolt (0.8/1.1s)     | 8.7       |
| 4   | Frost Staff T2   | 6.0     | +100%       | 12.0         | frostbolt + frost_nova   | 8.9       |
| 5   | NR Frost Staff   | 7.0     | +120%       | 15.4         | frostbolt (0.8/1.1s)     | 11.2      |
| 6   | NR Frost Staff   | 7.0     | +120%       | 15.4         | frostbolt sustained      | 11.2      |
| 7   | NR Staff+ench    | ~8.0    | +120%       | 17.6         | frostbolt (0.8/1.1s)     | 12.8      |
| 8   | Custom T4 Staff  | 8.0     | +120%       | 17.6         | frostbolt sustained      | 12.8      |

> Nota: Frost wizard tiene el mejor sustained single-target (frostbolt: sin CD, 1.1s cast).
> Fire/Arcane burst (meteor 1.0/1s/10sCD, arcane_beam 1.0/5sCh/10sCD) son altos pero el sustained con fillers es menor.
> Wizard DPS es ~30% menor que melee pero bypasea armor — contra Coloso (14 armor) wizard es competitivo.
> Analisis detallado: [spell-power-analysis.md](spell-power-analysis.md)

## Boss HP by Chapter (base, solo, y con 8 jugadores)

Formula: `HP = 800 * 1.44^(cap-1)` (crece x1.44/cap, 13x total).
Curva anterior (200 * 2^cap) crecia 128x total — divergia del DPS que solo crece 1.8x.
Justificacion: [spell-power-analysis.md](spell-power-analysis.md)

| Cap | Base HP | Solo | 2p | 4p | 8p |
|-----|---------|------|-------|-------|--------|
| 1 | 800 | 800 | 1,040 | 1,520 | 2,480 |
| 2 | 1,200 | 1,200 | 1,560 | 2,280 | 3,720 |
| 3 | 1,600 | 1,600 | 2,080 | 3,040 | 4,960 |
| 4 | 2,400 | 2,400 | 3,120 | 4,560 | 7,440 |
| 5 | 3,400 | 3,400 | 4,420 | 6,460 | 10,540 |
| 6 | 5,000 | 5,000 | 6,500 | 9,500 | 15,500 |
| 7 | 7,200 | 7,200 | 9,360 | 13,680 | 22,320 |
| 8 | 10,400 | 10,400 | 13,520 | 19,760 | 32,240 |

## Enemy HP Scaling (fijo, sin scaling por jugadores)
| Cap | Normal | Champion 1x | Champion 2x | Champion 3x (dungeon) |
|-----|--------|----------|----------|---------------------|
| 1 | 20 | 50 | - | - |
| 2 | 24 | 60 | 90 | - |
| 3 | 28 | 70 | 105 | - |
| 4 | 34 | 85 | 128 | - |
| 5 | 40 | 100 | 150 | - |
| 6 | 48 | 120 | 180 | - |
| 7 | 58 | 145 | 218 | 327 |
| 8 | 70 | 175 | 263 | 394 |

## Champion Affixes

Lista completa de affixes (4 categorias), spawn rates por stage, y mecanica de post-procesamiento: ver [mechanics/champions.md](../mechanics/champions.md).

## Spawn Rates (Champions Unofficial)
Escalan por **stage del jugador mas cercano** (per-player via ProgressiveStages). servo_core post-procesa champions y hace downgrade si exceden el tier permitido.

- Overworld/Nether: tier max basado en stage del player mas cercano
- Dungeons: tier basado en llave usada para crear la instancia (independiente del player stage)
- Del Nucleo: 35%, 3 affixes, pool completo (16 incl. Paralyzing, Wounding)
- Pool de affixes disponibles crece con cada capitulo completado (4→6→8→10→12→15)
