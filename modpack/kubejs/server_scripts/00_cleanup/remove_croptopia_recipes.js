// PROPOSITO: Quitar TODAS las recetas de Croptopia.
// Solo queremos sus crops/ingredientes crudos.
// Las recetas se redirigiran a workstations de FD en otros scripts.

ServerEvents.recipes(event => {
    const removed = event.remove({ mod: 'croptopia' });
    console.log(`[Servo] Removed ${removed} Croptopia recipes`);
});
