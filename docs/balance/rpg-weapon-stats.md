# RPG Series - Weapon & Armor Stats
> Extraido del source code de ZsoltMolnarrr (SpellEngine, Wizards, Rogues, Paladins)
> Branch: 1.21.1

## Weapon Damage by Tier (Attack Damage base)

Los valores son `attack_damage` (se suma al 1.0 base del jugador).
Attack speed es modifier (base 4.0, se resta el valor).

| Weapon Type    | Speed  | T0    | T1    | T2    | T3     | T4     |
|----------------|--------|-------|-------|-------|--------|--------|
| **Dagger**     | -1.6   | 2.6   | 3.3   | 4.0   | 4.7    | 5.5    |
| **Sickle**     | -2.0   | 3.2   | 4.1   | 5.0   | 5.9    | 6.8    |
| **Sword**      | -2.4   | 4.0   | 5.0   | 6.0   | 7.0    | 8.0    |
| **Spear**      | -2.6   | 4.0   | 5.0   | 6.0   | 7.0    | 8.0    |
| **Glaive**     | -2.6   | 4.6   | 5.8   | 7.0   | 8.1    | 9.3    |
| **Mace**       | -2.8   | 5.7   | 7.0   | 8.3   | 9.6    | 11.0   |
| **Double Axe** | -2.8   | 5.6   | 7.0   | 8.3   | 9.6    | 11.0   |
| **Claymore**   | -3.0   | 6.8   | 8.3   | 9.9   | 11.5   | 13.0   |
| **Staff**      | -3.0   | 4.0   | 4.0   | 4.0   | 4.0    | 4.0    |
| **Hammer**     | -3.2   | 8.5   | 10.3  | 12.2  | 14.1   | 16.0   |
| **Wand**       | -2.4   | 2.0   | 2.0   | 2.0   | 2.0    | 2.0    |

### DPS Calculado (damage * attacks_per_sec)
attacks_per_sec = 4.0 + speed_modifier

| Weapon Type    | APS  | T0 DPS | T1 DPS | T2 DPS | T3 DPS | T4 DPS |
|----------------|------|--------|--------|--------|--------|--------|
| **Dagger**     | 2.4  | 6.2    | 7.9    | 9.6    | 11.3   | 13.2   |
| **Sickle**     | 2.0  | 6.4    | 8.2    | 10.0   | 11.8   | 13.6   |
| **Sword**      | 1.6  | 6.4    | 8.0    | 9.6    | 11.2   | 12.8   |
| **Spear**      | 1.4  | 5.6    | 7.0    | 8.4    | 9.8    | 11.2   |
| **Glaive**     | 1.4  | 6.4    | 8.1    | 9.8    | 11.3   | 13.0   |
| **Mace**       | 1.2  | 6.8    | 8.4    | 10.0   | 11.5   | 13.2   |
| **Double Axe** | 1.2  | 6.7    | 8.4    | 10.0   | 11.5   | 13.2   |
| **Claymore**   | 1.0  | 6.8    | 8.3    | 9.9    | 11.5   | 13.0   |
| **Hammer**     | 0.8  | 6.8    | 8.2    | 9.8    | 11.3   | 12.8   |

> Nota: Staves/Wands tienen bajo DPS melee pero su daño real viene de spells.

## Spell Power by Weapon Tier

| Weapon Type       | T0  | T1  | T2  | T3  | T4  |
|-------------------|-----|-----|-----|-----|-----|
| **Damage Wand**   | 3.0 | 4.0 | 5.0 | 5.5 | 8.0 |
| **Healing Wand**  | 3.0 | 4.0 | 5.0 | 5.5 | 8.0 |
| **Damage Staff**  | 4.5 | 5.0 | 6.0 | 7.0 | 8.0 |
| **Healing Staff**  | 4.5 | 5.0 | 6.0 | 7.0 | 8.0 |

## Armor Protection Values

Protection total = head + chest + legs + feet

