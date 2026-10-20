package wolforce.vaultopic.net;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.vaultopic.Vaultopic;

public class VTOpenVice implements IMessage {

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	public static class VTOpenViceHandler implements IMessageHandler<VTOpenVice, IMessage> {

		@Override
		public IMessage onMessage(VTOpenVice message, MessageContext ctx) {
			Vaultopic.onOpenVice(message, ctx);
			return null; // No response packet
		}
	}
}
