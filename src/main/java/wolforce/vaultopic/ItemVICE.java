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

public class ItemVICE extends Item {

	public final int range = VaultopicConfig.meta.range_big;

	public ItemVICE(String name) {
		VU.setReg(this, name);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Right click to open crafting grid.");
		tooltip.add("Use JEI to pull items from nearby inventories.");
		tooltip.add("Retains VIEW functionality.");
		tooltip.add("May be used from inventory with a keybind.");
		// tooltip.add(stack.getTagCompound() + "");
		// if (world != null && world.isRemote) {
		// int keyIndex =
		// Arrays.binarySearch(Minecraft.getMinecraft().gameSettings.keyBindings,
		// Keybinds.vice);
		// KeyBinding key = Minecraft.getMinecraft().gameSettings.keyBindings[keyIndex];
		// tooltip.add("You can also press " + key.getKeyDescription() + " to open if
		// this is in your inventory.");
		// }
		tooltip.add("Range: " + range);
		super.addInformation(stack, world, tooltip, flagIn);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote)
			player.openGui(Vaultopic.instance, Vaultopic.VICE, player.world, 0, 0, 0);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

}
