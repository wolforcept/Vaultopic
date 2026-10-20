package wolforce.vaultopic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wolforce.vaultopic.client.VIEWRenderer.InvPos;
import wolforce.vaultopic.client.VIEWRenderer.ItemStackInSlot;
import wolforce.vaultopic.net.VTDeliver;

public class Searcher {

	public static void grabAndSpawn(EntityPlayerMP serverPlayer, BlockPos invPos, int slotToGrab, EnumFacing facing,
			boolean grabAll) {

		TileEntity te = serverPlayer.getServerWorld().getTileEntity(invPos);
		if (te == null)
			return;

		IItemHandler inv = getInv(te, facing);
		if (inv == null)
			return;

		ItemStack grabbedStack = inv.extractItem(slotToGrab, grabAll ? 64 : 1, false);

		if (!VU.isValid(grabbedStack))
			return;

		VU.spawnItem(serverPlayer.world, new Vec3d(serverPlayer.posX, serverPlayer.posY + .5, serverPlayer.posZ),
				grabbedStack);
	}

	//

	//

	//

	public static void searchAndSend(EntityPlayerMP player, String filterText, int range) {
		if (filterText.trim().equals(""))
			return;

		WorldServer world = (WorldServer) player.getEntityWorld();
		HashSet<Integer> searchedInvs = new HashSet<>();

		for (int x = -range; x < range; x++) {
			for (int y = -range; y < range; y++) {
				for (int z = -range; z < range; z++) {
					final BlockPos pos = new BlockPos(player.posX + x, player.posY + y, player.posZ + z);
					final TileEntity te = world.getTileEntity(pos);
					// final Block block = world.getBlockState(pos).getBlock();

					if (te != null) {
						for (EnumFacing facing : EnumFacing.VALUES) {
							IItemHandler inv = getInv(te, facing);
							if (inv == null || searchedInvs.contains(hash(x, y, z, inv)))
								continue;
							searchedInvs.add(hash(x, y, z, inv));
							ArrayList<ItemStackInSlot> items = searchinv(inv, filterText.trim());
							if (!items.isEmpty()) {
								ItemStackInSlot[] array = new ItemStackInSlot[items.size()];
								for (int i = 0; i < array.length; i++) {
									array[i] = items.get(i);
								}
								world.addScheduledTask(() -> {
									VTDeliver message = new VTDeliver();
									message.inv = new InvPos(pos, facing, array);
									Vaultopic.NET.sendTo(message, player);
								});
							}
						}
					}

				}
			}
		}
	}

	private static Integer hash(int x, int y, int z, IItemHandler inv) {
		return (x + "" + y + "" + z + "" + inv.hashCode()).hashCode();
	}

	private static ArrayList<ItemStackInSlot> searchinv(IItemHandler inv, String filterText) {
		ArrayList<ItemStackInSlot> items = new ArrayList<>();
		for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (VU.isValid(stack) && isValid(stack, filterText.toLowerCase()))
				items.add(new ItemStackInSlot(stack, i));
		}
		return items;
	}

	private static boolean isValid(ItemStack stack, String filterText) {

		String itemname = stack.getDisplayName().toLowerCase();
		String modname = stack.getItem().getRegistryName().getResourceDomain().toLowerCase();

		String[] filters = filterText.split(" ");

		for (String filter : filters) {
			if (filter.length() == 0)
				continue;
			if (filter.startsWith("@")) {
				if (!modname.contains(filter.substring(1)))
					return false;
			} else if (!itemname.contains(filter))
				return false;
		}
		return true;
	}

	// static Field furnaceItemStacks;
	//
	// static {
	// try {
	// furnaceItemStacks =
	// EntityRenderer.class.getDeclaredField("furnaceItemStacks");
	// furnaceItemStacks.setAccessible(true);
	// } catch (NoSuchFieldException | SecurityException e) {
	// System.err.println("Could not access furnace item stacks");
	// }
	// }

	// @SuppressWarnings("unchecked")
	private static IItemHandler getInv(TileEntity te, EnumFacing facing) {

		// try {
		//
		// if (te instanceof TileEntityFurnace && furnaceItemStacks != null) {
		// if (facing == EnumFacing.NORTH) {
		// Object fis = furnaceItemStacks.get((TileEntityFurnace) te);
		//
		// if (fis != null && fis instanceof NonNullList)
		// return VaultUtil.makeItemHandler((NonNullList<ItemStack>) fis);
		// } else
		// return null;
		// }
		//
		// } catch (IllegalArgumentException | IllegalAccessException e) {
		// System.err.println("Could not access furnace item stacks");
		// }

		// if (te instanceof TileEntityFurnace && facing ==
		// EnumFacing.values()[VaultopicClientEvents.getNrForDebugFromHand()]) {
		// return null;
		// }

		if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
			return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}
		return null;

	}

	//

	//

	//

	//

	//

	//

	public static void searchNearbyInvs(World world, BlockPos _pos, EntityPlayer player, int range, boolean takeMax,
			ItemStack[][] ingredients, LinkedList<FoundStack> foundItems) {

		if (world == null || _pos == null || player == null || ingredients == null)
			return;

		for (int x = -range; x < range; x++) {
			for (int y = -range; y < range; y++) {
				for (int z = -range; z < range; z++) {
					final BlockPos pos = _pos.add(x, y, z);
					final TileEntity te = world.getTileEntity(pos);

					if (te == null || world.getBlockState(pos) == null)
						continue;

					final Block block = world.getBlockState(pos).getBlock();

					if (block == null)
						continue;

					for (EnumFacing facing : EnumFacing.VALUES) {

						IItemHandler inv = getInv(te, facing);

						if (inv == null)
							continue;

						LinkedList<Integer> indexes = searchInv(inv, ingredients, takeMax);
						// System.out.println(indexes.toArray());
						for (int index : indexes) {
							if (index >= 0 && index < inv.getSlots() //
									&& VU.isValid(inv.getStackInSlot(index))) {
								foundItems.add(new FoundStack(inv, index));
							}
						}
					}
				}
			}
		}
	}

	private static LinkedList<Integer> searchInv(IItemHandler inv, ItemStack[][] ingredients, boolean takeMax) {
		LinkedList<Integer> indexes = new LinkedList<>();
		for (int i = 0; i < inv.getSlots(); i++) {
			if (searchInvSlot(inv, i, ingredients, takeMax)) {
				indexes.add(i);
			}
		}
		return indexes;
	}

	private static boolean searchInvSlot(IItemHandler inv, int i, ItemStack[][] ingredients, boolean takeMax) {
		ItemStack stack = inv.getStackInSlot(i);
		for (ItemStack[] ingredient : ingredients) {
			if (ingredient == null)
				continue;
			for (ItemStack ingredientStack : ingredient) {
				if (ingredientStack == null)
					continue;
				if (VU.isValid(stack) && VU.equalExceptAmount(stack, ingredientStack))
					return true;
			}
		}
		return false;
	}

	//

	//

	public static ItemStack getFromInv(EntityPlayer player, ItemStack[] ingredient) {
		for (int i = 0; i < 36; i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (VU.isValid(stack))
				for (ItemStack requestedStack : ingredient) {
					if (requestedStack == null)
						continue;
					if (VU.equalExceptAmount(stack, requestedStack)) {
						return player.inventory.decrStackSize(i, 1);
					}
				}
		}
		return null;
	}

	public static class FoundStack {
		public IItemHandler handler;
		public int index;

		public FoundStack(IItemHandler handler, int index) {
			this.handler = handler;
			this.index = index;
		}
	}

}
