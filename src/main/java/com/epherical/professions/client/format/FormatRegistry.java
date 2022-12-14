package com.epherical.professions.client.format;

import com.epherical.professions.Constants;
import com.epherical.professions.client.entry.ArrayEntry;
import com.epherical.professions.client.entry.CompoundAwareEntry;
import com.epherical.professions.client.entry.CompoundEntry;
import com.epherical.professions.client.entry.DatapackEntry;
import com.epherical.professions.client.entry.NumberEntry;
import com.epherical.professions.client.entry.RegistryEntry;
import com.epherical.professions.client.entry.StringEntry;
import com.epherical.professions.mixin.accessor.EnchantmentPredicateAccess;
import com.epherical.professions.profession.action.AbstractAction;
import com.epherical.professions.profession.action.builtin.blocks.BlockAbstractAction;
import com.epherical.professions.profession.action.builtin.blocks.BreakBlockAction;
import com.epherical.professions.profession.action.builtin.blocks.PlaceBlockAction;
import com.epherical.professions.profession.action.builtin.blocks.TntDestroyAction;
import com.epherical.professions.profession.action.builtin.entity.AbstractEntityAction;
import com.epherical.professions.profession.action.builtin.entity.BreedAction;
import com.epherical.professions.profession.action.builtin.entity.KillAction;
import com.epherical.professions.profession.action.builtin.entity.TameAction;
import com.epherical.professions.profession.action.builtin.items.AbstractItemAction;
import com.epherical.professions.profession.action.builtin.items.BrewAction;
import com.epherical.professions.profession.action.builtin.items.CraftingAction;
import com.epherical.professions.profession.action.builtin.items.EnchantAction;
import com.epherical.professions.profession.action.builtin.items.FishingAction;
import com.epherical.professions.profession.action.builtin.items.SmeltItemAction;
import com.epherical.professions.profession.action.builtin.items.TakeSmeltAction;
import com.epherical.professions.profession.action.builtin.items.TradeAction;
import com.epherical.professions.profession.conditions.ActionCondition;
import com.epherical.professions.profession.conditions.ActionConditionType;
import com.epherical.professions.profession.conditions.ActionConditions;
import com.epherical.professions.profession.rewards.Reward;
import com.epherical.professions.profession.rewards.RewardType;
import com.epherical.professions.profession.rewards.Rewards;
import com.epherical.professions.profession.rewards.builtin.ItemReward;
import com.epherical.professions.profession.rewards.builtin.OccupationExperience;
import com.epherical.professions.profession.rewards.builtin.PaymentReward;
import com.epherical.professions.profession.unlock.builtin.BlockBreakUnlock;
import com.epherical.professions.profession.unlock.builtin.BlockDropUnlock;
import com.epherical.professions.profession.unlock.builtin.ToolUnlock;
import com.epherical.professions.util.ActionEntry;
import com.epherical.professions.util.EnchantmentContainer;
import com.google.common.collect.Lists;
import com.mojang.serialization.Lifecycle;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.epherical.professions.RegistryConstants.*;

public class FormatRegistry {

    public static final ResourceKey<Registry<FormatBuilder<?>>> BUILDER_KEY = ResourceKey.createRegistryKey(Constants.modID("builder"));
    public static final Registry<FormatBuilder<?>> BUILDERS = new MappedRegistry<>(BUILDER_KEY, Lifecycle.experimental(), null);

    public static final FormatBuilder<OccupationExperience> FB_OC_EXP = register(formatID(REWARD_KEY, "occupation_exp"), new RewardFormats.OccupationExp());
    public static final FormatBuilder<ItemReward> FB_ITEM_REWARD = register(formatID(REWARD_KEY, "item"), new RewardFormats.Item());
    public static final FormatBuilder<PaymentReward> FB_PAYMENT_REWARD = Registry.register(BUILDERS, formatID(REWARD_KEY, "payment"), new RewardFormats.Payment());

    public static final FormatBuilder<BlockDropUnlock> FB_BLOCK_DROP_UNLOCK = register(formatID(UNLOCK_KEY, "block_drop"), new UnlockFormats.BlockDrop());
    public static final FormatBuilder<BlockBreakUnlock> FB_BLOCK_BREAK_UNLOCK = register(formatID(UNLOCK_KEY, "block_break"), new UnlockFormats.BlockBreak());
    public static final FormatBuilder<ToolUnlock> FB_TOOL_UNLOCK = register(formatID(UNLOCK_KEY, "tool_unlock"), new UnlockFormats.Tool());


