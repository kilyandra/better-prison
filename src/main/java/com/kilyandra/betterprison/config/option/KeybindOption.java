package com.kilyandra.betterprison.config.option;

import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.autoconfig.util.Utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;

import java.util.Collections;


public class KeybindOption {
    public int keyCode;
    public short modifiers;
    private transient boolean isKeyPressed = false;

    public KeybindOption(int keyCode, short modifiers) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
    }

    public static KeybindOption empty() {
        return new KeybindOption(InputConstants.UNKNOWN.getValue(), Modifier.none().getValue());
    }

    public ModifierKeyCode toModifierKeyCode() {
        return ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(keyCode), Modifier.of(modifiers));
    }

    public boolean click() {
        if (!isKeyPressed) {
            isKeyPressed = true;
            return true;
        }

        return false;
    }

    public void release() {
        isKeyPressed = false;
    }

    public static void register(GuiRegistry registry) {
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> {
            KeybindOption current = Utils.getUnsafely(field, config);
            KeybindOption defaultVal = Utils.getUnsafely(field, defaults);

            return Collections.singletonList(
                    ConfigEntryBuilder.create()
                            .startModifierKeyCodeField(
                                    Component.translatable(i18n),
                                    current.toModifierKeyCode()
                            )
                            .setDefaultValue(() -> defaultVal.toModifierKeyCode().getKeyCode())
                            .setModifierDefaultValue(defaultVal::toModifierKeyCode)
                            .setKeySaveConsumer(keyCode -> {
                                current.keyCode = keyCode.getValue();
                                Utils.setUnsafely(field, config, current);
                            })
                            .setModifierSaveConsumer(newValue -> {
                                current.keyCode = newValue.getKeyCode().getValue();
                                current.modifiers = newValue.getModifier().getValue();
                                Utils.setUnsafely(field, config, current);
                            })
                            .setAllowMouse(false)
                            .build()
            );
        }, KeybindOption.class);
    }
}
