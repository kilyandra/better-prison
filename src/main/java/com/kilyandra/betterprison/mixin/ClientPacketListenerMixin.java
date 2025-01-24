package com.kilyandra.betterprison.mixin;

import net.minecraft.client.multiplayer.ChunkBatchSizeCalculator;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Set;

import static com.kilyandra.betterprison.BetterPrison.CLIENT;
import static com.kilyandra.betterprison.utils.Utils.onPrison;
import static com.kilyandra.betterprison.utils.Utils.onMineland;
import static com.kilyandra.betterprison.utils.Utils.onNettyThread;
import static com.kilyandra.betterprison.config.utils.ConfigUtils.isNpcDisplayName;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.betterChunks;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.modEnabled;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.tabNpc;


@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

	@Inject(method = "handleForgetLevelChunk", at = @At(value = "HEAD"), cancellable = true)
	private void handleForgetLevelChunk(ClientboundForgetLevelChunkPacket packet, CallbackInfo ci) {
		//Отмена выгрузки чанков для CONFIG.prisonSection.renderSection.betterChunks
		if (modEnabled() && onPrison() && betterChunks())
			ci.cancel();
	}

	@Inject(method = "handleLevelChunkWithLight", at = @At(value = "HEAD"))
	private void handleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
		if (modEnabled() && onPrison() && betterChunks() && onNettyThread())
			CLIENT.CHUNK_MANAGER.saveChunk(packet);
	}

	@Final
	@Shadow
	private ChunkBatchSizeCalculator chunkBatchSizeCalculator;

	@Inject(method = "handleChunkBatchFinished", at = @At(value = "HEAD"), cancellable = true)
	private void handleLocalChunksBatchFinished(ClientboundChunkBatchFinishedPacket packet, CallbackInfo ci) {
		//Отмена отправки пакета серверу после ChunkManager.renderSavedChunks
		if (modEnabled() && onPrison() && packet.batchSize() == CLIENT.CHUNK_MANAGER.chunksAmount) {
			this.chunkBatchSizeCalculator.onBatchFinished(CLIENT.CHUNK_MANAGER.chunksAmount);
			ci.cancel();
		}
	}

	@Inject(method = "handleAddObjective", at = @At(value = "HEAD"), cancellable = true)
	private void handleScoreboardName(ClientboundSetObjectivePacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland())
			CLIENT.EVENT_MANAGER.handleSetScoreboard(packet, ci);
	}

	@Inject(method = "handleOpenScreen", at = @At(value = "HEAD"), cancellable = true)
	private void handleOpenedContainer(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland())
			CLIENT.EVENT_MANAGER.handleOpenedContainer(packet, ci);
	}

	@Inject(method = "handleContainerContent", at = @At(value = "HEAD"), cancellable = true)
	private void handleContainerContent(ClientboundContainerSetContentPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland())
			CLIENT.EVENT_MANAGER.handleContainerContent(packet, ci);
	}

	@Inject(method = "handleSystemChat", at = @At(value = "HEAD"), cancellable = true)
	private void handleChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland())
			CLIENT.EVENT_MANAGER.handleChatMessage(packet, ci);
	}

	@Inject(method = "handleSoundEvent", at = @At(value = "HEAD"), cancellable = true)
	private void handleSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland())
			CLIENT.EVENT_MANAGER.handleSoundEvent(packet, ci);
	}

	@Final
	@Shadow
	private Set<PlayerInfo> listedPlayers;

	@Inject(method = "applyPlayerInfoUpdate", at = @At(value = "TAIL"))
	private void handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket.Action action, ClientboundPlayerInfoUpdatePacket.Entry entry, PlayerInfo playerInfo, CallbackInfo ci) {
		//CONFIG.prisonSection.otherSection.tabNpc
		if (modEnabled() && onMineland() && !tabNpc()) {
			if (isNpcDisplayName(playerInfo, entry) &&
					Objects.requireNonNull(action) == ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED)
				listedPlayers.remove(playerInfo);
		}
	}
}