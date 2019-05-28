package wolforce;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class VaultopicCreativeTab extends CreativeTabs {

	public VaultopicCreativeTab() {
		super("vaultopic");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Main.view_small);
	}
}