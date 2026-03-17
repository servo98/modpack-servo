# Sistema: Terminal de Entrega (Space Elevator) — Diseno Completo

> Modulo: **servo_delivery** (mod standalone). Coordination con servo_core para stage unlocks.
> Dependencias: ninguna (acepta raw items directamente). FTB Quests (auto-complete), ProgressiveStages (stage unlock via servo_core), FTB Teams (multiplayer sync)
> Prioridad: CRITICA — sin esto no hay progresion de capitulos
> Relacionado: [Progression](progression.md), [Game Loop](game-loop.md), [Create Automation](create-automation.md)

---

# 1. CONCEPTO

El Terminal de Entrega es el eje central del modpack. Es un multibloque que el jugador construye con bloques que recibe como recompensa de quest. Inspirado en el Space Elevator de Satisfactory: insertas items requeridos y al completar todos, avanzas de capitulo.

El Terminal acepta **items directos** (raw items sueltos o en stack). No requiere empacar. Los drops de boss tambien van directo.

---

# 2. EL MULTIBLOQUE

## 2.1 Estructura fisica

```
Vista frontal (3 ancho x 3 alto):

  [Antena ]                    <- Decorativo (particulas cuando activo)
  [Puerto][Pantalla][Puerto]   <- Fila funcional
  [Base  ][Base   ][Base  ]   <- Cimentacion

Profundidad: 1 bloque (plano)
Total: 9 bloques
```

### Bloques servo_delivery:
| Bloque | ID | Funcion | Cant. |
|--------|----|---------|-------|
| Terminal de Entrega (Pantalla) | `servo_delivery:delivery_terminal` | Bloque principal. Click derecho = GUI. Almacena progreso. | 1 |
| Puerto de Entrega | `servo_delivery:delivery_port` | Acepta items por automation (hopper/funnel/belt/exporter). | 2 |
| Base del Elevator | `servo_delivery:elevator_base` | Estructural. | 3 |
| Antena del Elevator | `servo_delivery:elevator_antenna` | Decorativo. Particulas y beam de luz al completar capitulo. | 1 |

### Propiedades:
- **Indestructible una vez armado**: dureza -1 (como bedrock). No se rompen, no se mueven con Carry On, no explotan.
- **Progreso global**: todos los terminales del mundo comparten el mismo progreso (SavedData). Si destruyes y reconstruyes el terminal, el progreso se mantiene. Se pueden tener multiples terminales activos simultaneamente.
- **Chunk forzado**: el chunk del Terminal se force-loadea automaticamente al armarlo. *(pendiente de implementar)*
- **Validacion de multibloque**: al colocar el Terminal, servo_delivery verifica que los 9 bloques esten en la formacion correcta. Si no, muestra "Estructura incompleta" y no se activa.

## 2.2 Como lo obtiene el jugador

**Recompensa de quest temprana en Ch1** (ej: "Cocina tu primera comida en el Cooking Pot"). El jugador recibe los 9 bloques y los coloca donde quiera — idealmente cerca de su base.

**Red de seguridad**: si los pierde (bug, accidente, lava), se desbloquea receta de crafteo:
| Bloque | Receta de backup |
|--------|-----------------|
| Terminal de Entrega | 4 Iron Ingot + 2 Redstone + 2 Glass + 1 Crafting Table |
| Puerto de Entrega | 2 Iron Ingot + 1 Hopper + 1 Chest |
| Base del Elevator | 3 Iron Block + 3 Stone Brick |
| Antena del Elevator | 4 Iron Ingot + 1 Lightning Rod |

La receta se desbloquea SOLO despues de haber recibido los bloques por quest (stage check). Previene crafteo prematuro pero permite recovery.

---

# 3. GUI DE LA PANTALLA

## 3.1 Layout

Click derecho en el Terminal de Entrega:

```
+------------------------------------------------------+
|  TERMINAL DE ENTREGA — Capitulo 1: Raices            |
|                                                       |
|  Progreso: ########........  52%                      |
|                                                       |
|  +---------------------------------------------+     |
|  | ENTREGAS REQUERIDAS                          |     |
|  |                                              |     |
|  | [#] Caja de Comida Variada    12/16          |     |
|  | [#] Caja de Crops Basicos      8/8  ok       |     |
|  | [ ] Raiz del Guardian          0/1           |     |
|  | [#] Set de Iron Tools          1/1  ok       |     |
|  |                                              |     |
|  +---------------------------------------------+     |
|                                                       |
|  +------------------+                                |
|  |  [ LAUNCH ]      |  <- Habilitado al 100%          |
|  +------------------+                                |
|                                                       |
|  Tip: Click derecho en el Terminal o en los           |
|  Puertos laterales con Cajas de Envio en mano.        |
+------------------------------------------------------+
```

