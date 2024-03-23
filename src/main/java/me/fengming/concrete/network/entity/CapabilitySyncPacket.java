package me.fengming.concrete.network.entity;

import me.fengming.concrete.capability.entity.CountCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CountCapabilitySyncPacket {
    private final UUID uuid;
    private final CountCapability capability;

    public CountCapabilitySyncPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
        this.capability = buf.readNbt();
    }

    public CountCapabilitySyncPacket(Entity entity, CountCapability capability) {
        this.uuid = entity.getUUID();
        this.capability = capability;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeNbt(this.capability.serializeNBT());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LOGGER.info(this.message);
        });
        ctx.get().setPacketHandled(true);
    }
}
