package com.kilyandra.betterprison.client.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kilyandra.betterprison.BetterPrison.CLIENT;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.*;
import static com.kilyandra.betterprison.config.utils.ConfigUtils.*;
import static com.kilyandra.betterprison.utils.Utils.isSoundPlayerTag;
import static com.kilyandra.betterprison.utils.Utils.setOnPrison;
import static com.kilyandra.betterprison.utils.Utils.onMineland;
import static com.kilyandra.betterprison.utils.Utils.onPrison;


public class EventManager {
    private final ClientPacketListener handler;
    private final Minecraft minecraft;

    private boolean secretStorageMessageReceived = false;
    private boolean chunksLoaded = false;

    private int containerId;
    private int fasttpKeybindLocation = -1;

    public EventManager(ClientPacketListener listener, Minecraft client){
        this.handler = listener;
        this.minecraft = client;
    }

    public void handleChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        String message = packet.content().getString();

        //Глобальные
        if (isMessageSecretStorage(message) && !messageSecretStorage()){
            secretStorageMessageReceived = true;
            ci.cancel();
        } else if
            ((isMessageAsk(message) && !messageAsk()) ||
            (isMessageJoinLeave(message) && !messageJoinLeave()) ||
            (isMessageUnreadMail(message) && !messageUnreadMail()) ||
            (isMessageGuardOnJoin(message) && !messageGuardOnJoin()) ||
        //Prison
            (isMessageLevelDifference(message) && !messageLevelDifference()) ||
            (isMessageAttackSameFaction(message) && !messageAttackSameFaction()) ||
            (isMessageHugeDamage(message) && !messageHugeDamage()) ||
            (isMessageBeaten(message) && !messageBeaten()) ||
            (isMessageResourcesLost(message) && !messageResourcesLost()) ||
            (isMessagePvpOver(message) && !messagePvpOver()) ||
            (isMessageClear7Slot(message) && !messageClear7Slot()) ||
            (isMessageBossDefeated(message) && !messageBossDefeated()))
        ci.cancel();
    }

    public void handleSetScoreboard(ClientboundSetObjectivePacket packet, CallbackInfo ci) {
        setOnPrison(isPrisonScoreboard(packet) && onMineland());
        if (!onPrison()) return;

        //Сделано так, потому что ClientPlayConnectionEvents.JOIN срабатывает перед получением скорборда.
        if (!chunksLoaded) {
            chunksLoaded = true;
            if (betterChunks()) minecraft.execute(() -> CLIENT.CHUNK_MANAGER.renderSavedChunks(handler));
        }
    }

    public void handleSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
        var key = packet.getSound().unwrapKey();
        if (key.isEmpty()) return;

        String soundName = key.get().location().toString();

        //Глобальные
        if (isSoundPlayerTag(soundName) && !messageSecretStorage() && secretStorageMessageReceived) {
            secretStorageMessageReceived = false;
            ci.cancel();
        }
    }

    public void handleContainerContent(ClientboundContainerSetContentPacket packet, CallbackInfo ci) {
        //Быстрые телепортации
        if (packet.getContainerId() == containerId && fasttpKeybindLocation != -1) {
            ci.cancel();

            int k = fasttpKeybindLocation;
            fasttpKeybindLocation = -1;

            ItemStack itemStack = packet.getItems().get(k);
            Int2ObjectMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();
            int2ObjectMap.put(k, itemStack);

            handler.send(new ServerboundContainerClickPacket(
                    packet.getContainerId(),
                    packet.getStateId(),
                    k,
                    0,
                    ClickType.PICKUP,
                    itemStack,
                    int2ObjectMap
            ));
        }
    }

    public void handleOpenedContainer(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
        String containerName = packet.getTitle().getString();

        //Глобальные
        if (isVoteScreen(containerName) && !guiVote()) {
            ci.cancel();
            handler.send(new ServerboundContainerClosePacket(packet.getContainerId()));
        }

        //Быстрые телепортации
        else if (isFastTpScreen(containerName) && keybindsEnabled() && fasttpKeybindLocation != -1) {
            containerId = packet.getContainerId();
            ci.cancel();
        }

    }

    public void handleFasttpKeybind(int i) {
        fasttpKeybindLocation = i;
        handler.sendCommand("fasttp");
    }
}
