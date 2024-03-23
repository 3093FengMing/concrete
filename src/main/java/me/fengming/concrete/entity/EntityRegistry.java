package me.fengming.concrete.entities;

import me.fengming.concrete.Concrete;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Concrete.MODID);
    public static final RegistryObject<EntityType<NPeltata>> N_PELTATA = ENTITY_TYPES.register("n_peltata",
            () -> EntityType.Builder.<NPeltata>of(NPeltata::new, MobCategory.WATER_CREATURE)
                    .sized(0.5F, 1.2F)
                    .clientTrackingRange(8)
                    .build("n_peltata")
    );
    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
