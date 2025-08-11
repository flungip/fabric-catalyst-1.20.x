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

    public static void registerAll() {
        Registry.register(Registries.ITEM, new Identifier("catalyst", "stew"),                STEW);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "aed_badge"),                AED_BADGE);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "potion_of_transhumance"), POTION_OF_TRANSHUMANCE);
        Registry.register(Registries.ITEM, new Identifier("catalyst", "potion_of_withdrawl"),    POTION_OF_WITHDRAWL);
    }
}