### Wizard Robes (light armor)
| Set                    | Tier | Head | Chest | Legs | Feet | **Total** | Durability | Spell Power/piece | Secondary/piece |
|------------------------|------|------|-------|------|------|-----------|------------|-------------------|-----------------|
| Wizard Robe            | T1   | 1    | 3     | 2    | 1    | **7**     | 10         | +20% all elements | - |
| Arcane Robe            | T2   | 1    | 3     | 2    | 1    | **7**     | 20         | +25% arcane | +2% haste |
| Fire Robe              | T2   | 1    | 3     | 2    | 1    | **7**     | 20         | +25% fire | +2% crit chance |
| Frost Robe             | T2   | 1    | 3     | 2    | 1    | **7**     | 20         | +25% frost | +5% crit damage |
| Netherite Arcane Robe  | T3   | 1    | 3     | 2    | 1    | **7**     | 30         | +30% arcane | +3% haste |
| Netherite Fire Robe    | T3   | 1    | 3     | 2    | 1    | **7**     | 30         | +30% fire | +3% crit chance |
| Netherite Frost Robe   | T3   | 1    | 3     | 2    | 1    | **7**     | 30         | +30% frost | +6% crit damage |

> Full set spell power bonus: x4 pieces. Ej: Arcane T2 = +100% arcane spell power total

### Rogue Armor (medium armor)
| Set                     | Tier | Head | Chest | Legs | Feet | **Total** | Durability | Bonus/piece |
|-------------------------|------|------|-------|------|------|-----------|------------|-------------|
| Rogue Armor             | T1   | 1    | 3     | 3    | 1    | **8**     | 15         | +3% evasion, +4% haste |
| Assassin Armor          | T2   | 2    | 4     | 4    | 2    | **12**    | 25         | +4% evasion, +5% haste, +2% dmg, +2% crit |
| Netherite Assassin      | T3   | 2    | 4     | 4    | 2    | **12**    | 37         | +5% evasion, +5% haste, +5% dmg, +2.5% crit |

### Warrior Armor (heavy armor)
| Set                     | Tier | Head | Chest | Legs | Feet | **Total** | Durability | Bonus/piece |
|-------------------------|------|------|-------|------|------|-----------|------------|-------------|
| Warrior Armor           | T1   | 2    | 5     | 4    | 1    | **12**    | 15         | +4% dmg |
| Berserker Armor         | T2   | 3    | 8     | 6    | 2    | **19**    | 25         | +5% dmg, +0.1 knockback, +4% crit dmg |
| Netherite Berserker     | T3   | 3    | 8     | 6    | 2    | **19**    | 37         | +5% dmg, +1 toughness, +0.1 knockback, +5% crit dmg |

### Paladin Armor (heavy armor + healing)
| Set                     | Tier | Head | Chest | Legs | Feet | **Total** | Durability | Bonus/piece |
|-------------------------|------|------|-------|------|------|-----------|------------|-------------|
| Paladin Armor           | T1   | 2    | 6     | 5    | 2    | **15**    | 15         | +0.5 healing power |
| Crusader Armor          | T2   | 3    | 8     | 6    | 3    | **20**    | 25         | +1.0 healing power |
| Netherite Crusader      | T3   | 3    | 8     | 6    | 3    | **20**    | 37         | +1.0 healing power, +1 toughness |

### Priest Robe (light armor + healing)
| Set                     | Tier | Head | Chest | Legs | Feet | **Total** | Durability | Bonus/piece |
|-------------------------|------|------|-------|------|------|-----------|------------|-------------|
| Priest Robe             | T1   | 1    | 3     | 2    | 1    | **7**     | 10         | +20% healing |
| Prior Robe              | T2   | 1    | 3     | 2    | 1    | **7**     | 20         | +25% healing, +3% haste |
| Netherite Prior Robe    | T3   | 1    | 3     | 2    | 1    | **7**     | 30         | +30% healing, +4% haste |

## Comparacion con Vanilla

| Armor Set    | Total Protection | Toughness |
|--------------|-----------------|-----------|
| Leather      | 7               | 0         |
| Iron         | 15              | 0         |
| Diamond      | 20              | 8         |
| Netherite    | 20              | 12        |
| Wizard Robe  | 7               | 0         |
| Rogue T2     | 12              | 0         |
| Warrior T2   | 19              | 0         |
| Paladin T2   | 20              | 0         |
| Paladin T3   | 20              | 4         |

