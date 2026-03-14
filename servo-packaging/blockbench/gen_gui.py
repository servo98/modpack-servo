"""
Generates the Packing Station GUI texture (176x166 main area, 256x256 total).
Matches vanilla MC GUI style with 2 slots + progress arrow.
Run: python gen_gui.py
Produces: packing_station.png in the same directory.
"""
from PIL import Image, ImageDraw

W, H = 256, 256
GUI_W, GUI_H = 176, 166

# Colors (vanilla MC GUI palette)
BG = (198, 198, 198)         # Main background
BORDER_DARK = (55, 55, 55)   # Frame shadow (bottom/right)
BORDER_LIGHT = (255, 255, 255)  # Frame highlight (top/left)
BLACK = (0, 0, 0)
SLOT_DARK = (55, 55, 55)     # Slot top/left border
SLOT_LIGHT = (255, 255, 255) # Slot bottom/right border
SLOT_BG = (139, 139, 139)    # Slot interior
ARROW_BG = (177, 177, 177)   # Empty arrow (slightly darker than BG)
ARROW_FILL = (255, 255, 255) # Filled arrow (white)
ARROW_OUTLINE = (105, 105, 105)  # Arrow outline
ACCENT = (160, 120, 70)      # Cardboard brown accent
TRANSPARENT = (0, 0, 0, 0)

img = Image.new("RGBA", (W, H), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)

# === MAIN GUI BACKGROUND (176x166) ===
# Outer border
draw.rectangle([0, 0, GUI_W - 1, GUI_H - 1], fill=BG, outline=BLACK)

# 3D frame effect - highlight on top/left
# Top edge highlight
draw.line([(1, 1), (GUI_W - 2, 1)], fill=BORDER_LIGHT)
draw.line([(2, 2), (GUI_W - 3, 2)], fill=BORDER_LIGHT)
# Left edge highlight
draw.line([(1, 1), (1, GUI_H - 2)], fill=BORDER_LIGHT)
draw.line([(2, 2), (2, GUI_H - 3)], fill=BORDER_LIGHT)
# Bottom edge shadow
draw.line([(1, GUI_H - 2), (GUI_W - 2, GUI_H - 2)], fill=BORDER_DARK)
draw.line([(2, GUI_H - 3), (GUI_W - 3, GUI_H - 3)], fill=BORDER_DARK)
# Right edge shadow
draw.line([(GUI_W - 2, 1), (GUI_W - 2, GUI_H - 2)], fill=BORDER_DARK)
draw.line([(GUI_W - 3, 2), (GUI_W - 3, GUI_H - 3)], fill=BORDER_DARK)

# Fill interior (inside the 3px border)
draw.rectangle([3, 3, GUI_W - 4, GUI_H - 4], fill=BG)

# === CARDBOARD ACCENT STRIPE (top decorative bar) ===
draw.rectangle([4, 4, GUI_W - 5, 13], fill=ACCENT)
# Darker line at bottom of accent
draw.line([(4, 13), (GUI_W - 5, 13)], fill=(130, 95, 55))

def draw_slot(x, y):
    """Draw a standard 18x18 MC inventory slot at (x, y)."""
    # Dark border (top and left) - creates inset 3D effect
    draw.line([(x, y), (x + 17, y)], fill=SLOT_DARK)        # top
    draw.line([(x, y), (x, y + 17)], fill=SLOT_DARK)        # left
    # Light border (bottom and right) - creates inset 3D effect
    draw.line([(x + 17, y), (x + 17, y + 17)], fill=SLOT_LIGHT)  # right
    draw.line([(x, y + 17), (x + 17, y + 17)], fill=SLOT_LIGHT)  # bottom
    # Slot background
    draw.rectangle([x + 1, y + 1, x + 16, y + 16], fill=SLOT_BG)

# === INPUT SLOT (flat_cardboard goes here) ===
# Menu slot position is (56, 35) — slot background is at (55, 34)
draw_slot(55, 34)

