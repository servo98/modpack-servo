# Estado de Implementacion: servo_packaging

> Mod ID: `servo_packaging`
> Version: 0.1.0
> Ultima actualizacion: 2026-03-12 (Sesion 14)
> Diseno de referencia: [docs/mechanics/packaging.md](../mechanics/packaging.md)
> Arquitectura: [docs/architecture.md](../architecture.md)

---

## Tabla de features

| Feature | Diseno (packaging.md) | Estado | Notas |
|---------|----------------------|--------|-------|
| Item: Carton Plano | `servo_packaging:flat_cardboard`, stack 64 | **done** | Registrado, stack 64 |
| Item: Caja Abierta | `servo_packaging:open_box`, stack 16 | **done** | Registrado como `Item` base, stack 16 |
| Item: Caja de Envio | `servo_packaging:shipping_box`, stack 16, datos internos | **done** | Registrado, stack 16, usa DataComponent |
| Doblar carton (click derecho en aire) | Carton -> Caja Abierta, animacion, sonido | **partial** | Logica OK, sonido placeholder (`BOOK_PAGE_TURN`), sin animacion de manos |
| Doblar carton (crafting batch) | Shapeless 1:1 en crafting table | **done** | `fold_cardboard.json`: shapeless 1 flat_cardboard -> 1 open_box |
| Receta Carton Plano | 4 Paper + 1 String -> 4 Carton Plano | **done** | `flat_cardboard.json`: shaped 2x2 Paper + String en L, result x4 |
| Receta Empacadora | 4 Iron Ingot + 4 Plank + 1 Hopper | **done** | `packing_station.json`: shaped cruz alternada Iron/Plank + Hopper centro |
| Bloque: Empacadora | `servo_packaging:packing_station`, sin GUI | **done** | `BaseEntityBlock`, FACING property, VoxelShape 14/16 alto |
| Empacadora: state machine | EMPTY / BOX_PLACED / FILLING / SEALED | **done** | 4 estados implementados correctamente |
| Empacadora: colocar Caja Abierta | Click derecho con open_box -> Estado BOX_PLACED | **done** | Implementado en `handleEmpty` |
| Empacadora: meter items (1 a la vez) | Click derecho -> inserta 1 item | **done** | Implementado, con mensaje ActionBar si item diferente |
| Empacadora: shift-click llena todo | Shift-click -> inserta hasta completar | **done** | `player.isShiftKeyDown()` en `tryAddItem` |
| Empacadora: cierre automatico | Al llegar a targetCount -> `sealBox()` | **done** | Instantaneo. Sin animacion visual |
| Empacadora: recoger caja cerrada | Click en estado SEALED -> da Caja de Envio | **done** | Implementado en `handleSealed` |
| Empacadora: cancelar packing | Click vacio en FILLING -> devuelve items + caja | **done** | Implementado en `cancelPacking` |
| Empacadora: drop al romper | Drop todos los contenidos | **done** | `dropAllContents` en `onRemove` |
| Empacadora: NBT persistence | Guardar/cargar estado entre sesiones | **done** | `saveAdditional`/`loadAdditional` completo |
| Validacion: solo mismo tipo | No mezclar items distintos en una caja | **done** | Check por `packedItemId` en `tryAddItem` |
| Validacion: packable tag | Solo items con tag `#servo_packaging:packable` | **done** | `isPackable()` usa `stack.is(PackagingRegistry.PACKABLE_TAG)`. Tag JSON con defaults razonables. |
| Pack sizes por tag | Tags `pack_size_1/8/16`, default 4 | **done** | Tags definidos, `getPackSize()` los lee correctamente |
| Categorias por tag | 5 tags de categoria (food/crops/processed/magic/special) | **done** | Tags JSON creados, `detectCategory()` los lee. Categoria "general" como fallback |
| Feedback ActionBar: progreso | "Empacando: 2/4" al insertar items | **done** | `sendActionBar` con `packing.progress` key |
| Feedback ActionBar: caja sellada | "Caja sellada!" al completar | **done** | `sendActionBar` con `packing.sealed` key |
| Feedback ActionBar: cancelado | "Empaque cancelado" al cancelar | **done** | `sendActionBar` con `packing.cancelled` key |
| Feedback ActionBar: no empacable | "Este item no se puede empacar" | **done** | `sendActionBar` con `packing.not_packable` key |
| Feedback ActionBar: tipo distinto | "Solo items del mismo tipo..." | **done** | `sendActionBar` con `packing.wrong_type` key |
| Tooltip de Caja de Envio | "Contiene: Nx Item", hint gris | **done** | Implementado con `appendHoverText` y llaves i18n |
| Colores por categoria en tooltip | Verde/amarillo/azul/morado/dorado | **done** | `getCategoryColor()` usada directamente en `appendHoverText` con `withStyle(color)` |
| DataComponent BoxContents | Codec + StreamCodec para red/disco | **done** | `BoxContents` record con CODEC y STREAM_CODEC |
| Automatizacion: Hopper top (Caja Abierta) | Hopper arriba inserta open_box | **done** | `WorldlyContainer`, `SLOT_BOX_IN` en UP, `canPlaceItemThroughFace` |
| Automatizacion: Hopper lados (items) | Hopper lateral inserta items | **done** | `SLOT_ITEM_IN` en sides, logica de llenado en `setItem` |
| Automatizacion: Hopper abajo (output) | Hopper abajo extrae Caja de Envio | **done** | `SLOT_OUTPUT` en DOWN, `canTakeItemThroughFace` |
| Creative tab | Tab propio con los 4 items | **done** | `PACKAGING_TAB` con icono de shipping_box |
| Facing al colocar | El bloque mira hacia el jugador | **done** | `getStateForPlacement` con HORIZONTAL_FACING |
| Lang: en_us | Traducciones en ingles | **done** | `en_us.json`: 4 items/bloques + 2 tooltip keys + 5 feedback keys |
| Lang: es_mx | Traducciones en espanol | **done** | `es_mx.json`: todas las claves con traduccion completa |
| Tag JSONs | Archivos en data/ para packable/sizes/categorias | **done** | 9 JSONs: packable, pack_size_1/8/16, category/food/crops/processed/magic/special |
| Blockstate JSON | Rotacion por `facing` | **done** | 4 variantes (N/S/E/W) con rotacion Y |
| Modelos JSON | Block + 4 item models | **done** | 5 JSONs. Block: cubo 14/16 alto con texturas propias. Items: `item/generated` con textura layer0 |
| Texturas placeholder | PNGs 16x16 de color solido | **done** | 5 PNGs: `packing_station_top`, `packing_station_side`, `flat_cardboard`, `open_box`, `shipping_box` |
| Modelo 3D bloque real | Mesa con superficie plana, bordes levantados | **pending** | Cubo simplificado 14/16. Artista debe reemplazar con modelo detallado |
| Sonidos custom | Crujido carton, clonk, cinta adhesiva | **pending** | Usando vanilla placeholders (WOOL_PLACE, BUNDLE_INSERT, BARREL_CLOSE) |
| Animacion cierre | Animacion visual de caja cerrandose | **pending** | No implementado. Cierre instantaneo. |
| Items visuales en caja abierta | Items se ven dentro de la caja (renderer) | **pending** | Sin BlockEntityRenderer |
| Create compat | Funnel/belt I/O (en servo_create) | **pending** | Intencionalmente fuera de este mod |

