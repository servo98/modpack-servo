// =============================================================================
// 02_cooking_redirects.js
// PROPOSITO: Re-crear las recetas de Croptopia que fueron eliminadas en
//            00_cleanup/remove_croptopia_recipes.js, pero ahora usando
//            las workstations "correctas" del modpack:
//
//   - Cooking Pot (farmersdelight:cooking)   — sopas, guisos, salsas, mermeladas
//   - Keg (brewinandchewin)                  — bebidas y curados (TODO: formato pendiente)
//   - Create Millstone (create:milling)       — molienda seca
//   - Create Mechanical Press (create:pressing) — prensado
//   - Create Mixer (create:mixing)            — mezclado humedo
//   - Horno/ahumador/fogata (minecraft:smelting / smoking / campfire_cooking)
//                                             — ingredientes intermedios cocidos
//
// NOTA SOBRE INGREDIENTES:
//   Los inputs usan tags comunes (#c:salt, #c:flour, etc.) definidos en
//   01_cooking_cleanup.js para que funcionen con items de cualquier mod.
//   Los outputs usan IDs especificos de Croptopia porque ese es el item que
//   se muestra al jugador en EMI y que vale como comida.
//
// NOTA SOBRE DUPLICADOS ELIMINADOS:
//   Se omiten recetas cuyo resultado ya existe en otro mod:
//     croptopia:beef_stew     → farmersdelight:beef_stew
//     croptopia:pumpkin_soup  → farmersdelight:pumpkin_soup
//     croptopia:ratatouille   → farmersdelight:ratatouille
//     croptopia:beer          → brewinandchewin:beer
//     croptopia:mead          → brewinandchewin:mead
//
// IDs verificados contra tools/recipe-tree-data.js (ITEM_NAMES)
// Formato verificado contra documentacion de FD, Create y KubeJS 6.
// =============================================================================

