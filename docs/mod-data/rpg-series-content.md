# RPG Series - Contenido Completo y Distribución por Capítulo

## Resumen de Mods (11 JARs)
| Mod | Items | Spells | Recipes | Rol |
|-----|-------|--------|---------|-----|
| Wizards | 55 | 15 | 125 | Clases mago (Arcane/Fire/Frost) |
| Rogues & Warriors | 60 | 10 | 125 | Clases melee (Rogue/Warrior) |
| Paladins & Priests | 71 | 9 | 140 | Clases tanque/healer |
| Spell Engine | 0 | 10 | 1 | Framework + ataques genéricos |
| Spell Power | 76 | 0 | 0 | Pociones/flechas/enchantments de spell power |
| More RPG Classes Skill Tree | 0 | ~120 | 0 | 8 árboles de especialización |
| Runes | 10 | 0 | 20 | Sistema de runas para casteo |
| Jewelry | 85 | 0 | 87 | Gemas, anillos, collares, ores custom |
| Ranged Weapon API | 10 | 0 | 0 | Pociones/flechas de ranged buffs |
| Skill Tree | 0 | 0 | 0 | UI del árbol de habilidades |
| **TOTAL** | **367** | **~164** | **498** | |

---

## 6 Clases Base + Especialización

### Clase 1: Wizard (Arcane)
- Arma: Staff/Wand Arcane
- Armadura: Arcane Robes (4pc)
- Libro: Tome of Arcane
- Spells: Arcane Blast, Arcane Bolt, Arcane Beam, Arcane Missiles, Blink
- Materiales: Ender Pearl, Amethyst, Gold, Wool
- Especialización (Skill Tree): **Forcemaster** (puñetazos arcanos melee)

### Clase 2: Wizard (Fire)
- Arma: Staff/Wand Fire
- Armadura: Fire Robes (4pc)
- Libro: Tome of Fire
- Spells: Fireball, Pyroblast, Fire Breath, Meteor, Wall of Flames, Scorch
- Materiales: Blaze Powder, Gunpowder, Nether Brick, Gold, Wool
- Especialización (Skill Tree): **War Archer** (arquero de fuego)

### Clase 3: Wizard (Frost)
- Arma: Staff/Wand Frost
- Armadura: Frost Robes (4pc)
- Libro: Tome of Frost
- Spells: Frost Shard, Frostbolt, Frost Nova, Frost Shield, Blizzard
- Materiales: Prismarine, Snowball, Iron, Wool
- Especialización (Skill Tree): **Tundra Hunter** (arquero de hielo)

### Clase 4: Rogue
- Arma: Dagger, Sickle
- Armadura: Rogue (4pc) → Assassin (4pc)
- Libro: Rogue Handbook
- Spells: Shadowstep, Vanish, Shock Powder, Fan of Knives
- Materiales: Leather, Rabbit Hide, Ink Sac, Gold
- Especialización (Skill Tree): **Deadeye** (venenos y trampas)

### Clase 5: Warrior
- Arma: Double Axe, Glaive
- Armadura: Warrior (4pc) → Berserker (4pc)
- Libro: Warriors' Codex
- Spells: Charge, Whirlwind, Demoralizing Shout, Slice & Dice, Shattering Throw
- Materiales: Iron, Leather, Chain, String, Netherite Scrap (Berserker)
- Especialización (Skill Tree): **Berserker** (rage y sangrado)

### Clase 6: Paladin
- Arma: Claymore, Great Hammer, Mace, Kite Shield
- Armadura: Paladin (4pc) → Crusader (4pc)
- Libro: Paladin Libram
- Spells: Judgement, Divine Protection, Battle Banner, Barrier
- Materiales: Iron, Copper, Gold, Ghast Tear (Crusader)
- Especialización (Skill Tree): N/A (inherente)

