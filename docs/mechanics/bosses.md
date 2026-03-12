# Sistema de Jefes - Modpack Servo

> Fuente: GDD v2, seccion 3.8
> Implementacion: servo_core custom
> Relacionado: [Dungeons](dungeons.md), [Death System](death-system.md), [Combat Scaling](../balance/combat-scaling.md), [RPG Weapon Stats](../balance/rpg-weapon-stats.md), [Tokens](tokens.md)

---

# 1. VISION

8 jefes unicos, 1 por capitulo. Cada jefe es un evento memorable con mecanicas que obligan al jugador a pensar, no solo a atacar. Los jefes son el examen de cada capitulo: testean que el jugador domina las mecanicas introducidas.

**Principios de diseno**:
- Cada boss testea una habilidad o sistema del capitulo
- Morir debe sentirse justo ("sabia que iba a pasar, no esquive")
- Nunca "pegar y esquivar" como unico patron: cada boss tiene una mecanica interactiva unica
- Fases con transiciones cinematicas (GeckoLib)
- Multiplayer scaling: mas jugadores = mas HP y dano, mecanicas identicas
- Invocacion directa al boss via Boss Altar — los dungeons son un sistema aparte

---

# 2. INVOCACION: BOSS ALTAR

## 2.1 Concepto

El Boss Altar es un bloque custom de servo_core. Existe 1 por mundo, pre-colocado en el spawn o crafteable a partir de Ch1. El jugador craftea una Boss Key del capitulo correspondiente, la inserta en el Altar, y el grupo entero es teleportado a la Boss Chamber (una arena instanciada en la dimension void de servo_core).

No hay salas previas, no hay dungeon. Es directo: llave → altar → boss.

## 2.2 Flujo de invocacion

```
1. Jugador craftea Boss Key del capitulo
2. Se acerca al Boss Altar con su grupo
3. Inserta la Boss Key → la llave se consume
4. El Altar abre un portal (visual: vortice tematico del jefe)
5. Todos los jugadores cercanos (radio 8 bloques) tienen 30 segundos para entrar
6. Al entrar: teleport a la Boss Chamber instanciada
7. Cuando el ultimo jugador entra O pasan los 30s → el portal se cierra
8. El jefe aparece con cinematica de spawn
9. Victoria o derrota → portal de salida aparece → regreso al Altar
```

## 2.3 Reglas del portal y multiplayer

**Entrada**:
- El portal permanece abierto 30 segundos despues de insertar la llave
- Cualquier jugador en radio 8 bloques del Altar puede entrar durante esos 30s
- El HP del jefe se calcula al momento del spawn basado en cuantos jugadores hay dentro cuando el portal se cierra
- Si solo entraron 2 de 4, el boss escala para 2, no para 4

**Re-entrada despues de morir**:
- Si un jugador muere dentro de la Boss Chamber, respawnea en el overworld junto al Boss Altar
- El portal se REABRE automaticamente por 15 segundos cuando un jugador muere, permitiendole re-entrar
- El HP del boss NO se recalcula al re-entrar (sigue con el HP que tenia)
- El jugador re-entra con sus items Soulbound (gear equipado T2+) pero sin los consumibles que perdio
- Esto le permite re-entrar a ayudar al grupo, pero debilitado (sin pociones, sin comida extra)
- Si TODOS los jugadores mueren simultaneamente, la Boss Chamber se destruye y hay que usar otra Boss Key

**Salida**:
- Victoria: portal de salida aparece en la arena. 60 segundos para recoger loot y salir
- Derrota total: todos respawnean en el overworld junto al Altar. Boss Key consumida, hay que craftear otra
- Desconexion: si un jugador se desconecta, cuenta como muerto para efectos de la pelea. Al reconectar aparece en el overworld

**Reglas de la Boss Chamber**:
- Dimension void instanciada, unica para esa pelea
- No se pueden colocar bloques (excepto mecanicas especificas del jefe que lo requieran)
- No se puede usar Ender Pearl para escapar del arena
- Waystones deshabilitados dentro de la chamber
- Al terminar (victoria o derrota total), la instancia se limpia

## 2.4 Crafteo de Boss Keys

| Cap | Boss Key | Materiales | Costo estimado |
|-----|----------|-----------|----------------|
| 1 | Llave del Bosque | 8 Oak Log + 4 Iron Ingot + 1 Bone | Muy barato (tutorial) |
| 2 | Llave Voraz | 4 Gold Ingot + 2 Rotten Flesh + 1 Fermented Spider Eye + 3 comidas cocinadas | Barato |
| 3 | Llave Mecanica | 4 Iron Ingot + 2 Redstone Block + 1 Cogwheel (Create) + 1 Clock | Medio |
| 4 | Llave Fantasma | 4 Blaze Rod + 2 Soul Sand + 1 Rail + 1 Ender Pearl | Medio |
| 5 | Llave Digital | 4 Diamond + 2 Quartz Block + 1 Controller (RS) + 1 Printed Circuit | Alto |
| 6 | Llave de la Cosecha | 4 Netherite Scrap + 8 comidas empacadas distintas + 1 Golden Hoe | Alto |
| 7 | Llave Cristalina | 2 Netherite Ingot + 4 Esencia de Dungeon + 1 Nether Star fragment | Muy alto |
| 8 | Llave del Vacio | 2 Netherite Ingot + 4 Fragmento de Cristal del Nucleo + 1 item de cada pilar* | Endgame |

*Llave del Vacio: 1 Caja empacada + 1 Brass Casing (Create) + 1 arma RPG T3+ (los 3 pilares).

---

# 3. TECH STACK

## 3.1 GeckoLib 4.8.x (NeoForge 1.21.1)

GeckoLib es la libreria de animacion. Ya esta como dependencia (RPG Series la usa). Version: `geckolib-neoforge-1.21.1-4.8.4`.

**Que aporta**:
- Modelos 3D complejos via Blockbench (`.geo.json`)
- Animaciones keyframe con 30+ easings
- Multiples AnimationControllers simultaneos (movimiento + ataques + fase)
- `triggerableAnim()` para disparar animaciones desde el servidor
- Sound/particle/event keyframes sincronizados con la animacion (el dano se aplica en el frame exacto del golpe)

## 3.2 Pipeline de creacion

