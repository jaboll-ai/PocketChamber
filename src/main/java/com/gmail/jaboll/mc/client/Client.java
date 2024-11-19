package com.gmail.jaboll.mc.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.gmail.jaboll.mc.blocks.particle.PocketChamberParticleProvider;

import static com.gmail.jaboll.mc.PocketChamber.*;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Client {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);



    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    STASIS_CHAMBER_ITEM.get(),
                    ResourceLocation.fromNamespaceAndPath(MODID, "playerinside"),
                    (stack, level, player, seed) -> stack.getComponents().get(PLAYER_PROFILE_COMPONENT.get()) == null ? 0 : 1
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