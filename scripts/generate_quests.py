#!/usr/bin/env python3
"""
Generate FTB Quests SNBT files from quest preview markdown docs.

Reads:  docs/quests/ch*-quests.md (8 files)
Writes: modpack/config/ftbquests/quests/chapters/ch*_*.snbt (8 files)
        modpack/config/ftbquests/quests/lang/es_mx.snbt
        modpack/config/ftbquests/quests/lang/en_us.snbt

Usage: python scripts/generate_quests.py
"""

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
QUEST_DOCS = ROOT / "docs" / "quests"
OUTPUT_DIR = ROOT / "modpack" / "config" / "ftbquests" / "quests"

# ─── Chapter metadata ───────────────────────────────────────────────

CHAPTERS = {
    1: {"name_es": "Cap 1: Raices", "name_en": "Ch 1: Roots",
        "filename": "ch1_raices", "icon": "farmersdelight:cooking_pot",
        "source": "ch1-quests.md"},
    2: {"name_es": "Cap 2: La Cocina + Clase Melee", "name_en": "Ch 2: Kitchen + Melee",
        "filename": "ch2_cocina_melee", "icon": "servo_cooking:blender",
        "source": "ch2-quests.md"},
    3: {"name_es": "Cap 3: Engranajes + Magia", "name_en": "Ch 3: Gears + Magic",
        "filename": "ch3_engranajes_magia", "icon": "create:mechanical_press",
        "source": "ch3-quests.md"},
    4: {"name_es": "Cap 4: Horizontes", "name_en": "Ch 4: Horizons",
        "filename": "ch4_horizontes", "icon": "create:brass_casing",
        "source": "ch4-quests.md"},
    5: {"name_es": "Cap 5: La Red + Poder", "name_en": "Ch 5: Network + Power",
        "filename": "ch5_red_poder", "icon": "refinedstorage:controller",
        "source": "ch5-quests.md"},
    6: {"name_es": "Cap 6: Maestria", "name_en": "Ch 6: Mastery",
        "filename": "ch6_maestria", "icon": "minecraft:netherite_ingot",
        "source": "ch6-quests.md"},
    7: {"name_es": "Cap 7: Profundidades", "name_en": "Ch 7: Depths",
        "filename": "ch7_profundidades", "icon": "servo_dungeons:core_key",
        "source": "ch7-quests.md"},
    8: {"name_es": "Cap 8: El Final", "name_en": "Ch 8: The End",
        "filename": "ch8_final", "icon": "minecraft:nether_star",
        "source": "ch8-quests.md"},
}

# ─── Section config ─────────────────────────────────────────────────

SECTION_KEYWORDS = [
    ("Historia", 1), ("Cocina", 2), ("Farming", 3), ("Dungeon", 4),
    ("Combate", 5), ("Exploracion", 6), ("Construccion", 7), ("Coleccion", 8),
]

SECTION_ICONS = {
    1: "minecraft:book", 2: "farmersdelight:cooking_pot", 3: "minecraft:wheat",
    4: "servo_dungeons:basic_key", 5: "minecraft:iron_sword", 6: "minecraft:compass",
    7: "minecraft:bricks", 8: "minecraft:chest",
}

SECTION_NAMES_ES = {
    1: "Historia", 2: "Cocina", 3: "Farming", 4: "Dungeon",
    5: "Combate", 6: "Exploracion", 7: "Construccion", 8: "Coleccion",
}

SECTION_NAMES_EN = {
    1: "Story", 2: "Cooking", 3: "Farming", 4: "Dungeon",
    5: "Combat", 6: "Exploration", 7: "Building", 8: "Collection",
}

# Layout: each section has a starting (x,y) and delta for laying out quests
SECTION_LAYOUT = {
    1: {"x0": -6.0, "y0": -8.0, "dx": 1.5, "dy": 0.0},
    2: {"x0": -10.0, "y0": -3.0, "dx": 0.0, "dy": 1.5},
    3: {"x0": 4.0, "y0": -3.0, "dx": 0.0, "dy": 1.5},
    4: {"x0": -10.0, "y0": 5.0, "dx": 0.0, "dy": 1.5},
    5: {"x0": 4.0, "y0": 5.0, "dx": 0.0, "dy": 1.5},
    6: {"x0": -4.0, "y0": 12.0, "dx": 1.5, "dy": 0.0},
    7: {"x0": -10.0, "y0": 15.0, "dx": 0.0, "dy": 1.5},
    8: {"x0": 4.0, "y0": 15.0, "dx": 0.0, "dy": 1.5},
}

