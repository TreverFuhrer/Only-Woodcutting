package net.toki.onlywoodcutting.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.recipe.custom.WoodcuttingRecipeJsonBuilder;

public class WoodcuttingRecipeProvider extends FabricRecipeProvider {

    public WoodcuttingRecipeProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // Logs → Planks
        for (var entry : Registries.ITEM.iterateEntries(ItemTags.LOGS)) {
            Item log = entry.value();
            Identifier id = Registries.ITEM.getId(log);
            String namespace = id.getNamespace();
            String path = id.getPath();

            String woodType = path.replace("_log", "").replace("stripped_", "");
            Identifier planksId = Identifier.of(namespace, woodType + "_planks");

            if (Registries.ITEM.containsId(planksId)) {
                Item planks = Registries.ITEM.get(planksId);
                offerWoodcuttingRecipe(exporter, log, planks, 4);
            }
        }

        // Planks → Variants
        for (var entry : Registries.ITEM.iterateEntries(ItemTags.PLANKS)) {
            Item plank = entry.value();
            Identifier id = Registries.ITEM.getId(plank);
            String wood = id.getPath().replace("_planks", "");

            tryWoodcutting(exporter, plank, wood + "_stairs", 1);
            tryWoodcutting(exporter, plank, wood + "_slab", 2);
            tryWoodcutting(exporter, plank, wood + "_fence", 1);
            tryWoodcutting(exporter, plank, wood + "_door", 1);
            tryWoodcutting(exporter, plank, wood + "_trapdoor", 1);
            tryWoodcutting(exporter, plank, wood + "_button", 1);
            tryWoodcutting(exporter, plank, wood + "_pressure_plate", 1);
            tryWoodcutting(exporter, plank, wood + "_sign", 1);
            tryWoodcutting(exporter, plank, wood + "_hanging_sign", 1);
        }
    }

    private void tryWoodcutting(RecipeExporter exporter, Item input, String outputPath, int count) {
        Identifier outputId = Identifier.of(Registries.ITEM.getId(input).getNamespace(), outputPath);
        if (Registries.ITEM.containsId(outputId)) {
            Item output = Registries.ITEM.get(outputId);
            offerWoodcuttingRecipe(exporter, input, output, count);
        }
    }

    private void offerWoodcuttingRecipe(RecipeExporter exporter, Item input, Item output, int count) {
        String inputName = Registries.ITEM.getId(input).getPath();
        String outputName = Registries.ITEM.getId(output).getPath();

        new WoodcuttingRecipeJsonBuilder(
                RecipeCategory.BUILDING_BLOCKS,
                Ingredient.ofItems(input),
                output,
                count
        ).criterion("has_input", InventoryChangedCriterion.Conditions.items(input))
         .offerTo(exporter, Identifier.of(OnlyWoodcutting.MOD_ID, inputName + "_to_" + outputName));
    }
}