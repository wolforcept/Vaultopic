package wolforce;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class InvLocRenderer {

	Minecraft mc = Minecraft.getMinecraft();

	private World world;
	private EntityPlayerSP player;
	private Vec3d diffPos;
	private float partialTicks;
	private RenderItem render;
	private float yaw, pitch;

	public InvLocRenderer(float partialTicks) {
		this.partialTicks = partialTicks;
		world = mc.world;
		player = mc.player;
		render = mc.getRenderItem();
		yaw = -mc.getRenderManager().playerViewY;
		pitch = mc.getRenderManager().playerViewX;
	}

	void setLocation(BlockPos location) {
		double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		diffPos = new Vec3d(location).subtract(px, py, pz);
	}

	void renderInventory(ItemStack[] stacks) {

		float x = (float) diffPos.x + .5f;
		float y = (float) diffPos.y + .5f;
		float z = (float) diffPos.z + .5f;

		double scale;
		if (stacks.length < 4)
			scale = .5;
		else if (stacks.length < 9)
			scale = .25;
		else
			scale = .175;

		renderStack(null);
		int xx = 0, yy = 0;
		int w = (int) Math.ceil(Math.sqrt(stacks.length));
		for (ItemStack stack : stacks) {
			GlStateManager.pushMatrix();

			GlStateManager.translate(x, y, z);

			GlStateManager.scale(scale, scale, scale);

			GlStateManager.rotate((float) yaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate((float) pitch, 1.0F, 0.0F, 0.0F);

			GlStateManager.translate(-(w / 2) + xx, -(w / 2) + yy, 0);
			// GlStateManager.translate(-i * scale * 5, 0, 0);

			GlStateManager.rotate(180, 1, 0, 0);
			GlStateManager.rotate(180, 0, 0, 1);

			GlStateManager.scale(1, 1, 0);

			renderStack(stack);

			GlStateManager.popMatrix();

			xx++;
			if (xx == w) {
				xx = 0;
				yy++;
			}
		}

	}

	private void renderStack(ItemStack stack) {
		GlStateManager.pushAttrib();

		GlStateManager.enableLighting();
		GlStateManager.enableLight(0);
		GlStateManager.enableLight(1);
		GlStateManager.enableColorMaterial();
		GlStateManager.colorMaterial(1032, 5634);
		GlStateManager.shadeModel(7424);

		if (stack != null)
			render.renderItem(stack, TransformType.GUI);

		GlStateManager.popAttrib();
	}

}
