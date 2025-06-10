package net.toki.onlywoodcutting.block.custom;

import static net.minecraft.block.StonecutterBlock.TITLE;

import net.minecraft.block.BlockState;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.toki.onlywoodcutting.screen.custom.WoodcutterScreenHandler;

public class WoodcutterBlock extends StonecutterBlock {

    private static final Text TITLE = Text.translatable("container.woodcutter");


    public WoodcutterBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
			(syncId, playerInventory, player) -> new WoodcutterScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE
		);
    }
}
