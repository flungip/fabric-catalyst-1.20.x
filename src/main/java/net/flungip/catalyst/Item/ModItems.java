package net.flungip.catalyst.Item;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModItems {

    public static final Item STEW = new Item(new Item.Settings().food(
            new FoodComponent.Builder()
                    .hunger(6)
                    .saturationModifier(0.6f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 24000), 1.0f)
                    .build()
    ));

    public static void registerModItems() {
        Registry.register(Registries.ITEM, new Identifier("catalyst", "stew"), STEW);
    }
}
