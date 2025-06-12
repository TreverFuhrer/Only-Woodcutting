package net.toki.onlywoodcutting.recipe.custom;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class WoodcuttingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {

	private final RecipeCategory category;
	private final Item output;
	private final Ingredient input;
	private final int count;
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	private String group;

	public WoodcuttingRecipeJsonBuilder(RecipeCategory category, Ingredient input, ItemConvertible output, int count) {
		this.category = category;
		this.output = output.asItem();
		this.input = input;
		this.count = count;
	}

	public static WoodcuttingRecipeJsonBuilder create(Ingredient input, RecipeCategory category, ItemConvertible output) {
		return new WoodcuttingRecipeJsonBuilder(category, input, output, 1);
	}

	public static WoodcuttingRecipeJsonBuilder create(Ingredient input, RecipeCategory category, ItemConvertible output, int count) {
		return new WoodcuttingRecipeJsonBuilder(category, input, output, count);
	}

	public WoodcuttingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	public WoodcuttingRecipeJsonBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

	@Override
	public Item getOutputItem() {
		return this.output;
	}

	@Override
	public void offerTo(RecipeExporter exporter, Identifier recipeId) {
		validate(recipeId);

		Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

		this.criteria.forEach(advancementBuilder::criterion);

		WoodcuttingRecipe recipe = new WoodcuttingRecipe(input, new ItemStack(output, count));

		exporter.accept(recipeId, recipe, advancementBuilder.build(recipeId.withPrefixedPath("recipes/" + category.getName() + "/")));
	}


	private void validate(Identifier id) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
}