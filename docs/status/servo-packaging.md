# Estado de Implementacion: servo_packaging

> Mod ID: `servo_packaging`
> Version: 0.3.0
> Ultima actualizacion: 2026-03-12
> Diseno de referencia: [docs/mechanics/packaging.md](../mechanics/packaging.md)
> Arquitectura: [docs/architecture.md](../architecture.md)

---

## Resumen

Mod standalone de empaque inmersivo sin GUI. Tres items + dos bloques:

1. **Carton Plano** — material base (4 Paper + 1 String)
2. **Empacadora** — bloque que dobla carton plano en cajas abiertas (40 ticks, animacion BER)
3. **Caja Abierta** — bloque placeable, se llena click derecho, auto-sella al tener 16 items
4. **Caja de Envio** — item sellado con contenido (DataComponent). Tooltip con icono + color por categoria. Placeable en el piso como bloque sellado.

Flujo: Craftear carton → Doblar en empacadora → Colocar caja abierta → Meter items → Auto-sella → Recoger/colocar caja de envio

---

## Tabla de features

| Feature | Estado | Notas |
|---------|--------|-------|
| **Items** | | |
| Item: Carton Plano (`flat_cardboard`) | **done** | Stack 64, sprite con textura corrugada |
| Item: Caja Abierta (`open_box`) | **done** | `BlockItem` que coloca `OpenBoxBlock` |
| Item: Caja de Envio (`shipping_box`) | **done** | DataComponent `BoxContents`, tooltip color por categoria, placeable como bloque sellado |
| DataComponent BoxContents | **done** | Codec + StreamCodec, persistente y sincronizado |
| **Packing Station (solo dobla carton)** | | |
| Bloque: Empacadora | **done** | `BaseEntityBlock`, FACING, VoxelShape 14/16, ticker server |
| State machine: IDLE → FOLDING → DONE | **done** | 2 slots (input/output), 40 ticks (2s) de fold |
| GUI con slots y barra de progreso | **done** | Textura custom `packing_station.png` (256x256) |
| Output stackeable | **done** | Produce open_box continuamente, stacks en output |
| Hopper compat | **done** | Top: acepta flat_cardboard. Bottom/sides: extrae open_box |
| Drop al romper | **done** | Input + output caen al piso |
| NBT persistence | **done** | Estado, ticks, slots guardados correctamente |
| Client sync (getUpdateTag/Packet) | **done** | Para animacion BER |
| Animacion de doblado (BER) | **done** | 5 paneles de carton que rotan 0°→90°, color solido cafe |
| **Open Box (bloque en el piso)** | | |
| Bloque: Caja Abierta | **done** | `BaseEntityBlock`, VoxelShape 12x8x12 |
| BlockState: ITEMS_STORED (0-4) | **done** | Trackea nivel de llenado visualmente |
| BlockState: SEALED (true/false) | **done** | Auto-seal al llenar 16 items |
| BlockState: FACING (N/S/E/W) | **done** | 4 direcciones, cara al jugador al colocar |
| Insertar items (click derecho con item) | **done** | Stack completo de golpe, mismo tipo obligatorio, max 16 items |
| Sacar items (click derecho vacio) | **done** | Saca el ultimo item insertado |
| Recoger caja vacia (shift+click vacio) | **done** | Drop contenido + devuelve open_box item |
| Auto-seal al llenar (16 items) | **done** | Cambia blockstate SEALED=true automaticamente |
| Recoger caja sellada (click en sealed) | **done** | Da ShippingBox con BoxContents, destruye bloque |
| Colocar ShippingBox como bloque | **done** | Click derecho en superficie coloca sealed OpenBoxBlock con contenido |
| Items visibles dentro (BER) | **done** | Items flotan con bobbing, layout 1/2/3/4, escala 0.35, rotados |
| Estampa de contenido en sealed (BER) | **done** | Icono del item empacado en cara frontal de caja sellada |
| Hopper compat | **done** | Top: inserta items packable del mismo tipo |
| Drop al romper | **done** | Sealed: drop ShippingBox. Open: drop contenido + 1 open_box |
| Carry On bloqueado | **done** | Tag `neoforge:immovable` evita que Carry On levante cajas/estacion |
| NBT persistence | **done** | ContainerHelper, totalCount, packedItemId, category |
| Client sync | **done** | getUpdateTag/getUpdatePacket para BER |
| Validacion: packable tag | **done** | Solo items con `#servo_packaging:packable` |
| Validacion: mismo tipo | **done** | Primer item define tipo, rechaza diferentes |
| Categorias por tag | **done** | 5 categorias (food/crops/processed/magic/special) + "general" fallback |
| **Client rendering** | | |
| PackingStationBER (animacion doblado) | **done** | 5 paneles de carton con rotacion progresiva |
| OpenBoxBER (items visibles) | **done** | Items flotando dentro de caja abierta |
| OpenBoxBER (estampa sealed) | **done** | Icono del contenido en cara frontal de caja sellada |
| ShippingBoxDecorator (overlay inventario) | **done** | Icono del contenido al 50% centrado sobre sprite de caja |
| PackingStationScreen (GUI) | **done** | Textura custom, flecha de progreso, 2 slots |
| **Recursos** | | |
| Blockstate: packing_station | **done** | 4 variantes por facing |
| Blockstate: open_box | **done** | 40 variantes (5 items x 2 sealed x 4 facing) con rotacion Y |
| Modelo bloque: open_box | **done** | Caja con fondo, 4 paredes, 2 solapas baked rotation |
| Modelo bloque: sealed_box | **done** | Caja cerrada con tira de cinta, rotacion baked |
| Modelo bloque: packing_station | **done** | Mesa con rodillos |
| Modelo item: open_box | **done** | Referencia al modelo de bloque 3D |
| Modelo item: flat_cardboard | **done** | Sprite 2D con textura corrugada |
| Modelo item: shipping_box | **done** | Sprite 2D caja 3/4 con cinta + area de etiqueta |
| Loot table: open_box | **done** | Pools vacios (drops manejados en onRemove) |
| Loot table: packing_station | **done** | Drop del bloque |
| Lang: en_us | **done** | Items, bloques, tooltips, feedback messages |
| Lang: es_mx | **done** | Traduccion completa al espanol |
| Receta: flat_cardboard | **done** | 4 Paper + 1 String = 4 Flat Cardboard |
| Receta: packing_station | **done** | Iron + Planks + Hopper |
| Tags (10 JSONs) | **done** | packable, pack_size_1/8/16, category/food/crops/processed/magic/special |
| Creative tab | **done** | 4 items/bloques |
| neoforge:immovable tag | **done** | Blacklist para Carry On |
| Textura GUI: packing_station | **done** | 256x256 con fondo, arrow, slots |
| Function: test_kit | **done** | `/function servo_packaging:test_kit` da items de prueba |

