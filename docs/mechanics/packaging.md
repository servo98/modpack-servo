# Sistema: Cajas de Carton y Empaque — Diseno Completo (v3)

> Modulo: **servo_packaging** (mod standalone, 0 dependencias). Create compat en servo_create addon.
> Sin GUI en las cajas — interaccion inmersiva en el mundo (estilo Cooking Pot de FD)
> Relacionado: [Terminal de Entrega](space-elevator.md), [Create Automation](create-automation.md), [Cooking](cooking.md), [Architecture](../architecture.md)
> Estado: **CODIGO COMPLETO** — ver [docs/status/servo-packaging.md](../status/servo-packaging.md)

---

# 1. CONCEPTO

Las Cajas de Envio son un proceso **inmersivo y fisico**, sin menus de empaque. El jugador interactua directamente en el mundo: dobla carton en la empacadora, coloca la caja abierta en el piso, mete items con click derecho, y la caja se sella sola al llenarse. Todo se ve y se escucha.

Cuando el jugador llega a Create, el proceso se automatiza: hoppers/funnels alimentan la empacadora y llenan cajas. En servo_create, deployers en belts automatizan completamente.

```
MANUAL:                          AUTOMATIZADO (Create via hoppers):

  [Empacadora: dobla carton]       [Hopper: carton → Empacadora]
       |                                  |
  Recoge Caja Abierta               [Hopper: extrae open_box]
       |                                  |
  [Coloca en piso]                 [Hopper: items → Open Box en piso]
       |                                  |
  Click derecho: meter items        Auto-seal → recoger
       |
  Caja se sella sola (16 items)
       |
  Recoger / Colocar / Entregar
```

---

# 2. ITEMS DEL SISTEMA

| Item | ID | Descripcion | Stack |
|------|----|------------|-------|
| **Carton Plano** | `servo_packaging:flat_cardboard` | Lamina de carton sin doblar. Material base. | x64 |
| **Caja Abierta** | `servo_packaging:open_box` | Caja doblada. Es un BlockItem — se coloca en el piso. | x16 |
| **Caja de Envio** | `servo_packaging:shipping_box` | Caja sellada con contenido. Muestra icono del contenido en inventario. | x16 |

### Carton Plano — receta
```
Crafting Table (shaped):
[Paper  ][Paper ]
[Paper  ][Paper ]
[    String     ]

Output: 4x Carton Plano
```

### Caja de Envio — datos internos
```java
// DataComponent BoxContents (no NBT)
BoxContents(itemId: "farmersdelight:vegetable_soup", count: 16, category: "food")
```

**Tooltip visible:**
```
Caja de Envio
Contiene: 16x Vegetable Soup    [verde — color por categoria]
[gris] Click derecho: desempacar / colocar
```

**En inventario:** sprite de caja cafe con el icono del contenido superpuesto al 50%.

**Etiqueta por categoria:**
| Color | Categoria | Tag | Ejemplos |
|-------|-----------|-----|----------|
| Verde | Comida | `category/food` | Soups, smoothies, cheese |
| Amarillo | Crops/raw | `category/crops` | Wheat, Iron, Blaze Rod |
| Azul | Procesados | `category/processed` | Andesite Alloy, Enchanted Books |
| Morado | RPG/Magia | `category/magic` | Runas, Jewelry, Gear |
| Dorado | Especial | `category/special` | Trofeos, items raros |
| Gris | General | (sin tag) | Cualquier otro item packable |

---

# 3. FLUJO DE INTERACCION (Sin GUI, inmersivo)

## 3.1 Empacadora — dobla carton en cajas

### El bloque
| Propiedad | Valor |
|-----------|-------|
| ID | `servo_packaging:packing_station` |
| Receta | 4 Iron Ingot + 4 Plank + 1 Hopper |
| Stage | `servo_ch1` (disponible desde el inicio) |
| GUI | Si — 2 slots (input: carton, output: caja abierta) + barra de progreso |

### Como funciona
1. **Abrir GUI**: click derecho en la empacadora
2. **Input**: poner Carton Plano en el slot izquierdo
3. **Proceso**: barra de progreso (40 ticks = 2 segundos)
4. **Output**: 1 Caja Abierta aparece en el slot derecho (se stackea)
5. **Animacion BER**: 5 paneles de carton se doblan de 0° a 90°
6. **Continuo**: si hay mas carton, sigue doblando automaticamente

### Hopper automation
| Cara | Funcion |
|------|---------|
| Arriba (top) | Input: acepta flat_cardboard |
| Abajo/lados | Output: extrae open_box |

