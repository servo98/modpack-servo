# Estado de Implementacion: servo_delivery

> Mod ID: `servo_delivery`
> Version: 0.1.0-scaffold
> Ultima actualizacion: 2026-03-14
> Diseno de referencia: [docs/mechanics/space-elevator.md](../mechanics/space-elevator.md)
> Arquitectura: [docs/architecture.md](../architecture.md)

---

## Resumen

Mod standalone de entrega por capitulo. Un multibloque 3x3 (Terminal de Entrega) acepta Cajas de Envio de `servo_packaging` y verifica su contenido contra una lista de entregas definida via JSON datapack. Al completar todas las entregas de un capitulo y presionar LAUNCH, dispara `DeliveryCompleteEvent` que `servo_core` escucha para avanzar el stage del jugador.

Flujo: Ensamblar multibloque → Click derecho con Caja de Envio en mano sobre Terminal o Puerto → Terminal valida contenido → Progreso global via SavedData → Jugador presiona LAUNCH → servo_core otorga stage

---

## Tabla de features

| Feature | Estado | Notas |
|---------|--------|-------|
| **Scaffold y build** | | |
| Proyecto scaffold (build.gradle, gradle.properties, toml) | **done** | Compila sin errores |
| Dependencia GeckoLib configurada | **done** | Para animacion del terminal |
| Dependencia servo_packaging configurada | **done** | Hard dep: acepta ShippingBox con BoxContents |
| **Bloques (4 tipos)** | | |
| Bloque: Terminal (`delivery_terminal`) | **done** | Master del multibloque, GeckoLib BlockEntity |
| Bloque: Puerto (`delivery_port`) | **done** | Slaves perimetrales, delegan a master via tryInsertItem |
| Bloque: Base (`elevator_base`) | **done** | Plataforma del multibloque |
| Bloque: Antena (`elevator_antenna`) | **done** | Parte superior visual |
| **Multibloque** | | |
| Validacion de estructura 3x3 | **done** | Verifica posiciones relativas al colocar terminal |
| Patron master/slave (SlaveBlockEntity) | **done** | Terminal es master; puerto, base y antena son slaves |
| **GeckoLib (Terminal BlockEntity)** | | |
| Renderer con overlay de texto en cara del bloque | **done** | Muestra capitulo actual + barra de progreso en la cara del bloque |
| Rotacion del modelo segun FACING | **done** | El terminal rota correctamente en las 4 direcciones |
| geo.json (placeholder — cubos) | **done** | Existe como placeholder; necesita modelo Blockbench real |
| animation.json (placeholder — sin controladores activos) | **done** | Existe; registerControllers() esta vacio, sin animaciones reales aun |
| **Sistema de entregas** | | |
| Data-driven via JSON datapack | **done** | DeliveryDataLoader carga desde data/servo_delivery/delivery/*.json |
| Progreso global via DeliverySavedData | **done** | Todos los terminales del mundo comparten el mismo progreso; sobrevive destruccion del bloque |
| DeliveryCompleteEvent | **done** | Evento NeoForge disparado al presionar LAUNCH con 100% de progreso |
| Validacion server-side | **done** | Verificacion de BoxContents en servidor |
| Chapter 1 JSON de prueba | **done** | chapter_1.json con datos de prueba |
| **GUI / Menu** | | |
| DeliveryTerminalMenu + DeliveryTerminalScreen | **done** | GUI con lista de requisitos, barras de progreso y boton LAUNCH |
| Boton LAUNCH (habilitado solo al 100%) | **done** | No hay auto-complete — el jugador debe presionar LAUNCH |
| **Recursos base** | | |
| Lang: en_us | **done** | Nombres de bloques e items |
| Lang: es_mx | **done** | Traduccion completa al espanol |
| Blockstates | **done** | Estados visuales para las 4 direcciones |
| Modelos placeholder | **done** | Modelos cubo placeholder por bloque |
| **Features pendientes** | | |
| BoxContents matching (verificar tipo de caja vs requerimiento) | **pending** | Actualmente acepta cualquier ShippingBox; falta validar que el contenido corresponda al requisito |
| Create automation para puertos (hopper/funnel extraction) | **pending** | Compat con cintas y funnels de Create |
| Efectos de particulas durante celebracion | **pending** | VFX al completar capitulo |
| Efectos de sonido (idle, insercion, celebracion) | **pending** | Placeholders vanilla en uso |
| Beacon beam durante celebracion | **pending** | Haz de luz vertical al completar |
| Loot tables | **pending** | Drops al romper bloques |
| Recetas de crafteo | **pending** | Recetas para ensamblar el multibloque |
| JSONs de entregas Ch2-Ch8 | **pending** | Solo Ch1 placeholder existe |
| Funcion de test in-game | **pending** | `/function servo_delivery:test_kit` |
| Chunk force-loading al armar multibloque | **pending** | Requiere decision de diseno (ver TODO 8.20) |
| Buffer de 1 slot en puertos | **pending** | Actualmente passthrough; buffer + redstone pendiente de decision (ver TODO 8.21) |
| Tooltip en hover sobre requisitos del GUI | **pending** | |
| Animaciones GeckoLib reales | **pending** | registerControllers() vacio; geo.json y animation.json son placeholders |

---

## Archivos Java

```
servo-delivery/src/main/java/com/servo/delivery/
├── ServoDelivery.java
├── DeliveryRegistry.java
├── block/
│   ├── DeliveryTerminalBlock.java
│   ├── DeliveryTerminalBlockEntity.java
│   ├── DeliveryTerminalMenu.java
│   ├── DeliveryCompleteEvent.java
│   ├── DeliveryPortBlock.java
│   ├── ElevatorBaseBlock.java
│   ├── ElevatorAntennaBlock.java
│   └── SlaveBlockEntity.java
├── client/
│   ├── DeliveryClientSetup.java
│   ├── DeliveryTerminalRenderer.java
│   └── DeliveryTerminalScreen.java
└── data/
    ├── ChapterDelivery.java
    ├── DeliveryDataLoader.java
    └── DeliverySavedData.java
```

---

## Assets para artista / Blockbench

### Prioridad ALTA — Modelo 3D del Terminal (GeckoLib)

| Archivo | Tamano | Estado actual | Descripcion deseada |
|---------|--------|---------------|---------------------|
| `delivery_terminal.geo.json` | — | Placeholder (cubos) | Modelo GeckoLib del bloque terminal. Estilo: maquinaria industrial con antena, pantalla de datos, bobinas laterales |
| `delivery_terminal.animation.json` | — | Placeholder (sin controladores) | 3 animaciones: `idle` (bobinas rotando lento), `celebration` (bobinas rapido + pulso de luz), `inactive` (todo quieto) |
| `delivery_terminal.png` | 256x256 | No existe | Atlas de texturas para el modelo GeckoLib. Estilo metalico industrial. |

### Prioridad ALTA — Texturas de bloque

| Archivo | Tamano | Estado actual | Descripcion deseada |
|---------|--------|---------------|---------------------|
| `textures/block/delivery_terminal.png` | 16x16 | Cubo placeholder | Cara frontal del terminal: pantalla de datos |
| `textures/block/delivery_port.png` | 16x16 | Cubo placeholder | Puerto lateral: ranura de insercion metalica |
| `textures/block/elevator_base.png` | 16x16 | Cubo placeholder | Plataforma: placa metalica con relieves |
| `textures/block/elevator_antenna.png` | 16x16 | Cubo placeholder | Antena: estructura delgada con luz en punta |

### Prioridad BAJA — Sonidos custom

| Sonido | Placeholder actual | Descripcion |
|--------|-------------------|-------------|
| Idle loop | ninguno | Zumbido electrico suave, loop de ~3s |
| Insercion de caja | `BUNDLE_INSERT` | Clic mecanico al recibir caja |
| Celebracion | `LEVEL_UP_SOUND` | Fanfarria corta de maquinaria |
| Estructura invalida | `NOTE_BLOCK_BASS` | Buzz de error |

---

## JSONs de entrega (data-driven)

```
servo-delivery/src/main/resources/data/servo_delivery/delivery/
├── chapter_1.json    # placeholder de prueba — existe
├── chapter_2.json    # pending
├── chapter_3.json    # pending
├── chapter_4.json    # pending
├── chapter_5.json    # pending
├── chapter_6.json    # pending
├── chapter_7.json    # pending
└── chapter_8.json    # pending
```

Formato: cada entrada especifica `category` (tag de categoria de `servo_packaging`) y `count` (numero de cajas requeridas).

---

## Decisiones de implementacion

### Progreso global via SavedData
El progreso de entrega se almacena en `DeliverySavedData` (world-level). Todos los terminales del mundo leen y escriben el mismo estado. Si el jugador destruye el terminal y lo reconstruye, el progreso no se pierde.

### LAUNCH manual — no auto-complete
Al completar el 100% de las entregas, el boton LAUNCH se habilita en el GUI. El jugador debe abrirlo y presionarlo. Esto da al jugador control sobre cuando exactamente cierra el capitulo.

### Puertos como passthrough
Los `DeliveryPort` delegan a `tryInsertItem` del master. Actualmente no tienen buffer propio — si el item es rechazado, se le devuelve al jugador. Buffer de 1 slot + senal redstone es una decision pendiente.

### DeliveryCompleteEvent es la interfaz con servo_core
El mod no sabe nada de ProgressiveStages ni de stages. Solo dispara el evento. `servo_core` lo escucha, otorga el stage y maneja las recompensas. Esto mantiene servo_delivery standalone.

### GeckoLib en el Terminal
El Terminal es el bloque visualmente central del modpack. El renderer ya funciona y muestra texto (capitulo + progreso) en la cara del bloque como overlay. Las animaciones reales (idle/celebration) esperan modelo Blockbench definitivo.
