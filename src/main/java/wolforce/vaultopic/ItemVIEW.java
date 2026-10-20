package wolforce.vaultopic;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemVIEW extends Item {

	public final int range;

	public ItemVIEW(String name, int range) {
		VU.setReg(this, name);
		this.range = range;
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Right click to search nearby chests.");
		tooltip.add("Sneak to repeat last search.");
		tooltip.add("May be used from inventory with a keybind.");
		// if (world != null && world.isRemote) {
		// int keyIndex =
		// Arrays.binarySearch(Minecraft.getMinecraft().gameSettings.keyBindings,
		// Keybinds.vice);
		// KeyBinding key = Minecraft.getMinecraft().gameSettings.keyBindings[keyIndex];
		// tooltip.add("You can also press " + key.getKeyDescription() + " to open if
		// this is in your inventory.");
		// }
		tooltip.add("Range: " + range);
		// tooltip.add(stack.getTagCompound() + "");
		super.addInformation(stack, world, tooltip, flagIn);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			Vaultopic.proxy.openView(player, player.isSneaking());
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

}
