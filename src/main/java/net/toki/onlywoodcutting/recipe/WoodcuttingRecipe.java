package net.toki.onlywoodcutting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public record WoodcuttingRecipe(Ingredient inputItem, ItemStack output)
        implements Recipe<WoodcuttingRecipeInput> {

    @Override
    public boolean matches(WoodcuttingRecipeInput input, World world) {
        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(WoodcuttingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<WoodcuttingRecipeInput>> getSerializer() {
        return ModRecipes.WOODCUTTING_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<WoodcuttingRecipeInput>> getType() {
        return ModRecipes.WOODCUTTING_TYPE;
    }


    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forSingleSlot(inputItem);
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<WoodcuttingRecipe> {
        public static final MapCodec<WoodcuttingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(WoodcuttingRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(WoodcuttingRecipe::output)
        ).apply(inst, WoodcuttingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, WoodcuttingRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, WoodcuttingRecipe::inputItem,
                        ItemStack.PACKET_CODEC, WoodcuttingRecipe::output,
                        WoodcuttingRecipe::new);

        @Override
        public MapCodec<WoodcuttingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, WoodcuttingRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}