package net.toki.onlywoodcutting;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.toki.onlywoodcutting.screen.ModScreenHandlers;
import net.toki.onlywoodcutting.screen.WoodcutterScreen;

public class OnlyWoodcuttingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModScreenHandlers.registerScreenHandlers();
		HandledScreens.register(ModScreenHandlers.WOODCUTTER_SCREEN_HANDLER, WoodcutterScreen::new);
	}
}