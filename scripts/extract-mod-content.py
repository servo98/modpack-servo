"""
Extract all items, blocks, and recipes from mod JARs.
Outputs a comprehensive content catalog for quest/GDD design.
"""
import json, zipfile, os, sys
from collections import defaultdict

MODS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "modpack", "mods")
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "docs", "mod-data")

# Only extract gameplay-relevant mods (skip libs, performance, dev tools)
GAMEPLAY_MODS = {
    "FarmersDelight": "farmersdelight",
    "BrewinAndChewin": "brewinandchewin",
    "expandeddelight": "expandeddelight",
    "croptopia": "croptopia",
    "Aquaculture": "aquaculture",
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

def extract_recipes(jar_path, mod_id):
    """Extract recipe JSONs"""
    recipes = {}
    try:
        with zipfile.ZipFile(jar_path, 'r') as z:
            for name in z.namelist():
                if "/recipe/" in name and name.endswith(".json"):
                    try:
                        data = json.loads(z.read(name))
                        recipe_name = name.split("/")[-1].replace(".json", "")
                        recipe_type = data.get("type", "unknown")

                        # Extract result
                        result = "unknown"
                        if "result" in data:
                            r = data["result"]
                            if isinstance(r, dict):
                                result = r.get("id", r.get("item", "unknown"))
                                count = r.get("count", 1)
                                if count > 1:
                                    result += f" x{count}"
                            elif isinstance(r, str):
                                result = r

                        recipes[recipe_name] = {
                            "type": recipe_type,
                            "result": result,
                        }
                    except:
                        pass
    except Exception as e:
        print(f"  Error reading recipes from {jar_path}: {e}")

    return recipes

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