---

## Implementado

El mod esta **completamente funcional**. Compila, toda la logica de juego es correcta, y el contenido data-driven (recetas, tags, lang, modelos, blockstates) esta presente. Lo que falta es exclusivamente visual/sonido (assets de artista).

### Items
- `flat_cardboard` — click derecho convierte 1 unidad en 1 `open_box`. Sonido placeholder.
- `open_box` — item base sin comportamiento especial. Sirve de "token" para la empacadora.
- `shipping_box` — item con `BoxContents` DataComponent. Tooltip muestra contenido con color de categoria (`GREEN` food, `YELLOW` crops, `BLUE` processed, `DARK_PURPLE` magic, `GOLD` special, `GRAY` general).

### Empacadora (PackingStationBlock / PackingStationBlockEntity)
- State machine de 4 estados (EMPTY / BOX_PLACED / FILLING / SEALED) funcionando correctamente.
- Interaccion manual completa: colocar caja, meter items uno a uno, shift-click para llenar de golpe, recoger la caja sellada, cancelar empaque (devuelve items + open_box).
- Feedback via ActionBar en cada interaccion: progreso "Empacando: 2/4", "Caja sellada!", "Empaque cancelado", "Este item no se puede empacar", "Solo items del mismo tipo caben en una caja".
- `isPackable()` verifica el tag `#servo_packaging:packable`. Solo items en ese tag son aceptados.
- Automatizacion con hoppers nativos via `WorldlyContainer`: top acepta solo open_box, lados aceptan items empacables del mismo tipo, abajo solo extrae shipping_box sellada.
- NBT persistence completo (sobrevive reloads de chunk y reinicios de mundo).
- Drop de contenidos al romper el bloque.
- Block facing (mira hacia el jugador al colocar).
- VoxelShape levemente reducida (14/16 de alto, simula mesa).

