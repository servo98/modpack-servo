"""
Converts Blockbench .bbmodel files to Minecraft-compatible block model JSONs + extracts textures.
Run from the blockbench/ folder: python convert.py

For each .bbmodel file, outputs:
  - models/block/<name>.json  (MC block model)
  - textures/block/<texture_name>.png  (extracted texture)

Handles:
  - UV scaling from bbmodel coords (0-textureSize) to MC coords (0-16)
  - Texture namespace prefixing
  - Bakes rotations beyond vanilla MC limits into element geometry
  - Valid MC rotations (-45 to 45, single axis) are kept as-is
  - Removes Blockbench-only fields (format_version, groups, etc.)
"""

import json, base64, os, sys, math

MODELS_DIR = "../src/main/resources/assets/servo_packaging/models/block"
TEXTURES_DIR = "../src/main/resources/assets/servo_packaging/textures/block"
NAMESPACE = "servo_packaging"
MAX_ANGLE = 45.0

# Face normal vectors
FACE_NORMALS = {
    "north": (0, 0, -1), "south": (0, 0, 1),
    "east":  (1, 0, 0),  "west":  (-1, 0, 0),
    "up":    (0, 1, 0),  "down":  (0, -1, 0),
}

# Face local UV axes in world coordinates (U direction, V direction)
# U = direction of increasing u, V = direction of increasing v
FACE_UV_AXES = {
    "north": ((-1, 0, 0), (0, -1, 0)),
    "south": ((1, 0, 0),  (0, -1, 0)),
    "east":  ((0, 0, -1), (0, -1, 0)),
    "west":  ((0, 0, 1),  (0, -1, 0)),
    "up":    ((1, 0, 0),  (0, 0, 1)),
    "down":  ((1, 0, 0),  (0, 0, -1)),
}

def is_valid_mc_rotation(rotation):
    """Check if a bbmodel rotation can be represented in vanilla MC."""
    if rotation is None:
        return True
    rx, ry, rz = rotation
    non_zero = sum(1 for v in [rx, ry, rz] if abs(v) > 0.001)
    if non_zero == 0:
        return True
    if non_zero > 1:
        return False
    angle = max(abs(rx), abs(ry), abs(rz))
    return angle <= MAX_ANGLE + 0.001

def convert_rotation(rotation, origin):
    """Convert bbmodel rotation [rx, ry, rz] to MC rotation format (single axis, -45..45)."""
    if rotation is None:
        return None
    rx, ry, rz = rotation
    if abs(rx) > 0.001:
        return {"angle": rx, "axis": "x", "origin": origin}
    elif abs(ry) > 0.001:
        return {"angle": ry, "axis": "y", "origin": origin}
    elif abs(rz) > 0.001:
        return {"angle": rz, "axis": "z", "origin": origin}
    return None

def rotate_point(point, rotation, origin):
    """Apply Euler XYZ rotation (degrees) around origin to a 3D point."""
    x = point[0] - origin[0]
    y = point[1] - origin[1]
    z = point[2] - origin[2]
    rx, ry, rz = rotation

    # Rotate around X
    if abs(rx) > 0.001:
        rad = math.radians(rx)
        c, s = math.cos(rad), math.sin(rad)
        y, z = y * c - z * s, y * s + z * c

    # Rotate around Y
    if abs(ry) > 0.001:
        rad = math.radians(ry)
        c, s = math.cos(rad), math.sin(rad)
        x, z = x * c + z * s, -x * s + z * c

    # Rotate around Z
    if abs(rz) > 0.001:
        rad = math.radians(rz)
        c, s = math.cos(rad), math.sin(rad)
        x, y = x * c - y * s, x * s + y * c

    return (
        round(x + origin[0], 4),
        round(y + origin[1], 4),
        round(z + origin[2], 4),
    )

def get_face_remap(rotation):
    """Determine how face orientations change after rotation.
    Returns dict: old_face_name -> new_face_name."""
    # Rotate each face normal and find which standard direction it matches
    remap = {}
    for face_name, normal in FACE_NORMALS.items():
        rotated = rotate_point(normal, rotation, (0, 0, 0))
        # Round to nearest int to snap to axis
        rounded = (round(rotated[0]), round(rotated[1]), round(rotated[2]))
        # Find matching face
        for target_name, target_normal in FACE_NORMALS.items():
            if target_normal == rounded:
                remap[face_name] = target_name
                break
        else:
            # No exact match (non-90-degree rotation) — keep same face
            remap[face_name] = face_name
    return remap

