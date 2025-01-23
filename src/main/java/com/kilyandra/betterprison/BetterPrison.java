package com.kilyandra.betterprison;

import com.kilyandra.betterprison.chunk.ChunkManager;
import com.kilyandra.betterprison.client.ClientManager;
import com.kilyandra.betterprison.config.ModConfig;

import net.fabricmc.api.ClientModInitializer;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class BetterPrison implements ClientModInitializer {
	public static Logger LOGGER;
	public static ModConfig CONFIG;

	public static ChunkManager CHUNK_MANAGER;
	public static ClientManager CLIENT_MANAGER;

	@Override
	public void onInitializeClient() {
		LOGGER = LoggerFactory.getLogger("betterprison");

		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		CHUNK_MANAGER = new ChunkManager();
		CLIENT_MANAGER = new ClientManager();
	}
}