package net.flungip.catalyst.block;

import net.flungip.catalyst.Catalyst;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public final class ModBlocks {
    public static final Block ALCHEMY_TABLE = new AlchemyTableBlock(
            FabricBlockSettings.create()
                    .strength(2.5f)
                    .nonOpaque()
    );

    private ModBlocks() {}

    public static void registerBlocks() {
        Identifier id = new Identifier(Catalyst.MOD_ID, "alchemy_table");
        // register the block
        Registry.register(Registries.BLOCK, id, ALCHEMY_TABLE);
        // register its item form
        Registry.register(Registries.ITEM, id,
                new BlockItem(ALCHEMY_TABLE, new Item.Settings()));
    }
}