# ─── Known special reward items ─────────────────────────────────────

KNOWN_SPECIAL_ITEMS = [
    ("gacha machine verde", {"id": "gachamachine:gacha_machine_5", "count": 1}),
    ("gacha machine rosa", {"id": "gachamachine:gacha_machine_8", "count": 1}),
    ("gacha machine azul", {"id": "gachamachine:gacha_machine_6", "count": 1}),
    ("gacha machine purpura", {"id": "gachamachine:gacha_machine_7", "count": 1}),
    ("spell binding table", {"id": "spell_engine:spell_binding_table", "count": 1}),
    ("llave basica", {"id": "servo_dungeons:basic_key", "count": 1}),
    ("llave avanzada", {"id": "servo_dungeons:advanced_key", "count": 1}),
    ("llave maestra", {"id": "servo_dungeons:master_key", "count": 1}),
    ("llave del nucleo", {"id": "servo_dungeons:core_key", "count": 1}),
    ("pedestal", {"id": "servo_dungeons:dungeon_altar", "count": 1}),
]


# ─── ID generation ──────────────────────────────────────────────────

def make_id(ch, sec, idx, kind, sub=1):
    """Deterministic 16-char hex ID.
    ch(1-8), sec(0-8), idx(0-50), kind(0=chapter,1=quest,2=task,3=reward), sub(1+)
    """
    val = (ch << 56) | (sec << 48) | (idx << 40) | (kind << 32) | (sub << 16) | 0x0001
    return format(val, '016X')


def chapter_id(ch):
    return make_id(ch, 0, 0, 0, 0)


def quest_id(ch, sec, idx):
    return make_id(ch, sec, idx, 1)


def task_id(ch, sec, idx, n=1):
    return make_id(ch, sec, idx, 2, n)


def reward_id(ch, sec, idx, n=1):
    return make_id(ch, sec, idx, 3, n)


# ─── Parsing ────────────────────────────────────────────────────────

def detect_section(text):
    for kw, code in SECTION_KEYWORDS:
        if kw.lower() in text.lower():
            return code
    return None


def parse_deps(text):
    if text.strip() in ('-', ''):
        return []
    return [int(n) for n in re.findall(r'#(\d+)', text)]


def parse_detection(det):
    det = det.strip()
    if det.startswith('item:'):
        m = re.match(r'item:(.+?)(?:\s+x(\d+))?$', det)
        if m:
            return {"type": "item", "item_id": m.group(1).rstrip('?'),
                    "count": int(m.group(2)) if m.group(2) else 1}
    elif det.startswith('kill:'):
        m = re.match(r'kill:(.+?)$', det)
        if m:
            return {"type": "kill", "entity": m.group(1).rstrip('?'), "value": 1}
    elif det.startswith('custom:'):
        return {"type": "checkmark", "hint": det[7:]}
    elif det.startswith('observation:'):
        return {"type": "checkmark", "hint": det[12:]}
    return {"type": "checkmark", "hint": det}


def parse_rewards(text):
    pc = 0
    m = re.search(r'(\d+)\s*PC', text)
    if m:
        pc = int(m.group(1))

    specials = []
    parts = text.split('+')
    for part in parts[1:]:
        p = part.strip()
        if not p:
            continue

        # Hearts
        hm = re.search(r'(\d+)\s*corazon', p, re.IGNORECASE)
        if hm:
            specials.append({"type": "hearts", "amount": int(hm.group(1))})
            continue

        # Terminal blocks
        tm = re.search(r'(\d+)\s+bloques?\s+Terminal', p, re.IGNORECASE)
        if tm:
            specials.append({"type": "item", "id": "servo_delivery:delivery_terminal",
                             "count": int(tm.group(1))})
            continue

        # Runes
        rm = re.search(r'(\d+)\s+Runas?', p)
        if rm:
            specials.append({"type": "item", "id": "runes:blank_rune",
                             "count": int(rm.group(1))})
            continue

        # Known items (fuzzy match)
        matched = False
        for key, item_data in KNOWN_SPECIAL_ITEMS:
            if key in p.lower():
                cm = re.match(r'(\d+)\s+', p)
                count = int(cm.group(1)) if cm else item_data["count"]
                specials.append({"type": "item", "id": item_data["id"], "count": count})
                matched = True
                break

        # Unknown specials are silently skipped (boss drops, etc.)

    return pc, specials


