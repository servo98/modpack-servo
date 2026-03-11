# Sistema: Cajas de Carton y Empaque — Diseno Completo (v2)

> Modulo: servo_core (Java), Create como soft dependency
> Sin GUI — interaccion inmersiva en el mundo (estilo Cooking Pot de FD)
> Relacionado: [Terminal de Entrega](space-elevator.md), [Create Automation](create-automation.md), [Cooking](cooking.md)

---

# 1. CONCEPTO

Las Cajas de Envio son un proceso **inmersivo y fisico**, sin menus. El jugador interactua directamente en el mundo: dobla carton, mete items, cierra la caja. Todo se ve y se escucha. Es la misma filosofia del Cooking Pot de Farmer's Delight — pones ingredientes encima, esperas, recoges el resultado.

Cuando el jugador llega a Create, el proceso se automatiza de forma natural: belts traen materiales, deployers doblan y llenan, las cajas cerradas salen por el otro lado. Sin interfaces, solo ingenieria visual.

```
MANUAL:                          AUTOMATIZADO (Create):

  Jugador click items              Belt -> Deployer dobla
       |                                  |
  [Empacadora]                     [Empacadora / Deployer llena]
       |                                  |
  Recoge caja cerrada              Belt -> Terminal de Entrega
```

---

# 2. ITEMS DEL SISTEMA

| Item | ID | Descripcion | Stack |
|------|----|------------|-------|
| **Carton Plano** | `servo_core:flat_cardboard` | Lamina de carton sin doblar | x64 |
| **Caja Abierta** | `servo_core:open_box` | Caja doblada, sin contenido, lista para llenar | x16 |
| **Caja de Envio** | `servo_core:shipping_box` | Caja cerrada con contenido (NBT interno) | x16 por tipo |

### Carton Plano — receta
```
Crafting Table (shaped):
[Paper  ][Paper ]
[Paper  ][Paper ]
[    String     ]

Output: 4x Carton Plano
```

Recursos: Paper (cana de azucar) + String (aranas/lana). Renovable desde Ch1.

### Caja de Envio — datos internos (NBT)
```java
// El jugador NUNCA ve esto, solo ve el tooltip
{
  "contents": "farmersdelight:vegetable_soup",
  "count": 4,
  "category": "food"  // define color de etiqueta
}
```

**Tooltip visible:**
```
Caja de Envio
Contiene: 4x Vegetable Soup
[gris] Entregar al Terminal de Entrega
```

**Etiqueta por categoria:**
| Color | Categoria | Ejemplos |
|-------|-----------|----------|
| Verde | Comida | Soups, smoothies, cheese |
| Amarillo | Crops/raw | Wheat, Iron, Blaze Rod |
| Azul | Procesados | Andesite Alloy, Enchanted Books |
| Morado | RPG/Magia | Runas, Jewelry, Gear |
| Dorado | Especial | Trofeos, items raros |

### Cantidades por caja segun tipo
| Tipo de contenido | Items por caja |
|-------------------|---------------|
| Comida cocinada | 4 |
| Bebidas | 4 |
| Crops/materiales | 16 |
| Items procesados | 8 |
| Gear/herramientas | 1 |
| Items valiosos | 1 |

---

# 3. INTERACCION MANUAL (Sin GUI, inmersivo)

## 3.1 Doblar carton -> Caja Abierta

**En cualquier lugar, sin bloque:**
- Jugador tiene Carton Plano en mano
- Click derecho en el aire o sobre cualquier superficie
- Animacion: manos doblan el carton (~0.5s)
- Sonido: crujido de carton
- Se consume 1 Carton Plano -> aparece 1 Caja Abierta en inventario

**En crafting table (batch):**
- Shapeless: 1 Carton Plano -> 1 Caja Abierta
- Para doblar muchas de golpe

## 3.2 La Empacadora — bloque inmersivo

### El bloque
| Propiedad | Valor |
|-----------|-------|
| ID | `servo_core:packing_station` |
| Receta | 4 Iron Ingot + 4 Plank + 1 Hopper (sin Redstone, accesible en Ch1 temprano) |
| Stage | `servo_ch1` (disponible desde el inicio, parte del tutorial) |
| Modelo | Mesa de trabajo con superficie plana, bordes levantados |

### Como funciona (estilo Cooking Pot)

**Estado 1: Vacia**
- La Empacadora esta vacia, superficie limpia
- Visual: mesa de empaque vacia

**Estado 2: Colocar Caja Abierta**
- Click derecho con Caja Abierta en mano
- La Caja Abierta aparece ENCIMA de la Empacadora (modelo 3D, como comida en el Cooking Pot)
- Visual: caja de carton abierta sentada en la mesa
- Sonido: "thud" suave de carton
- La Caja Abierta se consume del inventario

