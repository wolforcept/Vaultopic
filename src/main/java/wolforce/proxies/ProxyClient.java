package wolforce.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import wolforce.GuiHandlerVICE;
import wolforce.GuiVICE;
import wolforce.GuiVIEW;
import wolforce.Main;
import wolforce.Vaultopic;
import wolforce.VaultopicEvents;

public class ProxyClient implements Proxy {

	@Override
	public void pre(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Vaultopic.instance, new GuiHandlerVICE());

	}

	@Override
	public void post(FMLPostInitializationEvent event) {
	}

	@Override
	public void openGuiSearch(EntityPlayer player) {
		VaultopicEvents.clearInventories();
		net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new GuiVIEW(player.isSneaking()));
	}

	@Override
	public void openGuiVice(EntityPlayer player) {
		net.minecraft.client.Minecraft.getMinecraft()
				.displayGuiScreen(new GuiVICE(player.inventory, player.world, player.getPosition()));
	}

}
