// =============================================================================
// 02_hide_gacha_unused.js  (client_scripts)
// PROPOSITO: Ocultar de EMI las maquinas gacha, monedas y capsulas del mod
//   Bloo's Gacha Machine que NO se usan en el modpack Servo.
//
// El modpack usa SOLO 4 maquinas de las 10 disponibles:
//   Maquina 5 (Verde)   — Gacha Basica, disponible Ch1+
//   Maquina 6 (Azul)    — Gacha Avanzada, disponible Ch3+
//   Maquina 7 (Purpura) — Gacha Superior, disponible Ch5+
//   Maquina 8 (Rosa)    — Gacha de Muebles, disponible Ch2+
//
// Las 6 maquinas restantes (default, _2 roja, _3 naranja, _4 amarilla,
// _9 negra, _10 blanca), sus monedas y sus capsulas se ocultan para evitar
// confusion al jugador. Esos items siguen existiendo en el juego; solo se
// eliminan del buscador de recetas de EMI.
//
// IMPORTANTE: Las 4 maquinas activas NO se ocultan. El jugador debe verlas
// en EMI con sus recetas de monedas (ver server_scripts/03_gacha/).
//
// Referencia de IDs verificados en docs/mod-data/gachamachine.json
// Referencia de diseno: docs/mechanics/gacha.md (seccion "Mapping de IDs")
//
// API: ClientEvents.hideItemsFromViewers (KubeJS 6, NeoForge 1.21.1)
//   Esta API es respetada por EMI, JEI y REI automaticamente.
// =============================================================================

