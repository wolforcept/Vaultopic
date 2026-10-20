package wolforce.vaultopic.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.vaultopic.VU;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.client.VIEWRenderer.InvPos;

public class VTGrab implements IMessage {

	public InvPos invPos;
	public int slot;
	public boolean grabAll;

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
		buf.writeBoolean(grabAll);
		VU.writeInvPos(invPos, buf);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		grabAll = buf.readBoolean();
		invPos = VU.readInvPos(buf);
	}

	public static class VTGrabHandler implements IMessageHandler<VTGrab, IMessage> {
		@Override
		public IMessage onMessage(VTGrab message, MessageContext ctx) {
			Vaultopic.onGrab(message, ctx);
			return null;
		}
	}

	public BlockPos getPos() {
		return invPos.pos;
	}

	public EnumFacing getEnumFacing() {
		return invPos.facing;
	}

	public int getSlot() {
		return slot;
	}

}
