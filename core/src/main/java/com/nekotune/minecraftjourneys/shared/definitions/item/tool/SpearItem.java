package com.nekotune.minecraftjourneys.shared.definitions.item.tool;

import com.nekotune.minecraftjourneys.shared.definitions.entity.projectile.ThrownSpear;
import com.nekotune.minecraftjourneys.shared.registries.MJSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;

public class SpearItem extends TieredItem implements ProjectileItem {
    protected static final RandomSource random = RandomSource.create();

    protected final float throwDamage;

    public SpearItem(Tier tier, int attackDamageBonus, float attackSpeed, Properties properties) {
        super(tier, properties.attributes(
                SpearItem.createAttributes(tier, attackDamageBonus, attackSpeed)));
        throwDamage = attackDamageBonus + tier.getAttackDamageBonus() + 4.0f;
    }

    protected static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)(attackDamage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            )
            .build();
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        if (i < 10) return;
        if (isTooDamagedToUse(stack)) return;
        if (!level.isClientSide) {
            
            ThrownSpear thrownSpear = asProjectile(level, player.position().add(0.0f, 1.65f, 0.0f), stack, player.getDirection());
            thrownSpear.setOwner(player);
            if (player.hasInfiniteMaterials()) {
                thrownSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            } else {
                thrownSpear.pickup = AbstractArrow.Pickup.ALLOWED;
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                player.getInventory().removeItem(stack);
            }
            thrownSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6F, 1.0F);

            level.addFreshEntity(thrownSpear);
            final float fv = 0.2F;
            final float pitch = random.nextFloat() * fv + 1.0F - (fv/2.0F);
            level.playSound(null, thrownSpear, 
                    MJSoundEvents.SPEAR_THROW.get(), SoundSource.PLAYERS,
                    1.0F, pitch);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see {@link #onItemUse}.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isTooDamagedToUse(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise the damage on the stack.
     */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_TRIDENT_ACTIONS.contains(itemAbility);
    }

    @Override
    public ThrownSpear asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new ThrownSpear(throwDamage,
                level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
    }
}
