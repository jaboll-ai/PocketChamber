package com.gmail.jaboll.mc.client;

import com.gmail.jaboll.mc.PocketChamber;
import net.minecraft.client.renderer.BiomeColors;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static com.gmail.jaboll.mc.PocketChamber.STASIS_CHAMBER;
import static com.gmail.jaboll.mc.PocketChamber.STASIS_CHAMBER_ITEM;

@EventBusSubscriber(modid = PocketChamber.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorRegister {
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1,
                STASIS_CHAMBER.value());
    }

    @SubscribeEvent
    public static void registerItemBlockColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> 4159204, STASIS_CHAMBER_ITEM.get());
    }
}