def parse_quest_file(filepath, ch_num):
    text = filepath.read_text(encoding='utf-8')
    quests = []
    cur_sec = None
    sec_counter = {}

    for line in text.split('\n'):
        # Section header: ### Historia/Tutorial (12)
        sm = re.match(r'###\s+(.+?)\s*\(\d+\)', line)
        if sm:
            cur_sec = detect_section(sm.group(1))
            if cur_sec:
                sec_counter[cur_sec] = 0
            continue

        # Table row: | # | Quest | Cat | `det` | Reward | Deps |
        rm = re.match(
            r'\|\s*(\d+)\s*\|'
            r'\s*(.+?)\s*\|'
            r'\s*(.+?)\s*\|'
            r'\s*`(.+?)`\s*\|'
            r'\s*(.+?)\s*\|'
            r'\s*(.+?)\s*\|',
            line
        )
        if rm and cur_sec is not None:
            gnum = int(rm.group(1))
            name_raw = rm.group(2).strip()
            det_raw = rm.group(4).strip()
            rew_raw = rm.group(5).strip()
            deps_raw = rm.group(6).strip()

            sec_counter[cur_sec] = sec_counter.get(cur_sec, 0) + 1
            lidx = sec_counter[cur_sec]

            name = re.sub(r'\*\*', '', name_raw)
            is_gate = 'GATE:' in name_raw
            is_boss = is_gate and 'kill:' in det_raw
            is_elev = is_gate and 'Space Elevator' in name_raw

            quests.append({
                'ch': ch_num, 'sec': cur_sec, 'lidx': lidx, 'gnum': gnum,
                'name': name, 'det': parse_detection(det_raw),
                'pc': parse_rewards(rew_raw)[0],
                'specials': parse_rewards(rew_raw)[1],
                'deps': parse_deps(deps_raw),
                'is_gate': is_gate, 'is_boss': is_boss, 'is_elev': is_elev,
            })

    return quests


# ─── SNBT generation helpers ───────────────────────────────────────

def esc(text):
    return text.replace('\\', '\\\\').replace('"', '\\"')


def icon_for(q):
    d = q['det']
    if d['type'] == 'item':
        return d['item_id']
    if d['type'] == 'kill':
        return "minecraft:iron_sword"
    return SECTION_ICONS.get(q['sec'], "minecraft:book")


def shape_size(q, first_set):
    if q['is_boss'] or q['is_elev']:
        return "gear", 2.5
    if q['gnum'] in first_set:
        return "hexagon", 2.0
    return "square", 1.5


def position(q):
    L = SECTION_LAYOUT[q['sec']]
    i = q['lidx'] - 1
    return L['x0'] + i * L['dx'], L['y0'] + i * L['dy']


# ─── SNBT output ───────────────────────────────────────────────────

def snbt_task(q):
    tid = task_id(q['ch'], q['sec'], q['lidx'])
    d = q['det']
    if d['type'] == 'item':
        cnt = f" count: {d['count']}" if d['count'] > 1 else ""
        return (f'\t\t\t{{\n'
                f'\t\t\t\tid: "{tid}"\n'
                f'\t\t\t\ttype: "item"\n'
                f'\t\t\t\titem: {{ id: "{d["item_id"]}"{cnt} }}\n'
                f'\t\t\t}}')
    if d['type'] == 'kill':
        return (f'\t\t\t{{\n'
                f'\t\t\t\tid: "{tid}"\n'
                f'\t\t\t\ttype: "kill"\n'
                f'\t\t\t\tentity: "{d["entity"]}"\n'
                f'\t\t\t\tvalue: {d["value"]}L\n'
                f'\t\t\t}}')
    return (f'\t\t\t{{\n'
            f'\t\t\t\tid: "{tid}"\n'
            f'\t\t\t\ttype: "checkmark"\n'
            f'\t\t\t}}')


