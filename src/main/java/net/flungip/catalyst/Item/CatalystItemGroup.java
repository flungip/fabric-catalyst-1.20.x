package net.flungip.catalyst.Item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.flungip.catalyst.Catalyst;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CatalystItemGroup {
    public static final ItemGroup CATALYST_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(Catalyst.MOD_ID, "catalyst_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.catalyst_tab"))
                    .icon(() -> new ItemStack(ModItems.STEW))
                    .entries((context, entries) -> {
                        entries.add(ModItems.STEW);
                    })
                    .build()
    );

    public static void registerItemGroups() {
    }
}