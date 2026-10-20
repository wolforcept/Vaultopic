package wolforce.vaultopic.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import wolforce.vaultopic.VaultMain;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.net.VTOpenVice;

public class Keybinds {

	public static KeyBinding view;
	public static KeyBinding vice;

	public static void register() {

		String category = "key.vaultopic.category";
		view = new KeyBinding("key.vaultopic.view", Keyboard.KEY_G, category);
		vice = new KeyBinding("key.vaultopic.vice", Keyboard.KEY_V, category);

		ClientRegistry.registerKeyBinding(view);
		ClientRegistry.registerKeyBinding(vice);
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;

		if (Keybinds.view.isPressed() && canView(player))
			Vaultopic.proxy.openView(player, player.isSneaking());
		if (Keybinds.vice.isPressed() && canVice(player))
			Vaultopic.NET.sendToServer(new VTOpenVice());
	}

	private boolean canView(EntityPlayerSP player) {
		return player.inventory.hasItemStack(new ItemStack(VaultMain.view_small)) || //
				player.inventory.hasItemStack(new ItemStack(VaultMain.view_big)) || //
				player.inventory.hasItemStack(new ItemStack(VaultMain.vice));
	}

	private boolean canVice(EntityPlayerSP player) {
		return player.inventory.hasItemStack(new ItemStack(VaultMain.vice));
	}

}