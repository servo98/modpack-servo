// PROPOSITO: Crear recetas custom para items de Tier 4 de los mods RPG Series.
// Problema: Los items T4 (Aeternium, Ruby, Crystal/Smaragdant) usan materiales de
//   BetterEnd (Aeternium Ingot) y BetterNether (Nether Ruby) — mods NO instalados.
//   Sin recetas validas, estos items son completamente inaccesibles.
// Solucion: Quitar las recetas originales rotas y agregar recetas custom con
//   materiales de dungeon del modpack (Ch7-8).
//
// Issue #50 - RPG T4 Custom Recipes
// Tier 4 = Ch7-8, gate: boss drops de dungeon
//
// Patron de receta (upgrade del T3):
//   F C F
//   C T C     F = servo_core:core_crystal_fragment
//   N E N     C = servo_core:dungeon_essence
//             T = pieza T3 (el item que se mejora)
//             N = minecraft:netherite_ingot
//             E = minecraft:netherite_ingot
//
// Materiales verificados contra docs/mod-data/:
//   rogues.json  : aeternium_*, ruby_* en rogues:
//   paladins.json: aeternium_*, ruby_* en paladins:
//   wizards.json : staff_crystal_arcane, staff_ruby_fire, staff_smaragdant_frost en wizards:
//
// NOTA: Los items servo_core:core_crystal_fragment y servo_core:dungeon_essence son
//   items custom del mod servo_core (aun en desarrollo). Seran drops de bosses de
//   dungeon Ch6-7. Las recetas ya pueden existir en el script para cuando el mod
//   este disponible. Si el mod no esta cargado, KubeJS ignora silenciosamente
//   las recetas con items no encontrados (log en kubejs/server.log).