# === OUTPUT SLOT (open_box comes out here) ===
# Menu slot position is (116, 35) — slot background is at (115, 34)
draw_slot(115, 34)

# === PROGRESS ARROW (between slots) ===
# Arrow at (79, 34), 24x17 pixels
# Empty arrow outline (shows when not crafting)
ax, ay = 79, 35
aw, ah = 24, 16

# Arrow body (rectangular part)
body_w = 17
body_h = 10
body_y = ay + 3  # centered vertically

# Draw empty arrow background
draw.rectangle([ax, body_y, ax + body_w - 1, body_y + body_h - 1], outline=ARROW_OUTLINE, fill=BG)

# Arrow head (triangle on the right)
head_x = ax + body_w
# Triangle points: top, tip, bottom
for i in range(7):
    x_pos = head_x + i
    top = ay + 3 - i  # expands upward
    bot = ay + 3 + body_h - 1 + i  # expands downward
    if top >= ay and bot <= ay + ah:
        draw.line([(x_pos, top), (x_pos, bot)], fill=ARROW_OUTLINE)

# Redraw the outline cleaner - just make a nice arrow shape
# Clear and redraw
draw.rectangle([ax, ay, ax + aw - 1, ay + ah - 1], fill=BG)

# Draw arrow outline properly
# Body rectangle
bx1, by1 = ax, ay + 3
bx2, by2 = ax + 15, ay + 12
draw.rectangle([bx1, by1, bx2, by2], outline=ARROW_OUTLINE, fill=ARROW_BG)

# Arrow head (triangle)
points = [
    (ax + 16, ay),       # top of head
    (ax + 23, ay + 8),   # tip
    (ax + 16, ay + 15),  # bottom of head
]
draw.polygon(points, outline=ARROW_OUTLINE, fill=ARROW_BG)

# === FILLED ARROW SPRITE (at 176, 14 in texture) ===
# This is drawn over the empty arrow as progress fills
fax, fay = 176, 14

# Body
draw.rectangle([fax, fay + 3, fax + 15, fay + 12], fill=ARROW_FILL, outline=ARROW_OUTLINE)

# Arrow head
fpoints = [
    (fax + 16, fay),
    (fax + 23, fay + 8),
    (fax + 16, fay + 15),
]
draw.polygon(fpoints, fill=ARROW_FILL, outline=ARROW_OUTLINE)

# Make the fill a green/cardboard color instead of white for better visibility
ARROW_PROGRESS = (120, 180, 80)  # Green progress
draw.rectangle([fax + 1, fay + 4, fax + 14, fay + 11], fill=ARROW_PROGRESS)
fpoints_inner = [
    (fax + 16, fay + 1),
    (fax + 22, fay + 8),
    (fax + 16, fay + 14),
]
draw.polygon(fpoints_inner, fill=ARROW_PROGRESS)

# === PLAYER INVENTORY SLOTS ===
# Main inventory: 3 rows of 9, starting at (8, 84)
for row in range(3):
    for col in range(9):
        draw_slot(7 + col * 18, 83 + row * 18)

# Hotbar: 1 row of 9 at (8, 142) — but with a gap above it
for col in range(9):
    draw_slot(7 + col * 18, 141)

# === SEPARATOR LINE between inventory and hotbar ===
# (vanilla has a small visual gap, our slot positions handle this)

# === LABEL HINTS ===
# Small arrow/icon hints near slots
# Input label area (above input slot): small flat cardboard icon hint
draw.rectangle([59, 22, 68, 31], outline=(140, 105, 60), fill=(180, 140, 80))  # Small cardboard square

# Output label area (above output slot): small box icon hint
draw.rectangle([119, 22, 128, 31], outline=(140, 105, 60), fill=(180, 140, 80))
# Add a small "opening" to make it look like an open box
draw.rectangle([121, 22, 126, 24], fill=BG)

# Save
img.save("packing_station.png")
print("Generated packing_station.png")
