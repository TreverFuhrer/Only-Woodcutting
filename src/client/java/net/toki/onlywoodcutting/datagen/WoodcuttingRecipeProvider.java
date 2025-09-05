package net.toki.onlywoodcutting.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.WoodcuttingRecipeJsonBuilder;

public class WoodcuttingRecipeProvider extends FabricRecipeProvider {

    public WoodcuttingRecipeProvider(
            FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup lookup, RecipeExporter exporter) {
        return new RecipeGenerator(lookup, exporter) {
            @Override
            public void generate() {
                Registries.ITEM.forEach(item -> {
                    Identifier id = Registries.ITEM.getId(item);
                    String path = id.getPath();

                    // Bamboo → stick
                    if (path.equals("bamboo")) {
                        offerWoodcuttingRecipe(exporter, item, Items.STICK, 1);
                    }

                    // Log-like materials
                    boolean isLog = path.endsWith("_log");
                    boolean isStem = path.endsWith("_stem");
                    boolean isStripped = path.startsWith("stripped_");
                    boolean isHyphae = path.endsWith("_hyphae");
                    boolean isWood = path.endsWith("_wood") || isHyphae;

                    // Regular log/stem → stripped
                    if ((isLog || isStem) && !isStripped) {
                        String core = path.replace("_log", "").replace("_stem", "");
                        tryWoodcutting(exporter, item, "stripped_" + core + (isStem ? "_stem" : "_log"), 1);
                    }

                    // Stripped log/stem → stripped wood/hyphae
                    if (isStripped && (isLog || isStem)) {
                        String core = path.replace("stripped_", "").replace("_log", "").replace("_stem", "");
                        String suffix = isStem ? "_hyphae" : "_wood";
                        tryWoodcutting(exporter, item, "stripped_" + core + suffix, 1);
                    }

                    // Wood/hyphae conversions
                    if (isWood) {
                        String core = path.replace("stripped_", "").replace("_wood", "").replace("_hyphae", "");
                        tryWoodcutting(exporter, item, core + "_planks", 4);

                        if (!isStripped) {
                            tryWoodcutting(exporter, item, "stripped_" + core + (isHyphae ? "_hyphae" : "_wood"), 1);
                            tryWoodcutting(exporter, item, "stripped_" + core + (isHyphae ? "_stem" : "_log"), 1);
                        }
                    }

                    // Stripped wood/hyphae → stripped log/stem
                    if (isStripped && isWood) {
                        String core = path.replace("stripped_", "").replace("_wood", "").replace("_hyphae", "");
                        tryWoodcutting(exporter, item, "stripped_" + core + (isHyphae ? "_stem" : "_log"), 1);
                    }

                    // Log/stem → planks
                    if (isLog || isStem) {
                        String core = path.replace("stripped_", "").replace("_log", "").replace("_stem", "");
                        tryWoodcutting(exporter, item, core + "_planks", 4);
                    }

                    // Planks family
                    if (path.endsWith("_planks")) {
                        String wood = path.replace("_planks", "");

                        offerWoodcuttingRecipe(exporter, item, Items.STICK, 4);
                        tryWoodcutting(exporter, item, wood + "_stairs", 1);
                        tryWoodcutting(exporter, item, wood + "_slab", 2);
                        tryWoodcutting(exporter, item, wood + "_fence", 1);
                        tryWoodcutting(exporter, item, wood + "_door", 1);
                        tryWoodcutting(exporter, item, wood + "_trapdoor", 1);
                        tryWoodcutting(exporter, item, wood + "_button", 2);
                        tryWoodcutting(exporter, item, wood + "_pressure_plate", 2);
                        tryWoodcutting(exporter, item, wood + "_sign", 1);
                        tryWoodcutting(exporter, item, wood + "_fence_gate", 1);
                        tryWoodcutting(exporter, item, "ladder", 1);
                        tryWoodcutting(exporter, item, wood + "_wall", 1);

                        String boatPath = wood.equals("bamboo") ? "bamboo_raft" : wood + "_boat";
                        tryWoodcutting(exporter, item, boatPath, 1);
                    }

                    // Bamboo specifics
                    if (path.equals("bamboo_block")) {
                        tryWoodcutting(exporter, item, "bamboo_planks", 2);
                        tryWoodcutting(exporter, item, "bamboo_mosaic", 1);
                        tryWoodcutting(exporter, item, "stripped_bamboo_block", 1);
                    }
                    if (path.equals("stripped_bamboo_block")) {
                        tryWoodcutting(exporter, item, "bamboo_planks", 2);
                    }
                    if (path.equals("bamboo_mosaic")) {
                        tryWoodcutting(exporter, item, "bamboo_mosaic_stairs", 1);
                        tryWoodcutting(exporter, item, "bamboo_mosaic_slab", 2);
                    }

                    // Special conversions
                    if (path.endsWith("_fence") && !path.endsWith("_fence_gate")) {
                        String core = path.replace("_fence", "");
                        tryWoodcutting(exporter, item, core + "_fence_gate", 1);
                    }
                    if (path.endsWith("_door") && !path.endsWith("_trapdoor")) {
                        String core = path.replace("_door", "");
                        tryWoodcutting(exporter, item, core + "_trapdoor", 1);
                    }
                    if (path.endsWith("_pressure_plate")) {
                        String core = path.replace("_pressure_plate", "");
                        tryWoodcutting(exporter, item, core + "_button", 1);
                    }
                    if (path.endsWith("_slab")) {
                        String core = path.replace("_slab", "");
                        tryWoodcutting(exporter, item, core + "_pressure_plate", 1);
                    }
                });
            }
        };
    }

    @Override
    public String getName() {
        return "OnlyWoodcutting Recipes";
    }

    /* ----------------------- Helper methods ----------------------------- */

    private static void tryWoodcutting(RecipeExporter exporter, Item input, String outputPath, int count) {
        Identifier outputId = Identifier.of(Registries.ITEM.getId(input).getNamespace(), outputPath);
        if (Registries.ITEM.containsId(outputId)) {
            Item output = Registries.ITEM.get(outputId);
            offerWoodcuttingRecipe(exporter, input, output, count);
        }
    }

    private static void offerWoodcuttingRecipe(RecipeExporter exporter, Item input, Item output, int count) {
        String inputName  = Registries.ITEM.getId(input).getPath();
        String outputName = Registries.ITEM.getId(output).getPath();

        RegistryKey<Recipe<?>> key = RegistryKey.of(
                RegistryKeys.RECIPE,
                Identifier.of(OnlyWoodcutting.MOD_ID, inputName + "_to_" + outputName)
        );

        WoodcuttingRecipeJsonBuilder
                .create(Ingredient.ofItems(input), RecipeCategory.BUILDING_BLOCKS, output, count)
                .criterion("has_input", InventoryChangedCriterion.Conditions.items(input))
                .offerTo(exporter, key);
    }
}