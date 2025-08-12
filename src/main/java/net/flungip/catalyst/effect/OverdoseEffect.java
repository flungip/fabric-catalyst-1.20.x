package net.flungip.catalyst.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class OverdoseEffect extends StatusEffect {
    public OverdoseEffect() {
        super(StatusEffectCategory.HARMFUL, 0x3A0A4A); // UI tint
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.getWorld().isClient) return;

        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, true, true));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 6, false, true, true));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, true, true));

        entity.getWorld().playSound(
                null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS,
                0.6f, 1.2f
        );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // tick every tick
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity p) {
            p.setSwimming(true);                // flag
            p.setSprinting(false);
            p.setPose(EntityPose.SWIMMING);     // pose
            p.fallDistance = 0f;
        }

        if (!entity.getWorld().isClient) {
            var inst = entity.getStatusEffect(ModEffects.OVERDOSE);
            if (inst != null) {
                int dur = inst.getDuration();           // counts down
                if (dur > 0 && dur % 12 == 0) {         // ~8 beats over 5s
                    double t = 1.0 - Math.min(1.0, dur / 100.0);
                    float vol = (float) (0.4 + 0.8 * t);
                    float pitch = (float) (1.0 - 0.3 * t);
                    entity.getWorld().playSound(
                            null, entity.getX(), entity.getY(), entity.getZ(),
                            net.minecraft.sound.SoundEvents.ENTITY_WARDEN_HEARTBEAT,
                            net.minecraft.sound.SoundCategory.PLAYERS,
                            vol, pitch
                    );
                }
            }
        }
    }
}