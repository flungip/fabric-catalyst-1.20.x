package net.flungip.catalyst.mixin;

import net.flungip.catalyst.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void catalyst$denyWhileImmune(StatusEffectInstance incoming, CallbackInfoReturnable<Boolean> cir) {
        StatusEffect type = incoming.getEffectType();
        boolean immune = this.hasStatusEffect(ModEffects.NULLING) || this.hasStatusEffect(ModEffects.WITHDRAWAL);
        if (immune && type != ModEffects.NULLING && type != ModEffects.WITHDRAWAL && type != ModEffects.OVERDOSE) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
            at = @At("HEAD"), cancellable = true)
    private void catalyst$blockAddWhileImmune(StatusEffectInstance instance, CallbackInfoReturnable<Boolean> cir) {
        StatusEffect type = instance.getEffectType();
        boolean immune = this.hasStatusEffect(ModEffects.NULLING) || this.hasStatusEffect(ModEffects.WITHDRAWAL);
        if (immune && type != ModEffects.NULLING && type != ModEffects.WITHDRAWAL && type != ModEffects.OVERDOSE) {
            cir.setReturnValue(false);
        }
    }
}

