package com.gmail.jaboll.mc.blocks;

import com.gmail.jaboll.mc.PocketChamber;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;


public class StasisChamberBlockEntity extends BlockEntity {
    private String playerID;
    private String playerName;
    private static final Logger LOGGER = LogUtils.getLogger();
    public StasisChamberBlockEntity(BlockPos pos, BlockState blockState) {
        super(PocketChamber.STASIS_CHAMBER_BE.get(), pos, blockState);
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
        this.setChanged();
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        this.setChanged();
    }

    public boolean tryInsertPlayer(Player ply){
        if (this.playerID == null) {
            this.playerID = ply.getStringUUID();
            this.playerName = ply.getScoreboardName();
            this.setChanged();
            if (this.level != null){
                this.level.markAndNotifyBlock(this.getBlockPos(), this.level.getChunkAt(getBlockPos()), this.getBlockState(), this.getBlockState(), 2, 0);
            }
            return true;
        }
        return false;
    }

    public void removePlayerInside(){
        this.playerID = null;
        this.playerName = null;
        this.setChanged();
        if (this.level != null){
            this.level.markAndNotifyBlock(this.getBlockPos(), this.level.getChunkAt(getBlockPos()), this.getBlockState(), this.getBlockState(), 2, 0);
        }
    }

    public boolean hasPlayerInside(){
        return this.getPlayerInside() != null;
    }

    public String getPlayerInside(){
        return this.playerID;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag pTag) {
        this.playerID = pTag.getString("playerID");
        this.playerName = pTag.getString("playerName");
        super.load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        if (this.playerID!=null){
            pTag.putString("playerID", this.playerID);
        } else pTag.remove("playerID");
        if (this.playerName!=null){
            pTag.putString("playerName", this.playerName);
        } else pTag.remove("playerName");
        super.saveAdditional(pTag);
    }
}
