package com.gmail.jaboll.mc;

import com.gmail.jaboll.mc.blocks.StasisChamberBlock;
import com.gmail.jaboll.mc.blocks.StasisChamberBlockEntity;
import com.gmail.jaboll.mc.blocks.StasisChamberBlockItem;

import com.gmail.jaboll.mc.event.PCProjectileImpact;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PocketChamber.MODID)
public class PocketChamber {
    public static final String MODID = "pocketchamber";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PocketChamber.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);


    //BLOCKS
    public static final RegistryObject<StasisChamberBlock> STASIS_CHAMBER = BLOCKS.register("stasis_chamber",
            () -> new StasisChamberBlock(BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    //BE
    public static final Supplier<BlockEntityType<StasisChamberBlockEntity>> STASIS_CHAMBER_BE = BLOCK_ENTITIES.register("stasis_chamber_be",
            () -> BlockEntityType.Builder.of(StasisChamberBlockEntity::new, STASIS_CHAMBER.get()).build(null));
    //ITEMS
    public static final RegistryObject<BlockItem> STASIS_CHAMBER_ITEM = ITEMS.register("stasis_chamber", () -> new StasisChamberBlockItem(new Item.Properties()));
    //Particles
    public static final Supplier<SimpleParticleType> STASIS_CHAMBER_PARTICLE = PARTICLES.register("stasis_chamber_particle",
            ()-> new SimpleParticleType(false));

    public static final RegistryObject<CreativeModeTab> PC_TAB = CREATIVE_MODE_TABS.register(MODID,
            () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.pocketchamber")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> STASIS_CHAMBER_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(STASIS_CHAMBER_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public PocketChamber() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        PARTICLES.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(PCProjectileImpact::onProjectileHit);

    }
}
