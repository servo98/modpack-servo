# ServoMart (Sistema IKEA)

> Fuente: GDD v2, seccion 3.4
> Implementacion: servo_core custom
> Relacionado: [Tokens](tokens.md), [Gacha](gacha.md), [Progression](progression.md)

## Overview

Bloque custom tipo tablet/computadora con catalogo de muebles. Muebles se compran y llegan en cajas.

## Como funciona

1. Abrir catalogo en bloque ServoMart
2. Muebles desbloqueados por capitulo
3. "Comprar" con Pepe Coins o materiales
4. Llega **Caja de Carton** que al colocarla y abrirla aparece el mueble
5. Muebles de Macaw's, y Refurbished se obtienen por aqui

## Gacha de muebles

- Opcion de **caja misteriosa** con mueble random
- Separado del gacha de artefactos
- Costo: 5 Pepe Coins por pull

## Muebles funcionales (Refurbished, afectan progresion)

| Mueble | Funcion |
|--------|---------|
| Nevera | Conserva comida (no se pudre) |
| Estufa | Workstation alternativa para cocinar |
| Fregadero | Lava items (crafting ingredient) |

Ciertas recetas Ch4+ requieren cocina equipada con nevera+estufa+fregadero.

## Desbloqueo por capitulo

| Cap | Muebles disponibles |
|-----|---------------------|
| 2 | Macaw's Furniture basico |
| 3 | Refurbished basico (nevera, estufa, fregadero) |
| 4 | Macaw's completo (bridges, roofs, windows, trapdoors) |
| 5 | Create Deco + Refurbished completo |
| 6+ | Todo |

## Estado: PENDIENTE

- [ ] Diseno completo del GUI
- [ ] Sistema de pedidos
- [ ] Que muebles van en que capitulo
