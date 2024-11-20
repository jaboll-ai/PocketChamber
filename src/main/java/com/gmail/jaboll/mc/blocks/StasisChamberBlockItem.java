package com.gmail.jaboll.mc.blocks;

import com.gmail.jaboll.mc.PocketChamber;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class StasisChamberBlockItem extends BlockItem {
    public StasisChamberBlockItem(Properties properties) {
        super(PocketChamber.STASIS_CHAMBER.get(), properties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if(pStack.getOrCreateTag().contains("playerID")){
            MutableComponent playerInside = Component.translatable("tooltip.pocketchamber.playerinside").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(" "+pStack.getTag().getString("playerName")).withStyle(ChatFormatting.GOLD));
            pTooltip.add(playerInside);
        }
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
