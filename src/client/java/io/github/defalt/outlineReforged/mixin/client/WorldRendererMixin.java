package io.github.defalt.outlineReforged.mixin.client;

import io.github.defalt.outlineReforged.client.config.OutlineConfig;
import io.github.defalt.outlineReforged.client.config.OutlineConfigManager;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @ModifyArg(
            method = "renderTargetBlockOutline",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;DDDLnet/minecraft/client/render/state/OutlineRenderState;IF)V"),
            index = 6
    )
    private int outlineReforged$modifyOutlineColor(int originalColor) {
        OutlineConfig config = OutlineConfigManager.getConfig();
        int argb = OutlineConfigManager.getCurrentArgbColor();
        int alpha = Math.min(255, ((argb >>> 24) & 0xFF) + config.outlineGlow * 12);
        return (alpha << 24) | (argb & 0x00FFFFFF);
    }

    @ModifyArg(
            method = "renderTargetBlockOutline",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;DDDLnet/minecraft/client/render/state/OutlineRenderState;IF)V"),
            index = 7
    )
    private float outlineReforged$modifyOutlineWidth(float originalWidth) {
        OutlineConfig outlineConfig = OutlineConfigManager.getConfig();
        float thickness = Math.max(0.5F, outlineConfig.outlineThickness);
        return thickness + (outlineConfig.outlineGlow * 0.15F);
    }

}