    public static final FormatBuilder<BreakBlockAction> FB_BREAK_BLOCK = register(formatID(ACTION_TYPE_KEY, "break_block"), breakBlockAction ->
            createBlockFormat(BreakBlockAction.class));
    public static final FormatBuilder<PlaceBlockAction> FB_PLACE_BLOCK = register(formatID(ACTION_TYPE_KEY, "place_block"), placeBlockAction ->
            createBlockFormat(PlaceBlockAction.class));
    public static final FormatBuilder<TntDestroyAction> FB_TNT_DESTROY = register(formatID(ACTION_TYPE_KEY, "tnt_destroy"), tntDestroyAction ->
            createBlockFormat(TntDestroyAction.class));

    public static final FormatBuilder<BreedAction> FB_BREED_ACTION = register(formatID(ACTION_TYPE_KEY, "breed"), breedAction ->
            createEntityAction(BreedAction.class));
    public static final FormatBuilder<KillAction> FB_KILL_ACTION = register(formatID(ACTION_TYPE_KEY, "kill_entity"), killAction ->
            createEntityAction(KillAction.class));
    public static final FormatBuilder<TameAction> FB_TAME_ACTION = register(formatID(ACTION_TYPE_KEY, "tame"), tameAction ->
            createEntityAction(TameAction.class));

    public static final FormatBuilder<FishingAction> FISH_FORMAT = register(formatID(ACTION_TYPE_KEY, "catch_fish"), o ->
            createItemAction(FishingAction.class));
    public static final FormatBuilder<CraftingAction> CRAFT_ITEM_FORMAT = register(formatID(ACTION_TYPE_KEY, "craft_item"), o ->
            createItemAction(CraftingAction.class));
    public static final FormatBuilder<TakeSmeltAction> TAKE_COOKED_FORMAT = register(formatID(ACTION_TYPE_KEY, "take_smelted_item"), o ->
            createItemAction(TakeSmeltAction.class));
    public static final FormatBuilder<SmeltItemAction> ON_ITEM_COOKED_FORMAT = register(formatID(ACTION_TYPE_KEY, "on_item_smelted"), o ->
            createItemAction(SmeltItemAction.class));
    public static final FormatBuilder<BrewAction> BREW_ITEM_FORMAT = register(formatID(ACTION_TYPE_KEY, "brew"), o ->
            createItemAction(BrewAction.class));
    public static final FormatBuilder<TradeAction> TRADE_FORMAT = register(formatID(ACTION_TYPE_KEY, "villager_trade"), o ->
            createItemAction(TradeAction.class));


    public static <T extends BlockAbstractAction> Format<T> createBlockFormat(Class<T> clazz) {
        return new RegularFormat<>((embed, y, width) -> new FormatEntryBuilder<T>()
                .addEntry(arrayBlockString(embed, y, width, "blocks", (o, entry) -> {
                    for (ActionEntry<Block> block : o.getBlocks()) {
                        for (String s : block.serializeString(Registry.BLOCK)) {
                            StringEntry<String> entry1 = entry.createEntry();
                            entry1.deserialize(s);
                            entry.addEntry(entry1);
                        }
                    }
                }, clazz))
                .addEntry(commonActionEntries(embed, y, width)));
    }

    public static <T extends AbstractEntityAction> Format<T> createEntityAction(Class<T> clazz) {
        return new RegularFormat<>((embed, y, width) -> new FormatEntryBuilder<T>()
                .addEntry(arrayBlockString(embed, y, width, "entities", (o, entry) -> {
                    for (ActionEntry<EntityType<?>> block : o.getEntities()) {
                        for (String s : block.serializeString(Registry.ENTITY_TYPE)) {
                            StringEntry<String> entry1 = entry.createEntry();
                            entry1.deserialize(s);
                            entry.addEntry(entry1);
                        }
                    }
                }, clazz))
                .addEntry(commonActionEntries(embed, y, width)));
    }

