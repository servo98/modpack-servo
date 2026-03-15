package com.servo.mart.block;

import com.servo.mart.MartRegistry;
import com.servo.mart.data.CatalogDataLoader;
import com.servo.mart.data.CatalogEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PepeMartBlockEntity extends BlockEntity {

    public PepeMartBlockEntity(BlockPos pos, BlockState state) {
        super(MartRegistry.PEPE_MART_BE.get(), pos, state);
    }

    public void openScreen(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        List<CatalogEntry> catalog = CatalogDataLoader.getAllEntries();

        serverPlayer.openMenu(new net.minecraft.world.MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("block.servo_mart.pepe_mart");
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                    int containerId, net.minecraft.world.entity.player.Inventory inv,
                    Player p) {
                return new PepeMartMenu(containerId, inv, PepeMartBlockEntity.this);
            }
        }, buf -> {
            PepeMartMenu.writeCatalogData(buf, catalog);
        });
    }
}
