// =============================================================================
// 02_freezer_recipes.js
// PROPOSITO: Agregar 7 recetas de helados/congelados al Freezer de
//            Refurbished Furniture (refurbished_furniture:freezer_solidifying).
//
// Contexto (issue #71):
//   Los helados de Croptopia y el palito de melon de FD tenian recetas en
//   crafting table que no tienen sentido tematico. Se redirigen al Freezer,
//   que si existe fisicamente en el mundo y pertenece al pilar de Cocina.
//
//   Las recetas de Croptopia ya fueron eliminadas en:
//     00_cleanup/remove_croptopia_recipes.js  (event.remove({ mod: 'croptopia' }))
//   La receta de FD melon_popsicle se elimina en este mismo script.
//
// Progresion:
//   El Freezer (Refurbished Furniture) se desbloquea en Ch3 junto con los
//   muebles funcionales de cocina. Los 5 helados de Ch3 usan ingredientes
//   disponibles desde ese capitulo. Vanilla Ice Cream usa croptopia:vanilla
//   (Ch4) y Kiwi Sorbet usa croptopia:kiwi (Ch5).
//
// Formato freezer_solidifying (verificado en docs/mod-data/refurbished_furniture.json):
//   {
//     type: 'refurbished_furniture:freezer_solidifying',
//     result: { item: '<id>' },
//     ingredients: [{ item: '<id>' }, ...]   // o { tag: '<tag>' }
//   }
//
// IDs verificados contra docs/mod-data/croptopia.json y farmersdelight.json.
// Tags Croptopia verificados: #c:mangos, #c:kiwis, #c:pecans, #c:vanilla,
//   #c:raisins, #c:rums (todos usados en recetas originales de CT).
// =============================================================================

ServerEvents.recipes(event => {

    // =========================================================================
    // ELIMINAR RECETA ORIGINAL: Melon Popsicle de FD
    // La receta original (crafting_shaped) usa melon_slice + ice + stick.
    // La reemplazamos por la version del Freezer de abajo.
    // =========================================================================
    event.remove({ output: 'farmersdelight:melon_popsicle' });


    // =========================================================================
    // FREEZER SOLIDIFYING — HELADOS Y CONGELADOS
    // Todos desbloquean en Ch3 (cuando el jugador obtiene el Freezer),
    // salvo donde se indica capitulo posterior por disponibilidad de crop.
    // =========================================================================

    // -------------------------------------------------------------------------
    // 1. Chocolate Ice Cream (Ch3)
    //    Ingredientes: leche (#c:milk) + cacao (minecraft:cocoa_beans) + azucar
    //    Origen: Croptopia (shaped, crafting table) — eliminado por 00_cleanup
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { tag: 'c:milk' },
            { item: 'minecraft:cocoa_beans' },
            { item: 'minecraft:sugar' }
        ],
        result: { item: 'croptopia:chocolate_ice_cream' }
    });

    // -------------------------------------------------------------------------
    // 2. Melon Popsicle (Ch3)
    //    Ingredientes: rebanada de melon + azucar + palo
    //    Origen: Farmer's Delight (shaped, crafting table) — eliminado arriba
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { item: 'minecraft:melon_slice' },
            { item: 'minecraft:sugar' },
            { item: 'minecraft:stick' }
        ],
        result: { item: 'farmersdelight:melon_popsicle' }
    });

    // -------------------------------------------------------------------------
    // 3. Pecan Ice Cream (Ch3)
    //    Ingredientes: nuez pecan (#c:pecans) + leche + azucar
    //    Origen: Croptopia (shapeless, crafting table) — eliminado por 00_cleanup
    //    NOTA: La receta original tambien pedia minecraft:egg, lo mantenemos.
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { tag: 'c:pecans' },
            { tag: 'c:milk' },
            { item: 'minecraft:sugar' },
            { item: 'minecraft:egg' }
        ],
        result: { item: 'croptopia:pecan_ice_cream' }
    });

    // -------------------------------------------------------------------------
    // 4. Rum Raisin Ice Cream (Ch3)
    //    Ingredientes: ron (#c:rums) + pasas (#c:raisins) + leche + azucar
    //    Origen: Croptopia (shapeless, crafting table) — eliminado por 00_cleanup
    //    NOTA: Ron (croptopia:rum) se obtiene de fermentacion en Ch3+.
    //          La receta original pedia minecraft:egg, lo mantenemos.
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { tag: 'c:rums' },
            { tag: 'c:raisins' },
            { tag: 'c:milk' },
            { item: 'minecraft:sugar' },
            { item: 'minecraft:egg' }
        ],
        result: { item: 'croptopia:rum_raisin_ice_cream' }
    });

    // -------------------------------------------------------------------------
    // 5. Mango Ice Cream (Ch3)
    //    Ingredientes: mango (#c:mangos) + leche + azucar
    //    Origen: Croptopia (shapeless, crafting table) — eliminado por 00_cleanup
    //    NOTA: La receta original pedia minecraft:egg, lo mantenemos.
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { tag: 'c:mangos' },
            { tag: 'c:milk' },
            { item: 'minecraft:sugar' },
            { item: 'minecraft:egg' }
        ],
        result: { item: 'croptopia:mango_ice_cream' }
    });

    // -------------------------------------------------------------------------
    // 6. Vanilla Ice Cream (Ch4)
    //    Ingredientes: vaina de vanilla (#c:vanilla) + leche + azucar
    //    Origen: Croptopia (shapeless, crafting table) — eliminado por 00_cleanup
    //    NOTA: croptopia:vanilla (vaina) se cultiva en Ch4+.
    //          La receta original pedia minecraft:egg, lo mantenemos.
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { tag: 'c:vanilla' },
            { tag: 'c:milk' },
            { item: 'minecraft:sugar' },
            { item: 'minecraft:egg' }
        ],
        result: { item: 'croptopia:vanilla_ice_cream' }
    });

    // -------------------------------------------------------------------------
    // 7. Kiwi Sorbet (Ch5)
    //    Ingredientes: kiwi (#c:kiwis) + miel (minecraft:honey_bottle)
    //    Origen: Croptopia (shaped, crafting table) — eliminado por 00_cleanup
    //    NOTA: croptopia:kiwi se desbloquea en Ch5 segun progresion de crops.
    //          La receta original solo usaba kiwi + honey_bottle; la respetamos.
    //          Sorbet no lleva leche — es base de agua/fruta congelada.
    // -------------------------------------------------------------------------
    event.custom({
        type: 'refurbished_furniture:freezer_solidifying',
        ingredients: [
            { tag: 'c:kiwis' },
            { item: 'minecraft:honey_bottle' }
        ],
        result: { item: 'croptopia:kiwi_sorbet' }
    });


    console.log('[Servo] 02_freezer_recipes: 7 recetas de congelados registradas en Freezer (refurbished_furniture).');
});
