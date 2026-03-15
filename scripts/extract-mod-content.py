"""
Extract all items, blocks, and recipes from mod JARs.
Outputs a comprehensive content catalog for quest/GDD design.
Also generates tools/recipe-tree-data.js for the recipe tree web tool.
Extracts item textures as base64 for the web tool icons.
"""
import json, zipfile, os, sys, base64, glob
from collections import defaultdict

MODS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "modpack", "mods")
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "docs", "mod-data")
TOOLS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "tools")

# Only extract gameplay-relevant mods (skip libs, performance, dev tools)
GAMEPLAY_MODS = {
    "FarmersDelight": "farmersdelight",
    "BrewinAndChewin": "brewinandchewin",
    "expandeddelight": "expandeddelight",
    "croptopia": "croptopia",
    "MysticalAgriculture": "mysticalagriculture",
    "Cucumber": "cucumber",
    "create-1.21": "create",
    "createaddition": "createaddition",
    "createdeco": "createdeco",
    "create-enchantment-industry": "create_enchantment_industry",
    "sliceanddice": "sliceanddice",
    "botanypots": "botanypots",
    "alexsmobs": "alexsmobs",
    "dimdungeons": "dimdungeons",
    "StorageDrawers": "storagedrawers",
    "toms_storage": "toms_storage",
    "refinedstorage": "refinedstorage",
    "gachamachine": "gachamachine",
    "mcw-bridges": "mcwbridges",
    "mcw-roofs": "mcwroofs",
    "mcw-furniture": "mcwfurnitures",
    "mcw-mcwwindows": "mcwwindows",
    "mcw-trapdoors": "mcwtrpdoors",
    "chipped": "chipped",
    "refurbished_furniture": "refurbished_furniture",
    "curios": "curios",
    "plushables": "plushables",
    "Spice of Life": "solonion",
    "lootr": "lootr",
    "progressivestages": "progressivestages",
    # RPG Series
    "spell_engine": "spell_engine",
    "spell_power": "spell_power",
    "wizards": "wizards",
    "rogues": "rogues",
    "paladins": "paladins",
    "ranged_weapon_api": "ranged_weapon_api",
    "skill_tree": "skill_tree",
    "mrpgc_skill_tree": "mrpgc_skill_tree",
    "runes": "runes",
    "jewelry": "jewelry",
}

# Mods de cocina para el consolidado de recipe-tree-data.js
COOKING_MODS = ["farmersdelight", "brewinandchewin", "expandeddelight", "croptopia"]

def find_jar(prefix):
    for f in os.listdir(MODS_DIR):
        if f.endswith(".jar") and prefix.lower() in f.lower():
            return os.path.join(MODS_DIR, f)
    return None

def extract_lang(jar_path, mod_id):
    """Extract en_us.json lang file for item/block names"""
    items = {}
    blocks = {}
    entities = {}
    other = {}

    try:
        with zipfile.ZipFile(jar_path, 'r') as z:
            # Try common lang file paths
            for lang_path in [
                f"assets/{mod_id}/lang/en_us.json",
                f"assets/{mod_id.replace('_', '')}/lang/en_us.json",
            ]:
                if lang_path in z.namelist():
                    data = json.loads(z.read(lang_path))
                    for k, v in data.items():
                        if k.startswith("item."):
                            items[k] = v
                        elif k.startswith("block."):
                            blocks[k] = v
                        elif k.startswith("entity."):
                            entities[k] = v
                        elif "tooltip" not in k.lower() and "subtitle" not in k.lower():
                            other[k] = v
                    break
            else:
                # Try to find any lang file
                for name in z.namelist():
                    if name.endswith("en_us.json") and "lang" in name:
                        data = json.loads(z.read(name))
                        for k, v in data.items():
                            if k.startswith("item."):
                                items[k] = v
                            elif k.startswith("block."):
                                blocks[k] = v
                            elif k.startswith("entity."):
                                entities[k] = v
                        break
    except Exception as e:
        print(f"  Error reading {jar_path}: {e}")

    return items, blocks, entities


