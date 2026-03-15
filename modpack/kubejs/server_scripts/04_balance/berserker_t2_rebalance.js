// PROPOSITO: Rebalancear la armadura Berserker (T2, mod rogues).
// Problema original: la receta usa netherite_scrap, que es un material de T3 (Nether).
//   Esto bloquea la clase Warrior/Berserker en Ch4-5 de forma injusta.
// Solucion: reemplazar netherite_scrap por iron_ingot en todas las piezas de Berserker.
//   La armadura sigue requiriendo chain (que ya tenia) mas iron, coherente con su tier.
//
// Issue #17 - RPG Balance: Berserker T2 Recipe
// Tier 2 = Ch4-5, materiales: Diamond + Chain + Iron (sin Nether todavia)
//
// IDs verificados contra docs/mod-data/rogues.json:
//   rogues:berserker_armor_head
//   rogues:berserker_armor_chest
//   rogues:berserker_armor_legs
//   rogues:berserker_armor_feet

ServerEvents.recipes(event => {

    // Reemplazar netherite_scrap por iron_ingot en TODAS las recetas del mod rogues
    // que afecten a piezas de Berserker. Usar filtro por output para ser preciso
    // y no tocar otras recetas del mod.
    const piezasBerserker = [
        'rogues:berserker_armor_head',
        'rogues:berserker_armor_chest',
        'rogues:berserker_armor_legs',
        'rogues:berserker_armor_feet',
    ];

    piezasBerserker.forEach(pieza => {
        event.replaceInput(
            { output: pieza },          // filtro: solo recetas que produzcan esta pieza
            'minecraft:netherite_scrap', // ingrediente a reemplazar
            'minecraft:iron_ingot'       // reemplazo: iron_ingot (disponible en Ch2+)
        );
    });

    console.log('[Servo] Berserker T2: netherite_scrap -> iron_ingot en 4 piezas de armadura');
});
