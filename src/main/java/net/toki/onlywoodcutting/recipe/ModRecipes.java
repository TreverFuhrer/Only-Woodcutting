package net.toki.onlywoodcutting.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;

public class ModRecipes {
    public static final RecipeSerializer<WoodcuttingRecipe> WOODCUTTING_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutting"),
            new WoodcuttingRecipe.Serializer()
    );

    public static final RecipeType<WoodcuttingRecipe> WOODCUTTING_TYPE = Registry.register(
            Registries.RECIPE_TYPE,
            Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutting"),
            new RecipeType<>() {
                @Override
                public String toString() {
                    return "woodcutting";
                }
            }
    );

    public static void register() {
        OnlyWoodcutting.LOGGER.info("Registering Woodcutting Recipe.");
    }
}