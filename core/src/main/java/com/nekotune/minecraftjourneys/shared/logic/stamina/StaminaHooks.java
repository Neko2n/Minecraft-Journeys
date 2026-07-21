package com.nekotune.minecraftjourneys.shared.logic.stamina;

import java.util.List;

import javax.annotation.Nullable;

import com.evandev.reliable_gliders.api.GlidingState;
import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.MinecraftJourneys.Dependency;
import com.nekotune.minecraftjourneys.MinecraftJourneys.ModDependency;
import com.nekotune.minecraftjourneys.MinecraftJourneys.DependentEventBusSubscriber;

import net.bettercombat.api.AttackHand;
import net.bettercombat.api.client.BetterCombatClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class StaminaHooks {
    private static final ModDependency betterCombat = MinecraftJourneys.DEPENDENCIES.get(Dependency.BETTER_COMBAT);

    private static float lastAttackStrengthScale = 1.0f;
    private static boolean attackedFullStrength = false;

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Pre event) {
        final var player = Minecraft.getInstance().player;
        if (player == null)
            return;
        final float attackStrengthScale = player.getAttackStrengthScale(0.0f);
        if (lastAttackStrengthScale == 1.0f && attackStrengthScale < 1.0f) {
            attackedFullStrength = true;
        }
        lastAttackStrengthScale = player.getAttackStrengthScale(0.0f);
    }

    /**
     * Stamina drains while sprinting.
     */
    @SubscribeEvent
    public static void onStaminaCyclePre(StaminaEvent.TickEvent.CycleEvent.Pre event) {
        final var player = event.getPlayer();
        if (player.isSprinting()) {
            event.addBasicDrain();
            event.getStamina().delayRegen();
        }
    }

    /**
     * XP orbs give stamina instead of experience.
     */
    @SubscribeEvent
    public static void onXpChanged(PlayerXpEvent.XpChange event) {
        final int xpAdded = event.getAmount();
        final var player = event.getEntity();
        final var stamina = PlayerStamina.get(player);
        final float staminaAdded = ((float) xpAdded) * MJConfig.XP_TO_STAMINA.get().floatValue();
        stamina.setValue(stamina.getValue() + staminaAdded,
                GUIAnimationProperties.defaultScaled(staminaAdded));
    }

    @SubscribeEvent
    public static void onLevelChange(PlayerXpEvent.LevelChange event) {
        event.setCanceled(true);
    }

    /**
     * Give a little bit of stamina upon dealing damage to enemies.
     */
    @SubscribeEvent
    public static void onHitEnemy(LivingDamageEvent.Post event) {
        final var source = event.getSource();
        if (!source.is(DamageTypeTags.IS_PLAYER_ATTACK))
            return;
        final var attacker = source.getEntity();
        if (!(attacker instanceof final Player player))
            return;
        final var target = event.getEntity();
        if (!(target instanceof Enemy))
            return;
        final float damage = event.getNewDamage();
        final float reward = Math.min(0.2f, (damage - 3f) / 40f);
        if (reward <= 0f)
            return;
        final var stamina = PlayerStamina.get(player);
        stamina.setValue(stamina.getValue() + reward,
                GUIAnimationProperties.defaultScaled(reward));
    }

    @SubscribeEvent
    public static void onPlayerLeftClickEmpty(final LeftClickEmpty event) {
        if (betterCombat.isLoaded())
            return;
        swingWeaponEvent(event.getHand());
    }

    @SubscribeEvent
    public static void onPlayerAttackEntity(final AttackEntityEvent event) {
        if (betterCombat.isLoaded())
            return;
        swingWeaponEvent(event.getEntity().swingingArm);
    }

    /**
     * Stamina penalties for swinging weapons.
     */
    private static void swingWeaponEvent(final InteractionHand hand) {
        final var minecraft = Minecraft.getInstance();
        final var player = minecraft.player;

        // Verify the player swung a weapon
        final var heldItem = player.getItemInHand(hand);
        final double attackSpeedMod = getAttackSpeedModifier(heldItem);
        float reduction = -3.0f - ((float) attackSpeedMod);
        if (reduction > 0f) {

            // Apply global weapon swing penalty (regen delay)
            final var stamina = PlayerStamina.get(player);
            stamina.delayRegen();

            // If weapon is heavy enough and attack was fully charged, apply further stamina
            // penalty
            if (!attackedFullStrength && !betterCombat.isLoaded())
                return;
            reduction /= 3f;
            reduction *= MJConfig.HEAVY_SWING_STAMINA_MULTIPLIER.get().floatValue();
            stamina.setValue(stamina.getValue() - reduction,
                    GUIAnimationProperties.defaultScaled(reduction));
        }
        attackedFullStrength = false;
    }

    private static double getAttackSpeedModifier(ItemStack stack) {
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null)
            return 0d;
        for (final var entry : modifiers.modifiers()) {
            final var modifier = entry.modifier();
            if (modifier.is(Attributes.ATTACK_SPEED.value().getBaseId())) {
                return modifier.amount();
            }
        }
        return 0d;
    }

    @DependentEventBusSubscriber(dependency = Dependency.BETTER_COMBAT, value = Dist.CLIENT)
    @OnlyIn(value = Dist.CLIENT)
    public static final class BetterCombatClientHooks {

        private static boolean registered = false;

        @SubscribeEvent
        public static void onClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
            if (registered)
                return;
            BetterCombatClientEvents.ATTACK_HIT.register(BetterCombatClientHooks::onAttackHit);
            BetterCombatClientHooks.registered = true;
        }

        private static void onAttackHit(LocalPlayer player, AttackHand attackHand,
                List<Entity> targets, @Nullable Entity cursorTarget) {
            if (targets.isEmpty()) {
                swingWeaponEvent(attackHand.isOffHand() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            }
            // If the attack hit any entities, onHitEnemy already handles success
        }
    }

    /**
     * Drain stamina while gliding, and cancel gliding if stamina runs out.
     */
    @DependentEventBusSubscriber(dependency = Dependency.RELIABLE_GLIDERS)
    public static final class ReliableGlidersHooks {

        @SubscribeEvent
        public static void onStaminaCyclePre(StaminaEvent.TickEvent.CycleEvent.Pre event) {
            final var player = event.getPlayer();
            if (GlidingState.wasGliding(player)) {
                event.addBasicDrain(MJConfig.STAMINA_GLIDER_DRAIN_MULTIPLIER.get().floatValue());
                event.getStamina().delayRegen();
            }
        }
    }
}
