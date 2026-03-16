// =============================================================================
// 01_hide_duplicates.js  (client_scripts)
// PROPOSITO: Ocultar de EMI los items de Croptopia que:
//   A) Tienen un duplicado canonico en FD/ED/B&C (ingredientes crudos)
//   B) Son comidas finales cuya receta fue eliminada Y ya existe equivalente en otro mod
//   C) Son comidas finales cuya receta fue eliminada y seran re-implementadas
//      en servo_cooking (workstations futuras: Prep Station, Licuadora, Wok, Baker's Oven)
//
// NOTA: Los items cuya receta fue RE-CREADA en 02_cooking_redirects.js
//       NO se ocultan — el jugador debe verlos en EMI con su nueva receta.
//
// NOTA: Ocultar un item de EMI NO lo elimina del juego.
//   Sigue siendo crafteable/obtenible y funciona en recetas via tags.
//   Solo desaparece del buscador de recetas para reducir confusion visual.
//
// API: ClientEvents.hideItemsFromViewers (KubeJS 6, NeoForge 1.21.1)
//   Esta API es respetada por EMI, JEI y REI automaticamente.
//
// IDs verificados contra tools/recipe-tree-data.js (ITEM_NAMES, croptopia entries)
// =============================================================================

ClientEvents.hideItemsFromViewers(event => {

    // =========================================================================
    // SECCION A: INGREDIENTES CRUDOS DUPLICADOS
    // Croptopia tiene versiones de ingredientes que ya existen en FD o ED.
    // Ocultamos la version de CT y dejamos visible el canonico del otro mod.
    // =========================================================================

    // --- Croptopia → Farmer's Delight ---
    // Cebolla (usar farmersdelight:onion)
    event.hide('croptopia:onion');
    // Green onion NO se oculta — no tiene equivalente exacto en FD/ED
    // event.hide('croptopia:greenonion'); // VISIBLE — ingrediente unico

    // Tomate (usar farmersdelight:tomato)
    event.hide('croptopia:tomato');

    // Repollo (usar farmersdelight:cabbage)
    event.hide('croptopia:cabbage');

    // Arroz crudo (usar farmersdelight:rice)
    event.hide('croptopia:rice');

    // Leche en botella (usar farmersdelight:milk_bottle)
    event.hide('croptopia:milk_bottle');

    // --- Croptopia → Expanded Delight ---
    // Camote crudo (usar expandeddelight:sweet_potato)
    // NOTA: CT usa 'sweetpotato' sin guion bajo — asi esta en el JAR.
    event.hide('croptopia:sweetpotato');

    // Esparragos (usar expandeddelight:asparagus)
    event.hide('croptopia:asparagus');

    // Chile pepper (usar expandeddelight:chili_pepper)
    // bellpepper y pepper de CT son DISTINTOS a chili_pepper — se dejan visibles
    event.hide('croptopia:chile_pepper');

    // Canela especia (usar expandeddelight:cinnamon)
    event.hide('croptopia:cinnamon');

    // Limon (usar expandeddelight:lemon)
    event.hide('croptopia:lemon');

    // Sal (usar expandeddelight:salt)
    // Mountain salt es variante de drop de mina — se deja visible
    event.hide('croptopia:salt');

    // Arandano rojo (usar expandeddelight:cranberries)
    // CT usa 'cranberry' singular, ED usa 'cranberries' plural
    event.hide('croptopia:cranberry');

    // Cacahuate (usar expandeddelight:peanut)
    event.hide('croptopia:peanut');

    // Mantequilla de cacahuate (usar expandeddelight:peanut_butter)
    // La receta de CT fue eliminada; la de ED sigue en Cooking Pot
    event.hide('croptopia:peanut_butter');

    // =========================================================================
    // SECCION B: COMIDAS FINALES DUPLICADAS CON OTRO MOD
    // Sus recetas fueron eliminadas Y ya existe un equivalente en FD/ED/B&C.
    // Se ocultan para no confundir al jugador con items sin receta.
    // En cada caso se indica el item canonico con receta.
    // =========================================================================

    // Estofado de res → farmersdelight:beef_stew (Cooking Pot de FD)
    event.hide('croptopia:beef_stew');

    // Sopa de calabaza → farmersdelight:pumpkin_soup (Cooking Pot de FD)
    event.hide('croptopia:pumpkin_soup');

    // Ratatouille → farmersdelight:ratatouille (Cooking Pot de FD)
    event.hide('croptopia:ratatouille');

    // Ensalada de frutas → farmersdelight:fruit_salad (Cutting Board o Cooking Pot de FD)
    event.hide('croptopia:fruit_salad');

    // Hamburguesa → farmersdelight:hamburger (Cutting Board: beef_patty + bun)
    event.hide('croptopia:hamburger');

    // Cerveza → brewinandchewin:beer (Keg de B&C, fermentacion)
    event.hide('croptopia:beer');

    // Aguamiel → brewinandchewin:mead (Keg de B&C, fermentacion con miel)
    event.hide('croptopia:mead');

    // Jugo de manzana → expandeddelight:apple_juice (Juicer de ED)
    event.hide('croptopia:apple_juice');

    // Jugo de arandano → expandeddelight:cranberry_juice (Juicer de ED)
    event.hide('croptopia:cranberry_juice');

    // Sandwich de queso fundido → expandeddelight:grilled_cheese (campfire cooking de ED)
    event.hide('croptopia:grilled_cheese');

    // Jugo de melon → farmersdelight:melon_juice (FD, Cooking Pot o crafting)
    event.hide('croptopia:melon_juice');

    // Tocino cocinado → farmersdelight:cooked_bacon (FD, horno/ahumador/fogata)
    event.hide('croptopia:cooked_bacon');

    // Camote asado → expandeddelight:baked_sweet_potato (ED, horno/ahumador/fogata)
    event.hide('croptopia:baked_sweet_potato');

    // =========================================================================
    // SECCION C: DERIVADOS DE CT QUE USAN INGREDIENTES OCULTOS
    // Items combinados especificos de Croptopia cuya receta fue eliminada.
    // =========================================================================

    // Mantequilla de cacahuate con apio — combinacion CT sin equivalente en otro mod
    event.hide('croptopia:peanut_butter_with_celery');

    // Mantequilla de cacahuate con mermelada
    event.hide('croptopia:peanut_butter_and_jam');

    // =========================================================================
    // SECCION D: COMIDAS FINALES SIN RECETA — WORKSTATIONS FUTURAS (servo_cooking)
    // Las recetas de estos items fueron eliminadas por remove_croptopia_recipes.js
    // y AUN NO han sido re-implementadas porque requieren workstations que no existen:
    //   - Prep Station (sandwiches, ensaladas, tortillas, sushi)
    //   - Licuadora / Blender (smoothies, jugos, horchata)
    //   - Wok (frituras profundas, fried chicken, fish and chips)
    //   - Baker's Oven (panes, pasteles, pies, galletas, crepes)
    //
    // Cuando servo_cooking implemente estas workstations, se agregan las recetas
    // y se quitan los event.hide() correspondientes.
    // =========================================================================

    // --- Frituras (futuro: Wok) ---
    event.hide('croptopia:deep_fried_shrimp');
    event.hide('croptopia:fish_and_chips');
    event.hide('croptopia:french_fries');
    event.hide('croptopia:fried_calamari');
    event.hide('croptopia:fried_chicken');
    event.hide('croptopia:fried_frog_legs');
    event.hide('croptopia:grilled_eggplant');
    event.hide('croptopia:grilled_oysters');
    event.hide('croptopia:hashed_brown');
    event.hide('croptopia:kale_chips');
    event.hide('croptopia:onion_rings');
    event.hide('croptopia:potato_chips');
    event.hide('croptopia:roasted_asparagus');
    event.hide('croptopia:roasted_pumpkin_seeds');
    event.hide('croptopia:roasted_radishes');
    event.hide('croptopia:roasted_squash');
    event.hide('croptopia:roasted_sunflower_seeds');
    event.hide('croptopia:roasted_turnips');
    event.hide('croptopia:steamed_broccoli');
    event.hide('croptopia:steamed_clams');
    event.hide('croptopia:steamed_crab');
    event.hide('croptopia:steamed_green_beans');
    event.hide('croptopia:sweet_potato_fries');
    event.hide('croptopia:buttered_green_beans');

    // --- Sandwiches, wraps y comida rapida (futuro: Prep Station) ---
    event.hide('croptopia:ajvar_toast');
    event.hide('croptopia:avocado_toast');
    event.hide('croptopia:blt');
    event.hide('croptopia:burrito');
    event.hide('croptopia:buttered_toast');
    event.hide('croptopia:caesar_salad');
    event.hide('croptopia:cabbage_roll');
    event.hide('croptopia:cheeseburger');
    event.hide('croptopia:cheesy_asparagus');
    event.hide('croptopia:chimichanga');
    event.hide('croptopia:dumpling');
    event.hide('croptopia:egg_roll');
    event.hide('croptopia:enchilada');
    event.hide('croptopia:fajitas');
    event.hide('croptopia:ham_sandwich');
    event.hide('croptopia:leafy_salad');
    event.hide('croptopia:quesadilla');
    event.hide('croptopia:scrambled_eggs');
    event.hide('croptopia:stuffed_poblanos');
    event.hide('croptopia:sunny_side_eggs');
    event.hide('croptopia:taco');
    event.hide('croptopia:toast_sandwich');
    event.hide('croptopia:toast_with_jam');
    event.hide('croptopia:tofuburger');
    event.hide('croptopia:tostada');
    event.hide('croptopia:trail_mix');
    event.hide('croptopia:tuna_roll');
    event.hide('croptopia:tuna_sandwich');
    event.hide('croptopia:veggie_salad');
    event.hide('croptopia:cucumber_salad');
    event.hide('croptopia:beetroot_salad');
    event.hide('croptopia:sushi');
    event.hide('croptopia:protein_bar');
    event.hide('croptopia:chili_relleno');

    // --- Licuados / jugos (futuro: Licuadora) ---
    event.hide('croptopia:banana_smoothie');
    event.hide('croptopia:fruit_smoothie');
    event.hide('croptopia:grape_juice');
    event.hide('croptopia:horchata');
    event.hide('croptopia:kale_smoothie');
    event.hide('croptopia:kiwi_sorbet');
    event.hide('croptopia:lemonade');
    event.hide('croptopia:limeade');
    event.hide('croptopia:orange_juice');
    event.hide('croptopia:pineapple_juice');
    event.hide('croptopia:pumpkin_spice_latte');
    event.hide('croptopia:saguaro_juice');
    event.hide('croptopia:soy_milk');
    event.hide('croptopia:strawberry_smoothie');
    event.hide('croptopia:tea');
    event.hide('croptopia:coffee');
    event.hide('croptopia:tomato_juice');

    // --- Panaderia y reposteria (futuro: Baker's Oven) ---
    event.hide('croptopia:apple_pie');
    event.hide('croptopia:almond_brittle');
    event.hide('croptopia:baked_beans');
    event.hide('croptopia:baked_crepes');
    event.hide('croptopia:baked_yam');
    event.hide('croptopia:banana_cream_pie');
    event.hide('croptopia:banana_nut_bread');
    event.hide('croptopia:brownies');
    event.hide('croptopia:candied_kumquats');
    event.hide('croptopia:candied_nuts');
    event.hide('croptopia:cheese_cake');
    event.hide('croptopia:cherry_pie');
    event.hide('croptopia:chocolate_ice_cream');
    event.hide('croptopia:chocolate_milkshake');
    event.hide('croptopia:churros');
    event.hide('croptopia:cinnamon_roll');
    event.hide('croptopia:corn_bread');
    event.hide('croptopia:croque_madame');
    event.hide('croptopia:croque_monsieur');
    event.hide('croptopia:doughnut');
    event.hide('croptopia:dragon_egg_omelette');
    event.hide('croptopia:eton_mess');
    event.hide('croptopia:figgy_pudding');
    event.hide('croptopia:fruit_cake');
    event.hide('croptopia:glowing_calamari');
    event.hide('croptopia:lemon_coconut_bar');
    event.hide('croptopia:macaron');
    event.hide('croptopia:mango_ice_cream');
    event.hide('croptopia:meringue');
    event.hide('croptopia:nether_star_cake');
    event.hide('croptopia:nougat');
    event.hide('croptopia:nutty_cookie');
    event.hide('croptopia:pecan_ice_cream');
    event.hide('croptopia:pecan_pie');
    event.hide('croptopia:pumpkin_bars');
    event.hide('croptopia:quiche');
    event.hide('croptopia:raisin_oatmeal_cookie');
    event.hide('croptopia:rhubarb_crisp');
    event.hide('croptopia:rhubarb_pie');
    event.hide('croptopia:rum_raisin_ice_cream');
    event.hide('croptopia:saucy_chips');
    event.hide('croptopia:scones');
    event.hide('croptopia:snicker_doodle');
    event.hide('croptopia:sticky_toffee_pudding');
    event.hide('croptopia:strawberry_ice_cream');
    event.hide('croptopia:sweet_crepes');
    event.hide('croptopia:treacle_tart');
    event.hide('croptopia:tres_leche_cake');
    event.hide('croptopia:trifle');
    event.hide('croptopia:vanilla_ice_cream');

    // --- Pizzas (futuro: Baker's Oven o Prep Station) ---
    event.hide('croptopia:anchovy_pizza');
    event.hide('croptopia:cheese_pizza');
    event.hide('croptopia:pineapple_pepperoni_pizza');
    event.hide('croptopia:pizza');
    event.hide('croptopia:supreme_pizza');

    // --- Bebidas fermentadas (futuro: Keg una vez confirmado el formato) ---
    // NOTA: rum y wine estan comentados en 02_cooking_redirects.js (TODO: formato B&C).
    // Se ocultan temporalmente hasta que el Keg este implementado.
    event.hide('croptopia:rum');
    event.hide('croptopia:wine');

    // --- Tasajos / jerky (futuro: Keg) ---
    event.hide('croptopia:beef_jerky');
    event.hide('croptopia:pork_jerky');

    // =========================================================================
    // HERRAMIENTAS Y BLOQUES DE CROPTOPIA
    // No son items de comida pero los ocultamos para limpiar el inventario creativo.
    // El jugador no necesita la Frying Pan, Food Press, Mortar and Pestle, Knife
    // ni Cooking Pot de CT porque usamos las workstations de FD/Create.
    // =========================================================================
    event.hide('croptopia:cooking_pot');
    event.hide('croptopia:food_press');
    event.hide('croptopia:frying_pan');
    event.hide('croptopia:knife');
    event.hide('croptopia:mortar_and_pestle');

    // El guide de Croptopia se oculta — usamos FTB Quests como guia del modpack
    event.hide('croptopia:guide');

    console.log('[Servo] 01_hide_duplicates: items de Croptopia ocultos de EMI.');
});
