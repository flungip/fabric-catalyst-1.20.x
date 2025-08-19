package net.flungip.catalyst.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class ModDamageTypes {
    public static final RegistryKey<DamageType> OVERDOSE =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("catalyst", "overdose"));

    public static DamageSource overdose(World world) {
        RegistryEntry<DamageType> entry =
                world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(OVERDOSE);
        return new DamageSource(entry);
    }

    private ModDamageTypes() {}
}
