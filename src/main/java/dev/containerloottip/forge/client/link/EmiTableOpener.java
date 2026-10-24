package dev.containerloottip.forge.client.link;

import dev.containerloottip.client.link.TableOpener;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.resources.ResourceLocation;

/**
 * EMI lookup for ALI pages.
 *
 * <p>ALI registers each loot table as a synthetic EMI recipe whose path is prefixed with {@code /};
 * the recipe can therefore be fetched by id without scanning categories.</p>
 */
public final class EmiTableOpener implements TableOpener {
    @Override
    public boolean open(ResourceLocation tableId) {
        ResourceLocation synthetic = new ResourceLocation(tableId.getNamespace(), "/" + tableId.getPath());
        EmiRecipe recipe = EmiApi.getRecipeManager().getRecipe(synthetic);

        if (recipe == null) {
            return false;
        }

        EmiApi.displayRecipe(recipe);
        return true;
    }
}
