package net.flungip.catalyst;

public class CatalystClient implements net.fabricmc.api.ClientModInitializer {
    @Override public void onInitializeClient() {
        net.flungip.catalyst.client.OverdoseCameraJitter.init();
    }
}