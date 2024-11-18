package com.gmail.jaboll.mc.client;

import com.gmail.jaboll.mc.PocketChamber;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.SkullBlock;

import java.util.Map;

public class BEWLR extends BlockEntityWithoutLevelRenderer {

    private final EntityModelSet entityModelSet;
    private Map<SkullBlock.Type, SkullModelBase> skullModels;


    public BEWLR(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.skullModels = SkullBlockRenderer.createSkullRenderers(this.entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (Screen.hasShiftDown()){
            ResolvableProfile resolvableProfile =  stack.getComponents().get(PocketChamber.PLAYER_PROFILE_COMPONENT.get());
            PlayerHeadBlock playerHeadBlock = (PlayerHeadBlock) ((BlockItem) Items.PLAYER_HEAD).getBlock();
            SkullModelBase skullmodelbase = this.skullModels.get(playerHeadBlock.getType());
            RenderType rendertype = SkullBlockRenderer.getRenderType(playerHeadBlock.getType(), resolvableProfile);
            SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, buffer, packedLight, skullmodelbase, rendertype);
        }
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }
}
