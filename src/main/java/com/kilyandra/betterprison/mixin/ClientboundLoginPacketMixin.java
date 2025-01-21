package com.kilyandra.betterprison.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.protocol.game.ClientboundLoginPacket;

import static com.kilyandra.betterprison.BetterPrison.onPrison;
import static com.kilyandra.betterprison.Utils.isBetterChunksEnabled;
import static com.kilyandra.betterprison.Utils.isModEnabled;


@Mixin(ClientboundLoginPacket.class)
public abstract class ClientboundLoginPacketMixin {

    @Inject(method = "chunkRadius", at = @At(value = "HEAD"), cancellable = true)
    private void changeServerRenderDistance(CallbackInfoReturnable<Integer> cir){
        if (!isModEnabled()) return;

        if (onPrison && isBetterChunksEnabled()) cir.setReturnValue(128);
    }

}
