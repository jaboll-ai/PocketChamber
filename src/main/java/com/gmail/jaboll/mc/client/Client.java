package com.gmail.jaboll.mc.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
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

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().computeIfPresent(
                // The model resource location of the model to modify.
                ModelResourceLocation.inventory(
                        ResourceLocation.fromNamespaceAndPath("pocketchamber", "stasis_chamber")
                ),
                // A BiFunction with the location and the original models as parameters, returning the new model.
                (location, model) -> new BakedModelWrapper<>(model) {
                    @Override
                    public boolean isCustomRenderer() {
                        return true; // Ensure custom rendering
                    }
                }
        );
    }

    @SubscribeEvent
    public static void registerClientExtension(RegisterClientExtensionsEvent event){
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new BEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        }, STASIS_CHAMBER_ITEM);
    }
}