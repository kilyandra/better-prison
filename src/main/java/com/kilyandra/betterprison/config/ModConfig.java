package com.kilyandra.betterprison.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import com.kilyandra.betterprison.config.option.KeybindOption;


@Config(name = "betterprison")
@Config.Gui.Background(value = Config.Gui.Background.TRANSPARENT)
public class ModConfig implements ConfigData {

    public static void register() {
        GuiRegistry registry = AutoConfig.getGuiRegistry(ModConfig.class);
        KeybindOption.register(registry);

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public static ModConfig getConfig() {
        register();
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    @Category("globalSection")
    @ConfigEntry.Gui.TransitiveObject
    public GlobalSection globalSection = new GlobalSection();

    public static class GlobalSection {
        @ConfigEntry.Gui.Tooltip()
        public boolean modEnabled = true;

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public ChatSection chat = new ChatSection();

        public static class ChatSection {
            @ConfigEntry.Gui.Tooltip()
            public boolean messageSecretStorage = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageUnreadMail = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageAsk = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageJoinLeave = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageGuardOnJoin = true;
        }

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public OtherSection other = new OtherSection();

        public static class OtherSection {
            @ConfigEntry.Gui.Tooltip()
            public boolean guiVote = true;
        }
    }

    @Category("prisonSection")
    @ConfigEntry.Gui.TransitiveObject
    public PrisonSection prisonSection = new PrisonSection();

    public static class PrisonSection {
        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public RenderSection render = new RenderSection();

        public static class RenderSection {
            @ConfigEntry.Gui.Tooltip(count = 2)
            public boolean betterChunks = false;
        }

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public ChatSection chat = new ChatSection();

        public static class ChatSection {
            @ConfigEntry.Gui.Tooltip()
            public boolean messageLevelDifference = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageAttackSameFaction = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageBeaten = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageHugeDamage = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageResourcesLost = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messagePvpOver = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageClear7Slot = true;

            @ConfigEntry.Gui.Tooltip()
            public boolean messageBossDefeated = true;
        }

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public OtherSection other = new OtherSection();

        public static class OtherSection {
            @ConfigEntry.Gui.Tooltip()
            public boolean tabNpc = true;
        }
    }

    @Category("fasttpSection")
    @ConfigEntry.Gui.TransitiveObject
    public FasttpSection fasttpSection = new FasttpSection();

    public static class FasttpSection {
        @ConfigEntry.Gui.Tooltip()
        public boolean keybindsEnabled = true;

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public KeybindsSection keybinds = new KeybindsSection();

        public static class KeybindsSection {
            public KeybindOption location1 = KeybindOption.empty();
            public KeybindOption location2 = KeybindOption.empty();
            public KeybindOption location3 = KeybindOption.empty();
            public KeybindOption location4 = KeybindOption.empty();
            public KeybindOption location5 = KeybindOption.empty();
            public KeybindOption location6 = KeybindOption.empty();
            public KeybindOption location7 = KeybindOption.empty();
            public KeybindOption location8 = KeybindOption.empty();
            public KeybindOption location9 = KeybindOption.empty();
            public KeybindOption location10 = KeybindOption.empty();
            public KeybindOption location11 = KeybindOption.empty();
            public KeybindOption location12 = KeybindOption.empty();
            public KeybindOption location13 = KeybindOption.empty();
            public KeybindOption location14 = KeybindOption.empty();
            public KeybindOption location15 = KeybindOption.empty();
            public KeybindOption location16 = KeybindOption.empty();
            public KeybindOption location17 = KeybindOption.empty();
            public KeybindOption location18 = KeybindOption.empty();
            public KeybindOption location19 = KeybindOption.empty();
            public KeybindOption location20 = KeybindOption.empty();
            public KeybindOption location21 = KeybindOption.empty();
            public KeybindOption location22 = KeybindOption.empty();
            public KeybindOption location23 = KeybindOption.empty();
            public KeybindOption location24 = KeybindOption.empty();
            public KeybindOption location25 = KeybindOption.empty();
        }
    }
}