    public static <T extends AbstractItemAction> Format<T> createItemAction(Class<T> clazz) {
        return new RegularFormat<>((embed, y, width) -> new FormatEntryBuilder<T>()
                .addEntry(arrayBlockString(embed, y, width, "items", (o, entry) -> {
                    for (ActionEntry<Item> item : o.getItems()) {
                        for (String s : item.serializeString(Registry.ITEM)) {
                            StringEntry<String> entry1 = entry.createEntry();
                            entry1.deserialize(s);
                            entry.addEntry(entry1);
                        }
                    }
                }, clazz))
                .addEntry(commonActionEntries(embed, y, width)));
    }

    public static final FormatBuilder<EnchantAction> ENCHANT_ITEM_FORMAT = register(formatID(ACTION_TYPE_KEY, "enchant"), action ->
            new RegularFormat<>(((embed, y, width) -> {
                FormatEntryBuilder<EnchantAction> builder = new FormatEntryBuilder<>();
                builder.addEntry(createItemAction(EnchantAction.class).entries().apply(embed, y, width).build());
                builder.addEntry(createEnchantArrayEntry(embed, y, width, "enchants", (o1, entry) -> {
                    for (EnchantmentContainer enchantment : o1.getEnchantments()) {
                        StringEntry<String> entry1 = entry.createEntry();
                        String key = Registry.ENCHANTMENT.getKey(enchantment.enchantment()).toString() + "#" + enchantment.level();
                        entry1.setValue(key);
                        entry.addEntry(entry1);
                    }
                }, EnchantAction.class));
                return builder;
            })));


    public static <T> FormatBuilder<T> register(ResourceLocation id, FormatBuilder<T> format) {
        return Registry.register(BUILDERS, id, format);
    }

    public static <T> ResourceLocation formatID(ResourceKey<T> registryKey, String id) {
        return formatID(registryKey, "professions", id);
    }

    public static <T> ResourceLocation formatID(ResourceKey<T> registryKey, String modID, String name) {
        return formatID(registryKey.location().toString().replaceAll(":", "_"), modID, name);
    }

    public static ResourceLocation formatID(String registryKey, String modID, String name) {
        return new ResourceLocation(modID, registryKey + "_" + name);
    }

    @Nullable
    public static <T, OBJ> FormatBuilder<OBJ> grabBuilder(Registry<T> registry, T value) {
        ResourceLocation valueID = registry.getKey(value);
        if (valueID != null) {
            ResourceLocation formatLocation = formatID(registry.key(), valueID.getNamespace(), valueID.getPath());
            return (FormatBuilder<OBJ>) BUILDERS.get(formatLocation);
        }
        return null;
    }

    public static void init() {
        ActionConditionFormats.init();
    }

    public static <T> ArrayEntry<T, StringEntry<String>> arrayBlockString(int x, int y, int width, String usage,
                                                                          DatapackEntry.Deserializer<T, ArrayEntry<T, StringEntry<String>>> deserializer, Class<T> genericHelp) {
        return new ArrayEntry<>(x, y, width, usage, (x1, y2, wid) -> {
            return new StringEntry<>(x1, y2, wid, "blocks", "minecraft:stone", Optional.of("blocks"), (s, entry) -> {
                entry.setValue(s);
            }, DatapackEntry.Type.REMOVE);
        }, deserializer);
    }

    public static <T> ArrayEntry<T, StringEntry<String>> arrayItemString(int x, int y, int width, String usage,
                                                                         DatapackEntry.Deserializer<T, ArrayEntry<T, StringEntry<String>>> deserializer, Class<T> genericHelp) {
        return new ArrayEntry<>(x, y, width, usage, (x1, y2, wid) -> {
            return new StringEntry<>(x1, y2, wid, "items", "minecraft:stone_sword", Optional.of("items"), (s, entry) -> {
                entry.setValue(s);
            }, DatapackEntry.Type.REMOVE);
        }, deserializer);
    }

    private static <T> ArrayEntry<T, StringEntry<String>> createEnchantArrayEntry(int x, int y, int width, String usage,
                                                                                  DatapackEntry.Deserializer<T, ArrayEntry<T, StringEntry<String>>> deserializer, Class<T> genericHelp) {
        return new ArrayEntry<>(x, y, width, usage, (x1, y2, wid) -> {
            return new StringEntry<>(x1, y2, wid, "enchants", "minecraft:sharpness#2", Optional.of("enchants"), (s, entry) -> {
                entry.setValue(s);
            }, DatapackEntry.Type.REMOVE);
        }, deserializer);
    }

