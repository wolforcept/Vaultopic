package wolforce;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import wolforce.VaultopicEvents.InvPos;
import wolforce.net.Searcher;
import wolforce.net.VTDeliver;
import wolforce.net.VTRequest;
import wolforce.net.VaultopicClientNet;
import wolforce.net.VaultopicServerNet;
import wolforce.proxies.Proxy;

@Mod(modid = Vaultopic.MODID, name = Vaultopic.NAME, version = Vaultopic.VERSION)
@Mod.EventBusSubscriber(modid = Vaultopic.MODID)
public class Vaultopic {

	@Instance(Vaultopic.MODID)
	public static Vaultopic instance;

	@SidedProxy(serverSide = "wolforce.proxies.ProxyServer", clientSide = "wolforce.proxies.ProxyClient")
	public static Proxy proxy;

	public static final String MODID = "vaultopic";
	public static final String NAME = "Vaultopic";
	public static final String VERSION = "0.2";
	public static final Logger logger = LogManager.getLogger(NAME);

	public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(Vaultopic.MODID);

	public static final int VICE = 1;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Main.preInit(event);
		proxy.pre(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.post(event);
		if (event.getSide() == Side.CLIENT) {
			VaultopicClientNet.init();
		} else
			VaultopicServerNet.init();
	}

	public static void onRequest(VTRequest message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		final String filterText = message.filterText;
		serverPlayer.getServerWorld().addScheduledTask(() -> {
			Searcher.searchAndSend(serverPlayer, filterText, getRange(ctx.getServerHandler().player));
		});
	}

	private static int getRange(EntityPlayerMP player) {
		if (player.getHeldItemMainhand().getItem() instanceof ItemVIEW)
			return ((ItemVIEW) player.getHeldItemMainhand().getItem()).range;
		if (player.getHeldItemOffhand().getItem() instanceof ItemVIEW)
			return ((ItemVIEW) player.getHeldItemOffhand().getItem()).range;
		return 5;
	}

	public static void onDeliver(VTDeliver message, MessageContext ctx) {
		InvPos inv = message.inv;
		VaultopicEvents.add(inv);
	}

}
