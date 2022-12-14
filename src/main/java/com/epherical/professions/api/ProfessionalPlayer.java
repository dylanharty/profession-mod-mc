package com.epherical.professions.api;

import com.epherical.professions.data.Storage;
import com.epherical.professions.profession.Profession;
import com.epherical.professions.profession.ProfessionContext;
import com.epherical.professions.profession.action.Action;
import com.epherical.professions.profession.action.ActionType;
import com.epherical.professions.profession.modifiers.perks.Perk;
import com.epherical.professions.profession.modifiers.perks.PerkType;
import com.epherical.professions.profession.progression.Occupation;
import com.epherical.professions.profession.progression.OccupationSlot;
import com.epherical.professions.profession.unlock.Unlock;
import com.epherical.professions.profession.unlock.UnlockType;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProfessionalPlayer {

    UUID getUuid();

    @Nullable
    ServerPlayer getPlayer();

    void setPlayer(ServerPlayer player);

    void handleAction(ProfessionContext context, ServerPlayer player);

    /**
     * Manually save the player using an indicated storage method.
     *
     * @param storage The storage method to use to save the player.
     */
    void save(Storage<ProfessionalPlayer, UUID> storage);

    /**
     * Indicate when changes have taken place on the Player that require saving.
     */
    void needsToBeSaved();

    /**
     * Checks if the player has previously joined or leveled an occupation. This only applies when the config is
     * set to allow players to keep their occupation exp when they mark it as inactive.
     *
     * @param profession The profession to check
     * @return true if the player currently has progress in their occupation, false otherwise.
     */
    boolean alreadyHasOccupation(Profession profession);

    boolean isOccupationActive(Profession profession);

    boolean joinOccupation(Profession occupation, OccupationSlot slot);

    boolean leaveOccupation(Profession profession);

    /**
     * This removes the occupation from the player, deleting any and all progression the user may have made.
     */
    boolean fireFromOccupation(Profession profession);

    Occupation getOccupation(Profession profession);

    void updateOccupationPerks();

    List<Occupation> getActiveOccupations();

    List<Occupation> getInactiveOccupations();

    <T> List<Unlock.Singular<T>> getLockedKnowledge(T object, Set<UnlockType<T>> unlockTypes);

    <T> List<Unlock.Singular<T>> getLockedKnowledge(UnlockType<T> unlockType, T object);

    <T> List<Unlock.Singular<T>> getLockedKnowledge(T object);

    List<Perk> getPerkByType(PerkType perkType);

    <T> List<Action.Singular<T>> getActions(T object, Set<ActionType> unlockTypes);
    <T> List<Action.Singular<T>> getActions(T object);

}