```
MODELADO (Blockbench)
├── Instalar plugin "GeckoLib Animation Utils" en Blockbench
├── Crear como "GeckoLib Animated Model" → tipo Entity
├── Bones en jerarquia (root > body > arms > head)
├── Textura 512x512 para bosses grandes
└── Exportar: .geo.json + textura .png

ANIMACION (Blockbench, tab Animate)
├── idle (loop), walk (loop)
├── attack_melee, attack_ranged, attack_special_1/2/3
├── phase_transition, spawn, death, stagger, enrage
├── Sound keyframes en momentos de impacto
├── Particle keyframes para VFX
├── Event keyframes para logica (ej: aplicar dano en frame 16)
└── Exportar: .animation.json

CODIGO (servo_core, Java/NeoForge)
├── Entidad: extiende Monster, implementa GeoEntity
├── Modelo: extiende GeoModel<BossEntity>
├── Renderer: extiende GeoEntityRenderer<BossEntity>
├── 3 AnimationControllers por boss (ver 3.3)
├── BossAI con Goals custom (ver 3.4)
├── BossPhaseManager (ver 3.5)
└── Registrar entity type
```

## 3.3 AnimationControllers (3 por jefe)

Cada jefe usa 3 controllers simultaneos para que las animaciones no se pisen:

```java
@Override
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    // 1: Movimiento base — siempre activo
    controllers.add(new AnimationController<>(this, "movement", 5, this::movementController));

    // 2: Ataques — triggered desde servidor
    controllers.add(new AnimationController<>(this, "attacks", 0, state -> PlayState.STOP)
        .triggerableAnim("melee", ATTACK_MELEE)
        .triggerableAnim("ranged", ATTACK_RANGED)
        .triggerableAnim("special1", ATTACK_SPECIAL_1)
        .triggerableAnim("stagger", STAGGER_ANIM));

    // 3: Estado/Fase — transiciones cinematicas
    controllers.add(new AnimationController<>(this, "phase", 10, state -> PlayState.STOP)
        .triggerableAnim("spawn", SPAWN_ANIM)
        .triggerableAnim("phase_transition", PHASE_TRANSITION)
        .triggerableAnim("enrage", ENRAGE_ANIM)
        .triggerableAnim("death", DEATH_ANIM));
}
```

El parametro numerico (5, 0, 10) = transition ticks. 0 = corte duro (ataques), 5-10 = transicion suave (movimiento/fases).

## 3.4 AI: State Machine

```
SPAWN → IDLE ←→ ATTACK_MELEE
          ↕         ↕
      CHASE    ATTACK_RANGED
          ↕         ↕
   SPECIAL_ATTACK  DEFEND
          ↕
   PHASE_TRANSITION → (nueva fase, nuevos Goals)
          ↕
       ENRAGE → (stats buff, nuevos ataques)
          ↕
        DEATH
```

Goals por prioridad:
1. `BossPhaseTransitionGoal` (siempre interrumpe)
2. `BossDeathGoal`
3. `BossSpecialAttackGoal` (cooldown-based)
4. `BossDefendGoal` (condicional)
5. `BossMeleeAttackGoal`
6. `BossRangedAttackGoal`
7. `BossChaseGoal`
8. `BossIdleGoal`

## 3.5 BossPhaseManager

Controla las transiciones de fase por umbral de HP. Durante transicion: boss invulnerable, cinematica de GeckoLib, se reconfiguran los Goals activos.

```java
public class BossPhaseManager {
    private int currentPhase = 1;
    private final float[] thresholds; // ej: {0.75f, 0.50f, 0.25f}

    public void tick(BossEntity boss) {
        float hpPercent = boss.getHealth() / boss.getMaxHealth();
        int expected = calculatePhase(hpPercent);
        if (expected > currentPhase) {
            boss.setInvulnerable(true);
            boss.triggerAnim("phase", "phase_transition");
            // Despues de X ticks: aplicar buffs, cambiar Goals
            boss.setInvulnerable(false);
            currentPhase = expected;
        }
    }
}
```

---

# 4. TABLA RESUMEN DE JEFES

| Cap | Jefe | HP | Dmg | Fases | Duracion | Mecanica unica |
|-----|------|-----|-----|-------|----------|----------------|
| 1 | Guardian del Bosque | 200 | 5.0 | 2 | 3-5 min | Esquivar + matar minions cerca del boss para debuffarlo |
| 2 | Bestia Glotona | 400 | 6.8 | 2 | 5-7 min | Lanzarle comida (envenenada = ventana de dano) |
| 3 | Coloso Mecanico | 800 | 9.1 | 3 | 7-10 min | Golpear paneles naranjas, activar Nodos de Sabotaje |
| 4 | Locomotora Fantasma | 1,600 | 12.3 | 2 | 8-12 min | Pelea en movimiento sobre tren circular |
| 5 | El Arquitecto | 3,200 | 16.6 | 3 | 10-14 min | Hackear terminales en equipo para bajar Firewall |
| 6 | Senor de las Cosechas | 6,400 | 22.4 | 3 | 12-16 min | Cosechar crops corruptos, plantar reales para danarlo |
| 7 | Nucleo del Dungeon | 12,800 | 30.3 | 4 | 15-20 min | Arena que se destruye, pilares que explotar |
| 8 | Devorador de Mundos | 25,600 | 40.9 | 4 | 20-30 min | 3 pilares combinados (combate + Create + cocina) |

**Formulas de scaling** (se fijan al spawnear):
- HP: `base * (1 + (jugadores-1) * 0.3)`
- Dmg: `base * (1 + (jugadores-1) * 0.15)`
- Dmg base por capitulo: `5.0 * 1.35^(cap-1)`

---

# 5. JEFES — DISENO DETALLADO

---

## 5.1 GUARDIAN DEL BOSQUE (Capitulo 1)

### Identidad
- **Visual**: Humanoide de 3 bloques hecho de raices, musgo y corteza. Ojos verdes brillantes. Flores marchitas en los hombros.
- **Arena**: Claro de bosque circular (radio 15 bloques). Arboles en los bordes como cobertura. Suelo de tierra con raices que sobresalen.
- **Musica**: Percusion organica, cuerdas tensas.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 200 | 260 | 380 | 620 |
| Dano | 5.0 | 5.75 | 7.25 | 10.25 |
| Velocidad | 0.28 | — | — | — |
| Armor | 4 | — | — | — |
| Knockback Res | 0.6 | — | — | — |

