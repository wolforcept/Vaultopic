package wolforce.vaultopic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class VaultopicCreativeTab extends CreativeTabs {

	public VaultopicCreativeTab() {
		super("vaultopic");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(VaultMain.view_small);
	}
}