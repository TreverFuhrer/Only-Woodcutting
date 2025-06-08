package com.toki.onlywoodcutting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.toki.onlywoodcutting.block.WoodcutterBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlyWoodcutting implements ModInitializer {
    public static final String MOD_ID = "onlywoodcutting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Block WOODCUTTER = new WoodcutterBlock(Block.Settings.copy(Blocks.STONECUTTER));

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "woodcutter"), WOODCUTTER);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "woodcutter"), new BlockItem(WOODCUTTER, new Item.Settings()));
        LOGGER.info("Initialized Only Woodcutting");
    }
}
