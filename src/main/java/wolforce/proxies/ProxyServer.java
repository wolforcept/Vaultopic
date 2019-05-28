package wolforce.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import wolforce.GuiHandlerVICE;
import wolforce.Vaultopic;

public class ProxyServer implements Proxy {

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
	}

	@Override
	public void openGuiVice(EntityPlayer player) {
	}

}