**Estado 3: Meter items**
- Click derecho con items en mano (ej: Vegetable Soup)
- Cada click mete 1 item (o shift-click mete la cantidad completa)
- Visual: los items aparecen DENTRO de la caja abierta (se ven asomando, como el Cooking Pot muestra ingredientes)
- Sonido: "clonk" de item cayendo en caja
- Un contador flotante aparece: "2/4" -> "3/4" -> "4/4"

**Estado 4: Caja llena -> se cierra automaticamente**
- Al llegar a la cantidad correcta (ej: 4/4 para comida):
  - Animacion: la caja se cierra sola (~0.5s)
  - Sonido: carton cerrandose + cinta adhesiva "rip"
  - Visual: caja cerrada con etiqueta de color encima de la Empacadora
  - Particulas sutiles de confirmacion

**Estado 5: Recoger**
- Click derecho en la Empacadora -> recoge la Caja de Envio cerrada al inventario
- La Empacadora vuelve a Estado 1 (vacia)
- O: si hay un hopper/funnel debajo -> la caja se extrae automaticamente

### Resumen del flujo visual

```
Mesa vacia    ->  [Click: Caja Abierta]  ->  Caja abierta visible
                                                  |
              ->  [Click: Item x1]       ->  Item aparece dentro "1/4"
              ->  [Click: Item x1]       ->  "2/4"
              ->  [Click: Item x1]       ->  "3/4"
              ->  [Click: Item x1]       ->  "4/4" -> CIERRE AUTOMATICO
                                                  |
              ->  [Click vacio]          ->  Recoge Caja de Envio cerrada
```

### Reglas importantes
- Solo acepta items del **mismo tipo** por caja (no puedes mezclar)
- Si metes un item de tipo diferente al primero -> mensaje: "Solo puedes empacar items del mismo tipo"
- Si metes un item que no es empacable -> mensaje: "Este item no se puede empacar"
- Click derecho SIN item en mano en Estado 3 (items parciales) -> te devuelve los items y la caja abierta (cancelar)
- Si rompes la Empacadora con items dentro -> todo dropea al suelo

---

# 4. AUTOMATIZACION CON CREATE (Sin interfaz)

## 4.1 Principio: la Empacadora como "Basin de cajas"

La Empacadora se comporta como un Basin de Create: tiene lados de input que aceptan Funnels y un output que puede alimentar belts. No necesita GUI porque todo se hace por los lados.

```
Vista de la Empacadora con Create:

          [Funnel arriba: mete Caja Abierta]
                    |
    [Funnel lado: mete items] -> [EMPACADORA] -> [Funnel/belt abajo: saca Caja cerrada]
```

### Lados de la Empacadora (inventario por cara)
| Cara | Funcion | Que acepta |
|------|---------|------------|
| Arriba (top) | Input de Caja Abierta | Solo `servo_core:open_box` |
| Lados (north/south/east/west) | Input de items | Cualquier item empacable |
| Abajo (bottom) | Output | Caja de Envio cerrada |

### Comportamiento automatico:
1. Funnel/hopper mete Caja Abierta por arriba -> Estado 2
2. Funnel/hopper mete items por el lado -> se van acumulando (Estado 3)
3. Al llegar a cantidad correcta -> cierre automatico (Estado 4)
4. Caja cerrada disponible en output (abajo) -> hopper/funnel/belt la extrae
5. Vuelta a Estado 1

## 4.2 Pipeline completo: Deployer como alternativa

Si el jugador NO quiere usar la Empacadora como bloque, puede hacer TODO con Deployers en un belt:

```
PIPELINE A: Deployer dobla + Deployer llena

[Chest: Carton Plano]
        | Funnel
[Belt: Carton Plano viajando]
        |
[Deployer 1: modo "Use"] -> Convierte Carton Plano en Caja Abierta
        |
[Belt: Caja Abierta viajando]
        |
[Deployer 2: cargado con items (ej: stack de Veg Soup)]
  -> Detecta Caja Abierta en belt
  -> Inserta 4x Veg Soup
  -> Sale Caja de Envio cerrada
        |
[Belt: Caja cerrada viajando]
        |
[Belt -> Puerto de Entrega del Terminal]
```

**Como funciona Deployer 2 tecnicamente:**
- El Deployer tiene un stack de Vegetable Soup (o cualquier item empacable)
- Cuando una Caja Abierta pasa en el belt, el Deployer la detecta
- Hace "use" del item en su mano sobre la Caja Abierta
- Inserta la cantidad correcta (4 para comida) de golpe
- La Caja Abierta se convierte en Caja de Envio cerrada en el belt
- Si el Deployer no tiene suficientes items, no hace nada (la caja sigue abierta)