### Recetas (3 JSONs)
- `flat_cardboard.json` — shaped: 2x2 de Paper + String en posicion (3,2), produce 4x carton plano.
- `fold_cardboard.json` — shapeless: 1 flat_cardboard -> 1 open_box (alternativa al click derecho en aire).
- `packing_station.json` — shaped: patron IPI/PHP/IPI con Iron (`c:ingots/iron`), Plank (`minecraft:planks`), Hopper en centro.

### Tags (9 JSONs)
- `packable.json` — defaults: `#c:foods`, `#c:crops`, `#c:ores`, `#c:ingots`, `#c:gems`, `#c:raw_materials`, `#minecraft:coals`, ejemplos de tools/weapons/enchanted_book.
- `pack_size_1.json` — tools, armor, weapons, bow, crossbow, shield, enchanted_book (items de valor unitario).
- `pack_size_8.json` — ingots, gems (materiales de valor medio).
- `pack_size_16.json` — crops, ores, raw_materials, coals (materiales a granel).
- `category/food.json` — `#c:foods`.
- `category/crops.json` — `#c:crops`.
- `category/processed.json` — `#c:ingots`, `#c:gems`, `#c:raw_materials`.
- `category/magic.json` — vacio por ahora (se llenara con tags RPG Series).
- `category/special.json` — vacio por ahora (se llenara con items custom de servos).

### Lang files (2 JSONs)
- `en_us.json` y `es_mx.json` — cubren: nombres de 3 items + 1 bloque + creative tab, 2 keys de tooltip de shipping_box, 5 keys de feedback ActionBar.

### Modelos y blockstates (6 JSONs)
- `blockstates/packing_station.json` — 4 variantes por facing con rotacion Y.
- `models/block/packing_station.json` — cubo 14/16 alto con 3 texturas (`top`, `side`, `bottom` = oak_planks vanilla).
- `models/item/flat_cardboard.json`, `open_box.json`, `shipping_box.json` — `item/generated` con layer0.
- `models/item/packing_station.json` — referencia al modelo de bloque.

### Texturas placeholder (5 PNGs 16x16)
- `textures/block/packing_station_top.png` — cuadrado de color solido.
- `textures/block/packing_station_side.png` — cuadrado de color solido.
- `textures/item/flat_cardboard.png` — cuadrado de color solido.
- `textures/item/open_box.png` — cuadrado de color solido.
- `textures/item/shipping_box.png` — cuadrado de color solido.

### DataComponents
- `BoxContents` record con codec persistente y stream codec para red. Almacena: `itemId` (ResourceLocation), `count` (int), `category` (String).
- Registrado como `DataComponentType` con `persistent + networkSynchronized`.

---

## Pendiente

### Prioridad media (pulido de UX)

| Que falta | Notas |
|-----------|-------|
| Modelo 3D real del bloque | Actualmente cubo simplificado 14/16 alto. Ver seccion "Assets pendientes" al final. |
| Texturas reales para todos los items y el bloque | Placeholders son cuadrados de color solido. Ver seccion "Assets pendientes". |
| Sonidos custom | Doblar carton: `BOOK_PAGE_TURN`. Colocar caja: `WOOL_PLACE`. Meter item: `BUNDLE_INSERT`. Cierre: `BARREL_CLOSE`. Reemplazar con sounds custom cuando haya assets. |