### Clase 7: Priest (subclase de Paladins mod)
- Arma: Holy Staff, Holy Wand, Acolyte Wand
- Armadura: Priest (4pc) → Prior (4pc)
- Libro: Holy Book
- Spells: Heal, Flash Heal, Holy Shock, Circle of Healing, Holy Light
- Materiales: Wool, Chain, Gold, Ghast Tear (Prior)
- Especialización (Skill Tree): **Water** (healing) o **Earth** (defensa)

### Especialización Extra (Skill Tree)
- **Air**: Magia de viento, knockback, tornados
- **Earth**: Magia de tierra, armadura, terremotos
- **Water**: Magia de agua, healing, cleanse

---

## Sistema de Tiers de Equipo

| Tier | Armas | Armadura | Acceso |
|------|-------|----------|--------|
| 0 | Flint Dagger, Stick Staff, Novice Wand | Ninguna especial | Ch1 |
| 1 | Iron/Stone weapons, Wizard Staff | Wizard Robes, Rogue set, Warrior set, Paladin set, Priest set | Ch2-3 |
| 2 | Diamond/Gold weapons, Elemental Staffs | Arcane/Fire/Frost Robes, Assassin, Berserker, Crusader, Prior | Ch4-5 |
| 3 | Netherite weapons (smithing upgrade) | Netherite variants de todo | Ch6-7 |
| 4 | Aeternium/Ruby — **recetas custom KubeJS** (BetterEnd/BetterNether no instalados) | Aeternium/Ruby variants | Ch8 (custom) |

### PROBLEMA: Tier 4 requiere BetterEnd/BetterNether
- Aeternium Ingot → betterend mod (no instalado)
- Nether Ruby → betternether mod (no instalado)
- **SOLUCIÓN**: Crear recetas custom con KubeJS usando materiales de dungeon

---

## Materiales Requeridos por Tier

### Tier 0 (Primitivo) - Disponible desde Ch1
- Stick, Coal, Flint, Stone, Wood

### Tier 1 (Básico) - Ch2-3
- Iron Ingot, Gold Ingot, Copper Ingot
- Leather, Wool, String, Lapis Lazuli, Quartz, Paper
- Red Dye, Ink Sac

### Tier 2 (Especializado) - Ch4-5
- Diamond, Chain
- Ender Pearl, Amethyst Shard (Arcane)
- Blaze Powder, Gunpowder, Nether Brick (Fire) ← REQUIERE NETHER
- Prismarine Shard/Crystals, Snowball (Frost)
- Rabbit Hide (Assassin)
- Goat Horn (Berserker helmet) ← Screamer goats
- Netherite Scrap (Berserker body) ← CARO para tier 2, considerar rebalanceo
- Ghast Tear (Crusader/Prior) ← REQUIERE NETHER
- Jewelry gems: Ruby, Sapphire, etc. (custom ore)

### Tier 3 (Netherite) - Ch6-7
- Netherite Ingot + Smithing Template
- Todo lo anterior

### Tier 4 (Custom Endgame) - Ch8
- Materiales de dungeon (custom via KubeJS)
- Reemplazar Aeternium/Ruby con drops exclusivos

### Consumibles (Runas)
- Arcane: Amethyst → Ender Pearl
- Fire: Gunpowder/Coal → Blaze Powder
- Frost: Snowball → Ice/Packed Ice/Blue Ice
- Healing: Gold Nugget → Honeycomb/Gold Ingot
- Rune Pouches: Wool + String + Ink Sac/Phantom Membrane/Nether Star

---

## Contenido Especial

### NPC Merchants (3 tipos, spawn natural en villages)
1. **Wizard Merchant** → Vende scrolls, robes básicas
2. **Arms Dealer** → Vende armas melee, armor básica
3. **Monk** → Vende armas holy, paladin gear
4. **Jeweler** → Vende gems y jewelry básica

