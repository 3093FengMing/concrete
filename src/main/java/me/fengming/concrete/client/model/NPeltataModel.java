package me.fengming.concrete.client.render.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.fengming.concrete.Concrete;
import me.fengming.concrete.entities.NPeltata;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class NPeltataModel extends EntityModel<NPeltata> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation(Concrete.MODID, "textures/entity/n_peltata"), "main");
    private final ModelPart body;
    private final ModelPart head;

    public NPeltataModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
    }
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).addBox(0.0001F, -2.6F, 0.0003F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0175F, 0.0F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 22).addBox(0.0002F, -9.6F, -0.0002F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0183F, -0.0001F, 0.0F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 0).addBox(0.001F, -12.29F, -4.29F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.0541F, 0.0F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -14.9F, -4.4F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.2F, 4.0F, -0.0698F, 0.0F, 0.0F));

        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 5).addBox(0.0F, -18.1F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0698F, -0.0349F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 11).addBox(1.0F, -19.0F, -4.0F, 5.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -19.0F, -4.0F, 5.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(28, 3).addBox(0.0F, -19.0F, -4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 2).addBox(-1.0F, -19.0F, -4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(14, 25).addBox(-14.0F, -0.8602F, 5.975F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -18.2F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(18, 10).addBox(5.7004F, -0.7995F, -5.0001F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(15, 24).addBox(-1.4996F, -0.4995F, -4.05F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.2F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(4, 5).addBox(-2.0F, -0.8001F, 5.993F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 23).addBox(-6.65F, -0.8002F, 5.995F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.2F, 0.0F, -0.0419F, 0.0F, 0.0F));

        PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(20, 0).addBox(-6.65F, -0.78F, -4.905F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.2F, 0.0F, 0.0454F, 0.0F, 0.0F));

        PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(28, 23).addBox(0.4902F, -0.4995F, -4.05F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-6.6996F, -0.7995F, -5.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.2F, 0.0F, 0.0F, 0.0F, -0.0436F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(NPeltata entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
