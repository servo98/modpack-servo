// PROPOSITO: Cuando hay items duplicados entre mods,
// elegir UNO como el canonico y reemplazar el otro en todas las recetas.
// NOTA: Esta lista se actualizara con los conflictos reales encontrados
// despues de cargar ambos mods (Fase 1.4.1)

ServerEvents.recipes(event => {
    // Ejemplo - verificar cuales realmente se duplican:
    // event.replaceInput({}, 'croptopia:tomato', 'farmersdelight:tomato');
    // event.replaceInput({}, 'croptopia:onion', 'farmersdelight:onion');
    // event.replaceInput({}, 'croptopia:cabbage', 'farmersdelight:cabbage');
    // event.replaceInput({}, 'croptopia:rice', 'farmersdelight:rice');
    console.log('[Servo] Duplicate item replacement ready (update after mod install)');
});
