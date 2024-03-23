package me.fengming.concrete.network;

import me.fengming.concrete.Concrete;
import me.fengming.concrete.network.entity.CapabilitySyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Concrete.MODID, "capability_sync"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(CapabilitySyncPacket.class, nextID())
                .encoder(CapabilitySyncPacket::toBytes)
                .decoder(CapabilitySyncPacket::new)
                .consumerMainThread(CapabilitySyncPacket::mainHandler)
                .add();
    }

    public static <MSG> void sendToClientPlayer(Player player, MSG message) {
        if (player == null) {
            sendToAllClients(message);
        } else {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
        }
    }

    public static <MSG> void sendToAllClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