## Weapon Assignments by Mod

### Wizards
| Weapon          | Type         | Tier | Material    | Spell School |
|-----------------|-------------|------|-------------|--------------|
| Novice Wand     | Damage Wand | T0   | Stick       | Fire         |
| Wizard Staff    | Damage Staff| T1   | Stick       | All 3        |
| Arcane Wand     | Damage Wand | T2   | Gold        | Arcane       |
| Fire Wand       | Damage Wand | T2   | Gold        | Fire         |
| Frost Wand      | Damage Wand | T2   | Iron        | Frost        |
| Arcane Staff    | Damage Staff| T2   | Gold        | Arcane       |
| Fire Staff      | Damage Staff| T2   | Gold        | Fire         |
| Frost Staff     | Damage Staff| T2   | Iron        | Frost        |
| NR Arcane Wand  | Damage Wand | T3   | Netherite   | Arcane       |
| NR Fire Wand    | Damage Wand | T3   | Netherite   | Fire         |
| NR Frost Wand   | Damage Wand | T3   | Netherite   | Frost        |
| NR Arcane Staff | Damage Staff| T3   | Netherite   | Arcane       |
| NR Fire Staff   | Damage Staff| T3   | Netherite   | Fire         |
| NR Frost Staff  | Damage Staff| T3   | Netherite   | Frost        |
| Ruby Fire Staff | Damage Staff| T4   | Ruby (BN)   | Fire         |
| Crystal Arcane  | Damage Staff| T4   | Aeternium (BE)| Arcane     |
| Smaragdant Frost| Damage Staff| T4   | Aeternium (BE)| Frost      |

### Rogues
| Weapon           | Type       | Tier | Material  |
|------------------|-----------|------|-----------|
| Flint Dagger     | Dagger    | T0   | Flint     |
| Iron Dagger      | Dagger    | T1   | Iron      |
| Diamond Dagger   | Dagger    | T2   | Diamond   |
| Netherite Dagger | Dagger    | T3   | Netherite |
| Iron Sickle      | Sickle    | T1   | Iron      |
| Diamond Sickle   | Sickle    | T2   | Diamond   |
| Netherite Sickle | Sickle    | T3   | Netherite |
| Stone DblAxe     | Double Axe| T0   | Stone     |
| Iron DblAxe      | Double Axe| T1   | Iron      |
| Diamond DblAxe   | Double Axe| T2   | Diamond   |
| Netherite DblAxe | Double Axe| T3   | Netherite |
| Iron Glaive      | Glaive    | T1   | Iron      |
| Diamond Glaive   | Glaive    | T2   | Diamond   |
| Netherite Glaive | Glaive    | T3   | Netherite |
| Ruby *           | Various   | T4   | Ruby (BN) |
| Aeternium *      | Various   | T4   | Aeternium (BE)|

### Paladins
| Weapon           | Type         | Tier | Material  |
|------------------|-------------|------|-----------|
| Stone Claymore   | Claymore    | T0   | Cobble    |
| Iron Claymore    | Claymore    | T1   | Iron      |
| Diamond Claymore | Claymore    | T2   | Diamond   |
| NR Claymore      | Claymore    | T3   | Netherite |
| Wooden Hammer    | Hammer      | Wood | Planks    |
| Stone Hammer     | Hammer      | T0   | Stone     |
| Iron Hammer      | Hammer      | T1   | Iron      |
| Diamond Hammer   | Hammer      | T2   | Diamond   |
| NR Hammer        | Hammer      | T3   | Netherite |
| Iron Mace        | Mace        | T1   | Iron      |
| Diamond Mace     | Mace        | T2   | Diamond   |
| NR Mace          | Mace        | T3   | Netherite |
| Acolyte Wand     | Heal Wand   | T0   | Stick     |
| Holy Wand        | Heal Wand   | T1   | Gold      |
| Diamond Holy Wand| Heal Wand   | T2   | Diamond   |
| NR Holy Wand     | Heal Wand   | T3   | Netherite |
| Holy Staff       | Heal Staff  | T1   | Gold      |
| Diamond Holy Staff| Heal Staff | T2   | Diamond   |
| NR Holy Staff    | Heal Staff  | T3   | Netherite |
| Ruby *           | Various     | T4   | Ruby (BN) |
| Aeternium *      | Various     | T4   | Aeternium (BE)|

