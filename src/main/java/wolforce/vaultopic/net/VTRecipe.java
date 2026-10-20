package wolforce.vaultopic.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.vaultopic.Vaultopic;

public class VTRecipe implements IMessage {

	public ItemStack[][] items;
	public boolean maxTransfer;

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeBoolean(maxTransfer);

		for (ItemStack[] stacks : items) {
			if (stacks == null) {
				buf.writeInt(0);
			} else {
				buf.writeInt(stacks.length);
				for (ItemStack s : stacks) {
					ByteBufUtils.writeItemStack(buf, s);
				}
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		maxTransfer = buf.readBoolean();

		items = new ItemStack[9][];
		int i = 0;
		while (buf.readableBytes() > 0) {
			int length = buf.readInt();
			if (length == 0) {
				items[i] = null;
			} else {
				items[i] = new ItemStack[length];
				for (int j = 0; j < length; j++)
					items[i][j] = ByteBufUtils.readItemStack(buf);
			}
			i++;
		}
	}

	public static class VTRecipeHandler implements IMessageHandler<VTRecipe, IMessage> {

		@Override
		public IMessage onMessage(VTRecipe message, MessageContext ctx) {
			Vaultopic.onRecipe(message, ctx);
			return null; // No response packet
		}
	}
}
