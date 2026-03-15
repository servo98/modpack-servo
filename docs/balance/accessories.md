# Accessories System - Modpack Servo

> Consistente con docs/mechanics/accessories.md y docs/mechanics/gacha.md (4 maquinas por tier)

## Base: Curios API (7 slots)
| Slot | Fuente | Tematica |
|------|--------|----------|
| Ring x2 | Jewelry mod | Combate / Utilidad |
| Necklace | Jewelry mod | Combate / Spell Power |
| Belt (Cinturon) | servo_core | Defensa/Utilidad |
| Back (Capa) | servo_core | Ofensa/Movilidad |
| Feet (Zapatos) | servo_core | Soporte/Exploracion |
| Head (Sombrero) | servo_core | Percepcion/Magia |

> Rings y Necklaces vienen del mod Jewelry (85 items). Belt/Back/Feet/Head son custom de servo_core.

## Tiers (custom servo_core: Belt/Back/Feet/Head)
| Tier | Nombre | Boost | Cantidad | Obtencion |
|------|--------|-------|----------|-----------|
| 1 | Tosco | +5% | 15 | Craft, quests, Gacha Basica (Raro 12%) |
| 2 | Pulido | +12% | 15 | Gacha Basica (Epico 5%), Gacha Avanzada (Raro 15%), champions |
| 3 | Refinado | +22% + bonus | 12 | Gacha Avanzada (Epico 7%), Gacha Superior (Raro 17%), bosses |
| 4 | Excepcional | +40% + bonus + visual | 8 | Gacha Superior (Epico 9%), dungeon boss |
| 5 | Maestro | +70% + 2 bonus + particulas | 4 | Boss Ch7 y Ch8 SOLAMENTE (excluido del gacha) |
| **Total** | | | **54** | |

## Effect Families (15 clases Java)
| Familia | Efecto | Slots |
|---------|--------|-------|
| SpeedBoost | +X% velocidad (cocina/cosecha/minado) | Belt, Back |
| DamageBoost | +X% dano | Back |
| DefenseBoost | +X% armor/resistance | Belt |
| LuckBoost | +X% chance extra drops | Feet |
| RegenBoost | +X regen/lifesteal | Feet |
| EfficiencyBoost | +X% velocidad maquinas | Belt |
| AnimalBoost | +X% breeding/taming | Feet |
| FoodBoost | +X% saturacion/nutricion | Belt |
| ExplorationBoost | +X% velocidad movimiento | Back |
| TokenBoost | +X% tokens ganados | Feet |
| DungeonBoost | +X% loot en dungeon | Back |
| SpellPowerBoost | +X% spell power | Head |
| VisionBoost | +X vision (night vision, loot sense) | Head |
| XPBoost | +X% XP ganada | Head |
| SpecialEffect | Efecto unico por accesorio | Cualquiera (T2+) |

## Work Estimate
- 15 clases Java + 1 base = ~2,000 lineas
- 12 pixelarts base x 5 recolores/tier = ~54 texturas
- 108 strings traduccion (54 x 2 idiomas)
- ~20-25 horas total

## Acquisition Over Full Modpack
| Fuente | Accesorios | Tiers | Detalle |
|--------|-----------|-------|---------|
| Craft | 15 | T1 | Recetas vanilla-style |
| Gacha | ~28-30 | T1-T4 | Basica: ~12 (T1-T2), Avanzada: ~10 (T2-T3), Superior: ~6-8 (T3-T4) |
| Boss drops | 10 | T2-T5 | Bosses de capitulo (T5 solo Ch7-Ch8) |
| Dungeon boss | 14 | T2-T4 | Cofres y bosses de dungeon |
| Champions | 10 | T1-T2 | Champions overworld y dungeon |
| Quest rewards | 8 | T1-T2 | Quests de capitulo |
| **Total** | **~94-96** (con duplicados) | |
| Unicos esperados | ~48-52 de 68 (70-76%) | |

> Calculo de gacha basado en gacha-rates.md: 60 pulls Basica + 36 Avanzada + 16 Superior.
