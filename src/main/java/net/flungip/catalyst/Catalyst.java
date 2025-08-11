package net.flungip.catalyst;

import net.fabricmc.api.ModInitializer;
import net.flungip.catalyst.effect.ModEffects;
import net.flungip.catalyst.item.CatalystItemGroup;
import net.flungip.catalyst.block.ModBlocks;
import net.flungip.catalyst.registry.ModBlockEntities;
import net.flungip.catalyst.item.ModItems;
import net.flungip.catalyst.registry.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Catalyst implements ModInitializer {
	public static final String MOD_ID = "catalyst";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CatalystItemGroup.register();
		ModBlocks.registerBlocks();
		ModBlockEntities.registerAll();
		ModItems.registerAll();
		ModScreenHandlers.registerAll();
		ModEffects.init();
		net.flungip.catalyst.effect.ModPotions.registerRecipes();

		LOGGER.info("Catalyst successfully initialized");
	}
}
