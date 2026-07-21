package com.nekotune.minecraftjourneys.shared.logic;

import java.util.Optional;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public final class ToolRequirements {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        final Optional<BlockPos> pos = event.getPosition();
        if (!pos.isPresent())
            return;
        final Level level = event.getEntity().level();
        if (!event.getEntity().hasCorrectToolForDrops(event.getState(), level, pos.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        if (player == null)
            return;
        if (player.isCreative())
            return;
        if (player.hasCorrectToolForDrops(
                event.getState(), player.level(), event.getPos()))
            return;
        event.setCanceled(true);
    }
}
