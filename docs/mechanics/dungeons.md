# Sistema de Dungeons

> Fuente: GDD v2, seccion 3.5
> Relacionado: [Bosses](bosses.md), [Champions](champions.md), [Death System](death-system.md), [RPG Classes](rpg-classes.md), [Accessories](accessories.md), [Tokens](tokens.md)

## Overview

Sistema 100% custom en servo_core. Dimension void, generacion procedural de salas, 4 tiers de llave, multiplayer, roguelike (muerte = pierde loot de la run). **Dimensional Dungeons REMOVIDO** (licencia ARR).

## Llaves de Dungeon

### Reglas
- Cada entrada **consume 1 llave** (1 llave = 1 run)
- Llave Basica siempre crafteable barata → nunca te quedas sin acceso
- Primera llave gratis como quest reward en Ch1
- Llaves superiores: decision riesgo/recompensa

### Crafteo

| Llave | Materiales | Costo | Disponible |
|-------|-----------|-------|------------|
| Basica | 4 Iron + 2 Gold | Barato, infinito | Ch1 |
| Avanzada | 2 Diamond + 4 Blaze Rod + 1 Ender Pearl + drop boss Ch2 | Medio, requiere Nether | Ch3 |
| Maestra | 4 Diamond + 2 Netherite Scrap + 1 Nether Star frag + 1 Esencia de Dungeon | Alto, requiere dungeon previa | Ch5 |
| Del Nucleo | 2 Netherite Ingot + 4 Esencia de Dungeon + 1 Cristal de Boss Ch6 | Muy alto | Ch7 |

Cada tier necesita drop del tier anterior → **cadena de progresion natural**.

## Estructura por tier

| Llave | Salas | Duracion | Champions | Boss dungeon |
|-------|-------|----------|-----------|--------------|
| Basica | 5-7 | ~15-20 min | Max 1 affix, 15% spawn | No |
| Avanzada | 10-14 | ~30-40 min | Max 2 affix, 25% spawn | No |
| Maestra | 15-20 | ~45-60 min | Max 3 affix, 30% spawn | No |
| Del Nucleo | 20-25 | ~60-75 min | Max 3 affix + exclusivos, 35% spawn | Si |

## Loot por tier

### Basica (Ch1+)
| Drop | Fuente | Chance |
|------|--------|--------|
| Ores vanilla | cofres | Comun |
| Pepe Coins (5-10) | cofres + champions | Garantizado |
| Runas basicas | cofres | 30% |
| Enchanted Books I-II | cofres | 20% |
| Accesorio custom T1 | cofres raros | 10% |
| Molde postre 1-2 estrellas | cofres raros | 5% |

### Avanzada (Ch3+) — todo lo de Basica +
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (10-20) | cofres + champions | Garantizado |
| Materiales Nether | cofres | Comun |
| RPG gear T1-T2 | cofres raros | 15% |
| **Esencia de Dungeon** | champions | 15% por champion |
| Unique Jewelry | cofre final | 5% |

### Maestra (Ch5+) — todo lo de Avanzada +
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (20-35) | cofres + champions | Garantizado |
| Materiales raros (Netherite Scrap, Nether Star) | cofres | 15% |
| Accesorio custom T3 | cofres raros | 12% |
| Esencia de Dungeon (x2-3) | champions | 25% |
| Unique Jewelry | cofre final | 15% |
| Moldes 4-5 estrellas | cofres raros | 8% |

### Del Nucleo (Ch7+) — todo lo de Maestra +
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (35-50) | cofres + champions | Garantizado |
| **Fragmento de Cristal del Nucleo** | boss dungeon | Garantizado (1-2) |
| Accesorio custom T4 | boss dungeon | 25% |
| **Unique Jewelry** | boss dungeon | **Garantizado 1** |

## Items exclusivos de dungeon

