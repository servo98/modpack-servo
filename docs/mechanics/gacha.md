# Sistema Gacha

> Fuente: GDD v2, seccion 3.11
> Mod: Bloo's Gacha Machine
> Relacionado: [Tokens](tokens.md), [ServoMart](servomart.md), [Accessories](accessories.md), [Dungeons](dungeons.md), [Jewelry](jewelry.md)
> Balance detallado: docs/balance/gacha-rates.md

## Overview

4 maquinas gacha fisicas, cada una con su tier de contenido. Usa Pepe Coins convertidos a monedas de color. Engagement hook, NO pay-to-win.

## Reglas generales

- **NO crafteables** — cada maquina se obtiene como quest reward en su capitulo
- **Unica por grupo** — no se pueden duplicar
- Cada maquina usa **su propia moneda de color** (crafteada desde Pepe Coins)
- 1 moneda = 1 pull (el costo esta en la conversion Pepe Coins → moneda)
- Los pools son **fijos** por maquina (no cambian por stage del jugador)
- Loot tables se configuran via capsulas del mod

## Las 4 maquinas

| Maquina | Color | Disponible | Costo/pull | Contenido principal |
|---------|-------|------------|------------|---------------------|
| Basica | Verde | Ch1 | 10 Pepe Coins | T1-T2, materiales vanilla, gemas basicas |
| Avanzada | Azul | Ch3 | 10 Pepe Coins | T2-T3, RPG gear, gemas raras, unique jewelry |
| Superior | Purpura | Ch5 | 15 Pepe Coins | T3-T5, netherite, unique jewelry, llaves |
| Muebles | Rosa | Ch2 | 5 Pepe Coins | Macaw's Furniture, Refurbished Furniture |

---

## Gacha Basica (Verde) — Ch1+

**Quest reward**: Tutorial de gacha (Ch1).
**Pull**: 1 Green Coin (= 10 Pepe Coins).
**Tema**: Materiales utiles, accesorios iniciales, primeras gemas.

| Rareza | % | Pool de items |
|--------|---|---------------|
| Comun (gris) | 55% | **Materiales**: 8-16 Iron, 4-8 Gold, 8-16 Copper, 16-32 Coal, 4-8 Lapis, 4-8 Redstone, 8-16 Leather, 4-8 String. **Comida**: 4-8 ingredientes FD (trigo, papa, zanahoria, tomate, cebolla, arroz). **Runas**: 2-4 Small Arcane/Frost Rune |
| Poco Comun (verde) | 25% | **Gemas**: 1-2 Citrine o Jade. **Libros**: Enchanted Book I-II (Protection, Sharpness, Efficiency, etc). **Preview**: 2-4 ingredientes Croptopia Ch2 (Lettuce, Corn, Strawberry). **Runas**: 1 Small Rune Pouch |
| Raro (azul) | 12% | **Accesorios**: 1 Accesorio custom T1 aleatorio (Belt/Back/Feet). **Moldes**: 1 Molde de postre 1-2★. **Jewelry**: Copper Ring o Iron Ring |
| Epico (purpura) | 5% | **Accesorios**: 1 Accesorio custom T2 aleatorio. **Moldes**: 1 Molde de postre 3★. **Jewelry**: Gold Ring, Citrine Ring, o Jade Ring |
| Legendario (dorado) | 3% | **Armas**: 1 arma RPG T1 (Iron Dagger, Iron Sickle, Wizard Staff, etc). **Accesorios**: 1 Accesorio T2 con SpecialEffect. **Llaves**: 1 Llave de Dungeon Basica |

---

## Gacha Avanzada (Azul) — Ch3+

**Quest reward**: Quest de capitulo 3 (al desbloquear clases magicas).
**Pull**: 1 Blue Coin (= 10 Pepe Coins).
**Tema**: RPG gear, gemas raras, primeras unique jewelry, accesorios medios.

| Rareza | % | Pool de items |
|--------|---|---------------|
| Comun (gris) | 50% | **Materiales**: 2-4 Diamond, 4-8 Amethyst Shard, 4-8 Prismarine Shard, 2-4 Blaze Rod, 2-4 Ender Pearl. **Runas**: 2-4 Medium Rune (tipo aleatorio). **Comida**: 4-8 ingredientes Ch3-4 (frutas, especias) |
| Poco Comun (verde) | 25% | **RPG Armor**: 1 pieza T1 aleatoria (Rogue, Warrior, Wizard, Paladin, o Priest set). **Gemas**: 1-2 Ruby o Sapphire. **Jewelry**: Ruby/Sapphire Ring o Necklace. **Libros**: Enchanted Book II-III o Spell Power enchant |
| Raro (azul) | 15% | **Accesorios**: 1 Accesorio custom T2 aleatorio. **Moldes**: 1 Molde de postre 3★. **RPG**: 1 pieza T1 armor de clase especifica. **Llaves**: 1 Llave Basica |
| Epico (purpura) | 7% | **Accesorios**: 1 Accesorio custom T3 aleatorio. **Jewelry**: Tanzanite/Topaz/Diamond Ring. **Unique Jewelry**: 1 del pool basico (ver tabla abajo). **Moldes**: 1 Molde de postre 4★ |
| Legendario (dorado) | 3% | **Armas**: 1 arma RPG T2 (Diamond weapon, Elemental Staff). **Accesorios**: 1 Accesorio T3 con bonus especial. **Unique Jewelry**: 1 del pool medio |

