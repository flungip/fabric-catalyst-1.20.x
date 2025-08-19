package net.flungip.catalyst;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import net.flungip.catalyst.registry.ModScreenHandlers;
import net.flungip.catalyst.screen.MortarItemScreen;   // <-- add this

public class CatalystClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.MORTAR_ITEM, MortarItemScreen::new);
        System.out.println("[Catalyst] Client: registered MortarItem screen");
    }
}
