// PROPOSITO: Recetas craft para accesorios custom T1 y T2 de servo_core.
// Problema: Los 4 slots custom (Belt/Back/Feet/Head) son items de servo_core que aun
//   no existen como JAR. T1 y T2 son los unicos tiers crafteable — T3+ vienen de
//   gacha, bosses y dungeons (ver docs/balance/accessories.md).
// Solucion: Definir las recetas con anticipacion. Si servo_core no esta cargado,
//   KubeJS ignora silenciosamente las recetas con items no encontrados
//   (log en kubejs/server.log). Mismo patron que rpg_t4_recipes.js.
//
// Issue #90 - Accessory T1/T2 Recipes
//
// Convencion de IDs: servo_core:{slot}_{effect}_t{tier}
//   Slots: belt | back | feet | head
//   Effects: speed | defense | efficiency | food | damage | exploration | dungeon |
//            luck | regen | animal | token | spellpower | vision | xp
//
// TIER 1 — "Tosco" (cobre, +5% boost) — 15 items, Ch1-2
//   Patron shaped 3x3:
//     S C S     S = minecraft:string
//     T B T     C = minecraft:copper_ingot  (tier material T1)
//     S C S     B = base del slot (leather o wool)
//                   T = ingrediente tematico del efecto
//
// TIER 2 — "Pulido" (hierro, +12% boost) — 15 items, Ch3-4
//   Patron shaped 3x3 (upgrade del T1):
//     _ I _     I = minecraft:iron_ingot
//     I A I     A = accesorio T1 (el item a mejorar)
//     _ T _     T = ingrediente tematico mejorado para T2
//
// Mapa de ingredientes tematicos:
//   Efecto           T1                          T2
//   speed            sugar                       blaze_powder
//   defense          iron_nugget                 iron_ingot
//   efficiency       redstone                    repeater
//   food             golden_carrot               honey_bottle
//   damage           flint                       blaze_rod
//   exploration      feather                     phantom_membrane
//   dungeon          bone                        bone_block
//   luck             rabbit_foot                 emerald
//   regen            glistering_melon_slice      golden_apple
//   animal           wheat                       golden_carrot
//   token            gold_nugget                 gold_ingot
//   spellpower       amethyst_shard              lapis_lazuli
//   vision           spider_eye                  ender_pearl
//   xp               lapis_lazuli                experience_bottle
//
// Materiales de slot base:
//   belt  → leather
//   back  → white_wool
//   feet  → leather
//   head  → leather