### Prioridad baja (nice to have)

| Que falta | Notas |
|-----------|-------|
| BlockEntityRenderer (items visibles en caja) | Items "asomando" de la caja abierta. Requiere renderer cliente con `GeoModel` o renderer custom. |
| Animacion de cierre | El cierre es instantaneo. Diseno dice ~0.5s de animacion. Requiere block entity animation o estado de transicion. |
| Caja abierta como entidad en el bloque | La caja abierta "sentada encima" requiere renderer. Actualmente solo logica, sin visual. |
| Animacion de manos al doblar carton | El click derecho en aire no tiene animacion de manos. |
| Tags RPG Series en category/magic | `magic.json` esta vacio, se llenara cuando el KubeJS de RPG Series este definido. |
| Tags custom servo en category/special | `special.json` esta vacio, se llenara con items servo_core definidos. |
| Create compat | Funnel/belt I/O — va en `servo_create` como addon separado. |

---

## Decisiones de implementacion

Estas son las decisiones tomadas al codificar que difieren o aclaran el diseno original.

### DataComponents en vez de NBT en el item
El diseno en `packaging.md §2` muestra un bloque de codigo NBT como ejemplo. La implementacion usa `DataComponentType<BoxContents>` (MC 1.21.1 API moderna). La semantica es identica: el jugador no ve el dato interno, solo el tooltip. El componente es persistente y se sincroniza por red automaticamente.

### Caja de Envio con stack size 16 siempre
El diseno dice "x16 por tipo". La implementacion usa `stacksTo(16)` fijo. Esto significa que cajas de `food` y cajas de `gear` se apilan igual (ambas x16). Si se quiere que `gear` apile x1, hay que sobreescribir `getMaxStackSize` en `ShippingBoxItem` para consultar la categoria del DataComponent.

### `isPackable()` usa tag en produccion
El metodo verifica `stack.is(PackagingRegistry.PACKABLE_TAG)` directamente. El tag `packable.json` tiene defaults razonables que cubren los casos del modpack (comida, cultivos, minerales, herramientas). El KubeJS del modpack puede extender el tag con `#servo_packaging:packable` en sus propios data packs.

### Pack size default = 4
Items sin tag de tamano tienen `targetCount = 4`. El diseno asigna 4 a comida cocinada, que es el caso mas comun. Es un default razonable.

### Receta de Empacadora usa patron distinto al diseno
El diseno dice "4 Iron Ingot + 4 Plank + 1 Hopper". La receta implementada usa el patron IPI/PHP/IPI (cruz alternada), que encaja visualmente con el concepto de maquina y es mas intuitivo en la crafting table. Los ingredientes son los mismos.

### WorldlyContainer en vez de IItemHandler
La automatizacion usa la interfaz vanilla `WorldlyContainer` con logica por cara (`getSlotsForFace`). Esto da compatibilidad con hoppers nativos y con cualquier mod que use la API vanilla. La compat Create (funnels, belts) va en `servo_create` como addon separado, consistente con la arquitectura.

### Categoria "general" como fallback
Si un item no tiene ninguna de las 5 tags de categoria, `detectCategory()` retorna `"general"`. El tooltip usa `ChatFormatting.GRAY` para esta categoria. No estaba en el diseno original (que solo define 5 categorias), pero es necesario para items sin categoria asignada.

### Estado BOX_PLACED no es FILLING
La state machine tiene 4 estados en vez de los 5 descritos en el diseno. El diseno agrupa "caja colocada" y "primer item" como distintos, pero el codigo los maneja como transicion interna dentro de `tryAddItem`. La diferencia practica: `BOX_PLACED` puede recibir el primer item directamente, y la transicion a `FILLING` ocurre en el mismo tick. No hay estado intermedio que el jugador perciba.

### Facing del bloque
El bloque rota para mirar hacia el jugador al colocar (`getStateForPlacement`). No estaba en los requerimientos del diseno pero es convencion estandar en bloques de trabajo tipo mesa.

