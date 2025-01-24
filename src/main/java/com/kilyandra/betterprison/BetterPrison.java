package com.kilyandra.betterprison;

import com.kilyandra.betterprison.client.ClientManager;
import com.kilyandra.betterprison.config.ModConfig;

import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class BetterPrison implements ClientModInitializer {
	public static Logger LOGGER;
	public static ModConfig CONFIG;
	public static ClientManager CLIENT;

	@Override
	public void onInitializeClient() {
		LOGGER = LoggerFactory.getLogger("betterprison");
		CONFIG = ModConfig.getConfig();
		CLIENT = new ClientManager();
	}
}