---

## Archivos Java (15 archivos)

```
servo-packaging/src/main/java/com/servo/packaging/
├── ServoPackaging.java              # Mod entry point
├── PackagingRegistry.java           # Blocks, items, BEs, menus, components, tags, creative tab
├── component/
│   └── BoxContents.java             # DataComponent record (itemId, count, category)
├── block/
│   ├── PackingStationBlock.java     # Station block (folds cardboard, opens GUI)
│   ├── PackingStationBlockEntity.java # IDLE→FOLDING→DONE, 2 slots, ticker, WorldlyContainer
│   ├── OpenBoxBlock.java            # Placeable box (fill interactively, ITEMS/SEALED/FACING)
│   └── OpenBoxBlockEntity.java      # Stores items, hopper compat, auto-seal, WorldlyContainer
├── item/
│   ├── FlatCardboardItem.java       # Simple item (no behavior)
│   └── ShippingBoxItem.java         # Unbox on use-in-air, place as block on use-on-surface
└── client/
    ├── PackagingClientSetup.java     # Registers BERs, menu screens, item decorators
    ├── PackingStationBER.java        # Fold animation (5 cardboard panels)
    ├── PackingStationScreen.java     # GUI with progress arrow and 2 slots
    ├── OpenBoxBER.java              # Items floating inside + sealed stamp on front face
    └── ShippingBoxDecorator.java    # Overlay: content item icon on shipping box in inventory
```

