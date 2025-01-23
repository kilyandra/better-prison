package com.kilyandra.betterprison.config.utils;

import static com.kilyandra.betterprison.BetterPrison.CONFIG;


public class ConfigGetters {

    public static boolean modEnabled() {
        return CONFIG.globalSection.modEnabled;
    }

    public static boolean messageSecretStorage() {
        return CONFIG.globalSection.chatSection.messageSecretStorage;
    }

    public static boolean messageUnreadMail() {
        return CONFIG.globalSection.chatSection.messageUnreadMail;
    }

    public static boolean messageAsk() {
        return CONFIG.globalSection.chatSection.messageAsk;
    }

    public static boolean messageJoinLeave() {
        return CONFIG.globalSection.chatSection.messageJoinLeave;
    }

    public static boolean messageGuardOnJoin() {
        return CONFIG.globalSection.chatSection.messageGuardOnJoin;
    }

    public static boolean guiVote() {
        return CONFIG.globalSection.guiSection.guiVote;
    }

    public static boolean betterChunks() {
        return CONFIG.prisonSection.renderSection.betterChunks;
    }

    public static boolean messageLevelDifference() {
        return CONFIG.prisonSection.chatSection.messageLevelDifference;
    }

    public static boolean messageAttackSameFaction() {
        return CONFIG.prisonSection.chatSection.messageAttackSameFaction;
    }

    public static boolean messageBeaten() {
        return CONFIG.prisonSection.chatSection.messageBeaten;
    }

    public static boolean messageHugeDamage() {
        return CONFIG.prisonSection.chatSection.messageHugeDamage;
    }

    public static boolean messageResourcesLost() {
        return CONFIG.prisonSection.chatSection.messageResourcesLost;
    }

    public static boolean messagePvpOver() {
        return CONFIG.prisonSection.chatSection.messagePvpOver;
    }

    public static boolean messageClear7Slot() {
        return CONFIG.prisonSection.chatSection.messageClear7Slot;
    }

    public static boolean messageBossDefeated() {
        return CONFIG.prisonSection.chatSection.messageBossDefeated;
    }
}
