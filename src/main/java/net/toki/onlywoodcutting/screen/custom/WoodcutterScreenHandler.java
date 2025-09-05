package net.toki.onlywoodcutting.screen.custom;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.toki.onlywoodcutting.block.ModBlocks;
import net.toki.onlywoodcutting.recipe.ModRecipes;
import net.toki.onlywoodcutting.recipe.custom.WoodcuttingRecipe;
import net.toki.onlywoodcutting.recipe.custom.WoodcuttingRecipeInput;
import net.toki.onlywoodcutting.screen.ModScreenHandlers;

public class WoodcutterScreenHandler extends ScreenHandler {

    public static final int INPUT_ID = 0;   // handler slot id
    public static final int OUTPUT_ID = 1;  // handler slot id

    private final ScreenHandlerContext context;
    private final Property selectedRecipe = Property.create();
    private final World world;

    private List<RecipeEntry<WoodcuttingRecipe>> availableRecipes = List.of();
    private ItemStack inputStack = ItemStack.EMPTY;
    long lastTakeTime;

    final Slot inputSlot;
    final Slot outputSlot;

    private Runnable contentsChangedListener = () -> {};

    public final Inventory input = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            WoodcutterScreenHandler.this.onContentChanged(this);
            WoodcutterScreenHandler.this.contentsChangedListener.run();
        }
    };
    final CraftingResultInventory output = new CraftingResultInventory();

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.getWorld(), pos));
    }

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.WOODCUTTER_SCREEN_HANDLER, syncId);
        this.context = context;
        this.world = playerInventory.player.getWorld();

        this.inputSlot = this.addSlot(new Slot(this.input, /*inv idx*/ 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.output, /*inv idx*/ 0, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraftByPlayer(player, stack.getCount());
                WoodcutterScreenHandler.this.output.unlockLastRecipe(player, this.getInputStacks());
                ItemStack consumed = WoodcutterScreenHandler.this.inputSlot.takeStack(1);
                if (!consumed.isEmpty()) {
                    WoodcutterScreenHandler.this.populateResult();
                }

                context.run((world, pos) -> {
                    long now = world.getTime();
                    if (WoodcutterScreenHandler.this.lastTakeTime != now) {
                        world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        WoodcutterScreenHandler.this.lastTakeTime = now;
                    }
                });
                super.onTakeItem(player, stack);
            }

            private List<ItemStack> getInputStacks() {
                return List.of(WoodcutterScreenHandler.this.inputSlot.getStack());
            }
        });

        // Player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addProperty(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<RecipeEntry<WoodcuttingRecipe>> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.WOODCUTTER);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
            this.populateResult();
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack current = this.inputSlot.getStack();
        if (!current.isOf(this.inputStack.getItem())) {
            this.inputStack = current.copy();
            this.updateInput(inventory, current);
        }
    }

    private static WoodcuttingRecipeInput createRecipeInput(Inventory inventory) {
        return new WoodcuttingRecipeInput(inventory.getStack(0));
    }

    // Compute matches on the SERVER; client will be synced
    private void updateInput(Inventory inv, ItemStack stack) {
        this.availableRecipes = List.of();
        this.selectedRecipe.set(-1);
        this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);

        if (stack.isEmpty() || this.world.isClient()) return;

        ServerWorld sw = (ServerWorld) this.world;
        WoodcuttingRecipeInput in = createRecipeInput(inv);

        this.availableRecipes = sw.getRecipeManager()
                .getAllMatches(ModRecipes.WOODCUTTING, in, sw)
                .toList();
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            RecipeEntry<WoodcuttingRecipe> entry = this.availableRecipes.get(this.selectedRecipe.get());
            ItemStack out = entry.value().craft(createRecipeInput(this.input), this.world.getRegistryManager());
            if (out.isItemEnabled(this.world.getEnabledFeatures())) {
                this.output.setLastRecipe(entry);
                this.outputSlot.setStackNoCallbacks(out);
            } else {
                this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            }
        } else {
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreenHandlers.WOODCUTTER_SCREEN_HANDLER;
    }

    public void setContentsChangedListener(Runnable listener) {
        this.contentsChangedListener = listener != null ? listener : () -> {};
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            Item item = stack.getItem();
            ret = stack.copy();

            if (slotIndex == OUTPUT_ID) {
                item.onCraftByPlayer(stack, player);
                if (!this.insertItem(stack, 2, 38, true)) return ItemStack.EMPTY;
                slot.onQuickTransfer(stack, ret);

            } else if (slotIndex == INPUT_ID) {
                if (!this.insertItem(stack, 2, 38, false)) return ItemStack.EMPTY;

            } else if (!this.world.isClient()) {
                ServerWorld sw = (ServerWorld) this.world;
                boolean hasMatch = sw.getRecipeManager()
                        .getFirstMatch(ModRecipes.WOODCUTTING, createRecipeInput(this.input), sw)
                        .isPresent();
                if (hasMatch) {
                    if (!this.insertItem(stack, 0, 1, false)) return ItemStack.EMPTY;
                } else if (slotIndex >= 2 && slotIndex < 29) {
                    if (!this.insertItem(stack, 29, 38, false)) return ItemStack.EMPTY;
                } else if (slotIndex >= 29 && slotIndex < 38 && !this.insertItem(stack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            slot.markDirty();
            if (stack.getCount() == ret.getCount()) return ItemStack.EMPTY;

            slot.onTakeItem(player, stack);
            this.sendContentUpdates();
        }
        return ret;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(0); // result inventory has only slot 0
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}