ServerEvents.recipes(event => {

    // =========================================================================
    // INGREDIENTES INTERMEDIOS — SMELTING / SMOKING / CAMPFIRE
    // Estas recetas de Croptopia fueron eliminadas por remove_croptopia_recipes.js.
    // Son necesarias como inputs en cadenas de otras recetas de este script,
    // asi que se re-crean aqui antes de las recetas de workstations.
    // =========================================================================

    // Tocino cocinado
    // NOTA: farmersdelight:cooked_bacon YA existe como item canonico.
    // Creamos la receta en horno usando farmersdelight:bacon como input
    // para que siga siendo obtenible por smelting. No re-creamos la de CT.
    // (No es necesario: FD ya tiene receta de smelting para su propio bacon.)

    // Anchoa cocinada
    event.smelting('croptopia:cooked_anchovy', 'croptopia:anchovy').xp(0.35).cookingTime(200);
    event.smoking('croptopia:cooked_anchovy', 'croptopia:anchovy').xp(0.35).cookingTime(100);
    event.campfireCooking('croptopia:cooked_anchovy', 'croptopia:anchovy').xp(0.35).cookingTime(600);

    // Atun cocinado
    event.smelting('croptopia:cooked_tuna', 'croptopia:tuna').xp(0.35).cookingTime(200);
    event.smoking('croptopia:cooked_tuna', 'croptopia:tuna').xp(0.35).cookingTime(100);
    event.campfireCooking('croptopia:cooked_tuna', 'croptopia:tuna').xp(0.35).cookingTime(600);

    // Camaron cocinado
    event.smelting('croptopia:cooked_shrimp', 'croptopia:shrimp').xp(0.35).cookingTime(200);
    event.smoking('croptopia:cooked_shrimp', 'croptopia:shrimp').xp(0.35).cookingTime(100);
    event.campfireCooking('croptopia:cooked_shrimp', 'croptopia:shrimp').xp(0.35).cookingTime(600);

    // Calamar cocinado
    event.smelting('croptopia:cooked_calamari', 'croptopia:calamari').xp(0.35).cookingTime(200);
    event.smoking('croptopia:cooked_calamari', 'croptopia:calamari').xp(0.35).cookingTime(100);
    event.campfireCooking('croptopia:cooked_calamari', 'croptopia:calamari').xp(0.35).cookingTime(600);

    // Camote asado
    // NOTA: farmersdelight:cooked_bacon y expandeddelight:baked_sweet_potato son canonicos.
    // No re-creamos croptopia:baked_sweet_potato — usamos el de ED.
    // (Se oculta en 01_hide_duplicates.js)

    // Tostada (pan tostado)
    event.smelting('croptopia:toast', 'minecraft:bread').xp(0.1).cookingTime(100);
    event.campfireCooking('croptopia:toast', 'minecraft:bread').xp(0.1).cookingTime(300);

    // Caramelo
    event.smelting('croptopia:caramel', 'minecraft:sugar').xp(0.1).cookingTime(100);

    // Melaza
    event.smelting('croptopia:molasses', 'minecraft:sugar_cane').xp(0.1).cookingTime(100);

    // Palomitas de maiz (popcorn)
    event.smelting('croptopia:popcorn', 'croptopia:corn').xp(0.1).cookingTime(100);
    event.campfireCooking('croptopia:popcorn', 'croptopia:corn').xp(0.1).cookingTime(300);

    // Uvas pasas / raisins
    event.smelting('croptopia:raisins', 'croptopia:grape').xp(0.1).cookingTime(100);

    // Nueces tostadas / roasted_nuts (usa varios frutos secos como input con tag)
    // Usamos almond como representante; el tag c:nuts no existe, usamos items especificos.
    event.smelting('croptopia:roasted_nuts', 'croptopia:almond').xp(0.1).cookingTime(100);
    event.smelting('croptopia:roasted_nuts', 'croptopia:cashew').xp(0.1).cookingTime(100);
    event.smelting('croptopia:roasted_nuts', 'croptopia:pecan').xp(0.1).cookingTime(100);
    event.smelting('croptopia:roasted_nuts', 'croptopia:walnut').xp(0.1).cookingTime(100);
    event.campfireCooking('croptopia:roasted_nuts', 'croptopia:almond').xp(0.1).cookingTime(300);

    // Sal de Croptopia — se obtiene del horno con sal ore
    // (No la ocultamos para que sirva como ingrediente; se une al tag c:salt)
    event.smelting('croptopia:salt', 'croptopia:salt_ore').xp(0.1).cookingTime(100);


    // =========================================================================
    // CREATE: MILLSTONE (create:milling)
    // Molienda en seco. processingTime en ticks (20 ticks = 1 segundo).
    // =========================================================================

    // Harina de trigo
    event.custom({
        type: 'create:milling',
        ingredients: [{ item: 'minecraft:wheat' }],
        results: [{ id: 'croptopia:flour', count: 1 }],
        processingTime: 100
    });

    // Paprika (se hace de pepper)
    event.custom({
        type: 'create:milling',
        ingredients: [{ item: 'croptopia:pepper' }],
        results: [{ id: 'croptopia:paprika', count: 1 }],
        processingTime: 100
    });

    // Carne molida de cerdo (ground pork)
    event.custom({
        type: 'create:milling',
        ingredients: [{ item: 'minecraft:porkchop' }],
        results: [{ id: 'croptopia:ground_pork', count: 2 }],
        processingTime: 150
    });

    // Mantequilla de cacahuate (via molienda — alternativa al Cooking Pot de ED)
    // NOTA: croptopia:peanut_butter se oculta en EMI porque ED lo tiene como canonico.
    // Esta receta usa expandeddelight:peanut (canonico) como input.
    event.custom({
        type: 'create:milling',
        ingredients: [{ tag: 'c:peanut', count: 4 }],
        results: [{ id: 'croptopia:peanut_butter', count: 2 }],
        processingTime: 150
    });


    // =========================================================================
    // CREATE: MECHANICAL PRESS (create:pressing)
    // Prensado. Solo acepta un ingrediente.
    // =========================================================================

    // Aceite de oliva
    event.custom({
        type: 'create:pressing',
        ingredients: [{ item: 'croptopia:olive', count: 2 }],
        results: [{ id: 'croptopia:olive_oil', count: 1 }]
    });

    // Tofu (de soya prensada)
    event.custom({
        type: 'create:pressing',
        ingredients: [{ item: 'croptopia:soybean', count: 2 }],
        results: [{ id: 'croptopia:tofu', count: 1 }]
    });


    // =========================================================================
    // CREATE: MIXER (create:mixing)
    // Mezclado humedo. Puede tener fluidos si se necesita.
    // =========================================================================

    // Mantequilla (de leche)
    event.custom({
        type: 'create:mixing',
        ingredients: [{ tag: 'c:milk', count: 2 }],
        results: [{ id: 'croptopia:butter', count: 1 }]
    });

    // Chocolate (tableta)
    event.custom({
        type: 'create:mixing',
        ingredients: [
            { item: 'minecraft:cocoa_beans' },
            { tag: 'c:milk' },
            { item: 'minecraft:sugar' }
        ],
        results: [{ id: 'croptopia:chocolate', count: 4 }]
    });

    // Fideo seco (noodle) — harina + huevo
    event.custom({
        type: 'create:mixing',
        ingredients: [
            { tag: 'c:flour' },
            { item: 'minecraft:egg' }
        ],
        results: [{ id: 'croptopia:noodle', count: 2 }]
    });

    // Crema para batir (whipping cream) — de leche
    event.custom({
        type: 'create:mixing',
        ingredients: [{ tag: 'c:milk', count: 3 }],
        results: [{ id: 'croptopia:whipping_cream', count: 4 }]
    });

    // Yogur — de leche
    event.custom({
        type: 'create:mixing',
        ingredients: [{ tag: 'c:milk', count: 2 }],
        results: [{ id: 'croptopia:yoghurt', count: 2 }]
    });

    // Queso — leche + sal
    event.custom({
        type: 'create:mixing',
        ingredients: [
            { tag: 'c:milk', count: 3 },
            { tag: 'c:salt' }
        ],
        results: [{ id: 'croptopia:cheese', count: 2 }]
    });

    // Salchicha (sausage) — ground pork + sal + pimienta
    event.custom({
        type: 'create:mixing',
        ingredients: [
            { item: 'croptopia:ground_pork' },
            { tag: 'c:salt' },
            { item: 'croptopia:pepper' }
        ],
        results: [{ id: 'croptopia:sausage', count: 2 }]
    });

    // Pepperoni — ground pork + paprika + chile + sal
    event.custom({
        type: 'create:mixing',
        ingredients: [
            { item: 'croptopia:ground_pork' },
            { item: 'croptopia:paprika' },
            { tag: 'c:pepper' },
            { tag: 'c:salt' }
        ],
        results: [{ id: 'croptopia:pepperoni', count: 4 }]
    });

    // Mazapan de maiz (candy corn) — azucar + miel + maiz
    event.custom({
        type: 'create:mixing',
        ingredients: [
            { item: 'minecraft:sugar', count: 2 },
            { item: 'minecraft:honey_bottle' },
            { item: 'croptopia:corn' }
        ],
        results: [{ id: 'croptopia:candy_corn', count: 4 }]
    });


    // =========================================================================
    // COOKING POT (farmersdelight:cooking)
    // Ingredientes + bowl/container en un pot sobre fogon.
    // Format: ingredients[] (items/tags) + container (opcional) + result.
    // experience: XP por craft. cookingtime: ticks (200 = 10 segundos).
    // =========================================================================

    // --- SOPAS Y GUISOS ---

    // Borscht (sopa de remolacha)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:minced_beef' },
            { item: 'minecraft:beetroot' },
            { tag: 'c:cabbage' },
            { tag: 'c:tomato' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:borscht', count: 2 },
        experience: 0.35,
        cookingtime: 200
    });

    // Goulash (guiso hungaro)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:minced_beef' },
            { item: 'croptopia:bellpepper' },
            { tag: 'c:tomato' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:goulash', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Sopa de poro (leek soup)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:leek', count: 2 },
            { item: 'minecraft:potato' },
            { item: 'farmersdelight:bone_broth' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:leek_soup', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Sopa de papa (potato soup)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:potato', count: 2 },
            { item: 'farmersdelight:cooked_bacon' },
            { tag: 'c:milk' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:potato_soup', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Estofado de nether wart
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:nether_wart', count: 2 },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:nether_wart_stew', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Pollo con bolitas de masa (chicken and dumplings)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_chicken_cuts' },
            { tag: 'c:dough' },
            { tag: 'c:onion' },
            { tag: 'c:milk' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:chicken_and_dumplings', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Pollo con fideos (chicken and noodles)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_chicken_cuts' },
            { tag: 'c:noodle' },
            { tag: 'c:onion' },
            { item: 'minecraft:carrot' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:chicken_and_noodles', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Pollo con arroz (chicken and rice)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_chicken_cuts' },
            { item: 'farmersdelight:cooked_rice' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:chicken_and_rice', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Frijoles con tocino (pork and beans)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_bacon' },
            { item: 'croptopia:blackbean', count: 2 },
            { tag: 'c:tomato' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:pork_and_beans', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Frijoles refritos (refried beans)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:blackbean', count: 3 },
            { tag: 'c:onion' },
            { tag: 'c:cooking_oil' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:refried_beans', count: 2 },
        experience: 0.3,
        cookingtime: 200
    });

    // Ravioles (ravioli)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { tag: 'c:dough' },
            { tag: 'c:cheese' },
            { item: 'farmersdelight:tomato_sauce' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:ravioli', count: 2 },
        experience: 0.35,
        cookingtime: 200
    });

    // Tofu con bolitas de masa (tofu and dumplings)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:tofu' },
            { tag: 'c:dough' },
            { item: 'croptopia:soy_sauce' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:tofu_and_dumplings', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Calabaza espagueti (spaghetti squash)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:squash' },
            { item: 'farmersdelight:tomato_sauce' },
            { tag: 'c:cheese' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:spaghetti_squash', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Saltado de res (beef stir fry)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:minced_beef' },
            { item: 'croptopia:bellpepper' },
            { item: 'croptopia:soy_sauce' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:beef_stir_fry', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Saltado de verduras (stir fry)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:carrot' },
            { item: 'croptopia:bellpepper' },
            { item: 'croptopia:soy_sauce' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:stir_fry', count: 1 },
        experience: 0.3,
        cookingtime: 200
    });

    // Pollo con anacardos (cashew chicken)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_chicken_cuts' },
            { item: 'croptopia:cashew' },
            { item: 'croptopia:soy_sauce' },
            { item: 'croptopia:bellpepper' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:cashew_chicken', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Pollo al limon (lemon chicken)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_chicken_cuts' },
            { tag: 'c:lemon', count: 2 },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:lemon_chicken', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Carnitas (cerdo deshebrado)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:porkchop', count: 2 },
            { tag: 'c:onion' },
            { tag: 'c:pepper' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:carnitas', count: 1 },
        experience: 0.35,
        cookingtime: 300
    });

    // Tamales
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { tag: 'c:dough' },
            { item: 'minecraft:porkchop' },
            { tag: 'c:pepper' },
            { item: 'croptopia:corn' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:tamales', count: 2 },
        experience: 0.35,
        cookingtime: 300
    });

    // Berenjena a la parmesana (eggplant parmesan)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:eggplant' },
            { item: 'farmersdelight:tomato_sauce' },
            { tag: 'c:cheese' },
            { tag: 'c:flour' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:eggplant_parmesan', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Papas dauphine (croquetas de papa)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:potato', count: 2 },
            { tag: 'c:flour' },
            { item: 'minecraft:egg' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:dauphine_potatoes', count: 2 },
        experience: 0.3,
        cookingtime: 200
    });

    // Pure de papa (mashed potatoes)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:potato', count: 2 },
            { tag: 'c:butter' },
            { tag: 'c:milk' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:mashed_potatoes', count: 2 },
        experience: 0.3,
        cookingtime: 200
    });

    // Alcachofa rellena (stuffed artichoke)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:artichoke' },
            { tag: 'c:cheese' },
            { tag: 'c:flour' },
            { item: 'croptopia:garlic' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:stuffed_artichoke', count: 1 },
        experience: 0.35,
        cookingtime: 200
    });

    // Avena (oatmeal)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:oat', count: 2 },
            { tag: 'c:milk' },
            { item: 'minecraft:sugar' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:oatmeal', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Arroz al vapor (steamed rice)
    // NOTA: Diferente de farmersdelight:cooked_rice (que es arroz frito).
    // Croptopia:steamed_rice es especificamente arroz blanco hervido.
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { tag: 'c:rice', count: 2 },
            { item: 'croptopia:water_bottle' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:steamed_rice', count: 2 },
        experience: 0.2,
        cookingtime: 200
    });

    // El gran desayuno (the big breakfast)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_bacon' },
            { item: 'farmersdelight:fried_egg' },
            { item: 'croptopia:toast' },
            { tag: 'c:tomato' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:the_big_breakfast', count: 1 },
        experience: 0.5,
        cookingtime: 200
    });

    // Desayuno transcendental (transcendental breakfast)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:cooked_bacon' },
            { item: 'farmersdelight:fried_egg' },
            { tag: 'c:cheese' },
            { item: 'croptopia:sausage' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:transcendental_breakfast', count: 1 },
        experience: 0.6,
        cookingtime: 200
    });

    // Pastel del pastor (shepherd's pie)
    // NOTA: FD ya tiene farmersdelight:shepherds_pie como su propia receta.
    // Esta es croptopia:shepherds_pie (item distinto, stats distintos).
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:minced_beef' },
            { item: 'minecraft:potato', count: 2 },
            { tag: 'c:onion' },
            { item: 'minecraft:carrot' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:shepherds_pie', count: 1 },
        experience: 0.5,
        cookingtime: 300
    });

    // Pasty de Cornualles (cornish pasty)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'farmersdelight:minced_beef' },
            { item: 'minecraft:potato' },
            { tag: 'c:onion' },
            { tag: 'c:dough' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:cornish_pasty', count: 1 },
        experience: 0.4,
        cookingtime: 250
    });

    // Res Wellington (beef wellington)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'minecraft:beef' },
            { tag: 'c:dough' },
            { item: 'minecraft:brown_mushroom' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:beef_wellington', count: 1 },
        experience: 0.8,
        cookingtime: 400
    });

    // Peperonata (pimientos fritos)
    // NOTA: expandeddelight ya tiene su propia peperonata (expandeddelight:peperonata).
    // La version de Croptopia es un item distinto.
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:bellpepper', count: 2 },
            { tag: 'c:tomato' },
            { tag: 'c:onion' },
            { item: 'minecraft:bowl' }
        ],
        result: { id: 'croptopia:peperonata', count: 1 },
        experience: 0.3,
        cookingtime: 200
    });

    // --- MERMELADAS ---
    // Todas siguen el patron: fruta x2 + sugar → jam

    // Mermelada de chabacano (apricot jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:apricot', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:apricot_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de mora azul/zarzamora (blackberry jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:blackberry', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:blackberry_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de arandano azul (blueberry jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:blueberry', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:blueberry_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de cereza (cherry jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:cherry', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:cherry_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de sauco (elderberry jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:elderberry', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:elderberry_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de uva (grape jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:grape', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:grape_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de durazno (peach jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:peach', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:peach_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de frambuesa (raspberry jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:raspberry', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:raspberry_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de fresa (strawberry jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:strawberry', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:strawberry_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // Mermelada de name (yam jam)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:yam', count: 2 },
            { item: 'minecraft:sugar' }
        ],
        result: { id: 'croptopia:yam_jam', count: 1 },
        experience: 0.2,
        cookingtime: 200
    });

    // --- SALSAS Y ADEREZOS ---

    // Salsa (para tacos y burritos)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { tag: 'c:tomato', count: 2 },
            { tag: 'c:onion' },
            { tag: 'c:pepper' }
        ],
        result: { id: 'croptopia:salsa', count: 4 },
        experience: 0.2,
        cookingtime: 200
    });

    // Ajvar (salsa de pimientos asados)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:bellpepper' },
            { item: 'croptopia:eggplant' },
            { item: 'croptopia:garlic' },
            { tag: 'c:cooking_oil' }
        ],
        result: { id: 'croptopia:ajvar', count: 1 },
        experience: 0.25,
        cookingtime: 200
    });

    // Dip de alcachofa (artichoke dip)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:artichoke' },
            { tag: 'c:cheese' },
            { item: 'croptopia:garlic' }
        ],
        result: { id: 'croptopia:artichoke_dip', count: 1 },
        experience: 0.25,
        cookingtime: 200
    });

    // Salsa de soya (soy sauce)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { item: 'croptopia:soybean', count: 2 },
            { tag: 'c:salt' },
            { item: 'croptopia:water_bottle' }
        ],
        result: { id: 'croptopia:soy_sauce', count: 2 },
        experience: 0.2,
        cookingtime: 400
    });

    // Crema agria / crema mexicana (crema)
    event.custom({
        type: 'farmersdelight:cooking',
        ingredients: [
            { tag: 'c:milk', count: 2 },
            { tag: 'c:lemon' }
        ],
        result: { id: 'croptopia:crema', count: 4 },
        experience: 0.2,
        cookingtime: 200
    });


    // =========================================================================
    // KEG (brewinandchewin)
    // TODO: El formato exacto de KubeJS para recetas del Keg de Brewin' and Chewin'
    //       en NeoForge 1.21.1 no esta documentado publicamente y requiere
    //       inspeccion del JAR (mixin de RecipeSerializer) para confirmar el schema.
    //
    // Las recetas de fermentacion/curado de Croptopia que iban aqui:
    //   - croptopia:rum   — ron (fermentacion de cana de azucar)
    //   - croptopia:wine  — vino (fermentacion de uva)
    //   - croptopia:beef_jerky  — tasajo de res (curado con sal)
    //   - croptopia:pork_jerky  — tasajo de cerdo (curado con sal)
    //
    // Cuando se confirme el formato, descomenta y completa:
    //
    // Ron (rum)
    // event.custom({
    //     type: 'brewinandchewin:fermenting',
    //     ingredients: [{ item: 'minecraft:sugar_cane', count: 3 }],
    //     fluidIngredient: { fluid: 'minecraft:water', amount: 250 },
    //     result: { id: 'croptopia:rum', count: 1 },
    //     fermentingTime: 6000
    // });
    //
    // Vino (wine)
    // event.custom({
    //     type: 'brewinandchewin:fermenting',
    //     ingredients: [{ item: 'croptopia:grape', count: 4 }],
    //     fluidIngredient: { fluid: 'minecraft:water', amount: 250 },
    //     result: { id: 'croptopia:wine', count: 1 },
    //     fermentingTime: 6000
    // });
    //
    // Tasajo de res (beef jerky)
    // event.custom({
    //     type: 'brewinandchewin:fermenting',
    //     ingredients: [{ item: 'minecraft:beef', count: 2 }, { tag: 'c:salt' }],
    //     result: { id: 'croptopia:beef_jerky', count: 1 },
    //     fermentingTime: 4000
    // });
    //
    // Tasajo de cerdo (pork jerky)
    // event.custom({
    //     type: 'brewinandchewin:fermenting',
    //     ingredients: [{ item: 'minecraft:porkchop', count: 2 }, { tag: 'c:salt' }],
    //     result: { id: 'croptopia:pork_jerky', count: 1 },
    //     fermentingTime: 4000
    // });
    // =========================================================================

    console.log('[Servo] 02_cooking_redirects: recetas de Croptopia redirigidas a workstations.');
});
