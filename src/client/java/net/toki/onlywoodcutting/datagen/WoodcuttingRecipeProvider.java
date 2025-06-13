package net.toki.onlywoodcutting.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.WoodcuttingRecipeJsonBuilder;

public class WoodcuttingRecipeProvider extends FabricRecipeProvider {

    public WoodcuttingRecipeProvider(FabricDataOutput output, CompletableFuture<net.minecraft.registry.RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        Registries.ITEM.forEach(item -> {
            Identifier id = Registries.ITEM.getId(item);
            String path = id.getPath();

            /* ================= Bamboo item directly to stick ================= */
            if (path.equals("bamboo")) {
                offerWoodcuttingRecipe(exporter, item, Items.STICK, 1);
            }

            /* ===== LOG‑LIKE MATERIALS ===== */
            boolean isLog = path.endsWith("_log");
            boolean isStem = path.endsWith("_stem");
            boolean isStripped = path.startsWith("stripped_");
            boolean isHyphae = path.endsWith("_hyphae");
            boolean isWood = path.endsWith("_wood") || isHyphae;

            /* -- Regular log / stem  → Stripped variant -- */
            if ((isLog || isStem) && !isStripped) {
                String core = path.replace("_log", "").replace("_stem", "");
                tryWoodcutting(exporter, item, "stripped_" + core + (isStem ? "_stem" : "_log"), 1);
            }

            /* -- Stripped log / stem → Stripped wood / hyphae -- */
            if (isStripped && (isLog || isStem)) {
                String core = path.replace("stripped_", "").replace("_log", "").replace("_stem", "");
                String suffix = isStem ? "_hyphae" : "_wood";
                tryWoodcutting(exporter, item, "stripped_" + core + suffix, 1);
            }

            /* -- Bark blocks (wood / hyphae) conversions -- */
            if (isWood) {
                String core = path.replace("stripped_", "").replace("_wood", "").replace("_hyphae", "");

                // To planks (×4)
                tryWoodcutting(exporter, item, core + "_planks", 4);

                // If unstripped wood → stripped wood / log
                if (!isStripped) {
                    tryWoodcutting(exporter, item, "stripped_" + core + (isHyphae ? "_hyphae" : "_wood"), 1);
                    tryWoodcutting(exporter, item, "stripped_" + core + (isHyphae ? "_stem" : "_log"), 1);
                }
            }

            /* -- Stripped wood / hyphae back to stripped log/stem or planks -- */
            if (isStripped && isWood) {
                String core = path.replace("stripped_", "").replace("_wood", "").replace("_hyphae", "");
                tryWoodcutting(exporter, item, "stripped_" + core + (isHyphae ? "_stem" : "_log"), 1);
            }

            /* -- Log / Stem → Planks (×4) -- */
            if (isLog || isStem) {
                String core = path.replace("stripped_", "").replace("_log", "").replace("_stem", "");
                tryWoodcutting(exporter, item, core + "_planks", 4);
            }

            /* ===== PLANK FAMILY ===== */
            if (path.endsWith("_planks")) {
                String wood = path.replace("_planks", "");

                /* Standard variants */
                offerWoodcuttingRecipe(exporter, item, Items.STICK, 4);
                tryWoodcutting(exporter, item, wood + "_stairs",         1);
                tryWoodcutting(exporter, item, wood + "_slab",           2);
                tryWoodcutting(exporter, item, wood + "_fence",          1);
                tryWoodcutting(exporter, item, wood + "_door",           1);
                tryWoodcutting(exporter, item, wood + "_trapdoor",       1);
                tryWoodcutting(exporter, item, wood + "_button",         2);
                tryWoodcutting(exporter, item, wood + "_pressure_plate", 2);
                tryWoodcutting(exporter, item, wood + "_sign",           1);
                tryWoodcutting(exporter, item, wood + "_fence_gate",     1);
                tryWoodcutting(exporter, item, "ladder", 1);

                // Decor mods that add <wood>_wall)
                tryWoodcutting(exporter, item, wood + "_wall", 1);

                // Boat / raft handling (bamboo special)
                String boatPath = wood.equals("bamboo") ? "bamboo_raft" : wood + "_boat";
                tryWoodcutting(exporter, item, boatPath, 1);
            }

            /* ===== BAMBOO‑SPECIFIC BLOCKS ===== */
            // Bamboo block to planks & mosaic
            if (path.equals("bamboo_block")) {
                tryWoodcutting(exporter, item, "bamboo_planks", 2);
                tryWoodcutting(exporter, item, "bamboo_mosaic", 1);
                tryWoodcutting(exporter, item, "stripped_bamboo_block", 1);
            }

            // 2 planks from stripped block
            if (path.equals("stripped_bamboo_block")) {
                tryWoodcutting(exporter, item, "bamboo_planks", 2);
            }

            // Mosaic → stair or slab
            if (path.equals("bamboo_mosaic")) {
                tryWoodcutting(exporter, item, "bamboo_mosaic_stairs", 1);
                tryWoodcutting(exporter, item, "bamboo_mosaic_slab",   2);
            }

            /* ===== SPECIAL CONVERSIONS ===== */
            // Fence → gate
            if (path.endsWith("_fence") && !path.endsWith("_fence_gate")) {
                String core = path.replace("_fence", "");
                tryWoodcutting(exporter, item, core + "_fence_gate", 1);
            }

            // Door → trapdoor
            if (path.endsWith("_door") && !path.endsWith("_trapdoor")) {
                String core = path.replace("_door", "");
                tryWoodcutting(exporter, item, core + "_trapdoor", 1);
            }

            // Pressure‑plate → button
            if (path.endsWith("_pressure_plate")) {
                String core = path.replace("_pressure_plate", "");
                tryWoodcutting(exporter, item, core + "_button", 1);
            }

            // Slab → pressure‑plate
            if (path.endsWith("_slab")) {
                String core = path.replace("_slab", "");
                tryWoodcutting(exporter, item, core + "_pressure_plate", 1);
            }
        });
    }


    /* -----------------------Helper methods----------------------------- */

    private void tryWoodcutting(RecipeExporter exporter, Item input, String outputPath, int count) {
        Identifier outputId = Identifier.of(Registries.ITEM.getId(input).getNamespace(), outputPath);
        if (Registries.ITEM.containsId(outputId)) {
            Item output = Registries.ITEM.get(outputId);
            offerWoodcuttingRecipe(exporter, input, output, count);
        }
    }

    private void offerWoodcuttingRecipe(RecipeExporter exporter, Item input, Item output, int count) {
        String inputName  = Registries.ITEM.getId(input).getPath();
        String outputName = Registries.ITEM.getId(output).getPath();

        WoodcuttingRecipeJsonBuilder
                .create(Ingredient.ofItems(input), RecipeCategory.BUILDING_BLOCKS, output, count)
                .criterion("has_input", InventoryChangedCriterion.Conditions.items(input))
                .offerTo(exporter, Identifier.of(OnlyWoodcutting.MOD_ID, inputName + "_to_" + outputName));
    }
}