### ActionBar en vez de chat para feedback
El feedback de progreso y errores usa `player.displayClientMessage(message, true)` que muestra el mensaje en la barra de accion (encima del hotbar, no en el chat). Es menos intrusivo y apropiado para mensajes de estado de corta duracion.

---

## Assets pendientes (para artista)

Los siguientes assets son placeholders que deben ser reemplazados por un artista. Todos los placeholders son PNGs 16x16 de color solido. El mod compila y funciona con ellos; son puramente visuales.

| Tipo | Archivo y ruta | Descripcion | Tamano / Formato | Prioridad |
|------|---------------|-------------|-----------------|-----------|
| Textura | `assets/servo_packaging/textures/item/flat_cardboard.png` | Hoja de carton corrugado vista de frente, beige/cafe claro, lineas de ondulacion visibles en los bordes, apariencia 2D tipo item de Minecraft | 16x16 PNG | alta |
| Textura | `assets/servo_packaging/textures/item/open_box.png` | Caja de carton abierta en perspectiva isometrica, alas abiertas hacia arriba, tono cafe, estilo pixel art Minecraft | 16x16 PNG | alta |
| Textura | `assets/servo_packaging/textures/item/shipping_box.png` | Caja de carton sellada con cinta adhesiva cruzada encima, tono cafe, pequena etiqueta de color en el frente (para distinguir categorias). La etiqueta en el tooltip indica la categoria pero la textura base es generica. | 16x16 PNG | alta |
| Textura | `assets/servo_packaging/textures/block/packing_station_top.png` | Vista superior de una mesa de empaque: superficie de madera oscura con marcas de trabajo, quizas una guia rectangular indicando donde va la caja, y un pequeno reborde levantado en los bordes | 16x16 PNG | alta |
| Textura | `assets/servo_packaging/textures/block/packing_station_side.png` | Vista lateral de la mesa: madera de trabajo, tablones horizontales, tornillos o refuerzos metalicos en las esquinas. Debe combinar con la textura top. | 16x16 PNG | alta |
| Modelo 3D | `assets/servo_packaging/models/block/packing_station.json` | La geometria actual es un cubo simple 14/16 alto. El artista debe reemplazarla con un modelo detallado: mesa de trabajo con patas, superficie plana con reborde levantado, quizas un rollo de cinta adhesiva o una prensa a un lado. Compatible con el sistema de rotacion por facing (N/S/E/W via blockstate). | JSON BlockModel NeoForge 1.21.1 | media |
| Sonido | `assets/servo_packaging/sounds/fold_cardboard.ogg` | Sonido de doblar carton: crujido seco de papel/carton, ~0.5s. No existe el archivo aun; el codigo usa `BOOK_PAGE_TURN` como placeholder. Requiere agregar `sounds.json` cuando se tenga el OGG. | OGG Vorbis, mono, ~22kHz | media |
| Sonido | `assets/servo_packaging/sounds/pack_item.ogg` | Sonido de meter un item en la caja: golpe sordo suave, como un objeto cayendo en una caja de carton, ~0.3s. Placeholder actual: `BUNDLE_INSERT`. | OGG Vorbis, mono, ~22kHz | media |
| Sonido | `assets/servo_packaging/sounds/seal_box.ogg` | Sonido de sellar la caja: tira de cinta adhesiva rasgandose y pegandose, ~0.8s. Placeholder actual: `BARREL_CLOSE`. | OGG Vorbis, mono, ~22kHz | media |
| Sonido | `assets/servo_packaging/sounds/place_box.ogg` | Sonido de colocar la caja abierta en la empacadora: golpe hueco de carton sobre madera, ~0.3s. Placeholder actual: `WOOL_PLACE`. | OGG Vorbis, mono, ~22kHz | baja |

> Nota sobre `sounds.json`: cuando se agreguen los OGGs, crear `assets/servo_packaging/sounds.json` en el directorio raiz de assets del mod con las entradas correspondientes para cada sonido. El codigo en `PackingStationBlockEntity.playSound()` y `FlatCardboardItem` debe actualizarse para usar `SoundEvents` registrados en `PackagingRegistry` en vez de los vanilla placeholders.
