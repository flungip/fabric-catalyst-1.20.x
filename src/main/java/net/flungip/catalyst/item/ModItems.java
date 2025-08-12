package net.flungip.catalyst.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item STEW = new Item(new Item.Settings().food(
            new FoodComponent.Builder()
                    .hunger(6)
                    .saturationModifier(0.6f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 24000), 1.0f)
                    .build()
    ));

    public static final Item AED_BADGE =
            new TranshumancePotionItem(new Item.Settings().maxCount(1));

    public static final Item POTION_OF_TRANSHUMANCE =
            new TranshumancePotionItem(new Item.Settings().maxCount(1));

    public static final Item POTION_OF_WITHDRAWL =
            new WithdrawalPotionItem(new Item.Settings().maxCount(1));

    public static final Item WITHER_CREAM   = new Item(new Item.Settings());
    public static final Item BLAST_POWDER   = new Item(new Item.Settings());
    public static final Item BONE_DUST      = new Item(new Item.Settings());
    public static final Item CHORUS_POWDER  = new Item(new Item.Settings());
    public static final Item ENDER_SLIME    = new Item(new Item.Settings());
    public static final Item NETHER_POWDER  = new Item(new Item.Settings());
    public static final Item WITHER_SLIME   = new Item(new Item.Settings());
    public static final Item WITHER_DUST    = new Item(new Item.Settings());
    public static final Item XADRYL         = new Item(new Item.Settings());
    public static final Item XADRYL_TAB     = new Item(new Item.Settings());

    public static void registerAll() {
        Registry.register(Registries.ITEM, new Identifier("catalyst", "stew"),                STEW);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "aed_badge"),                AED_BADGE);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "potion_of_transhumance"), POTION_OF_TRANSHUMANCE);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "potion_of_withdrawl"),    POTION_OF_WITHDRAWL);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "wither_cream"),   WITHER_CREAM);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "blast_powder"),   BLAST_POWDER);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "bone_dust"),      BONE_DUST);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "chorus_powder"),  CHORUS_POWDER);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "ender_slime"),    ENDER_SLIME);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "nether_powder"),  NETHER_POWDER);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "wither_slime"),   WITHER_SLIME);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "wither_dust"),    WITHER_DUST);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "xadryl"),         XADRYL);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "xadryl_tab"),     XADRYL_TAB);
    }
}