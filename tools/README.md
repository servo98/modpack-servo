# Tools

Herramientas web standalone (HTML/JS, sin servidor) para el desarrollo del modpack.
Abrir directamente en el navegador como `file:///`.

## GUI Builder (`gui-builder.html`)

Editor visual para disenar GUIs de NeoForge (menus de bloques custom).

**Que hace:**
- Canvas interactivo a escala 3x con grid de 18px (tamano de slot MC)
- Paleta de elementos: Input/Output/Fuel slots, Large Output (26x26), Progress Arrow, Flame, Fluid Tank, Labels
- Preview fantasma al colocar, drag & drop para mover, resize de la GUI con handles
- Background image custom con auto-conversion a pixel art
- Genera codigo Java listo para copiar (5 tabs: Menu, Screen, Registry, ClientSetup, PNG)
- Save/Load como JSON
- Presets: Empty, Simple, Furnace-like, 3-input 1-output
- Tamanos preset: Standard (176x166), Compact, Mini, Large, hasta 512x512

**Controles:**
- Click palette -> place
- Click elemento -> select
- Drag -> move
- Del -> remove
- Ctrl+Z -> undo
- Arrow keys -> nudge (Shift = 1px libre)
- Esc -> cancel place
- Drag handles (bordes) -> resize GUI

## Texture Toolkit (`texture-toolkit.html`)

Convertidor de imagenes a texturas pixel art estilo Minecraft.

**Que hace:**
- Subir imagen (drag & drop o click) en PNG, JPG, BMP, WebP
- Convertir a tamanos estandar MC:
  - 16x16 (items, bloques)
  - 32x32 (armas RPG)
  - 64x64 (entidades, bloques detallados)
  - 64x32 (armor layers)
  - 128x128 (HD)
  - 256x256 (GUI atlas)
  - Custom hasta 512x512
- 2 algoritmos de downscale:
  - **Nearest Neighbor**: pixeles duros, default MC
  - **Area Average**: mas suave, mejor para fotos
- Reduccion de colores (Median Cut, 2-64 colores)
- 4 modos de dithering: None (default MC), Floyd-Steinberg, Ordered Bayer 4x4, Atkinson
- Post-effects: auto-outline oscuro (estilo items MC), inner highlight (pillow shading), brightness, contrast, saturation
- Preview: zoomed con grid de pixeles + tamano real + original
- Tiling 3x3 para ver como se repite como cara de bloque
- Info: colores unicos, transparencia, tamano estimado
- Paleta extraida con click para copiar hex
- Export: download PNG con nombre y path type (item/block/entity/gui/particle)
- Download All: descarga 16x16 + 32x32 + 64x64 de un tiro
