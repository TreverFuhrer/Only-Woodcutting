package net.toki.onlywoodcutting.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.recipe.custom.WoodcuttingRecipe;

public class ModRecipeSerializers {
    public static final RecipeSerializer<WoodcuttingRecipe> WOODCUTTING =
        new WoodcuttingRecipe.Serializer();

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutting"), WOODCUTTING);
        System.out.println("Woodcutting serializer registered: " + WOODCUTTING);
    }
}
