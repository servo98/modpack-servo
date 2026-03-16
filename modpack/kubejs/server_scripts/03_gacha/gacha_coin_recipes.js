// PROPOSITO: Recetas de conversion Pepe Coins → Monedas de Gacha.
// Permite a los jugadores convertir su moneda principal del modpack en monedas
// especificas para cada maquina gacha, gateando el acceso a cada pool.
//
// Issue #86 - Gacha Coin Conversion Recipes
//
// Costos documentados en docs/mechanics/gacha.md (tabla "Las 4 maquinas"):
//   Muebles  (Rosa)   → 5  Pepe Coins    Ch2+
//   Basica   (Verde)  → 10 Pepe Coins    Ch1+
//   Avanzada (Azul)   → 10 Pepe Coins    Ch3+
//   Superior (Purpura)→ 15 Pepe Coins    Ch5+
//
// RESTRICCION DE GRID: La cuadricula de crafting vanilla es 3x3 (max 9 slots, 1 item por slot).
// No es posible representar literalmente 10 o 15 Pepe Coins en una receta vanilla.
//
// Aproximaciones adoptadas:
//   Rosa    (5x) → shapeless con 5 items. Coincide exactamente.
//   Verde  (10x) → shaped 3x3, los 9 slots son Pepe Coins. Ajuste: -1 moneda.
//   Azul   (10x) → shaped 3x3, los 9 slots son Pepe Coins. Ajuste: -1 moneda.
//   Purpura(15x) → shaped 3x3: 8 Pepe Coins + 1 Amethyst Shard (center).
//                  La amethyst shard: a) distingue el tier purpura del verde/azul,
//                  b) requiere Ch3+ (minado en geoda), c) cost total ~11 unidades
//                  de recursos, acercandose al premium de 15 sin necesitar grid extra.
//
// IDs verificados en docs/mod-data/gachamachine.json:
//   gachamachine:gacha_coin_5 (Green)
//   gachamachine:gacha_coin_6 (Blue)
//   gachamachine:gacha_coin_7 (Purple)
//   gachamachine:gacha_coin_8 (Pink)
//
// servo_core:pepe_coin registrado en servo_core Java (ModRegistry.java).
// Si servo_core no esta cargado, KubeJS ignora silenciosamente las recetas
// con items no encontrados (log en run/logs/kubejs/server.log).

ServerEvents.recipes(event => {

    // =========================================================================
    // GACHA MUEBLES (Rosa) — Ch2+
    // 5x Pepe Coins → 1x Pink Gacha Coin
    // Costo exacto del doc. Shoppers vibe, precio mas bajo, pool de muebles y plushies.
    // =========================================================================
    event.shapeless('gachamachine:gacha_coin_8', [
        'servo_core:pepe_coin',
        'servo_core:pepe_coin',
        'servo_core:pepe_coin',
        'servo_core:pepe_coin',
        'servo_core:pepe_coin'
    ]).id('servo_core:gacha_coin_pink');

    // =========================================================================
    // GACHA BASICA (Verde) — Ch1+
    // 9x Pepe Coins → 1x Green Gacha Coin  (doc dice 10, ajuste por grid 3x3)
    // Primer gacha disponible. Materiales T1-T2, gemas basicas, runas.
    // =========================================================================
    event.shaped('gachamachine:gacha_coin_5', [
        'P P P',
        'P P P',
        'P P P'
    ], {
        P: 'servo_core:pepe_coin'
    }).id('servo_core:gacha_coin_green');

    // =========================================================================
    // GACHA AVANZADA (Azul) — Ch3+
    // 9x Pepe Coins → 1x Blue Gacha Coin  (doc dice 10, ajuste por grid 3x3)
    // Mismo costo base que Verde. El gate real es el stage Ch3 via FTB Quests
    // (la maquina azul se entrega como quest reward al desbloquear clases magicas).
    // Pool superior: RPG gear T2, jewelry, gemas raras.
    // =========================================================================
    event.shaped('gachamachine:gacha_coin_6', [
        'P P P',
        'P P P',
        'P P P'
    ], {
        P: 'servo_core:pepe_coin'
    }).id('servo_core:gacha_coin_blue');

    // =========================================================================
    // GACHA SUPERIOR (Purpura) — Ch5+
    // 8x Pepe Coins + 1x Amethyst Shard → 1x Purple Gacha Coin
    // Doc dice 15 Pepe Coins. Patron alternativo para marcar el tier premium:
    //   P P P
    //   P A P     A = minecraft:amethyst_shard (geodas — Ch3+ gate)
    //   P P P     P = servo_core:pepe_coin (x8)
    //
    // La amethyst shard diferencia visualmente esta moneda de las anteriores,
    // senala el capitulo minimo (Ch3 para geodas, Ch5 para la maquina), y
    // eleva el costo efectivo de recursos. Pool endgame: netherite, unique jewelry.
    // =========================================================================
    event.shaped('gachamachine:gacha_coin_7', [
        'P P P',
        'P A P',
        'P P P'
    ], {
        P: 'servo_core:pepe_coin',
        A: 'minecraft:amethyst_shard'
    }).id('servo_core:gacha_coin_purple');

    console.log('[Servo] Gacha: registradas 4 recetas de conversion Pepe Coins → Gacha Coins');
    console.log('[Servo] Gacha:   Pink  (5x pepe_coin  → gacha_coin_8)');
    console.log('[Servo] Gacha:   Verde (9x pepe_coin  → gacha_coin_5)');
    console.log('[Servo] Gacha:   Azul  (9x pepe_coin  → gacha_coin_6)');
    console.log('[Servo] Gacha:   Purpura (8x pepe_coin + 1x amethyst_shard → gacha_coin_7)');
});
