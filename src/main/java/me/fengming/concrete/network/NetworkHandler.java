package me.fengming.concrete.network;

import me.fengming.concrete.Concrete;
import me.fengming.concrete.network.entity.CapabilitySyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Concrete.MODID, "count_sync"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(CapabilitySyncPacket.class, nextID())
                .encoder(CapabilitySyncPacket::toBytes)
                .decoder(CapabilitySyncPacket::new)
                .consumerMainThread(CapabilitySyncPacket::mainHandler)
                .consumerNetworkThread(CapabilitySyncPacket::networkHandler)
                .add();
    }
}