ServerEvents.recipes(event => {

    // =========================================================================
    // HELPER: crea receta T3->T4 con patron de upgrade
    // t3Item  : ID del item T3 (va al centro)
    // t4Item  : ID del resultado T4
    // recipeId: sufijo para el namespace servo_core:
    // =========================================================================
    const agregarRecetaT4 = (t3Item, t4Item, recipeId) => {
        event.shaped(t4Item, [
            'F C F',
            'C T C',
            'N E N'
        ], {
            F: 'servo_core:core_crystal_fragment',
            C: 'servo_core:dungeon_essence',
            T: t3Item,
            N: 'minecraft:netherite_ingot',
            E: 'minecraft:netherite_ingot'
        }).id(`servo_core:t4_${recipeId}`);
    };

    // =========================================================================
    // PASO 1: Quitar recetas originales rotas de los 3 mods RPG
    // Quitamos por output para ser precisos. Las recetas de smithing_transform
    // que usan betternether:ruby o betterend:aeternium como template/addition
    // tambien se eliminan al filtrar por output.
    // =========================================================================

    // --- MOD: rogues ---
    const itemsT4Rogues = [
        'rogues:aeternium_dagger',
        'rogues:aeternium_double_axe',
        'rogues:aeternium_glaive',
        'rogues:aeternium_sickle',
        'rogues:ruby_dagger',
        'rogues:ruby_double_axe',
        'rogues:ruby_glaive',
        'rogues:ruby_sickle',
    ];

    // --- MOD: paladins ---
    const itemsT4Paladins = [
        'paladins:aeternium_claymore',
        'paladins:aeternium_great_hammer',
        'paladins:aeternium_kite_shield',
        'paladins:aeternium_mace',
        'paladins:ruby_claymore',
        'paladins:ruby_great_hammer',
        'paladins:ruby_kite_shield',
        'paladins:ruby_mace',
        'paladins:ruby_holy_staff',
    ];

    // --- MOD: wizards ---
    // staff_crystal_arcane = variante T4 Arcane (usa Aeternium de BetterEnd)
    // staff_ruby_fire      = variante T4 Fire (usa Ruby de BetterNether)
    // staff_smaragdant_frost = variante T4 Frost (usa Smaragdant de BetterEnd)
    const itemsT4Wizards = [
        'wizards:staff_crystal_arcane',
        'wizards:staff_ruby_fire',
        'wizards:staff_smaragdant_frost',
    ];

    const todosLosT4 = [...itemsT4Rogues, ...itemsT4Paladins, ...itemsT4Wizards];

    todosLosT4.forEach(item => {
        event.remove({ output: item });
    });

    console.log(`[Servo] RPG T4: eliminadas ${todosLosT4.length} recetas originales rotas`);

    // =========================================================================
    // PASO 2: Agregar recetas custom con materiales de dungeon
    // T3 verificado en rpg-weapon-stats.md y jsons de mods
    // =========================================================================

    // --- ROGUES: armas T4 ---
    // T3 = netherite variants (smithing_transform vanilla, esas SÍ funcionan)
    agregarRecetaT4('rogues:netherite_dagger',     'rogues:aeternium_dagger',     'rogue_dagger');
    agregarRecetaT4('rogues:netherite_double_axe', 'rogues:aeternium_double_axe', 'rogue_double_axe');
    agregarRecetaT4('rogues:netherite_glaive',     'rogues:aeternium_glaive',     'rogue_glaive');
    agregarRecetaT4('rogues:netherite_sickle',     'rogues:aeternium_sickle',     'rogue_sickle');

    // Ruby variants de rogues (se craftean igual que aeternium desde T3)
    // En el lore del modpack: Ruby = drop Ch6, Aeternium = drop Ch7
    // Aqui las igualamos en receta — la diferencia de poder la da el item mismo
    agregarRecetaT4('rogues:netherite_dagger',     'rogues:ruby_dagger',     'rogue_ruby_dagger');
    agregarRecetaT4('rogues:netherite_double_axe', 'rogues:ruby_double_axe', 'rogue_ruby_double_axe');
    agregarRecetaT4('rogues:netherite_glaive',     'rogues:ruby_glaive',     'rogue_ruby_glaive');
    agregarRecetaT4('rogues:netherite_sickle',     'rogues:ruby_sickle',     'rogue_ruby_sickle');

    // --- PALADINS: armas T4 ---
    agregarRecetaT4('paladins:netherite_claymore',    'paladins:aeternium_claymore',    'paladin_claymore');
    agregarRecetaT4('paladins:netherite_great_hammer','paladins:aeternium_great_hammer','paladin_great_hammer');
    agregarRecetaT4('paladins:netherite_kite_shield', 'paladins:aeternium_kite_shield', 'paladin_kite_shield');
    agregarRecetaT4('paladins:netherite_mace',        'paladins:aeternium_mace',        'paladin_mace');

    agregarRecetaT4('paladins:netherite_claymore',    'paladins:ruby_claymore',    'paladin_ruby_claymore');
    agregarRecetaT4('paladins:netherite_great_hammer','paladins:ruby_great_hammer','paladin_ruby_great_hammer');
    agregarRecetaT4('paladins:netherite_kite_shield', 'paladins:ruby_kite_shield', 'paladin_ruby_kite_shield');
    agregarRecetaT4('paladins:netherite_mace',        'paladins:ruby_mace',        'paladin_ruby_mace');
    // ruby_holy_staff: su T3 es netherite_holy_staff
    agregarRecetaT4('paladins:netherite_holy_staff',  'paladins:ruby_holy_staff',  'paladin_ruby_holy_staff');

    // --- WIZARDS: staves T4 ---
    // staff_crystal_arcane = Arcane T4 (BetterEnd Crystal). T3 = staff_netherite_arcane
    agregarRecetaT4('wizards:staff_netherite_arcane', 'wizards:staff_crystal_arcane',  'wizard_staff_arcane');
    // staff_ruby_fire = Fire T4 (BetterNether Ruby). T3 = staff_netherite_fire
    agregarRecetaT4('wizards:staff_netherite_fire',   'wizards:staff_ruby_fire',        'wizard_staff_fire');
    // staff_smaragdant_frost = Frost T4 (BetterEnd Smaragdant). T3 = staff_netherite_frost
    agregarRecetaT4('wizards:staff_netherite_frost',  'wizards:staff_smaragdant_frost', 'wizard_staff_frost');

    console.log('[Servo] RPG T4: agregadas 20 recetas custom con materiales de dungeon');
    console.log('[Servo] RPG T4: requiere servo_core:core_crystal_fragment y servo_core:dungeon_essence');
});