def snbt_rewards(q):
    parts = []
    rn = 1

    # Pepe Coins
    if q['pc'] > 0:
        rid = reward_id(q['ch'], q['sec'], q['lidx'], rn)
        cnt = f" count: {q['pc']}" if q['pc'] > 1 else ""
        parts.append(
            f'\t\t\t{{\n'
            f'\t\t\t\tid: "{rid}"\n'
            f'\t\t\t\ttype: "item"\n'
            f'\t\t\t\titem: {{ id: "servo_core:pepe_coin"{cnt} }}\n'
            f'\t\t\t}}'
        )
        rn += 1

    # XP
    xp = 50 if q['pc'] <= 3 else (100 if q['pc'] <= 5 else 200)
    rid = reward_id(q['ch'], q['sec'], q['lidx'], rn)
    parts.append(
        f'\t\t\t{{\n'
        f'\t\t\t\tid: "{rid}"\n'
        f'\t\t\t\ttype: "xp"\n'
        f'\t\t\t\txp: {xp}\n'
        f'\t\t\t}}'
    )
    rn += 1

    # Special items
    for sp in q['specials']:
        rid = reward_id(q['ch'], q['sec'], q['lidx'], rn)
        if sp.get('type') == 'item':
            cnt = f" count: {sp['count']}" if sp['count'] > 1 else ""
            parts.append(
                f'\t\t\t{{\n'
                f'\t\t\t\tid: "{rid}"\n'
                f'\t\t\t\ttype: "item"\n'
                f'\t\t\t\titem: {{ id: "{sp["id"]}"{cnt} }}\n'
                f'\t\t\t}}'
            )
            rn += 1
        # Hearts: skip for now (needs custom implementation)

    # Stage command for GATE quests
    if (q['is_boss'] or q['is_elev']) and q['ch'] < 8:
        rid = reward_id(q['ch'], q['sec'], q['lidx'], rn)
        stage = f"servo_ch{q['ch'] + 1}"
        parts.append(
            f'\t\t\t{{\n'
            f'\t\t\t\tid: "{rid}"\n'
            f'\t\t\t\ttype: "command"\n'
            f'\t\t\t\tcommand: "/kubejs stages add @p {stage}"\n'
            f'\t\t\t}}'
        )
        rn += 1

    return parts


def snbt_quest(q, dep_map, first_set):
    qid = quest_id(q['ch'], q['sec'], q['lidx'])
    ic = icon_for(q)
    sh, sz = shape_size(q, first_set)
    x, y = position(q)

    dep_ids = [f'"{dep_map[d]}"' for d in q['deps'] if d in dep_map]
    deps_str = ", ".join(dep_ids)

    task_str = snbt_task(q)
    rew_strs = snbt_rewards(q)
    rewards_block = "\n".join(rew_strs)

    return (
        f'\t\t{{\n'
        f'\t\t\tid: "{qid}"\n'
        f'\t\t\tdependencies: [{deps_str}]\n'
        f'\t\t\ticon: {{ id: "{ic}" }}\n'
        f'\t\t\tsize: {sz}d\n'
        f'\t\t\tshape: "{sh}"\n'
        f'\t\t\tx: {x}d\n'
        f'\t\t\ty: {y}d\n'
        f'\t\t\ttasks: [\n'
        f'{task_str}\n'
        f'\t\t\t]\n'
        f'\t\t\trewards: [\n'
        f'{rewards_block}\n'
        f'\t\t\t]\n'
        f'\t\t}}'
    )


def generate_chapter(ch_num, quests):
    meta = CHAPTERS[ch_num]
    cid = chapter_id(ch_num)

    # Build global_num -> hex_id map for deps
    dep_map = {q['gnum']: quest_id(q['ch'], q['sec'], q['lidx']) for q in quests}

    # First quest per section
    first_set = set()
    seen = set()
    for q in quests:
        if q['sec'] not in seen:
            first_set.add(q['gnum'])
            seen.add(q['sec'])

    blocks = [snbt_quest(q, dep_map, first_set) for q in quests]
    quests_block = "\n".join(blocks)

    return (
        f'{{\n'
        f'\tdefault_hide_dependency_lines: false\n'
        f'\tdefault_quest_shape: "square"\n'
        f'\tfilename: "{meta["filename"]}"\n'
        f'\tgroup: ""\n'
        f'\ticon: {{ id: "{meta["icon"]}" }}\n'
        f'\tid: "{cid}"\n'
        f'\torder_index: {ch_num - 1}\n'
        f'\tquests: [\n'
        f'{quests_block}\n'
        f'\t]\n'
        f'}}\n'
    )


