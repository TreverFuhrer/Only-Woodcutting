package net.toki.onlywoodcutting.screen;

import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.text.Text;

public class WoodcutterScreen extends StonecutterScreen {

    public WoodcutterScreen(StonecutterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

}
