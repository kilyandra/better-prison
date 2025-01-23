package com.kilyandra.betterprison.client;

import com.kilyandra.betterprison.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kilyandra.betterprison.Utils.*;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.*;
import static com.kilyandra.betterprison.config.utils.ConfigUtils.*;
import static com.kilyandra.betterprison.BetterPrison.*;


public class EventHandler {
    private final ClientPacketListener handler;
    private final Minecraft minecraft;

    //Для отключения звука от оповещения о секретном хранилище, но не от упоминания в чате.
    private boolean secretStorageMessageReceived = false;
    private boolean chunksLoaded = false;

    public EventHandler(ClientPacketListener listener, Minecraft client){
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

    public void handleOpenedContainer(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
        String containerName = packet.getTitle().getString();

        //Глобальные
        if (isVoteScreen(containerName) && !guiVote()) {
            ci.cancel();
            handler.send(new ServerboundContainerClosePacket(packet.getContainerId()));
        }

    }

    public void handleSetScoreboard(ClientboundSetObjectivePacket packet, CallbackInfo ci) {
        setOnPrison(isPrisonScoreboard(packet) && onMineland());
        if (!onPrison()) return;

        //Сделано так, потому что ClientPlayConnectionEvents.JOIN срабатывает перед получением скорборда.
        if (!chunksLoaded) {
            chunksLoaded = true;
            if (betterChunks()) minecraft.execute(() -> CHUNK_MANAGER.renderSavedChunks(handler));
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
}
