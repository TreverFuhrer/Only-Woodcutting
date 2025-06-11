package net.toki.onlywoodcutting.screen.custom;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.util.math.BlockPos;

public class WoodcutterScreenHandler extends StonecutterScreenHandler {

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }
    
    public WoodcutterScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, ScreenHandlerContext.create(inventory.player.getWorld(), pos));
    }
}
