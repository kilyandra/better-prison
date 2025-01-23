package com.kilyandra.betterprison.mixin;

import net.minecraft.client.multiplayer.ChunkBatchSizeCalculator;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kilyandra.betterprison.BetterPrison.LOGGER;
import static com.kilyandra.betterprison.BetterPrison.CHUNK_MANAGER;
import static com.kilyandra.betterprison.BetterPrison.CLIENT_MANAGER;
import static com.kilyandra.betterprison.Utils.onPrison;
import static com.kilyandra.betterprison.Utils.onMineland;
import static com.kilyandra.betterprison.Utils.onNettyThread;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.modEnabled;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.betterChunks;


@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleForgetLevelChunk", at = @At(value = "HEAD"), cancellable = true)
	private void handleForgetLevelChunk(ClientboundForgetLevelChunkPacket packet, CallbackInfo ci) {
		if (modEnabled() && onPrison() && betterChunks()) ci.cancel();
	}

	@Inject(method = "handleLevelChunkWithLight", at = @At(value = "HEAD"))
	private void handleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
		if (modEnabled() && onPrison() && betterChunks() && onNettyThread()) CHUNK_MANAGER.saveChunk(packet);
	}

	@Final
	@Shadow
	private ChunkBatchSizeCalculator chunkBatchSizeCalculator;

	@Inject(method = "handleChunkBatchFinished", at = @At(value = "HEAD"), cancellable = true)
	private void handleLocalChunksBatchFinished(ClientboundChunkBatchFinishedPacket packet, CallbackInfo ci) {
		if (modEnabled() && onPrison() && packet.batchSize() == CHUNK_MANAGER.chunksAmount) {
			this.chunkBatchSizeCalculator.onBatchFinished(CHUNK_MANAGER.chunksAmount);
			ci.cancel();
		}
	}

	@Inject(method = "handleAddObjective", at = @At(value = "HEAD"), cancellable = true)
	private void handleScoreboardName(ClientboundSetObjectivePacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland()) CLIENT_MANAGER.EVENT_HANDLER.handleSetScoreboard(packet, ci);
	}

	@Inject(method = "handleOpenScreen", at = @At(value = "HEAD"), cancellable = true)
	private void handleOpenedContainer(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland()) CLIENT_MANAGER.EVENT_HANDLER.handleOpenedContainer(packet, ci);
	}

	@Inject(method = "handleSystemChat", at = @At(value = "HEAD"), cancellable = true)
	private void handleChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland()) CLIENT_MANAGER.EVENT_HANDLER.handleChatMessage(packet, ci);
	}

	@Inject(method = "handleSoundEvent", at = @At(value = "HEAD"), cancellable = true)
	private void handleSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
		if (modEnabled() && onMineland()) CLIENT_MANAGER.EVENT_HANDLER.handleSoundEvent(packet, ci);
	}
}