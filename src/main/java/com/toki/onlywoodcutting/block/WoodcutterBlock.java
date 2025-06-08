package com.toki.onlywoodcutting.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.toki.onlywoodcutting.screen.WoodcutterScreenHandler;

public class WoodcutterBlock extends StonecutterBlock {
    private static final Text TITLE = Text.translatable("container.woodcutter");

    public WoodcutterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, player) -> new WoodcutterScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)),
                TITLE);
    }
}
