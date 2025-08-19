package net.flungip.catalyst.item;

import net.flungip.catalyst.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class WithdrawalPotionItem extends Item {
    private static final int NULLING_TICKS = 20 * 60 * 2;

    public WithdrawalPotionItem(Settings settings) {
        super(settings.maxCount(1).recipeRemainder(Items.GLASS_BOTTLE));
    }

    @Override public UseAction getUseAction(ItemStack stack) { return UseAction.DRINK; }
    @Override public int getMaxUseTime(ItemStack stack) { return 32; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            user.clearStatusEffects();
            user.addStatusEffect(new StatusEffectInstance(ModEffects.WITHDRAWAL, 20 * 60 * 2));
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
}
