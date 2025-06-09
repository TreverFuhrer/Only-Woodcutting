package net.toki.onlywoodcutting.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;

public class ModBlocks {

     private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(OnlyWoodcutting.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    
    public static void registerModItems() {
        OnlyWoodcutting.LOGGER.info("Registering Mod Items for " + OnlyWoodcutting.MOD_ID);
    }
}
