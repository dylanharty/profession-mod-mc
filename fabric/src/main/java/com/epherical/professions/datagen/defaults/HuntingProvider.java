package com.epherical.professions.datagen.defaults;

import com.epherical.professions.datagen.NamedProfessionBuilder;
import com.epherical.professions.profession.ProfessionBuilder;
import com.epherical.professions.profession.action.builtin.ExploreBiomeAction;
import com.epherical.professions.profession.action.builtin.entity.KillAction;
import com.epherical.professions.profession.action.builtin.entity.TameAction;
import com.epherical.professions.profession.modifiers.perks.Perks;
import com.epherical.professions.profession.modifiers.perks.builtin.ScalingAttributePerk;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static com.epherical.professions.profession.action.Actions.*;

public class HuntingProvider extends NamedProfessionBuilder {

    public HuntingProvider() {
        super(ProfessionBuilder.profession(
                TextColor.parseColor("#a6542e"),
                TextColor.parseColor("#FFFFFF"),
                new String[]{
                        "Earn money and experience",
                        "by hunting animals, killing monsters, and exploring"},
                "Hunting", 100));
    }


    @Override
    public void addData(ProfessionBuilder builder) {
        builder.addExperienceScaling(defaultLevelParser())
                .incomeScaling(defaultIncomeParser())
                .addAction(EXPLORE_BIOME, ExploreBiomeAction.explore()
                        .biome(ConventionalBiomeTags.IN_OVERWORLD)
                        .reward(moneyReward(1))
                        .reward(expReward(2)))
                .addAction(EXPLORE_BIOME, ExploreBiomeAction.explore()
                        .biome(ConventionalBiomeTags.IN_NETHER)
                        .reward(moneyReward(1))
                        .reward(expReward(2)))
                .addAction(EXPLORE_BIOME, ExploreBiomeAction.explore()
                        .biome(ConventionalBiomeTags.IN_THE_END)
                        .reward(moneyReward(1))
                        .reward(expReward(2)))
                .addAction(KILL_ENTITY, KillAction.kill()
                        .entity(EntityType.PIG, EntityType.CHICKEN, EntityType.SHEEP, EntityType.COW, EntityType.MOOSHROOM, EntityType.RABBIT)
                        .reward(moneyReward(8))
                        .reward(expReward(8))
                        .build())
                .addAction(KILL_ENTITY, KillAction.kill()
                        .entity(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SHULKER)
                        .reward(moneyReward(12))
                        .reward(expReward(12))
                        .build())
                .addAction(KILL_ENTITY, KillAction.kill()
                        .entity(EntityType.BLAZE, EntityType.WITHER_SKELETON, EntityType.PIGLIN_BRUTE, EntityType.HOGLIN, EntityType.PHANTOM)
                        .reward(moneyReward(15))
                        .reward(expReward(15))
                        .build())
                .addAction(KILL_ENTITY, KillAction.kill()
                        .entity(EntityType.WITHER)
                        .reward(moneyReward(100))
                        .reward(expReward(100))
                        .build())
                .addAction(KILL_ENTITY, KillAction.kill()
                        .entity(EntityType.ENDER_DRAGON)
                        .reward(moneyReward(600))
                        .reward(expReward(600))
                        .build())
                .addAction(TAME_ENTITY, TameAction.tame()
                        .entity(EntityType.WOLF)
                        .reward(moneyReward(15))
                        .reward(expReward(15))
                        .build());
        builder.addPerk(Perks.SCALING_ATTRIBUTE_PERK, ScalingAttributePerk.scaling()
                .level(1).attribute(Attributes.MAX_HEALTH).increaseBy(0.20));
        builder.addPerk(Perks.SCALING_ATTRIBUTE_PERK, ScalingAttributePerk.scaling()
                .level(10).attribute(Attributes.KNOCKBACK_RESISTANCE).increaseBy(0.002));
    }
}
