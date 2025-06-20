package net.toki.onlywoodcutting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.toki.onlywoodcutting.block.ModBlocks;
import net.toki.onlywoodcutting.screen.ModScreenHandlers;
import net.toki.onlywoodcutting.screen.WoodcutterScreen;

public class OnlyWoodcuttingClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.WOODCUTTER_SCREEN_HANDLER, WoodcutterScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODCUTTER, RenderLayer.getCutout());
	}
}