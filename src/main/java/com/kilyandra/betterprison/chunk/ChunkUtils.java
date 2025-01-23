package com.kilyandra.betterprison.chunk;

import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.FriendlyByteBuf;

import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.kilyandra.betterprison.BetterPrison.LOGGER;


public class ChunkUtils {

    public static FriendlyByteBuf[] loadChunks(String dir){
        File[] files = Objects.requireNonNull(new File(dir).listFiles());
        FriendlyByteBuf[] chunks = new FriendlyByteBuf[files.length];

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            FriendlyByteBuf chunk = loadFromFile(file);
            chunks[i] = chunk;
        }
        return chunks;
    }

    public static FriendlyByteBuf loadFromFile(File file){
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            return new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes));

        } catch (IOException e) {
            LOGGER.error("Ошибка при чтении файла: {} ", e.getMessage());
            return null;
        }
    }

    public static void writeToFile(FriendlyByteBuf buf, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            fos.write(bytes);

        } catch (IOException e) {
            LOGGER.error("Ошибка записи в файл: {} ", e.getMessage());
        }
    }

    public static FriendlyByteBuf unpackChunk(ClientboundLevelChunkWithLightPacket packet) {
        FriendlyByteBuf chunk = new FriendlyByteBuf(Unpooled.buffer());
        packet.write(chunk);

        return chunk;
    }

    public static String getFileName(ClientboundLevelChunkWithLightPacket packet) {
        int X = packet.getX();
        int Z = packet.getZ();

        return "chunk_" + X + "_" + Z + ".data";
    }

    public static void createDirs(String dir){
        File file = new File(dir);

        if (!file.mkdirs() && !file.exists()) {
            LOGGER.error("Не удалось создать директории");
        }
    }
}
