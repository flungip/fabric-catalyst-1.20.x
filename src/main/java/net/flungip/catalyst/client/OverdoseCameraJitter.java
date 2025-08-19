package net.flungip.catalyst.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.flungip.catalyst.effect.ModEffects;
import net.minecraft.util.math.MathHelper;

public final class OverdoseCameraJitter {
    private OverdoseCameraJitter() {}

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            var p = client.player;
            if (p == null || client.isPaused()) return;

            var inst = p.getStatusEffect(ModEffects.OVERDOSE);
            if (inst == null) return;

            double progress = 1.0 - Math.min(1.0, inst.getDuration() / 100.0);
            float amp = (float)(0.2 + 0.8 * progress);

            float t = p.age + client.getTickDelta();
            float yawJitter   = (MathHelper.sin(t * 0.9f) + MathHelper.sin(t * 1.7f + 1.3f)) * 0.5f * amp;
            float pitchJitter = (MathHelper.sin(t * 1.1f + 0.7f)) * 0.4f * amp;

            p.setYaw(p.getYaw() + yawJitter);
            p.setPitch(MathHelper.clamp(p.getPitch() + pitchJitter, -90f, 90f));
        });
    }
}
