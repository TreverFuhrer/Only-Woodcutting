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

public class ModBlocks {

    public static final Block WOOD_CUTTER_BLOCK = registerBlock("woodcutter", 
        new Block(AbstractBlock.Settings.create()
            .mapColor(MapColor.OAK_TAN)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.5F)
            .sounds(BlockSoundGroup.WOOD)
            .burnable())
        );

    // Helper method register block
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(OnlyWoodcutting.MOD_ID), block);
    }

    // Helper method register item for block
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(OnlyWoodcutting.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    
    public static void registerModBlocks() {
        OnlyWoodcutting.LOGGER.info("Registering Mod Items for " + OnlyWoodcutting.MOD_ID);

        // Add block item to creative inventory
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.WOOD_CUTTER_BLOCK);
        });
    }
}
