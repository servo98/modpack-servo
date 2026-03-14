package com.servo.delivery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.servo.delivery.ServoDelivery;
import com.servo.delivery.block.DeliveryTerminalBlock;
import com.servo.delivery.block.DeliveryTerminalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/**
 * GeckoLib renderer for the Delivery Terminal.
 * Renders the full 3x3 multiblock model + progress overlay on screen panel.
 */
public class DeliveryTerminalRenderer extends GeoBlockRenderer<DeliveryTerminalBlockEntity> {

    public DeliveryTerminalRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(ServoDelivery.MOD_ID, "delivery_terminal")));
    }

    @Override
    public void render(DeliveryTerminalBlockEntity animatable, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!animatable.isFormed()) return;

        // Rotate GeckoLib model to match block FACING
        Direction facing = animatable.getBlockState().getValue(DeliveryTerminalBlock.FACING);
        float modelYRot = getYRotation(facing);

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(modelYRot));
        poseStack.translate(-0.5, 0, -0.5);
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();

        renderScreenOverlay(animatable, poseStack, bufferSource, packedLight);
    }

    private static float getYRotation(Direction facing) {
        // Axis.YP uses right-hand rule (CCW from above)
        // Model default: screen faces SOUTH (+Z)
        return switch (facing) {
            case SOUTH -> 0;
            case EAST -> 90;
            case NORTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    private void renderScreenOverlay(DeliveryTerminalBlockEntity terminal,
                                     PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Direction facing = terminal.getBlockState().getValue(DeliveryTerminalBlock.FACING);
        int progress = terminal.getProgressPercent();
        int chapter = terminal.getCurrentChapter();

        poseStack.pushPose();

        // Position at center of terminal block
        poseStack.translate(0.5, 0.5, 0.5);

        // Rotate to face the correct direction
        poseStack.mulPose(Axis.YP.rotationDegrees(getYRotation(facing)));

        // Move to the OUTER face of the screen panel (protrudes 1/16 past the block face)
        poseStack.translate(0, 0.125, 0.575);

        // Scale for text rendering
        float scale = 1.0f / 96.0f;
        poseStack.scale(scale, -scale, scale);

        Font font = Minecraft.getInstance().font;

        // Chapter label
        String title = "Ch." + chapter;
        font.drawInBatch(title, -font.width(title) / 2.0f, -22, 0xFF00d4aa,
                false, poseStack.last().pose(), bufferSource,
                Font.DisplayMode.POLYGON_OFFSET, 0, packedLight);

        // Progress percentage
        String pctText = progress + "%";
        font.drawInBatch(pctText, -font.width(pctText) / 2.0f, -10, 0xFFe0e0e0,
                false, poseStack.last().pose(), bufferSource,
                Font.DisplayMode.POLYGON_OFFSET, 0, packedLight);

        // Text-based progress bar: ████░░░░ style
        int filled = progress / 10; // 0-10 blocks
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            bar.append(i < filled ? "\u2588" : "\u2591"); // █ or ░
        }
        int barColor = progress >= 100 ? 0xFF00ff88 : 0xFF00d4aa;
        font.drawInBatch(bar.toString(), -font.width(bar.toString()) / 2.0f, 2, barColor,
                false, poseStack.last().pose(), bufferSource,
                Font.DisplayMode.POLYGON_OFFSET, 0, packedLight);

        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(DeliveryTerminalBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        // Multiblock spans 3 wide, 4 tall (1 below + terminal + antenna + tip)
        return new AABB(
                pos.getX() - 2, pos.getY() - 1, pos.getZ() - 2,
                pos.getX() + 3, pos.getY() + 4, pos.getZ() + 3
        );
    }

    @Override
    public boolean shouldRenderOffScreen(DeliveryTerminalBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
