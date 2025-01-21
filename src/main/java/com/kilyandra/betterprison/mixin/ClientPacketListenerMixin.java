package com.kilyandra.betterprison.mixin;

import net.minecraft.client.multiplayer.ChunkBatchSizeCalculator;
import net.minecraft.network.protocol.game.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;

import static com.kilyandra.betterprison.BetterPrison.*;
import static com.kilyandra.betterprison.ChunkManager.chunksAmount;
import static com.kilyandra.betterprison.Utils.*;


@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

	@Final
	@Shadow
	private ChunkBatchSizeCalculator chunkBatchSizeCalculator;

    @Inject(method = "handleForgetLevelChunk", at = @At(value = "HEAD"), cancellable = true)
	private void cancelForgetLevelChunk(ClientboundForgetLevelChunkPacket packet, CallbackInfo ci) {
		if (!isModEnabled()) return;

		if (onPrison && isBetterChunksEnabled()) ci.cancel();
	}

	@Inject(method = "handleLevelChunkWithLight", at = @At(value = "HEAD"))
	private void handleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
		if (!isModEnabled()) return;

		if (onPrison && isBetterChunksEnabled() && onNettyThread()) CHUNK_MANAGER.saveChunk(packet);
	}

	@Inject(method = "handleChunkBatchFinished", at = @At(value = "HEAD"), cancellable = true)
	private void handleLocalChunksBatchFinished(ClientboundChunkBatchFinishedPacket packet, CallbackInfo ci) {
		if (packet.batchSize() == chunksAmount && isModEnabled()) {
			this.chunkBatchSizeCalculator.onBatchFinished(chunksAmount);
			ci.cancel();
		}
	}

	@Inject(method = "handleAddObjective", at = @At(value = "HEAD"), cancellable = true)
	private void checkScoreboardName(ClientboundSetObjectivePacket packet, CallbackInfo ci) {
		if (!isModEnabled()) return;

		if (onMineland) handleScoreboard(packet, ci);
	}

	@Inject(method = "handleOpenScreen", at = @At(value = "HEAD"), cancellable = true)
	private void getOpenedContainer(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
		if (!isModEnabled()) return;

		if (onMineland) handleOpenedContainer(packet, ci);
	}

	@Inject(method = "handleSystemChat", at = @At(value = "HEAD"), cancellable = true)
	private void getChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
		if (!isModEnabled()) return;

		//Подробнее в методе снизу
		handleChatMessage(packet, ci);
	}

	@Inject(method = "handleSoundEvent", at = @At(value = "HEAD"), cancellable = true)
	private void getSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
		if (!isModEnabled()) return;

		if (onMineland) handleSoundEvent(packet, ci);
	}
}