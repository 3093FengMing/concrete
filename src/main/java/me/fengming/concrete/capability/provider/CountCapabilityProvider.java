package me.fengming.concrete.capability.provider;

import me.fengming.concrete.capability.entity.CountCapability;
import me.fengming.concrete.capability.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CountCapabilityProvider implements ICapabilityProvider, NonNullSupplier<CountCapability>, ICapabilitySerializable<CompoundTag> {

    private final CountCapability capability;
    public CountCapabilityProvider() {
        this.capability = new CountCapability();
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.COUNT_CAPABILITY ? LazyOptional.of(this).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.capability.deserializeNBT(nbt);
    }

    @Override
    public @NotNull CountCapability get() {
        return this.capability;
    }
}
