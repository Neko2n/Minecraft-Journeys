package com.nekotune.minecraftjourneys.shared.definition.item.tool;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.data.tags.MJBlockTags;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.common.ItemAbility;

public class MultitoolItem extends DiggerItem {

    private static final Supplier<Item> EXAMPLE_AXE = () -> Items.IRON_AXE;
    private static final Supplier<Item> EXAMPLE_PICKAXE = () -> Items.IRON_PICKAXE;

    public MultitoolItem(Tier tier, int attackDamageBonus, float attackSpeed, Properties properties) {
        super(tier, MJBlockTags.MINEABLE_WITH_MULTITOOL, properties
                .attributes(MultitoolItem.createAttributes(tier, attackDamageBonus, attackSpeed)));
    }

    protected static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return DiggerItem.createAttributes(tier, (float)attackDamage, attackSpeed);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result;
        result = EXAMPLE_AXE.get().useOn(context);
        if (result.consumesAction()) return result;
        result = EXAMPLE_PICKAXE.get().useOn(context);
        return result;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return EXAMPLE_AXE.get().canPerformAction(stack, itemAbility)
            || EXAMPLE_PICKAXE.get().canPerformAction(stack, itemAbility);
    }

    // Copied from SwordItem
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    // Copied from SwordItem
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}
