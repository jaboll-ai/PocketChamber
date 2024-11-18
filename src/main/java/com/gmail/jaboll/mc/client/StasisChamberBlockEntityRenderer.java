package com.gmail.jaboll.mc.client;

import com.gmail.jaboll.mc.blocks.StasisChamberBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StasisChamberBlockEntityRenderer implements BlockEntityRenderer<StasisChamberBlockEntity> {

    private final BlockEntityRendererProvider.Context ctx;

    public StasisChamberBlockEntityRenderer(BlockEntityRendererProvider.Context ctx){this.ctx = ctx;}

    @Override
    public void render(StasisChamberBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
//        this.ctx.getItemRenderer().render(Items.ENDER_PEARL, ItemDisplayContext.GROUND, false, poseStack, packedLight, packedOverlay,
//                );
        Level lvl = blockEntity.getLevel();
        long tickTime = lvl.getGameTime();
        double yOffset = Math.sin((tickTime+ partialTick)/3)/5 + ((int)((tickTime + partialTick) / 120) % 4) * 0.02;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.75 + yOffset, 0.5);
        this.ctx.getItemRenderer().renderStatic(new ItemStack(Items.ENDER_PEARL), ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(StasisChamberBlockEntity blockEntity, Vec3 cameraPos) {
        if (!blockEntity.hasPlayerInside()) return false;
        if (blockEntity.getLevel() == null) return false;
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

}