ClientEvents.hideItemsFromViewers(event => {

    // =========================================================================
    // SECCION 1: MAQUINAS NO USADAS
    // Las maquinas se obtienen como quest rewards — no son crafteables por el
    // jugador. Las 6 que no se usan no deben aparecer en EMI.
    //
    // VISIBLES (NO ocultar):
    //   gachamachine:gacha_machine_5  (Verde)   — Gacha Basica   Ch1+
    //   gachamachine:gacha_machine_6  (Azul)    — Gacha Avanzada Ch3+
    //   gachamachine:gacha_machine_7  (Purpura) — Gacha Superior Ch5+
    //   gachamachine:gacha_machine_8  (Rosa)    — Gacha Muebles  Ch2+
    // =========================================================================

    // Maquina default (sin color, ID base del mod)
    event.hide('gachamachine:gacha_machine');

    // Maquina Roja (_2)
    event.hide('gachamachine:gacha_machine_2');

    // Maquina Naranja (_3)
    event.hide('gachamachine:gacha_machine_3');

    // Maquina Amarilla (_4)
    event.hide('gachamachine:gacha_machine_4');

    // Maquina Negra (_9)
    event.hide('gachamachine:gacha_machine_9');

    // Maquina Blanca (_10)
    event.hide('gachamachine:gacha_machine_10');

    // =========================================================================
    // SECCION 2: MONEDAS NO USADAS
    // Solo se usan las 4 monedas de las maquinas activas. Las demas se ocultan.
    //
    // VISIBLES (NO ocultar):
    //   gachamachine:gacha_coin_5  (Verde)   — coin de Gacha Basica
    //   gachamachine:gacha_coin_6  (Azul)    — coin de Gacha Avanzada
    //   gachamachine:gacha_coin_7  (Purpura) — coin de Gacha Superior
    //   gachamachine:gacha_coin_8  (Rosa)    — coin de Gacha Muebles
    // =========================================================================

    // Moneda default (sin color, crafteable con Emerald en vanilla del mod)
    event.hide('gachamachine:gacha_coin');

    // Moneda Roja (_2)
    event.hide('gachamachine:gacha_coin_2');

    // Moneda Naranja (_3)
    event.hide('gachamachine:gacha_coin_3');

    // Moneda Amarilla (_4)
    event.hide('gachamachine:gacha_coin_4');

    // Moneda Blanca (_9)
    event.hide('gachamachine:gacha_coin_9');

    // Moneda Negra (_10)
    event.hide('gachamachine:gacha_coin_10');

    // =========================================================================
    // SECCION 3: CAPSULAS NO USADAS
    // Las capsulas son los contenedores de loot de cada maquina.
    // Solo se usan las 4 series activas:
    //   Serie E (e1-e10, Verde)   — capsulas de Gacha Basica
    //   Serie F (f1-f10, Azul)    — capsulas de Gacha Avanzada
    //   Serie G (g1-g10, Purpura) — capsulas de Gacha Superior
    //   Serie H (h1-h10, Rosa)    — capsulas de Gacha Muebles
    //
    // VISIBLES (NO ocultar): capsule_e1..e10, capsule_f1..f10,
    //                         capsule_g1..g10, capsule_h1..h10
    // =========================================================================

    // --- Serie A (default / Wood→Netherite, 10 capsulas) ---
    event.hide('gachamachine:capsule_a1');
    event.hide('gachamachine:capsule_a2');
    event.hide('gachamachine:capsule_a3');
    event.hide('gachamachine:capsule_a4');
    event.hide('gachamachine:capsule_a5');
    event.hide('gachamachine:capsule_a6');
    event.hide('gachamachine:capsule_a7');
    event.hide('gachamachine:capsule_a8');
    event.hide('gachamachine:capsule_a9');
    event.hide('gachamachine:capsule_a10');

    // --- Serie B (Roja, 10 capsulas) ---
    event.hide('gachamachine:capsule_b1');
    event.hide('gachamachine:capsule_b2');
    event.hide('gachamachine:capsule_b3');
    event.hide('gachamachine:capsule_b4');
    event.hide('gachamachine:capsule_b5');
    event.hide('gachamachine:capsule_b6');
    event.hide('gachamachine:capsule_b7');
    event.hide('gachamachine:capsule_b8');
    event.hide('gachamachine:capsule_b9');
    event.hide('gachamachine:capsule_b10');

    // --- Serie C (Naranja, 10 capsulas) ---
    event.hide('gachamachine:capsule_c1');
    event.hide('gachamachine:capsule_c2');
    event.hide('gachamachine:capsule_c3');
    event.hide('gachamachine:capsule_c4');
    event.hide('gachamachine:capsule_c5');
    event.hide('gachamachine:capsule_c6');
    event.hide('gachamachine:capsule_c7');
    event.hide('gachamachine:capsule_c8');
    event.hide('gachamachine:capsule_c9');
    event.hide('gachamachine:capsule_c10');

    // --- Serie D (Amarilla, 10 capsulas) ---
    event.hide('gachamachine:capsule_d1');
    event.hide('gachamachine:capsule_d2');
    event.hide('gachamachine:capsule_d3');
    event.hide('gachamachine:capsule_d4');
    event.hide('gachamachine:capsule_d5');
    event.hide('gachamachine:capsule_d6');
    event.hide('gachamachine:capsule_d7');
    event.hide('gachamachine:capsule_d8');
    event.hide('gachamachine:capsule_d9');
    event.hide('gachamachine:capsule_d10');

    // --- Serie I (Blanca, 10 capsulas) ---
    event.hide('gachamachine:capsule_i1');
    event.hide('gachamachine:capsule_i2');
    event.hide('gachamachine:capsule_i3');
    event.hide('gachamachine:capsule_i4');
    event.hide('gachamachine:capsule_i5');
    event.hide('gachamachine:capsule_i6');
    event.hide('gachamachine:capsule_i7');
    event.hide('gachamachine:capsule_i8');
    event.hide('gachamachine:capsule_i9');
    event.hide('gachamachine:capsule_i10');

    // --- Serie J (Negra, 10 capsulas) ---
    event.hide('gachamachine:capsule_j1');
    event.hide('gachamachine:capsule_j2');
    event.hide('gachamachine:capsule_j3');
    event.hide('gachamachine:capsule_j4');
    event.hide('gachamachine:capsule_j5');
    event.hide('gachamachine:capsule_j6');
    event.hide('gachamachine:capsule_j7');
    event.hide('gachamachine:capsule_j8');
    event.hide('gachamachine:capsule_j9');
    event.hide('gachamachine:capsule_j10');

    console.log('[Servo] 02_hide_gacha_unused: 6 maquinas, 6 monedas y 60 capsulas no usadas ocultas de EMI.');
    console.log('[Servo] 02_hide_gacha_unused: Visibles: maquinas _5/_6/_7/_8, coins _5/_6/_7/_8, capsulas e/f/g/h (x10).');
});
