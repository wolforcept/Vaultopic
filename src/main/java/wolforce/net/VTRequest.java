package wolforce.net;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.ItemVIEW;
import wolforce.Vaultopic;

public class VTRequest implements IMessage {

	public String filterText;

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeCharSequence(filterText, Charset.defaultCharset());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		filterText = (String) buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset());
	}

	public static class VTRequestHandler implements IMessageHandler<VTRequest, IMessage> {
		@Override
		public IMessage onMessage(VTRequest message, MessageContext ctx) {
			Vaultopic.onRequest(message, ctx);
			return null;
		}
	}

}
