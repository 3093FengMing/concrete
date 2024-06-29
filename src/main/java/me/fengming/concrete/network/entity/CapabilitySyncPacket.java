package me.fengming.concrete.network.entity;

import me.fengming.concrete.capability.ModCapabilities;
import me.fengming.concrete.capability.entity.CountCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CapabilitySyncPacket {
    private final UUID uuid;
    private final CompoundTag nbt;

    public CapabilitySyncPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
        this.nbt = buf.readNbt();
    }

    public CapabilitySyncPacket(Entity entity, INBTSerializable<CompoundTag> capability) {
        this.uuid = entity.getUUID();
        this.nbt = capability.serializeNBT();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeNbt(this.nbt);
    }

    public void mainHandler(Supplier<NetworkEvent.Context> ctx_) {
        NetworkEvent.Context ctx = ctx_.get();
        ctx.enqueueWork(() -> {
            assert Minecraft.getInstance().level != null;
            Entity entity = Minecraft.getInstance().level.getEntities().get(this.uuid);
            assert entity != null;
            entity.getCapability(ModCapabilities.COUNT_CAPABILITY).orElse(new CountCapability()).deserializeNBT(this.nbt);
        });
        ctx.setPacketHandled(true);
    }
}
