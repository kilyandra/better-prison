package com.kilyandra.betterprison.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.Minecraft;

import static com.kilyandra.betterprison.Utils.*;


public class ClientManager {
    public EventHandler EVENT_HANDLER;

    public boolean onMineland;
    public boolean onPrison;

    public ClientManager() {
        ClientPlayConnectionEvents.JOIN.register(this::onConnect);
    }

    private void onConnect(ClientPacketListener listener, PacketSender sender, Minecraft client) {
        client.executeBlocking(() -> {
            EVENT_HANDLER = new EventHandler(listener, client);

            onMineland = isMinelandIp(client);
            onPrison = false;
        });
    }
}
