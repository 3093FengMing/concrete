package me.fengming.concrete.block;

import me.fengming.concrete.Concrete;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Concrete.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
