package wolforce.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import wolforce.Main;
import wolforce.Vaultopic;

@Mod.EventBusSubscriber(modid = Vaultopic.MODID, value = Side.CLIENT)
public class RegisterModels {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (Item item : Main.items) {
			ModelLoader.setCustomModelResourceLocation(item, 0,
					new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}

}
