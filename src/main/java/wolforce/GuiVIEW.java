package wolforce;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.client.Request;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wolforce.net.VTRequest;
import wolforce.net.VaultopicClientNet;

@SideOnly(Side.CLIENT)
public class GuiVIEW extends GuiScreen {

	static String text = "";
	private GuiTextField textfield;

	public GuiVIEW(boolean sneaking) {
		if (!sneaking)
			text = "";
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		// addButton(buttonIn)
		textfield = new GuiTextField(0, fontRenderer, width / 2 - 75, height - 20 - 80, 150, 20);
		textfield.setCanLoseFocus(false);
		textfield.setFocused(true);
		textfield.setText(text);
		// textfield.setVisible(true);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void keyTyped(char c, int i) {

		if (!textfield.isFocused() || i == Keyboard.KEY_ESCAPE) {
			try {
				super.keyTyped(c, i);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (textfield.isFocused() && i == Keyboard.KEY_RETURN) {
			text = textfield.getText();
			Minecraft.getMinecraft().addScheduledTask(() -> {
				VTRequest request = new VTRequest();
				request.filterText = text;
				Vaultopic.NET.sendToServer(request);
			});

			// send escape to exit
			try {
				super.keyTyped('e', Keyboard.KEY_ESCAPE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (Character.isLetter(c) || Character.isDigit(c) || i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE
				|| i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || c == '@') {
			textfield.textboxKeyTyped(c, i);
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		// drawCenteredString(fontRenderer, text, width / 2, height / 2, 0);
		textfield.drawTextBox();
	}

}
