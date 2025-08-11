package net.flungip.catalyst.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.ArrayList;

public class WithdrawalEffect extends StatusEffect {

    public WithdrawalEffect() { super(StatusEffectCategory.NEUTRAL, 0xD4A56A); }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.getWorld().isClient) return;


        for (var inst : new ArrayList<>(entity.getStatusEffects())) {
            if (inst.getEffectType() != this) {
                entity.removeStatusEffect(inst.getEffectType());
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) { return false; }
}