def parse_ingredient(ing):
    """Parse a single ingredient entry into a string representation.

    Returns a string like:
      "minecraft:wheat"         - specific item
      "#c:salt"                 - tag
      "minecraft:cod|minecraft:salmon"  - alternatives
    """
    if isinstance(ing, str):
        return ing
    elif isinstance(ing, dict):
        if "item" in ing:
            return ing["item"]
        elif "tag" in ing:
            return "#" + ing["tag"]
        elif "id" in ing:
            return ing["id"]
        # NeoForge 1.21 format: {"type": "neoforge:compound", ...}
        elif "type" in ing and "children" in ing:
            children = [parse_ingredient(c) for c in ing["children"]]
            return "|".join(children)
        # Fallback
        return str(ing)
    elif isinstance(ing, list):
        # List of alternatives
        alts = []
        for alt in ing:
            alts.append(parse_ingredient(alt))
        if len(alts) == 1:
            return alts[0]
        return "|".join(alts)
    return "unknown"


def extract_ingredients(data):
    """Extract ingredients list from a recipe JSON.

    Handles all common recipe types:
    - crafting_shapeless: "ingredients" list
    - crafting_shaped: "pattern" + "key" mapping
    - farmersdelight:cooking: "ingredients" list
    - farmersdelight:cutting: "ingredients" list + "tool"
    - brewinandchewin:fermenting: "ingredients" list
    - expandeddelight:juicing: "ingredients" list
    - smelting/smoking/campfire: "ingredient" (singular)
    - create:mixing/filling: "ingredients" list
    """
    ingredients = []
    recipe_type = data.get("type", "")

    # --- Shaped crafting: parse pattern + key ---
    if "pattern" in data and "key" in data:
        key_map = data["key"]
        pattern = data["pattern"]
        # Collect unique ingredients from pattern
        seen_keys = set()
        for row in pattern:
            for char in row:
                if char != ' ' and char not in seen_keys:
                    seen_keys.add(char)
                    if char in key_map:
                        ingredients.append(parse_ingredient(key_map[char]))
        return ingredients

    # --- Singular "ingredient" (smelting, smoking, campfire, blasting) ---
    if "ingredient" in data and not isinstance(data["ingredient"], list):
        ing = data["ingredient"]
        ingredients.append(parse_ingredient(ing))
        return ingredients

    # Handle "ingredient" as list too (some mods)
    if "ingredient" in data and isinstance(data["ingredient"], list):
        for ing in data["ingredient"]:
            ingredients.append(parse_ingredient(ing))
        return ingredients

    # --- "ingredients" list (shapeless, cooking, cutting, fermenting, etc.) ---
    if "ingredients" in data:
        for ing in data["ingredients"]:
            ingredients.append(parse_ingredient(ing))

    # --- Tool for cutting board ---
    if "tool" in data:
        tool = data["tool"]
        tool_str = parse_ingredient(tool)
        # Don't add tool to ingredients list, store separately
        # But we note it for completeness

    return ingredients


def extract_recipes(jar_path, mod_id):
    """Extract recipe JSONs with full ingredient data"""
    recipes = {}
    try:
        with zipfile.ZipFile(jar_path, 'r') as z:
            for name in z.namelist():
                if "/recipe/" in name and name.endswith(".json"):
                    try:
                        data = json.loads(z.read(name))
                        recipe_name = name.split("/")[-1].replace(".json", "")
                        recipe_type = data.get("type", "unknown")

                        # Extract result — handles multiple formats:
                        # Standard: "result": {"id": "...", "count": N}
                        # FD cutting: "result": [{"item": {"id": "...", "count": N}}, ...]
                        # B&C keg_pouring: "output": {"id": "...", "count": N}
                        # B&C fermenting: "result": {"id": "...", "amount": N} (fluid)
                        result = "unknown"
                        result_count = 1

                        # Try "output" field first (B&C keg_pouring)
                        r = data.get("result", data.get("output"))

                        if r is not None:
                            if isinstance(r, list) and len(r) > 0:
                                # FD cutting: array of results, take first
                                first = r[0]
                                if isinstance(first, dict):
                                    inner = first.get("item", first)
                                    if isinstance(inner, dict):
                                        result = inner.get("id", inner.get("item", "unknown"))
                                        result_count = inner.get("count", 1)
                                    elif isinstance(inner, str):
                                        result = inner
                            elif isinstance(r, dict):
                                result = r.get("id", r.get("item", "unknown"))
                                result_count = r.get("count", 1)
                            elif isinstance(r, str):
                                result = r

                            if result_count > 1:
                                result += f" x{result_count}"

                        # Extract ingredients
                        ingredients = extract_ingredients(data)

                        # Extract tool for cutting recipes
                        tool = None
                        if "tool" in data:
                            tool = parse_ingredient(data["tool"])

                        recipe_entry = {
                            "type": recipe_type,
                            "result": result,
                            "ingredients": ingredients,
                        }
                        if tool:
                            recipe_entry["tool"] = tool

                        recipes[recipe_name] = recipe_entry
                    except Exception:
                        pass
    except Exception as e:
        print(f"  Error reading recipes from {jar_path}: {e}")

    return recipes