## 3.2 Caja Abierta — llenar en el piso

### Colocar
- Click derecho con Caja Abierta en mano sobre cualquier superficie
- Se coloca como bloque (12x8x12 pixeles, mas baja que un bloque completo)
- Se orienta mirando al jugador (4 direcciones: N/S/E/W)

### Meter items
- **Click derecho con items en mano**: mete el stack completo de golpe (hasta llenar)
- Solo acepta items del **mismo tipo** (el primer item define el tipo)
- Solo acepta items con tag `#servo_packaging:packable`
- Los items se ven **flotando dentro de la caja** (BER, bobbing animation)
- Layout visual: 1 centrado, 2 lado a lado, 3 triangulo, 4 cuadrado 2x2
- Mensaje en ActionBar: "Anadido [Item]"

### Sacar items
- **Click derecho mano vacia**: saca el ultimo item
- **Shift + click derecho mano vacia**: recoge la caja abierta (dropea contenido primero)

### Auto-seal
- Al llegar a 16 items: la caja se sella automaticamente
- Sonido: barrel_close
- Visual: modelo cambia a caja cerrada con cinta
- **Estampa BER**: icono del item empacado aparece en la cara frontal de la caja sellada

### Recoger caja sellada
- Click derecho (con cualquier cosa en mano o vacio): recoge como ShippingBox item
- El bloque desaparece, recibes el ShippingBox con su BoxContents

### Romper
- **Caja abierta**: dropea contenido + 1 open_box item
- **Caja sellada**: dropea como ShippingBox item (contenido intacto)

### Hopper insertion
- Solo por arriba (top face)
- Mismo tipo obligatorio
- Auto-seal al llenar

## 3.3 Caja de Envio — desempacar o colocar

### Dos comportamientos:
| Accion | Resultado |
|--------|-----------|
| **Click derecho en superficie** | Coloca como bloque sellado (OpenBoxBlock SEALED=true) |
| **Click derecho en aire** | Desempaca: items van al inventario + devuelve 1 open_box vacia |

### Al colocar como bloque:
- Se coloca como caja sellada orientada hacia el jugador
- Muestra estampa del contenido en la cara frontal (BER)
- Click derecho para recoger de vuelta como ShippingBox item

### Al desempacar en aire:
- Items van al inventario (overflow: dropea al suelo)
- Recibes 1 open_box vacia de vuelta
- Mensaje: "Desempacado: 16x [Item]"

---

# 4. AUTOMATIZACION CON CREATE (Futuro — servo_create addon)

## 4.1 Principio: hoppers nativos + funnels Create

La automatizacion basica ya funciona con hoppers de vanilla:
- Hopper arriba de empacadora: mete carton plano
- Hopper debajo: extrae cajas abiertas
- Hopper arriba de caja abierta: mete items (auto-seal al llenar)

servo_create agregara:
- Funnel compat mejorada
- Deployer support en belts
- Smart chute para cajas selladas

## 4.2 Pipeline completo con Create (servo_create)

```
PIPELINE: Deployer dobla + Deployer llena

[Chest: Carton Plano]
        | Funnel
[Belt: Carton Plano viajando]
        |
[Deployer 1: modo "Use"] -> Convierte Carton en Caja Abierta
        |
[Belt: Caja Abierta viajando]
        |
[Deployer 2: cargado con items]
  -> Detecta Caja Abierta
  -> Inserta items
  -> Sale Caja de Envio cerrada
        |
[Belt: Caja cerrada]
        |
[Terminal de Entrega]
```

## 4.3 Produccion de Carton con Create

```
[Sugar Cane Farm -> Create Harvester]
        | belt
[Mechanical Press: Sugar Cane -> Paper]
        | belt
[Mechanical Crafter: 4 Paper + 1 String = 4 Carton Plano]
        | belt
[Chest buffer / directo a Empacadora]
```

---

# 5. FABRICA EJEMPLO COMPLETA (Ch4+)

```
=== FABRICA DE CAJAS DE VEGETABLE SOUP ===

NIVEL 1 - FARMING
  Create Harvester x 4 (Carrot, Potato, Cabbage, Tomato)
        | belts convergentes

NIVEL 2 - COCINA
  Slice&Dice -> Cutting Board (corta ingredientes)
        | belt
  Cooking Pot + Water (Slice&Dice automatiza)
        | belt con Vegetable Soup

NIVEL 3 - CARTON
  Sugar Cane Harvester -> Press -> Paper
  Sheep farm -> Deployer shear -> Wool -> Crafter -> String
  Paper + String -> Crafter -> Carton Plano
  Carton Plano -> Empacadora -> Caja Abierta
        | hopper/belt

NIVEL 4 - EMPAQUE
  Caja Abierta (piso) + hoppers con Vegetable Soup
        |
  Auto-seal al llenar
        |
  Recoger Caja de Envio cerrada

NIVEL 5 - ENTREGA
  Belt/hopper -> Terminal de Entrega
  (o Tren si el Terminal esta lejos)
```

