// =============================================================================
// 01_hide_duplicates.js  (client_scripts)
// PROPOSITO: Ocultar de EMI los items que estan duplicados entre mods de cocina,
//            para que el jugador vea una sola version de cada ingrediente.
//
// Estrategia de canonizacion:
//   - Si FD y Croptopia tienen el mismo crop → usamos FD (es mas conocido y tiene mas recetas)
//   - Si ED y Croptopia tienen el mismo ingrediente → usamos ED
//   - Los items de comida final de Croptopia se ocultan (sus recetas ya fueron eliminadas
//     en server_scripts/00_cleanup/remove_croptopia_recipes.js)
//
// API: ClientEvents.hideItemsFromViewers (KubeJS 6, NeoForge 1.21.1)
//   Esta API es respetada por EMI, JEI y REI automaticamente.
//   No requiere addon adicional.
//
// IMPORTANTE: Ocultar un item de EMI NO lo elimina del juego.
//   Sigue siendo crafteable/obtenible y funciona en recetas via tags.
//   Solo desaparece del buscador de recetas para reducir confusion visual.
//
// IDs verificados contra docs/mod-data/croptopia.json
// =============================================================================

ClientEvents.hideItemsFromViewers(event => {

    // =========================================================================
    // INGREDIENTES CRUDOS: Croptopia vs Farmer's Delight
    // Usamos la version de FD como canonica — tiene mas integracion con
    // el Cooking Pot, Cutting Board y Skillet de FD.
    // =========================================================================

    // Cebolla — usar farmersdelight:onion
    event.hide('croptopia:onion');
    // Green onion no tiene equivalente exacto en FD pero esta en c:onion,
    // lo dejamos visible para que el jugador sepa que existe y puede usarla.
    // event.hide('croptopia:greenonion'); // VISIBLE — ingrediente unico

    // Tomate — usar farmersdelight:tomato
    event.hide('croptopia:tomato');

    // Repollo — usar farmersdelight:cabbage
    event.hide('croptopia:cabbage');

    // Arroz crudo — usar farmersdelight:rice
    event.hide('croptopia:rice');

    // Leche en botella — usar farmersdelight:milk_bottle
    event.hide('croptopia:milk_bottle');

    // =========================================================================
    // INGREDIENTES CRUDOS: Croptopia vs Expanded Delight
    // Usamos la version de ED como canonica — tiene recetas propias en el modpack.
    // =========================================================================

    // Camote crudo — usar expandeddelight:sweet_potato
    // NOTA: croptopia tiene 'sweetpotato' (sin guion bajo) segun el GDD.
    //       Verificado en croptopia.json como item.croptopia.sweetpotato.
    event.hide('croptopia:sweetpotato');

    // Esparagos — usar expandeddelight:asparagus
    event.hide('croptopia:asparagus');

    // Chile / chili pepper — usar expandeddelight:chili_pepper
    // Ocultamos el chile de CT pero dejamos visibles pepper y bellpepper
    // ya que no tienen equivalente exacto en ED.
    event.hide('croptopia:chile_pepper');

    // Canela (especia) — usar expandeddelight:cinnamon
    event.hide('croptopia:cinnamon');

    // Limon — usar expandeddelight:lemon
    event.hide('croptopia:lemon');

    // Sal — usar expandeddelight:salt
    event.hide('croptopia:salt');
    // Mountain salt es una variante diferente (bloque y drop de mina), lo dejamos visible.
    // event.hide('croptopia:mountain_salt'); // VISIBLE — variante de drop distinta

    // Arandanos — usar expandeddelight:cranberries
    // Croptopia tiene 'cranberry' (singular).
    event.hide('croptopia:cranberry');

    // Cacahuate — usar expandeddelight:peanut
    event.hide('croptopia:peanut');

    // Mantequilla de cacahuate — usar expandeddelight:peanut_butter
    // (La receta de CT fue eliminada; la de ED sigue existiendo en Cooking Pot)
    event.hide('croptopia:peanut_butter');

    // =========================================================================
    // COMIDAS FINALES DUPLICADAS DE CROPTOPIA
    // Sus recetas fueron eliminadas en 00_cleanup/remove_croptopia_recipes.js.
    // Ocultamos el item resultado para que no aparezca en EMI sin receta.
    // En cada caso indicamos cual es el equivalente que SI tiene receta.
    // =========================================================================

    // Hamburguesa → usar farmersdelight:hamburger (Cutting Board: beef_patty + bun)
    event.hide('croptopia:hamburger');

    // Estofado de res → usar farmersdelight:beef_stew (Cooking Pot de FD)
    event.hide('croptopia:beef_stew');

    // Sopa de calabaza → usar farmersdelight:pumpkin_soup (Cooking Pot de FD)
    // NOTA: FD llama al item 'pumpkin_soup' — verificado en farmersdelight.json
    event.hide('croptopia:pumpkin_soup');

    // Ratatouille → usar farmersdelight:ratatouille (Cooking Pot de FD)
    event.hide('croptopia:ratatouille');

    // Ensalada de fruta → farmersdelight:fruit_salad (crafting o Cooking Pot de FD)
    event.hide('croptopia:fruit_salad');

    // Cerveza → usar brewinandchewin:beer (Keg de BAC, fermentacion)
    event.hide('croptopia:beer');

    // Aguamiel → usar brewinandchewin:mead (Keg de BAC, fermentacion con miel)
    event.hide('croptopia:mead');

    // Jugo de manzana → usar expandeddelight:apple_juice (Juicer de ED)
    event.hide('croptopia:apple_juice');

    // Jugo de arandano → usar expandeddelight:cranberry_juice (Juicer de ED)
    event.hide('croptopia:cranberry_juice');

    // Sandwich de queso fundido → usar expandeddelight:grilled_cheese (campfire cooking de ED)
    event.hide('croptopia:grilled_cheese');

    // Jugo de melon → farmersdelight:melon_juice (FD, Cooking Pot o crafting)
    event.hide('croptopia:melon_juice');

    // Tocino cocinado → usar farmersdelight:cooked_bacon (FD, horno/ahumador/fogata)
    event.hide('croptopia:cooked_bacon');

    // Camote asado → usar expandeddelight:baked_sweet_potato (ED, horno/ahumador/fogata)
    event.hide('croptopia:baked_sweet_potato');

    // Mantequilla de cacahuate (ya listada arriba en ingredientes, la duplicamos
    // aqui como comida final para claridad conceptual — hide es idempotente)
    // event.hide('croptopia:peanut_butter'); // ya se hizo arriba

    // =========================================================================
    // DERIVADOS DE CROPTOPIA QUE PIERDEN UTILIDAD
    // Items que usan ingredientes ahora ocultos como input principal.
    // Se ocultan para no confundir, pero sus IDs siguen siendo validos en recetas
    // que usen tags (c:peanut_butter, c:cheese, etc.)
    // =========================================================================

    // Crema de cacahuate con apio — combinacion especifica de CT sin equivalente
    // La ocultamos porque su receta fue eliminada junto con todas las de CT.
    event.hide('croptopia:peanut_butter_with_celery');

    // Mermelada de cacahuate — misma razon
    event.hide('croptopia:peanut_butter_and_jam');

    console.log('[Servo] 01_hide_duplicates: items duplicados ocultos de EMI.');
});
