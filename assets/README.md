# Assets - Drop Folder

Carpeta para dejar assets (texturas, modelos, sonidos) que luego se distribuyen a los mods.

## Como usarla

1. Revisa tus issues asignados: `gh issue list --label "type:asset"`
2. Cada issue dice exactamente que archivos crear, formato, dimensiones y nombre
3. Deja los archivos en la subcarpeta del mod correspondiente (ej: `assets/servo-delivery/`)
4. Avisa a Claude para que los distribuya a las rutas correctas dentro del mod

## Estructura

```
assets/
├── servo-packaging/   # Texturas, modelos y sonidos de packaging
├── servo-delivery/    # Texturas, modelos y sonidos de delivery
└── servo-core/        # Assets de core (futuro)
```

No necesitas saber la estructura interna de cada mod. Solo deja los archivos aqui con el nombre correcto del issue.