# ─── Lang files ────────────────────────────────────────────────────

def lang_entries(ch_num, quests, lang):
    lines = []
    meta = CHAPTERS[ch_num]
    cid = chapter_id(ch_num)

    title_key = "name_es" if lang == "es" else "name_en"
    lines.append(f'\tchapter.{cid}.title: "{esc(meta[title_key])}"')

    for q in quests:
        qid = quest_id(q['ch'], q['sec'], q['lidx'])
        sec_name = SECTION_NAMES_ES[q['sec']] if lang == "es" else SECTION_NAMES_EN[q['sec']]

        title = q['name']
        d = q['det']
        if lang == "es":
            if d['type'] == 'item':
                nice = d['item_id'].split(':')[-1].replace('_', ' ')
                desc = f"Obten {nice}" + (f" x{d['count']}" if d.get('count', 1) > 1 else "")
            elif d['type'] == 'kill':
                nice = d['entity'].split(':')[-1].replace('_', ' ')
                desc = f"Derrota a {nice}"
            else:
                desc = d.get('hint', title)
        else:
            if d['type'] == 'item':
                nice = d['item_id'].split(':')[-1].replace('_', ' ')
                desc = f"Obtain {nice}" + (f" x{d['count']}" if d.get('count', 1) > 1 else "")
            elif d['type'] == 'kill':
                nice = d['entity'].split(':')[-1].replace('_', ' ')
                desc = f"Defeat {nice}"
            else:
                desc = d.get('hint', title)

        lines.append(f'\tquest.{qid}.title: "{esc(title)}"')
        lines.append(f'\tquest.{qid}.quest_subtitle: "{esc(sec_name)}"')
        lines.append(f'\tquest.{qid}.quest_desc: ["{esc(desc)}"]')

    return lines


# ─── Main ──────────────────────────────────────────────────────────

def main():
    all_data = {}

    for ch in range(1, 9):
        src = QUEST_DOCS / CHAPTERS[ch]["source"]
        if not src.exists():
            print(f"WARNING: {src} not found, skipping ch{ch}")
            continue
        quests = parse_quest_file(src, ch)
        all_data[ch] = quests
        print(f"Ch{ch}: {len(quests)} quests parsed")

    # Write chapter SNBT files
    ch_dir = OUTPUT_DIR / "chapters"
    ch_dir.mkdir(parents=True, exist_ok=True)
    for ch, quests in all_data.items():
        fp = ch_dir / (CHAPTERS[ch]["filename"] + ".snbt")
        fp.write_text(generate_chapter(ch, quests), encoding='utf-8')
        print(f"  Wrote {fp.relative_to(ROOT)}")

    # Write lang files
    lang_dir = OUTPUT_DIR / "lang"
    lang_dir.mkdir(parents=True, exist_ok=True)
    for code, lang in [("es_mx", "es"), ("en_us", "en")]:
        entries = []
        for ch in sorted(all_data):
            entries.extend(lang_entries(ch, all_data[ch], lang))
        content = "{\n" + "\n".join(entries) + "\n}\n"
        fp = lang_dir / f"{code}.snbt"
        fp.write_text(content, encoding='utf-8')
        print(f"  Wrote {fp.relative_to(ROOT)}")

    # Summary
    total = sum(len(q) for q in all_data.values())
    total_pc = sum(q['pc'] for qs in all_data.values() for q in qs)
    print(f"\n{'='*40}")
    print(f"Total: {total} quests, {total_pc} PC")
    for ch in sorted(all_data):
        qs = all_data[ch]
        pc = sum(q['pc'] for q in qs)
        gates = sum(1 for q in qs if q['is_gate'])
        print(f"  Ch{ch}: {len(qs)} quests, {pc} PC, {gates} GATE")


if __name__ == '__main__':
    main()
