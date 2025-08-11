package net.flungip.catalyst.effect;

import net.flungip.catalyst.Catalyst;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;  // <- use this in 1.20.1
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModPotions {
    public static final Potion NULLING = Registry.register(
            Registries.POTION, new Identifier(Catalyst.MOD_ID, "nulling"),
            new Potion(new StatusEffectInstance(ModEffects.NULLING, 20 * 60 * 2))
    );

    public static final Potion WITHDRAWAL = Registry.register(
            Registries.POTION, new Identifier(Catalyst.MOD_ID, "withdrawal"),
            new Potion(new StatusEffectInstance(ModEffects.WITHDRAWAL, 20 * 60 * 2))
    );



    public static void registerRecipes() {

        BrewingRecipeRegistry.registerPotionRecipe(Potions.REGENERATION, Items.MILK_BUCKET, NULLING);


        BrewingRecipeRegistry.registerPotionRecipe(Potions.TURTLE_MASTER, Items.MILK_BUCKET, WITHDRAWAL);

    }

    private ModPotions() {}
}