def compute_uv_transform(rotation, old_face, new_face):
    """Compute UV flip flags needed after baking a rotation.
    Returns (flip_u, flip_v, mc_face_rotation)."""
    old_u, old_v = FACE_UV_AXES[old_face]
    new_u, new_v = FACE_UV_AXES[new_face]

    # Rotate old face's UV axes by the element rotation
    rot_u = tuple(round(v) for v in rotate_point(old_u, rotation, (0, 0, 0)))
    rot_v = tuple(round(v) for v in rotate_point(old_v, rotation, (0, 0, 0)))

    # Compute 2x2 matrix: how rotated old axes relate to new face axes
    a = sum(x * y for x, y in zip(rot_u, new_u))  # rot_u · new_u
    b = sum(x * y for x, y in zip(rot_u, new_v))  # rot_u · new_v
    c = sum(x * y for x, y in zip(rot_v, new_u))  # rot_v · new_u
    d = sum(x * y for x, y in zip(rot_v, new_v))  # rot_v · new_v

    # Interpret matrix [a b; c d]
    if abs(a) >= 1 and abs(d) >= 1 and abs(b) < 0.5 and abs(c) < 0.5:
        # Pure flip (no rotation needed)
        return (a < 0), (d < 0), 0
    elif abs(b) >= 1 and abs(c) >= 1 and abs(a) < 0.5 and abs(d) < 0.5:
        # 90° or 270° texture rotation
        if c > 0 and b < 0:
            return False, False, 90
        elif c < 0 and b > 0:
            return False, False, 270
        elif c > 0 and b > 0:
            return False, True, 90
        else:
            return True, False, 270
    return False, False, 0  # identity or unhandled

def apply_uv_flip(uv, flip_u, flip_v):
    """Flip UV coordinates by swapping u1<->u2 and/or v1<->v2."""
    u1, v1, u2, v2 = uv
    if flip_u:
        u1, u2 = u2, u1
    if flip_v:
        v1, v2 = v2, v1
    return [u1, v1, u2, v2]

def bake_element_rotation(elem):
    """Bake a non-MC-compatible rotation into element geometry.
    Transforms from/to positions, remaps faces, and adjusts UVs."""
    rotation = elem["rotation"]
    origin = elem.get("origin", [8, 8, 8])

    # Get all 8 corners of the cuboid
    f = elem["from"]
    t = elem["to"]
    corners = []
    for x in [f[0], t[0]]:
        for y in [f[1], t[1]]:
            for z in [f[2], t[2]]:
                corners.append(rotate_point((x, y, z), rotation, origin))

    # New axis-aligned bounding box
    new_from = [min(c[i] for c in corners) for i in range(3)]
    new_to = [max(c[i] for c in corners) for i in range(3)]

    # Remap faces and adjust UVs
    face_remap = get_face_remap(rotation)
    old_faces = elem.get("faces", {})
    new_faces = {}
    for old_name, face_data in old_faces.items():
        new_name = face_remap.get(old_name, old_name)
        # Compute UV adjustment for this face
        flip_u, flip_v, mc_rot = compute_uv_transform(rotation, old_name, new_name)
        new_data = dict(face_data)  # copy
        # Re-enable all faces — visibility changes after rotation baking
        new_data.pop("enabled", None)
        if "uv" in new_data and (flip_u or flip_v):
            new_data["uv"] = apply_uv_flip(new_data["uv"], flip_u, flip_v)
        if mc_rot != 0:
            new_data["rotation"] = mc_rot
        new_faces[new_name] = new_data

    elem["from"] = new_from
    elem["to"] = new_to
    elem["faces"] = new_faces
    # Remove rotation — it's now baked into geometry
    del elem["rotation"]
    if "origin" in elem:
        del elem["origin"]

    return elem

def scale_uv(uv, tex_w, tex_h):
    """Scale UV from bbmodel pixel coords to MC 0-16 range."""
    return [
        round(uv[0] * 16.0 / tex_w, 4),
        round(uv[1] * 16.0 / tex_h, 4),
        round(uv[2] * 16.0 / tex_w, 4),
        round(uv[3] * 16.0 / tex_h, 4),
    ]

