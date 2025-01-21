package com.kilyandra.betterprison;

import com.kilyandra.betterprison.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.kilyandra.betterprison.Utils.*;


public final class BetterPrison implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("better-prison");
	public static final ChunkManager CHUNK_MANAGER = new ChunkManager();
	public static ModConfig CONFIG;

	public static ClientPacketListener handler;
	public static Minecraft minecraft;

	public static boolean onMineland = false;
	public static boolean onPrison = false;

	//Для отключения звука от оповещения о секретном хранилище, но не от упоминания в чате.
	private static boolean secretStorageMessageReceived = false;
	private static boolean chunksLoaded = false;

	public BetterPrison() {
		ClientPlayConnectionEvents.JOIN.register(this::onConnect);
	}

	private void onConnect(ClientPacketListener listener, PacketSender sender, Minecraft client) {
		client.executeBlocking(() -> {
			handler = listener;
			minecraft = client;
			onMineland = isMinelandIp(minecraft);

			secretStorageMessageReceived = false;
			chunksLoaded = false;

			if (!onMineland) onPrison = false;
		});
	}

	public static void handleOpenedContainer(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
		String containerName = packet.getTitle().getString();

		//Глобальные
		if (isVoteScreen(containerName) && !CONFIG.globalSection.guiSection.guiVote) {
			ci.cancel();
			handler.send(new ServerboundContainerClosePacket(packet.getContainerId()));
		}

	}

	public static void handleChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
		String message = packet.content().getString();

		//Глобальные
		if (isMessageSecretStorage(message) && !CONFIG.globalSection.chatSection.messageSecretStorage){
			secretStorageMessageReceived = true;
			ci.cancel();
			return;
		}

		//Перенесено из-за isMessageSecretStorage, так как ClientPlayConnectionEvents.JOIN
		//срабатывает после того, как сервер отправил это сообщение.
		if (!onMineland) return;

		if (isMessageAsk(message) && !CONFIG.globalSection.chatSection.messageAsk) ci.cancel(); else
		if (isMessageJoinLeave(message) && !CONFIG.globalSection.chatSection.messageJoinLeave) ci.cancel(); else
		if (isMessageUnreadMail(message) && !CONFIG.globalSection.chatSection.messageUnreadMail) ci.cancel(); else
		if (isMessageGuardOnJoin(message) && !CONFIG.globalSection.chatSection.messageGuardOnJoin) ci.cancel(); else

		//Prison
		if (isMessageLevelDifference(message) && !CONFIG.prisonSection.chatSection.messageLevelDifference) ci.cancel(); else
		if (isMessageAttackSameFaction(message) && !CONFIG.prisonSection.chatSection.messageAttackSameFaction) ci.cancel(); else
		if (isMessageHugeDamage(message) && !CONFIG.prisonSection.chatSection.messageHugeDamage) ci.cancel(); else
		if (isMessageBeaten(message) && !CONFIG.prisonSection.chatSection.messageBeaten) ci.cancel(); else
		if (isMessageResourcesLost(message) && !CONFIG.prisonSection.chatSection.messageResourcesLost) ci.cancel(); else
		if (isMessagePvpOver(message) && !CONFIG.prisonSection.chatSection.messagePvpOver) ci.cancel(); else
		if (isMessageClear7Slot(message) && !CONFIG.prisonSection.chatSection.messageClear7Slot) ci.cancel(); else
		if (isMessageBossDefeated(message) && !CONFIG.prisonSection.chatSection.messageBossDefeated) ci.cancel();
	}

	public static void handleSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
		var key = packet.getSound().unwrapKey();
		if (key.isEmpty()) return;

		String soundName = key.get().location().toString();

		//Глобальные
		if (isSoundPlayerTag(soundName) && !CONFIG.globalSection.chatSection.messageSecretStorage && secretStorageMessageReceived) {
			secretStorageMessageReceived = false;
			ci.cancel();
		}
	}

	public static void handleScoreboard(ClientboundSetObjectivePacket packet, CallbackInfo ci) {
		onPrison = isPrisonScoreboard(packet);
		if (!onPrison) return;

		//Сделано так, потому что ClientPlayConnectionEvents.JOIN срабатывает перед получением скорборда.
		if (!chunksLoaded) {
			chunksLoaded = true;
			if (CONFIG.prisonSection.renderSection.betterChunks)
				minecraft.execute(() -> CHUNK_MANAGER.renderSavedChunks(handler));
		}
	}

    @Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		LOGGER.info("Мод загружен.");
	}
}