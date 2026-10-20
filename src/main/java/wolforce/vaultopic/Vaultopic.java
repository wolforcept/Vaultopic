package wolforce.vaultopic;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
import wolforce.vaultopic.Searcher.FoundStack;
import wolforce.vaultopic.client.VIEWRenderer;
import wolforce.vaultopic.net.MessageType;
import wolforce.vaultopic.net.VTDeliver;
import wolforce.vaultopic.net.VTDeliver.VTDeliverHandler;
import wolforce.vaultopic.net.VTGrab;
import wolforce.vaultopic.net.VTGrab.VTGrabHandler;
import wolforce.vaultopic.net.VTOpenVice;
import wolforce.vaultopic.net.VTOpenVice.VTOpenViceHandler;
import wolforce.vaultopic.net.VTRecipe;
import wolforce.vaultopic.net.VTRecipe.VTRecipeHandler;
import wolforce.vaultopic.net.VTRequest;
import wolforce.vaultopic.net.VTRequest.VTRequestHandler;
import wolforce.vaultopic.proxies.Proxy;

@Mod(modid = Vaultopic.MODID, name = Vaultopic.NAME, version = Vaultopic.VERSION)
@Mod.EventBusSubscriber(modid = Vaultopic.MODID)
public class Vaultopic {

	@Instance(Vaultopic.MODID)
	public static Vaultopic instance;

	@SidedProxy(serverSide = "wolforce.vaultopic.proxies.ProxyServer", clientSide = "wolforce.vaultopic.proxies.ProxyClient")
	public static Proxy proxy;

	public static final String MODID = "vaultopic";
	public static final String NAME = "Vaultopic";
	public static final String VERSION = "2.2";
	public static final Logger logger = LogManager.getLogger(NAME);

	public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(Vaultopic.MODID);

	public static final int VICE = 1;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		VaultMain.preInit(event);
		proxy.pre(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.post(event);

		/** The fourth parameter is the side that your packet will be *received* on: */
		Vaultopic.NET.registerMessage(//
				VTRequestHandler.class, VTRequest.class, MessageType.REQUEST.ordinal(), Side.SERVER);

		Vaultopic.NET.registerMessage(//
				VTDeliverHandler.class, VTDeliver.class, MessageType.DELIVER.ordinal(), Side.CLIENT);

		Vaultopic.NET.registerMessage(//
				VTRecipeHandler.class, VTRecipe.class, MessageType.RECIPE.ordinal(), Side.SERVER);

		Vaultopic.NET.registerMessage(//
				VTOpenViceHandler.class, VTOpenVice.class, MessageType.OPENVICE.ordinal(), Side.SERVER);

		Vaultopic.NET.registerMessage(//
				VTGrabHandler.class, VTGrab.class, MessageType.GRAB.ordinal(), Side.SERVER);
	}

	public static void onRequest(VTRequest message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		final String filterText = message.filterText;
		serverPlayer.getServerWorld().addScheduledTask(() -> {
			Searcher.searchAndSend(serverPlayer, filterText, getRange(ctx.getServerHandler().player));
		});
	}

	public static void onGrab(VTGrab message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		final BlockPos pos = message.getPos();
		final EnumFacing facing = message.getEnumFacing();
		final int slot = message.getSlot();
		final boolean grabAll = message.grabAll;
		serverPlayer.getServerWorld().addScheduledTask(() -> {
			Searcher.grabAndSpawn(serverPlayer, pos, slot, facing, grabAll);
		});
	}

	private static int getRange(EntityPlayer player) {
		// int range = getRange2(player);
		// System.out.println("range = " + range);
		// return range;
		// }
		// private static int getRange2(EntityPlayer player) {
		if (VU.playerHasItem(player, VaultMain.vice))
			return VaultMain.vice.range;
		if (VU.playerHasItem(player, VaultMain.view_big))
			return VaultMain.view_big.range;
		if (VU.playerHasItem(player, VaultMain.view_small))
			return VaultMain.view_small.range;
		return 0;
	}

	public static void onDeliver(VTDeliver message, MessageContext ctx) {
		VIEWRenderer.range = getRange(proxy.getPlayer(ctx));
		VIEWRenderer.add(message.inv);
	}

