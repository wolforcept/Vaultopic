package wolforce.vaultopic.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import wolforce.vaultopic.client.GuiVICE;

@JEIPlugin
public class JeiIntegration implements IModPlugin {

	public static IJeiHelpers jeiHelpers;
	public static ICraftingGridHelper craftingGridHelper;
	public static IRecipeRegistry recipeRegistry;

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeClickArea(GuiVICE.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		recipeTransferRegistry.addRecipeTransferHandler(new VaultopicTranferHandler(registry.getJeiHelpers()),
				VanillaRecipeCategoryUid.CRAFTING);
		// recipeTransferRegistry.addRecipeTransferHandler(ContainerVICE.class,
		// VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}
}