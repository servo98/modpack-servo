# Sistema: Terminal de Entrega (Space Elevator) — Diseno Completo

> Modulo: **servo_delivery** (mod standalone). Coordination con servo_core para stage unlocks.
> Dependencias: servo_packaging (hard — acepta Cajas de Envio), FTB Quests (auto-complete), ProgressiveStages (stage unlock via servo_core), FTB Teams (multiplayer sync)
> Prioridad: CRITICA — sin esto no hay progresion de capitulos
> Relacionado: [Packaging (Cajas de Envio)](packaging.md), [Progression](progression.md), [Game Loop](game-loop.md), [Create Automation](create-automation.md)

---

# 1. CONCEPTO

El Terminal de Entrega es el eje central del modpack. Es un multibloque que el jugador construye con bloques que recibe como recompensa de quest. Inspirado en el Space Elevator de Satisfactory: insertas items requeridos y al completar todos, avanzas de capitulo.

El Terminal SOLO acepta **Cajas de Envio** (ver [packaging.md](packaging.md)), no items sueltos. Excepcion: drops de boss, que van directo.

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

El jugador inserta items haciendo **click derecho** directamente sobre el bloque Terminal o sobre los bloques Puerto, con la Caja de Envio en mano. No hay un slot de GUI para arrastrar items.

Flujo de validacion al hacer click derecho:
1. servo_delivery verifica:
   - Es Caja de Envio valida (`servo_packaging:shipping_box`) o item directo de boss?
   - Corresponde a un requisito de la entrega actual?
   - Aun se necesitan mas de ese tipo?
2. **Si valido**: item se consume del stack en mano, contador sube, particulas verdes, sonido "cha-ching"
3. **Si no valido**: item no se consume. Mensaje rojo en ActionBar: "Este item no es necesario para la entrega actual"
4. **Si ya completo ese tipo**: "Ya entregaste suficientes de este tipo"
5. **Al completar todo**: el boton LAUNCH se habilita en el GUI. El jugador debe abrir el GUI y presionarlo para cerrar el capitulo.

## 3.3 Los Puertos de Entrega (automation)

Los 2 bloques `delivery_port` a los lados del Terminal aceptan items por click derecho o por automatizacion:

- **Click derecho**: igual que el Terminal — valida y consume si el item es correcto
- **Automatizacion** *(pendiente)*: Hopper, Create Funnel, Create Belt, RS Exporter, Chute
- **Comportamiento actual**: los puertos delegan al master (`tryInsertItem`). Son passthrough — no tienen buffer propio.
- **Buffer de 1 slot por puerto** *(pendiente)*: retener items rechazados en vez de tirarlos
- **Senal de redstone por rechazo** *(pendiente)*: Puerto emite redstone 15 cuando tiene item rechazado (para que Create filtre)

Progresion de uso:
- **Ch1-Ch3**: jugador lleva cajas a mano al GUI
- **Ch4+**: Create belt -> Puerto. Pipeline automatico.
- **Ch5+**: RS Exporter -> Puerto. Full digital logistics.

---

# 4. ENTREGAS POR CAPITULO

## 4.0 Filosofia

Cada entrega debe:
1. **Demostrar dominio del capitulo** — usaste las mecanicas nuevas
2. **Ser alcanzable sin Create** — todo se puede hacer a mano
3. **Ser mas eficiente con Create** — automatizar ahorra tiempo, no es requisito
4. **Incluir 1 item de boss** — obliga a pelear antes de completar
5. **Escalar en cantidad** — Ch1 pide ~25, Ch8 pide ~90

## 4.1 Capitulo 1: "Primeras Raices"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Vegetable Soup | 4 | Cooking Pot -> empacar |
| 2 | Caja de Beef Stew | 4 | Cooking Pot -> empacar |
| 3 | Caja de Comida Variada | 8 | Min 8 tipos diferentes de comida -> empacar |
| 4 | Caja de Crops Basicos | 4 | Stack de cualquier crop Ch1 -> empacar |
| 5 | Caja de Pescado | 2 | Cualquier pez cocinado -> empacar |
| 6 | Raiz del Guardian | 1 | Drop del boss Ch1 (item directo) |
| 7 | Caja de Iron Pickaxe | 1 | Iron Pickaxe -> empacar |
| 8 | Caja de Iron Sword | 1 | Iron Sword -> empacar |
| **Total** | | **25** | **~2-3 horas** |

## 4.2 Capitulo 2: "La Mesa Servida"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Comida del Cooking Pot | 4 | Cooking Pot (recetas Ch2) -> empacar |
| 2 | Caja de Queso | 4 | B&C Flaxen/Scarlet Cheese -> empacar |
| 3 | Caja de Comida del Cutting Board | 4 | Cutting Board -> empacar |
| 4 | Caja de Bebida Fermentada | 4 | Keg (Beer, Mead, etc.) -> empacar |
| 5 | Caja de Comida Variada | 12 | Min 12 tipos diferentes |
| 6 | Caja de Crops Nuevos | 4 | Crops Croptopia Ch2 -> empacar |
| 7 | Mandibula de la Bestia | 1 | Drop del boss Ch2 (item directo) |
| 8 | Caja de Lingotes Create | 2 | Andesite Alloy x64 -> empacar |
| **Total** | | **35** | |

## 4.3 Capitulo 3: "Engranajes y Magia"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Comida del Keg | 6 | Keg (recetas Ch3) -> empacar |
| 2 | Caja de Comida Horneada | 4 | Furnace/Smoker (recetas avanzadas) -> empacar |
| 3 | Caja de Comida Variada | 16 | Min 16 tipos diferentes |
| 4 | Caja de Items Prensados | 4 | Mechanical Press output -> empacar |
| 5 | Caja de Items Molidos | 4 | Millstone output -> empacar |
| 6 | Caja de Materiales Nether | 4 | Blaze Rod, Nether Wart, Quartz -> empacar |
| 7 | Engranaje del Coloso | 1 | Drop del boss Ch3 (item directo) |
| 8 | Caja de Runas | 2 | Runas crafteadas -> empacar |
| **Total** | | **41** | |

