package com.kilyandra.betterprison;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static com.kilyandra.betterprison.BetterPrison.LOGGER;
import static com.kilyandra.betterprison.Utils.*;


public class ChunkManager {
    private static final String CHUNKS_DIR = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/prison_chunks/";
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static int chunksAmount;

    public void saveChunk(ClientboundLevelChunkWithLightPacket packet) {
        executorService.submit(() -> {
            createDirs(CHUNKS_DIR);
            writeToFile(unpackChunk(packet), CHUNKS_DIR + getFileName(packet));
        });
    }

    private static FriendlyByteBuf unpackChunk(ClientboundLevelChunkWithLightPacket packet) {
        FriendlyByteBuf chunk = new FriendlyByteBuf(Unpooled.buffer());
        packet.write(chunk);

        return chunk;
    }

    public void renderSavedChunks(ClientPacketListener listener) {
        FriendlyByteBuf[] chunks = loadChunks();
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

    private static FriendlyByteBuf[] loadChunks(){
        File[] files = Objects.requireNonNull(new File(CHUNKS_DIR).listFiles());
        FriendlyByteBuf[] chunks = new FriendlyByteBuf[files.length];

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            FriendlyByteBuf chunk = loadFromFile(file);
            chunks[i] = chunk;
        }
        return chunks;
    }

}
