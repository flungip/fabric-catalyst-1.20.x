package net.flungip.catalyst.effect;

import net.flungip.catalyst.Catalyst;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class  ModEffects {
    public static final StatusEffect NULLING     = register("nulling",     new NullingEffect());
    public static final StatusEffect WITHDRAWAL  = register("withdrawal",  new WithdrawalEffect()); // NEW
    public static final StatusEffect OVERDOSE = register("overdose", new OverdoseEffect());


    private static StatusEffect register(String id, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Catalyst.MOD_ID, id), effect);
    }

    public static void init() {}
}
