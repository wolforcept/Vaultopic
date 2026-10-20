package wolforce.vaultopic.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import wolforce.vaultopic.VaultopicConfig;

@Mod.EventBusSubscriber(Side.CLIENT)
public class VIEWRenderer {

	static Minecraft mc = Minecraft.getMinecraft();

	private static final long TIME = VaultopicConfig.meta.viewtime * 1000;
	static LinkedList<InvPos> inventories = new LinkedList<>();
	public static int range = 0;
	private static long endTime;

	/**
	 * Filtered Inventory at a Location
	 */
	public static class InvPos {

		public final BlockPos pos;
		public final EnumFacing facing;

		public final ItemStackInSlot[] items;

		public InvPos(BlockPos location, EnumFacing facing, ItemStackInSlot... item) {
			this.pos = location;
			this.facing = facing;
			this.items = item;
		}

		@Override
		public String toString() {
			return "InvPos[item=" + Arrays.toString(items) + ", pos=" + pos + "]";
		}

		public InvPos emptyClone() {
			return new InvPos(pos, facing);
		}

	}

	public static class ItemStackInSlot {
		public final ItemStack stack;
		public final int slot;

		public ItemStackInSlot(ItemStack stack, int slot) {
			this.stack = stack;
			this.slot = slot;
		}
	}

	@SubscribeEvent
	public static void onOpenContainer(PlayerContainerEvent.Open event) {
		clearInventories();
	}

	@SubscribeEvent
	public static void onOpenContainer(GuiOpenEvent event) {
		clearInventories();
	}

	static HashMap<BlockPos, LinkedList<InvPos>> invs = new HashMap<>();

	static float partialTicks = 0;

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		partialTicks = event.getPartialTicks();
		Projector.loadMatrixes(partialTicks);
	}

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderGameOverlayEvent.Post event) {

		VIEWGrabber.clearMiddle();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();

		// GlStateManager.pushMatrix();
		// GlStateManager.pushAttrib();

		// mc.entityRenderer.setupOverlayRendering();
		// GlStateManager.enableDepth();
		// RenderHelper.enableGUIStandardItemLighting();

		double actualX = mc.player.prevPosX + (mc.player.posX - mc.player.prevPosX) * partialTicks;
		double actualY = mc.player.prevPosY + (mc.player.posY - mc.player.prevPosY) * partialTicks;
		double actualZ = mc.player.prevPosZ + (mc.player.posZ - mc.player.prevPosZ) * partialTicks;

		synchronized (inventories) {
			invs.clear();
			for (Iterator<InvPos> iterator = inventories.iterator(); iterator.hasNext();) {
				InvPos invPos = (InvPos) iterator.next();
				double dist = new Vec3d(invPos.pos)
						.distanceTo(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ));
				if (dist > range || mc.player.world.getTileEntity(invPos.pos) == null)
					iterator.remove();
				else {
					if (!invs.containsKey(invPos.pos)) {
						LinkedList<InvPos> newlist = new LinkedList<>();
						invs.put(invPos.pos, newlist);
					}
					invs.get(invPos.pos).add(invPos);
				}
			}
		}

		for (Entry<BlockPos, LinkedList<InvPos>> entry : invs.entrySet()) {

			BlockPos pos = entry.getKey();
			LinkedList<InvPos> invPoses = entry.getValue();

			if (invPoses.isEmpty())
				continue;

			float[] p = Projector.getScrXY(//
					(float) (pos.getX() - actualX + .5), //
					(float) (pos.getY() - actualY + .5), //
					(float) (pos.getZ() - actualZ + .5), //
					partialTicks //
			);

			if (p[2] < 1) {

				ScaledResolution sr = new ScaledResolution(mc);

				float guiscalex = (float) (mc.displayWidth / sr.getScaledWidth_double());
				float guiscaley = (float) (mc.displayHeight / sr.getScaledHeight_double());

				float finalx = p[0] / guiscalex;
				float finaly = (float) new ScaledResolution(mc).getScaledHeight_double() - p[1] / guiscaley;

				VIEWGrabber.renderInv(invPoses, finalx, finaly, 16);
			}
		}

		// GlStateManager.disableBlend();
		//
		// GlStateManager.popAttrib();
		// GlStateManager.popMatrix();

		if (System.currentTimeMillis() > endTime)
			clearInventories();
	}

	public static void add(InvPos invpos) {
		endTime = System.currentTimeMillis() + TIME;
		synchronized (inventories) {
			VIEWRenderer.inventories.add(invpos);
		}
	}

	public static void clearInventories() {
		inventories.clear();
		range = 0;
	}

}