### Fase 1 (100%-50% HP): El Despertar

**Ataques**:

| Ataque | Dano | Rango | Cooldown | Senal visual (anticipacion) | Como esquivar |
|--------|------|-------|----------|-----------------------------|---------------|
| Zarpazo | 5.0 | 3 bloques melee | 2s | Levanta brazo derecho 0.8s antes | Retroceder o bloquear con escudo |
| Latigazo de Raiz | 4.0 + Slowness II 3s | 8 bloques linea recta | 6s | Raiz brilla verde en el suelo 1.2s antes | Moverse lateral (va en linea recta) |
| Espora Toxica | 2.0/s por 4s (Poison) | Radio 4 AoE | 10s | Se agacha y tiembla 1.5s, particulas verdes | Alejarse del boss |

**Comportamiento**: Persigue al mas cercano. Zarpazo en melee, Latigazo a distancia, Espora si 2+ jugadores en rango melee. Ventana de castigo de 0.5s despues de cada ataque.

**Defensa**: Ninguna en Fase 1. Recibe dano normal.

**Estrategia**: Atacar en la ventana post-ataque. Escudo bloquea Zarpazo. Arcos y wands son efectivos porque es lento.

### Fase 2 (50%-0% HP): Raices Vivas

**Transicion** (3s, invulnerable): Clava las manos en el suelo. Raices emergen del suelo. Rugido que aplica Slowness I 2s a todos.

**Cambios**: Velocidad +20% (0.28 → 0.34). Spawna 2 Retonos al entrar en fase, 2 mas cada 30s (max 4).

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Zarpazo (buff) | 6.5 | 3 bloques | 1.5s | Mas rapido (0.6s anticipacion) | Timing mas ajustado |
| Erupcion de Raices | 6.0 + knockback vertical | Radio 6, centrado en jugador aleatorio | 12s | Particulas marrones bajo los pies del target 2s antes | Moverse del punto marcado |

**Retonos** (minions): 20 HP, 3.0 dano, velocidad 0.35. Mueren en 2-3 hits. Si el Guardian muere, los Retonos mueren.

**Defensa**: Cada 20s se envuelve en raices. 50% menos dano por 5s.

**Contramedida del escudo de raices**: Matar un Retono a menos de 3 bloques del Guardian le aplica "raices cortadas" — cancela la defensa y le da Weakness I 5s. Esto es la mecanica central: no ignores los minions, usalos como herramienta.

**Estrategia**: Priorizar Retonos pero matarlos CERCA del Guardian. Un jugador distrae, otro hace DPS en la ventana de debuff. No quedarse quieto (Erupcion castiga posiciones estaticas).

### Loot (individual por participante)
| Drop | Chance |
|------|--------|
| 15-25 Pepe Coins | 100% |
| Semilla del Bosque (trophy + quest proof) | 100% |
| Drop de Boss Ch1 (para Llave Avanzada en Ch3) | 100% |
| Accesorio custom T1 (belt/back/feet aleatorio) | 30% |
| Molde de postre 2 estrellas | 20% |
| Runa Arcane x4 | 40% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Controller | Notas |
|--------|------|----------|------------|-------|
| idle | Loop | 3s | movement | Respirar, raices sutil |
| walk | Loop | 1s | movement | Pasos pesados |
| spawn | Once | 4s | phase | Emerge del suelo |
| attack_swipe | Once | 1.3s | attacks | Dano en frame 16 |
| attack_root_lash | Once | 2s | attacks | Brazo se extiende. Dano frame 20 |
| attack_spore | Once | 2.5s | attacks | Se agacha, AoE frame 30 |
| attack_root_eruption | Once | 3s | attacks | Manos al suelo, AoE frame 40 |
| defend_wrap | Once | 6s | phase | Raices lo envuelven |
| stagger | Once | 1.5s | attacks | Tambalea al recibir debuff |
| phase_transition | Once | 3.5s | phase | Clava manos, raices emergen |
| death | Once | 4s | phase | Se desmorona, luz verde escapa |

---

## 5.2 BESTIA GLOTONA (Capitulo 2)

### Identidad
- **Visual**: Criatura obesa, 2.5 bloques de alto y 3 de ancho. Piel gris-verde, boca enorme, mandibula desencajada. Babea constantemente.
- **Arena**: Cocina abandonada (20x20). Mesas rotas, ollas enormes, barriles de comida podrida.
- **Musica**: Ritmo pesado, sonidos de masticacion.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 400 | 520 | 760 | 1,240 |
| Dano | 6.8 | 7.82 | 9.86 | 13.94 |
| Velocidad | 0.22 | — | — | — |
| Armor | 6 | — | — | — |
| Knockback Res | 0.9 | — | — | — |

### Mecanica central: ALIMENTAR AL BOSS

La Bestia tiene un medidor de **Saciedad** (barra verde visible, 0%-100%, empieza en 100%).
- Baja 2%/s naturalmente
- A 0%: Frenesi (dano x2, velocidad x2 por 15s). Extremadamente peligroso.
- El jugador puede lanzarle comida (click derecho apuntando al boss):
  - **Comida normal**: +20% Saciedad, se calma
  - **Comida envenenada** (Rotten Flesh, Suspicious Stew, Poisonous Potato): +10% Saciedad + Nausea al boss 4s, se detiene y vomita, recibe x1.5 dano
  - **Comida de calidad alta** (Feast-tier FD): +40% Saciedad + el boss se cura 5% HP. Mal movimiento.

**Decision**: Mantener Saciedad estable con comida basica. Usar envenenada para crear ventanas de DPS. No desperdiciar comida buena.

### Fase 1 (100%-40% HP): La Comilona

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Embestida | 8.0 + knockback fuerte | 10 bloques linea recta | 8s | Se agacha y grune 1.5s | Moverse lateral |
| Mordisco | 6.8 + roba 1 comida del inventario | 2 bloques melee | 3s | Abre mandibula 1s antes | Retroceder |
| Vomito Acido | 3.0/s charco 5s + Nausea | 3x3 frente al boss | 10s | Se tambalea y sujeta la panza 2s | No pisar el charco verde |

**Defensa**: Saciedad > 80% = Resistance I (tanquea mucho, dejar que baje). Saciedad < 20% = +50% dano y +30% velocidad. Zona optima: 20%-60%.

### Fase 2 (40%-0% HP): Hambre Insaciable