def find_minecraft_client_jar():
    """Find the vanilla Minecraft client JAR in Gradle cache."""
    gradle_cache = os.path.expanduser("~/.gradle/caches")
    candidates = glob.glob(
        os.path.join(gradle_cache, "neoformruntime", "artifacts", "minecraft_*_client.jar")
    )
    if candidates:
        return candidates[0]
    # Fallback: search broader
    candidates = glob.glob(
        os.path.join(gradle_cache, "**", "minecraft*client*.jar"), recursive=True
    )
    return candidates[0] if candidates else None


def extract_icons_from_jar(jar_path, mod_id):
    """Extract 16x16 item textures as base64 PNG from a JAR."""
    icons = {}
    try:
        with zipfile.ZipFile(jar_path, 'r') as z:
            for name in z.namelist():
                # Item textures
                if f"assets/{mod_id}/textures/item/" in name and name.endswith(".png"):
                    item_name = name.split("/")[-1].replace(".png", "")
                    item_id = f"{mod_id}:{item_name}"
                    png_data = z.read(name)
                    icons[item_id] = base64.b64encode(png_data).decode('ascii')
                # Block textures (fallback for items that are blocks)
                elif f"assets/{mod_id}/textures/block/" in name and name.endswith(".png"):
                    block_name = name.split("/")[-1].replace(".png", "")
                    block_id = f"{mod_id}:{block_name}"
                    if block_id not in icons:  # item texture takes priority
                        png_data = z.read(name)
                        icons[block_id] = base64.b64encode(png_data).decode('ascii')
                    # Also register without common suffixes for better matching
                    # e.g. "asparagus_crate_side" -> also register as "asparagus_crate"
                    for suffix in ['_side', '_front', '_top', '_face', '_stage0', '_0']:
                        if block_name.endswith(suffix):
                            base_name = block_name[:-len(suffix)]
                            base_id = f"{mod_id}:{base_name}"
                            if base_id not in icons:
                                png_data = z.read(name)
                                icons[base_id] = base64.b64encode(png_data).decode('ascii')
    except Exception as e:
        print(f"  Error extracting icons from {jar_path}: {e}")
    return icons


def extract_all_icons(all_content):
    """Extract icons from all cooking mod JARs + vanilla MC."""
    all_icons = {}

    # Extract from cooking mod JARs
    for mod_id in COOKING_MODS:
        if mod_id not in all_content:
            continue
        jar_name = all_content[mod_id].get("jar", "")
        jar_path = os.path.join(MODS_DIR, jar_name)
        if os.path.exists(jar_path):
            icons = extract_icons_from_jar(jar_path, mod_id)
            all_icons.update(icons)
            print(f"  [{mod_id}] {len(icons)} icons extracted")

    # Extract vanilla MC icons
    mc_jar = find_minecraft_client_jar()
    if mc_jar:
        icons = extract_icons_from_jar(mc_jar, "minecraft")
        all_icons.update(icons)
        print(f"  [minecraft] {len(icons)} icons extracted")
    else:
        print("  [minecraft] Client JAR not found - no vanilla icons")

    return all_icons