---

# 6. COMPARACION DE METODOS

| Aspecto | Manual | Hoppers | Create Pipeline |
|---------|--------|---------|----------------|
| Interfaz | Click directo | Ninguna | Ninguna |
| Velocidad | ~30 seg/caja | ~10 seg/caja | ~5 seg/caja |
| Atencion | Constante | Poner hoppers | Corre solo |
| Satisfaccion | Llenar caja a mano | Ver flujo automatico | Ver fabrica entera |
| Create necesario | No | No | Si (servo_create) |

---

# 7. IMPLEMENTACION TECNICA

## 7.1 Mod standalone: servo_packaging

Separado en su propio mod. 0 dependencias externas. Namespace `servo_packaging:`.
Create compat va en **servo_create** (addon separado).
Terminal de Entrega (**servo_delivery**) acepta cajas como hard dependency.

Ver [Architecture](../architecture.md) para el grafo completo.

## 7.2 Estructura de codigo

```
servo-packaging/src/main/java/com/servo/packaging/
  ServoPackaging.java              # Entry point
  PackagingRegistry.java           # Todo registrado aqui
  component/BoxContents.java       # DataComponent (itemId, count, category)
  block/PackingStationBlock.java   # Empacadora (solo dobla, abre GUI)
  block/PackingStationBlockEntity.java  # IDLE→FOLDING→DONE, 2 slots, ticker
  block/OpenBoxBlock.java          # Caja placeable (ITEMS_STORED, SEALED, FACING)
  block/OpenBoxBlockEntity.java    # Items, hopper compat, auto-seal, WorldlyContainer
  item/FlatCardboardItem.java      # Item simple
  item/ShippingBoxItem.java        # Dual: unbox en aire, place en superficie
  client/PackagingClientSetup.java # BERs, menu, item decorator
  client/PackingStationBER.java    # Animacion de doblado
  client/PackingStationScreen.java # GUI empacadora
  client/OpenBoxBER.java           # Items visibles + estampa sealed
  client/ShippingBoxDecorator.java # Overlay icono contenido en inventario
```

## 7.3 DataComponent (no NBT)

MC 1.21.1 usa DataComponents. Shipping Box usa `BoxContents` (record):
```java
public record BoxContents(ResourceLocation itemId, int count, String category)
```
- Codec + StreamCodec implementados
- Persistente en world save
- Sincronizado cliente/servidor

## 7.4 Tags del sistema

| Tag | Proposito |
|-----|-----------|
| `servo_packaging:packable` | Items que pueden empacarse |
| `servo_packaging:pack_size_1` | 1 item por caja (futuro) |
| `servo_packaging:pack_size_8` | 8 items por caja (futuro) |
| `servo_packaging:pack_size_16` | 16 items por caja (actual default) |
| `servo_packaging:category/food` | Color verde |
| `servo_packaging:category/crops` | Color amarillo |
| `servo_packaging:category/processed` | Color azul |
| `servo_packaging:category/magic` | Color morado |
| `servo_packaging:category/special` | Color dorado |

---

# 8. DECISIONES TOMADAS

1. **Sin GUI en las cajas** — todo inmersivo estilo Cooking Pot. Solo la empacadora tiene GUI (2 slots).
2. **Empacadora solo dobla carton** — no empaca items. Simplifica el flujo y da mas libertad al jugador.
3. **Caja Abierta es bloque placeable** — se llena en cualquier lugar, no atado a la estacion.
4. **Auto-seal a 16 items fijo** — simple. Tags `pack_size_*` preparados para variar en futuro.
5. **ShippingBox dual** — click aire = unbox, click superficie = place. Versatil sin menus.
6. **Carry On bloqueado** — tag `neoforge:immovable` evita conflicto con el mod.
7. **Mod standalone** — reutilizable. Create compat en addon separado (servo_create).

## Pendientes

- [ ] Cantidades variables por tipo (usar tags `pack_size_*` en vez de 16 fijo)
- [ ] Sonidos custom (actualmente usa placeholders vanilla)
- [ ] Texturas/modelos de artista (funcionan los generados pero son placeholder)
- [ ] Animacion de cierre (actualmente instantaneo al llegar a 16)
