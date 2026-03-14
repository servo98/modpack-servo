package com.servo.delivery.block;

import com.servo.delivery.DeliveryRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Shared BlockEntity for all slave blocks (ports, bases, antenna).
 * Stores a reference to the master terminal position so interactions
 * can be delegated.
 */
public class SlaveBlockEntity extends BlockEntity {

    private @Nullable BlockPos masterPos;

    public SlaveBlockEntity(BlockPos pos, BlockState state) {
        super(DeliveryRegistry.SLAVE_BE.get(), pos, state);
    }

    public @Nullable BlockPos getMasterPos() {
        return masterPos;
    }

    public void setMasterPos(@Nullable BlockPos pos) {
        this.masterPos = pos;
        setChanged();
    }

    /**
     * Gets the master terminal BlockEntity, if the master exists and is valid.
     */
    public @Nullable DeliveryTerminalBlockEntity getMaster() {
        if (masterPos == null || level == null) return null;
        if (level.getBlockEntity(masterPos) instanceof DeliveryTerminalBlockEntity terminal) {
            return terminal;
        }
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (masterPos != null) {
            tag.put("MasterPos", NbtUtils.writeBlockPos(masterPos));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("MasterPos")) {
            masterPos = NbtUtils.readBlockPos(tag, "MasterPos").orElse(null);
        }
    }
}