### Workbenches (4 bloques)
1. **Spell Binding Table** (Spell Engine) → Vincular spells a libros
2. **Arms Station** (Rogues) → NPC workstation
3. **Monk Workbench** (Paladins) → NPC workstation
4. **Jeweler's Kit** (Jewelry) → NPC workstation
5. **Rune Crafting Altar** (Runes) → Crafteo eficiente de runas

### Unique Jewelry (LOOT ONLY - ~20 items)
Items únicos con nombres y lore, sin recetas de crafteo:
- **Arcane**: Amulet of Unfettered Magics, Ring of Omnipotence, Pendant of Acumen
- **Fire**: Pendant of Sunfire, Sunflare Signet
- **Frost**: Glacial Shard, Permafrost Stone
- **Holy**: Adria's Pendant, Anett's Embrace
- **Lightning**: Amulet of Unrelenting Storms, Ring of Unstable Currents
- **Shadow/Soul**: Occult Necklace, Signet of Captured Souls
- **Physical/Tank**: Sentinel's Amulet, Juggernaut Band
- **Rogue**: Angelista's Revenge, Chocker of Vile Intent
- **Combat**: Ring of Fury, Necklace of Anguish, Ring of Suffering, Amulet of Torture
- **Ranger**: Darnassian Strider Amulet, Thalassian Ranger Band
→ PERFECTOS para drops de dungeon y gacha

### Ore Generation (Jewelry)
- Gem Vein (overworld, nivel normal)
- Deepslate Gem Vein (overworld, deep)
- Drops: 6 gemas (Citrine, Jade, Ruby, Sapphire, Tanzanite, Topaz)

### Enchantments (Spell Power)
7 enchantments mágicos nuevos:
1. Spell Power (genérico)
2. Sunfire (arcane + fire)
3. Soulfrost (soul + frost)
4. Energize (healing + lightning)
5. Spell Haste
6. Spell Volatility (crit chance)
7. Amplify Spell (crit damage)
8. Magic Protection

---

## DISTRIBUCIÓN POR CAPÍTULO PROPUESTA

### Capítulo 1: Raíces (Survival + Primeros pasos)
**RPG disponible:**
- Spell Binding Table (desbloquear sistema de clases)
- Tier 0 weapons: Flint Dagger, Stone weapons, Novice Wand, Acolyte Wand
- Jewelry: Copper/Iron Ring (sin stats, introductorio)
- Small Rune Pouch + Arcane/Frost runes básicas (small)
- NO class books todavía → solo ataques genéricos del Spell Engine (Cleave, Thrust, etc.)
- Quest: "Encuentra la Spell Binding Table"

### Capítulo 2: La Cocina del Mundo
**RPG disponible:**
- Class books Tier 1: Rogue Handbook, Warriors' Codex (melee primero)
- Tier 1 armor: Rogue set, Warrior set
- Tier 1 weapons: Iron Dagger/Sickle/Double Axe/Glaive
- Jewelry básica: Gold Ring, gem rings básicos (Citrine, Jade)
- Fire/Healing runes pequeñas
- Arms Station crafteable
- Quest: "Elige tu clase melee" + "Crafteaun arma de tu clase"

### Capítulo 3: Engranajes
**RPG disponible:**
- Class books Tier 1 mágicos: Tome of Arcane, Tome of Fire, Tome of Frost
- Tier 1 armor mágica: Wizard Robes
- Tier 1 weapons mágicas: Wizard Staff, wands
- Paladin Libram + Holy Book (classes support)
- Tier 1 paladin/priest armor: Paladin set, Priest set
- Tier 1 paladin weapons: Iron Claymore/Great Hammer/Mace/Shield
- Jewelry: Diamond/Emerald necklaces, más gem rings
- Rune Crafting Altar
- Medium Rune Pouch
- Quest: "Elige tu clase mágica" + "Vincula tus primeros spells"

