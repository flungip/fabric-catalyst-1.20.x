package net.flungip.catalyst.item;

import net.flungip.catalyst.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class XadrylItem extends Item {
    public XadrylItem(Settings settings) {
        super(settings
                .maxCount(1)
                .food(new FoodComponent.Builder()
                        .snack()
                        .alwaysEdible()
                        .hunger(0).saturationModifier(0f)
                        .build()));
    }

    @Override public UseAction getUseAction(ItemStack stack) { return UseAction.EAT; }
    @Override public int getMaxUseTime(ItemStack stack) { return 32; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            // 5s overdose; Overdose effect handles blindness/slowness/nausea and death on removal
            user.addStatusEffect(new StatusEffectInstance(ModEffects.OVERDOSE, 100, 0, false, true, true));
        }

        if (user instanceof PlayerEntity p) {
            p.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!p.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        } else {
            stack.decrement(1);
        }
        return stack;
    }
}
