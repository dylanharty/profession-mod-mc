package com.epherical.professions.profession.rewards.builtin;

import com.epherical.octoecon.api.Currency;
import com.epherical.octoecon.api.Economy;
import com.epherical.octoecon.api.user.UniqueUser;
import com.epherical.professions.CommonPlatform;
import com.epherical.professions.config.ProfessionConfig;
import com.epherical.professions.profession.ProfessionContext;
import com.epherical.professions.profession.ProfessionParameter;
import com.epherical.professions.profession.action.Action;
import com.epherical.professions.profession.progression.Occupation;
import com.epherical.professions.profession.rewards.Reward;
import com.epherical.professions.profession.rewards.RewardType;
import com.epherical.professions.profession.rewards.Rewards;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.Serializer;
import org.slf4j.Logger;

import java.util.UUID;

public record PaymentReward(double amount, ResourceLocation currency) implements Reward {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public RewardType getType() {
        return Rewards.PAYMENT_REWARD;
    }

    @Override
    public void giveReward(ProfessionContext context, Action action, Occupation occupation) {

    }

    @Override
    public Component rewardChatInfo() {
        return Component.literal(String.format("$%.2f", amount)).setStyle(Style.EMPTY.withColor(ProfessionConfig.money));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class RewardSerializer implements Serializer<PaymentReward> {

        @Override
        public void serialize(JsonObject json, PaymentReward value, JsonSerializationContext serializationContext) {
            json.addProperty("amount", value.amount);
            json.addProperty("currency", value.currency.toString());
            json.addProperty("currency", "eights_economy:dollars");
        }

        @Override
        public PaymentReward deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            double amount = GsonHelper.getAsDouble(json, "amount");
            String currencyString = GsonHelper.getAsString(json, "currency");
            ResourceLocation resourceKey = new ResourceLocation(currencyString);
            return new PaymentReward(amount, resourceKey);
        }
    }

    public static class Builder implements Reward.Builder {
        private double amount;
        private Currency currency;

        public Builder money(double amount, Currency currency) {
            this.amount = amount;
            this.currency = currency;
            return this;
        }

        @Override
        public Reward build() {
            ResourceLocation location = new ResourceLocation(ProfessionConfig.overriddenCurrencyID);
            if (currency != null) {
                // is null
            }
            return new PaymentReward(amount, location);
        }
    }
}
