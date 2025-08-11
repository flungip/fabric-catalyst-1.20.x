package net.flungip.catalyst.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.flungip.catalyst.effect.ModEffects;


public class TranshumancePotionItem extends Item {
    private static final int RADIUS = 500;

    public TranshumancePotionItem(Settings settings) {
        super(settings.maxCount(1).recipeRemainder(Items.GLASS_BOTTLE));
    }

    @Override public UseAction getUseAction(ItemStack stack) { return UseAction.DRINK; }
    @Override public int getMaxUseTime(ItemStack stack) { return 32; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean immune = user.hasStatusEffect(ModEffects.NULLING)
                || user.hasStatusEffect(ModEffects.WITHDRAWAL);
        if (immune) {
            if (!world.isClient) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 0.8F, 1.0F);
            }
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && (user.hasStatusEffect(ModEffects.NULLING)
                || user.hasStatusEffect(ModEffects.WITHDRAWAL))) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 0.8F, 1.0F);
            return stack;
        }
        if (!world.isClient && world instanceof ServerWorld sw) {
            teleportRandomly(user, sw, RADIUS);
        }

        if (user instanceof PlayerEntity p) {
            p.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!p.getAbilities().creativeMode) {
                stack.decrement(1);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (stack.isEmpty()) return bottle;
                if (!p.getInventory().insertStack(bottle)) p.dropItem(bottle, false);
            }
        } else {
            stack.decrement(1);
        }
        return stack;
    }


    private void teleportRandomly(LivingEntity entity, ServerWorld world, int radius) {
        Random rng = world.getRandom();

        for (int tries = 0; tries < 32; tries++) {
            int x = MathHelper.floor(entity.getX() + (rng.nextDouble() * 2 - 1) * radius);
            int z = MathHelper.floor(entity.getZ() + (rng.nextDouble() * 2 - 1) * radius);

            int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, topY, z);
            if (!world.getWorldBorder().contains(pos)) continue;

            BlockState below = world.getBlockState(pos.down());
            boolean safeGround = below.isSolidBlock(world, pos.down());
            boolean noFluid = world.getFluidState(pos).isEmpty();
            if (!safeGround || !noFluid) continue;

            double sx = entity.getX(), sy = entity.getY(), sz = entity.getZ();
            world.playSound(null, sx, sy, sz,
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS,
                    1.0F, 0.9F + rng.nextFloat() * 0.2F);
            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    sx, sy + entity.getHeight() * 0.5, sz,
                    40,      // count
                    0.5, 0.5, 0.5, // spread
                    0.02     // speed
            );

            if (rng.nextFloat() < 0.05F
                    && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)
                    && world.getDifficulty() != Difficulty.PEACEFUL) {
                EndermiteEntity mite = EntityType.ENDERMITE.create(world);
                if (mite != null) {
                    mite.refreshPositionAndAngles(
                            sx + (rng.nextDouble() - 0.5), sy, sz + (rng.nextDouble() - 0.5),
                            rng.nextFloat() * 360F, 0.0F
                    );
                    world.spawnEntity(mite);
                }
            }

            double tx = x + 0.5, ty = topY + 1, tz = z + 0.5;
            if (entity instanceof ServerPlayerEntity sp) {
                sp.teleport(world, tx, ty, tz, sp.getYaw(), sp.getPitch());
            } else {
                entity.refreshPositionAndAngles(tx, ty, tz, entity.getYaw(), entity.getPitch());
            }

            world.playSound(null, tx, ty, tz,
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS,
                    1.0F, 0.9F + rng.nextFloat() * 0.2F);
            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    tx, ty, tz,
                    60,
                    0.6, 0.6, 0.6,
                    0.02
            );
            return;
        }
    }
}
