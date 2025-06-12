package net.toki.onlywoodcutting.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.toki.onlywoodcutting.recipe.ModRecipeSerializers;
import net.toki.onlywoodcutting.recipe.ModRecipes;

public record WoodcuttingRecipe(Ingredient ingredient, ItemStack result) implements Recipe<WoodcuttingRecipeInput> {
    
    @Override
    public boolean matches(WoodcuttingRecipeInput input, World world) {
        return ingredient.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(WoodcuttingRecipeInput inv, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.copyOf(Ingredient.EMPTY, ingredient);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.WOODCUTTING;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WOODCUTTING;
    }

    public static class Serializer implements RecipeSerializer<WoodcuttingRecipe> {
        public static final MapCodec<WoodcuttingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(WoodcuttingRecipe::ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(WoodcuttingRecipe::result)
        ).apply(inst, WoodcuttingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, WoodcuttingRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, WoodcuttingRecipe::ingredient,
                        ItemStack.PACKET_CODEC, WoodcuttingRecipe::result,
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
