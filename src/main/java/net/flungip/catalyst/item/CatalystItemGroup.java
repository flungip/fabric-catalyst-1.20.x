package net.flungip.catalyst.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.flungip.catalyst.Catalyst;

public final class CatalystItemGroup {

    public static final ItemGroup CATALYST_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.catalyst_tab"))
            .icon(() -> new ItemStack(ModItems.STEW))
            .entries((enabledFeatures, entries) -> {

                entries.add(ModItems.STEW);
                entries.add(ModItems.POTION_OF_TRANSHUMANCE);
                entries.add(ModItems.AED_BADGE);
                entries.add(ModItems.BLAST_POWDER);
                entries.add(ModItems.NETHER_POWDER);
                entries.add(ModItems.BONE_DUST);
                entries.add(ModItems.CHORUS_POWDER);
                entries.add(ModItems.ENDER_SLIME);
                entries.add(ModItems.WITHER_DUST);
                entries.add(ModItems.WITHER_SLIME);
                entries.add(ModItems.WITHER_CREAM);
                entries.add(ModItems.XADRYL);
                entries.add(ModItems.XADRYL_TAB);
                entries.add(ModItems.MORTAR_AND_PESTLE);
            })
            .build();

    private CatalystItemGroup() {}

    public static void register() {
        Registry.register(
                Registries.ITEM_GROUP,
                new Identifier(Catalyst.MOD_ID, "catalyst_tab"),
                CATALYST_GROUP
        );
    }
}
