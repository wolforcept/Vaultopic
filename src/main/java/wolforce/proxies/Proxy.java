package wolforce.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.net.VTDeliver;
import wolforce.net.VTRequest;

public interface Proxy {

	void pre(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);

	void post(FMLPostInitializationEvent event);

	//

	void openGuiSearch(EntityPlayer player);

	void openGuiVice(EntityPlayer player);

}