| Item | Tier minimo | Para que sirve |
|------|------------|----------------|
| Esencia de Dungeon | Avanzada | Craftear Llave Maestra y del Nucleo |
| Unique Jewelry (24 items) | Avanzada (5%) | Mejor equipo rings/necklaces |
| Fragmento de Cristal del Nucleo | Del Nucleo | RPG Tier 4 (endgame) |
| Accesorio custom T4-T5 | Maestra/Nucleo | Mejores accesorios |
| Moldes 4-5 estrellas | Maestra/Nucleo | Mejores moldes postres |

## Anti-skip de tiers

1. Esencia de Dungeon NO dropea en Basica
2. Llave Avanzada requiere drop de boss Ch2
3. Llave Maestra requiere Esencia
4. Llave del Nucleo requiere Cristal de Boss Ch6

## Puzzles con Supplementaries

- Pozos → necesitas Rope para bajar
- Paredes agrietadas → Bomb para abrir paso secreto
- Puertas cerradas → Key de Supplementaries
- Esto hace Supplementaries NECESARIO en dungeons

## Altar de Dungeon (Entrada)

### El multibloque

El Altar de Dungeon es la unica forma de entrar a una dungeon. Es un pequeno ritual en el piso: un pedestal central rodeado de 4 bloques de runa.

```
     [R]
[R]  [P]  [R]
     [R]

P = Pedestal de Dungeon (servo_core:dungeon_pedestal)
R = Runa de Dungeon (servo_core:dungeon_rune)
```

5 bloques total, patron de cruz en el suelo. Cuando se forma correctamente, particulas conectan las runas al pedestal.

### Como conseguirlo

- **Ch1 quest reward**: el tutorial de dungeons te da 1 Pedestal + 4 Runas
- **Recrafteable** (por si se pierde):

| Bloque | Receta |
|--------|--------|
| Pedestal | 4 Stone Brick + 1 Ender Pearl + 2 Gold Ingot + 1 Lapis |
| Runa (x4) | 4 Stone Brick + 1 Redstone + 1 Lapis |

Materiales accesibles en Ch1. Un altar sirve para TODAS las tiers — la llave determina la dificultad, no el altar.

---

## Ritual de Entrada

### Flujo paso a paso

```
1. Lider pone llave en pedestal (click derecho con llave en mano)
   → Llave se consume, aparece flotando sobre el pedestal
   → Runas empiezan a brillar, humming magico

2. Carga del ritual (~5 segundos)
   → Particulas espirales suben desde las runas al centro
   → Sonido crescendo
   → Las runas pulsan con el color del tier de llave:
     Basica=blanco, Avanzada=azul, Maestra=morado, Nucleo=dorado

3. Explosion de particulas → TELEPORT
   → Todos los jugadores elegibles son teleportados
   → Flash de pantalla + sonido de llegada
   → Aparecen en la sala de entrada de la dungeon
```

### Quien se teleporta

Usa **FTB Teams** como backend (el jugador no interactua con menus de team):

| Situacion | Quien entra |
|-----------|-------------|
| Lider en un FTB Team | Lider + todos los miembros del team que esten **dentro de 5 bloques del pedestal** |
| Lider sin team (solo) | Solo el lider |
| Miembro del team lejos del altar | NO se teleporta (debe estar cerca) |
| Jugador de otro team cerca | NO se teleporta (no es su team) |

**Regla clave**: Pararte en el circulo = "estoy listo". No hay boton ni menu. Si estas ahi cuando el ritual termina, entras. Si no, no.

### Limite: 1 dungeon activa

- Si ya hay una dungeon en progreso, meter llave en cualquier altar → mensaje: **"Una dungeon esta en progreso. Espera a que termine."**
- Las runas del altar activo brillan mientras la dungeon esta corriendo (indicador visual)
- La llave NO se consume si el ritual falla

---

## Multiplayer

### Grupos (FTB Teams)

- El sistema de team se configura una vez (FTB Teams tiene su UI propia)
- Para dungeons solo importa: estas en el mismo team? estas cerca del altar?
- Maximo 8 jugadores por run (limite del servidor)

