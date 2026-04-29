package io.github.defalt.blockselector.mixin.client;

import io.github.defalt.blockselector.client.render.BlockSelectorRenderSupport;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {
    private static boolean blockSelector$loggedMixinActive;

    @ModifyArg(
            method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDIF)V"),
            index = 6,
            require = 1
    )
    private int blockSelector$replaceColor(int originalColor) {
        blockSelector$logMixinActive();
        return BlockSelectorRenderSupport.resolveConfiguredColor();
    }

    @ModifyArg(
            method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDIF)V"),
            index = 7,
            require = 1
    )
    private float blockSelector$replaceWidth(float originalWidth) {
        blockSelector$logMixinActive();
        return BlockSelectorRenderSupport.resolveConfiguredLineWidth();
    }

    private static void blockSelector$logMixinActive() {
        if (!blockSelector$loggedMixinActive) {
            blockSelector$loggedMixinActive = true;
        }
    }

}