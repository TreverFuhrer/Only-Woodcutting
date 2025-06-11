package net.toki.onlywoodcutting.screen.custom;

import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;

import net.toki.onlywoodcutting.block.ModBlocks;
import net.toki.onlywoodcutting.screen.ModScreensHandlers;

public class WoodcutterScreenHandler extends StonecutterScreenHandler {
    // 1) shadow the private context so we can use it in canUse(...)
    private final ScreenHandlerContext context;

    // 2) server-side ctor: call the only available super(syncId, inv, ctx)
    public WoodcutterScreenHandler(int syncId,
                                   PlayerInventory inv,
                                   ScreenHandlerContext ctx) {
        super(syncId, inv, ctx);
        this.context = ctx;
    }

    // 3) client-side ctor: call the only available super(syncId, inv)
    public WoodcutterScreenHandler(int syncId, PlayerInventory inv) {
        super(syncId, inv);
        this.context = ScreenHandlerContext.EMPTY;
    }

    // 4) override getType() so Fabric sends your custom type, not STONECUTTER 
    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreensHandlers.WOODCUTTER_SCREEN_HANDLER;
    }

    // 5) override canUse() so “isWithinUsableDistance” checks YOUR block
    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.WOOD_CUTTER_BLOCK);
    }

    // —— no other overrides (yet) — this will open the vanilla stonecutter UI
    // and keep it open. Once that works you can go back and re-implement
    // onContentChanged(...) / populateResult(...) to swap in your WOODCUTTING recipes.
}