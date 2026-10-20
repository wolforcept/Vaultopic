package wolforce.vaultopic;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.IItemHandler;
import wolforce.vaultopic.client.VIEWRenderer.InvPos;
import wolforce.vaultopic.client.VIEWRenderer.ItemStackInSlot;

public class VU {

	public static void setReg(Block block, String name) {
		block.setUnlocalizedName(Vaultopic.MODID + "." + name);
		block.setRegistryName(VU.res(name));
	}

	public static void setReg(Item block, String name) {
		block.setUnlocalizedName(Vaultopic.MODID + "." + name);
		block.setRegistryName(VU.res(name));
	}

	// ITEMSTACKS
	public static boolean isValid(ItemStack stack) {
		return stack != null && stack.getCount() > 0 && !stack.isEmpty() && !stack.getItem().equals(Items.AIR);
	}

	public static boolean equalExceptAmount(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata();
	}

	public static void spawnItem(World world, BlockPos pos, ItemStack stack, double... speeds) {
		spawnItem(world, new Vec3d(pos.getX() + .5, pos.getY(), pos.getZ() + .5), stack, speeds);
	}

	public static void spawnItem(World world, Vec3d pos, ItemStack stack, double... speeds) {
		spawnItem(world, pos, stack, 0, speeds);
	}

	/**
	 * default pickupDelay = 10 player throw pickupDelay = 40
	 */
	public static void spawnItem(World world, Vec3d pos, ItemStack stack, int pickupDelay, double... speeds) {
		if (!VU.isValid(stack))
			return;
		EntityItem entityitem = new EntityItem(world, pos.x, pos.y, pos.z, stack);
		if (speeds.length == 0) {
			entityitem.motionX = Math.random() * .4 - .2;
			entityitem.motionY = Math.random() * .2;
			entityitem.motionZ = Math.random() * .4 - .2;
		} else {
			entityitem.motionX = speeds[0];
			entityitem.motionY = speeds[1];
			entityitem.motionZ = speeds[2];
		}
		entityitem.setPickupDelay(pickupDelay);
		world.spawnEntity(entityitem);
	}

	public static ResourceLocation res(String string) {
		return new ResourceLocation(Vaultopic.MODID, string);
	}

	public static void writeInvPos(InvPos inv, ByteBuf buf) {
		buf.writeInt(inv.pos.getX());
		buf.writeInt(inv.pos.getY());
		buf.writeInt(inv.pos.getZ());
		buf.writeInt(inv.facing.ordinal());
		for (ItemStackInSlot stack : inv.items) {
			ByteBufUtils.writeItemStack(buf, stack.stack);
			buf.writeInt(stack.slot);
		}
	}

	public static InvPos readInvPos(ByteBuf buf) {
		int invX = buf.readInt();
		int invY = buf.readInt();
		int invZ = buf.readInt();
		EnumFacing facing = EnumFacing.values()[buf.readInt()];
		return new InvPos(new BlockPos(invX, invY, invZ), facing, readStacks(buf));
	}

	private static ItemStackInSlot[] readStacks(ByteBuf buf) {
		// read how many you can into list
		ArrayList<ItemStackInSlot> stacks = new ArrayList<>();
		while (buf.readableBytes() > 0) {
			ItemStack stack = ByteBufUtils.readItemStack(buf);
			int slot = buf.readInt();
			stacks.add(new ItemStackInSlot(stack, slot));
		}
		// make list into array
		ItemStackInSlot[] stackarray = new ItemStackInSlot[stacks.size()];
		for (int i = 0; i < stackarray.length; i++) {
			stackarray[i] = stacks.get(i);
		}
		return stackarray;
	}

	public static boolean playerHasItem(EntityPlayer player, Item item) {
		for (ItemStack stack : player.inventory.offHandInventory) {
			if (stack.getItem() == item)
				return true;
		}
		for (ItemStack stack : player.inventory.mainInventory) {
			if (stack.getItem() == item)
				return true;
		}
		return false;
	}

	public static IItemHandler makeItemHandler(NonNullList<ItemStack> nonnulllist) {
		return new IItemHandler() {

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				ItemStack inslot = getStackInSlot(slot);
				ItemStack outslot = stack.copy();
				if (equalExceptAmount(outslot, inslot)) {
					if (stack.getCount() + inslot.getCount() > 64) {
						outslot.setCount(outslot.getCount() + inslot.getCount() - 64);
						if (!simulate)
							inslot.setCount(64);
					} else {
						if (!simulate)
							inslot.grow(outslot.getCount());
						outslot.setCount(0);
					}
				}
				return outslot;
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				return nonnulllist.get(slot);
			}

			@Override
			public int getSlots() {
				return nonnulllist.size();
			}

			@Override
			public int getSlotLimit(int slot) {
				return 64;
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {

				ItemStack inslot = simulate ? getStackInSlot(slot).copy() : getStackInSlot(slot);
				ItemStack outslot = inslot.copy();

				outslot.setCount(Math.min(amount, inslot.getCount()));
				inslot.shrink(amount);

				return outslot;
			}
		};
	}

	public static int getNrForDebugFromHand() {
		return getNrForDebugFromHand(false);
	}

	public static int getNrForDebugFromHand(boolean secondHand) {
		return getNrForDebugFromHand(Minecraft.getMinecraft().player.world,
				Minecraft.getMinecraft().player.getPosition(), secondHand);
	}

	public static int getNrForDebugFromHand(World world, BlockPos pos, boolean secondHand) {
		return getNrForDebugFromHand(world, pos.getX(), pos.getY(), pos.getZ(), secondHand);
	}

	public static int getNrForDebugFromHand(World world, double x, double y, double z, boolean secondHand) {

		EntityPlayer player = world.getClosestPlayer(x, y, z, 1000, false);
		if (player == null)
			return 0;

		ItemStack stack = secondHand ? player.getHeldItemOffhand() : player.getHeldItemMainhand();

		if (VU.isValid(stack))
			return stack.getCount();

		return 0;
	}

	static long nexttime = 0;

	public static void sout(Object... objects) {
		if (System.currentTimeMillis() < nexttime)
			return;
		String str = "";
		for (Object object : objects) {
			str += object.toString() + ",";
		}
		ITextComponent text = net.minecraftforge.common.ForgeHooks.newChatWithLinks(str, true);
		Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, text);
		nexttime = System.currentTimeMillis() + 250;
	}
}
