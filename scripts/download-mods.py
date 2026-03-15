"""
Download all modpack mods from Modrinth API.
Usage: python scripts/download-mods.py
Mods not found on Modrinth will be listed for manual download.
"""
import json, urllib.request, urllib.parse, os, sys, time

MODS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "modpack", "mods")
API = "https://api.modrinth.com/v2"
HEADERS = {"User-Agent": "modpack-servo/0.1.0 (github.com/modpack-servo)"}
MC_VERSION = "1.21.1"
LOADER = "neoforge"

# Modrinth slug or search query -> expected mod name
# Format: (search_query, expected_slug_or_none)
MODS = [
    # Tier 0: Extra deps
    ("cucumber", "cucumber"),
    ("geckolib", "geckolib"),
    ("curios", "curios"),
    # Tier 1: Core libs
    ("architectury-api", "architectury-api"),
    ("ftb-library", "ftb-library-forge"),
    ("bookshelf", "bookshelf-lib"),
    ("moonlight", "moonlight"),
    ("balm", "balm"),
    ("citadel", "citadel"),
    ("prickle", "prickle"),
    ("epherolib", "epherolib"),
    # Tier 2: Systems
    ("ftb-quests", "ftb-quests-forge"),
    ("ftb-teams", "ftb-teams-forge"),
    ("ftb-chunks", "ftb-chunks-forge"),
    ("kubejs", "kubejs"),
    ("rhino", "rhino"),
    ("progressive-stages", None),  # Might not be on Modrinth
    ("jei", "jei"),
    # Tier 3: Cooking/Farming
    ("farmers-delight", "farmers-delight"),
    ("brewinandchewin", "brewin-and-chewin"),
    ("expanded-delight", "expanded-delight"),
    ("croptopia", "croptopia"),
    ("spice-of-life-carrot-edition", "spice-of-life-carrot-edition"),
    ("create-slice-and-dice", "slice-and-dice"),
    ("botany-pots", "botany-pots"),
    ("mystical-agriculture", "mystical-agriculture"),
    # Tier 4: Exploration
    ("alexs-mobs", "alexs-mobs"),
    ("dimensional-dungeons", None),  # Might not be on Modrinth
    ("lootr", "lootr"),
    # Tier 5: Create
    ("create", "create"),
    ("createaddition", "createaddition"),
    ("create-deco", "create-deco"),
    ("create-enchantment-industry", "create-enchantment-industry-fabric"),
    # Tier 6: Storage
    ("iron-chests", "iron-chests"),
    ("storage-drawers", "storage-drawers"),
    ("toms-storage", "toms-storage"),
    ("refined-storage", "refined-storage"),
    # Tier 7: Accessories
    # curios already in tier 0
    # Tier 8: Gacha
    ("gacha", None),  # Bloo's Gacha Machine - might not be on Modrinth
    # Tier 9: Decoration
    ("macaws-bridges", "macaws-bridges"),
    ("macaws-roofs", "macaws-roofs"),
    ("macaws-furniture", "macaws-furniture"),
    ("macaws-windows", "macaws-windows"),
    ("macaws-trapdoors", "macaws-trapdoors"),
    ("chipped", "chipped"),
    ("refurbished-furniture", "refurbished-furniture"),
    # Tier 10: Performance
    ("embeddium", "embeddium"),
    ("ferritecore", "ferritecore"),
    ("modernfix", "modernfix"),
    ("clumps", "clumps"),
    ("entityculling", "entityculling"),
    ("immediatelyfast", "immediatelyfast"),
    # Tier 11: Dev tools
    ("just-enough-resources", "just-enough-resources-jer"),
]

def api_get(path):
    url = f"{API}{path}"
    req = urllib.request.Request(url, headers=HEADERS)
    try:
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read())
    except Exception as e:
        return None

def search_mod(query):
    facets = json.dumps([["project_type:mod"], [f"categories:{LOADER}"], [f"versions:{MC_VERSION}"]])
    path = f"/search?query={urllib.parse.quote(query)}&facets={urllib.parse.quote(facets)}&limit=5"
    data = api_get(path)
    if data and data.get("hits"):
        return data["hits"]
    return []

def get_version(project_id):
    loaders = urllib.parse.quote(json.dumps([LOADER]))
    versions = urllib.parse.quote(json.dumps([MC_VERSION]))
    path = f"/project/{project_id}/version?loaders={loaders}&game_versions={versions}"
    data = api_get(path)
    if data and len(data) > 0:
        return data[0]
    return None

def download_file(url, filename, dest_dir):
    filepath = os.path.join(dest_dir, filename)
    if os.path.exists(filepath):
        print(f"  SKIP (already exists): {filename}")
        return True
    print(f"  Downloading: {filename}...", end=" ", flush=True)
    req = urllib.request.Request(url, headers=HEADERS)
    try:
        with urllib.request.urlopen(req, timeout=60) as resp:
            with open(filepath, "wb") as f:
                f.write(resp.read())
        size_mb = os.path.getsize(filepath) / (1024*1024)
        print(f"OK ({size_mb:.1f} MB)")
        return True
    except Exception as e:
        print(f"FAILED: {e}")
        return False

def main():
    os.makedirs(MODS_DIR, exist_ok=True)

    downloaded = []
    failed = []
    skipped = []

    for query, expected_slug in MODS:
        print(f"\n[{query}]")
        hits = search_mod(query)

        # Try to find the right mod
        project = None
        if expected_slug:
            for h in hits:
                if h["slug"] == expected_slug:
                    project = h
                    break
        if not project and hits:
            project = hits[0]

        if not project:
            print(f"  NOT FOUND on Modrinth")
            failed.append(query)
            continue

        print(f"  Found: {project['title']} ({project['slug']})")

        # Get latest version
        version = get_version(project["project_id"])
        if not version:
            print(f"  No version for NeoForge {MC_VERSION}")
            failed.append(f"{query} ({project['title']})")
            continue

        # Download
        file_info = version["files"][0]
        if download_file(file_info["url"], file_info["filename"], MODS_DIR):
            downloaded.append(f"{project['title']} ({version['version_number']})")
        else:
            failed.append(f"{query} ({project['title']})")

        time.sleep(0.3)  # Rate limit

    # Summary
    print("\n" + "="*60)
    print(f"DOWNLOADED: {len(downloaded)}")
    for d in downloaded:
        print(f"  + {d}")

    if failed:
        print(f"\nFAILED/NOT FOUND: {len(failed)}")
        for f in failed:
            print(f"  ! {f}")
        print("\nThese mods need manual download from CurseForge.")

    print(f"\nTotal JARs in {MODS_DIR}:")
    jars = [f for f in os.listdir(MODS_DIR) if f.endswith(".jar")]
    print(f"  {len(jars)} files")

if __name__ == "__main__":
    main()
