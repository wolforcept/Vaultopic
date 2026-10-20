package wolforce.vaultopic.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface Proxy {

	void pre(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);

	void post(FMLPostInitializationEvent event);

	//

	void openView(EntityPlayer player, boolean lastView);

	void openVice(EntityPlayer player);

	EntityPlayer getPlayer(MessageContext ctx);

}
