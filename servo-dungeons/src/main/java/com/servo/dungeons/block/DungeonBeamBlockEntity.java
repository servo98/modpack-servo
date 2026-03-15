package com.servo.dungeons.block;

import com.servo.dungeons.DungeonRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * BlockEntity for the Dungeon Beam block.
 * Stores the position of the altar (pedestal) this beam belongs to.
 */
public class DungeonBeamBlockEntity extends BlockEntity {

    @Nullable
    private BlockPos altarPos;

    public DungeonBeamBlockEntity(BlockPos pos, BlockState state) {
        super(DungeonRegistry.BEAM_BE.get(), pos, state);
    }

    @Nullable
    public BlockPos getAltarPos() {
        return altarPos;
    }

    public void setAltarPos(@Nullable BlockPos altarPos) {
        this.altarPos = altarPos;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (altarPos != null) {
            tag.putInt("AltarX", altarPos.getX());
            tag.putInt("AltarY", altarPos.getY());
            tag.putInt("AltarZ", altarPos.getZ());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("AltarX")) {
            altarPos = new BlockPos(
                    tag.getInt("AltarX"),
                    tag.getInt("AltarY"),
                    tag.getInt("AltarZ")
            );
        }
    }
}