### Elementos del GUI:
| Elemento | Funcion |
|----------|---------|
| **Titulo** | Capitulo actual + nombre tematico |
| **Barra de progreso** | Porcentaje global (items entregados / total requerido) |
| **Lista de entregas** | Cada requisito con icono, nombre, contador, checkmark al completar |
| **Boton LAUNCH** | Aparece habilitado cuando todas las entregas estan al 100%. El jugador debe presionarlo para completar el capitulo. |
| **Tooltip** | Hover sobre requisito = descripcion + pista de como obtenerlo *(pendiente)* |
| **Tip contextual** | Cambia segun progreso: "Cocina variedad", "Derrota al boss", "Casi listo!" |

## 3.2 Como se insertan items

El jugador inserta items haciendo **click derecho** directamente sobre el bloque Terminal o sobre los bloques Puerto, con el item en mano. No hay un slot de GUI para arrastrar items.

Flujo de validacion al hacer click derecho:
1. servo_delivery verifica:
   - Corresponde a un requisito de la entrega actual?
   - Aun se necesitan mas de ese tipo?
2. **Si valido**: items se consumen del stack en mano (hasta la cantidad necesaria), contador sube, particulas verdes, sonido "cha-ching"
3. **Si no valido**: item no se consume. Mensaje rojo en ActionBar: "Este item no es necesario para la entrega actual"
4. **Si ya completo ese tipo**: "Ya entregaste suficientes de este tipo"
5. **Al completar todo**: el boton LAUNCH se habilita en el GUI. El jugador debe abrir el GUI y presionarlo para cerrar el capitulo.

## 3.3 Los Puertos de Entrega (automation)

Los 2 bloques `delivery_port` a los lados del Terminal aceptan items por click derecho o por automatizacion:

- **Click derecho**: igual que el Terminal — valida y consume si el item es correcto
- **Automatizacion** *(pendiente)*: Hopper, Create Funnel, Create Belt (con Create Packager/Frogport), RS Exporter, Chute
- **Comportamiento actual**: los puertos delegan al master (`tryInsertItem`). Son passthrough — no tienen buffer propio.
- **Buffer de 1 slot por puerto** *(pendiente)*: retener items rechazados en vez de tirarlos
- **Senal de redstone por rechazo** *(pendiente)*: Puerto emite redstone 15 cuando tiene item rechazado (para que Create filtre)

Progresion de uso:
- **Ch1-Ch3**: jugador lleva items a mano al GUI
- **Ch4+**: Create belt -> Puerto. Pipeline automatico con Packager/Stock Link.
- **Ch5+**: RS Exporter -> Puerto. Full digital logistics.

---

# 4. ENTREGAS POR CAPITULO

## 4.0 Filosofia

Cada entrega debe:
1. **Demostrar dominio del capitulo** — usaste las mecanicas nuevas
2. **Ser alcanzable sin Create** — todo se puede hacer a mano en capitulos tempranos
3. **Ser mas eficiente con Create** — automatizar ahorra tiempo, no es requisito obligatorio
4. **Incluir 1 item de boss** — obliga a pelear antes de completar
5. **Escalar en cantidad acumulada** — cada capitulo pide items de capitulos anteriores + nuevos
6. **Solo produccion, nunca equipo personal** — materias primas y productos procesados, no herramientas/armas del jugador

