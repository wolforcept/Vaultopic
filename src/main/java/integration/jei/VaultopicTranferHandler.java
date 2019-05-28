package integration.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import wolforce.ContainerVICE;

public class VaultopicTranferHandler implements IRecipeTransferHandler<ContainerVICE> {

	@Override
	public Class<ContainerVICE> getContainerClass() {
		return ContainerVICE.class;
	}

	@Override
	public IRecipeTransferError transferRecipe(ContainerVICE container, IRecipeLayout recipeLayout, EntityPlayer player,
			boolean maxTransfer, boolean doTransfer) {

		World world = player.getEntityWorld();

		System.out.println("VaultopicTranferHandler.transferRecipe()");

		return null;
	}

	interface TransferAction {

	}

}
