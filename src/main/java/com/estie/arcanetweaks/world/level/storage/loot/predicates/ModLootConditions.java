package com.estie.arcanetweaks.world.level.storage.loot.predicates;

import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ModLootConditions {
    public static final LootItemConditionType GAME_STAGE_PER_PLAYER = new LootItemConditionType(new GameStagePerPlayerCondition.Serializer());
}

