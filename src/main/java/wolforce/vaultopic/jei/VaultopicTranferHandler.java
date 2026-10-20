package wolforce.vaultopic.jei;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;
import mezz.jei.transfer.RecipeTransferErrorTooltip;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import wolforce.vaultopic.ContainerVICE;
import wolforce.vaultopic.VU;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.net.VTRecipe;

public class VaultopicTranferHandler implements IRecipeTransferHandler<ContainerVICE> {

	// private final IJeiHelpers jeiHelpers;

	public VaultopicTranferHandler(IJeiHelpers jeiHelpers) {
		// this.jeiHelpers = jeiHelpers;
	}

	@Override
	public Class<ContainerVICE> getContainerClass() {
		return ContainerVICE.class;
	}

	@Override
	public IRecipeTransferError transferRecipe(ContainerVICE container, IRecipeLayout recipeLayout, EntityPlayer player,
			boolean maxTransfer, boolean doTransfer) {

		if (!(recipeLayout.getRecipeCategory() instanceof CraftingRecipeCategory)) {
			return new RecipeTransferErrorTooltip("Recipe not available.");
		}

		if (doTransfer) {
			ItemStack[][] requested = new ItemStack[9][];
			Map<Integer, ? extends IGuiIngredient<ItemStack>> map = recipeLayout.getItemStacks().getGuiIngredients();
			for (Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : map.entrySet()) {
				if (entry.getKey() < 1 || entry.getKey() > 9)
					continue;

				List<ItemStack> ingredient = entry.getValue().getAllIngredients();
				ItemStack[] stacks = null;
				if (ingredient.size() != 0) {
					stacks = new ItemStack[ingredient.size()];
					for (int i = 0; i < stacks.length; i++) {
						stacks[i] = ingredient.get(i).copy();
					}
				}
				if (VU.isValid(entry.getValue().getDisplayedIngredient()))
					requested[entry.getKey() - 1] = stacks;
			}

			// System.out.println(requested);

			VTRecipe vtrecipe = new VTRecipe();
			vtrecipe.maxTransfer = maxTransfer;
			vtrecipe.items = requested;
			Vaultopic.NET.sendToServer(vtrecipe);
			Vaultopic.recipeFill(true, container, requested, player, maxTransfer);
		}

		return null;
	}
}
