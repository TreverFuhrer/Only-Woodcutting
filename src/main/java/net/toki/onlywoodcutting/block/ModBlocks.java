package net.toki.onlywoodcutting.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
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

import java.util.function.Function;

public class ModBlocks {

    public static final Block WOOD_CUTTER_BLOCK = register(
    "woodcutter",
        settings -> new WoodcutterBlock(settings),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.OAK_TAN)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.5F)
            .sounds(BlockSoundGroup.WOOD)
            .burnable()
            .pistonBehavior(PistonBehavior.NORMAL),
        true
    );

    // === Main block registration method ===
    private static Block register(
            String name,
            Function<AbstractBlock.Settings, Block> blockFactory,
            AbstractBlock.Settings settings,
            boolean registerItem) {

        RegistryKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        if (registerItem) {
            RegistryKey<Item> itemKey = keyOfItem(name);
            BlockItem item = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
            Registry.register(Registries.ITEM, itemKey, item);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    // === Utility methods for cleaner key creation ===
    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, id(name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, id(name));
    }

    private static Identifier id(String name) {
        return Identifier.of(OnlyWoodcutting.MOD_ID, name);
    }

    // === Creative tab registration ===
    public static void registerModBlocks() {
        OnlyWoodcutting.LOGGER.info("Registering Mod Blocks for " + OnlyWoodcutting.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(WOOD_CUTTER_BLOCK);
        });
    }
}