	public static void onOpenVice(VTOpenVice message, MessageContext ctx) {
		// proxy.openVice(ctx.getServerHandler().player);
		EntityPlayerMP player = ctx.getServerHandler().player;
		player.openGui(Vaultopic.instance, Vaultopic.VICE, player.world, (int) player.posX, (int) player.posY,
				(int) player.posZ);
	}

	public static void onRecipe(VTRecipe message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		if (player.openContainer instanceof ContainerVICE)
			recipeFill(false, (ContainerVICE) player.openContainer, message.items, player, message.maxTransfer);
	}

	public static void recipeFill(boolean isRemote, ContainerVICE container, ItemStack[][] requested,
			EntityPlayer player, boolean takeMax) {

		if (requested == null)
			return;

		World world = player.getEntityWorld();

		LinkedList<ItemStack> limbo = new LinkedList<>();

		for (int i = 0; i < 9; i++) {
			if (VU.isValid(container.craftMatrix.getStackInSlot(i))) {
				ItemStack stack = container.craftMatrix.removeStackFromSlot(i);
				if (!player.inventory.addItemStackToInventory(stack) && !isRemote) {
					limbo.add(stack);
				}
			}
		}
		if (!isRemote) {

			for (int i = 0; i < requested.length; i++) {
				if (requested[i] == null)
					continue;

				ItemStack fromLimbo = tryGetFromLimbo(requested[i], limbo);
				if (VU.isValid(fromLimbo))
					container.craftMatrix.setInventorySlotContents(i, fromLimbo);
				else {
					ItemStack stack = Searcher.getFromInv(player, requested[i]);
					if (VU.isValid(stack))
						container.craftMatrix.setInventorySlotContents(i, stack);
				}
			}

			if (!isComplete(requested, container.craftMatrix)) {

				LinkedList<FoundStack> foundItems = new LinkedList<>();
				Searcher.searchNearbyInvs(//
						world, player.getPosition(), player, getRange(player), takeMax, requested, foundItems);

				for (int i = 0; i < 9; i++) {
					if (requested[i] == null || VU.isValid(container.craftMatrix.getStackInSlot(i)))
						continue;

					ItemStack got = getOneFromFound(requested[i], foundItems);
					if (VU.isValid(got))
						container.craftMatrix.setInventorySlotContents(i, got);
				}

			}
		}

		for (ItemStack stack : limbo) {
			VU.spawnItem(world, player.getPosition(), stack);
		}
	}

	private static boolean isComplete(ItemStack[][] requested, InventoryCrafting craftMatrix) {
		for (int i = 0; i < requested.length; i++) {
			if (requested[i] == null)
				continue;
			ItemStack stack = craftMatrix.getStackInSlot(i);
			if (!isAny(stack, requested[i]))
				return false;
		}
		return true;
	}

	private static boolean isAny(ItemStack testStack, ItemStack[] ingredient) {
		for (ItemStack itemStack : ingredient) {
			if (VU.equalExceptAmount(testStack, itemStack))
				return true;
		}
		return false;
	}

	private static ItemStack getOneFromFound(ItemStack[] requested, LinkedList<FoundStack> foundItems) {
		for (ItemStack requestedStack : requested) {
			for (FoundStack foundStack : foundItems) {
				ItemStack stack = foundStack.handler.getStackInSlot(foundStack.index);
				if (VU.equalExceptAmount(requestedStack, stack) && VU.isValid(stack)) {
					return foundStack.handler.extractItem(foundStack.index, 1, false);
				}
			}
		}
		return null;
	}

	private static ItemStack tryGetFromLimbo(ItemStack[] requested, LinkedList<ItemStack> limbo) {
		for (ItemStack stackRequested : requested) {
			for (ItemStack stackLimbo : limbo) {
				if (VU.isValid(stackRequested) && VU.isValid(stackLimbo)
						&& VU.equalExceptAmount(stackLimbo, stackRequested)) {
					ItemStack ret = stackLimbo.copy();
					ret.setCount(1);
					stackLimbo.shrink(1);
					return ret;
				}
			}
		}
		return null;
	}

}
