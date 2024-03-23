package me.fengming.concrete.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.fengming.concrete.Concrete;
import me.fengming.concrete.client.model.NPeltataModel;
import me.fengming.concrete.entity.NPeltata;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NPeltataRenderer extends MobRenderer<NPeltata, NPeltataModel> {
    public NPeltataRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new NPeltataModel(pContext.bakeLayer(NPeltataModel.N_PELTATA_LOCATION)), 0.0f);
    }
    @Override
    public ResourceLocation getTextureLocation(NPeltata pEntity) {
        return new ResourceLocation(Concrete.MODID, "textures/entity/n_peltata.png");
    }

    @Override
    public void render(NPeltata pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.translate(0.0F, 0.3F, 0.0F);
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
