package com.kilyandra.betterprison.client.keybind;

import com.kilyandra.betterprison.config.option.KeybindOption;
import com.kilyandra.betterprison.config.ModConfig;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.List;
import org.lwjgl.glfw.GLFW;
import me.shedaniel.autoconfig.AutoConfig;
import com.mojang.blaze3d.platform.InputConstants;

import static com.kilyandra.betterprison.BetterPrison.CLIENT;
import static com.kilyandra.betterprison.BetterPrison.LOGGER;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.keybindsEnabled;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.fasttpKeybinds;


public class KeybindManager {
    private final List<KeybindOption> keybinds;
    private final KeyMapping menuKey;

    public KeybindManager() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.betterprison.menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.betterprison.main"
        ));

        keybinds = fasttpKeybinds();
        ClientTickEvents.START_WORLD_TICK.register(this::onStartWorldTick);
    }

    private void onStartWorldTick(ClientLevel level) {
        if (Minecraft.getInstance().screen != null || !keybindsEnabled()) return;
        if (menuKey.isDown()) Minecraft.getInstance().setScreen(
                AutoConfig.getConfigScreen(ModConfig.class, null).get());

        long window = Minecraft.getInstance().getWindow().getWindow();

        boolean Shift = InputConstants.isKeyDown(window, InputConstants.KEY_LSHIFT) ||
                InputConstants.isKeyDown(window, InputConstants.KEY_RSHIFT);
        boolean Ctrl = InputConstants.isKeyDown(window, InputConstants.KEY_LCONTROL) ||
                InputConstants.isKeyDown(window, InputConstants.KEY_RCONTROL);
        boolean Alt = InputConstants.isKeyDown(window, InputConstants.KEY_LALT) ||
                InputConstants.isKeyDown(window, InputConstants.KEY_RALT);

        for (int i = 0; i < keybinds.size(); i++) {
            KeybindOption keybind = keybinds.get(i);
            if (keybind.keyCode == -1) continue;

            boolean isKeyPressed = GLFW.glfwGetKey(window, keybind.keyCode) == GLFW.GLFW_PRESS;
            boolean areModifiersPressed = true;

            if (keybind.modifiers != 0) {
                areModifiersPressed = ((keybind.modifiers & 4) == 0 || Shift)
                                   && ((keybind.modifiers & 2) == 0 || Ctrl)
                                   && ((keybind.modifiers & 1) == 0 || Alt);
            }

            if (isKeyPressed && areModifiersPressed) {
                if (keybind.click()) {
                    LOGGER.info("Keybind {}", i);
                    CLIENT.EVENT_MANAGER.handleFasttpKeybind(i);
                }
            }
            else keybind.release();
        }
    }
}