def extract_tags_from_jar(jar_path):
    """Extract item tags (c: namespace) from a JAR. Returns {tag_name: [item_ids]}."""
    tags = {}
    try:
        with zipfile.ZipFile(jar_path, 'r') as z:
            for name in z.namelist():
                if '/tags/item/' in name and name.endswith('.json'):
                    try:
                        data = json.loads(z.read(name))
                        values = data.get("values", [])
                        # Build tag path: data/c/tags/item/almonds.json -> c:almonds
                        # data/c/tags/item/crops/tomato.json -> c:crops/tomato
                        parts = name.split('/tags/item/')
                        if len(parts) == 2:
                            namespace = parts[0].split('/')[-1]  # 'c', 'farmersdelight', etc
                            tag_name = parts[1].replace('.json', '')
                            full_tag = f"{namespace}:{tag_name}"

                            # Only collect direct item references (not sub-tags)
                            items = [v for v in values
                                     if isinstance(v, str) and not v.startswith('#')]
                            if items:
                                if full_tag not in tags:
                                    tags[full_tag] = []
                                tags[full_tag].extend(items)
                    except Exception:
                        pass
    except Exception as e:
        print(f"  Error extracting tags from {jar_path}: {e}")
    return tags


def extract_all_tags(all_content):
    """Extract tags from all cooking mod JARs. Returns merged tag dict."""
    all_tags = {}
    for mod_id in COOKING_MODS:
        if mod_id not in all_content:
            continue
        jar_name = all_content[mod_id].get("jar", "")
        jar_path = os.path.join(MODS_DIR, jar_name)
        if os.path.exists(jar_path):
            tags = extract_tags_from_jar(jar_path)
            for tag, items in tags.items():
                if tag not in all_tags:
                    all_tags[tag] = []
                all_tags[tag].extend(items)
            print(f"  [{mod_id}] {len(tags)} tags extracted")

    # Deduplicate values
    for tag in all_tags:
        all_tags[tag] = list(dict.fromkeys(all_tags[tag]))

    return all_tags


