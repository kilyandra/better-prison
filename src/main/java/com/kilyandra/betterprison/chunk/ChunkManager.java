package com.kilyandra.betterprison.chunk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static com.kilyandra.betterprison.BetterPrison.LOGGER;
import static com.kilyandra.betterprison.chunk.ChunkUtils.*;


public class ChunkManager {
    private final String CHUNKS_DIR = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/prison_chunks/";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public int chunksAmount;

    public void saveChunk(ClientboundLevelChunkWithLightPacket packet) {
        executorService.submit(() -> {
            createDirs(CHUNKS_DIR);
            writeToFile(unpackChunk(packet), CHUNKS_DIR + getFileName(packet));
        });
    }

    public void renderSavedChunks(ClientPacketListener listener) {
        FriendlyByteBuf[] chunks = loadChunks(CHUNKS_DIR);
        chunksAmount = chunks.length;

        LOGGER.info("Начинается вставка чанков...");
        listener.handleChunkBatchStart(new ClientboundChunkBatchStartPacket());

        for (FriendlyByteBuf chunk : chunks) {
            var chunkPacket = new ClientboundLevelChunkWithLightPacket(chunk);
            listener.handleLevelChunkWithLight(chunkPacket);
        }

        listener.handleChunkBatchFinished(new ClientboundChunkBatchFinishedPacket(chunksAmount));

        LOGGER.info("Вставка чанков завершена.");
    }
}
