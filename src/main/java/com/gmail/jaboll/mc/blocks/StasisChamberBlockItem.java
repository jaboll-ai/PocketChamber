package com.gmail.jaboll.mc.blocks;

import com.gmail.jaboll.mc.PocketChamber;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static com.gmail.jaboll.mc.PocketChamber.PLAYER_PROFILE_COMPONENT;

public class StasisChamberBlockItem extends BlockItem {
    public StasisChamberBlockItem(Properties properties) {
        super(PocketChamber.STASIS_CHAMBER.get(), properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ResolvableProfile resProf;
        if ((resProf = stack.getComponents().get(PLAYER_PROFILE_COMPONENT.get()))!= null){
            GameProfile gameProfile = resProf.gameProfile();
            MutableComponent playerInside = Component.translatable("tooltip.pocketchamber.playerinside").withStyle(ChatFormatting.GRAY)
                    .append( Component.literal(" "+gameProfile.getName()).withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(playerInside);
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
