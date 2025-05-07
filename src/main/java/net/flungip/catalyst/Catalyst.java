package net.flungip.catalyst;

import net.fabricmc.api.ModInitializer;

import net.flungip.catalyst.Item.CatalystItemGroup;
import net.flungip.catalyst.Item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Catalyst implements ModInitializer {
	public static final String MOD_ID = "catalyst";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		CatalystItemGroup.registerItemGroups();
	}
}