**Transicion** (4s, invulnerable): Vomita masivamente. Saciedad cae a 0%. Crece 30%. Mandibula se desencaja.

**Cambios**: Saciedad baja 4%/s (doble). Vomito deja charcos de 5x5 que duran 8s. Comida envenenada ahora da Nausea de 6s (ventana mas larga).

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Devorar | 4.0/s por 3s (inmoviliza jugador) | 2 bloques, solo si target < 6 HP | 15s | Se lanza sobre el target | Otro jugador debe golpear al boss para interrumpir |

**Estrategia**: Entrar con mucha comida variada (10+ items, incluyendo envenenada). Un jugador alimenta (support), otros hacen DPS. Lanzar envenenada → boss vomita 4-6s → DPS window → repetir. Fase 2 es carrera contra reloj. El Perfil B (cocinero) brilla: cocino variedad = tiene ventaja.

### Loot
| Drop | Chance |
|------|--------|
| 25-35 Pepe Coins | 100% |
| Mandibula de la Bestia (trophy) | 100% |
| Drop de Boss Ch2 (para Llave Avanzada) | 100% |
| Molde de postre 3 estrellas | 25% |
| Accesorio T2 (belt/back/feet) | 20% |
| Receta: "Estofado de la Bestia" (unica, +4 corazones temp) | 15% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle | Loop | 2s | Babea, mira alrededor |
| idle_hungry | Loop | 2s | Agresivo cuando Saciedad <30% |
| walk | Loop | 1.2s | Panza rebota |
| spawn | Once | 3s | Emerge de pila de comida |
| attack_charge | Once | 2.5s | Embestida |
| attack_bite | Once | 1.5s | Dano frame 15 |
| attack_vomit | Once | 3s | Vomita charco |
| attack_devour | Once | 4s | Agarra y mastica |
| eat_food | Once | 1.5s | Come, se calma |
| eat_poison | Once | 2.5s | Come, vomita, aturdida |
| eat_feast | Once | 2s | Come con gusto, brilla (se cura) |
| frenzy_roar | Once | 2s | Rugido a 0% Saciedad |
| phase_transition | Once | 4s | Vomito masivo, crece |
| death | Once | 5s | Colapsa, explota en comida |

---

## 5.3 COLOSO MECANICO (Capitulo 3)

### Identidad
- **Visual**: Robot de 5 bloques. Engranajes, pistons, cobre oxidado. Ojo rojo central. Vapor en junturas. Brazos enormes.
- **Arena**: Fabrica antigua (25x25). Engranajes en paredes. 4 Nodos de Sabotaje en esquinas. Lava en foso perimetral. 4 pilares de piedra.
- **Musica**: Industrial, martilleos ritmicos, silbidos de vapor.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 800 | 1,040 | 1,520 | 2,480 |
| Dano | 9.1 | 10.47 | 13.2 | 18.66 |
| Velocidad | 0.18 | — | — | — |
| Armor | 14 | — | — | — |
| Knockback Res | 1.0 | — | — | — |

### Mecanica central: PANELES DE SABOTAJE

El Coloso tiene 4 paneles en su cuerpo (espalda, pecho, brazo izq, brazo der).
- Recibe solo **25% de dano** en golpes normales (armor altisima)
- Cada 8s un panel diferente se activa (brilla naranja, sonido de vapor). Golpear panel activo = **x3 dano**
- Los 4 **Nodos de Sabotaje** en las esquinas del arena son interactivos (click derecho, 3s de canalizacion). Activar uno = TODOS los paneles se activan 6s. Cada nodo es 1 uso.
- **TNT**: colocar TNT junto a panel activo = 40 dano directo (ignora armor). Max 2 efectivos por pelea.

### Fase 1 (100%-65%): Protocolo de Defensa

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Punetazo | 9.1 + knockback | 4 bloques melee | 3s | Brazo retrocede 1s | Rodar lateral |
| Piston Slam | 12.0 AoE + Slowness II | Radio 5 frontal | 10s | Se eleva 1 bloque, 2s carga | Alejarse del frente |
| Rayo de Vapor | 4.0/s linea 3s | 12 bloques linea | 15s | Ojo rojo brilla 2s, laser apunta | Moverse perpendicular |

### Fase 2 (65%-30%): Modo Asalto

**Transicion** (3s): Brazo se rompe y cae, se reconfigura en canon. Armor baja a 10.

**Cambios**: Pierde Punetazo, gana Canon. Paneles rotan cada 5s (mas rapido). Spawna 2 Drones de Reparacion cada 20s (max 4). Si un drone llega al Coloso sin morir en 5s, lo cura 3% HP.

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Canon de Chatarra | 7.0 + explosion small | 20 bloques proyectil lento | 5s | Canon apunta y carga 1.5s | Moverse (proyectil lento) |

### Fase 3 (30%-0%): Sobrecarga

**Transicion**: Todos los paneles se abren. Alarma. Modo berserker.

**Cambios**: Todos los paneles activos PERMANENTEMENTE (x3 dano siempre). Velocidad sube a 0.30. Dano +40%. Sin Drones.

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Descarga Electrica | 6.0 AoE toda la arena | Toda la arena | 12s | Carga 3s, electricidad visible | Esconderse detras de pilar (4 pilares, aguantan 2 descargas c/u) |

**Estrategia**: F1: usar 1-2 Nodos + TNT para burst. F2: matar Drones, usar Nodos restantes. F3: DPS race. Paneles siempre abiertos, esconderse de Descargas. Mages brillan en F3.

### Loot
| Drop | Chance |
|------|--------|
| 30-45 Pepe Coins | 100% |
| Nucleo del Coloso (trophy) | 100% |
| Drop de Boss Ch3 | 100% |
| Cogwheel Dorado (decorativo) | 50% |
| Accesorio T2-T3 | 25% |
| Enchanted Book nivel III | 30% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle | Loop | 2s | Vapor, engranajes giran |
| walk | Loop | 1.5s | Pisadas pesadas, suelo tiembla |
| spawn | Once | 4s | Se enciende, ojo parpadea |
| attack_punch | Once | 1.8s | Dano frame 18 |
| attack_slam | Once | 3s | Se eleva, cae, AoE frame 36 |
| attack_steam_ray | Once | 4s | Ojo carga, rayo frame 24-60 |
| attack_cannon | Once | 2.5s | Canon dispara, retroceso |
| attack_discharge | Once | 4s | Electricidad, onda expansiva |
| arm_break | Once | 3s | Brazo cae, se reconfigura |
| panel_activate | Once | 0.5s | Panel brilla naranja |
| phase_transition | Once | 3.5s | Alarma, paneles abren |
| death | Once | 5s | Cortocircuito, explota en engranajes |

