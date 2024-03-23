package me.fengming.concrete;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import me.fengming.concrete.block.BlockRegistry;
import me.fengming.concrete.capability.entity.CountCapability;
import me.fengming.concrete.capability.ModCapabilities;
import me.fengming.concrete.capability.provider.CountCapabilityProvider;
import me.fengming.concrete.client.model.NPeltataModel;
import me.fengming.concrete.client.renderer.NPeltataRenderer;
import me.fengming.concrete.entity.EntityRegistry;
import me.fengming.concrete.entity.NPeltata;
import me.fengming.concrete.item.ItemRegistry;
import me.fengming.concrete.network.NetworkHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
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
import org.joml.Matrix4f;
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

    private void commonSetup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

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
            int count = ModCapabilities.getCount(event.getEntity(), "guan_ju");
            //System.out.println("count = " + count);
            if (count == 0) {
                return;
            }
            //LOGGER.info("text");
            float offset = event.getEntity().getBbHeight() + 0.2F;
            float[] rgb = new float[]{0.534F, 0.694F, 0.514F, 0.471F, 0.635F, 0.427F};
            float alpha = 1.0F;
            PoseStack poseStack = event.getPoseStack();

            poseStack.pushPose();
            poseStack.translate(0.0F, offset, 0.0F);
            poseStack.mulPose(event.getRenderer().entityRenderDispatcher.cameraOrientation());

            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            Matrix4f matrix4f = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();

            builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
            // outer
            builder.vertex(matrix4f, -0.12F, 0.0F, 0.002F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.2F, 0.002F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            builder.vertex(matrix4f, 0.12F, 0.0F, 0.002F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            // inner
            builder.vertex(matrix4f, -0.064F, 0.03F, 0.001F).color(rgb[3], rgb[4], rgb[5], alpha).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.14F, 0.001F).color(rgb[3], rgb[4], rgb[5], alpha).endVertex();
            builder.vertex(matrix4f, 0.064F, 0.03F, 0.001F).color(rgb[3], rgb[4], rgb[5], alpha).endVertex();

            tesselator.end();

            poseStack.scale(-0.015F, -0.015F, 0.01F);
            Font font = event.getRenderer().getFont();
            font.drawInBatch(String.valueOf(count), 5.0F, -14.0F,
                    -1, false, matrix4f, event.getMultiBufferSource(),
                    Font.DisplayMode.NORMAL, 0, event.getPackedLight());

            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();

            poseStack.popPose();
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
