package com.kilyandra.betterprison.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.protocol.game.ClientboundLoginPacket;

import static com.kilyandra.betterprison.BetterPrison.LOGGER;
import static com.kilyandra.betterprison.Utils.onPrison;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.modEnabled;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.betterChunks;


@Mixin(ClientboundLoginPacket.class)
public abstract class ClientboundLoginPacketMixin {

    @Inject(method = "chunkRadius", at = @At(value = "HEAD"), cancellable = true)
    private void changeServerRenderDistance(CallbackInfoReturnable<Integer> cir){
        LOGGER.info("on prison: {}", onPrison());
        if (modEnabled() && onPrison() && betterChunks()) cir.setReturnValue(128);
    }

}
