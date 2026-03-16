# Muebles Funcionales Refurbished Furniture: Progresion

> Documento de diseno: clasificacion de bloques de MrCrayfish Refurbished Furniture,
> cuales afectan progresion, y como se integran con servo_cooking, delivery y PepeMart.
>
> Issue: [#26](https://github.com/servo98/modpack-servo/issues/26)
> Relacionado: [progression.md](../mechanics/progression.md), [cooking.md](../mechanics/cooking.md), [cooking-workstation-proposal.md](cooking-workstation-proposal.md), [servomart.md](../mechanics/servomart.md)

---

## Inventario completo del mod

Refurbished Furniture agrega **672 recetas** y los siguientes tipos de bloque/item. Muchos vienen en variantes por tipo de madera (10: oak, birch, spruce, jungle, acacia, dark_oak, cherry, mangrove, crimson, warped) o por color de tinte (16 colores Minecraft).

### Items (herramientas y comida)

| Item | Tipo | Funcional? |
|------|------|-----------|
| Knife | Herramienta (cortar en Cutting Board) | Si |
| Spatula | Herramienta (voltear en Frying Pan) | Si |
| Wrench | Herramienta (rotar/configurar muebles) | Si |
| Television Remote | Item (controlar TV) | Minimo |
| Bread Slice, Toast, Dough, Wheat Flour, Sea Salt | Ingredientes intermedios | Si (recetas) |
| Cheese, Cheese Sandwich, Grilled Cheese | Comida | Si (recetas) |
| Raw/Cooked Meat Pizza, Raw/Cooked Vegetable Pizza, Pizza Slices | Comida | Si (recetas) |
| Glow Berry Jam, Sweet Berry Jam, Jam Toasts | Comida | Si (recetas) |
| Package | Item (entrega decorativa del mod) | No relevante |
| Milk (block) | Decorativo/funcional menor | No relevante |

### Bloques funcionales (con mecanica de juego)

Estos bloques tienen GUI, procesan items, o tienen interaccion unica.

| Bloque | Variantes | Recipe Type asociado | Mecanica |
|--------|-----------|---------------------|----------|
| **Fridge** (Dark/Light) | 2 | `freezer_solidifying` | Almacenamiento + congelado de items |
| **Stove** (Dark/Light) | 2 | `oven_baking` | Hornear items (pizza, sea salt) |
| **Microwave** (Dark/Light) | 2 | `microwave_heating` | Calentar items rapido |
| **Toaster** (Dark/Light) | 2 | `toaster_heating` | Tostar pan |
| **Frying Pan** | 1 | `frying_pan_cooking` | Freir items |
| **Cutting Board** | 10 (maderas) | `cutting_board_slicing`, `cutting_board_combining` | Cortar y combinar ingredientes |
| **Workbench** | 1 | `workbench_constructing` | Craftear muebles del mod |
| **Grill** | 16 (colores) | — (usa mecanica de Stove) | Asar al exterior |
| **Cooler** | 16 (colores) | — | Almacenamiento portatil frio |
| **Electricity Generator** (Dark/Light) | 2 | — | Alimenta bloques electricos |
| **Computer** | 1 | — | Interaccion decorativa |
| **Television** | 1 | — | Interaccion con Remote |

### Bloques de almacenamiento

| Bloque | Variantes | Mecanica |
|--------|-----------|----------|
| **Kitchen Storage Cabinet** | 10 maderas + 16 colores | Inventario (almacenamiento) |
| **Kitchen Drawer** | 10 maderas + 16 colores | Inventario (almacenamiento) |
| **Kitchen Cabinetry** | 10 maderas + 16 colores | Inventario (almacenamiento) |
| **Storage Cabinet** | 10 (maderas) | Inventario (almacenamiento) |
| **Storage Jar** | 10 (maderas) | Inventario pequeno |
| **Crate** | 10 (maderas) | Inventario (almacenamiento) |
| **Drawer** | 10 (maderas) | Inventario (almacenamiento) |

### Bloques decorativos (sin mecanica funcional)

| Bloque | Variantes | Notas |
|--------|-----------|-------|
| Chair | 10 (maderas) | Sentarse |
| Table | 10 (maderas) | Decorativo |
| Desk | 10 (maderas) | Decorativo |
| Sofa | 16 (colores) | Sentarse |
| Stool | 16 (colores) | Sentarse |
| Lamp | 16 (colores) | Luz |
| Ceiling Fan (Dark/Light) | 20 (10 maderas x 2) | Decorativo |
| Ceiling Light (Dark/Light) | 2 | Luz |
| Lightswitch (Dark/Light) | 2 | Toggle luces |
| Range Hood (Dark/Light) | 2 | Decorativo cocina |
| Bath | 10 maderas + 16 colores | Decorativo |
| Basin | 10 maderas + 16 colores | Decorativo |
| Toilet | 10 maderas + 16 colores | Decorativo |
| Kitchen Sink | 10 maderas + 16 colores | Decorativo (agua visual) |
| Hedge | 10 maderas + azalea | Decorativo exterior |
| Lattice Fence / Gate | 10 (maderas) | Decorativo exterior |
| Stepping Stones | 4 (stone, andesite, diorite, granite) | Decorativo exterior |
| Trampoline | 16 (colores) | Saltar (divertido, no funcional) |
| Mailbox | 10 (maderas) | Decorativo |
| Post Box | 1 | Decorativo |
| Doorbell | 1 | Sonido |
| Door Mat | 1 | Decorativo |
| Plate | 1 | Decorativo |
| Recycle Bin | 1 | Destruir items |

---

## Clasificacion de progresion

### Nivel 1: Criticos para progresion (afectan gameplay directamente)

Estos bloques tienen recipe types propios y son necesarios para cadenas de recetas del modpack.

| Bloque | Por que es critico | Capitulo | Integracion |
|--------|-------------------|----------|-------------|
| **Fridge (Freezer)** | Recipe type `freezer_solidifying`. Usado para las **7 recetas de congelados** (helados, sorbete, paleta) que van aqui en vez de crear 5ta workstation de servo_cooking. | Ch3 | **servo_cooking**: destino de helados. Issue #71. |
| **Stove (Oven)** | Recipe type `oven_baking`. Hornea pizzas y produce Sea Salt. Complementa al Baker's Oven de servo_cooking. | Ch3 | Las pizzas de Refurbished son bonus — el grueso de horneado es Baker's Oven (Ch3). |
| **Cutting Board** | Recipe types `cutting_board_slicing` + `cutting_board_combining`. Corta pan, combina sandwiches. Complementa al Cutting Board de FD y Prep Station. | Ch3 | Recetas propias del mod (bread slice, cheese sandwich). No interfiere con FD. |
| **Frying Pan** | Recipe type `frying_pan_cooking`. Frie items. Complementa al Wok de servo_cooking. | Ch3 | Recetas propias del mod. No interfiere con Wok. |
| **Workbench** | Recipe type `workbench_constructing`. Necesario para craftear **todos los muebles del mod**. Sin Workbench, no se puede hacer nada de Refurbished. | Ch3 | Gateway obligatorio al mod. |

### Nivel 2: Utiles pero no criticos (mejoran QoL)

| Bloque | Utilidad | Capitulo | Notas |
|--------|---------|----------|-------|
| **Microwave** | Recipe type `microwave_heating`. Alternativa rapida al Furnace para calentar comida. | Ch5 | Convenience, no bloquea nada. |
| **Toaster** | Recipe type `toaster_heating`. Tuesta pan. | Ch5 | Convenience, pocas recetas propias. |
| **Grill** | Asar items al exterior. | Ch5 | Variante estetica del Stove. |
| **Cooler** | Almacenamiento portatil frio. | Ch5 | QoL de almacenamiento. |
| **Electricity Generator** | Alimenta TV, Computer, luces electricas. | Ch5 | Necesario para bloques electricos, pero ningun bloque electrico es critico. |
| **Recycle Bin** | Destruir items. | Ch3 | Util, no critico. |
| **Kitchen Storage (Cabinet, Drawer, Cabinetry)** | Almacenamiento tematizado. | Ch3 | Alternativa estetica a cofres. |
| **Storage Cabinet / Jar / Crate / Drawer** | Almacenamiento. | Ch3 | Alternativa estetica a cofres. |

### Nivel 3: Puramente decorativos (sin impacto en progresion)

Todo lo demas: sillas, mesas, escritorios, sofas, taburetes, lamparas, ventiladores de techo, banos (bath, basin, toilet), kitchen sink, hedges, lattice fences, stepping stones, trampolines, mailbox, post box, doorbell, door mat, plate, computer, television.

**Estos NO necesitan gating especial** — se desbloquean con el Workbench y el stage correspondiente.

---

## Propuesta de integracion por sistema

### 1. servo_cooking: Freezer como 5ta workstation externa

**Ya decidido** (ver [cooking-workstation-proposal.md](cooking-workstation-proposal.md) seccion "Congelados"):

- El Freezer del Fridge de Refurbished es el destino de las 7 recetas de congelados
- NO se crea 5ta workstation en servo_cooking
- Las recetas se agregan via KubeJS usando recipe type `refurbished_furniture:freezer_solidifying`
- Issue #71 pendiente para implementacion

**Cadena de cocina con Refurbished**:

```
Crops/leche → Create (Mixer) → Ingrediente procesado → Freezer (Refurbished) → Helado
                                                      → Baker's Oven (servo_cooking) → Pastel
                                                      → Stove (Refurbished) → Pizza
```

El Freezer y el Stove de Refurbished complementan las 4 workstations de servo_cooking sin redundarlas.

### 2. servo_delivery: Muebles en entregas del Space Elevator

**Propuesta**: NO incluir muebles individuales como requisitos de entrega. Razones:

- Las entregas son cajas empacadas con servo_packaging — los muebles son bloques, no items empacables naturalmente
- Forzar crafteo de muebles especificos rompe la libertad decorativa
- El jugador ya tiene suficientes items de cocina/farming para las entregas

**Excepcion posible**: Una quest en Ch3 tipo "Instalar cocina funcional" (nevera + estufa + fregadero) que ya existe en el chapter doc. Pero es quest de FTB Quests, no entrega del Space Elevator.

### 3. PepeMart: Muebles en el catalogo

**Ya definido** en [servomart.md](../mechanics/servomart.md):

| Capitulo | Contenido en PepeMart |
|----------|----------------------|
| Ch3 | Muebles funcionales Refurbished: Fridge, Stove, Cutting Board, Frying Pan, Workbench |
| Ch5 | Refurbished completo: Microwave, Toaster, Grill, Cooler, TV, Computer, decorativos |

**Precios sugeridos para funcionales en PepeMart**:

| Item | Precio en materiales | Stage |
|------|---------------------|-------|
| Workbench | 16 Iron + 8 Planks | servo_ch3 |
| Fridge (Dark/Light) | 32 Iron + 16 Redstone | servo_ch3 |
| Stove (Dark/Light) | 24 Iron + 8 Redstone + 4 Lava Bucket | servo_ch3 |
| Cutting Board (cualquier madera) | 4 Iron + 8 Planks | servo_ch3 |
| Frying Pan | 8 Iron + 4 Gold | servo_ch3 |
| Microwave (Dark/Light) | 16 Iron + 8 Redstone + 4 Quartz | servo_ch5 |
| Toaster (Dark/Light) | 8 Iron + 4 Redstone | servo_ch5 |
| Grill (cualquier color) | 16 Iron + 8 Coal | servo_ch5 |
| Cooler (cualquier color) | 12 Iron + 4 Packed Ice | servo_ch5 |

**Nota**: Los muebles tambien se craftean normalmente en el Workbench. PepeMart es alternativa directa, no exclusiva.

### 4. FTB Quests: Quests de cocina funcional

Quests ya definidas en [ch3-engranajes-magia.md](../chapters/ch3-engranajes-magia.md):

| Quest | Tipo | Detalle |
|-------|------|---------|
| "Tutorial: Cocina funcional (Refurbished)" | Historia/Tutorial | Explica Workbench → Fridge → Stove → Cutting Board |
| "Instalar cocina funcional (nevera+estufa+fregadero)" | Cocina | Colocar los 3 bloques. Reward: XP + materiales |
| "Instalar cocina funcional Refurbished" | Construccion | Mismo objetivo, perspectiva de construccion |

**Quests adicionales propuestas**:

| Quest | Capitulo | Tipo | Detalle | Reward |
|-------|----------|------|---------|--------|
| Hacer primer helado en Freezer | Ch3 | Cocina | Craftear cualquier helado | XP + ingredientes |
| Hornear pizza en Stove | Ch3 | Cocina | Hornear pizza de carne o vegetales | XP |
| Cocina completa (8 muebles) | Ch5 | Construccion | Colocar: Fridge, Stove, Microwave, Toaster, Cutting Board, Frying Pan, Range Hood, Kitchen Sink | Titulo "Chef Profesional" |
| Casa amueblada (15 muebles) | Ch5 | Construccion | Colocar 15 muebles Refurbished diferentes | Tokens (15) |

---

## Consideraciones para el perfil "novia" (Perfil B)

La novia disfruta tareas estructuradas, repetitivas y satisfactorias. Los muebles de Refurbished son un reward loop natural:

1. **Recoleccion progresiva**: "Desbloquea el mod en Ch3 → craftea tu primera cocina → expande en Ch5 con microwave/toaster/grill → completa la casa"
2. **Personalizacion por colores**: 16 colores de sofas, lamps, stools, kitchen cabinets. Cambiar el color requiere crafteo con tinte = tarea repetitiva satisfactoria
3. **Quest de cocina completa**: La quest "Cocina completa (8 muebles)" premia decorar con un titulo especial
4. **No forzar**: Los muebles son compra directa en PepeMart o crafteo libre en Workbench. Sin RNG, sin gating excesivo. La motivacion es estetica + quests opcionales, no bloqueo

**Lo que NO hacer**:
- No gatear muebles decorativos individuales por capitulo — eso frustra la decoracion libre
- No requerir muebles especificos para entregas del Space Elevator
- No dar buffs mecanicos a muebles colocados (la opcion 2 del issue). Requiere codigo custom, es fragil, y los buffs son dificiles de balancear. Mantener simple.

---

## Decision: Opcion 1 (modificada) del issue

De las 3 opciones del issue #26:

| Opcion | Veredicto | Razon |
|--------|-----------|-------|
| 1. Muebles como requisitos de delivery | **NO** para delivery, **SI** para quests FTB | Delivery con bloques es raro. Quests de "instalar cocina" ya existen. |
| 2. Muebles como buffs pasivos | **NO** | Requiere codigo custom, dificil de balancear, fragil. |
| 3. Solo decorativos | **PARCIAL** | La mayoria son decorativos. Los 5 bloques funcionales SI afectan progresion via recipe types. |

**Resumen de la decision**:

- **5 bloques son criticos** (Fridge, Stove, Cutting Board, Frying Pan, Workbench) porque tienen recipe types que procesan items
- **6 bloques son QoL** (Microwave, Toaster, Grill, Cooler, Generator, Recycle Bin)
- **Todo lo demas es decorativo** y se desbloquea libremente con el stage del capitulo
- **Freezer del Fridge** es parte integral del sistema de cocina (7 recetas congelados)
- **PepeMart** ofrece compra directa de funcionales desde Ch3
- **Quests** premian instalar cocina pero no fuerzan muebles especificos en entregas
- **Cero codigo custom** para buffs — mantener simple

---

## Gating por ProgressiveStages (resumen)

| Stage | Bloques Refurbished disponibles |
|-------|-------------------------------|
| servo_ch3 | Workbench, Fridge, Stove, Cutting Board, Frying Pan, Range Hood, Kitchen Sink, Kitchen Cabinets/Drawers/Storage, Storage blocks, Recycle Bin, todos los decorativos basicos (chair, table, sofa, lamp, etc.) |
| servo_ch5 | Microwave, Toaster, Grill, Cooler, Electricity Generator, Computer, Television, TV Remote, Lightswitch, Ceiling Light, Trampoline, todos los decorativos restantes |

**Implementacion**: via KubeJS script de ProgressiveStages — asignar stage `servo_ch3` a items del grupo funcional basico, y `servo_ch5` al resto.