ServerEvents.recipes(event => {

    // =========================================================================
    // HELPERS
    // =========================================================================

    // Crea receta T1 shaped para un accesorio
    // slot    : 'belt' | 'back' | 'feet' | 'head'
    // effect  : nombre del efecto (ej. 'speed')
    // base    : item ID del material base del slot (ej. 'minecraft:leather')
    // tematico: item ID del ingrediente tematico del efecto
    const recetaT1 = (slot, effect, base, tematico) => {
        event.shaped(`servo_core:${slot}_${effect}_t1`, [
            'S C S',
            'T B T',
            'S C S'
        ], {
            S: 'minecraft:string',
            C: 'minecraft:copper_ingot',
            T: tematico,
            B: base
        }).id(`servo_core:acc_t1_${slot}_${effect}`);
    };

    // Crea receta T2 shaped (upgrade del T1 correspondiente)
    // slot    : igual que en T1
    // effect  : igual que en T1
    // tematico: ingrediente tematico MEJORADO para T2
    const recetaT2 = (slot, effect, tematico) => {
        event.shaped(`servo_core:${slot}_${effect}_t2`, [
            ' I ',
            'I A I',
            ' T '
        ], {
            I: 'minecraft:iron_ingot',
            A: `servo_core:${slot}_${effect}_t1`,
            T: tematico
        }).id(`servo_core:acc_t2_${slot}_${effect}`);
    };

    // =========================================================================
    // TIER 1 — BELT (4 items)
    // Base: minecraft:leather
    // =========================================================================

    // Cinturon de Velocidad: +5% velocidad cocina/cosecha/minado
    recetaT1('belt', 'speed',      'minecraft:leather', 'minecraft:sugar');
    // Cinturon de Defensa: +5% armor/resistance
    recetaT1('belt', 'defense',    'minecraft:leather', 'minecraft:iron_nugget');
    // Cinturon de Eficiencia: +5% velocidad maquinas Create
    recetaT1('belt', 'efficiency', 'minecraft:leather', 'minecraft:redstone');
    // Cinturon de Comida: +5% saturacion/nutricion
    recetaT1('belt', 'food',       'minecraft:leather', 'minecraft:golden_carrot');

    // =========================================================================
    // TIER 1 — BACK (4 items)
    // Base: minecraft:white_wool
    // =========================================================================

    // Capa de Dano: +5% dano
    recetaT1('back', 'damage',      'minecraft:white_wool', 'minecraft:flint');
    // Capa de Exploracion: +5% velocidad de movimiento
    recetaT1('back', 'exploration', 'minecraft:white_wool', 'minecraft:feather');
    // Capa de Dungeon: +5% loot en dungeon
    recetaT1('back', 'dungeon',     'minecraft:white_wool', 'minecraft:bone');
    // Capa de Velocidad: +5% velocidad general (alternativa en Back)
    recetaT1('back', 'speed',       'minecraft:white_wool', 'minecraft:sugar');

    // =========================================================================
    // TIER 1 — FEET (4 items)
    // Base: minecraft:leather
    // =========================================================================

    // Zapatos de Suerte: +5% chance extra drops
    recetaT1('feet', 'luck',   'minecraft:leather', 'minecraft:rabbit_foot');
    // Zapatos de Regeneracion: +5% regen/lifesteal
    recetaT1('feet', 'regen',  'minecraft:leather', 'minecraft:glistering_melon_slice');
    // Zapatos Animales: +5% breeding/taming speed
    recetaT1('feet', 'animal', 'minecraft:leather', 'minecraft:wheat');
    // Zapatos de Token: +5% tokens ganados
    recetaT1('feet', 'token',  'minecraft:leather', 'minecraft:gold_nugget');

    // =========================================================================
    // TIER 1 — HEAD (3 items)
    // Base: minecraft:leather
    // =========================================================================

    // Sombrero de Spell Power: +5% poder de hechizos
    recetaT1('head', 'spellpower', 'minecraft:leather', 'minecraft:amethyst_shard');
    // Sombrero de Vision: night vision pasivo + loot sense
    recetaT1('head', 'vision',     'minecraft:leather', 'minecraft:spider_eye');
    // Sombrero de XP: +5% experiencia ganada
    recetaT1('head', 'xp',         'minecraft:leather', 'minecraft:lapis_lazuli');

    console.log('[Servo] Accesorios T1: 15 recetas registradas (belt x4, back x4, feet x4, head x3)');

    // =========================================================================
    // TIER 2 — BELT (4 items)
    // Upgrade del T1 correspondiente + ingrediente tematico mejorado
    // =========================================================================

    // Cinturon de Velocidad T2: blaze_powder (velocidad + fuego)
    recetaT2('belt', 'speed',      'minecraft:blaze_powder');
    // Cinturon de Defensa T2: iron_ingot (mas hierro = mas defensa)
    recetaT2('belt', 'defense',    'minecraft:iron_ingot');
    // Cinturon de Eficiencia T2: repeater (senal redstone mas potente)
    recetaT2('belt', 'efficiency', 'minecraft:repeater');
    // Cinturon de Comida T2: honey_bottle (nutricion concentrada)
    recetaT2('belt', 'food',       'minecraft:honey_bottle');

    // =========================================================================
    // TIER 2 — BACK (4 items)
    // =========================================================================

    // Capa de Dano T2: blaze_rod (poder destructivo)
    recetaT2('back', 'damage',      'minecraft:blaze_rod');
    // Capa de Exploracion T2: phantom_membrane (movilidad extrema)
    recetaT2('back', 'exploration', 'minecraft:phantom_membrane');
    // Capa de Dungeon T2: bone_block (esencia de mazmorra concentrada)
    recetaT2('back', 'dungeon',     'minecraft:bone_block');
    // Capa de Velocidad T2: blaze_powder
    recetaT2('back', 'speed',       'minecraft:blaze_powder');

    // =========================================================================
    // TIER 2 — FEET (4 items)
    // =========================================================================

    // Zapatos de Suerte T2: emerald (comercio = suerte)
    recetaT2('feet', 'luck',   'minecraft:emerald');
    // Zapatos de Regeneracion T2: golden_apple (regeneracion poderosa)
    recetaT2('feet', 'regen',  'minecraft:golden_apple');
    // Zapatos Animales T2: golden_carrot (zanahoria dorada = breeding mejorado)
    recetaT2('feet', 'animal', 'minecraft:golden_carrot');
    // Zapatos de Token T2: gold_ingot (mas oro = mas tokens)
    recetaT2('feet', 'token',  'minecraft:gold_ingot');

    // =========================================================================
    // TIER 2 — HEAD (3 items)
    // =========================================================================

    // Sombrero de Spell Power T2: lapis_lazuli (encantamiento = magia)
    recetaT2('head', 'spellpower', 'minecraft:lapis_lazuli');
    // Sombrero de Vision T2: ender_pearl (ver a traves de dimensiones)
    recetaT2('head', 'vision',     'minecraft:ender_pearl');
    // Sombrero de XP T2: experience_bottle (experiencia embotellada)
    recetaT2('head', 'xp',         'minecraft:experience_bottle');

    console.log('[Servo] Accesorios T2: 15 recetas registradas (belt x4, back x4, feet x4, head x3)');
    console.log('[Servo] Accesorios total: 30 recetas. Items servo_core no disponibles seran ignorados silenciosamente hasta que el mod este cargado.');
});
