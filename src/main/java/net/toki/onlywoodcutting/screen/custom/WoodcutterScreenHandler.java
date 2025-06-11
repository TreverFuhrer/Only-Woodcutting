package net.toki.onlywoodcutting.screen.custom;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.StonecutterScreenHandler;

public class WoodcutterScreenHandler extends StonecutterScreenHandler {

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }
    
}