---

## 5.4 LOCOMOTORA FANTASMA (Capitulo 4)

### Identidad
- **Visual**: Tren espectral de 15 bloques. Cara demoniaca en la locomotora. Vagones con pasajeros fantasma. Aura morada, flota.
- **Arena**: Via ferrea circular (radio 20 bloques). Plataformas flotantes alrededor. El tren da vueltas continuamente. Debajo de la via: niebla fantasmal (2 dmg/s si caes, hay escaleras para subir).
- **Musica**: Silbido distorsionado, percusion acelerada, coro etereo.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 1,600 | 2,080 | 3,040 | 4,960 |
| Dano | 12.3 | 14.15 | 17.84 | 25.22 |
| Velocidad | Fija (rail) | — | — | — |
| Armor | 8 | — | — | — |
| Knockback Res | 1.0 | — | — | — |

### Mecanica central: PELEA EN MOVIMIENTO

El tren da vueltas constantemente. La Locomotora (cabeza) es el hitbox del boss.
- **Subirse al tren**: golpeas la Locomotora directamente (melee). Riesgo: te puede tirar.
- **Plataformas flotantes** (8 alrededor de la via): seguro, solo ranged efectivo.
- **Vagones**: tienen "enganches". 5 hits en el enganche = vagon se desacopla. Menos vagones = menos fantasmas.
- **Caer de la via**: niebla fantasmal abajo (2 dmg/s). Escaleras para subir.

### Fase 1 (100%-50%): El Tren Express

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Silbido Sonico | 6.0 + knockback (te tira del tren) | AoE en el tren | 8s | Silbido 1.5s antes | Agacharse (crouch) reduce knockback 70% |
| Vapor Fantasmal | 4.0/s nube 3s + Blindness 2s | Chimenea hacia atras | 10s | Vapor sale de chimenea | Ir al frente de la Locomotora |
| Pasajeros Fantasma | Spawn 3-4 fantasmas cada 15s | Salen de vagones | — | Ventanas brillan | Matarlos (10 HP c/u, aplican Slowness) |

**Mecanica de vagones**: Desacoplar vagones reduce spawns de fantasmas. 3 vagones iniciales.

### Fase 2 (50%-0%): Descarrilamiento

**Transicion**: Tren se acelera x1.5. 1 vagon explota. Secciones de via desaparecen (5s off, 5s on).

**Cambios**: Via inestable, caer mas seguido. Fantasmas explotan al morir (3 AoE radio 2).

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Embate Frontal | 15.0 | Frente del tren, 8 bloques | 12s | Cara brilla roja 2s | Saltar a plataforma |

**Estrategia**: F1: subirse al tren y golpear Locomotora, desacoplar vagones, companero en plataformas mata fantasmas. F2: alternar tren y plataformas, usar Ender Pearls entre plataformas. Rogues/Daggers son ideales (ataques rapidos en el tren).

### Loot
| Drop | Chance |
|------|--------|
| 35-50 Pepe Coins | 100% |
| Faro Fantasmal (trophy) | 100% |
| Drop de Boss Ch4 | 100% |
| Accesorio T3 (velocidad/movilidad) | 20% |
| Rail Encantado (decorativo) | 40% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle_moving | Loop | 2s | Tren en movimiento, vapor, ruedas |
| attack_whistle | Once | 2.5s | Silbido, onda sonora |
| attack_steam | Once | 3s | Chimenea expulsa vapor |
| attack_ram | Once | 3s | Cara brilla, acelera |
| ghost_spawn | Once | 1.5s | Ventanas brillan, fantasmas salen |
| wagon_detach | Once | 2s | Vagon se desacopla y cae |
| speed_up | Once | 1.5s | Tren acelera |
| phase_transition | Once | 4s | Vagones explotan, via se rompe |
| death | Once | 6s | Tren descarrila, explota en fantasmas |

---

## 5.5 EL ARQUITECTO (Capitulo 5)

### Identidad
- **Visual**: Holografico humanoide de 4 bloques. Cuadricula digital azul-cyan. Glitchea visualmente. Construye bloques del aire.
- **Arena**: Sala blanca (30x30) tipo laboratorio. Grid en el suelo. 6 Terminales en paredes. Techo-pantalla muestra mensajes.
- **Musica**: Electronica glitch, synths.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 3,200 | 4,160 | 6,080 | 9,920 |
| Dano | 16.6 | 19.09 | 24.07 | 34.03 |
| Velocidad | 0.32 + teleport | — | — | — |
| Armor | 6 | — | — | — |
| Knockback Res | 0.8 | — | — | — |

### Mecanica central: HACKEAR TERMINALES

El Arquitecto tiene Firewall activo → solo recibe **10% de dano**.
- Para bajarlo: hackear **3 de 6 Terminales** (click derecho, mantener 4s sin moverse por terminal).
- Firewall baja 12s → ventana de DPS.
- El Arquitecto ataca terminales activos para interrumpir.
- **Modo solo**: solo 2 terminales necesarios, Arquitecto ataca terminales mas lento.

### Fase 1 (100%-60%): Protocolo Guardian

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Rayo Digital | 10.0 hitscan | 15 bloques | 4s | Mira fijamente 1.5s, linea cyan | Romper linea de vision |
| Muro de Datos | Crea pared 3x3 (20 HP/bloque) | Cualquier lugar | 8s | Particulas cyan donde aparecera | Romper la pared |
| Cortocircuito | 8.0 + paralisis 2s | Radio 4 centrado en Terminal | 10s | Terminal chispea 2s | Alejarse del Terminal |

### Fase 2 (60%-30%): Modo Defensivo

**Cambios**: Necesita 4 terminales (3 en solo). Construye 2 Torretas Digitales por ciclo (3 dmg/2s, 30 HP). Rayo Digital rebota 1 vez en muros.

### Fase 3 (30%-0%): Desesperacion

**Transicion**: Glitch masivo. Sala oscurece. Se divide en 3 copias (1 real, 2 falsas con 1 HP pero dano real).

