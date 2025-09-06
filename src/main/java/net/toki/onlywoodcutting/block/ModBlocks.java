package net.toki.onlywoodcutting.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.block.custom.WoodcutterBlock;

public final class ModBlocks {

    private ModBlocks() {}

    // IDs / Keys
    public static final Identifier WOODCUTTER_ID = Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutter");
    public static final RegistryKey<Block> WOODCUTTER_KEY = RegistryKey.of(RegistryKeys.BLOCK, WOODCUTTER_ID);
    public static final RegistryKey<Item>  WOODCUTTER_ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM,  WOODCUTTER_ID);

    // Instances
    public static final Block WOODCUTTER = registerWoodcutter();

    private static Block registerWoodcutter() {
        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .mapColor(MapColor.OAK_TAN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.5F)
                .sounds(BlockSoundGroup.WOOD)
                .burnable()
                .registryKey(WOODCUTTER_KEY);

        Block block = new WoodcutterBlock(settings);

        // Register block
        Registry.register(Registries.BLOCK, WOODCUTTER_ID, block);

        // IMPORTANT: attach item key to Item.Settings BEFORE constructing BlockItem
        Item.Settings itemSettings = new Item.Settings().useBlockPrefixedTranslationKey().registryKey(WOODCUTTER_ITEM_KEY);
        Registry.register(Registries.ITEM, WOODCUTTER_ID, new BlockItem(block, itemSettings));

        return block;
    }

    public static void register() {
        OnlyWoodcutting.LOGGER.info("Registering Mod Blocks for " + OnlyWoodcutting.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register(entries -> entries.addAfter(Blocks.STONECUTTER, ModBlocks.WOODCUTTER));
    }
}