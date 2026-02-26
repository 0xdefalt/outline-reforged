package io.github.defalt.outlineReforged.mixin.client;

import io.github.defalt.outlineReforged.client.config.OutlineConfig;
import io.github.defalt.outlineReforged.client.config.OutlineConfigManager;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(
            method = "renderTargetBlockOutline",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;drawCurrentLayer()V")
    )
    private void outlineReforged$renderOutlineFill(VertexConsumerProvider.Immediate immediate, MatrixStack matrices, boolean renderBlockOutline, WorldRenderState renderStates, CallbackInfo ci) {
        OutlineRenderState outlineRenderState = renderStates.outlineRenderState;
        if (outlineRenderState == null || outlineRenderState.isTranslucent() != renderBlockOutline) {
            return;
        }
        int argb = OutlineConfigManager.getCurrentFillArgbColor();
        if (((argb >>> 24) & 0xFF) == 0) {
            return;
        }
        Vec3d cameraPos = renderStates.cameraRenderState.pos;
        VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayers.debugFilledBox());
        MatrixStack.Entry entry = matrices.peek();
        double offsetX = outlineRenderState.pos().getX() - cameraPos.x;
        double offsetY = outlineRenderState.pos().getY() - cameraPos.y;
        double offsetZ = outlineRenderState.pos().getZ() - cameraPos.z;
        outlineRenderState.shape().forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            float x1 = (float) (offsetX + minX);
            float y1 = (float) (offsetY + minY);
            float z1 = (float) (offsetZ + minZ);
            float x2 = (float) (offsetX + maxX);
            float y2 = (float) (offsetY + maxY);
            float z2 = (float) (offsetZ + maxZ);
            vertexConsumer.vertex(entry, x1, y1, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y1, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y2, z1).color(argb);
            vertexConsumer.vertex(entry, x1, y2, z1).color(argb);
            vertexConsumer.vertex(entry, x1, y1, z2).color(argb);
            vertexConsumer.vertex(entry, x2, y1, z2).color(argb);
            vertexConsumer.vertex(entry, x2, y2, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y2, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y1, z1).color(argb);
            vertexConsumer.vertex(entry, x1, y1, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y2, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y2, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y1, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y1, z2).color(argb);
            vertexConsumer.vertex(entry, x2, y2, z2).color(argb);
            vertexConsumer.vertex(entry, x2, y2, z1).color(argb);
            vertexConsumer.vertex(entry, x1, y2, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y2, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y2, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y2, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y1, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y1, z1).color(argb);
            vertexConsumer.vertex(entry, x2, y1, z2).color(argb);
            vertexConsumer.vertex(entry, x1, y1, z2).color(argb);
        });
    }

}