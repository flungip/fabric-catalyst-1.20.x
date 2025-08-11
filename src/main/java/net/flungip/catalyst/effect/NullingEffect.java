package net.flungip.catalyst.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class NullingEffect extends StatusEffect {
    public NullingEffect() {

        super(StatusEffectCategory.BENEFICIAL, 0xFFFFFF);
    }
}