### Pool de Unique Jewelry — Gacha Avanzada

Solo 8 de las 24 unique estan disponibles (las de stats mas generales):

| Item | Escuela |
|------|---------|
| Ring of Fury | Attack |
| Amulet of Torture | Attack |
| Ring of Suffering | Crit |
| Necklace of Anguish | Crit |
| Juggernaut Band | Tank |
| Sentinel's Amulet | Tank |
| Angelista's Revenge | Rogue |
| Chocker of Vile Intent | Rogue |

---

## Gacha Superior (Purpura) — Ch5+

**Quest reward**: Quest de capitulo 5 (al completar infraestructura digital).
**Pull**: 1 Purple Coin (= 15 Pepe Coins).
**Tema**: Endgame gear, unique jewelry completas, accesorios T3-T5, netherite.

| Rareza | % | Pool de items |
|--------|---|---------------|
| Comun (gris) | 45% | **Materiales**: 1-2 Netherite Scrap, 2-4 Diamond, 2-4 Blaze Rod, 1-2 Ghast Tear, 2-4 Magma Cream. **Runas**: 1 Large Rune (tipo aleatorio). **Libros**: Enchanted Book III-IV |
| Poco Comun (verde) | 25% | **RPG Armor**: 1 pieza T2 aleatoria (Assassin, Berserker, Elemental Robes, Crusader, Prior). **Jewelry**: Netherite Gem Ring/Necklace. **Enchants**: Spell Power, Sunfire, Soulfrost, o Energize enchant book |
| Raro (azul) | 17% | **Accesorios**: 1 Accesorio custom T3 aleatorio. **Moldes**: 1 Molde de postre 4-5★. **Unique Jewelry**: 1 del pool medio (ver tabla abajo). **RPG**: Netherite Upgrade Smithing Template |
| Epico (purpura) | 9% | **Accesorios**: 1 Accesorio custom T4 aleatorio. **Unique Jewelry**: 1 del pool completo (cualquiera de 24). **Llaves**: 1 Llave Avanzada |
| Legendario (dorado) | 4% | **Accesorios**: 1 Accesorio T4 con bonus doble (2 efectos). **Unique Jewelry**: 1 del pool legendario (ver tabla). **RPG**: 1 arma T3 (Netherite) |

### Pool de Unique Jewelry — Gacha Superior

**Pool medio** (Raro, 16 items — incluye los 8 de Avanzada + estos 8):

| Item | Escuela |
|------|---------|
| Pendant of Sunfire | Fire |
| Sunflare Signet | Fire |
| Glacial Shard | Frost |
| Permafrost Stone | Frost |
| Adria's Pendant | Healing |
| Anett's Embrace | Healing |
| Amulet of Unrelenting Storms | Lightning |
| Ring of Unstable Currents | Lightning |

**Pool legendario** (Legendario, 8 items — los mas poderosos, exclusivos de esta rareza):

| Item | Escuela |
|------|---------|
| Amulet of Unfettered Magics | Arcane |
| Ring of Subjugation | Arcane |
| Pendant of Acumen | Spell |
| Ring of Omnipotence | Spell |
| Occult Necklace | Soul |
| Signet of Captured Souls | Soul |
| Darnassian Strider Amulet | Archer |
| Thalassian Ranger Band | Archer |

---

## Gacha de Muebles (Rosa) — Ch2+

**Quest reward**: Introduccion a decoracion (Ch2).
**Pull**: 1 Pink Coin (= 5 Pepe Coins).
**Tema**: Muebles decorativos y funcionales. Separado del gacha de artefactos.

| Rareza | % | Pool de items |
|--------|---|---------------|
| Comun (gris) | 60% | 1 mueble Macaw's basico: silla, mesa, banco, estante, escalera |
| Poco Comun (verde) | 25% | 1 mueble Macaw's decorativo: lampara, espejo, cortina, alfombra, sofa |
| Raro (azul) | 10% | 1 mueble Macaw's especial (puerta decorativa, ventana, balcon) O 1 mueble Refurbished funcional (si capitulo lo permite: nevera Ch3, estufa Ch3, fregadero Ch3) |
| Epico (purpura) | 4% | 1 set tematico de 2-3 muebles que combinan (ej: mesa+sillas+lampara del mismo estilo) |
| Legendario (dorado) | 1% | 1 mueble exclusivo cosmetico (edicion limitada, con particulas o animacion) |

