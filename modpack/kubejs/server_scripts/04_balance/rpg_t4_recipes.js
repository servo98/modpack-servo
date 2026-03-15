// PROPOSITO: Crear recetas custom para items de Tier 4 de los mods RPG Series.
// Problema: Los items T4 de los 3 mods usan materiales de mods NO instalados:
//   - rogues/paladins "aeternium_*" → betterend:aeternium_ingot (BetterEnd, no instalado)
//   - rogues/paladins/wizards "ruby_*" → betternether:nether_ruby (BetterNether, no instalado)
//   - wizards "staff_crystal_arcane", "staff_smaragdant_frost" → betterend materials
//   - rogues/paladins/wizards "aether_*" → items loot-only del mod Aether (no instalado)
//   Sin recetas validas, todos estos items son completamente inaccesibles.
// Solucion: Quitar las recetas originales rotas y agregar recetas custom con
//   materiales de dungeon del modpack (Ch7-8).
//
// Issue #61 - RPG T4 Custom Recipes (B014)
// Tier 4 = Ch7-8, gate: boss drops de dungeon
//
// Patron de receta (upgrade del T3):
//   F C F
//   C T C     F = servo_core:core_crystal_fragment  (drop boss Ch7, 2 unidades)
//   N E N     C = servo_core:dungeon_essence         (drop boss Ch6, 4 unidades)
//             T = pieza T3 (el item que se mejora)
//             N = minecraft:netherite_ingot           (2 unidades)
//             E = minecraft:echo_shard                (1 unidad, Deep Dark - Ch7 gate)
//
// Costo total por upgrade: 2x crystal_fragment + 4x dungeon_essence + 2x netherite + 1x echo_shard
// Esto asegura gate apropiado: echo_shard requiere Ancient City (late Ch6 / early Ch7)
//
// Materiales verificados contra docs/mod-data/:
//   rogues.json  : aeternium_*, ruby_*, aether_* en rogues:
//   paladins.json: aeternium_*, ruby_*, aether_* en paladins:
//   wizards.json : staff_crystal_arcane, staff_ruby_fire, staff_smaragdant_frost, aether_wizard_staff en wizards:
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
    //
    // Patron:  F C F     F = core_crystal_fragment (drop boss Ch7)
    //          C T C     C = dungeon_essence (drop boss Ch6)
    //          N E N     T = item T3 (upgrade base)
    //                    N = netherite_ingot
    //                    E = echo_shard (Ancient City gate)
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
            E: 'minecraft:echo_shard'
        }).id(`servo_core:t4_${recipeId}`);
    };

    // =========================================================================
    // PASO 1: Quitar recetas originales rotas de los 3 mods RPG
    // Quitamos por output para ser precisos. Las recetas de smithing_transform
    // que usan betternether:ruby o betterend:aeternium como template/addition
    // tambien se eliminan al filtrar por output.
    // =========================================================================

    // --- MOD: rogues ---
    // aeternium_* → dep. BetterEnd (no instalado), recetas crafting_shaped rotas
    // ruby_*      → dep. BetterNether (no instalado), recetas smithing_transform rotas
    // aether_*    → loot-only del mod Aether (no instalado), sin receta original
    const itemsT4Rogues = [
        'rogues:aeternium_dagger',
        'rogues:aeternium_double_axe',
        'rogues:aeternium_glaive',
        'rogues:aeternium_sickle',
        'rogues:ruby_dagger',
        'rogues:ruby_double_axe',
        'rogues:ruby_glaive',
        'rogues:ruby_sickle',
        // Aether variants (Valkyrie loot — mod Aether no instalado)
        'rogues:aether_dagger',
        'rogues:aether_double_axe',
        'rogues:aether_glaive',
        'rogues:aether_sickle',
    ];

    // --- MOD: paladins ---
    // aeternium_* → dep. BetterEnd (no instalado), recetas crafting_shaped rotas
    // ruby_*      → dep. BetterNether (no instalado), recetas smithing_transform rotas
    //   (ruby_holy_staff tiene receta crafting_shaped, no smithing)
    // aether_*    → loot-only del mod Aether (no instalado), sin receta original
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
        // Aether variants (Valkyrie loot — mod Aether no instalado)
        'paladins:aether_claymore',
        'paladins:aether_great_hammer',
        'paladins:aether_kite_shield',
        'paladins:aether_mace',
        'paladins:aether_holy_staff',
    ];

    // --- MOD: wizards ---
    // staff_crystal_arcane    → dep. BetterEnd Aeternium, receta crafting_shaped rota
    // staff_ruby_fire         → dep. BetterNether Ruby, receta crafting_shaped rota
    // staff_smaragdant_frost  → dep. BetterEnd Smaragdant, receta crafting_shaped rota
    // aether_wizard_staff     → loot-only mod Aether (no instalado), sin receta original
    const itemsT4Wizards = [
        'wizards:staff_crystal_arcane',
        'wizards:staff_ruby_fire',
        'wizards:staff_smaragdant_frost',
        'wizards:aether_wizard_staff',
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

    // Aether variants de rogues (Valkyrie tier — equivalente a aeternium en poder)
    // T3 base: mismos items netherite, mismos materiales de upgrade
    agregarRecetaT4('rogues:netherite_dagger',     'rogues:aether_dagger',     'rogue_aether_dagger');
    agregarRecetaT4('rogues:netherite_double_axe', 'rogues:aether_double_axe', 'rogue_aether_double_axe');
    agregarRecetaT4('rogues:netherite_glaive',     'rogues:aether_glaive',     'rogue_aether_glaive');
    agregarRecetaT4('rogues:netherite_sickle',     'rogues:aether_sickle',     'rogue_aether_sickle');

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

    // Aether variants de paladins (Valkyrie / Holy tier)
    agregarRecetaT4('paladins:netherite_claymore',    'paladins:aether_claymore',    'paladin_aether_claymore');
    agregarRecetaT4('paladins:netherite_great_hammer','paladins:aether_great_hammer','paladin_aether_great_hammer');
    agregarRecetaT4('paladins:netherite_kite_shield', 'paladins:aether_kite_shield', 'paladin_aether_kite_shield');
    agregarRecetaT4('paladins:netherite_mace',        'paladins:aether_mace',        'paladin_aether_mace');
    // aether_holy_staff: su T3 es netherite_holy_staff
    agregarRecetaT4('paladins:netherite_holy_staff',  'paladins:aether_holy_staff',  'paladin_aether_holy_staff');

    // --- WIZARDS: staves T4 ---
    // staff_crystal_arcane = Arcane T4 (BetterEnd Crystal). T3 = staff_netherite_arcane
    agregarRecetaT4('wizards:staff_netherite_arcane', 'wizards:staff_crystal_arcane',    'wizard_staff_arcane');
    // staff_ruby_fire = Fire T4 (BetterNether Ruby). T3 = staff_netherite_fire
    agregarRecetaT4('wizards:staff_netherite_fire',   'wizards:staff_ruby_fire',          'wizard_staff_fire');
    // staff_smaragdant_frost = Frost T4 (BetterEnd Smaragdant). T3 = staff_netherite_frost
    agregarRecetaT4('wizards:staff_netherite_frost',  'wizards:staff_smaragdant_frost',   'wizard_staff_frost');
    // aether_wizard_staff = Aether/Valkyrie staff (unico, cubre los 3 schools)
    agregarRecetaT4('wizards:staff_netherite_arcane', 'wizards:aether_wizard_staff',      'wizard_staff_aether');

    // Conteo de recetas agregadas:
    //   Rogues:   4 aeternium + 4 ruby + 4 aether = 12
    //   Paladins: 4 aeternium + 5 ruby + 5 aether = 14
    //   Wizards:  4 staves (crystal + ruby + smaragdant + aether) = 4
    //   TOTAL: 30 recetas T4 custom
    console.log('[Servo] RPG T4: eliminadas recetas originales rotas (BetterEnd/BetterNether/Aether)');
    console.log('[Servo] RPG T4: agregadas 30 recetas custom con materiales de dungeon');
    console.log('[Servo] RPG T4: requiere servo_core:core_crystal_fragment, servo_core:dungeon_essence, minecraft:echo_shard');
});
