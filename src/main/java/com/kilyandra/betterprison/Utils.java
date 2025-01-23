package com.kilyandra.betterprison;

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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean onNettyThread() {
        return Thread.currentThread().getName().startsWith("Netty Client IO");
    }

    public static boolean onPrison() {
        return BetterPrison.CLIENT_MANAGER.onPrison;
    }

    public static void setOnPrison(boolean value) {
        BetterPrison.CLIENT_MANAGER.onPrison = value;
    }

    public static boolean onMineland() {
        return BetterPrison.CLIENT_MANAGER.onMineland;
    }

    public static void setOnMineland(boolean value) {
        BetterPrison.CLIENT_MANAGER.onMineland = value;
    }
}
