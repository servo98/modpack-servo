# Game Loop

> Fuente: GDD v2, seccion 3.6
> Relacionado: [Progression](progression.md), [Space Elevator](space-elevator.md), [Dungeons](dungeons.md), [Cooking](cooking.md), [Create Automation](create-automation.md), [RPG Classes](rpg-classes.md)

## Ciclo principal (por capitulo)

```
1. QUEST BOOK
   └─ Ve lo que necesitas: item, comida, material, entrega

2. OBTENER MATERIALES
   ├─ Farming/mining → overworld
   ├─ Nether → ingredientes cocina/RPG
   ├─ Dungeon → drops exclusivos
   └─ Automatizar → Create (opcional)

3. PROCESAR
   ├─ Cocinar → workstations (a mano = divertido)
   ├─ Craftear → RPG gear, accesorios, llaves
   └─ Empacar → Cajas de Envio para entregas

4. ENTREGAR al Space Elevator
   ├─ Manual: caminar con cajas
   ├─ Tren: carga → transporta → descarga
   └─ Create pipeline: auto (Ch5+)

5. BOSS DE CAPITULO
   └─ Craftear Boss Key → pelear → drop

6. COMPLETAR CAPITULO
   ├─ Delivery completa + Boss derrotado
   ├─ ProgressiveStages desbloquea siguiente
   └─ Nuevos mods, recetas, tiers, dungeons

REPETIR con contenido mas dificil
```

## Sub-loops

### Loop de Dungeon
```
Necesito item exclusivo → crafteo llave del tier correcto
  → entro a dungeon (arriesgo inventario)
  → sobrevivo y saco loot / muero y pierdo items
  → uso el drop para craftear/quest/entregar
```

### Loop de Cocina (Perfil B ama esto)
```
Nuevo crop desbloqueado → descubro recetas → cocino variedad
  → Spice of Life da corazones bonus → empaco para entregas
  → Create automatiza escala (Perfil A)
```

### Loop de RPG
```
Desbloqueo tier de gear → crafteo armor/arma → mejoro build
  → entro a dungeon mas dificil → looteo unique jewelry
  → preparo boss → derroto boss → siguiente tier
```

### Loop de Automatizacion (Perfil A ama esto)
```
Necesito producir en masa → diseño fabrica Create
  → optimizo pipeline → conecto a RS (Ch5+)
  → tren transporta materiales → Space Elevator recibe
```

## Interconexion de loops

Todos alimentan al ciclo principal. El Space Elevator es el punto de convergencia donde cocina, Create, RPG, y dungeons se unen en entregas por capitulo.
