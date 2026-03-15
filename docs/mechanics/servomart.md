# PepeMart (Tienda Online)

> Fuente: GDD v2, seccion 3.4
> Implementacion: **servo_mart** (mod standalone, deps: servo_packaging)
> Relacionado: [Tokens](tokens.md), [Gacha](gacha.md), [Progression](progression.md)

## Overview

Bloque custom tipo tablet/computadora. El jugador abre un catalogo, elige lo que quiere comprar, paga con **materiales** (NO Pepe Coins), y el item llega en una **Caja de Carton** (servo_packaging) que debe desempacar.

## Diferencia con Gacha

| | Gacha Rosa | PepeMart |
|---|---|---|
| **Pago** | Pepe Coins (5/pull) | Materiales (configurable) |
| **Resultado** | Random | Determinístico — eliges qué comprar |
| **Sensación** | "A ver qué me toca!" | "Quiero ESE específico" |
| **Precio relativo** | Barato por item esperado | Más caro que gacha |
| **Entrega** | Capsula de gacha | Caja de Cartón (servo_packaging) |

Ambos coexisten: gacha para la emocion del azar, PepeMart para compra directa con certeza.

## Como funciona

1. Colocar bloque **PepeMart** (tablet) en el mundo
2. Click derecho → abre GUI con catalogo
3. Catalogo organizado por **categorias** (muebles, plushies, accesorios, QoL)
4. Cada item muestra: nombre, preview, precio en materiales, stage requerido
5. Items bloqueados por stage se ven pero no se pueden comprar (candado)
6. Al comprar: materiales se consumen del inventario, aparece **Caja de Carton** con el item
7. Colocar la caja y abrirla → aparece el item

## Categorias del catalogo

### Muebles funcionales
- Nevera, estufa, fregadero (Refurbished Furniture)
- **Criticos para progresion** — bloquean recetas Ch4+
- Disponibles desde Ch3
- Precio medio en materiales

### Muebles decorativos
- Macaw's Furniture (sillas, mesas, bancos, estantes, lamparas)
- Macaw's Windows, Bridges, Roofs, Trapdoors
- Create Deco (bloques industriales)
- Refurbished completo (camas, mesas, sillones)
- Precio bajo-medio

### Plushies / Figuritas
- Plushables mod (49 plushies interactivos con sonidos)
- Algunos exclusivos de PepeMart, otros exclusivos de Gacha Rosa
- Coleccionables decorativos
- Precio bajo-medio

### Accesorios (T1-T2 solamente)
- Cinturones, capas, zapatos, sombreros custom (servo_core)
- Solo T1 y T2 — **T3+ es gacha/dungeon-only**
- Ventaja: eliges el efecto exacto que quieres
- Precio alto (más caro que la expectativa de gacha)

### Items QoL / Consumibles
- Items de conveniencia que no vale la pena craftear
- Precio variable

## Desbloqueo por capitulo

| Cap | Contenido nuevo en PepeMart |
|-----|---------------------------|
| 2 | Macaw's Furniture basico, plushies comunes |
| 3 | Refurbished funcional (nevera, estufa, fregadero), accesorios T1 |
| 4 | Macaw's Windows completo, accesorios T2 |
| 5 | Create Deco, Refurbished completo |
| 6+ | Todo disponible |

## Precios sugeridos (materiales)

| Categoria | Ejemplo | Precio |
|-----------|---------|--------|
| Mueble basico | Silla Macaw's | 16 Iron + 8 planks |
| Mueble funcional | Nevera | 32 Iron + 16 Redstone |
| Mueble decorativo | Lampara Macaw's | 8 Gold + 4 Glowstone |
| Plushie comun | Creeper Plush | 8 Wool + 4 String |
| Plushie raro | Ender Dragon Plush | 16 Wool + 4 Ender Pearl |
| Accesorio T1 | Cinturon de Cobre | 32 Iron + 8 Gold + 16 crops |
| Accesorio T2 | Capa de Hierro | 12 Diamond + 24 Gold + 32 Iron |

## Configuracion via JSON (datapack)

El catalogo es 100% configurable via JSON. Los devs definen cada entrada:

```json
{
  "entries": [
    {
      "item": "plushables:creeper_plush",
      "category": "plushies",
      "tier": 1,
      "price": {
        "minecraft:white_wool": 8,
        "minecraft:string": 4
      },
      "requires_stage": "servo_ch2"
    },
    {
      "item": "servo_core:belt_speed_t2",
      "category": "accessories",
      "tier": 2,
      "price": {
        "minecraft:diamond": 12,
        "minecraft:gold_ingot": 24,
        "minecraft:iron_ingot": 32
      },
      "requires_stage": "servo_ch4"
    },
    {
      "item": "refurbished_furniture:fridge",
      "category": "furniture",
      "tier": 1,
      "price": {
        "minecraft:iron_ingot": 32,
        "minecraft:redstone": 16
      },
      "requires_stage": "servo_ch3"
    }
  ]
}
```

Campos por entrada:
- `item`: ID del item a vender
- `category`: categoria para organizar en GUI (muebles, plushies, accesorios, qol)
- `tier`: tier visual (afecta color/borde en GUI)
- `price`: mapa de item ID → cantidad requerida
- `requires_stage`: stage de ProgressiveStages necesario para comprar

## Que NO se vende en PepeMart

| Item | Razon | Donde se obtiene |
|------|-------|-----------------|
| Accesorios T3-T5 | Demasiado poderosos | Gacha Azul/Purpura, bosses, dungeons |
| Unique Jewelry | Coleccionables escasos | Gacha Azul/Purpura, dungeons |
| Armas RPG | Pilar RPG separado | Gacha, craft, dungeons |
| Llaves de Dungeon | Se craftean con materiales | Craft (4 Iron + 2 Gold la Basica) |
| Materiales/gemas | Se farmean | Overworld, gacha |
| Plushies legendarios | Exclusivos de gacha | Gacha Rosa (Legendario 1%) |

## Implementacion (servo_mart)

### Bloques
- `servo_mart:pepe_mart` — bloque tablet con GUI

### GUI
- Pantalla tipo catalogo con tabs por categoria
- Grid de items con preview, nombre y precio
- Boton de compra (grisado si no hay materiales o stage)
- Indicador de stage requerido para items bloqueados

### Dependencias
- servo_packaging (hard) — entrega en cajas
- ProgressiveStages (soft) — gating por stage

## Estado: EN DISENO

- [x] Concepto general
- [x] Diferenciacion con Gacha
- [x] Categorias y precios sugeridos
- [x] Formato JSON configurable
- [ ] Catalogo completo por item (ver docs/balance/pepemart-catalog.md)
- [ ] GUI mockup
- [ ] Scaffold Java