def convert_element(elem, tex_w, tex_h):
    """Convert a bbmodel element to MC format."""
    mc_elem = {}
    mc_elem["from"] = list(elem["from"])  # copy to avoid mutating original
    mc_elem["to"] = list(elem["to"])

    # Clamp coordinates to -16..32 range (MC limit)
    for i in range(3):
        mc_elem["from"][i] = max(-16, min(32, mc_elem["from"][i]))
        mc_elem["to"][i] = max(-16, min(32, mc_elem["to"][i]))

    # Ensure from < to on each axis (baked rotations can swap min/max)
    for i in range(3):
        if mc_elem["from"][i] > mc_elem["to"][i]:
            mc_elem["from"][i], mc_elem["to"][i] = mc_elem["to"][i], mc_elem["from"][i]

    # Rotation (only if it's a valid MC rotation — non-valid ones were already baked)
    rotation = elem.get("rotation")
    origin = elem.get("origin", [8, 8, 8])
    if rotation and is_valid_mc_rotation(rotation):
        mc_rot = convert_rotation(rotation, origin)
        if mc_rot:
            mc_elem["rotation"] = mc_rot

    # Faces
    mc_faces = {}
    for face_name in ["north", "east", "south", "west", "up", "down"]:
        if face_name in elem.get("faces", {}):
            face = elem["faces"][face_name]
            if face.get("enabled", True) is False:
                continue
            mc_face = {"texture": "#0"}
            if "uv" in face:
                mc_face["uv"] = scale_uv(face["uv"], tex_w, tex_h)
            mc_faces[face_name] = mc_face
    mc_elem["faces"] = mc_faces

    return mc_elem

def convert_bbmodel(bbmodel_path):
    """Convert a single bbmodel file."""
    with open(bbmodel_path, 'r', encoding='utf-8') as f:
        data = json.load(f)

    name = data.get("name", os.path.splitext(os.path.basename(bbmodel_path))[0])
    resolution = data.get("resolution", {"width": 16, "height": 16})
    tex_w = resolution["width"]
    tex_h = resolution["height"]

    # Convert elements
    mc_elements = []
    baked = 0
    for elem in data.get("elements", []):
        rotation = elem.get("rotation")
        if rotation and not is_valid_mc_rotation(rotation):
            # Bake the rotation into geometry instead of skipping
            print(f"  BAKING rotation {rotation} for element '{elem.get('name', '?')}'")
            bake_element_rotation(elem)
            baked += 1
        mc_elements.append(convert_element(elem, tex_w, tex_h))

    # Get texture info
    textures = data.get("textures", [])
    tex_name = name  # default
    if textures:
        tex = textures[0]
        raw_name = tex.get("name", name)
        if raw_name.endswith(".png"):
            raw_name = raw_name[:-4]
        tex_name = raw_name

    # Build MC model JSON
    mc_model = {
        "parent": "minecraft:block/block",
        "texture_size": [tex_w, tex_h],
        "textures": {
            "0": f"{NAMESPACE}:block/{tex_name}",
            "particle": f"{NAMESPACE}:block/{tex_name}"
        },
        "elements": mc_elements
    }

    # Write model JSON
    os.makedirs(MODELS_DIR, exist_ok=True)
    model_path = os.path.join(MODELS_DIR, f"{name}.json")
    with open(model_path, 'w', encoding='utf-8') as f:
        json.dump(mc_model, f, indent='\t')
    print(f"  Model: {model_path}")

    # Extract and save texture
    if textures:
        tex = textures[0]
        source = tex.get("source", "")
        if source.startswith("data:image/png;base64,"):
            b64_data = source.split(",", 1)[1]
            png_bytes = base64.b64decode(b64_data)
            os.makedirs(TEXTURES_DIR, exist_ok=True)
            tex_path = os.path.join(TEXTURES_DIR, f"{tex_name}.png")
            with open(tex_path, 'wb') as f:
                f.write(png_bytes)
            print(f"  Texture: {tex_path}")

    if baked:
        print(f"  ({baked} elements had rotations baked into geometry)")

    return name, baked

def main():
    bbmodel_files = [f for f in os.listdir('.') if f.endswith('.bbmodel')]
    if not bbmodel_files:
        print("No .bbmodel files found in current directory.")
        return

    print(f"Found {len(bbmodel_files)} bbmodel files:\n")
    total_baked = 0
    for f in sorted(bbmodel_files):
        print(f"Converting {f}...")
        name, baked = convert_bbmodel(f)
        total_baked += baked
        print()

    print("Done!")
    if total_baked:
        print(f"\n{total_baked} elements had rotations baked into geometry.")
        print("(Rotations > 45 degrees are pre-computed into element positions.)")

if __name__ == "__main__":
    main()