## 4.3 Pipeline alternativo: Empacadora central

```
PIPELINE B: Empacadora como hub

                    [Create Harvester: farms]
                            | belt
                    [Slice&Dice / Cooking Pot: cocina]
                            | belt
[Chest: Carton] -> Funnel -> [EMPACADORA] -> Funnel -> belt
                   (top)    ^ Funnel (lado)         |
                            | items de cocina    [Puerto de Entrega]
                            +-- belt ------------+
```

## 4.4 Produccion de Carton con Create

```
[Sugar Cane Farm -> Create Harvester]
        | belt
[Mechanical Press: Sugar Cane -> Paper]
        | belt
[Mechanical Crafter: 4 Paper + 1 String = 4 Carton Plano]
        | belt
[Chest buffer / directo a Empacadora]
```

String: Mechanical Crafter (2 Wool -> 4 String) o spider farm.

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
  Carton Plano -> Deployer 1 -> Caja Abierta
        | belt

NIVEL 4 - EMPAQUE
  Caja Abierta (belt) + Vegetable Soup (belt)
        |
  Deployer 2: llena caja
        |
  Caja de Envio cerrada

NIVEL 5 - ENTREGA
  Belt -> Puerto de Entrega del Terminal
  (o Tren si el Terminal esta lejos)
```

Tiempo por caja: ~5 segundos. Velocidad limitada por el Cooking Pot (~10 seg por comida).

---

# 6. COMPARACION DE METODOS

| Aspecto | Manual | Empacadora | Create Pipeline |
|---------|--------|-----------|----------------|
| Interfaz | Ninguna | Ninguna | Ninguna |
| Interaccion | Click derecho | Click derecho en bloque | Deployers + Belts |
| Velocidad | ~2-3 min/caja | ~30 seg/caja | ~5 seg/caja |
| Atencion | Constante | Por caja | Corre solo |
| Satisfaccion | Manual craft | Ver caja cerrarse | Ver fabrica funcionar |
| Create necesario | No | No | Si |

---

# 7. IMPLEMENTACION TECNICA

## 7.1 Vive dentro de servo_core

La Empacadora y las cajas viven en servo_core porque:
- El Terminal de Entrega (servo_core) es el unico consumidor de cajas — estan acoplados
- Los items son del namespace `servo_core:`
- La logica de NBT la valida el Terminal que ya esta en servo_core
- Create es **soft dependency**: si esta, la Empacadora expone inventario por caras. Si no, solo click derecho.

## 7.2 Soft dependency con Create

```java
// servo_core verifica si Create esta presente
public class PackingStationBlock extends Block {

    // Si Create esta: expone inventario por caras (funnel/hopper)
    // Si no: solo acepta click derecho del jugador

    @Override
    public IItemHandler getInventoryForFace(Direction face) {
        if (!ModList.get().isLoaded("create")) return null;
        // Top -> slot caja abierta
        // Sides -> slot items
        // Bottom -> slot output
    }
}
```

## 7.3 Archivos estimados

```
servo_core/
  items/
    FlatCardboardItem.java         // click derecho = doblar
    OpenBoxItem.java               // caja abierta
    ShippingBoxItem.java           // caja cerrada + NBT
  blocks/
    PackingStationBlock.java       // sin GUI, inmersiva
  blockentity/
    PackingStationBlockEntity.java // estado interno (5 estados)
  compat/
    CreateCompat.java             // soft dep: registra behaviours
  render/
    PackingStationRenderer.java   // renderiza items dentro de la caja
```

Lineas estimadas: ~1200-1500

---

# 8. DECISIONES TOMADAS Y PENDIENTES

## Resueltas
1. **Sin GUI** — todo inmersivo estilo Cooking Pot
2. **En servo_core** — Create es soft dependency, no addon separado
3. **Empacadora como Basin**: input por caras, output por abajo
4. **Deployer entiende Caja Abierta**: puede doblar y llenar en belt
5. **Empacadora desde Ch1**: parte del tutorial
6. **3 niveles de automatizacion**: manual -> Empacadora -> Create

## Pendientes
- [ ] Animacion de cierre: block entity animation o particulas?
- [ ] Cajas como entidades en el suelo? (para belt pickup)
- [ ] Modelo 3D de caja en belt: 3D o item plano?
- [ ] Sonidos custom: carton, items, cinta, cierre
- [ ] Velocidad en modo auto? Propuesta: instantaneo manual, 40 ticks auto
