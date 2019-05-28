package wolforce;

import java.util.LinkedList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Main {

	public static LinkedList<Item> items;

	public static Item vault_searcher, view_small, view_big, vice;

	public static CreativeTabs creativeTab;

	//

	public static void preInit(FMLPreInitializationEvent event) {

		items = new LinkedList<>();

		vault_searcher = new Item();
		Util.setReg(vault_searcher, "vault_searcher");
		items.add(vault_searcher);

		view_small = new ItemVIEW("view_small", VaultopicConfig.meta.range_small);
		items.add(view_small);

		view_big = new ItemVIEW("view_big", VaultopicConfig.meta.range_big);
		items.add(view_big);

		vice = new ItemVICE("vice");
		items.add(vice);

		creativeTab = new VaultopicCreativeTab();
		for (Item item : items)
			item.setCreativeTab(creativeTab);

	}

}