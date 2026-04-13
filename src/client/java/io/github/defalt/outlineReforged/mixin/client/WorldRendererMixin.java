package io.github.defalt.outlineReforged.mixin.client;

import io.github.defalt.outlineReforged.client.render.OutlineRenderSupport;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {
    private static boolean outlineReforged$loggedMixinActive;

    @ModifyArg(
            method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDIF)V"),
            index = 6,
            require = 1
    )
    private int outlineReforged$replaceColor(int originalColor) {
        outlineReforged$logMixinActive();
        return OutlineRenderSupport.resolveConfiguredColor();
    }

    @ModifyArg(
            method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDIF)V"),
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