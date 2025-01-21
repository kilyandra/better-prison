package com.kilyandra.betterprison;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.kilyandra.betterprison.BetterPrison.LOGGER;
import static com.kilyandra.betterprison.BetterPrison.CONFIG;


public class Utils {
    public static boolean isModEnabled(){
        return CONFIG.modEnabled;
    }

    public static boolean isBetterChunksEnabled(){
        return CONFIG.prisonSection.renderSection.betterChunks;
    }

    //Проверки сервера
    public static boolean isMinelandIp(Minecraft minecraft) {
        if (minecraft.getCurrentServer() == null) return false;

        String serverIp = minecraft.getCurrentServer().ip;
        return (serverIp.endsWith("mineland.net") ||
                serverIp.endsWith("batyacraft.ru") ||
                serverIp.endsWith("alphacraft.ru"));
    }

    public static boolean isPrisonScoreboard(ClientboundSetObjectivePacket packet) {
        return packet.getDisplayName().getString().equals("PRISON");
    }

    //Проверки сообщений/экранов(gui)/звуков
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

    //Нужно получить текст на английском
    public static boolean isMessageGuardOnJoin(String message) {
        return (message.equals("Guard » Идет проверка, пожалуйста, подождите... \n Не двигайтесь, все происходит автоматически. \n  ") ||
                message.equals("Введите код с изображения в чат. \n Если вы не видите изображение, то нужно полностью переустановить Minecraft.")) ||
               (message.equals("Guard » Checking in progress, please wait... \n Do not move, everything happens automatically. \n  ") ||
                message.equals("Guard » Enter code from the image into chat.  If you don't see the image, you need to completely reinstall Minecraft."));
    }

    public static boolean isSoundPlayerTag(String soundName) {
        return soundName.equals("minecraft:entity.experience_orb.pickup");
    }

    public static boolean isVoteScreen(String containerName) {
        return containerName.equals("§4Голосуй за награду") ||
               containerName.equals("§4Vote for reward");
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

    //Чанки
    public static String getFileName(ClientboundLevelChunkWithLightPacket packet) {
        int X = packet.getX();
        int Z = packet.getZ();

        return "chunk_" + X + "_" + Z + ".data";
    }

    public static FriendlyByteBuf loadFromFile(File file){
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            return new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes));

        } catch (IOException e) {
            LOGGER.error("Ошибка при чтении файла: {} ", e.getMessage());
            return null;
        }
    }

    public static void writeToFile(FriendlyByteBuf buf, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            fos.write(bytes);

        } catch (IOException e) {
            LOGGER.error("Ошибка записи в файл: {} ", e.getMessage());
        }
    }

    //Другое
    public static void createDirs(String dir){
        File file = new File(dir);

        if (!file.mkdirs() && !file.exists()) {
            LOGGER.error("Не удалось создать директории");
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean onNettyThread() {
        return Thread.currentThread().getName().startsWith("Netty Client IO");
    }
}
