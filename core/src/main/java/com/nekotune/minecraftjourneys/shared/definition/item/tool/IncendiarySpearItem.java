package com.nekotune.minecraftjourneys.shared.definition.item.tool;

import com.nekotune.minecraftjourneys.shared.definition.entity.projectile.ThrownIncendiarySpear;
import com.nekotune.minecraftjourneys.shared.definition.entity.projectile.ThrownSpear;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class IncendiarySpearItem extends SpearItem {
    public IncendiarySpearItem(Tier tier, int attackDamageBonus, float attackSpeed, Properties properties) {
        super(tier, attackDamageBonus, attackSpeed, properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        target.igniteForSeconds(30.0F);
    }

    @Override
    public ThrownSpear asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ThrownSpear thrownSpear = new ThrownIncendiarySpear(throwDamage,
                level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
        thrownSpear.pickup = AbstractArrow.Pickup.ALLOWED;
        return thrownSpear;
    }
}
