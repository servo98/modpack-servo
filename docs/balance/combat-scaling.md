# Combat Scaling - Modpack Servo

## Decision: RPG Series + custom systems
- RPG Series (Daedelus): armas, armaduras y spells por clase
- Elite mobs con affixes tipo Diablo via **Champions Unofficial** (16 affixes, reemplaza custom servo_core)
- Accesorios: Jewelry (rings/necklaces) + custom servo_core (belt, back, feet)
- Max 8 jugadores
- Stats detallados en `rpg-weapon-stats.md`

## Boss Scaling (se fija al spawnear, 0 ticks extra)
- HP: `base * (1 + (players-1) * 0.3)`
- Dmg: `base * (1 + (players-1) * 0.15)`
- NO cambia mid-fight (si alguien se desconecta, queda igual)
- Champions (elite mobs): stats configurados via Champions Unofficial, sin scaling por jugadores

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

Spell DPS = spell_power * coefficient / cast_time.
Con Staff T2 (6.0 SP) + Arcane Robe T2 (+100% arcane) = 12.0 effective SP.

| Cap | Weapon           | Base SP | Armor Bonus | Effective SP | Best Spell (coeff) | Spell DPS |
|-----|------------------|---------|-------------|--------------|--------------------| ----------|
| 1   | Novice Wand T0   | 3.0     | -           | 3.0          | fire_scorch (0.6)  | 1.5       |
| 2   | -                | -       | -           | -            | (melee only Ch2)   | -         |
| 3   | Arcane Staff T2  | 6.0     | +100% (robe)| 12.0         | arcane_blast (0.8) | 6.4       |
| 4   | Arcane Staff T2  | 6.0     | +100%       | 12.0         | arcane_beam (1.0)  | 12.0/5s   |
| 5   | NR Arcane Staff  | 7.0     | +120%       | 15.4         | fire_meteor (1.0)  | 15.4      |
| 6   | NR Arcane Staff  | 7.0     | +120%       | 15.4         | fire_meteor (1.0)  | 15.4      |
| 7   | NR Staff+ench    | ~8.0    | +120%       | 17.6         | arcane_beam (1.0)  | 17.6/5s   |
| 8   | Custom T4 Staff  | 8.0     | +120%       | 17.6         | frost_blizzard(0.7)| 12.3/8s   |

> Nota: Mages hacen burst damage alto pero con cooldowns. Melee es sustained.

## Boss HP by Chapter (base, solo, y con 8 jugadores)
| Cap | Base HP | Solo | 2p | 4p | 8p |
|-----|---------|------|-----|-----|-----|
| 1 | 200 | 200 | 260 | 380 | 620 |
| 2 | 400 | 400 | 520 | 760 | 1,240 |
| 3 | 800 | 800 | 1,040 | 1,520 | 2,480 |
| 4 | 1,600 | 1,600 | 2,080 | 3,040 | 4,960 |
| 5 | 3,200 | 3,200 | 4,160 | 6,080 | 9,920 |
| 6 | 6,400 | 6,400 | 8,320 | 12,160 | 19,840 |
| 7 | 12,800 | 12,800 | 16,640 | 24,320 | 39,680 |
| 8 | 25,600 | 25,600 | 33,280 | 48,640 | 79,360 |

## Enemy HP Scaling (fijo, sin scaling por jugadores)
| Cap | Normal | Elite 1x | Elite 2x | Elite 3x (dungeon) |
|-----|--------|----------|----------|---------------------|
| 1 | 20 | 50 | - | - |
| 2 | 24 | 60 | 90 | - |
| 3 | 28 | 70 | 105 | - |
| 4 | 34 | 85 | 128 | - |
| 5 | 40 | 100 | 150 | - |
| 6 | 48 | 120 | 180 | - |
| 7 | 58 | 145 | 218 | 327 |
| 8 | 70 | 175 | 263 | 394 |

## Elite Mob Prefixes
| Prefix | Efecto | Visual |
|--------|--------|--------|
| Veloz | Speed II, +30% dmg | Particulas azules |
| Tanque | +150% HP, Resistance I | Tamano +20%, brillo |
| Explosivo | Explota al morir (sin block damage) | Particulas fuego |
| Invocador | Spawna 2 copias cada 15s | Particulas ender |
| Vampiro | Se cura 20% del dmg dado | Particulas rojas |
| Reflector | Devuelve 15% dmg recibido | Escudo visual |
| Toxico | Nube de veneno al morir | Particulas verdes |
| Gelido | Slowness II al pegar | Escarcha visual |

## Spawn Rates (Champions Unofficial)
- Overworld: 8% base, max 1 affix (Ch1-2), max 2 affix (Ch3+)
- Nether: 15%, max 2 affix
- Dungeon Basica: 15%, max 1 affix
- Dungeon Avanzada: 25%, max 2 affix
- Dungeon Maestra: 30%, max 3 affix
- Dungeon Del Nucleo: 35%, max 3 affix + exclusivos (Teleporter/Invisible/Shield)
- Ver detalle completo: docs/mechanics/champions.md
