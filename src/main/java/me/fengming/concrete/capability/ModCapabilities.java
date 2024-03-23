package me.fengming.concrete.capability;

import me.fengming.concrete.capability.entity.CountCapability;
import me.fengming.concrete.network.NetworkHandler;
import me.fengming.concrete.network.entity.CapabilitySyncPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {
    public static Capability<CountCapability> COUNT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static int getCount(Entity entity, String id) {
        return entity.getCapability(COUNT_CAPABILITY).orElse(new CountCapability(id)).getCount(id);
    }

    public static void decreaseCount(Player player, Entity entity, String id) {
        int count = getCount(entity, id);
        setCount(player, entity, id, count - (count == 0 ? 0 : 1), -1);
    }

    public static void clearCount(Player player, Entity entity, String id) {
        setCount(player, entity, id, 0, -1);
    }

    public static void setCount(Player player, Entity entity, String id, int count, int max) {
        int _count = count <= max || max == -1 ? count : max;
        CountCapability capability = entity.getCapability(COUNT_CAPABILITY).orElse(new CountCapability(id));
        capability.setCount(id, _count);
        NetworkHandler.sendToClientPlayer(player, new CapabilitySyncPacket(entity, capability));
    }
}
