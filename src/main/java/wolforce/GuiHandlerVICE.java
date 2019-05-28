package wolforce;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerVICE implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Vaultopic.VICE && //
				(player.getHeldItemMainhand().getItem() == Main.vice
						|| player.getHeldItemOffhand().getItem() == Main.vice))
			return new GuiVICE(player.inventory, world, new BlockPos(x, y, z));
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Vaultopic.VICE && //
				(player.getHeldItemMainhand().getItem() == Main.vice
						|| player.getHeldItemOffhand().getItem() == Main.vice))
			return new GuiVICE(player.inventory, world, new BlockPos(x, y, z));
		return null;
	}
}