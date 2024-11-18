package com.gmail.jaboll.mc;

import com.gmail.jaboll.mc.blocks.StasisChamberBlock;
import com.gmail.jaboll.mc.blocks.StasisChamberBlockEntity;
import com.gmail.jaboll.mc.blocks.particle.PocketChamberParticleProvider;
import com.gmail.jaboll.mc.client.StasisChamberBlockEntityRenderer;
import com.gmail.jaboll.mc.event.PCProjectileImpact;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PocketChamber.MODID)
public class PocketChamber {
    public static final String MODID = "pocketchamber";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PocketChamber.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "pocketchamber");
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);


    //BLOCKS
    public static final DeferredBlock<StasisChamberBlock> STASIS_CHAMBER = BLOCKS.registerBlock("stasis_chamber",
            StasisChamberBlock::new, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion());
    //BE
    public static final Supplier<BlockEntityType<StasisChamberBlockEntity>> STASIS_CHAMBER_BE = BLOCK_ENTITIES.register("stasis_chamber_be",
            () -> new BlockEntityType<>(StasisChamberBlockEntity::new, STASIS_CHAMBER.get()));
    //Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>>  PLAYER_ID_COMPONENT = DATA_COMPONENTS.registerComponentType("player_inside",
            builder -> builder.persistent(Codec.STRING));
    //ITEMS
    public static final DeferredItem<BlockItem> STASIS_CHAMBER_ITEM = ITEMS.registerItem("stasis_chamber",
            properties -> new BlockItem(STASIS_CHAMBER.get(), properties.component(PLAYER_ID_COMPONENT, "")));
    //Particles
    public static final Supplier<SimpleParticleType> STASIS_CHAMBER_PARTICLE = PARTICLES.register("stasis_chamber_particle",
            ()-> new SimpleParticleType(false));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab",
            () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.pocketchamber")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> STASIS_CHAMBER_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(STASIS_CHAMBER_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public PocketChamber(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        PARTICLES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this); // not necessary only for below @nnotated fucntions
        NeoForge.EVENT_BUS.addListener(PCProjectileImpact::onProjectileHit);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

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
}
