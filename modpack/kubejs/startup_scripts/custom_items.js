// PROPOSITO: Items simples creados via KubeJS (tokens, keys, etc.)
// Items complejos con logica van en Java (servo_core)

StartupEvents.registry('item', event => {
    // Pepe Coin - moneda para gacha machine
    event.create('servo_core:pepe_coin')
        .displayName('Pepe Coin')
        .maxStackSize(64)
        .rarity('uncommon');
});
