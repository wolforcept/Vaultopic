package wolforce.net;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wolforce.Vaultopic;
import wolforce.VaultopicEvents;
import wolforce.VaultopicEvents.InvPos;

public class VTDeliver implements IMessage {

	public InvPos inv;

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(inv.pos.getX());
		buf.writeInt(inv.pos.getY());
		buf.writeInt(inv.pos.getZ());
		for (ItemStack stack : inv.item) {
			ByteBufUtils.writeItemStack(buf, stack);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();

		ArrayList<ItemStack> stacks = new ArrayList<>();
		while (buf.readableBytes() > 0) {
			ItemStack stack = ByteBufUtils.readItemStack(buf);
			stacks.add(stack);
		}
		BlockPos pos = new BlockPos(x, y, z);
		ItemStack[] stackarray = new ItemStack[stacks.size()];
		for (int i = 0; i < stackarray.length; i++) {
			stackarray[i] = stacks.get(i);
		}
		inv = new InvPos(pos, stackarray);
	}

	public static class VTDeliverHandler implements IMessageHandler<VTDeliver, IMessage> {

		@Override
		public IMessage onMessage(VTDeliver message, MessageContext ctx) {
			Vaultopic.onDeliver(message, ctx);
			return null; // No response packet
		}
	}
}
