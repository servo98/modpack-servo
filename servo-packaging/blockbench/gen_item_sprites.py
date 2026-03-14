"""
Generates improved item sprites for servo_packaging items.
Run from the blockbench/ folder: python gen_item_sprites.py

Outputs:
  - textures/item/flat_cardboard.png  (corrugated cardboard sheet)
  - textures/item/shipping_box.png    (sealed box with tape)
"""

from PIL import Image, ImageDraw
import os

TEXTURES_DIR = "../src/main/resources/assets/servo_packaging/textures/item"

# Color palette
TRANSPARENT = (0, 0, 0, 0)
DARK_BROWN = (101, 67, 33, 255)       # edges, outlines
MED_BROWN = (160, 120, 60, 255)       # front face, body
LIGHT_BROWN = (195, 163, 104, 255)    # top face, highlights
VERY_LIGHT = (212, 185, 140, 255)     # inner cardboard
CORRUGATION = (145, 108, 55, 255)     # corrugation lines
TAPE = (200, 190, 165, 255)           # packing tape
TAPE_EDGE = (175, 160, 130, 255)      # tape edge
SHADOW = (85, 58, 28, 255)            # right side shadow
LABEL_BG = (180, 150, 95, 255)        # label area on box


def gen_flat_cardboard():
    """Generate a flat corrugated cardboard sheet sprite."""
    img = Image.new('RGBA', (16, 16), TRANSPARENT)
    draw = ImageDraw.Draw(img)

    # Main cardboard body - rectangular sheet
    draw.rectangle([2, 3, 13, 13], fill=LIGHT_BROWN)
    # Border
    draw.rectangle([2, 3, 13, 13], outline=DARK_BROWN)

    # Inner area slightly lighter
    draw.rectangle([3, 4, 12, 12], fill=VERY_LIGHT)

    # Corrugation lines (horizontal wavy texture)
    for y in [5, 7, 9, 11]:
        draw.line([(3, y), (12, y)], fill=CORRUGATION)

    # Fold line hint (diagonal crease)
    draw.line([(3, 4), (5, 4)], fill=CORRUGATION)
    draw.line([(10, 12), (12, 12)], fill=CORRUGATION)

    # Small folded corner (top-right)
    img.putpixel((12, 4), DARK_BROWN)
    img.putpixel((11, 4), MED_BROWN)
    img.putpixel((12, 5), MED_BROWN)

    return img


def gen_shipping_box():
    """Generate a sealed shipping box sprite with tape."""
    img = Image.new('RGBA', (16, 16), TRANSPARENT)
    draw = ImageDraw.Draw(img)

    # === 3D box from slight angle ===

    # Top face (lighter, slight perspective)
    draw.rectangle([2, 1, 12, 4], fill=LIGHT_BROWN)
    draw.rectangle([2, 1, 12, 4], outline=DARK_BROWN)

    # Right side strip (3D depth effect)
    draw.rectangle([13, 2, 14, 13], fill=SHADOW)
    draw.line([(13, 1), (14, 2)], fill=DARK_BROWN)  # top-right diagonal
    draw.line([(13, 13), (14, 13)], fill=DARK_BROWN)

    # Front face
    draw.rectangle([2, 4, 12, 13], fill=MED_BROWN)
    draw.rectangle([2, 4, 12, 13], outline=DARK_BROWN)

    # Top face tape stripe (horizontal, centered)
    draw.rectangle([6, 2, 8, 3], fill=TAPE)
    draw.line([(6, 2), (6, 3)], fill=TAPE_EDGE)
    draw.line([(8, 2), (8, 3)], fill=TAPE_EDGE)

    # Front face tape stripe (vertical, continuing from top)
    draw.rectangle([6, 4, 8, 7], fill=TAPE)
    draw.line([(6, 4), (6, 7)], fill=TAPE_EDGE)
    draw.line([(8, 4), (8, 7)], fill=TAPE_EDGE)

    # Edge line between top and front (horizontal seam)
    draw.line([(2, 4), (12, 4)], fill=DARK_BROWN)

    # Label area (lighter center) - this is where the dynamic overlay goes
    draw.rectangle([4, 8, 10, 12], fill=LABEL_BG)
    draw.rectangle([4, 8, 10, 12], outline=CORRUGATION)

    # Bottom shadow
    draw.line([(3, 14), (14, 14)], fill=SHADOW)

    return img


def main():
    os.makedirs(TEXTURES_DIR, exist_ok=True)

    cardboard = gen_flat_cardboard()
    cardboard_path = os.path.join(TEXTURES_DIR, "flat_cardboard.png")
    cardboard.save(cardboard_path)
    print(f"Generated: {cardboard_path}")

    box = gen_shipping_box()
    box_path = os.path.join(TEXTURES_DIR, "shipping_box.png")
    box.save(box_path)
    print(f"Generated: {box_path}")

    print("\nDone! Item sprites generated.")


if __name__ == "__main__":
    main()
