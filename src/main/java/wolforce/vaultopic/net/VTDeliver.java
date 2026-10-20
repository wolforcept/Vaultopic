package wolforce.vaultopic.net;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.vaultopic.VU;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.client.VIEWRenderer.InvPos;

public class VTDeliver implements IMessage {

	public InvPos inv;

	@Override
	public void toBytes(ByteBuf buf) {
		VU.writeInvPos(inv, buf);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inv = VU.readInvPos(buf);
	}

	public static class VTDeliverHandler implements IMessageHandler<VTDeliver, IMessage> {

		@Override
		public IMessage onMessage(VTDeliver message, MessageContext ctx) {
			Vaultopic.onDeliver(message, ctx);
			return null; // No response packet
		}
	}
}
