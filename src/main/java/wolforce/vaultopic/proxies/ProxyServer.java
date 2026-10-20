package wolforce.vaultopic.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.vaultopic.GuiHandlerVICE;
import wolforce.vaultopic.Vaultopic;

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
	public void openView(EntityPlayer player, boolean lastView) {
	}

	@Override
	public void openVice(EntityPlayer player) {
		player.openGui(Vaultopic.instance, Vaultopic.VICE, player.world, 0, 0, 0);
	}

	@Override
	public EntityPlayer getPlayer(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

}