> Para las tablas completas de entregas por capitulo con cantidades exactas ver [progression.md → Entregas al Space Elevator](progression.md#entregas-al-space-elevator-por-capitulo).

## 4.1 Capitulo 1: "Primeras Raices"

Manual puro. Cantidades pequenas.

| Item requerido | Cant. | Como se obtiene |
|---------------|-------|-----------------|
| Vegetable Soup | 16 | Cooking Pot |
| Beef Stew | 16 | Cocina con carne |
| Comida variada (min 8 tipos) | 16 | Diversidad culinaria |
| Crops Ch1 (cualquiera) | 32 | Farming |
| Pescado cocinado | 8 | Exploracion |
| Iron Ingot | 32 | Mineria |
| Leather + String | 16+16 | Caza/exploracion |
| Raiz del Guardian | 1 | **Boss** |
| **Total** | **~153** | ~3 horas manual |

## 4.2 Capitulo 2: "Engranajes"

Belts mueven cosas. Todo de Ch1 sigue pidiendo.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Cooking Pot recipe (cualquiera) | 32 | Acumulado (x2) |
| Cutting Board output | 32 | Acumulado |
| Cerveza/Hidromiel (Keg) | 16 | **Nuevo** |
| Queso (B&C) | 16 | **Nuevo** |
| Crops Croptopia | 64 | **Nuevo** |
| Comida variada (min 14 tipos) | 32 | Acumulado |
| Andesite Alloy | 32 | **Nuevo** — primer Create |
| Mandibula de la Bestia | 1 | **Boss** |
| **Total** | **~225** | |

## 4.3 Capitulo 3: "Automatizacion"

Maquinas procesan. Las cantidades ya piden Create.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado (x2) |
| Cutting Board output | 32 | Acumulado |
| Keg product | 32 | Acumulado (x2) |
| Wheat Flour | 128 | **Nuevo** — Millstone |
| Iron Sheet | 64 | **Nuevo** — Press |
| Copper Sheet | 64 | **Nuevo** — Press |
| Fan-washed output | 32 | **Nuevo** — Encased Fan |
| Slicer output | 64 | **Nuevo** — Slice&Dice |
| Jugo/smoothie (Expanded Delight) | 32 | **Nuevo** |
| Comida variada (min 22 tipos) | 32 | Acumulado |
| Blaze Rod | 16 | **Nuevo** — Nether |
| Engranaje del Coloso | 1 | **Boss** |
| **Total** | **~561** | |

## 4.4 Capitulo 4: "La Fabrica"

Fabrica completa. Steam Engine porque Water Wheel ya no alcanza.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado |
| Keg product | 32 | Acumulado |
| Wheat Flour | 128 | Acumulado |
| Iron/Copper Sheet | 128 | Acumulado (x2) |
| Slicer output | 64 | Acumulado |
| Brass Ingot | 128 | **Nuevo** — Mixer |
| Precision Mechanism | 8 | **Nuevo** — Sequenced Assembly |
| Crushed ore (cualquiera) | 128 | **Nuevo** — Crushing Wheel |
| Deployer recipe output | 32 | **Nuevo** — Deployer |
| Feast completo (FD) | 8 | **Nuevo** |
| Wok recipe (servo_cooking) | 32 | **Nuevo** |
| Baker's Oven recipe | 32 | **Nuevo** |
| Comida variada (min 30 tipos) | 32 | Acumulado |
| Senal Fantasma | 1 | **Boss** |
| **Total** | **~807** | |

## 4.5 Capitulo 5: "La Red"

Escala industrial. Trenes mueven entre ubicaciones.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado |
| Keg product | 32 | Acumulado |
| Sheets (iron+copper+brass) | 256 | Acumulado (x2) |
| Flour | 128 | Acumulado |
| Crushed ore | 128 | Acumulado |
| Slicer output | 64 | Acumulado |
| Wok + Baker's recipes | 64 | Acumulado |
| Iron/Copper/Brass Rod | 64 | **Nuevo** — Rolling Mill |
| Wire (cualquier tipo) | 64 | **Nuevo** — Rolling Mill |
| Enchanted Book III+ | 16 | **Nuevo** — Enchantment Industry |
| Comida exotica (crops Ch5) | 64 | **Nuevo** |
| Comida variada (min 38 tipos) | 32 | Acumulado |
| Quartz | 64 | **Nuevo** — material para Refined Storage |
| Nucleo del Arquitecto | 1 | **Boss** |
| **Total** | **~1041** | |

## 4.6 Capitulo 6: "Maestria"

Menos volumen, items dificiles.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Sheets + Rods + Wire (variados) | 128 | Acumulado |
| Crushed ore | 64 | Acumulado |
| ALL workstations output | 64 | Acumulado |
| Slicer output | 32 | Acumulado |
| Comida variada (min 42 tipos) | 32 | Acumulado |
| Netherite Scrap | 16 | **Nuevo** |
| Netherite Ingot | 4 | **Nuevo** |
| End Stone (Dragons Plus Ending) | 32 | **Nuevo** |
| Blue Ice (Dragons Plus Freezing) | 32 | **Nuevo** |
| Multi-step recipe (3+ workstations) | 32 | **Nuevo** |
| Hoz del Senor | 1 | **Boss** |
| **Total** | **~437** | |

## 4.7 Capitulo 7: "Profundidades"

Dungeon farming.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Factory output variado | 128 | Acumulado |
| ALL workstations output | 64 | Acumulado |
| Comida variada (min 44 tipos) | 32 | Acumulado |
| Unique Jewelry | 3 | **Nuevo** — dungeon farming |
| Esencia de Dungeon | 16 | **Nuevo** — champions en dungeon |
| Loot del Nucleo | 8 | **Nuevo** — Llave del Nucleo |
| Cristal del Nucleo | 1 | **Boss** |
| **Total** | **~252** | |

## 4.8 Capitulo 8 (FINAL): "El Legado"

Todo combinado. La prueba final.

| Item requerido | Cant. | Nuevo/Acumulado |
|---------------|-------|-----------------|
| Comida variada (min 48 tipos) | 64 | Acumulado |
| Comida legendaria (endgame recipes) | 32 | **Nuevo** |
| Factory output (sheets+rods+wire+crushed) | 256 | Acumulado |
| ALL workstations output | 64 | Acumulado |
| Cristal del Nucleo | 8 | **Nuevo** — dungeon endgame farming |
| Trofeos de Boss (1 de cada) | 7 | **Nuevo** — todos los bosses |
| Fragmento del Devorador | 1 | **Boss** |
| **Total** | **~432** | |

## 4.9 Resumen de escalado

| Cap | Total items | Boss drop | Foco de la entrega |
|-----|-------------|-----------|-------------------|
| 1 | ~153 | 1 | Manual: cocina + farming basico |
| 2 | ~225 | 1 | Belts: Keg, Create basico, Croptopia |
| 3 | ~561 | 1 | Maquinas: Press, Mill, Fan, Slicer obligatorios |
| 4 | ~807 | 1 | Fabrica: Mixer, Crusher, Deployer, Sequenced Assembly |
| 5 | ~1041 | 1 | Industrial: Rolling Mill, Enchanting, trains/logistics |
| 6 | ~437 | 1 | Maestria: items dificiles (Netherite, Dragons Plus) |
| 7 | ~252 | 1 | Dungeon: farming de jewelry, esencia, loot |
| 8 | ~432 | 1 | Final: todo combinado |

---

# 5. FLUJO TECNICO: COMPLETAR UN CAPITULO

```
Jugador presiona LAUNCH cuando todas las entregas estan completas
  |
  +- servo_delivery valida que el progreso es 100% y dispara DeliveryCompleteEvent
  |
  +- 1. ANIMACION: terminal brilla, particulas, sonido epico (3-5 seg)
  |
  +- 2. FTB QUESTS: quest "Entrega Ch[N] completa" -> done
  |
  +- 3. VERIFICAR (via servo_core): boss Ch[N] tambien derrotado?
  |     +- SI -> otorgar stage servo_ch[N+1] a todo el team
  |     +- NO -> mensaje: "Entrega completa. Derrota al boss para avanzar."
  |
  +- 4. SI AMBAS CONDICIONES:
  |     +- servo_core via ProgressiveStages: grant servo_ch[N+1] a todos los miembros del team
  |     +- Chat broadcast: "El equipo ha completado el Capitulo [N]!"
  |     +- Title screen grande tipo achievement
  |     +- Terminal se actualiza a entregas del capitulo siguiente
  |
  +- 5. RECOMPENSA: manejada por servo_core al escuchar DeliveryCompleteEvent
        +- servo_delivery solo dispara el evento — no sabe de items ni de stages
        +- servo_core otorga: 15 Pepe Coins + 1 Gacha Ticket
        +- Item tematico del siguiente capitulo
        +- Llave de Dungeon del tier correspondiente (si aplica)
```

### Data que servo_core trackea por team (glue entre servo_delivery y servo_dungeons):
```java
team_data.delivery_ch[N] = true/false   // entrega completa (event from servo_delivery)
team_data.boss_ch[N] = true/false       // boss derrotado (event from servo_dungeons)
// Cuando ambos son true -> grant servo_ch[N+1]
```

**Las condiciones pueden cumplirse en cualquier orden.** Boss primero -> entrega despues, o viceversa.

---

# 6. DECISIONES TOMADAS Y PENDIENTES

## Resueltas
1. **Obtencion**: recompensa de quest temprana Ch1 + receta de backup
2. **Items directos**: Terminal acepta raw items, no requiere empacar (servo_packaging eliminado)
3. **Packager Create**: Create 6.0 tiene Packager nativo para automatizar entregas en belt → Delivery Port

## Pendientes
- [ ] "Comida variada" verifica tipos unicos? Propuesta: si, estricto
- [ ] Terminal acepta items del capitulo siguiente? Propuesta: no, solo actual
- [ ] Cual quest exacta da los bloques? Propuesta: "Cocina tu primera comida"

## Resueltas (balance check sesion 12 + update sesion 17)
- [x] Recompensa: 15 Pepe Coins + 1 Gacha Ticket por capitulo (alineado con gacha-rates.md)
- [x] Unique Jewelry Ch7: 3 items (varianza de drop rates)
- [x] Curva de entregas suavizada: acumulativa, sin retrocesos
- [x] Entregas = solo produccion, nunca equipo personal del jugador
