package me.fengming.concrete.client.renderer.mark;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import me.fengming.concrete.capability.ModCapabilities;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.joml.Matrix4f;

public class GuanJuMarkRenderer {
    public static void render(RenderLivingEvent<?, ?> event) {
        int count = ModCapabilities.getCount(event.getEntity(), "guan_ju");
        if (count == 0) return;

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
        builder.vertex(matrix4f, -0.12F, 0.0F, 0.001F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
        builder.vertex(matrix4f, 0.0F, 0.2F, 0.001F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
        builder.vertex(matrix4f, 0.12F, 0.0F, 0.001F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
        // inner
        builder.vertex(matrix4f, -0.064F, 0.03F, 0.0F).color(rgb[3], rgb[4], rgb[5], alpha).endVertex();
        builder.vertex(matrix4f, 0.0F, 0.14F, 0.0F).color(rgb[3], rgb[4], rgb[5], alpha).endVertex();
        builder.vertex(matrix4f, 0.064F, 0.03F, 0.0F).color(rgb[3], rgb[4], rgb[5], alpha).endVertex();

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