def get_manual_recipes():
    """Recetas que no están en JSON — definidas en código Java o mecánicas especiales.
    Estas las agrega manualmente para tener cobertura 100%."""
    return {
        # === EXPANDED DELIGHT: Cask (mecánica Java, no recipe JSON) ===
        "expandeddelight:cheese_wheel_from_cask": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:cheese_wheel",
            "ingredients": ["minecraft:milk_bucket"],
            "_manual": True,
            "_note": "Cask: llenar con leche, esperar tiempo → Cheese Wheel"
        },
        "expandeddelight:goat_cheese_wheel_from_cask": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:goat_cheese_wheel",
            "ingredients": ["expandeddelight:goat_milk_bucket"],
            "_manual": True,
            "_note": "Cask: llenar con goat milk, esperar → Goat Cheese Wheel"
        },
        # === BREWIN AND CHEWIN: Keg pouring (fluido → item via honeycomb mold) ===
        # flaxen_cheese (fluido del keg) → se vierte en honeycomb → unripe wheel
        # unripe wheel → esperar (mundo) → flaxen_cheese_wheel
        "brewinandchewin:flaxen_cheese_wheel_aging": {
            "type": "brewinandchewin:aging",
            "result": "brewinandchewin:flaxen_cheese_wheel",
            "ingredients": ["brewinandchewin:unripe_flaxen_cheese_wheel"],
            "_manual": True,
            "_note": "Colocar en el mundo, espera tiempo → madura a Cheese Wheel"
        },
        "brewinandchewin:scarlet_cheese_wheel_aging": {
            "type": "brewinandchewin:aging",
            "result": "brewinandchewin:scarlet_cheese_wheel",
            "ingredients": ["brewinandchewin:unripe_scarlet_cheese_wheel"],
            "_manual": True,
            "_note": "Colocar en el mundo, espera tiempo → madura a Cheese Wheel"
        },
        # Keg pouring: fluido → honeycomb container → unripe wheel
        "brewinandchewin:unripe_flaxen_pour": {
            "type": "brewinandchewin:keg_pouring",
            "result": "brewinandchewin:unripe_flaxen_cheese_wheel",
            "ingredients": ["brewinandchewin:flaxen_cheese", "minecraft:honeycomb"],
            "_manual": True,
            "_note": "Keg: verter fluido flaxen cheese en honeycomb"
        },
        "brewinandchewin:unripe_scarlet_pour": {
            "type": "brewinandchewin:keg_pouring",
            "result": "brewinandchewin:unripe_scarlet_cheese_wheel",
            "ingredients": ["brewinandchewin:scarlet_cheese", "minecraft:honeycomb"],
            "_manual": True,
            "_note": "Keg: verter fluido scarlet cheese en honeycomb"
        },
        # B&C: Keg pouring de bebidas a tankard/botella
        "brewinandchewin:beer_pour": {
            "type": "brewinandchewin:keg_pouring",
            "result": "brewinandchewin:beer",
            "ingredients": ["brewinandchewin:tankard"],
            "_manual": True,
            "_note": "Keg: verter Beer en Tankard"
        },
        "brewinandchewin:vodka_pour": {
            "type": "brewinandchewin:keg_pouring",
            "result": "brewinandchewin:vodka",
            "ingredients": ["brewinandchewin:tankard"],
            "_manual": True,
            "_note": "Keg: verter Vodka en Tankard"
        },
        "brewinandchewin:mead_pour": {
            "type": "brewinandchewin:keg_pouring",
            "result": "brewinandchewin:mead",
            "ingredients": ["brewinandchewin:tankard"],
            "_manual": True,
            "_note": "Keg: verter Mead en Tankard"
        },
        "brewinandchewin:rice_wine_pour": {
            "type": "brewinandchewin:keg_pouring",
            "result": "brewinandchewin:rice_wine",
            "ingredients": ["brewinandchewin:tankard"],
            "_manual": True,
            "_note": "Keg: verter Rice Wine en Tankard"
        },
        # === FARMER'S DELIGHT: Stove/Skillet (colocar item encima) ===
        "farmersdelight:stove_fried_egg": {
            "type": "farmersdelight:stove",
            "result": "farmersdelight:fried_egg",
            "ingredients": ["minecraft:egg"],
            "_manual": True,
            "_note": "Stove: colocar egg encima → Fried Egg"
        },
        "farmersdelight:skillet_beef_patty": {
            "type": "farmersdelight:skillet",
            "result": "farmersdelight:beef_patty",
            "ingredients": ["minecraft:beef"],
            "_manual": True,
            "_note": "Skillet: colocar beef → Beef Patty (también en smelting)"
        },
        "farmersdelight:skillet_cooked_chicken_cuts": {
            "type": "farmersdelight:skillet",
            "result": "farmersdelight:cooked_chicken_cuts",
            "ingredients": ["farmersdelight:chicken_cuts"],
            "_manual": True,
            "_note": "Skillet: colocar chicken cuts → Cooked Chicken Cuts"
        },
        "farmersdelight:skillet_cooked_bacon": {
            "type": "farmersdelight:skillet",
            "result": "farmersdelight:cooked_bacon",
            "ingredients": ["farmersdelight:bacon"],
            "_manual": True,
            "_note": "Skillet: colocar bacon → Cooked Bacon"
        },
        "farmersdelight:skillet_cooked_cod_slice": {
            "type": "farmersdelight:skillet",
            "result": "farmersdelight:cooked_cod_slice",
            "ingredients": ["farmersdelight:cod_slice"],
            "_manual": True,
            "_note": "Skillet: colocar cod slice → Cooked Cod Slice"
        },
        "farmersdelight:skillet_cooked_salmon_slice": {
            "type": "farmersdelight:skillet",
            "result": "farmersdelight:cooked_salmon_slice",
            "ingredients": ["farmersdelight:salmon_slice"],
            "_manual": True,
            "_note": "Skillet: colocar salmon slice → Cooked Salmon Slice"
        },
        "farmersdelight:skillet_cooked_mutton_chops": {
            "type": "farmersdelight:skillet",
            "result": "farmersdelight:cooked_mutton_chops",
            "ingredients": ["farmersdelight:mutton_chops"],
            "_manual": True,
            "_note": "Skillet: colocar mutton chops → Cooked Mutton Chops"
        },
        # === CROPTOPIA: Cheese Cake (missing from shapeless) ===
        "croptopia:cheese_cake_manual": {
            "type": "minecraft:crafting_shapeless",
            "result": "croptopia:cheese_cake",
            "ingredients": ["#c:cheeses", "#c:milks", "minecraft:sugar", "minecraft:egg", "#c:flour"],
            "_manual": True,
            "_note": "Receta verificada de Croptopia (no extraida correctamente del JAR)"
        },
        # === EXPANDED DELIGHT: Cheese Cask intermedios ===
        "expandeddelight:milk_cask_fill": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:milk_cask",
            "ingredients": ["minecraft:milk_bucket", "expandeddelight:cask"],
            "_manual": True,
            "_note": "Click derecho con milk bucket en Cask vacío"
        },
        "expandeddelight:goat_milk_cask_fill": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:goat_milk_cask",
            "ingredients": ["expandeddelight:goat_milk_bucket", "expandeddelight:cask"],
            "_manual": True,
            "_note": "Click derecho con goat milk bucket en Cask vacío"
        },
        "expandeddelight:cheese_cask_from_milk": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:cheese_cask",
            "ingredients": ["expandeddelight:milk_cask"],
            "_manual": True,
            "_note": "Milk Cask madura con el tiempo → Cheese Cask"
        },
        "expandeddelight:goat_cheese_cask_from_milk": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:goat_cheese_cask",
            "ingredients": ["expandeddelight:goat_milk_cask"],
            "_manual": True,
            "_note": "Goat Milk Cask madura → Goat Cheese Cask"
        },
        "expandeddelight:cheese_wheel_from_cheese_cask": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:cheese_wheel",
            "ingredients": ["expandeddelight:cheese_cask"],
            "_manual": True,
            "_note": "Cheese Cask madura → Cheese Wheel (romper para obtener)"
        },
        "expandeddelight:goat_cheese_wheel_from_cask2": {
            "type": "expandeddelight:cask",
            "result": "expandeddelight:goat_cheese_wheel",
            "ingredients": ["expandeddelight:goat_cheese_cask"],
            "_manual": True,
            "_note": "Goat Cheese Cask madura → Goat Cheese Wheel"
        },
    }


