package wolforce;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.client.Request;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wolforce.net.VTRequest;
import wolforce.net.VaultopicClientNet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiVICE extends GuiContainer {

	private static final ResourceLocation backgroundResourceLocation = new ResourceLocation(
			"textures/gui/container/crafting_table.png");

	public GuiVICE(InventoryPlayer playerInventory, World world, BlockPos pos) {
		super(new ContainerVICE(playerInventory, world, pos));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(I18n.translateToLocal(Main.vice.getUnlocalizedName() + ".name"), 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(backgroundResourceLocation);

		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
	}

}