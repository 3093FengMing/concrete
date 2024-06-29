package me.fengming.concrete;

import com.mojang.logging.LogUtils;
import me.fengming.concrete.block.BlockRegistry;
import me.fengming.concrete.capability.ModCapabilities;
import me.fengming.concrete.capability.entity.CountCapability;
import me.fengming.concrete.capability.provider.CountCapabilityProvider;
import me.fengming.concrete.client.model.NPeltataModel;
import me.fengming.concrete.client.renderer.entity.NPeltataRenderer;
import me.fengming.concrete.client.renderer.mark.GuanJuMarkRenderer;
import me.fengming.concrete.entity.EntityRegistry;
import me.fengming.concrete.entity.NPeltata;
import me.fengming.concrete.item.ItemRegistry;
import me.fengming.concrete.network.NetworkHandler;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Concrete.MODID)
public class Concrete {
    public static final String MODID = "concrete";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Concrete() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ItemRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EntityRegistry.N_PELTATA.get(), NPeltataRenderer::new);
        }

        @SubscribeEvent
        public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(NPeltataModel.N_PELTATA_LOCATION, NPeltataModel::createBodyLayer);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onEntityRender(RenderLivingEvent.Post<?, ?> event) {
            GuanJuMarkRenderer.render(event);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModCommonEvents {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(NetworkHandler::registerMessage);
        }

        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(EntityRegistry.N_PELTATA.get(), NPeltata.createAttributes().build());
        }

        @SubscribeEvent
        public static void registerCaps(RegisterCapabilitiesEvent event) {
            event.register(CountCapability.class);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeCommonEvents {
        @SubscribeEvent
        public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
            event.addCapability(new ResourceLocation(MODID, "concrete_count"), new CountCapabilityProvider());
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            // Inheritance capability
            event.getOriginal().reviveCaps();
            LazyOptional<CountCapability> oldCap = event.getOriginal().getCapability(ModCapabilities.COUNT_CAPABILITY);
            LazyOptional<CountCapability> newCap = event.getEntity().getCapability(ModCapabilities.COUNT_CAPABILITY);
            if (oldCap.isPresent() && newCap.isPresent()) {
                newCap.ifPresent((newCap1) -> oldCap.ifPresent((oldCap1) -> newCap1.deserializeNBT(oldCap1.serializeNBT())));
            }
            event.getOriginal().invalidateCaps();
        }
    }
}
