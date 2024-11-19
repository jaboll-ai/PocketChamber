package com.gmail.jaboll.mc.blocks;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

import java.util.Set;

import static com.gmail.jaboll.mc.PocketChamber.*;

public class StasisChamberBlock extends Block implements EntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public StasisChamberBlock(Properties properties) {
		super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(2, 0, 2, 14, 16, 14);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new StasisChamberBlockEntity(blockPos, blockState);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if(blockentity instanceof StasisChamberBlockEntity scblockentity){
            if(!level.isClientSide && player.isCreative() && scblockentity.hasPlayerInside()){
                ItemStack itemstack = new ItemStack(STASIS_CHAMBER.get());
                itemstack.applyComponents(scblockentity.collectComponents());
                ItemEntity itementity = new ItemEntity(
                        level, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, itemstack
                );
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            }

        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof StasisChamberBlockEntity scblockentity) {
            if (scblockentity.hasPlayerInside()) {
                if (!level.isClientSide()) {
                    GameProfile gameProfile = scblockentity.getPlayerInside().gameProfile();
                    Player thrower = level.getServer().getPlayerList().getPlayer(gameProfile.getId());
                    if (thrower != null) {
                        if (thrower.level() == level){
                            thrower.teleportTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                        }
                        else{
                            thrower.teleportTo((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Set.of(), thrower.getYRot(), thrower.getXRot());
                        }
                        level.playSound(thrower, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.9F, 1F);
                        if (!player.isCreative()) scblockentity.removePlayerInside();
                    } else player.displayClientMessage(Component.literal(gameProfile.getName()+" ").withStyle(ChatFormatting.GOLD)
                            .append(Component.translatable("chat.pocketchamber.playerunavailable").withStyle(ChatFormatting.GRAY)), true);
                } else {
                    Player thrower = level.getPlayerByUUID(scblockentity.getPlayerInside().gameProfile().getId());
                    if (thrower != null) {
                        level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.9F, 1F, false);
                        if (!player.isCreative()) scblockentity.removePlayerInside();
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof StasisChamberBlockEntity stasisChamberBlockEntity) {
            if (!stasisChamberBlockEntity.hasPlayerInside()) return;
        }
        level.addParticle(STASIS_CHAMBER_PARTICLE.get(), pos.getX()+0.5, pos.getY()+0.2, pos.getZ()+0.5, 0, 0.4, 0);
        if (random.nextInt(3) == 0) {
            level.playLocalSound(
                    pos.getX()+0.5,
                    pos.getY()+0.7,
                    pos.getZ()+0.5,
                    SoundEvents.GENERIC_SPLASH,
                    SoundSource.BLOCKS,
                    0.015F + random.nextFloat() * 0.05F,
                    0.9F + random.nextFloat() * 0.15F,
                    false
            );
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof StasisChamberBlockEntity stasisChamberBlockEntity){
            ItemStack stack = new ItemStack(this);
            stack.applyComponents(stasisChamberBlockEntity.components());
            return stack;
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }
}
