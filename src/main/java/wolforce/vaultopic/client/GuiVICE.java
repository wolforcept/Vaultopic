package wolforce.vaultopic.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wolforce.vaultopic.ContainerVICE;

@SideOnly(Side.CLIENT)
public class GuiVICE extends GuiContainer {

	private static final ResourceLocation backgroundResourceLocation = new ResourceLocation(
			"textures/gui/container/crafting_table.png");
	public static final String name = "hwell_gui_vice";

	public GuiVICE(InventoryPlayer playerInventory, World world, BlockPos pos) {
		super(new ContainerVICE(playerInventory, world, pos));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(backgroundResourceLocation);

		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		this.fontRenderer.drawString("V.I.C.E.", 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		renderHoveredToolTip(mouseX - this.guiLeft, mouseY - this.guiTop);
	}
}