package net.flungip.catalyst;

import net.fabricmc.api.ModInitializer;
import net.flungip.catalyst.effect.ModEffects;
import net.flungip.catalyst.item.CatalystItemGroup;
import net.flungip.catalyst.item.ModItems;
import net.flungip.catalyst.registry.ModScreenHandlers;

public class Catalyst implements ModInitializer {
	public static final String MOD_ID = "catalyst";

	@Override
	public void onInitialize() {
		CatalystItemGroup.register();
		ModItems.registerAll();
		ModScreenHandlers.registerAll();
		ModEffects.init();
		net.flungip.catalyst.effect.ModPotions.registerRecipes();
	}
}
