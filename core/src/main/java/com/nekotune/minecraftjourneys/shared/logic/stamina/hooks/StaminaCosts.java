package com.nekotune.minecraftjourneys.shared.logic.stamina.hooks;

import java.util.List;

import javax.annotation.Nullable;

import com.evandev.reliable_gliders.api.GlidingState;
import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.MinecraftJourneys.Dependency;
import com.nekotune.minecraftjourneys.MinecraftJourneys.ModDependency;
import com.nekotune.minecraftjourneys.shared.logic.stamina.GUIAnimationProperties;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;
import com.nekotune.minecraftjourneys.shared.logic.stamina.StaminaEvent;
import com.nekotune.minecraftjourneys.MinecraftJourneys.DependentEventBusSubscriber;

import net.bettercombat.api.AttackHand;
import net.bettercombat.api.client.BetterCombatClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class StaminaCosts {
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
    public static void onStaminaCyclePre(final StaminaEvent.TickEvent.CycleEvent.Pre event) {
        final var player = event.getPlayer();
        if (player.isSprinting()) {
            event.addBasicDrain();
            event.getStamina().delayRegen();
        }
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

    private static double getAttackSpeedModifier(final ItemStack stack) {
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
        public static void onClientLoggedIn(final ClientPlayerNetworkEvent.LoggingIn event) {
            if (registered)
                return;
            BetterCombatClientEvents.ATTACK_HIT.register(BetterCombatClientHooks::onAttackHit);
            BetterCombatClientEvents.ATTACK_START.register(BetterCombatClientHooks::onAttackStart);
            BetterCombatClientHooks.registered = true;
        }

        /**
         * Delay stamina regeneration upon swinging any weapon.
         */
        private static void onAttackStart(final LocalPlayer player, final AttackHand attackHand) {
            PlayerStamina.get(player).delayRegen();
        }

        /**
         * Apply the swing weapon penalty *only* upon missing an attack.
         * @see StaminaRewards#onHitEnemy
         */
        private static void onAttackHit(final LocalPlayer player, final AttackHand attackHand,
                final List<Entity> targets, @Nullable final Entity cursorTarget) {
            if (targets.isEmpty()) {
                swingWeaponEvent(attackHand.isOffHand() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            }
        }
    }

    /**
     * Drain stamina while gliding.
     */
    @DependentEventBusSubscriber(dependency = Dependency.RELIABLE_GLIDERS)
    public static final class ReliableGlidersHooks {

        @SubscribeEvent
        public static void onStaminaCyclePre(final StaminaEvent.TickEvent.CycleEvent.Pre event) {
            final var player = event.getPlayer();
            if (GlidingState.wasGliding(player)) {
                event.addBasicDrain(MJConfig.STAMINA_GLIDER_DRAIN_MULTIPLIER.get().floatValue());
                event.getStamina().delayRegen();
            }
        }
    }
}