El pool de muebles se expande por capitulo segun desbloqueo de ServoMart (ver [ServoMart](servomart.md)):
- Ch2: Macaw's basico
- Ch3: + Refurbished basico (nevera, estufa, fregadero)
- Ch4: + Macaw's Windows completo
- Ch5: + Create Deco + Refurbished completo
- Ch6+: Todo

---

## Pity System

- Se aplica **por maquina, por jugador** (no compartido entre maquinas ni jugadores)
- +2% a Epico por cada pull sin obtener Epico o superior
- **Hard pity a 50 pulls**: garantiza Legendario
- Se reinicia al obtener Epico o superior
- Tracking via servo_core (PlayerCapability con contadores por maquina)

## Items EXCLUIDOS del gacha (dungeon-only)

Estos items **nunca** aparecen en ninguna maquina gacha:

| Item | Razon | Donde se obtiene |
|------|-------|-----------------|
| Esencia de Dungeon | Moneda de progresion dungeon | Champions en dungeon Avanzada+ |
| Fragmento de Cristal del Nucleo | Material T4 RPG | Boss de dungeon Del Nucleo |
| Llave Maestra | Demasiado valiosa | Craft (requiere Esencia) |
| Llave del Nucleo | Demasiado valiosa | Craft (requiere Fragmento) |
| RPG T4 gear | Endgame, debe ser crafteado | Craft con materiales de dungeon |
| Accesorio T5 Maestro (3 unicos) | Reward de bosses finales | Boss Ch7 y Ch8 |

## Moldes de postres (detalle)

7 tipos de molde, cada uno en 5 rarezas (1-5★). Total: 35 moldes.

| Tipo | Recetas que desbloquea |
|------|----------------------|
| Molde de Pastel | Pasteles decorados |
| Molde de Pie | Pies dulces y salados |
| Molde de Tarta | Tartas francesas |
| Molde de Galleta | Galletas con forma |
| Molde de Dona | Donas glaseadas |
| Molde de Muffin | Muffins y cupcakes |
| Molde de Flan | Flanes y puddings |

**Obtencion por rareza de molde**:
- 1-2★: Gacha Basica (Raro), quests, dungeon Basica
- 3★: Gacha Basica (Epico), Gacha Avanzada (Raro), dungeon Avanzada
- 4★: Gacha Avanzada (Epico), Gacha Superior (Raro), dungeon Maestra
- 5★: Gacha Superior (Raro, poco frecuente), dungeon Maestra/Nucleo

---

## Implementacion con Bloo's Gacha Machine

### Mapping de IDs del mod

| Concepto | Block ID | Coin ID | Capsulas |
|----------|----------|---------|----------|
| Basica | `gachamachine:gacha_machine_5` (Green) | `gachamachine:gacha_coin_5` (Green) | `capsule_e1` - `capsule_e10` |
| Avanzada | `gachamachine:gacha_machine_6` (Blue) | `gachamachine:gacha_coin_6` (Blue) | `capsule_f1` - `capsule_f10` |
| Superior | `gachamachine:gacha_machine_7` (Purple) | `gachamachine:gacha_coin_7` (Purple) | `capsule_g1` - `capsule_g10` |
| Muebles | `gachamachine:gacha_machine_8` (Pink) | `gachamachine:gacha_coin_8` (Pink) | `capsule_h1` - `capsule_h10` |

### Recipes KubeJS necesarias

```
// Conversion Pepe Coins → Gacha Coins
10x servo_core:pepe_coin → 1x gachamachine:gacha_coin_5  (Basica)
10x servo_core:pepe_coin → 1x gachamachine:gacha_coin_6  (Avanzada)
15x servo_core:pepe_coin → 1x gachamachine:gacha_coin_7  (Superior)
5x servo_core:pepe_coin  → 1x gachamachine:gacha_coin_8  (Muebles)
```

### Capsulas como rareza

Cada color de capsula tiene 10 grades (1-10). Mapeo a rarezas:

| Capsulas | Rareza | Peso sugerido |
|----------|--------|---------------|
| 1-5 | Comun | ~11% cada una (55% total) |
| 6-7 | Poco Comun | ~12.5% cada una (25% total) |
| 8 | Raro | 12-17% (varia por maquina) |
| 9 | Epico | 5-9% (varia por maquina) |
| 10 | Legendario | 1-4% (varia por maquina) |

> **Nota**: Los pesos exactos dependen de como Bloo's Gacha Machine configura probabilidades por capsula. Verificar en la config del mod (`config/gachamachine/`) o via KubeJS override.

### Maquinas restantes (sin usar)

IDs disponibles para expansion futura si se necesitan:
- `gacha_machine` (default), `_2` (Red), `_3` (Orange), `_4` (Yellow), `_9` (Black), `_10` (White)

### Ocultar items no usados

Usar KubeJS para ocultar de EMI las maquinas, monedas, y capsulas que no usamos:

```js
// En client_scripts/
EMIPlugin.removeRecipes(recipes => {
  // Ocultar maquinas/coins/capsulas no usadas
})
```
