package net.toki.onlywoodcutting.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;

public class ModItems {

    public static final Item WOOD_CUTTER = registerItem("wood_cutter", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(OnlyWoodcutting.MOD_ID, name), item);
    }
    
    public static void registerModItems() {
        OnlyWoodcutting.LOGGER.info("Registering Mod Items for " + OnlyWoodcutting.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(WOOD_CUTTER);
        });
    }
}