def generate_recipe_tree_data(all_content):
    """Generate tools/recipe-tree-data.js with consolidated cooking mod data."""
    os.makedirs(TOOLS_DIR, exist_ok=True)

    # Build recipe data for cooking mods only
    recipe_data = {}
    item_names = {}
    block_names = {}

    for mod_id in COOKING_MODS:
        if mod_id not in all_content:
            continue
        content = all_content[mod_id]

        recipe_data[mod_id] = {
            "recipes": content["recipes"],
            "summary": content["summary"],
        }

        # Build item name mapping (mod_id:item_name -> Human Name)
        for key, name in content["items"].items():
            # key is like "item.farmersdelight.tomato"
            parts = key.split(".")
            if len(parts) >= 3:
                item_id = f"{parts[1]}:{'.'.join(parts[2:])}"
                item_names[item_id] = name

        for key, name in content["blocks"].items():
            parts = key.split(".")
            if len(parts) >= 3:
                block_id = f"{parts[1]}:{'.'.join(parts[2:])}"
                block_names[block_id] = name

    # Add common vanilla item names
    vanilla_items = {
        "minecraft:wheat": "Wheat", "minecraft:sugar": "Sugar",
        "minecraft:egg": "Egg", "minecraft:milk_bucket": "Milk Bucket",
        "minecraft:water_bucket": "Water Bucket", "minecraft:bread": "Bread",
        "minecraft:potato": "Potato", "minecraft:carrot": "Carrot",
        "minecraft:beetroot": "Beetroot", "minecraft:apple": "Apple",
        "minecraft:melon_slice": "Melon Slice", "minecraft:pumpkin": "Pumpkin",
        "minecraft:sweet_berries": "Sweet Berries", "minecraft:glow_berries": "Glow Berries",
        "minecraft:cocoa_beans": "Cocoa Beans", "minecraft:honey_bottle": "Honey Bottle",
        "minecraft:sugar_cane": "Sugar Cane", "minecraft:kelp": "Kelp",
        "minecraft:dried_kelp": "Dried Kelp", "minecraft:mushroom_stew": "Mushroom Stew",
        "minecraft:bowl": "Bowl", "minecraft:glass_bottle": "Glass Bottle",
        "minecraft:beef": "Raw Beef", "minecraft:porkchop": "Raw Porkchop",
        "minecraft:chicken": "Raw Chicken", "minecraft:mutton": "Raw Mutton",
        "minecraft:rabbit": "Raw Rabbit", "minecraft:cod": "Raw Cod",
        "minecraft:salmon": "Raw Salmon", "minecraft:cooked_beef": "Steak",
        "minecraft:cooked_porkchop": "Cooked Porkchop",
        "minecraft:cooked_chicken": "Cooked Chicken",
        "minecraft:cooked_mutton": "Cooked Mutton",
        "minecraft:cooked_cod": "Cooked Cod",
        "minecraft:cooked_salmon": "Cooked Salmon",
        "minecraft:brown_mushroom": "Brown Mushroom",
        "minecraft:red_mushroom": "Red Mushroom",
        "minecraft:bamboo": "Bamboo", "minecraft:cactus": "Cactus",
        "minecraft:iron_ingot": "Iron Ingot", "minecraft:gold_ingot": "Gold Ingot",
        "minecraft:diamond": "Diamond", "minecraft:emerald": "Emerald",
        "minecraft:coal": "Coal", "minecraft:stick": "Stick",
        "minecraft:bone_meal": "Bone Meal", "minecraft:string": "String",
        "minecraft:leather": "Leather", "minecraft:feather": "Feather",
        "minecraft:paper": "Paper", "minecraft:book": "Book",
        "minecraft:clay_ball": "Clay Ball", "minecraft:brick": "Brick",
        "minecraft:nether_wart": "Nether Wart", "minecraft:blaze_rod": "Blaze Rod",
        "minecraft:ghast_tear": "Ghast Tear", "minecraft:ender_pearl": "Ender Pearl",
        "minecraft:dragon_egg": "Dragon Egg", "minecraft:nether_star": "Nether Star",
        "minecraft:cake": "Cake", "minecraft:cookie": "Cookie",
        "minecraft:pumpkin_pie": "Pumpkin Pie",
        "minecraft:golden_apple": "Golden Apple",
        "minecraft:enchanted_golden_apple": "Enchanted Golden Apple",
        "minecraft:seagrass": "Seagrass", "minecraft:ink_sac": "Ink Sac",
        "minecraft:spider_eye": "Spider Eye",
        "minecraft:fermented_spider_eye": "Fermented Spider Eye",
        "minecraft:gunpowder": "Gunpowder", "minecraft:glowstone_dust": "Glowstone Dust",
        "minecraft:redstone": "Redstone", "minecraft:lapis_lazuli": "Lapis Lazuli",
        "minecraft:snowball": "Snowball", "minecraft:ice": "Ice",
        "minecraft:packed_ice": "Packed Ice", "minecraft:blue_ice": "Blue Ice",
        "minecraft:magma_cream": "Magma Cream",
    }
    item_names.update(vanilla_items)

    # Merge block names into item names (some blocks are also items)
    for k, v in block_names.items():
        if k not in item_names:
            item_names[k] = v

    # Add manual recipes (Java-only mechanics not in JSON)
    manual = get_manual_recipes()
    manual_by_mod = defaultdict(dict)
    for rname, recipe in manual.items():
        mod = rname.split(":")[0]
        manual_by_mod[mod][rname.split(":")[1]] = recipe

    for mod, recipes in manual_by_mod.items():
        if mod in recipe_data:
            recipe_data[mod]["recipes"].update(recipes)
            print(f"  [{mod}] +{len(recipes)} manual recipes added")
        else:
            recipe_data[mod] = {"recipes": recipes, "summary": {"total_items": 0, "total_blocks": 0, "total_entities": 0, "total_recipes": len(recipes)}}
            print(f"  [{mod}] {len(recipes)} manual recipes (new mod in data)")

    # Extract tags
    print("\n[recipe-tree] Extracting item tags...")
    item_tags = extract_all_tags(all_content)

    # Extract item icons (textures as base64)
    print("\n[recipe-tree] Extracting item icons...")
    item_icons = extract_all_icons(all_content)

    # Write JS file
    tree_file = os.path.join(TOOLS_DIR, "recipe-tree-data.js")
    with open(tree_file, 'w', encoding='utf-8') as f:
        f.write("// Auto-generated by extract-mod-content.py\n")
        f.write("// Run: python scripts/extract-mod-content.py\n")
        f.write("// Generated from JARs in modpack/mods/\n\n")
        f.write("const RECIPE_DATA = ")
        json.dump(recipe_data, f, indent=2, ensure_ascii=False)
        f.write(";\n\n")
        f.write("const ITEM_NAMES = ")
        json.dump(item_names, f, indent=2, ensure_ascii=False)
        f.write(";\n\n")
        # Icons as compact JSON (no indent — they're big)
        f.write("const ITEM_ICONS = ")
        json.dump(item_icons, f, ensure_ascii=True)
        f.write(";\n\n")
        # Tags: tag_id -> [item_ids]
        f.write("const ITEM_TAGS = ")
        json.dump(item_tags, f, ensure_ascii=False)
        f.write(";\n")

    file_size = os.path.getsize(tree_file)
    print(f"\n[recipe-tree] Generated {tree_file}")
    print(f"  Mods: {', '.join(m for m in COOKING_MODS if m in all_content)}")
    total_recipes = sum(all_content[m]["summary"]["total_recipes"] for m in COOKING_MODS if m in all_content)
    print(f"  Total recipes: {total_recipes}")
    print(f"  Item names: {len(item_names)}")
    print(f"  Item icons: {len(item_icons)}")
    print(f"  Item tags: {len(item_tags)}")
    print(f"  File size: {file_size / 1024:.0f} KB")


