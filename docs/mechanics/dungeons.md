# Sistema de Dungeons y Bosses

> Fuente: GDD v2, secciones 3.5 y 3.8
> Implementacion: **servo_dungeons** (un solo mod para dungeons + bosses)
> Relacionado: [Bosses (detalle de cada jefe)](bosses.md), [Champions](champions.md), [Death System](death-system.md), [RPG Classes](rpg-classes.md), [Accessories](accessories.md), [Tokens](tokens.md)

## Overview

Sistema 100% custom en **servo_dungeons**. Un solo mod maneja tanto dungeons procedurales como peleas de jefes de capitulo. Una sola dimension void compartida, multiples instancias simultaneas separadas por offsets de coordenadas. **Dimensional Dungeons REMOVIDO** (licencia ARR).

**Dos modos de uso, mismo altar**:
- **Dungeon Key** (4 tiers) → dungeon procedural con salas, mobs, loot
- **Boss Key** (8, una por capitulo) → arena directa de boss, sin salas previas

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
| Del Nucleo | 20-25 | ~60-75 min | Max 3 affix (16 pool completo incl. Paralyzing, Wounding), 35% spawn | Si |

## Loot por tier

### Basica (Ch1+)
| Drop | Fuente | Chance |
|------|--------|--------|
| Ores vanilla | cofres | Comun |
| Pepe Coins (5-10) | cofres + champions | Garantizado |
| Runas basicas | cofres | 30% |
| Enchanted Books I-II | cofres | 20% |
| Accesorio custom T1 | cofres raros | 10% |

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

## Anti-skip de tiers

1. Esencia de Dungeon NO dropea en Basica
2. Llave Avanzada requiere drop de boss Ch2
3. Llave Maestra requiere Esencia
4. Llave del Nucleo requiere Cristal de Boss Ch6


## Altar Unificado (Dungeon + Boss)

### El multibloque

El Altar es la unica forma de entrar a dungeons y peleas de boss. Es un ritual en el piso: un pedestal central rodeado de 4 bloques de runa. **El mismo altar sirve para ambos modos** — la llave determina que se genera.

```
     [R]
[R]  [P]  [R]
     [R]

P = Pedestal (servo_dungeons:dungeon_pedestal)
R = Runa de Dungeon (servo_dungeons:dungeon_rune)
```

5 bloques total, patron de cruz en el suelo. Cuando se forma correctamente, particulas conectan las runas al pedestal.

### Como conseguirlo

- **Ch1 quest reward**: el tutorial de dungeons te da 1 Pedestal + 4 Runas
- **Recrafteable** (por si se pierde):

| Bloque | Receta |
|--------|--------|
| Pedestal | 4 Stone Brick + 1 Ender Pearl + 2 Gold Ingot + 1 Lapis |
| Runa (x4) | 4 Stone Brick + 1 Redstone + 1 Lapis |

Materiales accesibles en Ch1. Un altar sirve para TODO — la llave determina que pasa, no el altar.

---

## Ritual de Entrada y Beam

### Flujo paso a paso

```
1. Jugador pone llave en pedestal (click derecho con llave en mano)
   → Llave se consume
   → Runas empiezan a brillar, humming magico

2. Carga del ritual (~5 segundos)
   → Particulas espirales suben desde las runas al centro
   → Sonido crescendo
   → Las runas pulsan con el color del tier de llave:
     Basica=blanco, Avanzada=azul, Maestra=morado, Nucleo=dorado

3. BEAM aparece sobre el pedestal
   → Columna de luz/particulas vertical
   → El jugador que puso la llave es teleportado automaticamente
   → El beam PERSISTE mientras la dungeon/boss este activo

4. Otros jugadores pueden entrar
   → Cualquiera que toque el beam es teleportado al dungeon/boss
   → No hay limite de tiempo, no hay restriccion de team
   → El beam es la puerta: tocas = entras
```

### Beam como mecanica central

**NO usa FTB Teams para entrada**. FTB Teams es puramente social (chat, waypoints). La entrada al dungeon es fisica: tocas el beam, entras.

| Situacion | Resultado |
|-----------|-----------|
| Tocas el beam | Teleport a la dungeon/boss activa de ese altar |
| Dungeon cerrada (beam apagado) | Nada, necesitas una llave nueva |
| Mueres y quieres re-entrar | Tocas el beam otra vez → entras por la ENTRADA |

### Multiples dungeons simultaneas

Se pueden tener **multiples dungeons activas** al mismo tiempo en la misma dimension void. Cada una se genera en coordenadas separadas:

```
Altar A abre dungeon → instancia en X=0, Z=0
Altar B abre dungeon → instancia en X=10,000, Z=0
Altar C abre dungeon → instancia en X=20,000, Z=0
```

Cada altar trackea SU instancia. Si un altar ya tiene dungeon activa, no puedes meter otra llave en ESE altar — pero otro altar si puede abrir otra.

---

## Multiplayer

### Entrada libre via beam

- **No hay restriccion de team** para entrar. Cualquiera que toque el beam entra.
- Maximo 8 jugadores por run (limite del servidor)
- Para coordinar, usas FTB Teams (chat, waypoints) pero NO es requerido para entrar

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
Jugador va al Altar → el beam sigue activo
       ↓
Toca el beam → teleport GRATIS a la ENTRADA de la dungeon
       ↓
