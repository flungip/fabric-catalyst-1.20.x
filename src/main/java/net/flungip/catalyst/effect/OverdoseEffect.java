package net.flungip.catalyst.effect;

import net.flungip.catalyst.damage.ModDamageTypes;
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
        super(StatusEffectCategory.HARMFUL, 0x3A0A4A);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.getWorld().isClient) return;

        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0, false, true, true));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 6, false, true, true));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0, false, true, true));

        entity.getWorld().playSound(
                null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS,
                0.6f, 1.2f
        );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient) {
            var inst = entity.getStatusEffect(ModEffects.OVERDOSE);
            if (inst == null) return;

            int dur = inst.getDuration();

            if (dur <= 1) {
                entity.damage(ModDamageTypes.overdose(entity.getWorld()), 1_000_000f);
                return;
            }

            if (dur % 12 == 0) {
                double t = 1.0 - Math.min(1.0, dur / 100.0);
                float vol = (float) (0.4 + 0.8 * t);
                float pitch = (float) (1.0 - 0.3 * t);
                entity.getWorld().playSound(
                        null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ENTITY_WARDEN_HEARTBEAT,
                        SoundCategory.PLAYERS,
                        vol, pitch
                );
            }
        }

        if (entity instanceof PlayerEntity p) {
            p.setSwimming(true);
            p.setSprinting(false);
            if (p.getPose() != EntityPose.SWIMMING) {
                p.setPose(EntityPose.SWIMMING);
                p.calculateDimensions();
            }
            p.fallDistance = 0f;
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!(entity instanceof PlayerEntity p)) return;

        p.setSwimming(false);

        boolean shouldRestore =
                !p.isTouchingWater() &&
                        !p.isSubmergedInWater() &&
                        !p.isFallFlying() &&
                        p.getVehicle() == null;

        if (shouldRestore && p.getPose() == EntityPose.SWIMMING) {
            p.setPose(EntityPose.STANDING);
            p.calculateDimensions();
        }
    }
}
