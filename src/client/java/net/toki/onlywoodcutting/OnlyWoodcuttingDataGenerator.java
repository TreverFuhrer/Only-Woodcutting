package net.toki.onlywoodcutting;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.toki.onlywoodcutting.datagen.WoodcuttingRecipeProvider;

public class OnlyWoodcuttingDataGenerator implements DataGeneratorEntrypoint {
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(WoodcuttingRecipeProvider::new);
	}
}
