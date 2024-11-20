package com.gmail.jaboll.mc.blocks.loot;


import com.gmail.jaboll.mc.blocks.StasisChamberBlockEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class StasisChamberLootItemFunction extends LootItemConditionalFunction {

    protected StasisChamberLootItemFunction(LootItemCondition[] pPredicates) {
        super(pPredicates);
    }

    @Override
    protected ItemStack run(ItemStack pStack, LootContext pContext) {
        BlockEntity be = pContext.getParam(LootContextParams.BLOCK_ENTITY);
        if (be instanceof StasisChamberBlockEntity stasisChamberBlockEntity){
            CompoundTag tag = pStack.getOrCreateTag();
            String playerID = stasisChamberBlockEntity.getPlayerInside();
            String playerName = stasisChamberBlockEntity.getPlayerName();
            if (playerID != null) tag.putString("playerID", playerID);
            if (playerName != null) tag.putString("playerName", playerName);
        }
        return pStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return null;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<StasisChamberLootItemFunction> {

        @Override
        public StasisChamberLootItemFunction deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, LootItemCondition[] pConditions) {
            return new StasisChamberLootItemFunction(pConditions);
        }
    }
}
