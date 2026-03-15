// =============================================================================
// 01_cooking_cleanup.js
// PROPOSITO: Unificar ingredientes de cocina entre mods mediante tags comunes.
//
// Mods de cocina en el modpack:
//   - Farmer's Delight (FD)   — workstations base: Cutting Board, Cooking Pot, Skillet
//   - Expanded Delight (ED)   — extiende FD: Juicer, Cask, ingredientes propios
//   - Brewin' And Chewin' (BAC) — fermentacion: Keg, Heating Cask, quesos, bebidas
//   - Croptopia (CT)          — ingredientes crudos SOLAMENTE
//
// NOTA SOBRE RECETAS DE CROPTOPIA:
//   Las recetas de Croptopia se eliminan en:
//     server_scripts/00_cleanup/remove_croptopia_recipes.js
//   Ese script hace event.remove({ mod: 'croptopia' }) — elimina TODAS sus recetas.
//   Este script NO necesita repetir esa operacion.
//
// NOTA SOBRE ITEMS DUPLICADOS (replaceInput):
//   La logica de reemplazo de inputs en recetas existentes se hace en:
//     server_scripts/00_cleanup/remove_duplicate_items.js
//   Este script SOLO agrega items a tags comunes.
//
// IDs verificados contra docs/mod-data/*.json
// =============================================================================

