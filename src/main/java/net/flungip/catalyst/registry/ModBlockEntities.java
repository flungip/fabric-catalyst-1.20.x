package net.flungip.catalyst.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.flungip.catalyst.Catalyst;
import net.flungip.catalyst.block.AlchemyTableBlockEntity;
import net.flungip.catalyst.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlockEntities {
    public static final BlockEntityType<AlchemyTableBlockEntity> ALCHEMY_TABLE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(Catalyst.MOD_ID, "alchemy_table"),
                    FabricBlockEntityTypeBuilder.create(
                            AlchemyTableBlockEntity::new,
                            ModBlocks.ALCHEMY_TABLE
                    ).build()
            );

    private ModBlockEntities() {}

    /** Called from your main mod initializer */
    public static void registerAll() {
        // no-op: static initializers did it all
    }
}