## 4.4 Capitulo 4: "Horizontes"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Feast | 4 | FD Feasts -> empacar |
| 2 | Caja de Comida Variada | 24 | Min 24 tipos diferentes |
| 3 | Caja de Brass | 4 | Brass Ingot x64 -> empacar |
| 4 | Caja de Mechanical Crafting | 4 | Mechanical Crafter output -> empacar |
| 5 | Caja de Gemas | 2 | Gemas de Jewelry -> empacar |
| 6 | Scroll de Especializacion | 1 | Prueba de Skill Tree spec (custom item, directo) |
| 7 | Senal Fantasma | 1 | Drop del boss Ch4 (item directo) |
| **Total** | | **40** | |

## 4.5 Capitulo 5: "La Red"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Comida Variada | 24 | Min 24 tipos diferentes |
| 2 | Caja de Comida Exotica | 8 | Recetas con crops exoticos Ch5 -> empacar |
| 3 | Caja de Items Autocraft | 8 | RS Autocrafting output -> empacar |
| 4 | Caja de Enchanted Books | 4 | Enchanted Books III+ -> empacar |
| 5 | Caja de RS Components | 2 | RS Controller/Disk -> empacar |
| 6 | Caja de Items Encantados | 4 | Gear con Spell Power enchants -> empacar |
| 7 | Nucleo del Arquitecto | 1 | Drop del boss Ch5 (item directo) |
| **Total** | | **51** | |

## 4.6 Capitulo 6: "Maestria"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Comida Variada | 32 | Min 32 tipos diferentes |
| 2 | Caja de Receta Multi-Step | 8 | Recetas de 3+ workstations -> empacar |
| 3 | Caja de Netherite Gear | 2 | T3 RPG gear -> empacar |
| 4 | Caja de Tipped Arrows | 4 | Tipped Arrows Spell Power -> empacar |
| 5 | Caja de Produccion Nether | 13 | Items de fabrica Nether -> empacar |
| 6 | Hoz del Senor de las Cosechas | 1 | Drop del boss Ch6 (item directo) |
| **Total** | | **60** | |

## 4.7 Capitulo 7: "Profundidades"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Comida Variada | 48 | Min 40 tipos diferentes |
| 2 | Caja de Unique Jewelry | 3 | Unique Jewelry (loot-only) -> empacar |
| 3 | Caja de Esencia de Dungeon | 10 | Esencia de Dungeon -> empacar |
| 4 | Caja de Items del Nucleo | 4 | Loot de Dungeon del Nucleo -> empacar |
| 5 | Cristal del Nucleo | 1 | Drop del boss Ch7 (item directo) |
| **Total** | | **66** | |

## 4.8 Capitulo 8 (FINAL): "El Legado"

| # | Item requerido | Cant. | Como se obtiene |
|---|---------------|-------|-----------------|
| 1 | Caja de Comida Variada | 48 | Min 40 tipos diferentes |
| 2 | Caja de Comida Legendaria | 12 | Recetas endgame -> empacar |
| 3 | Item Maestro | 1 | Mega-craft con items de todos los capitulos (directo) |
| 4 | Caja de Arma T4 | 1 | Arma T4 custom -> empacar |
| 5 | Trofeos de Boss | 8 | 1 de cada boss (items directos) |
| 6 | Caja de Factory Output | 19 | Pipeline RS+Create output -> empacar |
| 7 | Fragmento del Devorador | 1 | Drop del boss final (item directo) |
| **Total** | | **90** | |

## 4.9 Resumen de escalado

| Cap | Cajas | Boss drop | Foco de la entrega |
|-----|-------|-----------|-------------------|
| 1 | 24 | 1 | Tutorial: cocina + farming basico |
| 2 | 34 | 1 | Variedad: queso, cutting board, cooking pot, Create basico |
| 3 | 40 | 1 | Create processing + Nether + keg avanzado |
| 4 | 39 | 1 | Brass + feasts + RPG spec |
| 5 | 50 | 1 | RS + enchants + escala industrial |
| 6 | 59 | 1 | Netherite + multi-step + Nether factory |
| 7 | 65 | 1 | Dungeon endgame farming |
| 8 | 89 | 1 | Todo combinado + boss final |

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
2. **Cajas**: sistema fisico separado (ver [packaging.md](packaging.md))
3. **Empacadora**: bloque disponible desde Ch1. Sin crafting manual previo. Deployer compat (servo_create) en Ch4.

## Pendientes
- [ ] "Caja de Comida Variada" verifica tipos unicos? Propuesta: si, estricto
- [ ] Items de boss necesitan caja? Propuesta: no, van directo
- [ ] Terminal acepta items del capitulo siguiente? Propuesta: no, solo actual
- [ ] Cual quest exacta da los bloques? Propuesta: "Cocina tu primera comida"

## Resueltas (balance check sesion 12)
- [x] Recompensa: 15 Pepe Coins + 1 Gacha Ticket por capitulo (alineado con gacha-rates.md)
- [x] Caja de Iron Tools separada en 2 cajas (Pick + Sword) para respetar regla "mismo tipo"
- [x] Unique Jewelry Ch7: bajado de 5 a 3 (varianza de drop rates)
- [x] Comida Variada Ch8: bajado de 64 a 48 (min 40 tipos distintos)
- [x] Curva de cajas suavizada: sin retrocesos entre capitulos
- [x] Empacadora: quitado Piston (requeria Redstone) de la receta