### Capítulo 4: Locomotoras (Create Trains + Nether)
**RPG disponible:**
- Tier 2 melee: Diamond weapons, Assassin armor, Berserker armor
- Tier 2 mágico: Elemental Robes (Arcane/Fire/Frost), Elemental Staffs/Wands
- Tier 2 paladin: Diamond weapons, Crusader armor (ghast tear)
- Tier 2 priest: Prior vestments, Diamond Holy Staff/Wand
- Jewelry: Ruby/Sapphire rings y necklaces
- Medium runes (todas)
- Skill Tree se desbloquea → elegir primera especialización
- Quest: "Especialízate" + "Consigue tu Tier 2 completo"

### Capítulo 5: Cerebro Digital (Refined Storage)
**RPG disponible:**
- Jewelry: Tanzanite/Topaz rings y necklaces
- Netherite jewelry (gem + netherite)
- Enchantments mágicos disponibles (Spell Power, Sunfire, etc.)
- Pociones de Spell Power (via brewing)
- Soul/Lightning runes
- Large Rune Pouch (Nether Star)
- Quest: "Encanta tu equipo mágico" + "Brewea pociones de poder"

### Capítulo 6: Cosecha Maestra
**RPG disponible:**
- Tier 3: Netherite upgrades para TODO el equipo RPG
- Smithing templates disponibles
- Unique jewelry empieza a aparecer en dungeons (drop rate bajo)
- Quest: "Mejora tu equipo a Netherite"

### Capítulo 7: Las Profundidades
**RPG disponible:**
- Unique jewelry como drops de dungeon (20 items únicos)
- Flechas de Spell Power (tipped arrows)
- Quest: "Colecciona 5 joyas únicas de las profundidades"

### Capítulo 8: El Devorador
**RPG disponible:**
- Tier 4 custom: Armas/armaduras endgame con materiales de dungeon
  - Reemplazar Aeternium con "Cristal del Núcleo" (drop de Ch7 boss)
  - Reemplazar Ruby con "Rubí Infernal" (drop de Ch6 boss)
- Unique jewelry legendarias (Ring of Omnipotence, etc.)
- Todas las especializaciones maxeadas
- Quest: "Craftea tu arma legendaria" + "Domina tu clase"

---

## CONFLICTOS Y DECISIONES PENDIENTES

### 1. Jewelry vs Curios Custom (OVERLAP)
El mod Jewelry agrega rings y necklaces que usan Curios slots.
Nuestro plan original era 54 accesorios custom via Curios API.
**Opciones:**
- A) Usar Jewelry como base y agregar solo los accesorios que falten
- B) Reemplazar Jewelry con nuestros custom completos
- C) Combinar: Jewelry para rings/necklaces, custom para otros slots (belt, back, feet, head)
**RECOMENDACIÓN**: Opción C. Jewelry ya tiene 85 items con sistema de tiers y ores. Nuestros custom se enfocan en otros slots.

### 2. Tier 4 Aeternium/Ruby no crafteable
Sin BetterEnd/BetterNether, los items Tier 4 no tienen receta.
**Solución**: KubeJS custom recipes usando materiales de dungeon/boss.

### 3. Berserker Netherite Scrap en Tier 2
La armadura Berserker usa netherite_scrap en la receta shaped (no smithing).
Es muy caro para un tier 2. **Solución**: KubeJS rebalanceo.

### 4. Ghast Tear para Crusader/Prior (Tier 2)
Requiere ir al Nether. Si Nether se desbloquea en Ch4, las clases Crusader/Prior no están disponibles hasta entonces.
**Esto está bien** - los jugadores usan Paladin/Priest set básico hasta Ch4.

### 5. Runas como consumible
Las runas se gastan al castear spells. Esto puede ser tedioso.
**Opciones:** Configurar el mod para que runas no se gasten, o que se gasten lento, o que las rune pouches sean infinitas.
**RECOMENDACIÓN**: Rune pouches no gastan runas (configurar), craftear la pouch es el gate.
