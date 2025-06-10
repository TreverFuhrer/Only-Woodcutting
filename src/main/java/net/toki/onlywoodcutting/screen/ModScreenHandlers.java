package net.toki.onlywoodcutting.screen;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.screen.custom.WoodcutterScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<WoodcutterScreenHandler> WOODCUTTER_HANDLER =
        ScreenHandlerRegistry.registerSimple(
            Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutter"),
            WoodcutterScreenHandler::new
        );

    public static void registerScreenHandlers() {
        OnlyWoodcutting.LOGGER.info("Registered screen handlers");
    }
}

