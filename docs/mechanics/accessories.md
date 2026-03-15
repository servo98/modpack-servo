# Sistema de Accesorios (Curios API)

> Fuente: GDD v2, seccion 3.10
> Mods: Curios API (framework), Jewelry (rings/necklaces), servo_core (belt/back/feet/head)
> Relacionado: [Jewelry](jewelry.md), [RPG Classes](rpg-classes.md), [Dungeons](dungeons.md), [Gacha](gacha.md)
> Balance detallado: docs/balance/accessories.md

## Overview

7 slots totales: 3 de Jewelry mod + 4 custom de servo_core.

## Slots del jugador

| Slot | Fuente | Cantidad | Items |
|------|--------|----------|-------|
| Ring | Jewelry mod | 2 | 85 items (gem rings, unique, netherite) |
| Necklace | Jewelry mod | 1 | gem necklaces, unique necklaces |
| Belt (Cinturon) | servo_core | 1 | Cinturones custom |
| Back (Capa) | servo_core | 1 | Capas/cloaks custom |
| Feet (Zapatos) | servo_core | 1 | Zapatos/botas custom |
| Head (Sombrero) | servo_core | 1 | Sombreros/gorros custom |

## Tiers custom (belt/back/feet/head)

| Tier | Nombre | Boost | Obtencion |
|------|--------|-------|-----------|
| 1 | Comun (cobre) | +5% | Craft, Gacha Basica (Raro), quest, **PepeMart (Ch3)** |
| 2 | Uncommon (hierro) | +12% | Gacha Basica (Epico), Gacha Avanzada (Raro), champion drops, **PepeMart (Ch4)** |
| 3 | Raro (oro) | +22% | Boss drops, Gacha Avanzada (Epico), Gacha Superior (Raro) |
| 4 | Epico (diamante) | +40% | Boss drops, Gacha Superior (Epico), dungeon boss |
| 5 | Legendario (netherite) | +70% | Boss Ch7 y Ch8 SOLAMENTE (excluido del gacha) |

## Familias de efecto por slot

- **Belt (Cinturon)**: utilidad/defensa (velocidad, armor, knockback res, fire res, mining speed)
- **Back (Capa)**: ofensa/movilidad (ATK, crit, speed, jump, fall reduction)
- **Feet (Zapatos)**: soporte/exploracion (regen, luck/loot, swim, XP, water breathing)
- **Head (Sombrero)**: percepcion/magia (spell power, night vision, XP bonus, loot sense)

## Items custom (~65+)

5 tiers x 4 slots x 3-4 efectos = ~60-80 items curados (no todas las combinaciones).

## Modelos (Blockbench)

- **Cinturones**: T1=cuerda, T3=cuero con gema, T5=metal ornamentado
- **Capas**: T1=trapo, T3=capa bordada, T5=capa legendaria luminosa
- **Zapatos**: T1=sandalias, T3=botas con gema, T5=botas arcanas
- **Sombreros**: T1=gorro de tela, T3=sombrero con pluma, T5=corona arcana
