package net.toki.onlywoodcutting;

import net.fabricmc.api.ModInitializer;
import net.toki.onlywoodcutting.block.ModBlocks;
import net.toki.onlywoodcutting.recipe.ModRecipeSerializers;
import net.toki.onlywoodcutting.recipe.ModRecipes;
import net.toki.onlywoodcutting.screen.ModScreenHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlyWoodcutting implements ModInitializer {

	public static final String MOD_ID = "onlywoodcutting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModScreenHandlers.register();
		ModRecipes.register();
		ModRecipeSerializers.register();
		ModStats.register();
	}
}