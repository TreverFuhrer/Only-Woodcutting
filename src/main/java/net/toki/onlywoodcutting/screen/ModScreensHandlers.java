package net.toki.onlywoodcutting.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.screen.custom.WoodcutterScreenHandler;

// ModScreens.java
public final class ModScreensHandlers {
        public static final ScreenHandlerType<WoodcutterScreenHandler> WOODCUTTER_SCREEN_HANDLER =
                Registry.register(Registries.SCREEN_HANDLER, Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutter"),
                        new ExtendedScreenHandlerType<>(
                            (syncId, inv, data) -> new WoodcutterScreenHandler(syncId, inv),
                            PacketCodec.unit(Unit.INSTANCE)
                ));

        public static void registerHandlers() {
                OnlyWoodcutting.LOGGER.info("Registering Woodcutting Recipe.");
        }
}
