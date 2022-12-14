package com.epherical.professions.util;

import com.epherical.professions.api.ProfessionalPlayer;
import com.epherical.professions.config.ProfessionConfig;
import com.epherical.professions.profession.unlock.Unlock;
import com.epherical.professions.profession.unlock.UnlockType;
import com.epherical.professions.profession.unlock.Unlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public class ProfessionUtil {

    /**
     * Checks all potential unlocks on the player, and then determines if any of them are false.
     * Don't use if you want to know specifically which one is keeping you from doing an action.
     *
     * @return true if it can be used, false if not.
     */
    public static <T> boolean canUse(ProfessionalPlayer player, UnlockType<T> type, T object) {
        List<Unlock.Singular<T>> unlocks = player.getLockedKnowledge(type, object);
        for (Unlock.Singular<T> unlock : unlocks) {
            if (!unlock.canUse(player)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if the player CAN break the block, and false if they can't.
     */
    public static boolean canBreak(ProfessionalPlayer player, Player onlinePlayer, Block block) {
        boolean canBreak = true;
        UnlockErrorHelper helper = new UnlockErrorHelper(Component.literal("=-=-=-= Level Requirements =-=-=-="));
        List<Unlock.Singular<Block>> unlocks = player.getLockedKnowledge(Unlocks.BLOCK_BREAK_UNLOCK, block);
        for (Unlock.Singular<Block> singular : unlocks) {
            if (!singular.canUse(player)) {
                helper.newLine();
                helper.levelRequirementNotMet(singular);
                canBreak = false;
            }
        }
        List<Unlock.Singular<Item>> itemUnlocks = player.getLockedKnowledge(block.asItem(), Set.of(Unlocks.ADVANCEMENT_UNLOCK));
        for (Unlock.Singular<Item> singular : itemUnlocks) {
            if (!singular.canUse(player)) {
                helper.newLine();
                helper.levelRequirementNotMet(singular);
                canBreak = false;
            }
        }

        if (!canBreak) {
            Component hover = Component.literal("Hover to see which occupations prevented the block break.")
                    .setStyle(Style.EMPTY.withColor(ProfessionConfig.variables)
                            .withUnderlined(true)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, helper.getComponent())));
            onlinePlayer.sendSystemMessage(Component.translatable("%s", hover)
                    .setStyle(Style.EMPTY.withColor(ProfessionConfig.errors)));
        }
        return canBreak;
    }
}
