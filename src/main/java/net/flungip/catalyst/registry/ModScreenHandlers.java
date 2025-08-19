package net.flungip.catalyst.registry;

import net.flungip.catalyst.screen.MortarItemScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;

public class ModScreenHandlers {
    public static ScreenHandlerType<MortarItemScreenHandler> MORTAR_ITEM;

    public static void registerAll() {
        MORTAR_ITEM = Registry.register(
                Registries.SCREEN_HANDLER,
                new Identifier("catalyst", "mortar_item"),
                new ExtendedScreenHandlerType<>((syncId, playerInv, buf) -> {
                    Hand hand = buf.readEnumConstant(Hand.class);
                    return new MortarItemScreenHandler(syncId, playerInv, hand, true);
                })
        );
    }

    public static ScreenHandler createMortarItem(int syncId, PlayerInventory inv, Hand hand) {
        return new MortarItemScreenHandler(syncId, inv, hand);
    }
}
