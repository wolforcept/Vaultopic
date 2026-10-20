package wolforce.vaultopic.registry;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wolforce.vaultopic.VaultMain;
import wolforce.vaultopic.Vaultopic;

@Mod.EventBusSubscriber(modid = Vaultopic.MODID)
public class RegisterItems {

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for (Item item : VaultMain.items) {
			event.getRegistry().register(item);
		}
	}

}