def main():
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    all_content = {}

    for prefix, mod_id in sorted(GAMEPLAY_MODS.items()):
        jar_path = find_jar(prefix)
        if not jar_path:
            print(f"[SKIP] {prefix} - JAR not found")
            continue

        print(f"[{mod_id}] Extracting from {os.path.basename(jar_path)}...")

        items, blocks, entities = extract_lang(jar_path, mod_id)
        recipes = extract_recipes(jar_path, mod_id)

        content = {
            "mod_id": mod_id,
            "jar": os.path.basename(jar_path),
            "items": {k: v for k, v in sorted(items.items())},
            "blocks": {k: v for k, v in sorted(blocks.items())},
            "entities": {k: v for k, v in sorted(entities.items())},
            "recipes_count": len(recipes),
            "recipe_types": list(set(r["type"] for r in recipes.values())),
            "recipes": recipes,
            "summary": {
                "total_items": len(items),
                "total_blocks": len(blocks),
                "total_entities": len(entities),
                "total_recipes": len(recipes),
            }
        }

        all_content[mod_id] = content

        # Write individual mod file
        mod_file = os.path.join(OUTPUT_DIR, f"{mod_id}.json")
        with open(mod_file, 'w', encoding='utf-8') as f:
            json.dump(content, f, indent=2, ensure_ascii=False)

        print(f"  Items: {len(items)}, Blocks: {len(blocks)}, Entities: {len(entities)}, Recipes: {len(recipes)}")

    # Write summary
    summary = {}
    for mod_id, content in sorted(all_content.items()):
        summary[mod_id] = content["summary"]

    summary_file = os.path.join(OUTPUT_DIR, "_summary.json")
    with open(summary_file, 'w', encoding='utf-8') as f:
        json.dump(summary, f, indent=2, ensure_ascii=False)

    # Generate recipe tree data for web tool
    generate_recipe_tree_data(all_content)

    # Print summary table
    print("\n" + "="*70)
    print(f"{'Mod':<30} {'Items':>6} {'Blocks':>7} {'Entities':>9} {'Recipes':>8}")
    print("-"*70)
    totals = [0, 0, 0, 0]
    for mod_id, s in sorted(summary.items()):
        print(f"{mod_id:<30} {s['total_items']:>6} {s['total_blocks']:>7} {s['total_entities']:>9} {s['total_recipes']:>8}")
        totals[0] += s['total_items']
        totals[1] += s['total_blocks']
        totals[2] += s['total_entities']
        totals[3] += s['total_recipes']
    print("-"*70)
    print(f"{'TOTAL':<30} {totals[0]:>6} {totals[1]:>7} {totals[2]:>9} {totals[3]:>8}")
    print(f"\nDetailed files saved to: {OUTPUT_DIR}")

if __name__ == "__main__":
    main()