**Cambios**: Firewall desactivado permanentemente (full damage siempre). Teleporta cada 8s. El real tiene particulas mas brillantes.

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Reinicio del Sistema | Congela TODOS 1.5s | Toda la arena | 20s | Flash de pantalla | Inevitable. Boss teleporta despues para atacar |

**Estrategia**: F1-2: coordinacion de equipo, hackear terminales, destruir torretas. F3: identificar al real, burst DPS post-Reinicio. AoE del mage encuentra al real entre copias.

### Loot
| Drop | Chance |
|------|--------|
| 40-60 Pepe Coins | 100% |
| Chip del Arquitecto (trophy) | 100% |
| Drop de Boss Ch5 | 100% |
| Accesorio T3 | 25% |
| Enchanted Book nivel IV | 30% |
| Printed Circuit x4 | 50% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle | Loop | 2s | Flota, glitch sutil |
| float_move | Loop | 1s | Desplazamiento flotando |
| spawn | Once | 3s | Se materializa pixel por pixel |
| attack_ray | Once | 2.5s | Ojo carga, dispara |
| attack_wall | Once | 2s | Manos construyen muro |
| attack_short_circuit | Once | 2s | Chispas en terminal |
| attack_system_reset | Once | 3s | Flash, todo se congela |
| teleport | Once | 0.8s | Glitch, desaparece, reaparece |
| split | Once | 3s | Se divide en 3 copias |
| build_turret | Once | 2s | Construye torreta |
| firewall_down | Once | 1s | Escudo digital colapsa |
| phase_transition | Once | 4s | Glitch masivo, sala oscurece |
| death | Once | 5s | Se pixelea, colapsa en datos |

---

## 5.6 SENOR DE LAS COSECHAS (Capitulo 6)

### Identidad
- **Visual**: Treant corrupto de 6 bloques. Tronco retorcido con crops marchitas. Corona de espigas de trigo muertas. Brazos-raiz con hoces de hueso.
- **Arena**: Campo de cultivo muerto (30x30). 16 parcelas de tierra (4x4 c/u) en grid. Arbol muerto central. Cielo rojo.
- **Musica**: Orquestal siniestra, sonidos de cosecha invertida.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 6,400 | 8,320 | 12,160 | 19,840 |
| Dano | 22.4 | 25.76 | 32.48 | 45.92 |
| Velocidad | 0.24 | — | — | — |
| Armor | 12 | — | — | — |
| Knockback Res | 1.0 | — | — | — |

### Mecanica central: CULTIVAR PARA SOBREVIVIR

16 parcelas con crops corruptos. Cada 10s el Senor absorbe parcelas:
- Parcela con crop corrupto → cura 2% HP (max 32% si no haces nada)
- Parcela vacia → nada
- **Parcela con crop REAL** (plantado por jugador) → dana al boss 5% HP max (se envenena con comida pura)

El jugador debe cosechar crops corruptos y plantar reales. Llevar semillas/crops al fight.

### Fase 1 (100%-60%): Cosecha Corrupta

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Segadora | 15.0 sweep | 5 bloques frontal 120° | 5s | Levanta hoz-brazo 1.5s | Ir detras de el |
| Raiz Subterranea | 10.0 + root 2s | Emerge bajo jugador, 20 bloques | 8s | Tierra bajo target 2s antes | Moverse del punto |
| Esporas de Cosecha | 3.0/s zona 6s + Hunger III | Radio 6 AoE | 15s | Sacude corona, esporas doradas | Salir del area |

**Ciclo de absorcion**: Cada 10s canaliza 3s (quieto, brilla verde). DPS window durante canalizacion.

### Fase 2 (60%-30%): Hambruna

**Cambios**: Crops corruptos crecen cada 7s.

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Ola de Raices | 12.0 desde arbol central | Toda la arena EXCEPTO parcelas | 15s | Arbol pulsa 2s | Pararte en parcela (vacia o con crop real = safe, con corrupto = 2/s dano) |

### Fase 3 (30%-0%): Putrefaccion

**Transicion**: Se fusiona con arbol central. ENORME (8 bloques). Inmovil.

**Cambios**: Inmovil pero ataques cubren toda la arena. Absorcion continua (no por ciclos). Parcelas con crop real lo danan 1% HP/3s constantemente.

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Raiz Tsunami | 8.0 | Olas recorren arena en patrones | 8s | Olas visibles | Saltar |
| Esporas Finales | 2.0/s a todos, constante | Toda la arena | Permanente | Esporas llenan el aire | Plantar 8+ crops reales para cancelar |

**Estrategia**: Llevar 2+ stacks de semillas. Un jugador (Perfil B) cosecha corruptos y planta reales. DPS golpea durante canalizacion. F3: plantar masivamente para DPS pasivo + cancelar esporas.

### Loot
| Drop | Chance |
|------|--------|
| 50-70 Pepe Coins | 100% |
| Corazon Marchito (trophy) | 100% |
| Cristal de Boss Ch6 (para Llave del Nucleo) | 100% |
| Drop de Boss Ch6 | 100% |
| Accesorio T4 | 15% |
| Golden Hoe (Fortune III) | 30% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle | Loop | 3s | Crops se mueven, raices pulsan |
| walk | Loop | 1.5s | Raices se arrastran |
| spawn | Once | 4s | Crece del suelo como planta |
| attack_scythe | Once | 2s | Barrido con hoz. Dano frame 20 |
| attack_root_underground | Once | 3s | Manos al suelo, raiz emerge |
| attack_spore | Once | 3s | Sacude corona |
| attack_root_wave | Once | 4s | Arbol pulsa, onda de raices |
| attack_root_tsunami | Once | 3s | Olas de raices (F3) |
| absorb | Once | 3s | Canaliza, brilla verde |
| absorb_poisoned | Once | 2s | Absorbe real, se sacude, dano |
| merge_tree | Once | 5s | Se fusiona con arbol |
| phase_transition | Once | 4s | Crece, raices se expanden |
| death | Once | 6s | Se marchita, flores caen, luz |

---

## 5.7 NUCLEO DEL DUNGEON (Capitulo 7)

