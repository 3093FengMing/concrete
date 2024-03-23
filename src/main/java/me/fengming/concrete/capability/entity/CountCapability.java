package me.fengming.concrete.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;

public class CountCapability implements INBTSerializable<CompoundTag> {

    public CountCapability(Entity entity) {

    }
    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
