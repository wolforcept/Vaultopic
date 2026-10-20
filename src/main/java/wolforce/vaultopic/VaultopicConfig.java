package wolforce.vaultopic;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//@Config(modid = Hwell.MODID)
@Mod.EventBusSubscriber(modid = Vaultopic.MODID)
@Config(modid = Vaultopic.MODID, category = "vaultopic_config")
public class VaultopicConfig {

	public static Meta meta = new Meta();

	public static class Meta {
		
		@Comment({ "VIEW time on screen (default: 20, min: 1, max: 60)" })
		@Config.RangeInt(min = 1, max = 60)
		public int viewtime = 20;
		
		@Comment({ "Small Vaultopic Range (default: 5, min: 1, max:25)" })
		@Config.RangeInt(min = 1, max = 25)
		public int range_small = 5;

		@Comment({ "Big Vaultopic Range (default: 10, min: 1, max: 25)" })
		@Config.RangeInt(min = 1, max = 25)
		public int range_big = 10;
	}

	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Vaultopic.MODID)) {
			ConfigManager.sync(Vaultopic.MODID, Config.Type.INSTANCE);
		}
	}
}
