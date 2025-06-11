package net.toki.onlywoodcutting.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.toki.onlywoodcutting.screen.custom.WoodcutterScreenHandler;

public class WoodcutterBlock extends StonecutterBlock {

    public WoodcutterBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
            (syncId, inv, player) -> 
                new WoodcutterScreenHandler(syncId, inv, pos),
            Text.translatable("container.woodcutter")
        );
    }

}
