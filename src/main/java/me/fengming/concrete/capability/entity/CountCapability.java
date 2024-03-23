package me.fengming.concrete.capability.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class CountCapability implements INBTSerializable<CompoundTag> {
    private final HashMap<String, Integer> counts = new HashMap<>();

    public CountCapability() {}

    public CountCapability(String id) {
        counts.put(id, 0);
    }

    public int getCount(String id) {
        return counts.getOrDefault(id, 0);
    }

    public int setCount(String id, int count) {
        counts.put(id, count);
        return count;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        counts.forEach(nbt::putInt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getAllKeys().forEach(k -> this.counts.put(k, nbt.getInt(k)));
    }
}
