package dev.containerloottip.neoforge.client.link;

import com.yanny.ali.compatibility.common.GameplayLootType;
import com.yanny.ali.compatibility.common.IType;
import com.yanny.ali.compatibility.common.TradeLootType;
import com.yanny.ali.jei.compatibility.jei.RecipeHolder;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Finds the ALI recipe registered for a loot table and opens its JEI page.
 *
 * <p>ALI does not expose a public lookup by table id, so every visible category is scanned. This only
 * runs on an explicit link click, never during rendering.</p>
 */
public final class AliJeiLookup {
    private AliJeiLookup() {
    }

    public static boolean open(IJeiRuntime runtime, ResourceLocation tableId) {
        IRecipeManager manager = runtime.getRecipeManager();

        for (IRecipeCategory<?> category : manager.createRecipeCategoryLookup().includeHidden().get().toList()) {
            List<?> recipes = manager.createRecipeLookup(category.getRecipeType()).includeHidden().get().toList();

            for (Object recipe : recipes) {
                if (recipe instanceof RecipeHolder<?> holder && matches(holder.type(), tableId)) {
                    show(runtime, category, recipe);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean matches(IType type, ResourceLocation tableId) {
        if (type instanceof GameplayLootType gameplay) {
            return gameplay.id().equals(tableId);
        }

        if (type instanceof TradeLootType trade) {
            return trade.id().equals(tableId);
        }

        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void show(IJeiRuntime runtime, IRecipeCategory<?> category, Object recipe) {
        runtime.getRecipesGui().showRecipes((IRecipeCategory) category, List.of(recipe), List.of());
    }
}