Jugador pelea su camino de vuelta a su tumba
       ↓
Recupera sus items ← O muere otra vez intentando
```

**Reglas de muerte:**
- Re-entry via beam es **gratis** e **ilimitado** mientras la dungeon este activa
- No necesitas otra llave para re-entrar
- Apareces en la **sala de entrada** (no donde moriste) — tienes que caminar de vuelta
- Si mueres de nuevo, tumba anterior dropea items al suelo y nueva tumba se forma (comportamiento YIGD normal)
- Tus companeros siguen peleando mientras tu vuelves

**Esto es intencional**: la muerte NO es gratis en esfuerzo. Tienes que correr de vuelta, pelear sin tu loot, y arriesgarte a morir otra vez. Es castigo natural sin ser permanente.

### Salir de la dungeon

| Metodo | Como |
|--------|------|
| Completar | Limpiar ultima sala (o matar boss en CORE) → Exit Portal aparece en sala final |
| Abandonar | Exit Portal permanente en sala de entrada → pisarlo = vuelves al altar en overworld |
| Morir | Respawneas en overworld, puedes re-entrar (tocar beam) o no volver |

**No hay concepto de "lider"**. Nadie tiene control especial. Cualquiera entra, cualquiera sale.

### Cierre automatico de dungeon

La dungeon se cierra cuando **0 jugadores quedan adentro**:
- Todos salieron por Exit Portal
- Todos murieron y nadie re-entro en 10 minutos
- Todos se desconectaron y nadie reconecto en 10 minutos

Al cerrarse:
1. Beam desaparece (pedestal → INACTIVE)
2. Tumbas no recogidas se teleportan junto al altar
3. Todos los bloques de esa instancia se limpian (set AIR)
4. El offset se libera para reusar
5. Se puede abrir una nueva dungeon en ese altar

**Error de llave**: si metiste la llave equivocada, sal por el Exit Portal en la sala de entrada. La llave ya se consumio (costo del error), pero sales en 5 segundos.

### Desconexion / crash

- Si un jugador se desconecta dentro de la dungeon → su posicion se guarda
- Al reconectarse → aparece donde estaba (dentro de la dungeon)
- Si TODOS los jugadores se desconectan → dungeon persiste 10 minutos
- Despues de 10 min sin nadie → cierre automatico (misma limpieza)

### Resumen de estados del Altar

| Estado | Visual | Interaccion |
|--------|--------|-------------|
| Inactivo | Runas apagadas, sin beam | Click con llave = inicia ritual |
| Cargando ritual | Runas brillando, particulas, sonido | Esperar 5 seg |
| Dungeon/Boss activo | Runas pulsando, **beam vertical activo** | Tocar beam = entrar/re-entrar |

---

## Cuantos runs para T4 completo

- 1 pieza T4 = ~3 Fragmentos de Cristal del Nucleo
- Set completo (arma + 4 armor) = ~15 fragmentos
- Boss da 1-2 por kill → **~8-10 runs Del Nucleo**

## Salas

### Templates

- 100+ templates .nbt (creados con Structure Blocks)
- 7 tipos: entrada, pasillo, esquina, T, cruz, dead-end, boss
- Generacion procedural en dimension void

### Estados de sala

| Estado | Condicion | Puerta siguiente |
|--------|-----------|-----------------|
| LOCKED | No has entrado aun | Cerrada |
| ACTIVE | Entraste, mobs spawnearon | Cerrada |
| CLEARED | Mobs muertos + spawns agotados | Abierta |

Una sala es CLEARED cuando:
1. Todas las waves/spawns ya se dispararon (no quedan pendientes)
2. **0 mobs hostiles vivos** en el area de la sala

Salas sin enemigos (pasillos, salas de cofres) se marcan CLEARED inmediatamente al entrar.

### Spawning de mobs (programatico, NO spawners)

Los mobs se spawnean via codigo Java, NO vanilla spawners. Al entrar a una sala por primera vez:

```
DungeonInstance.getTier() → tier de la llave usada
    ↓
Sala define: cantidad de mobs, tipos, posiciones
    ↓
Por cada mob a spawnear:
    - Spawn normal (zombie, skeleton, etc.)
    - Roll de champion: tier.championChance (15%/25%/30%/35%)
    - Si champion: agregar affixes via Champions Unofficial API
      max affixes = tier.maxAffixes (1/2/3/3)
    ↓
Sala pasa a ACTIVE
    ↓
Cuando 0 mobs vivos → sala pasa a CLEARED → puerta siguiente se abre
```

### Dimension e instancias

Una sola dimension void (`servo_dungeons:dungeon`). Multiples instancias simultaneas separadas por offset:

```
Instancia 1: centro en X=0,       Z=0
Instancia 2: centro en X=10,000,  Z=0
Instancia 3: centro en X=20,000,  Z=0
...
```

Al cerrarse una dungeon, sus bloques se limpian (set AIR) y el offset se libera para reusar.

### Limpieza

Cuando 0 jugadores quedan en una instancia (por cualquier razon):
1. Timer de 10 min (por si alguien reconecta)
2. Si nadie vuelve → limpiar todos los bloques de esa instancia
3. Tumbas YIGD no recogidas → teleportar al altar en overworld
4. Liberar offset para reusar
