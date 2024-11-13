package com.gmail.jaboll.mc.event;

import com.gmail.jaboll.mc.PocketChamber;
import com.gmail.jaboll.mc.blocks.StasisChamberBlockEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import org.slf4j.Logger;


public class PCProjectileImpact {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void onProjectileHit(ProjectileImpactEvent evt) {
        if(!(evt.getProjectile() instanceof ThrownEnderpearl)) {
            return; //return early if not an EP
        }
        if(evt.getProjectile().getOwner() instanceof Player thrower && evt.getRayTraceResult() instanceof BlockHitResult hitResult){
            Level lvl = thrower.level();
            BlockState block = lvl.getBlockState(hitResult.getBlockPos());
            if (!block.is(PocketChamber.STASIS_CHAMBER.get())){
                return; //Not a stasis chamber
            }
            if (lvl.getBlockEntity(hitResult.getBlockPos()) instanceof StasisChamberBlockEntity be){
                boolean cancel = be.tryInsertPlayer(thrower);
                if (!cancel){
                    return; // Return early to not cancel [only cancels if someone is already inside the sc]
                }
                evt.getEntity().discard();
                evt.setCanceled(true);
            }
        }
    }
}
