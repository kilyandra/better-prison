package com.kilyandra.betterprison.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;


@Config(name = "betterprison")
@Config.Gui.Background(value = Config.Gui.Background.TRANSPARENT)
public class ModConfig implements ConfigData {

    @Category("globalSection")
    @ConfigEntry.Gui.TransitiveObject
    public GlobalSection globalSection = new GlobalSection();

    public static class GlobalSection {
        @ConfigEntry.Gui.Tooltip()
        public boolean modEnabled = true;

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public ChatSection chatSection = new ChatSection();

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
        public GuiSection guiSection = new GuiSection();

        public static class GuiSection {
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
        public RenderSection renderSection = new RenderSection();

        public static class RenderSection {
            @ConfigEntry.Gui.Tooltip(count = 2)
            public boolean betterChunks = false;
        }

        @ConfigEntry.Gui.Tooltip()
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public ChatSection chatSection = new ChatSection();

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
    }
}