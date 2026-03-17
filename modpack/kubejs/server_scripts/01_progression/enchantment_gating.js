// =============================================================================
// PROPOSITO: Gatear niveles de enchantment por capitulo (ProgressiveStages).
//
// Segun docs/mechanics/enchantments.md y docs/mechanics/progression.md:
//   Ch1-2 → Max nivel II  (default inicial)
//   Ch3-4 → Max nivel III (stage servo_ch3)
//   Ch5   → Max nivel IV  (+Spell Power enchants desbloqueados)
//   Ch6+  → Sin restriccion (nivel V, todo disponible)
//
// MECANISMO TECNICO (MC 1.21.1 + KubeJS 6):
//   En MC 1.21.1 los enchantments son Data Components, no NBT legacy.
//   No hay hook de KubeJS que intercepte la enchanting table directamente.
//
//   Approach adoptado:
//   - PlayerEvents.inventoryChanged: se dispara cuando un item entra al
//     inventario del jugador (desde enchanting table, anvil, loot, etc.)
//   - Se itera sobre los enchants del item usando item.enchantments
//   - Los enchants prohibidos se eliminan via item.enchantments.remove(key)
//   - Si el item queda sin enchants validos, se normaliza (queda sin enchants)
//   - El jugador recibe un mensaje en chat indicando los enchants removidos
//
//   NOTA IMPORTANTE sobre item.enchantments en KubeJS 6:
//   ItemStack.enchantments retorna un proxy mutable del componente
//   DataComponents.ENCHANTMENTS. Las llamadas a .remove() en este proxy
//   mutan el ItemStack directamente (confirmado en KubeJS6 source:
//   ItemStackWrapper wraps the DataComponent and delegates mutations).
//
// ENCHANTS DE SPELL POWER (Ch5+):
//   Los 8 enchants del mod spell_power son magicos — disponibles desde Ch5
//   junto con Create Enchantment Industry. Se bloquean para players sin Ch5.
//
// Issue #89 - Enchantment progression gating
// =============================================================================

// =============================================================================
// TABLA DE ENCHANTS DE SPELL POWER — bloqueados hasta Ch5
// IDs segun docs/mechanics/enchantments.md (namespace: spell_power)
// =============================================================================
const SPELL_POWER_ENCHANTS = new Set([
    'spell_power:spell_power',
    'spell_power:sunfire',
    'spell_power:soulfrost',
    'spell_power:energize',
    'spell_power:spell_haste',
    'spell_power:spell_volatility',
    'spell_power:amplify_spell',
    'spell_power:magic_protection',
]);

// =============================================================================
// HELPER: Convierte numero a numeral romano (1-5)
// =============================================================================
const toRoman = (n) => ['', 'I', 'II', 'III', 'IV', 'V'][n] || String(n);

// =============================================================================
// HELPER: Nivel maximo de enchant vanilla permitido segun stage del jugador.
//
// ProgressiveStages usa linear_progression=true, entonces:
//   servo_ch6 → 5  (sin restriccion, todo desbloqueado)
//   servo_ch5 → 4  (nivel IV max)
//   servo_ch3 → 3  (nivel III max)
//   ch1/ch2   → 2  (nivel II max, default)
// =============================================================================
const getNivelMax = (player) => {
    if (player.stages.has('servo_ch6')) return 5;
    if (player.stages.has('servo_ch5')) return 4;
    if (player.stages.has('servo_ch3')) return 3;
    return 2;
};

// =============================================================================
// HELPER: Capitulo requerido para desbloquear el siguiente nivel de enchant.
// Se muestra en el mensaje de notificacion al jugador.
//   nivelMax = 2 (Ch1/2) → necesita Ch3 para llegar a nivel III
//   nivelMax = 3 (Ch3/4) → necesita Ch5 para llegar a nivel IV
//   nivelMax = 4 (Ch5)   → necesita Ch6 para llegar a nivel V
// =============================================================================
const getCapRequerido = (nivelMax) => {
    if (nivelMax >= 4) return 6;  // nivel V requiere Ch6
    if (nivelMax >= 3) return 5;  // nivel IV requiere Ch5
    return 3;                      // nivel III requiere Ch3
};

// =============================================================================
// EVENTO PRINCIPAL: PlayerEvents.inventoryChanged
//
// Disparo: cada vez que un ItemStack entra al inventario del jugador.
// Cubre: enchanting table, anvil, loot, drops de mobs, /give, comandos.
// =============================================================================
PlayerEvents.inventoryChanged(event => {
    const player = event.player;

    // Bypass para modo creativo (consistente con allow_creative_bypass en PS)
    if (player.creative) return;

    const item = event.item;
    if (!item || item.empty) return;

    // Early exit si el item no tiene enchants
    if (item.enchantments.isEmpty()) return;

    const nivelMax = getNivelMax(player);
    const tieneCh5 = player.stages.has('servo_ch5');

    // Optimizacion: si el jugador tiene acceso completo, no procesar
    if (nivelMax >= 5 && tieneCh5) return;

    // =========================================================================
    // Recolectar enchants a remover
    // =========================================================================
    const aRemover = []; // { key: ResourceLocation, nivel: int, nombre: string }

    item.enchantments.forEach((nivel, key) => {
        // forEach en KubeJS 6: callback(level: int, key: ResourceLocation)
        const idStr = key.toString();

        // 1. Verificar Spell Power enchants (requieren Ch5 para cualquier nivel)
        if (!tieneCh5 && SPELL_POWER_ENCHANTS.has(idStr)) {
            aRemover.push({ key: key, nivel: nivel, nombre: idStr });
            return; // siguiente enchant
        }

        // 2. Verificar nivel maximo vanilla
        if (nivel > nivelMax) {
            aRemover.push({ key: key, nivel: nivel, nombre: idStr });
        }
    });

    if (aRemover.length === 0) return;

    // =========================================================================
    // Remover enchants prohibidos del item
    // En KubeJS 6, item.enchantments es mutable via el proxy del componente
    // =========================================================================
    aRemover.forEach(({ key }) => {
        item.enchantments.remove(key);
    });

    // =========================================================================
    // Notificar al jugador
    // =========================================================================
    const nombresFormateados = aRemover.map(({ nombre, nivel }) => {
        const corto = nombre.includes(':') ? nombre.split(':')[1] : nombre;
        return `${corto} ${toRoman(nivel)}`;
    }).join(', ');

    const capReq = getCapRequerido(nivelMax);

    player.tell(
        Text.of('[').gray()
            .append(Text.of('Servo').gold())
            .append('] ')
            .append(Text.of('Enchant removido: ').red())
            .append(Text.of(nombresFormateados).yellow())
            .append(Text.of(` — requiere Capitulo ${capReq}`).gray())
    );

    console.log(`[Servo] EnchantGating | ${player.username} | removido: ${nombresFormateados} | nivelMax: ${nivelMax}`);
});

// =============================================================================
// LOG DE INICIALIZACION
// =============================================================================
console.log('[Servo] Enchantment gating: cargado (Issue #89).');
console.log('[Servo]   Ch1-2: max nivel II  | Ch3-4: max nivel III');
console.log('[Servo]   Ch5:   max nivel IV + Spell Power desbloqueados');
console.log('[Servo]   Ch6+:  sin restriccion (nivel V)');
