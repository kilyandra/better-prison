package com.kilyandra.betterprison.utils;

import com.kilyandra.betterprison.BetterPrison;
import net.minecraft.client.Minecraft;


public class Utils {
    public static boolean isMinelandIp(Minecraft minecraft) {
        if (minecraft.getCurrentServer() == null) return false;

        String serverIp = minecraft.getCurrentServer().ip;
        return (serverIp.endsWith("mineland.net") ||
                serverIp.endsWith("batyacraft.ru") ||
                serverIp.endsWith("alphacraft.ru"));
    }

    public static boolean isSoundPlayerTag(String soundName) {
        return soundName.equals("minecraft:entity.experience_orb.pickup");
    }

    public static boolean onNettyThread() {
        return Thread.currentThread().getName().startsWith("Netty Client IO");
    }

    public static boolean onPrison() {
        return BetterPrison.CLIENT.onPrison;
    }

    public static void setOnPrison(boolean value) {
        BetterPrison.CLIENT.onPrison = value;
    }

    public static boolean onMineland() {
        return BetterPrison.CLIENT.onMineland;
    }
}
