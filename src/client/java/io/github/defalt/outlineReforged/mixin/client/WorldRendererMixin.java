package io.github.defalt.outlineReforged.mixin.client;

import io.github.defalt.outlineReforged.client.render.OutlineRenderSupport;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    private static boolean outlineReforged$loggedMixinActive;

    @ModifyArg(
            method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;DDDLnet/minecraft/client/render/state/OutlineRenderState;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDIF)V"),
            index = 6,
            require = 1
    )
    private int outlineReforged$replaceColor(int originalColor) {
        outlineReforged$logMixinActive();
        return OutlineRenderSupport.resolveConfiguredColor();
    }

    @ModifyArg(
            method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;DDDLnet/minecraft/client/render/state/OutlineRenderState;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDIF)V"),
            index = 7,
            require = 1
    )
    private float outlineReforged$replaceWidth(float originalWidth) {
        outlineReforged$logMixinActive();
        return OutlineRenderSupport.resolveConfiguredLineWidth();
    }

    private static void outlineReforged$logMixinActive() {
        if (!outlineReforged$loggedMixinActive) {
            outlineReforged$loggedMixinActive = true;
        }
    }

}