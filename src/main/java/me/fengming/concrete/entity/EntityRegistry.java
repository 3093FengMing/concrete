package me.fengming.concrete.entity;

import me.fengming.concrete.Concrete;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Concrete.MODID);
    public static final RegistryObject<EntityType<NPeltata>> N_PELTATA = ENTITY_TYPES.register("n_peltata",
            () -> EntityType.Builder.<NPeltata>of(NPeltata::new, MobCategory.WATER_CREATURE)
                    .sized(1.0F, 1.6F)
                    .clientTrackingRange(8)
                    .build("n_peltata")
    );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
