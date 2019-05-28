package wolforce.net;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wolforce.Util;
import wolforce.Vaultopic;
import wolforce.VaultopicEvents.InvPos;

public class Searcher {

	public static void searchAndSend(EntityPlayerMP player, String filterText, int range) {
		if (filterText.trim().equals(""))
			return;
		WorldServer world = (WorldServer) player.getEntityWorld();
		HashSet<IItemHandler> set = new HashSet<>();
		for (int x = -range; x < range; x++) {
			for (int y = -range; y < range; y++) {
				for (int z = -range; z < range; z++) {
					final BlockPos pos = new BlockPos(player.posX + x, player.posY + y, player.posZ + z);
					final TileEntity te = world.getTileEntity(pos);
					final Block block = world.getBlockState(pos).getBlock();

					if (te != null) {
						IItemHandler inv = getInv(te);
						if (inv == null)
							continue;
						if (set.contains(inv))
							continue;
						set.add(inv);
						ArrayList<ItemStack> items = searchinv(inv, filterText.trim());
						if (!items.isEmpty()) {
							ItemStack[] array = new ItemStack[items.size()];
							for (int i = 0; i < array.length; i++) {
								array[i] = items.get(i);
							}
							world.addScheduledTask(() -> {
								VTDeliver message = new VTDeliver();
								message.inv = new InvPos(pos, array);
								Vaultopic.NET.sendTo(message, player);
							});
						}
					}

				}
			}
		}
	}

	private static ArrayList<ItemStack> searchinv(IItemHandler inv, String filterText) {
		ArrayList<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (Util.isValid(stack) && isValid(stack, filterText.toLowerCase()))
				items.add(stack);
		}
		return items;
	}

	private static boolean isValid(ItemStack stack, String filterText) {

		String itemname = stack.getDisplayName().toLowerCase();
		String modname = stack.getItem().getRegistryName().getResourceDomain().toLowerCase();

		String[] filters = filterText.split(" ");

		System.out.println(modname);
		for (String filter : filters) {
			if (filter.length() == 0)
				continue;
			System.out.println(filter.substring(1));
			if (filter.startsWith("@")) {
				if (!modname.contains(filter.substring(1)))
					return false;
			} else if (!itemname.contains(filter))
				return false;
		}
		return true;
	}

	private static IItemHandler getInv(TileEntity te) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}

		return null;

	}
}
