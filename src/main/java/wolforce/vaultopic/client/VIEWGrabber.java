package wolforce.vaultopic.client;

import java.util.LinkedList;

import mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.client.VIEWRenderer.InvPos;
import wolforce.vaultopic.client.VIEWRenderer.ItemStackInSlot;
import wolforce.vaultopic.net.VTGrab;

@Mod.EventBusSubscriber(modid = Vaultopic.MODID, value = Side.CLIENT)
public class VIEWGrabber {

	static Minecraft mc = Minecraft.getMinecraft();
	static ItemStackRenderer isr = new ItemStackRenderer();

	private static InvPos invInMiddle;
	private static int slotInMiddle;

	static void clearMiddle() {
		invInMiddle = null;
		slotInMiddle = -1;
	}

	public static void renderInv(LinkedList<InvPos> invPoses, float x, float y, int scale) {

		int d = 16;

		// GlStateManager.pushMatrix();
		// GlStateManager.pushAttrib();
		// GlStateManager.loadIdentity();

		ScaledResolution sr = new ScaledResolution(mc);

		int sum = 0;
		for (InvPos invPos : invPoses) {
			sum += invPos.items.length;
		}
		float dx = 0, maxDx = Math.round(Math.sqrt(sum));
		float dy = 0;
		for (InvPos invPos : invPoses) {

			for (ItemStackInSlot stack : invPos.items) {

				int xx = (int) (x + dx - (maxDx * d / 2));
				int yy = (int) (y + dy - (maxDx * d / 2));

				int cx = xx - sr.getScaledWidth() / 2;
				int cy = yy - sr.getScaledHeight() / 2;
				if (cx <= 0 && cx >= -d && cy <= 0 && cy >= -d) {
					invInMiddle = invPos;
					slotInMiddle = stack.slot;
					Gui.drawRect(xx, yy, (int) (xx + d), (int) (yy + d), 0x55FFFFFF);
				}

				// GlStateManager.translate(-xx, -yy, 0);
				// GlStateManager.scale(2, 2, 1);
				// GlStateManager.translate(xx, yy, 0);

				isr.render(mc, xx, yy, stack.stack);

				dx += d + 1;
				if (dx > maxDx * d) {
					dx = 0;
					dy += d + 1;
				}
			}
		}

		// RenderHelper.disableStandardItemLighting();
		// GlStateManager.popMatrix();
		// GlStateManager.popAttrib();
	}

	@SubscribeEvent
	public static void onLeftClick(MouseEvent event) {
		if (event.getButton() == 0) {
			if (slotInMiddle >= 0) {
				if (!event.isButtonstate()) {
					Minecraft.getMinecraft().addScheduledTask(() -> {
						VTGrab grab = new VTGrab();
						grab.invPos = invInMiddle.emptyClone();
						grab.slot = slotInMiddle;
						grab.grabAll = mc.player.isSneaking();
						Vaultopic.NET.sendToServer(grab);
					});
					VIEWRenderer.clearInventories();
					GuiVIEW.send();
				}
				event.setCanceled(true);

			}
		}
	}

}
