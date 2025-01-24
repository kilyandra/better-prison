package com.kilyandra.betterprison.client;

import com.kilyandra.betterprison.client.chunk.ChunkManager;
import com.kilyandra.betterprison.client.event.EventManager;
import com.kilyandra.betterprison.client.keybind.KeybindManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.Minecraft;

import static com.kilyandra.betterprison.utils.Utils.isMinelandIp;


public class ClientManager {
    public final KeybindManager KEYBIND_MANAGER;
    public final ChunkManager CHUNK_MANAGER;
    public EventManager EVENT_MANAGER;

    public boolean onMineland;
    public boolean onPrison;

    public ClientManager() {
        KEYBIND_MANAGER = new KeybindManager();
        CHUNK_MANAGER = new ChunkManager();

        ClientPlayConnectionEvents.JOIN.register(this::onConnect);
    }

    private void onConnect(ClientPacketListener listener, PacketSender sender, Minecraft client) {
        client.executeBlocking(() -> {
            EVENT_MANAGER = new EventManager(listener, client);

            onMineland = isMinelandIp(client);
            onPrison = false;
        });
    }
}
