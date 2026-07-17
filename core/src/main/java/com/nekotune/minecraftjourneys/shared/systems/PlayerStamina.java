package com.nekotune.minecraftjourneys.shared.systems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import org.joml.Math;

import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.attachment.PlayerStaminaStored;
import com.nekotune.minecraftjourneys.shared.hooks.RegisterPayloads;
import com.nekotune.minecraftjourneys.shared.hooks.player.PlayerTick;
import com.nekotune.minecraftjourneys.shared.registries.MJAttachmentTypes;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlayerStamina {
    
    public static final Supplier<AttachmentType<PlayerStaminaStored>> ATTACHMENT_TYPE = MJAttachmentTypes.PLAYER_STAMINA_DATA;
    private static final Map<UUID, PlayerStamina> MAP = new HashMap<>();

    public final Player player;
    private float stamina;
    private int maxStamina;
    private float drainRate = 0.05f;
    private float regenRate = 0.05f;
    protected int regenCooldownTicks = 0;

    protected PlayerStamina(final Player player) {
        this.player = player;
        this.maxStamina = player.getData(ATTACHMENT_TYPE.get()).maxStamina();
        this.stamina = this.maxStamina;
    }

    /**
     * Sets the player's stamina.
     * Will log a warning if a value is provided that is < 0, or > maxStamina.
     */
    public void set(final float stamina) {
        this.stamina = Math.clamp(0, getMax(), stamina);
        if (stamina != this.stamina) {
            MinecraftJourneys.LOGGER.warn("Clamped value of parameter stamina; was " + stamina + ", clamped to " + this.stamina);
        }
    }

    /**
     * Sets the player's maximum stamina.
     * Will log a warning if a value is provided that is < 0.
     */
    public void setMax(final int maxStamina) {
        final int oldValue = this.maxStamina;
        this.maxStamina = maxStamina;
        final PlayerStaminaStored stored = new PlayerStaminaStored(maxStamina);
        player.setData(ATTACHMENT_TYPE.get(), stored);

        // If on server; update client
        if (player instanceof final ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, stored);
        }
        
        // Clamp stamina to the new maximum
        if (this.stamina > this.maxStamina) {
            this.stamina = this.maxStamina;
        }

        // Post event
        final var event = new PlayerStaminaMaxChangedEvent(this, oldValue);
        NeoForge.EVENT_BUS.post(event);
    }

    /**
     * Returns the player's current stamina.
     */
    public float get() {
        return stamina;
    }

    /**
     * Returns the player's maximum stamina.
     */
    public int getMax() {
        return maxStamina;
    }

    /**
     * Returns the per-second rate that stamina should regenerate at.
     */
    public float regenRate() {
        return regenRate * MJConfig.STAMINA_REGEN_MULTIPLIER.get().floatValue();
    }

    /**
     * Returns the per-second rate that stamina should drain at.
     */
    public float drainRate() {
        return drainRate * MJConfig.STAMINA_DRAIN_MULTIPLIER.get().floatValue();
    }

    /**
     * Returns whether or not the player's stamina is currently able to regenerate passively.
     */
    protected boolean canRegen() {
        return true;
    }

    /**
     * Returns true if the player's stamina should be draining.
     * @return
     */
    protected boolean shouldDrain() {
        return player.isSprinting();
    }

    private void resetRegenCooldown() {
        this.regenCooldownTicks = MJConfig.STAMINA_REGEN_COOLDOWN_TICKS.get();
    }

    /**
     * @see PlayerTick
     */
    public void tick() {
        if (this.shouldDrain()) {
            this.set(Math.max(0, this.get() - this.drainRate() / 20.0f));
            this.resetRegenCooldown();
        } else if (this.canRegen()) {
            if (regenCooldownTicks == 0) {
                this.set(Math.min(this.getMax(), this.get() + this.regenRate() / 20.0f));
            } else {
                regenCooldownTicks--;
            }
        } else {
            this.resetRegenCooldown();
        }
    }

    /**
     * Returns the PlayerStamina object attached to the given player.
     * @param player The player owner of the returned PlayerStamina object
     * @return PlayerStamina object
     */
    public static PlayerStamina get(final Player player) {
        return MAP.get(player.getUUID());
    }

    /**
     * @see EventHandler
     */
    private static void init(final Player player) {
        MAP.put(player.getUUID(), new PlayerStamina(player));
    }

    /**
     * @see EventHandler
     */
    private static void unlink(final Player player) {
        MAP.remove(player.getUUID());
    }

    /**
     * Handles data sent from the server to the client.
     * @see RegisterPayloads
     */
    public static class ClientPayloadHandler {

        public static void acceptPayload(final PlayerStaminaStored data, final IPayloadContext context) {
            final Player player = context.player();
            final PlayerStamina stamina = PlayerStamina.get(player);
            stamina.setMax(data.maxStamina());
        }
    }

    /**
     * Fired when the player's maximum stamina changes.
     * @see PlayerStamina
     */
    public static class PlayerStaminaMaxChangedEvent extends Event {
        private final PlayerStamina playerStamina;
        private final int oldValue;

        public PlayerStaminaMaxChangedEvent(final PlayerStamina playerStamina, final int oldValue) {
            this.playerStamina = playerStamina;
            this.oldValue = oldValue;
        }

        public PlayerStamina getStamina() {
            return playerStamina;
        }

        public Player getEntity() {
            return playerStamina.player;
        }

        public int oldValue() {
            return oldValue;
        }
    }

    @EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
    public static final class EventHandler {
        
        @SubscribeEvent
        public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
            init(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerLoggedOut(final PlayerLoggedOutEvent event) {
            unlink(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerClone(final PlayerEvent.Clone event) {
            unlink(event.getOriginal());
            init(event.getEntity());
        }
    }
}
