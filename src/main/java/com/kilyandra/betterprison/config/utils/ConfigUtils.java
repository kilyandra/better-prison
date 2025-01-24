package com.kilyandra.betterprison.config.utils;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Entry;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;

public class ConfigUtils {

    public static boolean isMessageSecretStorage(String message) {
        return message.startsWith("Награды » В Секретном Хранилище что-то есть...") ||
                message.startsWith("Rewards » There's something in the Secret Storage...");
    }

    public static boolean isMessageAsk(String message) {
        return message.startsWith("Вопросы »") ||
                message.startsWith("Questions »");
    }

    public static boolean isMessageUnreadMail(String message) {
        return (message.startsWith("Почта » У тебя есть") && message.endsWith("непрочитанных писем - /mail read")) ||
                (message.startsWith("Mail » Received") && message.endsWith("new mails - /mail read"));
    }

    public static boolean isMessageJoinLeave(String message) {
        return message.startsWith(" ▶ ");
    }

    public static boolean isMessageGuardOnJoin(String message) {
        return (message.equals("Guard » Идет проверка, пожалуйста, подождите... \n Не двигайтесь, все происходит автоматически. \n  ") ||
                message.equals("Введите код с изображения в чат. \n Если вы не видите изображение, то нужно полностью переустановить Minecraft.")) ||
                (message.equals("Guard » Checking in progress, please wait... \n Do not move, everything happens automatically. \n  ") ||
                 message.equals("Guard » Enter code from the image into chat.  If you don't see the image, you need to completely reinstall Minecraft."));
    }

    public static boolean isMessageLevelDifference(String message) {
        return message.equals("Prison » Слишком большая разница уровней. Нет смысла убивать!") ||
                message.equals("Prison » The level difference is too high. Killing is pointless!");
    }

    public static boolean isMessageAttackSameFaction(String message) {
        return message.equals("Prison » Ты не можешь атаковать игрока своей фракции.") ||
                message.equals("Prison » You can not attack player from the same faction.");
    }

    public static boolean isMessageBeaten(String message) {
        return message.startsWith("Prison » Ты был сильно избит игроком") ||
                message.startsWith("Prison » You were severely beaten by the player");
    }

    public static boolean isMessageHugeDamage(String message) {
        return message.equals("Prison » Ты получил сильный урон, но выжил!") ||
                message.equals("Prison » You've taken a huge amount of damage but survived!");
    }

    public static boolean isMessageResourcesLost(String message) {
        return message.equals(" Все твои накопленные ресурсы утрачены.") ||
                message.equals(" All of your resources have been lost.");
    }

    public static boolean isMessagePvpOver(String message) {
        return message.equals("Prison » Ты вышел из боя! Теперь можно спокойно покинуть игру, если это требуется.") ||
                message.equals("Prison » Battle is over! Now you can leave the game, if you want.");
    }

    public static boolean isMessageClear7Slot(String message) {
        return message.equals("Prison » Освободи 7 слот для выдачи.") ||
                message.equals("Prison » Please clear seventh slot.");
    }

    public static boolean isMessageBossDefeated(String message) {
        return (message.startsWith("Prison » Босс") && message.endsWith("игроков.")) ||
                (message.startsWith("Prison » Boss") && message.contains("was defeated! Players Involved:"));
    }

    public static boolean isPrisonScoreboard(ClientboundSetObjectivePacket packet) {
        return packet.getDisplayName().getString().equals("PRISON");
    }

    public static boolean isVoteScreen(String containerName) {
        return containerName.equals("§4Голосуй за награду") ||
                containerName.equals("§4Vote for reward");
    }

    public static boolean isFastTpScreen(String containerName) {
        return containerName.equals("§4Быстрые телепортации") ||
                containerName.equals("§4Fast teleportations");
    }

    public static boolean isNpcDisplayName(PlayerInfo playerInfo, Entry entry) {
        return entry.displayName() != null &&
                entry.displayName().getString().equals("NPC") &&
                !playerInfo.getProfile().getName().equals("NPC");
    }
}
