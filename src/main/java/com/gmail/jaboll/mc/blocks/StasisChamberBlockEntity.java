package com.gmail.jaboll.mc.blocks;

import com.gmail.jaboll.mc.PocketChamber;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import static com.gmail.jaboll.mc.PocketChamber.PLAYER_ID_COMPONENT;

public class StasisChamberBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    public StasisChamberBlockEntity(BlockPos pos, BlockState blockState) {
        super(PocketChamber.STASIS_CHAMBER_BE.get(), pos, blockState);
    }

    public boolean tryInsertPlayer(Player ply){
        String playerID = this.getPlayerInside();
        if (playerID.isEmpty()) {
            this.setComponents(DataComponentMap.builder().set(PLAYER_ID_COMPONENT, ply.getUUID().toString()).build());
            this.setChanged();
            return true;
        }
        return false;
    }

    public boolean hasPlayerInside(){
        return !this.getPlayerInside().isEmpty();
    }

    public String getPlayerInside(){
        return this.components().getOrDefault(PLAYER_ID_COMPONENT.value(), "");
    }
}
