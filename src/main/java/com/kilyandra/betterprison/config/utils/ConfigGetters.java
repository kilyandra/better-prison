package com.kilyandra.betterprison.config.utils;

import com.kilyandra.betterprison.config.option.KeybindOption;
import java.util.List;

import static com.kilyandra.betterprison.BetterPrison.CONFIG;


public class ConfigGetters {

    public static boolean modEnabled() {
        return CONFIG.globalSection.modEnabled;
    }

    public static boolean messageSecretStorage() {
        return CONFIG.globalSection.chat.messageSecretStorage;
    }

    public static boolean messageUnreadMail() {
        return CONFIG.globalSection.chat.messageUnreadMail;
    }

    public static boolean messageAsk() {
        return CONFIG.globalSection.chat.messageAsk;
    }

    public static boolean messageJoinLeave() {
        return CONFIG.globalSection.chat.messageJoinLeave;
    }

    public static boolean messageGuardOnJoin() {
        return CONFIG.globalSection.chat.messageGuardOnJoin;
    }

    public static boolean guiVote() {
        return CONFIG.globalSection.other.guiVote;
    }

    public static boolean betterChunks() {
        return CONFIG.prisonSection.render.betterChunks;
    }

    public static boolean messageLevelDifference() {
        return CONFIG.prisonSection.chat.messageLevelDifference;
    }

    public static boolean messageAttackSameFaction() {
        return CONFIG.prisonSection.chat.messageAttackSameFaction;
    }

    public static boolean messageBeaten() {
        return CONFIG.prisonSection.chat.messageBeaten;
    }

    public static boolean messageHugeDamage() {
        return CONFIG.prisonSection.chat.messageHugeDamage;
    }

    public static boolean messageResourcesLost() {
        return CONFIG.prisonSection.chat.messageResourcesLost;
    }

    public static boolean messagePvpOver() {
        return CONFIG.prisonSection.chat.messagePvpOver;
    }

    public static boolean messageClear7Slot() {
        return CONFIG.prisonSection.chat.messageClear7Slot;
    }

    public static boolean messageBossDefeated() {
        return CONFIG.prisonSection.chat.messageBossDefeated;
    }

    public static boolean tabNpc() {
        return CONFIG.prisonSection.other.tabNpc;
    }

    public static boolean keybindsEnabled() {
        return CONFIG.fasttpSection.keybindsEnabled;
    }

    public static List<KeybindOption> fasttpKeybinds() {
        var keybindsSection = CONFIG.fasttpSection.keybinds;

        return List.of(keybindsSection.location1, keybindsSection.location2, keybindsSection.location3,
                keybindsSection.location4, keybindsSection.location5, keybindsSection.location6,
                keybindsSection.location7, keybindsSection.location8, keybindsSection.location9,
                keybindsSection.location10, keybindsSection.location11, keybindsSection.location12,
                keybindsSection.location13, keybindsSection.location14, keybindsSection.location15,
                keybindsSection.location16, keybindsSection.location17, keybindsSection.location18,
                keybindsSection.location19, keybindsSection.location20, keybindsSection.location21,
                keybindsSection.location22, keybindsSection.location23, keybindsSection.location24,
                keybindsSection.location25);
    }
}
