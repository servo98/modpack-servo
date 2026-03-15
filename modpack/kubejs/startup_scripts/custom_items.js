// PROPOSITO: Items simples creados via KubeJS (tokens, keys, etc.)
// Items complejos con logica van en Java (servo_core)

StartupEvents.registry('item', event => {

    // === Moneda ===
    event.create('servo_core:pepe_coin')
        .displayName('Pepe Coin')
        .maxStackSize(64)
        .rarity('uncommon');

    // === Materiales de dungeon ===
    event.create('servo_core:dungeon_essence')
        .displayName('Dungeon Essence')
        .maxStackSize(64)
        .rarity('rare');

    event.create('servo_core:core_crystal_fragment')
        .displayName('Core Crystal Fragment')
        .maxStackSize(64)
        .rarity('epic');

    // === Llaves de dungeon ===
    event.create('servo_core:basic_dungeon_key')
        .displayName('Basic Dungeon Key')
        .maxStackSize(16)
        .rarity('common');

    event.create('servo_core:advanced_dungeon_key')
        .displayName('Advanced Dungeon Key')
        .maxStackSize(16)
        .rarity('uncommon');

    event.create('servo_core:master_dungeon_key')
        .displayName('Master Dungeon Key')
        .maxStackSize(16)
        .rarity('rare');

    event.create('servo_core:core_dungeon_key')
        .displayName('Core Dungeon Key')
        .maxStackSize(16)
        .rarity('epic');
});