### Dentro de la dungeon

- **Loot de cofres**: individual via Lootr (cada jugador ve su propio loot)
- **Boss drops**: individuales por participante (como MMO). Si 2 jugadores matan boss, AMBOS reciben drops completos
- **Champions drops**: van al que da el ultimo golpe (como vanilla)
- **Mobs comunes**: drops vanilla normales (primer pickup)

### Muerte en dungeon (Dark Souls style)

```
Jugador muere en dungeon
       ↓
YIGD crea tumba en el lugar de muerte (dentro de la dungeon)
       ↓
Items soulbound (T2+ equipado) se quedan con el jugador
Items de la run (loot, comida, materiales) quedan en la tumba
       ↓
Jugador respawnea en overworld (cama/spawn)
       ↓
Jugador va al Altar de Dungeon (sigue brillando, activo)
       ↓
Click derecho en pedestal → teleport GRATIS a entrada de dungeon
       ↓
Jugador pelea su camino de vuelta a su tumba
       ↓
Recupera sus items ← O muere otra vez intentando
```

**Reglas de muerte:**
- Re-entry es **gratis** e **ilimitado** mientras la dungeon este activa
- No necesitas otra llave para re-entrar
- Apareces en la **sala de entrada** (no donde moriste) — tienes que caminar
- Si mueres de nuevo, tumba anterior dropea items al suelo y nueva tumba se forma (comportamiento YIGD normal)
- Tus companeros siguen peleando mientras tu vuelves

**Esto es intencional**: la muerte NO es gratis en esfuerzo. Tienes que correr de vuelta, pelear sin tu loot, y arriesgarte a morir otra vez. Es castigo natural sin ser permanente.

### Salir de la dungeon

| Metodo | Como |
|--------|------|
| Completar | Matar boss final (Nucleo) o limpiar ultima sala → portal de salida aparece |
| Abandonar | Portal permanente en sala de entrada → click = vuelves al altar en overworld |
| Morir | Respawneas en overworld, puedes re-entrar o abandonar |

### Abandonar dungeon (lider)

- El **lider** (quien puso la llave) puede interactuar con el pedestal del altar y elegir **"Abandonar Dungeon"**
- Si hay jugadores adentro → reciben aviso: "El lider ha iniciado el cierre. 60 segundos para salir."
- Despues de 60 seg → jugadores restantes son teleportados al altar
- Tumbas no recogidas se teleportan al altar (items no se pierden)
- Chunks de la dungeon se marcan para limpieza

### Desconexion / crash

- Si un jugador se desconecta dentro de la dungeon → su posicion se guarda
- Al reconectarse → aparece donde estaba (dentro de la dungeon)
- Si TODOS los jugadores se desconectan → dungeon persiste 10 minutos
- Despues de 10 min sin nadie → limpieza automatica, tumbas teleportadas al altar

### Resumen de estados del Altar

| Estado | Visual | Interaccion |
|--------|--------|-------------|
| Inactivo | Runas apagadas, sin particulas | Click con llave = inicia ritual |
| Cargando ritual | Runas brillando, particulas, sonido | Esperar 5 seg, pararse cerca |
| Dungeon activa | Runas pulsando suavemente | Click = re-entry (team members) / Abandonar (lider) |
| Dungeon de otro | Runas apagadas | Click con llave = "Dungeon en progreso" |

---

## Cuantos runs para T4 completo

- 1 pieza T4 = ~3 Fragmentos de Cristal del Nucleo
- Set completo (arma + 4 armor) = ~15 fragmentos
- Boss da 1-2 por kill → **~8-10 runs Del Nucleo**

## Salas

- 100+ templates .nbt
- 7 tipos: entrada, pasillo, esquina, T, cruz, dead-end, boss
- Generacion procedural en dimension void
- Limpieza de chunks cuando 0 jugadores quedan
