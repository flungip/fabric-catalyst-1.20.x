package net.flungip.catalyst.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.flungip.catalyst.Catalyst;
import net.flungip.catalyst.registry.AlchemyTableScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class ModScreenHandlers {
    private ModScreenHandlers() {} // no instantiation

    @SuppressWarnings("deprecation")
    public static final ScreenHandlerType<AlchemyTableScreenHandler> ALCHEMY_TABLE =
            ScreenHandlerRegistry.registerExtended(
                    new Identifier(Catalyst.MOD_ID, "alchemy_table"),
                    AlchemyTableScreenHandler::new
            );

    /** Force class-loading so that registerExtended actually runs. */
    public static void registerAll() {}
}
