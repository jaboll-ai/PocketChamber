package com.gmail.jaboll.mc.blocks;

import com.gmail.jaboll.mc.PocketChamber;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import static com.gmail.jaboll.mc.PocketChamber.PLAYER_PROFILE_COMPONENT;

public class StasisChamberBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    public StasisChamberBlockEntity(BlockPos pos, BlockState blockState) {
        super(PocketChamber.STASIS_CHAMBER_BE.get(), pos, blockState);
    }

    public boolean tryInsertPlayer(Player ply){
        ResolvableProfile playerID = this.getPlayerInside();
        if (playerID == null) {
            ResolvableProfile resolvableProfile = new ResolvableProfile(ply.getGameProfile());
            this.setComponents(DataComponentMap.builder().set(PLAYER_PROFILE_COMPONENT, resolvableProfile).build());
            this.setChanged();
            if (this.level != null){
                this.level.markAndNotifyBlock(this.getBlockPos(), this.level.getChunkAt(getBlockPos()), this.getBlockState(), this.getBlockState(), 2, 0);
            }
            return true;
        }
        return false;
    }

    public void removePlayerInside(){
        this.setComponents(DataComponentMap.EMPTY);
        this.setChanged();
        if (this.level != null){
            this.level.markAndNotifyBlock(this.getBlockPos(), this.level.getChunkAt(getBlockPos()), this.getBlockState(), this.getBlockState(), 2, 0);
        }
    }

    public boolean hasPlayerInside(){
        return this.getPlayerInside() != null;
    }

    public ResolvableProfile getPlayerInside(){
        return this.components().get(PLAYER_PROFILE_COMPONENT.get());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
