package net.toki.onlywoodcutting.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.toki.onlywoodcutting.OnlyWoodcutting;
import net.toki.onlywoodcutting.screen.custom.WoodcutterScreenHandler;

public class ModScreenHandlers {

    public static final ScreenHandlerType<WoodcutterScreenHandler> WOODCUTTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, 
                Identifier.of(OnlyWoodcutting.MOD_ID, "woodcutter_screen_handler"),
                new ExtendedScreenHandlerType<>(WoodcutterScreenHandler::new, BlockPos.PACKET_CODEC));

    
    public static void registerScreenHandlers() {
        OnlyWoodcutting.LOGGER.info("Registering Screen Handlers for " + OnlyWoodcutting.MOD_ID);
    }
}
