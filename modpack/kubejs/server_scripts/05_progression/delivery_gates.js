// =============================================================================
// 05_progression/delivery_gates.js
// PROPOSITO: Gate de progression para bloques del Terminal de Entrega.
//
// ARQUITECTURA DEL SISTEMA:
//   Los bloques de servo_delivery son multibloques criticos para la progression.
//   El jugador LOS RECIBE como recompensa de quest en Ch1 — no necesita craftearlos.
//   Las recetas JSON en servo_delivery/recipe/ son unicamente "recetas de backup"
//   para recovery si los bloques se pierden (bug, accidente, lava).
//
//   Por lo tanto, las recetas de backup SOLO deben estar disponibles despues de
//   que el jugador ya recibio los bloques por quest. De lo contrario, un jugador
//   nuevo podria craftearlos en Ch1 antes de recibir los bloques por quest,
//   rompiendo el flujo de introduccion del modpack.
//
//   Este script implementa la SEGUNDA CAPA de seguridad para crafting via KubeJS.
//   La primera capa deberia ser ProgressiveStages (TOML), pero como servo_delivery
//   es un mod custom en desarrollo, KubeJS es la capa principal hasta que el TOML
//   de ProgressiveStages para servo_delivery sea configurado.
//
// FLUJO DE OBTENCION DE BLOQUES (doc: docs/mechanics/space-elevator.md):
//   Ch1 quest temprana ("Cocina tu primera comida") → recompensa: 9 bloques
//   (1x delivery_terminal, 2x delivery_port, 3x elevator_base, 1x elevator_antenna + extras)
//   Los bloques se dan ANTES de que el jugador necesite craftearlos.
//   La receta de backup es solo para el caso en que los pierda.
//
// STAGES:
//   servo_delivery bloques → requieren servo_ch1 (recibidos en quest Ch1)
//
// BLOQUES GATEADOS (IDs verificados en DeliveryRegistry.java):
//   servo_delivery:delivery_terminal  — bloque principal, GUI de entrega
//   servo_delivery:delivery_port      — puertos laterales para automation
//   servo_delivery:elevator_base      — base estructural del multibloque
//   servo_delivery:elevator_antenna   — decorativo, particulas al completar
//
// RECETAS ORIGINALES (en servo_delivery/src/main/resources/data/servo_delivery/recipe/):
//   delivery_terminal: 4 Iron Ingot + 2 Glass + 1 Redstone + 1 Crafting Table (shaped IRI/IGI/ICI)
//   delivery_port:     4 Iron Ingot + 1 Hopper + 1 Chest (shaped I I/IHI/ICI)
//   elevator_base:     3 Iron Block + 3 Stone Brick (via KubeJS abajo)
//   elevator_antenna:  4 Iron Ingot + 1 Lightning Rod (via KubeJS abajo)
//
// NOTA: elevator_base y elevator_antenna NO tienen receta JSON en el mod (no existen
//   en servo_delivery/recipe/). Este script define AMBAS recetas y las gateas.
//   delivery_terminal y delivery_port SI tienen receta JSON en el mod — este script
//   las cancela si el stage no se cumple (segunda capa de seguridad).
//
// IDs verificados contra: servo-delivery/src/main/java/com/servo/delivery/DeliveryRegistry.java
// Ingredientes verificados contra: docs/mechanics/space-elevator.md seccion 2 tabla backup
// Issue: #94
// =============================================================================

// =============================================================================
// RECETAS ADICIONALES: elevator_base y elevator_antenna
// Estas no existen como JSON en el mod, se definen aqui para completitud.
// Se agregan con namespace servo_core: (recetas custom del modpack)
// =============================================================================
ServerEvents.recipes(event => {

    // elevator_base: 3 Iron Block + 3 Stone Brick
    // Patron: fila superior Iron Blocks, fila media Stone Bricks
    //   B B B
    //   S S S
    //   (no ingrediente en fila inferior — receta 3x2 en grid 3x3)
    event.shaped('servo_delivery:elevator_base', [
        'BBB',
        'SSS'
    ], {
        B: 'minecraft:iron_block',
        S: 'minecraft:stone_bricks'
    }).id('servo_core:delivery_elevator_base_backup');

    // elevator_antenna: 4 Iron Ingot + 1 Lightning Rod
    // Patron: barra vertical con remate
    //   _I_
    //   ILI
    //   _I_
    event.shaped('servo_delivery:elevator_antenna', [
        ' I ',
        'ILI',
        ' I '
    ], {
        I: 'minecraft:iron_ingot',
        L: 'minecraft:lightning_rod'
    }).id('servo_core:delivery_elevator_antenna_backup');

    console.log('[Servo] delivery_gates: recetas backup de elevator_base y elevator_antenna registradas');
});

// =============================================================================
// GATE DE CRAFTING: servo_delivery → requiere servo_ch1
//
// Todos los bloques del Terminal de Entrega requieren haber completado la quest
// de Ch1 que otorga los bloques. El stage servo_ch1 se da al inicio del juego
// (o al completar la quest de onboarding).
//
// Si el jugador no tiene servo_ch1 → los bloques son inaccesibles via craft.
// Mensaje explicativo con instruccion de donde obtenerlos.
// =============================================================================
PlayerEvents.craftedItem(event => {
    const { item, player } = event;

    // Solo interceptar items de servo_delivery
    if (!item.id.startsWith('servo_delivery:')) return;

    // Si el jugador ya tiene Ch1, permitir el craft (recovery legitimo)
    if (player.stages.has('servo_ch1')) return;

    // Sin Ch1: cancelar craft y explicar como obtener los bloques
    event.cancel();
    player.tell(
        Text.of('[Servo] ')
            .append(item.name.yellow())
            .append(Text.of(' se obtiene como recompensa de quest en el '))
            .append(Text.of('Capitulo 1').green())
            .append(Text.of('. Completa "Cocina tu primera comida" en FTB Quests.'))
    );
    console.log(
        '[Servo] delivery_gates: ' + player.username +
        ' intento craftear ' + item.id +
        ' sin stage servo_ch1 (bloqueado — usar quest de Ch1 para obtenerlos)'
    );
});

console.log('[Servo] 05_progression/delivery_gates: gates del Terminal de Entrega cargados (servo_delivery → servo_ch1)');