## Spell Damage Coefficients

damage = base_spell_power * coefficient * (1 + armor_bonus)

| Spell              | Mod      | School  | Tier | Coeff | Cast   | CD  |
|--------------------|----------|---------|------|-------|--------|-----|
| arcane_bolt        | wizards  | arcane  | 0    | 0.7   | 1.0s   | -   |
| fire_scorch        | wizards  | fire    | 0    | 0.6   | 1.2s   | -   |
| fireball           | wizards  | fire    | 0    | 0.8   | 1.5s   | -   |
| frost_shard        | wizards  | frost   | 0    | 0.6   | 1.0s   | -   |
| arcane_blast       | wizards  | arcane  | 1    | 0.8   | 1.5s   | -   |
| fire_blast         | wizards  | fire    | 1    | 1.0   | 1.5s   | -   |
| frostbolt          | wizards  | frost   | 1    | 0.8   | 1.1s   | -   |
| arcane_missile     | wizards  | arcane  | 2    | 0.8   | 4.0s ch| 2s  |
| fire_breath        | wizards  | fire    | 2    | 0.9   | 5.0s ch| 10s |
| frost_nova         | wizards  | frost   | 2    | 0.5   | 0.5s   | 10s |
| arcane_beam        | wizards  | arcane  | 3    | 1.0   | 5.0s ch| 10s |
| fire_meteor        | wizards  | fire    | 3    | 1.0   | 1.0s   | 10s |
| frost_shield       | wizards  | frost   | 3    | -     | inst   | 30s |
| arcane_blink       | wizards  | arcane  | 4    | -     | inst   | 12s |
| fire_wall          | wizards  | fire    | 4    | 0.8   | inst   | 24s |
| frost_blizzard     | wizards  | frost   | 4    | 0.7   | 8.0s ch| 16s |
| heal               | paladins | healing | 0    | -     | 1.0s   | 4s  |
| holy_shock         | paladins | healing | 1    | 0.8/0.4| 1.5s  | -   |
| divine_protection  | paladins | healing | 2    | -     | inst   | 30s |
| flash_heal         | paladins | healing | 2    | -     | 0.5s   | 6s  |
| holy_beam          | paladins | healing | 2    | 0.8/0.4| 5.0s ch| 10s|
| circle_of_healing  | paladins | healing | 3    | -     | 0.5s   | 10s |
| judgement          | paladins | melee   | 3    | 0.9   | 0.5s   | 15s |
| barrier            | paladins | healing | 4    | -     | 0.5s   | 40s |
| battle_banner      | paladins | healing | 4    | -     | inst   | 45s |
| throw              | rogues   | melee   | 2    | 1.0   | 0.5s   | 8s  |
| shout              | rogues   | melee   | 3    | 0.1   | inst   | 12s |

> Heal coefficient: heal = 0.5 (heal), 1.2 (flash_heal), 0.4 (holy_shock/holy_beam/circle)

## Tier Mapping a Chapters del Modpack

| RPG Tier | Material     | Nuestro Chapter | Nota |
|----------|-------------|-----------------|------|
| T0       | Stone/Stick  | Ch1-2           | Starter weapons |
| T1       | Iron         | Ch2             | First real RPG gear |
| T2       | Diamond/Gold | Ch3-4           | Class specialization |
| T3       | Netherite    | Ch5-6           | Nether progression |
| T4       | Ruby/Aeternium| Ch7-8          | Custom recipes (no BetterEnd/BetterNether) |

## Notas para Balance
- Staves/Wands compensan bajo daño melee con alto spell power
- Warrior/Berserker armor tiene la mejor proteccion pero 0 spell power
- Paladin armor tiene proteccion comparable a Diamond/Netherite vanilla + healing
- Wizard robes son como Leather en proteccion pero con enorme spell power
- Rogue armor es medium con evasion y haste como defensa
- T4 weapons requieren materiales de BetterEnd/BetterNether que NO tenemos → custom KubeJS recipes
- Golden variants existen pero son novelty (baja durabilidad)
