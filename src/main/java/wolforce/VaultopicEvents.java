package wolforce;

import java.util.Arrays;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class VaultopicEvents {

	private static final long TIME = 5000;
	private static LinkedList<InvPos> inventories = new LinkedList<>();

	/**
	 * Filtered Inventory at a Location
	 */
	public static class InvPos {
		public long timeAdded;
		public final BlockPos pos;
		public final ItemStack[] item;

		public InvPos(BlockPos location, ItemStack... item) {
			this.pos = location;
			this.item = item;
		}

		@Override
		public String toString() {
			return "InvPos [timeAdded=" + timeAdded + ", item=" + Arrays.toString(item) + ", pos=" + pos + "]";
		}

	}

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {

		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.clearDepth(1.0D);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		InvLocRenderer invlocrenderer = new InvLocRenderer(event.getPartialTicks());
		long clearTime = System.currentTimeMillis() - TIME;

		LinkedList<InvPos> invClone = new LinkedList<>();
		synchronized (inventories) {
			for (InvPos invPos : inventories)
				invClone.add(invPos);
		}

		for (InvPos invPos : invClone) {
			invlocrenderer.setLocation(invPos.pos);
			invlocrenderer.renderInventory(invPos.item);
		}

		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	public static void add(InvPos invpos) {
		invpos.timeAdded = System.currentTimeMillis();
		synchronized (inventories) {
			VaultopicEvents.inventories.add(invpos);
		}
	}

	// {
	// GlStateManager.pushMatrix();
	// GlStateManager.translate(x, y, z);
	// GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
	// GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
	// GlStateManager.rotate(viewerPitch, 1.0F, 0.0F, 0.0F);
	// GlStateManager.scale(-0.025F, -0.025F, 0.025F);
	// GlStateManager.disableLighting();
	// GlStateManager.depthMask(false);
	// GlStateManager.disableDepth();
	//
	// GlStateManager.enableBlend();
	// // GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
	// // GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
	// // GlStateManager.SourceFactor.ONE,
	// // GlStateManager.DestFactor.ZERO);
	// // int i = fontRendererIn.getStringWidth(str) / 2;
	// // GlStateManager.disableTexture2D();
	// // Tessellator tessellator = Tessellator.getInstance();
	// // BufferBuilder bufferbuilder = tessellator.getBuffer();
	// // bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
	// // bufferbuilder.pos((double) (-i - 1), (double) (-1), 0.0D).color(0.0F,
	// 0.0F,
	// // 0.0F, 0.25F).endVertex();
	// // bufferbuilder.pos((double) (-i - 1), (double) (8), 0.0D).color(0.0F, 0.0F,
	// // 0.0F, 0.25F).endVertex();
	// // bufferbuilder.pos((double) (i + 1), (double) (8), 0.0D).color(0.0F, 0.0F,
	// // 0.0F, 0.25F).endVertex();
	// // bufferbuilder.pos((double) (i + 1), (double) (-1), 0.0D).color(0.0F, 0.0F,
	// // 0.0F, 0.25F).endVertex();
	// // tessellator.draw();
	// // GlStateManager.enableTexture2D();
	//
	// // GlStateManager.pushMatrix();
	// // GlStateManager.translate(-0.5F, -0.5F, -0.5F);
	// // GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	// // GlStateManager.enableRescaleNormal();
	// // GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
	// // stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
	// // GlStateManager.popMatrix();
	//
	// GlStateManager.scale(20, 20, 20);
	// GlStateManager.pushAttrib();
	// RenderHelper.enableStandardItemLighting();
	// mc.getRenderItem().renderItem(stack,
	// ItemCameraTransforms.TransformType.FIXED);
	// RenderHelper.disableStandardItemLighting();
	// GlStateManager.popAttrib();
	//
	// // fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, 0,
	// // 553648127);
	// GlStateManager.enableDepth();
	// GlStateManager.depthMask(true);
	// // fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, 0,
	// // -1);
	// GlStateManager.enableLighting();
	// GlStateManager.disableBlend();
	// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	// GlStateManager.popMatrix();
	// }

	private static void drawText(String text, int x, int y) {
		FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
		int color = 0xFFFFFF;
		fontRender.drawStringWithShadow(text, x, y, color);
	}

	public static void clearInventories() {
		inventories.clear();
	}

	//

	//

}
