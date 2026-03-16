# Cadenas de Recetas - 4 Workstations de servo_cooking

> Documento de diseno: cadenas completas de sub-ingredientes para las ~145 recetas
> de las 4 workstations nuevas (Prep Station, Licuadora, Wok, Baker's Oven).
>
> Regla: las recetas finales requieren SUB-INGREDIENTES ya procesados en otras workstations,
> NO ingredientes crudos directos (salvo excepciones simples como ensaladas basicas).

---

## Leyenda de workstations previas

| Abreviatura | Workstation | Mod |
|---|---|---|
| **CB** | Cutting Board | Farmer's Delight |
| **Furnace** | Furnace / Smoker | Vanilla |
| **CP** | Cooking Pot | Farmer's Delight |
| **Keg** | Keg | Brewin' & Chewin' |
| **Cask** | Cask | Expanded Delight |
| **Create** | Millstone / Mixer / Press | Create (via servo_create) |
| **Craft** | Crafting Table | Vanilla (shapeless) |
| **Juicer** | Juicer | Expanded Delight |

Ingredientes marcados como `[crudo]` son cosechados/obtenidos directamente (crops, drops de mobs, items vanilla). No necesitan procesado previo.

---

## WORKSTATION A: PREP STATION (57 recetas)

Mecanica: Superficie de ensamblaje frio. 4 slots de ingredientes -> 1 output.
Capitulo: Ch1 (disponible desde el inicio).
Create compat: Deployer en secuencia.

### --- SANDWICHES (21 recetas) ---

### 1. Bacon Sandwich -> Prep Station
```
Cadena:
1. CB: porkchop -> raw_bacon (ya existe en FD)
2. Furnace: raw_bacon -> cooked_bacon (ya existe en FD)
3. Prep Station: bread + cooked_bacon + leafy_green [crudo] + tomato [crudo] -> Bacon Sandwich
```

### 2. Chicken Sandwich -> Prep Station
```
Cadena:
1. CB: chicken -> chicken_cuts (ya existe en FD)
2. Furnace: chicken_cuts -> cooked_chicken_cuts (ya existe en FD)
3. Prep Station: bread + cooked_chicken_cuts + leafy_green [crudo] + carrot [crudo] -> Chicken Sandwich
```

### 3. Egg Sandwich -> Prep Station
```
Cadena:
1. Furnace: egg -> fried_egg (ya existe en FD)
2. Prep Station: bread + fried_egg x2 + leafy_green [crudo] -> Egg Sandwich
```

### 4. Hamburger -> Prep Station
```
Cadena:
1. CB: beef -> minced_beef x2 (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. Prep Station: bread + beef_patty + leafy_green [crudo] + tomato [crudo] -> Hamburger
```

### 5. Mutton Wrap -> Prep Station
```
Cadena:
1. CB: mutton -> mutton_chops (ya existe en FD)
2. Furnace: mutton_chops -> cooked_mutton_chops (ya existe en FD)
3. Prep Station: bread + cooked_mutton_chops + leafy_green [crudo] + onion [crudo] -> Mutton Wrap
```

### 6. Ham and Cheese Sandwich -> Prep Station
```
Cadena:
1. Furnace: ham -> smoked_ham (ya existe en FD)
2. Keg: milk -> flaxen_cheese_wheel (ya existe en B&C)
3. CB: flaxen_cheese_wheel -> cheese_wedge x4 (ya existe en B&C)
4. Prep Station: bread + smoked_ham + cheese_wedge x2 -> Ham and Cheese Sandwich x2
```

### 7. Cheese Sandwich -> Prep Station
```
Cadena:
1. Cask: milk -> cheese_wheel (ya existe en ED)
2. CB: cheese_wheel -> cheese_slice x4 (ya existe en ED)
3. Prep Station: bread + cheese_slice -> Cheese Sandwich
```

### 8. Peanut Butter Sandwich -> Prep Station
```
Cadena:
1. Create/CP: peanut -> peanut_butter (ya existe en ED Cooking Pot / Create Millstone)
2. Prep Station: bread + peanut_butter -> Peanut Butter Sandwich
```

### 9. PB & Honey Sandwich -> Prep Station
```
Cadena:
1. Create/CP: peanut -> peanut_butter (ya existe en ED/Create)
2. Prep Station: bread + peanut_butter + honey_bottle [crudo] -> PB & Honey Sandwich
```

### 10. PB & Glow Berry Jelly Sandwich -> Prep Station
```
Cadena:
1. Create/CP: peanut -> peanut_butter (ya existe en ED/Create)
2. CP: glow_berries + sugar + bowl -> glow_berry_jelly (ya existe en ED)
3. Prep Station: bread + peanut_butter + glow_berry_jelly -> PB & Glow Berry Jelly Sandwich
```

### 11. PB & Sweet Berry Jelly Sandwich -> Prep Station
```
Cadena:
1. Create/CP: peanut -> peanut_butter (ya existe en ED/Create)
2. CP: sweet_berries + sugar + bowl -> sweet_berry_jelly (ya existe en ED)
3. Prep Station: bread + peanut_butter + sweet_berry_jelly -> PB & Sweet Berry Jelly Sandwich
```

### 12. Cranberry Jelly Sandwich -> Prep Station
```
Cadena:
1. CP: cranberries + sugar + bowl -> cranberry_jelly (ya existe en ED)
2. Prep Station: bread + cranberry_jelly -> Cranberry Jelly Sandwich
```

### 13. Cheeseburger -> Prep Station
```
Cadena:
1. CB: beef -> minced_beef x2 (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Prep Station: bread + beef_patty + cheese + lettuce [crudo] + tomato [crudo] -> Cheeseburger
```

### 14. Tofuburger -> Prep Station
```
Cadena:
1. Create: soybean -> tofu (ya existe en Croptopia/Create Press)
2. Prep Station: bread + tofu + lettuce [crudo] + tomato [crudo] -> Tofuburger
```

### 15. Ham Sandwich -> Prep Station
```
Cadena:
1. Furnace: ham -> smoked_ham (ya existe en FD)
2. Prep Station: bread + smoked_ham + lettuce [crudo] + tomato [crudo] -> Ham Sandwich
```

### 16. BLT -> Prep Station
```
Cadena:
1. CB: porkchop -> raw_bacon (ya existe en FD)
2. Furnace: raw_bacon -> cooked_bacon (ya existe en FD)
3. Prep Station: bread + cooked_bacon + lettuce [crudo] + tomato [crudo] -> BLT
```

### 17. PB & J -> Prep Station
```
Cadena:
1. Create/CP: peanut -> peanut_butter (ya existe en ED/Create)
2. CP: [fruta] + sugar -> jam (ya existe en Croptopia, movido a CP)
3. Prep Station: bread + peanut_butter + jam -> PB&J
```

### 18. Croque Madame -> Prep Station
```
Cadena:
1. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
2. Furnace: ham -> smoked_ham (ya existe en FD)
3. Furnace: egg -> fried_egg (ya existe en FD)
4. Prep Station: bread + cheese + smoked_ham + fried_egg -> Croque Madame
```

### 19. Croque Monsieur -> Prep Station
```
Cadena:
1. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
2. Furnace: ham -> smoked_ham (ya existe en FD)
3. Prep Station: bread + cheese + smoked_ham -> Croque Monsieur
```

### 20. Grilled Cheese -> Prep Station
```
Cadena:
1. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Prep Station: bread + cheese + butter -> Grilled Cheese
```

### 21. Toast Sandwich -> Prep Station
```
Cadena:
1. Furnace: bread -> toast (ya existe en Croptopia smelting)
2. Prep Station: bread + toast + lettuce [crudo] -> Toast Sandwich x2
```

### --- TOASTS (5 recetas) ---

### 22. Cranberry Goat Cheese Toast -> Prep Station
```
Cadena:
1. Furnace: bread -> toast (ya existe en Croptopia smelting)
2. Cask: goat_milk -> goat_cheese_wheel (ya existe en ED)
3. CB: goat_cheese_wheel -> goat_cheese_slice (ya existe en ED)
4. Prep Station: toast + goat_cheese_slice + cranberry [crudo] -> Cranberry Goat Cheese Toast x2
```

### 23. Ajvar Toast -> Prep Station
```
Cadena:
1. Furnace: bread -> toast (ya existe en Croptopia smelting)
2. CP: pepper + eggplant + garlic -> ajvar (ya existe en Croptopia, movido a CP)
3. Prep Station: toast + ajvar -> Ajvar Toast
```

### 24. Avocado Toast -> Prep Station
```
Cadena:
1. Furnace: bread -> toast (ya existe en Croptopia smelting)
2. Prep Station: toast + avocado [crudo] -> Avocado Toast
```

### 25. Buttered Toast -> Prep Station
```
Cadena:
1. Furnace: bread -> toast (ya existe en Croptopia smelting)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Prep Station: toast + butter -> Buttered Toast
```

### 26. Toast with Jam -> Prep Station
```
Cadena:
1. Furnace: bread -> toast (ya existe en Croptopia smelting)
2. CP: [fruta] + sugar -> jam (ya existe en Croptopia, movido a CP)
3. Prep Station: toast + jam -> Toast with Jam
```

### --- SUSHI & ROLLS (5 recetas) ---

### 27. Cod Roll -> Prep Station
```
Cadena:
1. CB: cod -> cod_slice x2 (ya existe en FD)
2. CP: rice + water -> cooked_rice (ya existe en FD)
3. Prep Station: cooked_rice + cod_slice x2 -> Cod Roll x2
```

### 28. Salmon Roll -> Prep Station
```
Cadena:
1. CB: salmon -> salmon_slice x2 (ya existe en FD)
2. CP: rice + water -> cooked_rice (ya existe en FD)
3. Prep Station: cooked_rice + salmon_slice x2 -> Salmon Roll x2
```

### 29. Kelp Roll -> Prep Station
```
Cadena:
1. CB: salmon -> salmon_slice (ya existe en FD)
2. CP: rice + water -> cooked_rice (ya existe en FD)
3. Prep Station: dried_kelp [crudo] + salmon_slice + cooked_rice -> Kelp Roll
```

### 30. Sushi -> Prep Station
```
Cadena:
1. CB: cod/salmon -> fish_slice (ya existe en FD)
2. CP: rice + water -> cooked_rice (ya existe en FD)
3. Prep Station: cooked_rice + sea_lettuce [crudo] + fish_slice -> Sushi
```

### 31. Tuna Roll -> Prep Station
```
Cadena:
1. Furnace: tuna -> cooked_tuna (ya existe en Croptopia)
2. CP: rice + water -> cooked_rice (ya existe en FD)
3. Prep Station: cooked_rice + sea_lettuce [crudo] + cooked_tuna -> Tuna Roll x2
```

### --- WRAPS MEXICANOS (9 recetas) ---

### 32. Burrito -> Prep Station
```
Cadena:
1. CP: rice + water -> cooked_rice (ya existe en FD)
2. CP: blackbean + water -> refried_beans (ya existe en Croptopia, movido a CP)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Prep Station: tortilla [crudo/craft] + refried_beans + cooked_rice + cheese -> Burrito
```

### 33. Taco -> Prep Station
```
Cadena:
1. CB: beef -> minced_beef (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Prep Station: tortilla + beef_patty + cheese + lettuce [crudo] + tomato [crudo] -> Taco
```

### 34. Tostada -> Prep Station
```
Cadena:
1. CP: blackbean + water -> refried_beans (ya existe en Croptopia, movido a CP)
2. Prep Station: tortilla + refried_beans + lettuce [crudo] + tomato [crudo] -> Tostada
```

### 35. Fajitas -> Prep Station
```
Cadena:
1. CB: beef -> minced_beef (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. Prep Station: tortilla + beef_patty + bellpepper [crudo] + onion [crudo] -> Fajitas x2
```

### 36. Quesadilla -> Prep Station
```
Cadena:
1. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
2. Prep Station: tortilla + cheese x2 -> Quesadilla x2
```

### 37. Enchilada -> Prep Station
```
Cadena:
1. CB: chicken -> chicken_cuts (ya existe en FD)
2. Furnace: chicken_cuts -> cooked_chicken_cuts (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. CP: tomato + chile_pepper -> salsa (ya existe en Croptopia, movido a CP)
5. Prep Station: tortilla + cooked_chicken_cuts + cheese + salsa -> Enchilada x2
```

### 38. Chimichanga -> Prep Station
```
Cadena:
1. CB: beef -> minced_beef (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. CP: rice + water -> cooked_rice (ya existe en FD)
4. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
5. Prep Station: tortilla + beef_patty + cooked_rice + cheese -> Chimichanga
```

### 39. Chili Relleno -> Prep Station
```
Cadena:
1. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
2. Furnace: egg -> fried_egg (ya existe en FD)
3. CP: tomato + chile_pepper -> salsa (ya existe en Croptopia, movido a CP)
4. Prep Station: pepper [crudo] + cheese + fried_egg + salsa -> Chili Relleno
```

### 40. Stuffed Poblanos -> Prep Station
```
Cadena:
1. CB: beef -> minced_beef (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. CP: rice + water -> cooked_rice (ya existe en FD)
4. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
5. Prep Station: pepper [crudo] + beef_patty + cooked_rice + cheese -> Stuffed Poblanos
```

### --- ENSALADAS (10 recetas) ---

### 41. Mixed Salad -> Prep Station
```
Cadena:
1. Prep Station: leafy_green [crudo] + tomato [crudo] + beetroot [crudo] + bowl -> Mixed Salad
Nota: Ensalada simple, sin procesado previo requerido.
```

### 42. Fruit Salad (FD) -> Prep Station
```
Cadena:
1. CB: melon -> melon_slice x9 (ya existe en FD/vanilla)
2. Prep Station: apple [crudo] + melon_slice + sweet_berries [crudo] + bowl -> Fruit Salad
```

### 43. Nether Salad -> Prep Station
```
Cadena:
1. Prep Station: warped_fungus [crudo] + crimson_fungus [crudo] + bowl -> Nether Salad
Nota: Ingredientes del Nether, sin procesado previo.
```

### 44. Peanut Salad -> Prep Station
```
Cadena:
1. Furnace: peanut -> roasted_nuts (ya existe en Croptopia smelting)
2. Prep Station: roasted_nuts + leafy_green [crudo] + bowl -> Peanut Salad
```

### 45. Sweet Potato Salad -> Prep Station
```
Cadena:
1. Furnace: sweet_potato -> baked_sweet_potato (ya existe en ED/Croptopia)
2. Prep Station: baked_sweet_potato + leafy_green [crudo] + bowl -> Sweet Potato Salad
```

### 46. Goat Cheese Beetroot Salad -> Prep Station
```
Cadena:
1. Cask: goat_milk -> goat_cheese_wheel (ya existe en ED)
2. CB: goat_cheese_wheel -> goat_cheese_slice (ya existe en ED)
3. Prep Station: goat_cheese_slice + beetroot [crudo] + leafy_green [crudo] + bowl -> Goat Cheese Beetroot Salad
```

### 47. Caesar Salad -> Prep Station
```
Cadena:
1. Furnace: anchovy -> cooked_anchovy (ya existe en Croptopia)
2. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
3. Furnace: bread -> toast (ya existe en Croptopia) [funciona como crouton]
4. Prep Station: lettuce [crudo] + cheese + toast + cooked_anchovy -> Caesar Salad
```

### 48. Cucumber Salad -> Prep Station
```
Cadena:
1. Prep Station: cucumber [crudo] + onion [crudo] + bowl -> Cucumber Salad
Nota: Ensalada simple, ingredientes frescos.
```

### 49. Beetroot Salad -> Prep Station
```
Cadena:
1. Prep Station: beetroot [crudo] + onion [crudo] + leafy_green [crudo] + bowl -> Beetroot Salad
Nota: Ensalada simple.
```

### 50. Leafy Salad -> Prep Station
```
Cadena:
1. Prep Station: lettuce [crudo] x2 + tomato [crudo] + bowl -> Leafy Salad
Nota: La ensalada mas basica.
```

### 51. Veggie Salad -> Prep Station
```
Cadena:
1. Prep Station: lettuce [crudo] + carrot [crudo] + tomato [crudo] + cucumber [crudo] -> Veggie Salad
```

### 52. Fruit Salad (Croptopia) -> Prep Station
```
Cadena:
1. CB: melon -> melon_slice (ya existe en FD/vanilla)
2. Prep Station: apple [crudo] + melon_slice + banana [crudo] + sweet_berries [crudo] -> Fruit Salad (Croptopia)
```

### --- PLATOS CON CARNE (3 recetas) ---

### 53. Steak and Potatoes -> Prep Station
```
Cadena:
1. Furnace: beef -> cooked_beef (vanilla smelting)
2. Furnace: potato -> baked_potato (vanilla smelting)
3. Prep Station: cooked_beef + baked_potato + bowl -> Steak and Potatoes
```

### 54. Stuffed Potato -> Prep Station
```
Cadena:
1. Furnace: potato -> baked_potato (vanilla smelting)
2. Furnace: beef -> cooked_beef (vanilla smelting)
3. Prep Station: baked_potato + cooked_beef + milk_bottle -> Stuffed Potato
```

### 55. Roasted Mutton Chops -> Prep Station
```
Cadena:
1. CB: mutton -> mutton_chops (ya existe en FD)
2. Furnace: mutton_chops -> cooked_mutton_chops (ya existe en FD)
3. Prep Station: cooked_mutton_chops + tomato [crudo] + onion [crudo] + bowl -> Roasted Mutton Chops
```

### --- OTROS (2 recetas) ---

### 56. Egg Roll -> Prep Station
```
Cadena:
1. Create/Craft: flour + egg -> dough (ya existe en Croptopia/FD)
2. Create/CP: soybean -> soy_sauce (ya existe en Croptopia, movido a CP)
3. Prep Station: dough + cabbage [crudo] + carrot [crudo] + soy_sauce -> Egg Roll
```

### 57. Trail Mix -> Prep Station
```
Cadena:
1. Furnace: peanut/walnut -> roasted_nuts (ya existe en Croptopia)
2. Furnace: grape -> raisins (ya existe en Croptopia)
3. Create: cocoa_beans + milk + sugar -> chocolate (ya existe en Croptopia/Create Mixer)
4. Prep Station: roasted_nuts + raisins + chocolate -> Trail Mix x4
```

---

## WORKSTATION B: LICUADORA (20 recetas)

Mecanica: Slots de ingredientes + 1 slot de liquido base (agua, leche, jugo). Tiempo: 60 ticks (3 seg).
Capitulo: Ch2.
Create compat: Basin + Mixer.

### --- JUGOS (10 recetas) ---

### 1. Melon Juice -> Licuadora
```
Cadena:
1. CB: melon -> melon_slice x9 (ya existe en FD/vanilla)
2. Licuadora: melon_slice x4 + water_bottle -> Melon Juice
```

### 2. Apple Juice -> Licuadora
```
Cadena:
1. Licuadora: apple [crudo] x2 + water_bottle -> Apple Juice
Nota: Distinto del Juicer ED (1 apple, sin agua). Licuadora version rinde mas pero necesita agua.
```

### 3. Cranberry Juice -> Licuadora
```
Cadena:
1. Licuadora: cranberry [crudo] x3 + water_bottle -> Cranberry Juice
```

### 4. Grape Juice -> Licuadora
```
Cadena:
1. Licuadora: grape [crudo] x4 + water_bottle -> Grape Juice
```

### 5. Lemonade -> Licuadora
```
Cadena:
1. Licuadora: lemon [crudo] x2 + sugar [crudo] + water_bottle -> Lemonade
```

### 6. Limeade -> Licuadora
```
Cadena:
1. Licuadora: lime [crudo] x2 + sugar [crudo] + water_bottle -> Limeade
```

### 7. Orange Juice -> Licuadora
```
Cadena:
1. Licuadora: orange [crudo] x2 + water_bottle -> Orange Juice
```

### 8. Pineapple Juice -> Licuadora
```
Cadena:
1. Licuadora: pineapple [crudo] x2 + water_bottle -> Pineapple Juice
```

### 9. Saguaro Juice -> Licuadora
```
Cadena:
1. Licuadora: saguaro [crudo] x2 + water_bottle -> Saguaro Juice
```

### 10. Tomato Juice -> Licuadora
```
Cadena:
1. CP: tomato -> tomato_sauce (ya existe en FD)
2. Licuadora: tomato_sauce + water_bottle -> Tomato Juice
```

### --- SMOOTHIES (3 recetas) ---

### 11. Banana Smoothie -> Licuadora
```
Cadena:
1. Licuadora: banana [crudo] x2 + milk_bottle -> Banana Smoothie
```

### 12. Fruit Smoothie -> Licuadora
```
Cadena:
1. Licuadora: apple [crudo] + banana [crudo] + sweet_berries [crudo] + milk_bottle -> Fruit Smoothie x2
```

### 13. Kale Smoothie -> Licuadora
```
Cadena:
1. Licuadora: kale [crudo] x2 + banana [crudo] + milk_bottle -> Kale Smoothie
```

### --- MILKSHAKE (1 receta) ---

### 14. Chocolate Milkshake -> Licuadora
```
Cadena:
1. Create: cocoa_beans + milk + sugar -> chocolate (ya existe en Croptopia/Create Mixer)
2. Licuadora: chocolate + milk_bottle x2 + sugar -> Chocolate Milkshake
```

### --- BEBIDAS CALIENTES (3 recetas) ---

### 15. Coffee -> Licuadora
```
Cadena:
1. Licuadora: coffee_beans [crudo] + water_bottle + sugar -> Coffee
Nota: coffee_beans se usan directos. El proceso de "tostado" ocurre dentro de la licuadora.
```

### 16. Tea -> Licuadora
```
Cadena:
1. Licuadora: tea_leaves [crudo] + water_bottle + honey_bottle -> Tea
```

### 17. Pumpkin Spice Latte -> Licuadora
```
Cadena:
1. CB: pumpkin -> pumpkin_slice x4 (ya existe en FD)
2. CB: cinnamon_log -> cinnamon (ya existe en ED)
3. Licuadora: pumpkin_slice + coffee_beans + milk_bottle + cinnamon -> Pumpkin Spice Latte
```

### --- OTRAS BEBIDAS (3 recetas) ---

### 18. Horchata -> Licuadora
```
Cadena:
1. CP: rice + water -> cooked_rice (ya existe en FD)
2. CB: cinnamon_log -> cinnamon (ya existe en ED)
3. Licuadora: cooked_rice + milk_bottle + cinnamon + sugar -> Horchata
```

### 19. Soy Milk -> Licuadora
```
Cadena:
1. Licuadora: soybean [crudo] x2 + water_bottle -> Soy Milk
```

### 20. Melon Juice (Croptopia) -> Licuadora
```
Nota: Duplicado con #1 (FD melon_juice). Se unifica en una sola receta.
Mismo output que receta #1. Se elimina el duplicado de Croptopia via KubeJS.
```

---

## WORKSTATION C: WOK (21 recetas)

Mecanica: GUI con 3 slots de ingredientes + barra de calor con control de temperatura activo (bajo/medio/alto).
Capitulo: Ch2.
Create compat: Deployer + Blaze Burner.

### --- CARNES Y HUEVOS (6 recetas) ---

### 1. Barbecue on a Stick -> Wok
```
Cadena:
1. CB: beef -> minced_beef (ya existe en FD)
2. Furnace: minced_beef -> beef_patty (ya existe en FD)
3. Wok: beef_patty + tomato [crudo] + onion [crudo] + stick [crudo] -> Barbecue on a Stick
```

### 2. Bacon and Eggs -> Wok
```
Cadena:
1. CB: porkchop -> raw_bacon (ya existe en FD)
2. Wok: raw_bacon x2 + egg x2 + bowl -> Bacon and Eggs
Nota: El Wok COCINA el bacon y huevos in-situ (freir). No requieren pre-cocinado.
```

### 3. Grilled Salmon -> Wok
```
Cadena:
1. CB: salmon -> salmon_slice x2 (ya existe en FD)
2. Wok: salmon_slice x2 + sweet_berries [crudo] + bowl -> Grilled Salmon
Nota: El Wok asa el salmon crudo. No necesita pre-coccion.
```

### 4. Grilled Cheese (ED) -> Wok
```
Cadena:
1. Cask: milk -> cheese_wheel (ya existe en ED)
2. CB: cheese_wheel -> cheese_slice (ya existe en ED)
3. Prep Station: bread + cheese_slice -> cheese_sandwich (pre-ensamblado)
4. Wok: cheese_sandwich -> Grilled Cheese (ED)
Nota: Requiere pre-ensamblar el sandwich en Prep Station, luego asarlo en Wok. 2 workstations!
```

### 5. Fried Chicken -> Wok
```
Cadena:
1. CB: chicken -> chicken_cuts x2 (ya existe en FD)
2. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
3. Wok: chicken_cuts + flour + egg -> Fried Chicken
```

### 6. Scrambled Eggs -> Wok
```
Cadena:
1. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
2. Wok: egg x2 + butter + bowl -> Scrambled Eggs
```

### --- FRITURAS DE PAPA (4 recetas) ---

### 7. French Fries -> Wok
```
Cadena:
1. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
2. Wok: potato [crudo] + olive_oil -> French Fries
```

### 8. Sweet Potato Fries -> Wok
```
Cadena:
1. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
2. Wok: sweet_potato [crudo] + olive_oil -> Sweet Potato Fries
```

### 9. Potato Chips -> Wok
```
Cadena:
1. CB: salt_rock -> salt (ya existe en ED)
2. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
3. Wok: potato [crudo] + olive_oil + salt -> Potato Chips
```

### 10. Hashed Brown -> Wok
```
Cadena:
1. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
2. Wok: potato [crudo] x2 + olive_oil + onion [crudo] -> Hashed Brown x4
```

### --- FRITURAS VEGETALES (3 recetas) ---

### 11. Onion Rings -> Wok
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Wok: onion [crudo] + flour + egg -> Onion Rings
```

### 12. Kale Chips -> Wok
```
Cadena:
1. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
2. CB: salt_rock -> salt (ya existe en ED)
3. Wok: kale [crudo] + olive_oil + salt -> Kale Chips
```

### 13. Grilled Eggplant -> Wok
```
Cadena:
1. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
2. Wok: eggplant [crudo] + olive_oil -> Grilled Eggplant
```

### --- MARISCOS (5 recetas) ---

### 14. Fried Calamari -> Wok
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Wok: calamari [crudo] + flour + egg -> Fried Calamari x2
```

### 15. Deep Fried Shrimp -> Wok
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Wok: shrimp [crudo] + flour + egg -> Deep Fried Shrimp x2
```

### 16. Grilled Oysters -> Wok
```
Cadena:
1. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
2. Wok: oyster [crudo] x2 + butter -> Grilled Oysters x2
```

### 17. Crab Legs -> Wok
```
Cadena:
1. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
2. Wok: crab [crudo] + butter + lemon [crudo] -> Crab Legs x2
```

### 18. Steamed Clams -> Wok
```
Cadena:
1. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
2. Wok: clam [crudo] x2 + butter + water_bottle -> Steamed Clams x2
```

### 19. Steamed Crab -> Wok
```
Cadena:
1. CB: salt_rock -> salt (ya existe en ED)
2. Wok: crab [crudo] + water_bottle + salt -> Steamed Crab
```

### --- HUEVOS ESPECIALES (2 recetas) ---

### 20. Sunny Side Eggs -> Wok
```
Cadena:
1. Create: olive -> olive_oil (ya existe en Croptopia/Create Press)
2. Wok: egg x2 + olive_oil -> Sunny Side Eggs x2
```

### 21. Dragon Egg Omelette -> Wok
```
Cadena:
1. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Wok: dragon_egg [crudo, 1 uso especial] + cheese + butter + bowl -> Dragon Egg Omelette
Nota: Plato endgame, dragon egg como ingrediente novelty.
```

---

## WORKSTATION D: BAKER'S OVEN (47 recetas)

Mecanica: GUI con 4 slots de ingredientes + 2 modos (Hornear / Gratinar). Requiere calor debajo.
Capitulo: Ch3.
Create compat: Mixer Heated via Slice & Dice.

### --- COOKIES (7 recetas) ---

### 1. Honey Cookie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + butter + honey_bottle + egg -> Honey Cookie x8
```

### 2. Sweet Berry Cookie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + butter + sweet_berries [crudo] + egg -> Sweet Berry Cookie x8
```

### 3. Chocolate Cookie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: cocoa_beans + milk + sugar -> chocolate (ya existe en Croptopia/Create Mixer)
3. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: flour + butter + chocolate + egg -> Chocolate Cookie x8
```

### 4. Snickerdoodle (ED) -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. CB: cinnamon_log -> cinnamon (ya existe en ED)
4. Baker's Oven: flour + butter + cinnamon + sugar -> Snickerdoodle x8
```

### 5. Sugar Cookie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + butter + sugar x2 -> Sugar Cookie x8
```

### 6. Nutty Cookie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Furnace: peanut/walnut -> roasted_nuts (ya existe en Croptopia)
4. Baker's Oven: flour + butter + roasted_nuts + egg -> Nutty Cookie x4
```

### 7. Raisin Oatmeal Cookie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Furnace: grape -> raisins (ya existe en Croptopia)
4. Baker's Oven: flour + butter + raisins + oat [crudo] -> Raisin Oatmeal Cookie x4
```

### --- PIES (5 recetas) ---

### 8. Apple Pie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Baker's Oven: pie_crust + apple [crudo] x2 + sugar -> Apple Pie
```

### 9. Cherry Pie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Baker's Oven: pie_crust + cherry [crudo] x2 + sugar -> Cherry Pie
```

### 10. Pecan Pie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Furnace: sugar_cane -> molasses (ya existe en Croptopia)
5. Baker's Oven: pie_crust + pecan [crudo] + egg + molasses -> Pecan Pie
```

### 11. Rhubarb Pie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Baker's Oven: pie_crust + rhubarb [crudo] x2 + sugar -> Rhubarb Pie
```

### 12. Banana Cream Pie -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Create: milk -> whipping_cream x4 (ya existe en Croptopia/Create Mixer)
5. Baker's Oven: pie_crust + banana [crudo] + whipping_cream + sugar -> Banana Cream Pie
```

### --- PIZZAS (5 recetas) ---

### 13. Pizza (B&C) -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CP: tomato -> tomato_sauce (ya existe en FD)
3. Keg: milk -> flaxen_cheese_wheel (ya existe en B&C)
4. CB: flaxen_cheese_wheel -> cheese_wedge (ya existe en B&C)
5. Baker's Oven: dough + tomato_sauce + cheese_wedge + basil [crudo] -> Pizza (B&C)
```

### 14. Cheese Pizza -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CP: tomato -> tomato_sauce (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Baker's Oven: dough + tomato_sauce + cheese x2 -> Cheese Pizza
```

### 15. Anchovy Pizza -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CP: tomato -> tomato_sauce (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Furnace: anchovy -> cooked_anchovy (ya existe en Croptopia)
5. Baker's Oven: dough + tomato_sauce + cheese + cooked_anchovy -> Anchovy Pizza
```

### 16. Supreme Pizza -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CP: tomato -> tomato_sauce (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Create: ground_pork -> pepperoni (ya existe en Croptopia/Create Mixer)
5. Baker's Oven: dough + tomato_sauce + cheese + pepperoni -> Supreme Pizza
```

### 17. Pineapple Pepperoni Pizza -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CP: tomato -> tomato_sauce (ya existe en FD)
3. Create: ground_pork -> pepperoni (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: dough + tomato_sauce + pepperoni + pineapple [crudo] -> Pineapple Pepperoni Pizza
```

### --- CHEESECAKE / TARTS (3 recetas) ---

### 18. Sweet Berry Cheesecake -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Baker's Oven: flour + butter + cheese + sweet_berries [crudo] -> Sweet Berry Cheesecake
```

### 19. Honeyed Goat Cheese Tart -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Cask: goat_milk -> goat_cheese_wheel (ya existe en ED)
4. CB: goat_cheese_wheel -> goat_cheese_slice (ya existe en ED)
5. Baker's Oven: flour + butter + goat_cheese_slice + honey_bottle -> Honeyed Goat Cheese Tart
```

### 20. Quiche -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
5. CB: porkchop -> raw_bacon (ya existe en FD)
6. Furnace: raw_bacon -> cooked_bacon (ya existe en FD)
7. Baker's Oven: pie_crust + egg + cheese + cooked_bacon -> Quiche
```

### --- SWEET ROLLS (3 recetas) ---

### 21. Sweet Roll -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: dough + butter + sugar + egg -> Sweet Roll
```

### 22. Berry Sweet Roll -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: dough + butter + sweet_berries [crudo] + sugar -> Berry Sweet Roll
```

### 23. Glow Berry Sweet Roll -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: dough + butter + glow_berries [crudo] + sugar -> Glow Berry Sweet Roll
```

### --- PASTELES / CAKES (4 recetas) ---

### 24. Tres Leche Cake -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> whipping_cream x4 (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + egg + milk_bottle + whipping_cream -> Tres Leche Cake x2
```

### 25. Fruit Cake -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Furnace: grape -> raisins (ya existe en Croptopia)
3. Baker's Oven: flour + egg + raisins + cherry [crudo] -> Fruit Cake x3
```

### 26. Nether Star Cake -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Create: cocoa_beans + milk + sugar -> chocolate (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: flour + butter + chocolate + nether_star [drop] -> Nether Star Cake
Nota: Receta endgame. Nether star como ingrediente raro.
```

### 27. Snicker Doodle (Croptopia) -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. CB: cinnamon_log -> cinnamon (ya existe en ED)
4. Baker's Oven: flour + butter + cinnamon + sugar -> Snicker Doodle x4
Nota: Misma receta que Snickerdoodle ED (#4). Se unifica: un solo output, se elimina duplicado via KubeJS.
```

### --- BROWNIES / BARRAS (5 recetas) ---

### 28. Brownies -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: cocoa_beans + milk + sugar -> chocolate (ya existe en Croptopia/Create Mixer)
3. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: flour + chocolate + butter + egg -> Brownies
```

### 29. Lemon Coconut Bar -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + butter + lemon [crudo] + coconut [crudo] -> Lemon Coconut Bar x2
```

### 30. Pumpkin Bars -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. CB: pumpkin -> pumpkin_slice x4 (ya existe en FD)
3. CB: cinnamon_log -> cinnamon (ya existe en ED)
4. Baker's Oven: flour + pumpkin_slice + cinnamon + egg -> Pumpkin Bars x3
```

### 31. Protein Bar -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create/CP: peanut -> peanut_butter (ya existe en ED/Create)
3. Furnace: peanut/walnut -> roasted_nuts (ya existe en Croptopia)
4. Baker's Oven: flour + peanut_butter + roasted_nuts + honey_bottle -> Protein Bar x3
```

### 32. Nougat -> Baker's Oven
```
Cadena:
1. Furnace: peanut/walnut -> roasted_nuts (ya existe en Croptopia)
2. Baker's Oven: sugar + honey_bottle + roasted_nuts + egg -> Nougat x2
```

### --- PAN / BREAD (3 recetas) ---

### 33. Corn Bread -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: corn [crudo] + flour + butter + egg -> Corn Bread
```

### 34. Banana Nut Bread -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Furnace: peanut/walnut -> roasted_nuts (ya existe en Croptopia)
3. Baker's Oven: flour + banana [crudo] + roasted_nuts + egg -> Banana Nut Bread x2
```

### 35. Scones -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Create: milk -> whipping_cream (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: flour + butter + whipping_cream + sugar -> Scones x2
```

### --- CREPES (2 recetas) ---

### 36. Baked Crepes -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + egg + butter + milk_bottle -> Baked Crepes
```

### 37. Sweet Crepes -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> whipping_cream (ya existe en Croptopia/Create Mixer)
3. CP: [fruta] + sugar -> jam (ya existe en Croptopia, movido a CP)
4. Baker's Oven: flour + egg + whipping_cream + jam -> Sweet Crepes
```

### --- CHURROS / ROLLS (2 recetas) ---

### 38. Churros -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CB: cinnamon_log -> cinnamon (ya existe en ED)
3. Baker's Oven: dough + sugar + cinnamon + egg -> Churros x3
```

### 39. Cinnamon Roll -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. CB: cinnamon_log -> cinnamon (ya existe en ED)
4. Baker's Oven: dough + butter + cinnamon + sugar -> Cinnamon Roll x3
```

### --- MACARONS / MERINGUE (2 recetas) ---

### 40. Macaron -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: flour + egg + sugar + butter -> Macaron x2
```

### 41. Meringue -> Baker's Oven
```
Cadena:
1. Baker's Oven: egg x2 + sugar x2 -> Meringue x2
Nota: Receta simple — solo requiere batir claras con azucar y hornear. Minima cadena.
```

### --- PUDDINGS / POSTRES BRITANICOS (4 recetas) ---

### 42. Treacle Tart -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
3. Craft: flour + butter -> pie_crust (ya existe en FD)
4. Furnace: sugar_cane -> molasses (ya existe en Croptopia)
5. Baker's Oven: pie_crust + molasses + egg + lemon [crudo] -> Treacle Tart x3
```

### 43. Sticky Toffee Pudding -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Furnace: sugar -> caramel (ya existe en Croptopia)
3. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: flour + caramel + butter + date [crudo] -> Sticky Toffee Pudding
```

### 44. Figgy Pudding -> Baker's Oven
```
Cadena:
1. Create/Craft: wheat -> flour (ya existe en Croptopia/Create Millstone)
2. Furnace: grape -> raisins (ya existe en Croptopia)
3. Create: milk -> butter (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: flour + fig [crudo] + raisins + butter -> Figgy Pudding
```

### 45. Eton Mess -> Baker's Oven
```
Cadena:
1. Baker's Oven (paso previo): egg + sugar -> meringue (receta #41 de este mismo oven)
2. Create: milk -> whipping_cream (ya existe en Croptopia/Create Mixer)
3. Baker's Oven: meringue + whipping_cream + strawberry [crudo] -> Eton Mess
Nota: Requiere meringue pre-horneado. Cadena de 2 pasos en Baker's Oven.
```

### 46. Trifle -> Baker's Oven
```
Cadena:
1. Baker's Oven (paso previo): dough + butter + sugar + egg -> sweet_roll (receta #21 de este oven)
2. CP: [fruta] + sugar -> jam (ya existe en Croptopia, movido a CP)
3. Create: milk -> whipping_cream (ya existe en Croptopia/Create Mixer)
4. Baker's Oven: sweet_roll + jam + whipping_cream + egg -> Trifle
Nota: Usa sweet_roll pre-horneado como base. Cadena larga de 4+ pasos totales.
```

### 47. Pizza (Croptopia) -> Baker's Oven
```
Cadena:
1. Create/Craft: flour + water -> dough (ya existe en Croptopia/FD)
2. CP: tomato -> tomato_sauce (ya existe en FD)
3. Cask/Create: milk -> cheese (ya existe en Croptopia/Create)
4. Baker's Oven: dough + tomato_sauce + cheese -> Pizza (Croptopia)
Nota: Version mas simple que la B&C pizza (#13). Se puede unificar o diferenciar por output.
```

---

## RESUMEN DE COMPLEJIDAD DE CADENAS

| Complejidad | Pasos previos | Cantidad | Ejemplos |
|---|---|---|---|
| **Simple** | 0 (solo crudos) | 8 | Mixed Salad, Nether Salad, Cucumber Salad, Beetroot Salad, Leafy Salad, Meringue, jugos simples |
| **Basica** | 1 paso | 28 | Avocado Toast, Soy Milk, Lemonade, Sweet Potato Salad, French Fries, Egg Sandwich |
| **Media** | 2 pasos | 55 | Hamburger, BLT, Bacon Sandwich, Fried Chicken, Apple Pie, Brownies, la mayoria de cookies |
| **Compleja** | 3 pasos | 38 | Cheeseburger, Enchilada, Grilled Cheese ED, Supreme Pizza, Tres Leche, Caesar Salad |
| **Elaborada** | 4+ pasos | 16 | Ham & Cheese Sandwich, Chimichanga, Pizza B&C, Honeyed Goat Cheese Tart, Eton Mess, Trifle |
| **TOTAL** | | **145** | |

## INGREDIENTES PROCESADOS MAS USADOS (top 10)

| Ingrediente procesado | Fuente | Usos en recetas |
|---|---|---|
| **flour** | Create Millstone (wheat) | ~30 (Baker's Oven, Wok) |
| **butter** | Create Mixer (milk) | ~25 (Baker's Oven, Wok) |
| **cheese** (#c:cheeses) | Cask/Create/Keg (milk) | ~18 (Prep Station, Baker's Oven) |
| **cooked_bacon** | Furnace (raw_bacon del CB) | 5 (Prep Station, Wok) |
| **beef_patty** | Furnace (minced_beef del CB) | 7 (Prep Station) |
| **tomato_sauce** | CP (tomato) | 8 (Baker's Oven pizzas, Licuadora) |
| **dough** | Craft/Create (flour + water) | 10 (Baker's Oven) |
| **pie_crust** | Craft (flour + butter) | 7 (Baker's Oven pies) |
| **cooked_rice** | CP (rice + water) | 8 (Prep Station) |
| **whipping_cream** | Create Mixer (milk) | 6 (Baker's Oven) |

---

## NOTAS DE IMPLEMENTACION

### 1. Tags unificados
Usar `#c:cheeses` para aceptar cheese de Croptopia, ED (cheese_slice), o B&C (cheese_wedge) donde aplique. Similarmente `#c:flour`, `#c:doughs`, `#c:butters`.

### 2. Recetas duplicadas a unificar
- Snickerdoodle (ED) y Snicker Doodle (Croptopia) = misma receta, un solo output
- Melon Juice FD y Croptopia = un solo output
- Pizza B&C y Pizza Croptopia = diferenciar (B&C usa basil, Croptopia no)

### 3. Tortilla como ingrediente base
Croptopia tiene `tortilla` como item crafteable (flour + water, shapeless). Se mantiene en crafting table como ingrediente base, similar a `bread`.

### 4. Create como gateway de ingredientes intermedios
Flour, butter, cheese, olive_oil, chocolate, whipping_cream, tofu, peanut_butter todos se producen via Create machines. Esto fuerza al jugador a tener al menos un setup basico de Create para acceder a recetas medias-complejas. Alinea con el pilar Create/Automatizacion.

### 5. Progresion natural por capitulo
- **Ch1 (Prep Station)**: Sandwiches y ensaladas simples con ingredientes del Furnace/CB. Minima dependencia de Create.
- **Ch2 (Licuadora + Wok)**: Requiere algunos ingredientes de Create (olive_oil, butter). Introduce cadenas de 2-3 pasos.
- **Ch3 (Baker's Oven)**: Requiere flour, butter, dough, pie_crust = cadenas de 3-5 pasos. Create obligatorio para la mayoria de recetas.

### 6. Recetas que cruzan workstations nuevas
- **Grilled Cheese ED**: Prep Station (ensamblar sandwich) -> Wok (asarlo). Unica receta que usa 2 workstations nuevas.
- **Eton Mess**: Baker's Oven (meringue) -> Baker's Oven (eton mess). Misma workstation, 2 pasadas.
- **Trifle**: Baker's Oven (sweet roll) -> Baker's Oven (trifle). Misma workstation, 2 pasadas.