## Scripts de utilidad

```
servo-packaging/blockbench/
├── convert.py           # Convierte .bbmodel → MC block model JSON + extrae texturas
├── gen_gui.py           # Genera textura GUI de la Packing Station (256x256)
├── gen_item_sprites.py  # Genera sprites mejorados (flat_cardboard, shipping_box)
├── open_box.bbmodel     # Modelo Blockbench de caja abierta
├── sealed_box.bbmodel   # Modelo Blockbench de caja sellada
└── packing_station.bbmodel # Modelo Blockbench de empacadora
```

---

## Assets para artista / Blockbench

Los modelos y texturas actuales son **funcionales pero generados por codigo**. Para pulir:

### Prioridad ALTA — Texturas de bloque

| Archivo | Tamano | Estado actual | Descripcion deseada |
|---------|--------|---------------|---------------------|
| `textures/block/box_texture.png` | 32x32 | Generada por Blockbench | Carton corrugado cafe, interior mas claro |
| `textures/block/sealed_box.png` | 32x32 | Generada por Blockbench | Carton cerrado con cinta adhesiva gris |
| `textures/block/packing_station.png` | 48x48 | Generada por Blockbench | Mesa de trabajo con rodillos metalicos |

### Prioridad MEDIA — Modelos 3D

Los modelos actuales funcionan pero pueden mejorarse:
- **open_box**: Agregar solapas este/oeste (solo tiene norte/sur por limitacion de rotacion)
- **packing_station**: Hacer rodillos mas detallados
- **sealed_box**: Mejorar perfil de cinta

### Prioridad BAJA — Sonidos custom

| Sonido | Placeholder actual | Descripcion |
|--------|-------------------|-------------|
| Doblar carton | `BOOK_PAGE_TURN` | Crujido seco de carton, ~0.5s |
| Meter item en caja | `BUNDLE_INSERT` | Golpe sordo suave, ~0.3s |
| Sacar item de caja | `BUNDLE_REMOVE_ONE` | Pop suave, ~0.2s |
| Sellar caja | `BARREL_CLOSE` | Cinta adhesiva, ~0.8s |
| Colocar caja | `WOOL_PLACE` | Carton en piso, ~0.3s |

---

## Decisiones de implementacion (v0.3)

### Packing Station solo dobla
Station produce cajas vacias. No empaca items — eso se hace directamente en el piso.

### Open Box es un bloque placeable
BlockItem que coloca OpenBoxBlock. Interaccion inmersiva sin GUI estilo composter.

### Auto-seal al llenar (16 items fijos)
Constante `ITEMS_PER_BOX = 16`. No hay cantidades variables por tipo (simplificado). Se puede cambiar a futuro via tags `pack_size_*`.

### ShippingBox dual: unbox o place
Click derecho en aire = desempaca items. Click derecho en superficie = coloca como bloque sellado.

### BER dual: items visibles + estampa
Caja abierta muestra items flotando adentro. Caja sellada muestra icono del contenido en la cara frontal.

### Overlay en inventario
ShippingBoxDecorator renderiza el icono del contenido al 50% sobre el sprite base de la caja en el inventario.

### convert.py bakes rotations
Rotaciones >45° en modelos Blockbench se pre-computan en la geometria (posiciones from/to) y se remapean las caras UV. Esto evita la limitacion de vanilla MC de rotaciones ±45° en un solo eje.
