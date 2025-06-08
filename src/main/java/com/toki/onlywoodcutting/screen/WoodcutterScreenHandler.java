package com.toki.onlywoodcutting.screen;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class WoodcutterScreenHandler extends ScreenHandler {
    private final Inventory input = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            WoodcutterScreenHandler.this.updateResult();
        }
    };
    private final Inventory result = new SimpleInventory(1);
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.STONECUTTER, syncId);
        this.context = context;
        this.player = playerInventory.player;

        this.addSlot(new Slot(input, 0, 20, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ItemTags.PLANKS) || stack.isIn(ItemTags.LOGS);
            }
        });

        this.addSlot(new Slot(result, 1, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());
                context.run((world, pos) -> world.syncWorldEvent(1044, pos, 0));
                return super.onTakeItem(player, stack);
            }
        });

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void updateResult() {
        ItemStack stack = input.getStack(0);
        if (!stack.isEmpty() && (stack.isIn(ItemTags.PLANKS) || stack.isIn(ItemTags.LOGS))) {
            context.run((world, pos) -> {
                World w = world;
                Optional<StonecuttingRecipe> recipe = w.getRecipeManager().getFirstMatch(RecipeType.STONECUTTING, input, w);
                result.setStack(0, recipe.map(r -> r.craft(input, w.getRegistryManager())).orElse(ItemStack.EMPTY));
            });
        } else {
            result.setStack(0, ItemStack.EMPTY);
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        Slot from = this.getSlot(slot);
        if (!from.hasStack()) return ItemStack.EMPTY;
        ItemStack original = from.getStack();
        ItemStack copy = original.copy();
        if (slot == 1) {
            if (!this.insertItem(original, 2, 38, true)) return ItemStack.EMPTY;
            from.onQuickTransfer(original, copy);
        } else if (slot == 0) {
            if (!this.insertItem(original, 2, 38, false)) return ItemStack.EMPTY;
        } else {
            if (!this.insertItem(original, 0, 1, false)) return ItemStack.EMPTY;
        }
        if (original.isEmpty()) {
            from.setStack(ItemStack.EMPTY);
        } else {
            from.markDirty();
        }
        return copy;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}
