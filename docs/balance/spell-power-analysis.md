# Spell Power Balance: DPS vs Boss HP por Capitulo

> Analisis completo para [Issue #28](https://github.com/servo98/modpack-servo/issues/28)
> Relacionado: [combat-scaling.md](combat-scaling.md), [rpg-weapon-stats.md](rpg-weapon-stats.md), [bosses](../mechanics/bosses.md)

---

## 1. DPS SUSTENTADO POR CLASE (single-target, rotacion optima)

### Melee (de combat-scaling.md, incluye armor bonus de set)

| Cap | Warrior (Claymore) | Rogue (Dagger) | Paladin (Mace) | Nota equipo |
|-----|-------------------|----------------|----------------|-------------|
| 1   | 9.6               | 6.2            | ~6.8           | Vanilla sword / Flint dagger / Stone mace |
| 2   | 9.6               | 9.2            | 8.4            | Iron T1 + War/Rogue/Pal armor T1 |
| 3   | 11.9              | 12.5           | 10.0           | Diamond T2 + armor T2 |
| 4   | 11.9              | 12.5           | 10.0           | Mismo (T2 plateau) |
| 5   | 13.8              | 14.7           | 11.5           | Netherite T3 + armor T3 |
| 6   | 13.8              | 14.7           | 11.5           | Mismo (T3 plateau) |
| 7   | 16.2              | 18.5           | 13.0           | T3 + enchants |
| 8   | 15.6              | 17.2           | 13.2           | Custom T4 |

> Paladin: armor da healing power, no dmg bonus. DPS = puro weapon.
> Warrior: +4-5% dmg/pieza (x4 = +16-20%).
> Rogue: +2-5% dmg + haste/crit (x4 = scaling compuesto).

### Wizard (recalculado con rotacion optima)

**Formula**: damage = effective_SP * coefficient / cast_time

| Cap | Weapon + Armor | Eff SP | Mejor rotacion | Sustained DPS | Nota |
|-----|---------------|--------|----------------|---------------|------|
| 1   | Novice Wand T0 (sin armor) | 3.0 | fireball (0.8/1.5s) | **1.6** | Sin armor magica. 6x peor que warrior |
| 2   | — | — | melee only | **—** | No hay equipo magico disponible |
| 3   | Staff T2 + Robe T2 (+100%) | 12.0 | Frost: frostbolt (0.8/1.1s) | **8.7** | Frost > Fire (8.0) > Arcane (6.4) |
| 4   | Staff T2 + Robe T2 | 12.0 | frost_nova + frostbolt filler | **8.9** | Burst CDs mejoran poco el sustained |
| 5   | NR Staff T3 + Robe T3 (+120%) | 15.4 | frostbolt sustained | **11.2** | Frost sigue siendo mejor single-target |
| 6   | NR Staff T3 + Robe T3 | 15.4 | frostbolt sustained | **11.2** | T3 plateau |
| 7   | NR Staff enchanted (~8.0 SP) + Robe T3 | 17.6 | frostbolt | **12.8** | |
| 8   | Custom T4 Staff (8.0 SP) + Robe T3 | 17.6 | frostbolt | **12.8** | T4 staff pero no hay T4 robes |

**Correccion importante**: La tabla anterior en combat-scaling.md subestima el DPS de wizard. Frost wizard con frostbolt (sin cooldown, 1.1s cast) tiene el mejor sustained single-target de las 3 escuelas. El frost_blizzard (12.3 DPS listado para Ch8) es PEOR que frostbolt spam (12.8 DPS) para single-target — blizzard solo es mejor en AoE.

### Comparacion cross-class (DPS promedio por capitulo)

| Cap | Warrior | Rogue | Wizard | Paladin | Promedio | Clase mas fuerte |
|-----|---------|-------|--------|---------|----------|-----------------|
| 1   | 9.6     | 6.2   | 1.6    | 6.8     | 6.1      | Warrior (9.6)   |
| 2   | 9.6     | 9.2   | —      | 8.4     | 9.1      | Warrior (9.6)   |
| 3   | 11.9    | 12.5  | 8.7    | 10.0    | 10.8     | Rogue (12.5)    |
| 4   | 11.9    | 12.5  | 8.9    | 10.0    | 10.8     | Rogue (12.5)    |
| 5   | 13.8    | 14.7  | 11.2   | 11.5    | 12.8     | Rogue (14.7)    |
| 6   | 13.8    | 14.7  | 11.2   | 11.5    | 12.8     | Rogue (14.7)    |
| 7   | 16.2    | 18.5  | 12.8   | 13.0    | 15.1     | Rogue (18.5)    |
| 8   | 15.6    | 17.2  | 12.8   | 13.2    | 14.7     | Rogue (17.2)    |

> **Wizards bypasean armor** — contra bosses con armor alta (Coloso: 14), su DPS efectivo es mejor que melee. Ver seccion 3.

---

## 2. BOSS HP vs TTK: EL PROBLEMA

### Raw TTK (100% uptime, sin armor, sin mecanicas)

TTK = Boss HP / DPS

| Cap | Boss HP | Warrior TTK | Rogue TTK | Wizard TTK | Target duracion |
|-----|---------|-------------|-----------|------------|-----------------|
| 1   | 200     | 21s (0.3m)  | 32s (0.5m)| 125s (2.1m)| **3-5 min**    |
| 2   | 400     | 42s (0.7m)  | 43s (0.7m)| —          | **5-7 min**    |
| 3   | 800     | 67s (1.1m)  | 64s (1.1m)| 92s (1.5m) | **7-10 min**   |
| 4   | 1,600   | 134s (2.2m) | 128s (2.1m)| 180s (3.0m)| **8-12 min**  |
| 5   | 3,200   | 232s (3.9m) | 218s (3.6m)| 286s (4.8m)| **10-14 min** |
| 6   | 6,400   | 464s (7.7m) | 435s (7.3m)| 571s (9.5m)| **12-16 min** |
| 7   | 12,800  | 790s (13.2m)| 692s (11.5m)| 1000s (16.7m)| **15-20 min**|
| 8   | 25,600  | 1641s (27.4m)| 1488s (24.8m)| 2000s (33.3m)| **20-30 min**|

### Eficiencia necesaria para alcanzar duracion target

Eficiencia = % del tiempo que el jugador esta haciendo dano al boss.
**Valores realistas**: 40-60% (el resto es esquivar, curarse, mecanicas, fases invulnerables).

| Cap | Target min | Warrior: eff necesaria | Rogue: eff necesaria | Wizard: eff necesaria | Realista? |
|-----|-----------|----------------------|--------------------|--------------------|-----------|
| 1   | 180s      | **12%**              | 18%                | 69%                | NO — 12% = player golpea solo 1 de cada 8 segundos |
| 2   | 300s      | **14%**              | 14%                | —                  | NO — mecanicas tendrian que ocupar 86% del tiempo |
| 3   | 420s      | **16%**              | 15%                | 22%                | NO — solo 15% uptime es frustrante |
| 4   | 480s      | 28%                  | 27%                | 38%                | BAJO — posible con mecanicas muy demandantes |
| 5   | 600s      | 39%                  | 36%                | 48%                | OK — mecanicas ocupan ~60% del tiempo |
| 6   | 720s      | 64%                  | 60%                | 79%                | JUSTO — wizard casi no puede pausar |
| 7   | 900s      | 88%                  | 77%                | **111%**            | NO — wizard no puede matar boss en 15min ni al 100% |
| 8   | 1200s     | **137%**             | **124%**           | **167%**            | IMPOSIBLE — ninguna clase mata al boss solo en 20min |

### Diagnostico

| Problema | Capitulos | Severidad |
|----------|-----------|-----------|
| Boss muere demasiado rapido | Ch1-3 | ALTA — raw TTK < 2 min, necesita uptime irrealistamente bajo |
| Balance ajustado | Ch4-5 | OK con mecanicas moderadas |
| Boss resiste demasiado (solo) | Ch6 | MEDIA — wizard apenas alcanza |
| Boss imposible solo | Ch7-8 | CRITICA — aun con 100% uptime, excede target |

**Causa raiz**: HP crece x2 por capitulo (128x total en 8 caps), pero DPS crece ~15% por tier (1.8x total). La divergencia es exponencial.

---

## 3. FACTOR ARMOR (melee vs spells)

Los spells de RPG Series bypasean armor (solo aplican spell resistance, que es 0 por defecto). Esto importa contra bosses con armor alta.

**Formula MC armor**: `reduction = min(20, max(armor/5, armor - 4*damage/(toughness+8))) / 25`

| Boss | Armor | Warrior hit dmg | Reduccion | Effective DPS | Wizard DPS | Ventaja wizard |
|------|-------|-----------------|-----------|---------------|------------|---------------|
| Ch1 Guardian | 4 | 9.9 (claymore T0=vanilla sword) | 3% | 9.3 | 1.6 | Ninguna (wizard muy debil) |
| Ch2 Bestia | 6 | 9.6 | 5% | 9.1 | — | — |
| Ch3 Coloso | **14** | 9.9 (claymore T2 per-hit) | **32%** | **8.1** | 8.7 | **Wizard > Warrior** |
| Ch4 Locomotora | 8 | 9.9 | 8% | 10.9 | 8.9 | Menor |
| Ch5+ | ~10 est. | varies | ~12% | varies | varies | Moderada |

> El Coloso (Ch3, 14 armor) es el caso mas extremo. Melee pierde 32% de DPS por armor, haciendo que wizard sea competitivo a pesar de tener menor DPS raw. Esto es buen design — cada boss favorece clases distintas.

**Nota**: El Coloso ademas tiene la mecanica de 25% dano normal (solo x3 en paneles activos). Esto reduce el effective melee DPS aun mas. Wizard con arcane_beam a distancia puede explotar los paneles sin exponerse.

---

## 4. MULTIPLAYER SCALING

Formula: `HP = base * (1 + (players-1) * 0.3)`
DPS escala linealmente (n jugadores = n veces DPS).

**TTK multiplayer** = TTK_solo * `(1 + (n-1)*0.3) / n`

| Jugadores | Factor HP | Factor DPS | Factor TTK | % mas rapido que solo |
|-----------|-----------|-----------|-----------|----------------------|
| 1         | 1.0x      | 1.0x      | 1.00x     | — |
| 2         | 1.3x      | 2.0x      | 0.65x     | 35% mas rapido |
| 4         | 1.9x      | 4.0x      | 0.48x     | 52% mas rapido |
| 8         | 3.1x      | 8.0x      | 0.39x     | 61% mas rapido |

> La formula de scaling es correcta: mas jugadores = mas rapido, pero con rendimientos decrecientes. Un grupo de 4 NO es 4x mas rapido, sino ~2x. Esto incentiva cooperar sin trivializar.

**Ejemplo**: Ch8 boss con HP propuesto (10,400):
- Solo (warrior): 10,400 / (15.6 * 0.45) = 1,481s = **24.7 min** ✓
- 2 jugadores (war+rog): 13,520 / ((15.6+17.2) * 0.50) = 13,520 / 16.4 = 824s = **13.7 min** ✓
- 4 jugadores: 19,760 / (52 * 0.50) = 760s = **12.7 min** ✓

---

## 5. PROPUESTA: CURVA DE HP REVISADA

### Formula: `HP = 800 * 1.44^(cap-1)`

Reemplaza la curva actual de `200 * 2^(cap-1)`.

| Cap | HP actual | HP propuesto | Cambio | Growth ratio |
|-----|-----------|-------------|--------|-------------|
| 1   | 200       | **800**     | x4.0   | — |
| 2   | 400       | **1,200**   | x3.0   | 1.50x |
| 3   | 800       | **1,600**   | x2.0   | 1.33x |
| 4   | 1,600     | **2,400**   | x1.5   | 1.50x |
| 5   | 3,200     | **3,400**   | x1.1   | 1.42x |
| 6   | 6,400     | **5,000**   | x0.78  | 1.47x |
| 7   | 12,800    | **7,200**   | x0.56  | 1.44x |
| 8   | 25,600    | **10,400**  | x0.41  | 1.44x |

> Crece x1.44 por capitulo (13x total) en vez de x2 (128x total). Alinea con el crecimiento de DPS (~1.3-1.5x por tier de equipo).

### TTK proyectado con HP propuesto (45% eficiencia promedio)

| Cap | HP prop | Warrior TTK | Rogue TTK | Wizard TTK | Target | Status |
|-----|---------|-------------|-----------|------------|--------|--------|
| 1   | 800     | 3.1 min     | 4.8 min   | 18.5 min*  | 3-5m   | Melee ✓ Wizard ✗ (Ch1 no es para wizards) |
| 2   | 1,200   | 4.6 min     | 4.8 min   | —          | 5-7m   | ✓ (un poco rapido, mecanicas lo extienden) |
| 3   | 1,600   | 5.0 min     | 4.7 min   | 6.8 min    | 7-10m  | Necesita mecanicas para llegar a 7 min |
| 4   | 2,400   | 7.5 min     | 7.1 min   | 10.0 min   | 8-12m  | ✓ |
| 5   | 3,400   | 9.1 min     | 8.6 min   | 11.2 min   | 10-14m | ✓ |
| 6   | 5,000   | 13.4 min    | 12.6 min  | 16.5 min   | 12-16m | ✓ (wizard un poco largo) |
| 7   | 7,200   | 16.5 min    | 14.4 min  | 20.8 min   | 15-20m | Melee ✓ Wizard largo |
| 8   | 10,400  | 24.7 min    | 22.4 min  | 30.1 min   | 20-30m | Melee ✓ Wizard al borde |

*Ch1 wizard: 1.6 DPS es intencional — wizard no es viable hasta Ch3.

### Problema residual: Wizard TTK en Ch6-8

Con HP propuesto, wizard solo excede el target en Ch6-8. Opciones:

**Opcion A: Aceptar — wizard sacrifica DPS por utility**
- Wizards tienen AoE (blizzard, meteor, fire_breath) para adds/minions
- Frost nova = CC valioso
- Frost shield = supervivencia
- En grupo, un wizard no necesita ser top DPS; su AoE y CC compensan

**Opcion B: Buff SP de T3-T4 staves (+1.0 SP via KubeJS)**
- NR Staff T3: 7.0 → 8.0 SP → eff SP = 17.6 → DPS = 12.8 → 14.5 (ya en la tabla como Ch7)
- Custom T4 Staff: 8.0 → 9.5 SP → eff SP = 20.9 → DPS = 15.2
- Cierra la brecha wizard-melee en Ch6-8

**Opcion C: Agregar T4 Robes (+140% school power)**
- Actualmente T3 Robes (+120%) son el tope. No hay T4 Robes.
- Si T4 Robes dieran +35% por pieza (+140% total): eff SP = 8.0 * 2.4 = 19.2 → DPS = 14.0
- Require crear items custom en servo_core o KubeJS

**Recomendacion: Opcion A** para MVP. Wizard esta disenado como utility/AoE, no como top single-target DPS. En grupo (que es el caso esperado para Ch6-8), el wizard aporta mas que su DPS numerico.

---

## 6. BOSS DAMAGE VS PLAYER: VERIFICACION

### Boss damage base por capitulo

Formula: `dmg = 5.0 * 1.35^(cap-1)`

| Cap | Boss Dmg | Player HP (20) | Hits to kill player | Con food (28 HP est.) | Con armor (red%) |
|-----|----------|----------------|--------------------|-----------------------|-----------------|
| 1   | 5.0      | 20             | 4.0 hits           | 5.6 hits              | ~6.5 hits (iron)|
| 2   | 6.8      | 20             | 2.9                | 4.1                   | ~5.0            |
| 3   | 9.1      | 20             | 2.2                | 3.1                   | ~4.5 (diamond)  |
| 4   | 12.3     | 20             | 1.6                | 2.3                   | ~3.5            |
| 5   | 16.6     | 20             | 1.2                | 1.7                   | ~3.0 (netherite)|
| 6   | 22.4     | 20             | 0.9 (1-shot!)      | 1.3                   | ~2.5            |
| 7   | 30.3     | 20             | 0.7 (1-shot!)      | 0.9                   | ~2.0 (exactly!) |
| 8   | 40.9     | 20             | 0.5 (1-shot!)      | 0.7                   | ~1.5            |

### Problema: Ch6-8 one-shots sin armor

- Ch6+: boss hace mas de 20 HP por hit → 1-shot a jugadores sin armor
- Con Berserker T3 (19 prot, 4 tough): reduccion ~40-50% → sobrevive con 10-12 HP
- Con Wizard Robes T3 (7 prot, 0 tough): reduccion ~14% → 19.3 dmg → **1-shot**
- Rogues T3 (12 prot): reduccion ~24% → 17.0 dmg → sobrevive con 3 HP

> **Wizard survivability es un problema en Ch6-8**. Necesitan frost_shield + evasion perfecta o mueren de un hit. Esto es consistente con el arquetipo "glass cannon" pero puede ser frustrante.

### Verificacion del scaling de dmg

El boss damage crece x1.35 por capitulo (8.2x total). Player effective HP crece:
- Base HP: 20 → 20 (no crece)
- Armor: 0 → 19 prot + 4 tough (~50% reduccion) = x2 effective HP
- Food: +8 HP = x1.4
- Accessories regen: ~+2 HP/s = sustain

Total effective HP growth: ~2.8x. Boss damage growth: 8.2x. Ratio: 2.9x divergencia.

Esto significa que Ch8 boss hits son **~3x mas letales** relativo al jugador que Ch1. Esto es intencional (dificultad escalante) pero el wizard sin armor queda expuesto.

---

## 7. RESUMEN DE HALLAZGOS Y ACCIONES

### Hallazgos clave

| # | Hallazgo | Impacto |
|---|----------|---------|
| 1 | HP crece 128x pero DPS crece 1.8x → divergencia exponencial | CRITICO |
| 2 | Ch1-3: boss muere en <2 min raw, requiere uptime irrealista (<15%) para durar | ALTO |
| 3 | Ch7-8: boss imposible solo con HP actual (25,600 excede cualquier DPS) | CRITICO |
| 4 | Wizard DPS es 30% menor que melee pero bypasea armor | Aceptable (utility/AoE compensa) |
| 5 | Wizard Ch1: 6x menor DPS que warrior | OK (disenado asi, magia empieza Ch3) |
| 6 | Rogue es la clase con mayor sustained DPS en todo el modpack | Para monitorear |
| 7 | Frost wizard > Fire wizard > Arcane wizard en single-target sustained | Info |
| 8 | Wizard sobrevive 1 hit de boss Ch6+ solo con frost_shield activo | Riesgo de frustacion |
| 9 | Multiplayer scaling (30% HP/player) funciona correctamente | OK |

### Acciones recomendadas

| Accion | Prioridad | Esfuerzo | Detalle |
|--------|-----------|----------|---------|
| **Adoptar HP = 800 * 1.44^(cap-1)** | ALTA | Bajo | Cambiar 8 numeros en bosses.md y combat-scaling.md |
| **Recalcular boss damage** | ALTA | Bajo | Verificar que dmg sigue siendo letal pero no instant-kill con Berserker armor |
| **Corregir tabla wizard DPS** en combat-scaling.md | MEDIA | Bajo | Frost wizard sustained es 8.7-12.8, no los valores actuales |
| **Decidir wizard T4 armor** | BAJA | Medio | Opcion A (aceptar), B (buff staves) o C (T4 robes) |
| **Testear Ch3 Coloso melee** | MEDIA | Medio | 14 armor + 25% dano normal puede hacer melee insatisfactorio |
| **Ajustar boss armor** | BAJA | Bajo | Considerar cap de 10 armor para bosses (evitar situacion Coloso) |

### Archivos a actualizar si se adopta

- `docs/balance/combat-scaling.md` — Boss HP table, Wizard DPS table
- `docs/mechanics/bosses.md` — HP en tabla resumen y en cada boss individual
- `docs/balance/rpg-weapon-stats.md` — agregar nota sobre frost > fire > arcane sustained DPS