### Identidad
- **Visual**: Cristal viviente de 4 bloques que flota. Superficie reflectante, patrones geometricos. Fragmentos orbitando. Pulsos de luz.
- **Arena**: Sala hexagonal (radio 18 bloques). Suelo de obsidiana con cristal. 6 pilares de cristal en vertices. Vacio debajo (caer = muerte).
- **Musica**: Ambiental cristalina, crescendo por fase.

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 12,800 | 16,640 | 24,320 | 39,680 |
| Dano | 30.3 | 34.85 | 43.94 | 62.12 |
| Velocidad | 0 (flota, teleporta) | — | — | — |
| Armor | 10 | — | — | — |
| Knockback Res | 1.0 | — | — | — |

### Mecanica central: ARENA CAMBIANTE

La arena se reconfigura entre fases. Secciones desaparecen. Menos suelo = mas peligro.

Los 6 pilares de cristal son destructibles. Destruir pilar = 5% HP max como dano directo al Nucleo. Pero cada pilar destruido reduce el suelo seguro.

**Decision**: destruir pilares (burst damage) vs conservarlos (mas terreno).

### Fase 1 (100%-75%): Cristalizacion

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Rayo Prismatico | 15.0 | 20 bloques hitscan | 5s | Carga brillo 2s, linea laser | Romper vision con pilar |
| Lluvia de Shards | 8.0 por shard, 5 shards | Area 8x8 | 10s | Sombras en suelo 2s antes | Salir del area |
| Pulso Cristalino | 6.0 AoE | Radio 6 del Nucleo | 12s | Nucleo pulsa blanco 1.5s | Alejarse |

Arena completa. Aprender patrones.

### Fase 2 (75%-50%): Fragmentacion

**Transicion**: 3 secciones de suelo desaparecen. Spawna 2 Espejos.

**Espejos**: Copias cristalinas que reflejan ataques por el frente. Atacar por detras (40 HP). Respawnean cada 20s.

Lluvia de Shards ahora 8 shards. Rayo Prismatico rebota en Espejos.

### Fase 3 (50%-25%): Resonancia

**Transicion**: Arena se reduce a la mitad. Pilares emiten pulsos (3 dmg cada 8s si estas cerca).

**Cambios**: Nucleo cicla invulnerabilidad (2s invulnerable, 4s vulnerable).

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Secuencia Cristalina | 10.0 por onda, 3 ondas | Lineas en suelo | 15s | Lineas brillan 1.5s antes | Pararte entre ellas |

### Fase 4 (25%-0%): Colapso

**Transicion**: Solo plataforma central. Nucleo baja al suelo (melee accesible).

**Cambios**: Full damage siempre. Cada 15s destruye seccion de plataforma permanentemente. Enrage timer ~3 min.

| Ataque nuevo | Dano | Rango | Cooldown | Senal | Esquivar |
|-------------|------|-------|----------|-------|----------|
| Implosion | 20.0 + pull | Radio 10 | 20s | Se contrae 3s | Correr en direccion opuesta |
| Plataforma Colapsante | Destruye seccion | 1 seccion | 15s | Grietas brillantes 3s | Moverse |

**Estrategia**: F1: aprender patrones, conservar pilares. F2: Espejos por detras. F3: DPS en ventanas de vulnerabilidad. F4: DPS race, romper 1-2 pilares para burst si el timer aprieta.

### Loot
| Drop | Chance |
|------|--------|
| 60-80 Pepe Coins | 100% |
| Fragmento de Cristal del Nucleo x2-3 | 100% |
| Fragmento Central (trophy) | 100% |
| Unique Jewelry aleatoria (de las 24) | 50% |
| Accesorio T4-T5 | 25% |
| Molde de postre 5 estrellas | 20% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle_float | Loop | 3s | Flota, fragmentos orbitan |
| spawn | Once | 5s | Se cristaliza desde particulas |
| attack_ray | Once | 3s | Carga y dispara prisma |
| attack_shard_rain | Once | 4s | Fragmentos salen, caen |
| attack_pulse | Once | 2s | Pulso expansivo |
| attack_sequence | Once | 5s | Lineas de cristal en suelo |
| attack_implosion | Once | 4s | Se contrae, pull, explota |
| mirror_spawn | Once | 2s | Espejo se materializa |
| platform_collapse | Once | 3s | Seccion se agrieta y cae |
| invulnerable_cycle | Loop | 6s | Brilla cuando invulnerable |
| phase_transition | Once | 4s | Arena se reconfigura |
| death | Once | 7s | Se fragmenta, explosion de luz |

---

## 5.8 DEVORADOR DE MUNDOS (Capitulo 8 — Boss Final)

### Identidad
- **Visual**: Entidad cosmica. Comienza como esfera oscura de 3 bloques. Crece por fase hasta leviatan de 10+ bloques con tentaculos, ojos multiples, grietas de energia.
- **Arena**: Dimension void. Plataformas flotantes que el boss crea y destruye. Cielo de estrellas y nebulosas.
- **Musica**: Orquestal epica, 4 movimientos (uno por fase).

### Stats
| Stat | Base | 2 jug | 4 jug | 8 jug |
|------|------|-------|-------|-------|
| HP | 25,600 | 33,280 | 48,640 | 79,360 |
| Dano | 40.9 | 47.04 | 59.31 | 83.85 |
| Velocidad | Variable | — | — | — |
| Armor | 16 | — | — | — |
| Knockback Res | 1.0 | — | — | — |

### Mecanica central: LOS 3 PILARES COMO FASES

Cada fase testea un pilar del modpack. La fase final los combina.

### Fase 1 (100%-75%): COMBATE PURO

Pelea directa RPG. Tentaculos y rayos cosmicos.

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Azote de Tentaculo | 20.0 sweep | 6 bloques frontal | 4s | Tentaculo retrocede 1.2s | Ir detras |
| Rayo Cosmico | 25.0 hitscan | 25 bloques | 8s | Ojo carga 2.5s | Romper vision |
| Nova Oscura | 15.0 AoE + Wither II 3s | Radio 8 | 15s | Se contrae 2s | Alejarse |
| Tentaculos Rastreadores | 8.0 c/u, 4 tentaculos | Persiguen 5s | 20s | Tentaculos se elevan | Esquivar, cortar (15 HP c/u) |

### Fase 2 (75%-50%): CREATE

**Transicion** (6s): Arena se transforma en fabrica caotica. Devorador se integra con maquinaria.

