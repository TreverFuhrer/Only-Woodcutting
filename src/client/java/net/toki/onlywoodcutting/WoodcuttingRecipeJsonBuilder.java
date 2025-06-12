package net.toki.onlywoodcutting;

import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.recipe.custom.WoodcuttingRecipe;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Datagen helper for wood-only stonecutter recipes.
 * No dependence on CuttingRecipe – we instantiate the record directly.
 */
public class WoodcuttingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {

    /* ---------- state ---------- */
    private final RecipeCategory category;
    private final Ingredient input;
    private final Item output;
    private final int count;

    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable private String group;

    /* ---------- ctor & helpers ---------- */
    private WoodcuttingRecipeJsonBuilder(RecipeCategory cat, Ingredient in, ItemConvertible out, int count) {
        this.category = cat;
        this.input    = in;
        this.output   = out.asItem();
        this.count    = count;
    }

    public static WoodcuttingRecipeJsonBuilder create(Ingredient in, RecipeCategory cat, ItemConvertible out, int count) {
        return new WoodcuttingRecipeJsonBuilder(cat, in, out, count);
    }

    public static WoodcuttingRecipeJsonBuilder create(Ingredient in, RecipeCategory cat, ItemConvertible out) {
        return create(in, cat, out, 1);
    }

    /* ---------- fluent setters ---------- */
    @Override
    public WoodcuttingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> crit) {
        criteria.put(name, crit);
        return this;
    }

    @Override
    public WoodcuttingRecipeJsonBuilder group(@Nullable String g) {
        group = g;
        return this;
    }

    /* ---------- CraftingRecipeJsonBuilder impl ---------- */
    @Override public Item getOutputItem() { return output; }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier id) {
        if (criteria.isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + id);

        // ---- advancement ----
        Advancement.Builder adv = exporter.getAdvancementBuilder()
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        criteria.forEach(adv::criterion);

        // ---- the actual recipe record ----
        var recipe = new WoodcuttingRecipe(input, new ItemStack(output, count));

        exporter.accept(id, recipe, adv.build(id.withPrefixedPath("recipe/" + category.getName() + "/")));  // 1.21 folder name
    }
}
