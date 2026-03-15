// PROPOSITO: Items simples creados via KubeJS (tokens, keys, etc.)
// Items complejos con logica van en Java (servo_core)
//
// NOTA: Los items que estaban aqui (pepe_coin, dungeon_essence,
// core_crystal_fragment, llaves de dungeon) fueron migrados a
// servo_core Java. Ver ModRegistry.java.
//
// Este archivo queda vacio intencionalmente.
// Nuevos items simples pueden registrarse aqui si no necesitan logica Java.

StartupEvents.registry('item', event => {
    // Items migrados a servo_core Java:
    // - servo_core:pepe_coin
    // - servo_core:dungeon_essence
    // - servo_core:core_crystal_fragment
    // - servo_core:basic_dungeon_key
    // - servo_core:advanced_dungeon_key
    // - servo_core:master_dungeon_key
    // - servo_core:core_dungeon_key
});