**Mecanica**: Invulnerable a ataques directos. Activar 3 maquinas en orden correcto (rojo → azul → verde). Cada secuencia = Deployer le pega (10% HP). 3 secuencias completan la fase. Orden cambia cada vez.

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Engranaje Lanzado | 12.0 proyectil | 20 bloques | 3s | Engranaje se despega 1s | Agacharse |
| Piston Slam | 18.0 linea | 15 bloques | 8s | Piston cargando | Moverse lateral |
| Vapor Toxico | 5.0/s zona 4s | 6x6 | 12s | Vapor se acumula | Salir |

### Fase 3 (50%-25%): COCINA

**Transicion** (6s): Fabrica desaparece. Cooking Pot cosmico en centro. Devorador regenera 1% HP/5s.

**Mecanica**: Cocinar "Antidoto Cosmico" en el Cooking Pot. Requiere 3 ingredientes de tentaculos cortados. Cada antidoto: 5% HP de dano + aturde 8s (DPS window).

| Ataque | Dano | Rango | Cooldown | Senal | Esquivar |
|--------|------|-------|----------|-------|----------|
| Absorcion Vital | Drena HP | 15 bloques canal 5s | 20s | Hilos de energia | Salir de rango |
| Tentaculos de Ingrediente | 0 dmg, 30 HP c/u | 3 cada 15s | — | Brillan con color | Cortarlos = ingredientes |
| Onda de Hambre | 10.0 + Hunger III 5s | Radio 10 | 12s | Pulso oscuro | Alejarse |

### Fase 4 (25%-0%): TODO

**Transicion** (8s): 3 mecanicas activas simultaneamente. Arena se destruye progresivamente.

**Cambios**: Ataques de F1 + maquinas de F2 + antidotos de F3 (regenera 2% HP/5s). Plataformas desaparecen cada 30s.

**Enrage**: A los 5 min de F4, ataque de 999 dano. Hay que matarlo antes.

**Estrategia**: Coordinacion total. 1 tanque, 1 DPS melee, 1 ranged/mage, 1 support (corta tentaculos, cocina, activa maquinas). En solo: posible con T4 completo y mucha preparacion.

### Loot
| Drop | Chance |
|------|--------|
| 100+ Pepe Coins | 100% |
| Corazon del Vacio (trophy, mega quest) | 100% |
| Fragmento de Cristal del Nucleo x4-5 | 100% |
| Unique Jewelry Legendaria (Ring of Omnipotence O Pendant of Acumen) | 100% |
| Accesorio T5 Legendario | 50% |
| Titulo: "Devorador Derrotado" (chat prefix) | 100% |

### Animaciones GeckoLib
| Nombre | Tipo | Duracion | Notas |
|--------|------|----------|-------|
| idle_sphere | Loop | 3s | Esfera pulsa |
| idle_leviathan | Loop | 4s | Forma final, tentaculos |
| spawn | Once | 6s | Esfera aparece, crece |
| attack_tentacle | Once | 2s | Azote |
| attack_cosmic_ray | Once | 3.5s | Ojo carga, rayo |
| attack_nova | Once | 3s | Contraccion, explosion |
| attack_tracking_tentacles | Once | 6s | 4 tentaculos persiguen |
| attack_gear_throw | Once | 1.5s | Engranaje disparado (F2) |
| attack_piston | Once | 2.5s | Piston baja (F2) |
| attack_absorb | Once | 5s | Hilos drenan (F3) |
| attack_hunger_wave | Once | 3s | Pulso hambre (F3) |
| tentacle_ingredient | Once | 2s | Tentaculo brilla, cortable |
| grow_phase2 | Once | 6s | Crece, maquinaria |
| grow_phase3 | Once | 6s | Forma organica |
| grow_phase4 | Once | 8s | Leviatan cosmico |
| enrage | Once | 3s | Grito final |
| death | Once | 10s | Fragmenta, implosion, estrellas |

---

# 6. BOSSBAR Y UI

Todos los jefes usan BossBar nativa (BossEvent):
- Nombre con color tematico por jefe
- Color de barra cambia por fase (verde → amarillo → naranja → rojo)
- Sub-barra custom (overlay de servo_core) para mecanicas secundarias:
  - Bestia Glotona: Saciedad (barra verde)
  - Coloso Mecanico: Iconos de paneles (4 iconos naranja/gris)
  - Arquitecto: Firewall (barra azul)
  - Senor de las Cosechas: Parcelas corruptas vs reales (iconos)

---

# 7. MUERTE Y SOULBOUND EN BOSS CHAMBER

Reglas especificas para Boss Chamber (diferentes a dungeons — ver [Death System](death-system.md)):

**Soulbound** (se conserva al morir):
- Armor equipada (4 piezas)
- Arma principal equipada
- Rune Pouch equipada
- Accesorios Curios (rings, necklaces, belt, back, feet)
- Solo aplica a gear T2+

**Se pierde al morir**:
- Comida y pociones
- Bloques y materiales sueltos
- Items extra en inventario
- La Boss Key ya se consumio al entrar

**Diferencia con dungeons**: En boss chamber NO hay tumba YIGD. El jugador respawnea en el overworld junto al Altar con su gear soulbound, y el portal se reabre 15s para re-entrar. En dungeons, YIGD crea tumba y el jugador puede volver a buscarla.

---

# 8. BOSS DE DUNGEON

Separado de bosses de capitulo. Solo aparece en **Dungeon Del Nucleo** (Ch7+).
- Garantiza 1-2 Fragmentos de Cristal del Nucleo
- Garantiza 1 Unique Jewelry
- 25% chance accesorio custom T4

---

# 9. PREGUNTAS ABIERTAS

- [ ] Boss Altar: crafteable o pre-colocado? Si crafteable, que materiales?
- [ ] Animaciones: duraciones exactas requieren iteracion en Blockbench
- [ ] Sonidos custom vs vanilla reutilizados
- [ ] Devorador F2: diseno exacto de las 3 maquinas y secuencia
- [ ] Devorador F3: receta exacta del Antidoto Cosmico
- [ ] Balance: recraftear Boss Key al wipe es frustante? Alternativa: Boss Key no se consume si wipe total, solo en victoria
- [ ] Boss Key multiplayer: 1 llave por grupo confirmado. El que inserta hostea
- [ ] Death animations: duracion ideal
- [ ] Trofeos: solo decorativos o con stats?
- [ ] Locomotora Fantasma: arena circular en dimension void, verificar performance
