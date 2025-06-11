package net.toki.onlywoodcutting.recipe;

import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.recipe.custom.WoodcuttingRecipe;

public class ModRecipes {

    public static final RecipeType<WoodcuttingRecipe> WOODCUTTING =
        new RecipeType<>() {
            public String toString() {
                return "woodcutting";
            }
        };

    public static void register() {
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutting"), WOODCUTTING);
        System.out.println("Woodcutting recipe type registered: " + WOODCUTTING);
    }
}
