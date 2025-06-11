package net.toki.onlywoodcutting.block.custom;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.toki.onlywoodcutting.screen.custom.WoodcutterScreenHandler;

public class WoodcutterBlock extends StonecutterBlock {

    private static final Text TITLE = Text.translatable("container.woodcutter");


    public WoodcutterBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hit);
    }

    @Nullable
	@Override
	protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new ExtendedScreenHandlerFactory<>() {

            @Override
            public Text getDisplayName() {
                return TITLE;
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new WoodcutterScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
            }

            @Override
            public Object getScreenOpeningData(ServerPlayerEntity player) {
                return null;
            }
            
        };
	}
}
