package com.kilyandra.betterprison.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;

import static com.kilyandra.betterprison.utils.Utils.onPrison;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.modEnabled;
import static com.kilyandra.betterprison.config.utils.ConfigGetters.betterChunks;


@Mixin(ClientboundSetChunkCacheRadiusPacket.class)
public abstract class ClientboundSetChunkCacheRadiusPacketMixin {

    @Inject(method = "getRadius", at = @At(value = "HEAD"), cancellable = true)
    private void changeServerChunkCacheRadius(CallbackInfoReturnable<Integer> cir){
        //Изменение дальности кеширования чанков для CONFIG.prisonSection.renderSection.betterChunks
        if (modEnabled() && onPrison() && betterChunks())
            cir.setReturnValue(128);
    }

}
