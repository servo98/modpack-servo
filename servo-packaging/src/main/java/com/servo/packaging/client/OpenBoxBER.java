package com.servo.packaging.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.servo.packaging.block.OpenBoxBlock;
import com.servo.packaging.block.OpenBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Renders items visible inside an open (unsealed) box,
 * and a content stamp/label on the front face of sealed boxes.
 */
public class OpenBoxBER implements BlockEntityRenderer<OpenBoxBlockEntity> {

    public OpenBoxBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(OpenBoxBlockEntity be, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int light, int overlay) {

        if (be.getBlockState().getValue(OpenBoxBlock.SEALED)) {
            renderSealedStamp(be, partialTick, pose, buffers, light, overlay);
            return;
        }

        // === Open box: render floating items inside ===
        List<ItemStack> items = be.getVisibleItems();
        if (items.isEmpty()) return;

        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        long gameTime = be.getLevel() != null ? be.getLevel().getGameTime() : 0;
        float bob = (float) Math.sin((gameTime + partialTick) * 0.1) * 0.02f;

        for (int i = 0; i < items.size(); i++) {
            pose.pushPose();
            Vec3 offset = getItemOffset(i, items.size());
            pose.translate(0.5 + offset.x, 0.2 + bob, 0.5 + offset.z);
            pose.scale(0.35f, 0.35f, 0.35f);
            pose.mulPose(Axis.YP.rotationDegrees(45f * i));
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED,
                    light, overlay, pose, buffers, be.getLevel(), 0);
            pose.popPose();
        }
    }

    /**
     * Render the packed item's icon as a flat stamp on the front face of a sealed box.
     */
    private void renderSealedStamp(OpenBoxBlockEntity be, float partialTick, PoseStack pose,
                                    MultiBufferSource buffers, int light, int overlay) {
        ResourceLocation itemId = be.getPackedItemId();
        if (itemId == null) return;

        var item = BuiltInRegistries.ITEM.get(itemId);
        if (item == Items.AIR) return;

        ItemStack displayStack = new ItemStack(item);
        Direction facing = be.getBlockState().getValue(OpenBoxBlock.FACING);
        var itemRenderer = Minecraft.getInstance().getItemRenderer();

        pose.pushPose();

        // Box geometry: x=2..14, y=0..8, z=2..14 (in block pixels)
        // Front face center: depends on facing direction
        // Box center height: y = 4/16 = 0.25
        float faceOffset = 2.0f / 16.0f - 0.005f; // front face position, slight offset to avoid z-fighting

        switch (facing) {
            case NORTH -> {
                pose.translate(0.5, 0.25, faceOffset);
                // FIXED renders facing -Z, which is north — correct
            }
            case SOUTH -> {
                pose.translate(0.5, 0.25, 1.0 - faceOffset);
                pose.mulPose(Axis.YP.rotationDegrees(180));
            }
            case EAST -> {
                pose.translate(1.0 - faceOffset, 0.25, 0.5);
                pose.mulPose(Axis.YP.rotationDegrees(-90));
            }
            case WEST -> {
                pose.translate(faceOffset, 0.25, 0.5);
                pose.mulPose(Axis.YP.rotationDegrees(90));
            }
            default -> {
                pose.translate(0.5, 0.25, faceOffset);
            }
        }

        // Scale: the stamp should be ~6x6 MC pixels on the face
        // FIXED display renders ~1x1 in model space, so 0.25 scale = 4 block-pixels
        pose.scale(0.3f, 0.3f, 0.001f); // flat on the face

        itemRenderer.renderStatic(displayStack, ItemDisplayContext.FIXED,
                light, overlay, pose, buffers, be.getLevel(), 0);

        pose.popPose();
    }

    private Vec3 getItemOffset(int index, int total) {
        return switch (total) {
            case 1 -> Vec3.ZERO;
            case 2 -> switch (index) {
                case 0 -> new Vec3(-0.1, 0, 0);
                case 1 -> new Vec3(0.1, 0, 0);
                default -> Vec3.ZERO;
            };
            case 3 -> switch (index) {
                case 0 -> new Vec3(-0.1, 0, -0.06);
                case 1 -> new Vec3(0.1, 0, -0.06);
                case 2 -> new Vec3(0, 0, 0.08);
                default -> Vec3.ZERO;
            };
            default -> switch (index) { // 4
                case 0 -> new Vec3(-0.1, 0, -0.1);
                case 1 -> new Vec3(0.1, 0, -0.1);
                case 2 -> new Vec3(-0.1, 0, 0.1);
                case 3 -> new Vec3(0.1, 0, 0.1);
                default -> Vec3.ZERO;
            };
        };
    }
}
