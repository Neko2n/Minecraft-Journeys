package com.nekotune.minecraftjourneys.shared.logic.stamina.hooks;

import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.logic.stamina.GUIAnimationProperties;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class StaminaRewards {
    
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
}
