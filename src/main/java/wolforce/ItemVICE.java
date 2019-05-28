package wolforce;

import java.util.List;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
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
		Util.setReg(this, name);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Right click to search nearby chests.");
		tooltip.add("Sneak to repeat last search.");
		tooltip.add("Range: " + range);
		// tooltip.add(stack.getTagCompound() + "");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		// player.openGui(Vaultopic.instance, Vaultopic.VICE, world, 0, 0, 0);
		if (player.getEntityWorld().isRemote)
			player.displayGui(new BlockWorkbench.InterfaceCraftingTable(world, player.getPosition()));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

}