    public static <T> ArrayEntry<T, CompoundEntry<EnchantmentPredicate>> createEnchantmentPredicateArray(int x, int y, int width, String usage,
                                                                                                         DatapackEntry.Deserializer<T, ArrayEntry<T, CompoundEntry<EnchantmentPredicate>>> deserializer) {
        return new ArrayEntry<>(x, y, width, usage, (x1, y2, wid) -> {
            return new CompoundEntry<>(x1, y2, wid, Optional.of(usage), Lists.newArrayList(
                    new StringEntry<>(x1 + 8, y2, wid - 8, "enchantment", "minecraft:sharpness", (o, entry) -> {
                        EnchantmentPredicateAccess access = (EnchantmentPredicateAccess) o;
                        if (access.getEnchantment() != null) {
                            entry.setValue(Registry.ENCHANTMENT.getKey(access.getEnchantment()).toString());
                        }
                    }),
                    new CompoundEntry<>(x1 + 8, y, width - 8, Optional.of("levels"),
                            Lists.newArrayList(
                                    new NumberEntry<>(x1 + 8, y, width - 8, "min", 0, (o, entry) -> {
                                        EnchantmentPredicateAccess predicate = (EnchantmentPredicateAccess) o;
                                        Integer min = predicate.getLevel().getMin();
                                        if (min != null) {
                                            entry.setValue(String.valueOf(min));
                                        } else {
                                            entry.setValue("");
                                        }
                                    }),
                                    new NumberEntry<>(x1 + 8, y, width - 8, "max", 0, (o, entry) -> {
                                        EnchantmentPredicateAccess predicate = (EnchantmentPredicateAccess) o;
                                        Integer max = predicate.getLevel().getMax();
                                        if (max != null) {
                                            entry.setValue(String.valueOf(max));
                                        } else {
                                            entry.setValue("");
                                        }
                                    })
                            ),
                            (o, entry) -> {
                                for (DatapackEntry<EnchantmentPredicate, ?> entryEntry : entry.getEntries()) {
                                    entryEntry.deserialize(o);
                                }
                            })
            ), (t, entry) -> {
                for (DatapackEntry<EnchantmentPredicate, ?> entryEntry : entry.getEntries()) {
                    entryEntry.deserialize(t);
                }
            }, DatapackEntry.Type.REMOVE);
        }, deserializer);
    }

    public static <T extends AbstractAction> List<DatapackEntry<T, ?>> commonActionEntries(int embed, int y, int width) {
        return Lists.newArrayList(
                new ArrayEntry<T, CompoundAwareEntry<Reward, RewardType>>(embed, y, width, "rewards", (x1, y2, wid) -> {
                    return new CompoundAwareEntry<>(embed, y, width, x1, wid, REWARD_KEY,
                            new RegistryEntry<>(x1, y, wid, REWARDS, Rewards.EXPERIENCE_REWARD, Optional.of("reward"),
                                    (reward, entry) -> entry.setValue(reward.getType()), DatapackEntry.Type.REMOVE),
                            (reward, entry) -> entry.getEntry().deserialize(reward));
                }, (o, entry) -> {
                    for (Reward reward : o.getRewards()) {
                        CompoundAwareEntry<Reward, RewardType> entry1 = entry.createEntry();
                        entry1.deserialize(reward);
                        entry.addEntry(entry1);
                    }
                }),
                new ArrayEntry<T, CompoundAwareEntry<ActionCondition, ActionConditionType>>(embed, y, width, "conditions", (x1, y2, wid) -> {
                    return new CompoundAwareEntry<>(embed, y, width, x1, wid, ACTION_CONDITION_KEY,
                            new RegistryEntry<>(x1, y, wid, ACTION_CONDITION_TYPE, ActionConditions.TOOL_MATCHES, Optional.of("condition"),
                                    (con, entry) -> entry.setValue(con.getType()), DatapackEntry.Type.REMOVE),
                            (con, entry) -> entry.getEntry().deserialize(con));
                }, (o, entry) -> {
                    for (ActionCondition condition : o.getConditions()) {
                        CompoundAwareEntry<ActionCondition, ActionConditionType> entry1 = entry.createEntry();
                        entry1.deserialize(condition);
                        entry.addEntry(entry1);
                    }
                }));
    }

}