ServerEvents.tags('item', event => {

    // =========================================================================
    // SAL
    // Tag: c:salt
    // Canonico: expandeddelight:salt (viene de salt_rock en la fundicion)
    // =========================================================================
    event.add('c:salt', [
        'expandeddelight:salt',
        'croptopia:salt',
        'croptopia:mountain_salt',
    ]);

    // =========================================================================
    // QUESO
    // Tag: c:cheese
    // Incluimos todos los quesos disponibles en el modpack.
    // expandeddelight:cheese_slice viene del Cheese Wheel cortado (Cutting Board).
    // expandeddelight:goat_cheese_slice viene del Goat Cheese Wheel.
    // brewinandchewin: quesos fermentados en el Keg (flaxen = amarillo, scarlet = rojo).
    // =========================================================================
    event.add('c:cheese', [
        'croptopia:cheese',
        'brewinandchewin:flaxen_cheese_wedge',
        'brewinandchewin:scarlet_cheese_wedge',
        'expandeddelight:cheese_slice',
        'expandeddelight:goat_cheese_slice',
    ]);

    // =========================================================================
    // HARINA
    // Tag: c:flour
    // FD no tiene harina propia — croptopia:flour es el unico.
    // =========================================================================
    event.add('c:flour', [
        'croptopia:flour',
    ]);

    // =========================================================================
    // MASA / DOUGH
    // Tag: c:dough
    // farmersdelight:wheat_dough = masa de trigo (FD Cutting Board + agua/huevo)
    // croptopia:dough = masa basica (crafting table)
    // =========================================================================
    event.add('c:dough', [
        'croptopia:dough',
        'farmersdelight:wheat_dough',
    ]);

    // =========================================================================
    // MANTEQUILLA
    // Tag: c:butter
    // Solo Croptopia la tiene como item standalone.
    // =========================================================================
    event.add('c:butter', [
        'croptopia:butter',
    ]);

    // =========================================================================
    // LECHE (en botella, no bucket)
    // Tag: c:milk
    // farmersdelight:milk_bottle = FD (se obtiene con botella en vaca)
    // croptopia:milk_bottle = CT (se obtiene en crafting table con bucket)
    // expandeddelight:goat_milk_bottle = ED (se obtiene con botella en cabra)
    // =========================================================================
    event.add('c:milk', [
        'farmersdelight:milk_bottle',
        'croptopia:milk_bottle',
        'expandeddelight:goat_milk_bottle',
    ]);

    // =========================================================================
    // AZUCAR AMPLIADO
    // Tag: c:sugar (vanilla ya tiene minecraft:sugar, ampliamos con derivados)
    // croptopia:molasses = melaza (subproducto de caña de azucar en Croptopia)
    // croptopia:caramel = caramelo (se hace de azucar en crafting/horno Croptopia)
    // =========================================================================
    event.add('c:sugar', [
        'minecraft:sugar',
        'croptopia:molasses',
        'croptopia:caramel',
    ]);

    // =========================================================================
    // ACEITE DE COCINA
    // Tag: c:cooking_oil
    // Solo Croptopia tiene aceite de oliva como item.
    // =========================================================================
    event.add('c:cooking_oil', [
        'croptopia:olive_oil',
    ]);

    // =========================================================================
    // CREMA
    // Tag: c:cream
    // croptopia:whipping_cream = crema para batir
    // croptopia:crema = crema agria / sour cream
    // =========================================================================
    event.add('c:cream', [
        'croptopia:whipping_cream',
        'croptopia:crema',
    ]);

    // =========================================================================
    // CEBOLLA
    // Tag: c:onion
    // farmersdelight:onion = cebolla amarilla (FD, crop vanilla-like)
    // croptopia:onion = cebolla regular (CT, crop CT)
    // croptopia:greenonion = cebolla de cambray / spring onion (CT)
    // =========================================================================
    event.add('c:onion', [
        'farmersdelight:onion',
        'croptopia:onion',
        'croptopia:greenonion',
    ]);

    // =========================================================================
    // TOMATE
    // Tag: c:tomato
    // farmersdelight:tomato = tomate (FD, crop vanilla-like)
    // croptopia:tomato = tomate (CT, crop CT)
    // =========================================================================
    event.add('c:tomato', [
        'farmersdelight:tomato',
        'croptopia:tomato',
    ]);

    // =========================================================================
    // COL / REPOLLO
    // Tag: c:cabbage
    // farmersdelight:cabbage = repollo verde (FD)
    // croptopia:cabbage = repollo (CT)
    // =========================================================================
    event.add('c:cabbage', [
        'farmersdelight:cabbage',
        'croptopia:cabbage',
    ]);

    // =========================================================================
    // ARROZ (crudo / raw)
    // Tag: c:rice
    // farmersdelight:rice = arroz crudo en grano (FD, se cultiva en agua)
    // croptopia:rice = arroz crudo (CT)
    // =========================================================================
    event.add('c:rice', [
        'farmersdelight:rice',
        'croptopia:rice',
    ]);

    // =========================================================================
    // CANELA
    // Tag: c:cinnamon
    // expandeddelight:cinnamon = sticks de canela (ED, se obtiene del arbol de canela)
    // croptopia:cinnamon = canela en polvo (CT)
    // NOTA: expandeddelight:cinnamon_stick es la barra del arbol (madera), no la especia.
    //       expandeddelight:cinnamon es la especia comestible — ese es el canonico.
    // =========================================================================
    event.add('c:cinnamon', [
        'expandeddelight:cinnamon',
        'croptopia:cinnamon',
    ]);

    // =========================================================================
    // CACAHUATE / MANI / PEANUT
    // Tag: c:peanut
    // expandeddelight:peanut = cacahuate (ED, crop propio)
    // croptopia:peanut = cacahuate (CT)
    // =========================================================================
    event.add('c:peanut', [
        'expandeddelight:peanut',
        'croptopia:peanut',
    ]);

    // =========================================================================
    // MANTO DE SAL (mineral)
    // Tag: c:salt_ore
    // expandeddelight:salt_ore = bloque de mineral de sal (ED, overworld)
    // Croptopia no tiene su propio salt_ore como bloque separado —
    //   su sal viene directo de crafting o como drop.
    // NOTA: expandeddelight tambien tiene deepslate_salt_ore (bloque, no item).
    // =========================================================================
    event.add('c:salt_ore', [
        'expandeddelight:salt_ore',
    ]);

    // =========================================================================
    // CHILE / PIMIENTA / PEPPER
    // Tag: c:pepper
    // croptopia:pepper = pimienta negra molida (CT)
    // croptopia:chile_pepper = chile (CT)
    // croptopia:bellpepper = pimiento morrón (CT)
    // expandeddelight:chili_pepper = chile rojo (ED, crop propio)
    // =========================================================================
    event.add('c:pepper', [
        'croptopia:pepper',
        'croptopia:chile_pepper',
        'croptopia:bellpepper',
        'expandeddelight:chili_pepper',
    ]);

    // =========================================================================
    // FIDEOS / NOODLE
    // Tag: c:noodle
    // croptopia:noodle = fideo seco (CT, hecho de harina)
    // farmersdelight:raw_pasta = pasta cruda (FD, hecho de harina en Cutting Board)
    // =========================================================================
    event.add('c:noodle', [
        'croptopia:noodle',
        'farmersdelight:raw_pasta',
    ]);

    // =========================================================================
    // MANTEQUILLA DE CACAHUATE
    // Tag: c:peanut_butter
    // expandeddelight:peanut_butter = mantequilla de cacahuate (ED, Cooking Pot)
    // croptopia:peanut_butter = mantequilla de cacahuate (CT) — receta eliminada en 00_cleanup
    // =========================================================================
    event.add('c:peanut_butter', [
        'expandeddelight:peanut_butter',
        'croptopia:peanut_butter',
    ]);

    // =========================================================================
    // CAMOTE / BATATA / SWEET POTATO (crudo)
    // Tag: c:sweet_potato
    // expandeddelight:sweet_potato = camote crudo (ED, crop propio)
    // croptopia:sweetpotato = camote crudo (CT, nombre sin guion bajo — asi esta en el JAR)
    // Ambos van al tag para que los crops de cualquier mod funcionen en las recetas.
    // En EMI el de CT se oculta (ver 01_hide_duplicates.js).
    // =========================================================================
    event.add('c:sweet_potato', [
        'expandeddelight:sweet_potato',
        'croptopia:sweetpotato',
    ]);

    // =========================================================================
    // ESPARRAGOS
    // Tag: c:asparagus
    // expandeddelight:asparagus = esparragos (ED, crop propio)
    // croptopia:asparagus = esparragos (CT, crop CT)
    // Ambos al tag; el de CT se oculta en EMI.
    // =========================================================================
    event.add('c:asparagus', [
        'expandeddelight:asparagus',
        'croptopia:asparagus',
    ]);

    // =========================================================================
    // ARANDANOS / CRANBERRY
    // Tag: c:cranberry
    // expandeddelight:cranberries = arandanos rojos (ED, crop propio — nota: plural en ID)
    // croptopia:cranberry = arandano rojo (CT, singular en ID)
    // Ambos al tag; el de CT se oculta en EMI.
    // =========================================================================
    event.add('c:cranberry', [
        'expandeddelight:cranberries',
        'croptopia:cranberry',
    ]);

    // =========================================================================
    // LIMON
    // Tag: c:lemon
    // expandeddelight:lemon = limon amarillo (ED, crop propio)
    // =========================================================================
    event.add('c:lemon', [
        'expandeddelight:lemon',
    ]);

    console.log('[Servo] 01_cooking_cleanup: tags de ingredientes de cocina aplicados.');
});
