package net.toki.onlywoodcutting;

import net.fabricmc.api.ModInitializer;
import net.toki.onlywoodcutting.block.ModBlocks;
//import net.toki.onlywoodcutting.item.ModItems;
import net.toki.onlywoodcutting.recipe.ModRecipes;
import net.toki.onlywoodcutting.screen.ModScreensHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlyWoodcutting implements ModInitializer {
	public static final String MOD_ID = "onlywoodcutting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		//ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModRecipes.register();
		ModScreensHandlers.registerHandlers();
	}
}