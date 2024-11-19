package com.gmail.jaboll.mc.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import com.gmail.jaboll.mc.blocks.particle.PocketChamberParticleProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.gmail.jaboll.mc.PocketChamber.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Client {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);



    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    STASIS_CHAMBER_ITEM.get(),
                    new ResourceLocation(MODID, "playerinside"),
                    (stack, level, player, seed) -> stack.getOrCreateTag().contains("playerID") ? 1 : 0
            );
        });
    }
    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(STASIS_CHAMBER_BE.get(), StasisChamberBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(STASIS_CHAMBER_PARTICLE.get(), PocketChamberParticleProvider::new);
    }
}