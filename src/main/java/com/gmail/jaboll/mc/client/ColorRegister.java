package com.gmail.jaboll.mc.client;

import com.gmail.jaboll.mc.PocketChamber;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static com.gmail.jaboll.mc.PocketChamber.STASIS_CHAMBER;
import static com.gmail.jaboll.mc.PocketChamber.STASIS_CHAMBER_ITEM;

@Mod.EventBusSubscriber(modid = PocketChamber.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorRegister {
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1,
                STASIS_CHAMBER.get());
    }

    @SubscribeEvent
    public static void registerItemBlockColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> 4159204, STASIS_CHAMBER_ITEM.get());
    }
}
