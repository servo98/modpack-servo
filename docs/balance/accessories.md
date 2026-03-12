# Accessories System - Modpack Servo

> Consistente con docs/mechanics/accessories.md y docs/mechanics/gacha.md (4 maquinas por tier)

## Base: Curios API (6 slots)
| Slot | Fuente | Tematica |
|------|--------|----------|
| Ring x2 | Jewelry mod | Combate / Utilidad |
| Necklace | Jewelry mod | Cocina/Farming |
| Belt (Cinturon) | servo_core | Defensa/Exploracion |
| Back (Capa) | servo_core | Ofensa/Movilidad |
| Feet (Tobillera) | servo_core | Soporte/Exploracion |

> Rings y Necklaces vienen del mod Jewelry (85 items). Belt/Back/Feet son custom de servo_core.

## Tiers (custom servo_core: Belt/Back/Feet)
| Tier | Nombre | Boost | Cantidad | Obtencion |
|------|--------|-------|----------|-----------|
| 1 | Tosco | +5% | 20 | Craft, quests, Gacha Basica (Raro 12%) |
| 2 | Pulido | +12% | 15 | Gacha Basica (Epico 5%), Gacha Avanzada (Raro 15%), champions |
| 3 | Refinado | +22% + bonus | 10 | Gacha Avanzada (Epico 7%), Gacha Superior (Raro 17%), bosses |
| 4 | Excepcional | +40% + bonus + visual | 6 | Gacha Superior (Epico 9%), dungeon boss |
| 5 | Maestro | +70% + 2 bonus + particulas | 3 | Boss Ch7 y Ch8 SOLAMENTE (excluido del gacha) |
| **Total** | | | **54** | |

## Effect Families (12 clases Java)
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
| SpecialEffect | Efecto unico por accesorio | Cualquiera (T2+) |

## Work Estimate
- 12 clases Java + 1 base = ~1,500 lineas
- 10 pixelarts base x 5 recolores/tier = ~54 texturas
- 108 strings traduccion (54 x 2 idiomas)
- ~15-20 horas total

## Acquisition Over Full Modpack
| Fuente | Accesorios | Tiers | Detalle |
|--------|-----------|-------|---------|
| Craft | 20 | T1 | Recetas vanilla-style |
| Gacha | ~23-24 | T1-T4 | Basica: ~10 (T1-T2), Avanzada: ~8 (T2-T3), Superior: ~5 (T3-T4) |
| Boss drops | 8 | T2-T5 | Bosses de capitulo (T5 solo Ch7-Ch8) |
| Dungeon boss | 12 | T2-T4 | Cofres y bosses de dungeon |
| Champions | 8 | T1-T2 | Champions overworld y dungeon |
| Quest rewards | 6 | T1-T2 | Quests de capitulo |
| **Total** | **~77-78** (con duplicados) | |
| Unicos esperados | ~38-42 de 54 (70-78%) | |

> Calculo de gacha basado en gacha-rates.md: 60 pulls Basica + 36 Avanzada + 16 Superior.
