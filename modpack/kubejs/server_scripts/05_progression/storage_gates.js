// =============================================================================
// 05_progression/storage_gates.js
// PROPOSITO: Gate de progression para mods de storage.
//
// ARQUITECTURA DEL SISTEMA:
//   La fuente de verdad de los locks es ProgressiveStages (TOMLs en config/progressivestages/).
//   Este script es la SEGUNDA CAPA de seguridad para crafting, implementada via KubeJS.
//
//   ProgressiveStages (primera capa, TOMLs):
//     - servo_ch3.toml: mods = [..., "toms_storage", ...]
//     - servo_ch5.toml: mods = [..., "refinedstorage", ...]
//     - block_crafting = true, block_item_use = true, block_item_pickup = true
//     - show_locked_recipes = false  → items ocultos de EMI automaticamente
//
//   Este script (segunda capa, KubeJS):
//     - Cancela el craft si el jugador no tiene el stage requerido
//     - Funciona como fallback si ProgressiveStages no intercepta algun edge case
//     - Es mas explicito y trazable en logs de KubeJS (run/logs/kubejs/server.log)
//
// STAGES:
//   toms_storage  → requiere servo_ch3 (Capitulo 3: Engranajes y Magia)
//   refinedstorage → requiere servo_ch5 (Capitulo 5: Red y Poder)
//
// ITEMS VERIFICADOS:
//   toms_storage (docs/mod-data/toms_storage.json):
//     Bloques: storage_terminal, crafting_terminal, inventory_connector,
//              inventory_cable, inventory_cable_connector, inventory_interface,
//              inventory_proxy, filing_cabinet, basic_inventory_hopper,
//              level_emitter, open_crate, trim, inventory_cable_framed,
//              inventory_cable_connector_framed, painted_trim
//     Items:   wireless_terminal, adv_wireless_terminal, inventory_configurator,
//              item_filter, polymorphic_item_filter, tag_item_filter, paint_kit
//   refinedstorage (mod ID verificado en servo_ch5.toml):
//     Ver documentacion oficial RS2 — items gateados via mod lock completo
//
// NOTA SOBRE FALSOS POSITIVOS:
//   Ningun item de toms_storage es prerequisito de items de Ch1 o Ch2.
//   El inventory_cable usa planks/chests/trapdoors (materiales vanilla disponibles desde Ch1),
//   pero el cable en si es un item de Ch3 — correcto segun storage.md.
//   Ninguna receta de Ch1/Ch2 requiere materiales de toms_storage o refinedstorage.
//
// IDs verificados contra: docs/mod-data/toms_storage.json, docs/mechanics/storage.md
// Issue: #88
// =============================================================================

// =============================================================================
// GATE DE CRAFTING: Tom's Simple Storage → requiere servo_ch3
// =============================================================================
PlayerEvents.craftedItem(event => {
    const { item, player } = event;

    // Verificar si el item crafteado pertenece a toms_storage
    if (!item.id.startsWith('toms_storage:')) return;

    // Si el jugador no tiene Ch3, cancelar craft y notificar
    if (!player.stages.has('servo_ch3')) {
        event.cancel();
        player.tell(
            Text.of('[Servo] ')
                .append(item.name.yellow())
                .append(Text.of(' requiere desbloquear el '))
                .append(Text.of('Capitulo 3: Engranajes y Magia').aqua())
                .append(Text.of('.'))
        );
        console.log(
            '[Servo] storage_gates: ' + player.username +
            ' intento craftear ' + item.id +
            ' sin stage servo_ch3 (bloqueado)'
        );
    }
});

// =============================================================================
// GATE DE CRAFTING: Refined Storage → requiere servo_ch5
// =============================================================================
PlayerEvents.craftedItem(event => {
    const { item, player } = event;

    // Verificar si el item crafteado pertenece a refinedstorage
    if (!item.id.startsWith('refinedstorage:')) return;

    // Si el jugador no tiene Ch5, cancelar craft y notificar
    if (!player.stages.has('servo_ch5')) {
        event.cancel();
        player.tell(
            Text.of('[Servo] ')
                .append(item.name.yellow())
                .append(Text.of(' requiere desbloquear el '))
                .append(Text.of('Capitulo 5: Red y Poder').light_purple())
                .append(Text.of('.'))
        );
        console.log(
            '[Servo] storage_gates: ' + player.username +
            ' intento craftear ' + item.id +
            ' sin stage servo_ch5 (bloqueado)'
        );
    }
});

console.log('[Servo] 05_progression/storage_gates: gates de storage cargados (Tom\'s Ch3, RS Ch5)');
