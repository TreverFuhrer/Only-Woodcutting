package net.toki.onlywoodcutting.block.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
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
        return new ExtendedScreenHandlerFactory<BlockPos>() {
            @Override
            public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
                return pos;
            }

            @Override
            public Text getDisplayName() {
                return TITLE;
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new WoodcutterScreenHandler(syncId, inv, pos);
            }
        };
    }
}
