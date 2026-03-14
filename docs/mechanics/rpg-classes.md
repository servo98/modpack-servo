# Sistema RPG (Clases y Combate)

> Fuente: GDD v2, seccion 3.13
> Mods: RPG Series by Daedelus (Spell Engine, Spell Power, Wizards, Rogues, Paladins, Skill Tree, MRPGC Skill Tree, Runes, Jewelry, Ranged Weapon API)
> Relacionado: [Jewelry](jewelry.md), [Runes](runes.md), [Accessories](accessories.md), [Enchantments](enchantments.md), [Dungeons](dungeons.md)
> Datos detallados: docs/mod-data/rpg-series-content.md, docs/balance/rpg-weapon-stats.md, docs/balance/combat-scaling.md

## Overview

Tercer pilar del modpack. 7 clases, 8 especializaciones, 4 tiers de gear. 367 items, ~164 spells.

## Clases

| Clase | Mod | Arma principal | Armor base → T2 | Spells clave |
|-------|-----|----------------|-----------------|--------------|
| Wizard (Arcane) | wizards | Staff/Wand Arcane | Wizard Robes → Arcane Robes | Arcane Blast, Bolt, Beam, Missiles, Blink |
| Wizard (Fire) | wizards | Staff/Wand Fire | Wizard Robes → Fire Robes | Fireball, Pyroblast, Fire Breath, Meteor, Wall of Flames |
| Wizard (Frost) | wizards | Staff/Wand Frost | Wizard Robes → Frost Robes | Frost Shard, Frostbolt, Nova, Shield, Blizzard |
| Rogue | rogues | Dagger, Sickle | Rogue set → Assassin set | Shadowstep, Vanish, Shock Powder, Fan of Knives |
| Warrior | rogues | Double Axe, Glaive | Warrior set → Berserker set | Charge, Whirlwind, Demoralizing Shout, Shattering Throw |
| Paladin | paladins | Claymore, Hammer, Mace, Shield | Paladin set → Crusader set | Judgement, Divine Protection, Battle Banner, Barrier |
| Priest | paladins | Holy Staff, Holy Wand | Priest set → Prior set | Heal, Flash Heal, Holy Shock, Circle of Healing |

## Especializaciones (Skill Tree, Ch4+)

| Especializacion | Base | Estilo |
|-----------------|------|--------|
| Berserker | Warrior | Rage y sangrado melee |
| Deadeye | Rogue | Venenos y trampas |
| War Archer | Fire Wizard | Arquero de fuego |
| Tundra Hunter | Frost Wizard | Arquero de hielo |
| Forcemaster | Arcane Wizard | Punetazos arcanos melee |
| Air | Cualquiera | Viento, knockback, tornados |
| Earth | Cualquiera | Tierra, armadura, terremotos |
| Water | Cualquiera | Agua, healing, cleanse |

## Tier Progression

| Tier | Material | Crafteo | Capitulo |
|------|----------|---------|----------|
| 0 | Flint, Stone, Wood | Shaped basico | Ch1 |
| 1 | Iron, Gold, Leather, Wool | Shaped | Ch2 (melee), Ch3 (magico) |
| 2 | Diamond, Ender Pearl, Blaze Powder, Prismarine | Shaped | Ch3 (melee), Ch4 (magico) |
| 3 | Netherite Ingot + Smithing Template | Smithing Transform | Ch6 |
| 4 | Cristal del Nucleo / Rubi Infernal (boss drops) | KubeJS custom | Ch8 |

**Nota**: T4 (Aeternium/Ruby) NO crafteable con materiales vanilla. Requiere drops de boss/dungeon.

## Desbloqueo por capitulo

| Cap | Clases | Tier | Notas |
|-----|--------|------|-------|
| 1 | Ninguna (ataques genericos) | T0 | Spell Binding Table como quest |
| 2 | Rogue, Warrior | T1 melee | Melee primero (materiales accesibles) |
| 3 | + Wizard, Paladin, Priest | T1 magico + T2 melee | Clases magicas + Diamond melee gear |
| 4 | Todas + Skill Tree | T2 magico | Elegir especializacion + Diamond staves/robes |
| 5 | Todas | T2+ enchants | Enchants magicos, pociones Spell Power |
| 6 | Todas | T3 | Netherite upgrades |
| 7 | Todas | T3+ uniques | Unique jewelry coleccionables |
| 8 | Todas (maxeadas) | T4 custom | Armas legendarias |

## NPC Merchants

| NPC | Mod | Vende | Spawn |
|-----|-----|-------|-------|
| Wizard Merchant | wizards | Scrolls, robes basicas | Villages (Ch3+) |
| Arms Dealer | rogues | Armas melee, armor basica | Villages (Ch2+) |
| Monk | paladins | Armas holy, paladin gear | Villages (Ch3+) |
| Jeweler | jewelry | Gemas y jewelry basica | Villages (Ch3+) |

## Workstations RPG

| Bloque | Mod | Funcion | Capitulo |
|--------|-----|---------|----------|
| Spell Binding Table | spell_engine | Vincular spells a class books | Ch1 (quest) |
| Arms Station | rogues | Workstation Arms Dealer | Ch2 |
| Monk Workbench | paladins | Workstation Monk | Ch3 |
| Jeweler's Kit | jewelry | Workstation Jeweler | Ch3 |
| Rune Crafting Altar | runes | Crafteo eficiente runas (x8/x16) | Ch3 |

## Rebalanceo KubeJS

- Berserker armor T2: reemplazar netherite_scrap por chain+iron
- Tier 4: reemplazar Aeternium/Ruby (requieren BetterEnd/BetterNether) con materiales custom
- Rune Pouches: configurar para NO gastar runas

## Equipo completo de un jugador

```
ARMOR (RPG Series):  Helmet + Chest + Legs + Boots
MANOS:               Arma + Offhand (shield/spell book)
CURIOS (Jewelry):    Ring x2 + Necklace x1
CURIOS (Custom):     Belt + Back + Feet + Head
RUNES:               Rune Pouch (en Curios slot)
TOTAL:               ~14 piezas (build tipo MMO)
```
