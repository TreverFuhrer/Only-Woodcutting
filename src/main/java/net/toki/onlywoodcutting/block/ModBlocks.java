package net.toki.onlywoodcutting.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.block.custom.WoodcutterBlock;

public class ModBlocks {
    
    // Register Woodcutter block
    public static final Block WOODCUTTER = registerBlock("woodcutter",
            new WoodcutterBlock(AbstractBlock.Settings.create()
                .mapColor(MapColor.OAK_TAN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.5F)
                .sounds(BlockSoundGroup.WOOD)
                .burnable()
            ));


    // Registration helper methods
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(OnlyWoodcutting.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(OnlyWoodcutting.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void register() {
        OnlyWoodcutting.LOGGER.info("Registering Mod Blocks for " + OnlyWoodcutting.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.WOODCUTTER);
        });
    }
}
