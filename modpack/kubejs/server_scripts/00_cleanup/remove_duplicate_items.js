// PROPOSITO: Cuando hay items duplicados entre mods,
// elegir UNO como el canonico y reemplazar el otro en TODAS las recetas de otros mods.
// Esto asegura que recetas de FD, ED, B&C, etc. acepten el item canonico
// aunque originalmente pidieran el item de Croptopia.
//
// Estrategia de canonizacion:
//   Croptopia → Farmer's Delight  : onion, tomato, cabbage, rice, milk_bottle
//   Croptopia → Expanded Delight  : sweet_potato, asparagus, chili_pepper, cinnamon,
//                                   lemon, salt, cranberry, peanut, peanut_butter
//
// NOTA: replaceInput funciona DESPUES de que remove_croptopia_recipes elimina las recetas
// de Croptopia, asi que solo afecta recetas de OTROS mods que usaban ingredientes de CT.
//
// Ejecuta en orden alfabetico para facilitar mantenimiento.

ServerEvents.recipes(event => {

    // =========================================================================
    // Croptopia → Farmer's Delight
    // =========================================================================

    // Cebolla
    event.replaceInput({}, 'croptopia:onion', 'farmersdelight:onion');

    // Tomate
    event.replaceInput({}, 'croptopia:tomato', 'farmersdelight:tomato');

    // Repollo
    event.replaceInput({}, 'croptopia:cabbage', 'farmersdelight:cabbage');

    // Arroz crudo
    event.replaceInput({}, 'croptopia:rice', 'farmersdelight:rice');

    // Leche en botella
    event.replaceInput({}, 'croptopia:milk_bottle', 'farmersdelight:milk_bottle');

    // =========================================================================
    // Croptopia → Expanded Delight
    // =========================================================================

    // Camote crudo (nota: CT usa 'sweetpotato' sin guion bajo)
    event.replaceInput({}, 'croptopia:sweetpotato', 'expandeddelight:sweet_potato');

    // Esparragos
    event.replaceInput({}, 'croptopia:asparagus', 'expandeddelight:asparagus');

    // Chile pepper
    event.replaceInput({}, 'croptopia:chile_pepper', 'expandeddelight:chili_pepper');

    // Canela (especia, no el palo del arbol)
    event.replaceInput({}, 'croptopia:cinnamon', 'expandeddelight:cinnamon');

    // Limon
    event.replaceInput({}, 'croptopia:lemon', 'expandeddelight:lemon');

    // Sal
    event.replaceInput({}, 'croptopia:salt', 'expandeddelight:salt');

    // Arandanos (CT usa singular 'cranberry', ED usa plural 'cranberries')
    event.replaceInput({}, 'croptopia:cranberry', 'expandeddelight:cranberries');

    // Cacahuate
    event.replaceInput({}, 'croptopia:peanut', 'expandeddelight:peanut');

    // Mantequilla de cacahuate
    event.replaceInput({}, 'croptopia:peanut_butter', 'expandeddelight:peanut_butter');

    console.log('[Servo] remove_duplicate_items: inputs de Croptopia reemplazados por canonicos FD/ED.');
});
