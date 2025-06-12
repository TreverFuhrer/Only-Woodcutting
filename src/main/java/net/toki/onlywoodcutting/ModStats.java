package net.toki.onlywoodcutting;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class ModStats {
    public static final Identifier WOODCUTTER_INTERACT_ID = Identifier.of(OnlyWoodcutting.MOD_ID, "interact_with_woodcutter");
    private static final StatType<Identifier> CUSTOM = Stats.CUSTOM;
    public static Stat<Identifier> WOODCUTTER_INTERACT;

    public static void register() {
        Registry.register(Registries.CUSTOM_STAT, WOODCUTTER_INTERACT_ID, WOODCUTTER_INTERACT_ID);
        WOODCUTTER_INTERACT = CUSTOM.getOrCreateStat(WOODCUTTER_INTERACT_ID);
    }

    private ModStats() {}
}
