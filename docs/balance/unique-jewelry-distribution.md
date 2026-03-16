# Unique Jewelry: Distribucion por Tier de Dungeon

> Issue: [#25](https://github.com/servo98/modpack-servo/issues/25)
> Relacionado: [dungeons.md](../mechanics/dungeons.md), [jewelry.md](../mechanics/jewelry.md), [accessories.md](accessories.md), [gacha-rates.md](gacha-rates.md), [combat-scaling.md](combat-scaling.md)

---

## 1. Resumen del sistema

24 unique jewelry (12 necklaces + 12 rings) son items loot-only que dropean en dungeons, gacha y bosses. Este documento define **que items estan en el pool de cada tier de dungeon** y con que probabilidad.

### Principios de distribucion

1. **Progresion por poder**: items defensivos/soporte en tiers bajos, ofensivos/especializados en tiers altos
2. **Cobertura de clases**: cada tier tiene items utiles para al menos 4 de las 7 clases
3. **Motivacion de re-runs**: los items mas deseados requieren tiers altos, pero tiers bajos no son "vacios"
4. **Multi-tier con pesos**: algunos items aparecen en multiples tiers con drop rates distintos (raro en tier bajo, comun en tier alto)

### Fuentes de drop (recordatorio de dungeons.md)

| Tier | Fuente | Chance base | Capitulo |
|------|--------|-------------|----------|
| Basica | - | No dropean unique jewelry | Ch1+ |
| Avanzada | Cofre final | 5% | Ch3+ |
| Maestra | Cofre final | 15% | Ch5+ |
| Del Nucleo | Boss dungeon | **Garantizado 1** | Ch7+ |
| Del Nucleo | Cofres raros | +20% adicional | Ch7+ |

---

## 2. Mapa de clases y escuelas

Para entender a quien beneficia cada jewelry:

| Escuela | Clase(s) principal(es) | Stat principal | Notas |
|---------|----------------------|----------------|-------|
| Arcane | Wizard (Arcane), Forcemaster | Spell power (arcane) | Burst DPS magico |
| Fire | Wizard (Fire), War Archer | Spell power (fire) | AoE DPS magico |
| Frost | Wizard (Frost), Tundra Hunter | Spell power (frost) | Sustained DPS, CC |
| Healing | Priest, Paladin | Healing power | Soporte/tank |
| Lightning | Wizard (cualquiera), Air spec | Spell power (lightning) | DPS magico alternativo |
| Soul | Wizard (cualquiera) | Spell power (soul) | DPS magico oscuro |
| Spell | Todas las magicas (Wizard, Priest, Paladin) | Spell power (general) | Boost universal de magia |
| Attack | Warrior, Rogue, Paladin | Melee damage | Boost universal melee |
| Crit | Rogue, Warrior (Berserker) | Crit chance/damage | Scaling multiplicativo |
| Dex/Rogue | Rogue, Deadeye | Haste/evasion | Especializacion rogue |
| Tank | Warrior, Paladin | Armor/HP/resistance | Supervivencia |
| Archer | War Archer, Tundra Hunter, Deadeye | Ranged damage | Especializacion ranged |

---

## 3. Asignacion de tier principal

Cada item tiene un **tier principal** (donde aparece por primera vez o donde es mas probable). Items defensivos/genericos aparecen antes; items ofensivos/especializados aparecen despues.

### Tier 2 — Avanzada (3 sets = 6 items)

Items de soporte y supervivencia. Ideales para las primeras runs serias (Ch3-4).

| Registry ID | Nombre display | Tipo | Clases que beneficia | Justificacion |
|-------------|---------------|------|---------------------|---------------|
| `unique_healing_necklace` | Adria's Pendant | Necklace | Priest, Paladin | Healing es la primera necesidad en grupo. Soporte accesible temprano. |
| `unique_healing_ring` | Anett's Embrace | Ring | Priest, Paladin | Complemento de healing. Incentiva rol de soporte temprano. |
| `unique_tank_necklace` | Sentinel's Amulet | Necklace | Warrior, Paladin | Supervivencia basica. Todo jugador quiere mas vida/armor. |
| `unique_tank_ring` | Juggernaut Band | Ring | Warrior, Paladin | Complemento defensivo. Fundamental para tanquear champions. |
| `unique_frost_necklace` | Glacial Shard | Necklace | Wizard (Frost), Tundra Hunter | Frost es la escuela con mejor sustained DPS y CC. Accesible para el primer wizard que llega a dungeons. |
| `unique_frost_ring` | Permafrost Stone | Ring | Wizard (Frost), Tundra Hunter | Complemento de frost. Slow/CC es util para todo el grupo. |

**Cobertura de clases Tier 2**: Priest, Paladin, Warrior, Wizard (Frost) = **4 de 7 clases**.

### Tier 3 — Maestra (5 sets = 10 items)

Items ofensivos medios y especializaciones. Recompensa el compromiso con una clase (Ch5-6).

| Registry ID | Nombre display | Tipo | Clases que beneficia | Justificacion |
|-------------|---------------|------|---------------------|---------------|
| `unique_fire_necklace` | Pendant of Sunfire | Necklace | Wizard (Fire), War Archer | Fire es AoE y burst. Recompensa mid-game para magos ofensivos. |
| `unique_fire_ring` | Sunflare Signet | Ring | Wizard (Fire), War Archer | Complemento fire. Meteor + este ring = burst devastador. |
| `unique_arcane_necklace` | Amulet of Unfettered Magics | Necklace | Wizard (Arcane), Forcemaster | Arcane beam/missiles escalan mucho con spell power. Buen tier 3. |
| `unique_arcane_ring` | Ring of Subjugation | Ring | Wizard (Arcane), Forcemaster | Complemento arcane. Forcemaster melee-mage lo aprovecha al maximo. |
| `unique_attack_necklace` | Amulet of Torture | Necklace | Warrior, Rogue, Paladin | Primer boost puro de dano melee. Muy buscado por todas las clases melee. |
| `unique_attack_ring` | Ring of Fury | Ring | Warrior, Rogue, Paladin | Complemento de ATK. Stacks con armor bonuses de Berserker/Assassin. |
| `unique_lightning_necklace` | Amulet of Unrelenting Storms | Necklace | Wizard (cualquiera), Air spec | Lightning es escuela alternativa. Diversifica builds. |
| `unique_lightning_ring` | Ring of Unstable Currents | Ring | Wizard (cualquiera), Air spec | Complemento lightning. Combina con Air spec para knockback builds. |
| `unique_dex_necklace` | Chocker of Vile Intent | Necklace | Rogue, Deadeye | Haste y evasion. El Rogue ya tiene T2 armor en Ch5, ahora escala. |
| `unique_dex_ring` | Angelista's Revenge | Ring | Rogue, Deadeye | Complemento rogue. Fan of Knives + haste = clear de salas rapido. |

**Cobertura de clases Tier 3**: Wizard (Fire, Arcane, Lightning/Air), Warrior, Rogue, Paladin = **6 de 7 clases** (Priest obtiene healing de T2).

### Tier 4 — Del Nucleo (4 sets = 8 items)

Items mas poderosos y especializados. Endgame puro (Ch7-8). Drop garantizado del boss.

| Registry ID | Nombre display | Tipo | Clases que beneficia | Justificacion |
|-------------|---------------|------|---------------------|---------------|
| `unique_spell_necklace` | Pendant of Acumen | Necklace | Todas las magicas | Spell power general = el item mas versatil de magia. Solo endgame. |
| `unique_spell_ring` | Ring of Omnipotence | Ring | Todas las magicas | Complemento spell general. Con este + escuela especifica = DPS tope. |
| `unique_crit_necklace` | Necklace of Anguish | Necklace | Rogue, Warrior (Berserker) | Crit es scaling multiplicativo. Solo vale la pena con gear T3+. |
| `unique_crit_ring` | Ring of Suffering | Ring | Rogue, Warrior (Berserker) | Complemento crit. Berserker con crit damage de armor + esto = monstruo. |
| `unique_soul_necklace` | Occult Necklace | Necklace | Wizard (cualquiera) | Soul es la escuela mas oscura/rara. Tematica endgame perfecta. |
| `unique_soul_ring` | Signet of Captured Souls | Ring | Wizard (cualquiera) | Complemento soul. Rareza y tematica encajan con Del Nucleo. |
| `unique_archer_necklace` | Darnassian Strider Amulet | Necklace | War Archer, Tundra Hunter, Deadeye | Arquero es spec avanzada (Ch4+). Jewelry endgame para spec completa. |
| `unique_archer_ring` | Thalassian Ranger Band | Ring | War Archer, Tundra Hunter, Deadeye | Complemento archer. Requiere War Archer/Tundra Hunter para maximo provecho. |

**Cobertura de clases Tier 4**: Todas las magicas (Spell), Rogue, Warrior (Crit), Archer specs = **7 de 7 clases** con algo que perseguir.

---

## 4. Sistema multi-tier (drop rates por tier)

Los items NO estan exclusivamente bloqueados a un tier. Aparecen en su tier principal y en tiers superiores con rates mas altos. Esto significa que un item de Tier 2 tambien puede dropear en Tier 3 y 4.

### Tabla completa de drop rates

Cuando el jugador obtiene un unique jewelry (sea por el 5% de Avanzada, 15% de Maestra, o el garantizado de Del Nucleo), el juego elige un item del pool con los siguientes pesos:

#### Pool Tier 2 — Avanzada (6 items en pool)

| Registry ID | Nombre | Peso | Prob. por drop |
|-------------|--------|------|----------------|
| `unique_healing_necklace` | Adria's Pendant | 20 | 16.7% |
| `unique_healing_ring` | Anett's Embrace | 20 | 16.7% |
| `unique_tank_necklace` | Sentinel's Amulet | 20 | 16.7% |
| `unique_tank_ring` | Juggernaut Band | 20 | 16.7% |
| `unique_frost_necklace` | Glacial Shard | 20 | 16.7% |
| `unique_frost_ring` | Permafrost Stone | 20 | 16.7% |
| **Total** | | **120** | **100%** |

> Chance de obtener **cualquier** unique en Avanzada: 5% por run. Con 6 items equiprobables, cada item individual tiene ~0.83% por run.

#### Pool Tier 3 — Maestra (16 items en pool)

Incluye los 6 de Tier 2 (con peso reducido) + 10 nuevos de Tier 3.

| Registry ID | Nombre | Peso | Prob. por drop | Origen |
|-------------|--------|------|----------------|--------|
| `unique_healing_necklace` | Adria's Pendant | 8 | 3.6% | T2 (reducido) |
| `unique_healing_ring` | Anett's Embrace | 8 | 3.6% | T2 (reducido) |
| `unique_tank_necklace` | Sentinel's Amulet | 8 | 3.6% | T2 (reducido) |
| `unique_tank_ring` | Juggernaut Band | 8 | 3.6% | T2 (reducido) |
| `unique_frost_necklace` | Glacial Shard | 8 | 3.6% | T2 (reducido) |
| `unique_frost_ring` | Permafrost Stone | 8 | 3.6% | T2 (reducido) |
| `unique_fire_necklace` | Pendant of Sunfire | 18 | 8.1% | T3 (nuevo) |
| `unique_fire_ring` | Sunflare Signet | 18 | 8.1% | T3 (nuevo) |
| `unique_arcane_necklace` | Amulet of Unfettered Magics | 18 | 8.1% | T3 (nuevo) |
| `unique_arcane_ring` | Ring of Subjugation | 18 | 8.1% | T3 (nuevo) |
| `unique_attack_necklace` | Amulet of Torture | 18 | 8.1% | T3 (nuevo) |
| `unique_attack_ring` | Ring of Fury | 18 | 8.1% | T3 (nuevo) |
| `unique_lightning_necklace` | Amulet of Unrelenting Storms | 18 | 8.1% | T3 (nuevo) |
| `unique_lightning_ring` | Ring of Unstable Currents | 18 | 8.1% | T3 (nuevo) |
| `unique_dex_necklace` | Chocker of Vile Intent | 18 | 8.1% | T3 (nuevo) |
| `unique_dex_ring` | Angelista's Revenge | 18 | 8.1% | T3 (nuevo) |
| **Total** | | **228** | **100%** |

> Chance de obtener **cualquier** unique en Maestra: 15% por run. Items T3 (~8.1% cada uno) son ~2.2x mas probables que items T2 (~3.6% cada uno) dentro del mismo drop.

#### Pool Tier 4 — Del Nucleo (24 items en pool, drop garantizado)

Incluye todos los items anteriores (con pesos ajustados) + 8 exclusivos de Tier 4.

| Registry ID | Nombre | Peso | Prob. por drop | Origen |
|-------------|--------|------|----------------|--------|
| `unique_healing_necklace` | Adria's Pendant | 4 | 1.4% | T2 (minimo) |
| `unique_healing_ring` | Anett's Embrace | 4 | 1.4% | T2 (minimo) |
| `unique_tank_necklace` | Sentinel's Amulet | 4 | 1.4% | T2 (minimo) |
| `unique_tank_ring` | Juggernaut Band | 4 | 1.4% | T2 (minimo) |
| `unique_frost_necklace` | Glacial Shard | 4 | 1.4% | T2 (minimo) |
| `unique_frost_ring` | Permafrost Stone | 4 | 1.4% | T2 (minimo) |
| `unique_fire_necklace` | Pendant of Sunfire | 10 | 3.5% | T3 (reducido) |
| `unique_fire_ring` | Sunflare Signet | 10 | 3.5% | T3 (reducido) |
| `unique_arcane_necklace` | Amulet of Unfettered Magics | 10 | 3.5% | T3 (reducido) |
| `unique_arcane_ring` | Ring of Subjugation | 10 | 3.5% | T3 (reducido) |
| `unique_attack_necklace` | Amulet of Torture | 10 | 3.5% | T3 (reducido) |
| `unique_attack_ring` | Ring of Fury | 10 | 3.5% | T3 (reducido) |
| `unique_lightning_necklace` | Amulet of Unrelenting Storms | 10 | 3.5% | T3 (reducido) |
| `unique_lightning_ring` | Ring of Unstable Currents | 10 | 3.5% | T3 (reducido) |
| `unique_dex_necklace` | Chocker of Vile Intent | 10 | 3.5% | T3 (reducido) |
| `unique_dex_ring` | Angelista's Revenge | 10 | 3.5% | T3 (reducido) |
| `unique_spell_necklace` | Pendant of Acumen | 22 | 7.7% | **T4 (nuevo)** |
| `unique_spell_ring` | Ring of Omnipotence | 22 | 7.7% | **T4 (nuevo)** |
| `unique_crit_necklace` | Necklace of Anguish | 22 | 7.7% | **T4 (nuevo)** |
| `unique_crit_ring` | Ring of Suffering | 22 | 7.7% | **T4 (nuevo)** |
| `unique_soul_necklace` | Occult Necklace | 22 | 7.7% | **T4 (nuevo)** |
| `unique_soul_ring` | Signet of Captured Souls | 22 | 7.7% | **T4 (nuevo)** |
| `unique_archer_necklace` | Darnassian Strider Amulet | 22 | 7.7% | **T4 (nuevo)** |
| `unique_archer_ring` | Thalassian Ranger Band | 22 | 7.7% | **T4 (nuevo)** |
| **Total** | | **284** | **100%** |

> El boss de Del Nucleo **garantiza 1 unique**. Items T4 (~7.7% cada uno) son los mas probables. El 20% adicional de cofres raros usa el mismo pool.

---

## 5. Runs estimados para coleccion completa

### Por tier individual

| Tier | Chance/run | Items en pool | Runs para ver ~50% del pool | Runs para ver ~90% del pool |
|------|------------|---------------|----------------------------|-----------------------------|
| Avanzada | 5% | 6 | ~80 | ~200+ |
| Maestra | 15% | 16 | ~50 | ~120 |
| Del Nucleo | 100%+20% | 24 | ~35 | ~75 |

> La Avanzada es ineficiente para farmear jewelry (5% base). Su proposito es dar un "preview" de lo que viene, no ser la fuente principal.

### Camino optimo hacia 24/24

El quest de Ch8 pide 24/24 unique jewelry. Fuentes combinadas:

| Fuente | Unique esperados | Notas |
|--------|-----------------|-------|
| Dungeon Avanzada (~6 runs Ch3-6) | ~0.3 | Muy raro, bonus agradable |
| Dungeon Maestra (~8 runs Ch5-7) | ~1.2 | Empieza a acumular |
| Dungeon Del Nucleo (~15 runs Ch7-8) | ~18 drops (con duplicados) | Fuente principal. ~12-14 unicos de 24. |
| Gacha Avanzada (lifetime) | ~2-3 | Pool basico (8 items) |
| Gacha Superior (lifetime) | ~3-5 | Pool completo (24 items) |
| **Total esperado** | **~18-22 unicos** | Falta ~2-6 para 24/24 |

> Coleccion 24/24 requiere dedicacion. Un jugador casual llegara a ~15-18. Un completista necesitara ~20-25 runs Del Nucleo + gacha con suerte.

---

## 6. Verificacion de cobertura por clase

Cada clase debe tener **al menos 1 item directo** en cada tier donde puede hacer dungeons.

| Clase | Tier 2 | Tier 3 | Tier 4 | Items directos totales |
|-------|--------|--------|--------|----------------------|
| Wizard (Arcane) | Frost (util) | **Arcane (x2)**, Lightning (x2) | **Spell (x2)**, Soul (x2) | 6-8 |
| Wizard (Fire) | Frost (util) | **Fire (x2)**, Lightning (x2) | **Spell (x2)**, Soul (x2) | 6-8 |
| Wizard (Frost) | **Frost (x2)** | Lightning (x2) | **Spell (x2)**, Soul (x2) | 6-8 |
| Rogue | Tank (util) | **Dex (x2)**, Attack (x2) | **Crit (x2)**, Archer (x2) | 6-8 |
| Warrior | **Tank (x2)** | **Attack (x2)** | **Crit (x2)** | 6 |
| Paladin | **Healing (x2)**, **Tank (x2)** | **Attack (x2)** | **Spell (x2)** | 6-8 |
| Priest | **Healing (x2)** | Lightning (util) | **Spell (x2)** | 4-6 |

> Priest tiene menos items directos, pero Healing es su stat principal y lo obtiene desde Tier 2. Spell en T4 es su endgame. Las clases melee tienen buena cobertura en todos los tiers.

---

## 7. Resumen visual por tier

```
TIER 2 (Avanzada, Ch3+) — 6 items, 5% chance
  [Healing x2]  [Tank x2]  [Frost x2]
  Soporte       Defensa    CC/DPS

TIER 3 (Maestra, Ch5+) — +10 items (16 total), 15% chance
  [Fire x2]  [Arcane x2]  [Attack x2]  [Lightning x2]  [Dex x2]
  AoE burst  Burst magic  Melee puro   DPS alterno     Rogue spec

TIER 4 (Del Nucleo, Ch7+) — +8 items (24 total), garantizado
  [Spell x2]  [Crit x2]  [Soul x2]  [Archer x2]
  Magia pura  Multiplicador  Oscuro  Ranged spec
```

---

## 8. Notas de implementacion

### Para servo_dungeons (loot tables)
- Cada tier de dungeon tiene una loot table con weighted random selection
- El peso de cada item se define en JSON (configurable)
- La proteccion anti-duplicado es deseable pero NO obligatoria (duplicados = material de intercambio)

### Para gacha (KubeJS)
- Gacha Avanzada (Epico 7%): pool de 8 items basicos (Healing, Tank, Frost, Fire)
- Gacha Superior (Raro 17%): pool de 16 items (todos excepto T4-exclusivos)
- Gacha Superior (Epico 9%): pool completo de 24 items
- Gacha Superior (Legendario 4%): pool de 8 items T4-exclusivos (Spell, Crit, Soul, Archer)
- Consistente con [gacha-rates.md](gacha-rates.md)

### Para FTB Quests
- Quest Ch7: "Colecciona 5 unique jewelry" (deteccion por tag `jewelry:unique`)
- Quest Ch8: "Colecciona 10+ unique jewelry" y "24/24 unique jewelry" (completismo)
- Display de coleccion en base (trophy room quest)
