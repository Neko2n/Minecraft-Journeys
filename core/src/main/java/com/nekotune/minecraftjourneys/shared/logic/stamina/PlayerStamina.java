package com.nekotune.minecraftjourneys.shared.logic.stamina;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.joml.Math;

import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.logic.stamina.packet.MaxStaminaPacket;
import com.nekotune.minecraftjourneys.shared.logic.stamina.packet.StaminaPacket;
import com.nekotune.minecraftjourneys.shared.registries.MJAttachmentTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class PlayerStamina {

    public static final class Attachments {
        public static final Supplier<AttachmentType<MaxStaminaPacket.ToClient>> MAX_STAMINA = MJAttachmentTypes.PLAYER_MAX_STAMINA;
    }

    private static final Map<UUID, PlayerStamina> MAP = new HashMap<>();

    public final UUID playerId;
    private final String playerName;
    private float stamina;
    private int maxStamina;
    private float cycle = 0f;
    private Map<String, Cycle> cycleMap = new HashMap<>();
    private float drainRate = 0.05f;
    private float regenRate = 0.05f;
    private int regenCooldownTicks = 0;

    private PlayerStamina(final Player player) {
        this.playerId = player.getUUID();
        this.playerName = player.toString();
        this.maxStamina = player.getData(Attachments.MAX_STAMINA.get()).value();
        this.stamina = this.maxStamina;
    }

    /**
     * @return The player owner of this stamina instance.
     */
    public Optional<Player> player() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            final ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                return Optional.ofNullable(level.getPlayerByUUID(playerId));
            }
        } else {
            final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                return Optional.ofNullable(server.getPlayerList().getPlayer(playerId));
            }
        }
        return Optional.empty();
    }

    private boolean _setValue(final float stamina, final GUIAnimationProperties guiAnimation) {
        final float clamped = Math.clamp(0, getMaxValue(), stamina);
        if (this.stamina == clamped)
            return false;
        final float previousValue = this.stamina;
        this.stamina = clamped;

        // Post event
        final var event = new StaminaEvent.ChangedEvent(this, previousValue, guiAnimation);
        NeoForge.EVENT_BUS.post(event);
        return true;
    }

    /**
     * Sets the player's stamina.
     * Clamps if a value is provided that is < 0, or > maxStamina.
     * Replicates between the server and the client.
     * 
     * @param stamina      The value to set the player's stamina to.
     * @param guiAnimation How this change should render on the stamina GUI.
     */
    public void setValue(final float stamina, final GUIAnimationProperties guiAnimation) {
        if (!_setValue(stamina, guiAnimation)) return;

        // Replicate across the network
        if (FMLEnvironment.dist == Dist.CLIENT) {
            final StaminaPacket.ToServer packet = new StaminaPacket.ToServer(stamina);
            PacketDistributor.sendToServer(packet);
            MinecraftJourneys.LOGGER.debug("[Stamina] Sent StaminaPacket to server");
        } else {
            final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(playerId);
                final StaminaPacket.ToClient packet = new StaminaPacket.ToClient(stamina, guiAnimation.tweenTime());
                PacketDistributor.sendToPlayer(serverPlayer, packet);
                MinecraftJourneys.LOGGER.debug("[Stamina] Sent StaminaPacket to client");
            }
        }
    }

    /**
     * Sets the player's stamina.
     * Clamps if a value is provided that is < 0, or > maxStamina.
     * Replicates between the server and the client.
     * 
     * @param stamina The value to set the player's stamina to.
     */
    public void setValue(final float stamina) {
        setValue(stamina, new GUIAnimationProperties.Builder().build());
    }

    /**
     * Sets the player's maximum stamina.
     * Logs a warning and clamps to 0 if a value is provided that is < 0.
     * Replicates from the server to the client.
     * 
     * @param maxStamina The value to set the player's maximum stamina to.
     */
    public void setMaxValue(final int maxStamina) {
        final int previousValue = this.maxStamina;
        final int clamped = Math.max(0, maxStamina);
        if (maxStamina != clamped) {
            MinecraftJourneys.LOGGER.warn(
                    "Tried to set maxStamina for player " + playerName
                            + " to invalid value " + String.valueOf(maxStamina)
                            + "; clamped to 0.");
        }
        if (this.maxStamina == clamped) return;
        this.maxStamina = clamped;

        // If on server; update client and store to player data
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(playerId);
                final MaxStaminaPacket.ToClient packet = new MaxStaminaPacket.ToClient(this.maxStamina);
                serverPlayer.setData(Attachments.MAX_STAMINA.get(), packet);
                PacketDistributor.sendToPlayer(serverPlayer, packet);
                MinecraftJourneys.LOGGER.debug("[Stamina] Sent MaxStaminaPacket to client");
            }
        }

        // Clamp stamina to the new maximum
        if (this.stamina > this.maxStamina) {
            this.stamina = this.maxStamina;
        }

        // Post event
        final var event = new StaminaEvent.MaxChangedEvent(this, previousValue);
        NeoForge.EVENT_BUS.post(event);
    }

    /**
     * @return The player's current stamina.
     */
    public float getValue() {
        return stamina;
    }

    /**
     * @return The player's maximum stamina.
     */
    public int getMaxValue() {
        return maxStamina;
    }

    /**
     * @return True if the player's current stamina is 0.
     */
    public boolean isEmpty() {
        return stamina == 0f;
    }

    protected float getBaseRegenRate() {
        return regenRate * MJConfig.STAMINA_BASE_REGEN_MULTIPLIER.get().floatValue() / 20f;
    }

    protected float getBaseDrainRate() {
        return drainRate * MJConfig.STAMINA_BASE_DRAIN_MULTIPLIER.get().floatValue() / 20f;
    }

    /**
     * Adds a basic regen cycle to be applied each tick until its removal.
     * @return The removable cycle instance.
     */
    public Cycle addCycleBasicRegen() {
        return new Cycle(this, getBaseRegenRate());
    }

    /**
     * Adds a basic drain cycle to be applied each tick until its removal.
     * @return The removable cycle instance.
     */
    public Cycle addCycleBasicDrain() {
        return new Cycle(this, getBaseDrainRate());
    }

    /**
     * Adds a cycle to be applied each tick until its removal.
     * @param deltaStamina The stamina to be added each tick while this cycle is active.
     * @return The removable cycle instance.
     */
    public Cycle addCycle(final float deltaStamina) {
        return new Cycle(this, deltaStamina);
    }

    /**
     * Maps a cycle object to a given string key, to be indexed later.
     * @param key The string key to map the cycle instance to.
     * @param value The cycle instance to map to the key.
     * @return The passed in cycle instance.
     */
    public Cycle mapCycleToKey(final String key, final Cycle value) {
        cycleMap.put(key, value);
        return value;
    }

    /**
     * Fetches a cycle object using the given map key.
     * @param key The string key to use for lookup.
     * @return The cycle instance mapped to the given key.
     */
    public Optional<Cycle> getCycleFromKey(final String key) {
        final var c = cycleMap.get(key);
        return (c == null) ? Optional.empty() : Optional.of(c);
    }

    /**
     * @return The amount of passive regen that should be applied each tick.
     */
    public float passiveRegen() {
        return this.regenCooldownTicks > 0 ? 0f : getBaseRegenRate();
    }

    /**
     * Temporarily pauses the player's stamina regeneration.
     * @see MJConfig#STAMINA_REGEN_COOLDOWN_TICKS
     */
    public void delayRegen() {
        this.regenCooldownTicks = MJConfig.STAMINA_REGEN_COOLDOWN_TICKS.get();
    }

    /**
     * @see EventHooks#onPlayerTickPost
     */
    protected void tick(final Player player) {
        NeoForge.EVENT_BUS.post(new StaminaEvent.TickEvent.Pre(this, player));
        float deltaStamina = this.cycle;
        deltaStamina += passiveRegen();
        regenCooldownTicks = Math.max(0, regenCooldownTicks - 1);
        final var preCycleEvent = NeoForge.EVENT_BUS.post(
                new StaminaEvent.TickEvent.CycleEvent.Pre(this, player, deltaStamina));
        final float previousStamina = stamina;
        if (preCycleEvent.isCanceled()) {
            deltaStamina = 0f;
        } else {
            deltaStamina = preCycleEvent.getCycleValue();
            stamina = Math.clamp(0f, getMaxValue(), stamina + deltaStamina);
            deltaStamina = stamina - previousStamina;
        }
        NeoForge.EVENT_BUS.post(new StaminaEvent.TickEvent.CycleEvent.Post(this, player, deltaStamina, previousStamina));
        NeoForge.EVENT_BUS.post(new StaminaEvent.TickEvent.Post(this, player, deltaStamina));
    }

    /**
     * Returns the PlayerStamina object attached to the given player.
     * Returns an unlinked dummy instance if the player's stamina isn't initialized.
     * 
     * @param player The player owner of the returned PlayerStamina object
     * @return PlayerStamina object.
     */
    public static PlayerStamina get(final Player player) {
        return MAP.getOrDefault(player.getUUID(), new PlayerStamina(player));
    }

    /**
     * Returns the PlayerStamina object attached to the given player ID.
     * 
     * @param playerId The UUID of the player owner of the returned PlayerStamina object
     * @return PlayerStamina weak reference
     */
    public static Optional<PlayerStamina> get(final UUID playerId) {
        return Optional.ofNullable(MAP.get(playerId));
    }

    /**
     * @see EventHooks Implementation
     */
    private static void init(final Player player) {
        MAP.putIfAbsent(player.getUUID(), new PlayerStamina(player));
    }

    /**
     * @see EventHooks Implementation
     */
    private static void unlink(final Player player) {
        MAP.remove(player.getUUID());
    }

    private static final class PayloadHandler {

        private static void acceptPayload(final MaxStaminaPacket.ToClient data, final IPayloadContext context) {
            final Player player = context.player();
            final PlayerStamina stamina = PlayerStamina.get(player);
            stamina.setMaxValue(data.value());
            MinecraftJourneys.LOGGER.debug("[Stamina] Received MaxStaminaPacket from server");
        }

        private static void acceptPayload(final StaminaPacket.ToClient data, final IPayloadContext context) {
            final Player player = context.player();
            final PlayerStamina stamina = PlayerStamina.get(player);
            stamina._setValue(data.value(),
                    new GUIAnimationProperties.Builder()
                            .length(data.tweenTime())
                            .build());
            MinecraftJourneys.LOGGER.debug("[Stamina] Received StaminaPacket from server");
        }

        private static void acceptPayload(final StaminaPacket.ToServer data, final IPayloadContext context) {
            final Player player = context.player();
            final PlayerStamina stamina = PlayerStamina.get(player);
            stamina.stamina = data.value();
            MinecraftJourneys.LOGGER.debug("[Stamina] Received StaminaPacket from client");
        }
    }

    @EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
    public static final class EventHooks {

        @SubscribeEvent
        public static void onRegisterPayloadHandlers(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(
                    MaxStaminaPacket.ToClient.PAYLOAD.type(),
                    MaxStaminaPacket.ToClient.PAYLOAD.codec(),
                    new MainThreadPayloadHandler<>(PayloadHandler::acceptPayload));
            registrar.playToClient(
                    StaminaPacket.ToClient.PAYLOAD.type(),
                    StaminaPacket.ToClient.PAYLOAD.codec(),
                    new MainThreadPayloadHandler<>(PayloadHandler::acceptPayload));
            registrar.playToServer(
                    StaminaPacket.ToServer.PAYLOAD.type(),
                    StaminaPacket.ToServer.PAYLOAD.codec(),
                    new MainThreadPayloadHandler<>(PayloadHandler::acceptPayload));
        }

        @SubscribeEvent
        public static void onPlayerTickPost(PlayerTickEvent.Post event) {
            final Player player = event.getEntity();
            PlayerStamina.get(player.getUUID())
                    .ifPresent(stamina -> stamina.tick(player));
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
            PlayerStamina.init(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerLoggedOut(final PlayerLoggedOutEvent event) {
            PlayerStamina.unlink(event.getEntity());
        }

        @SubscribeEvent
        public static void onClientPlayerLoggingIn(final ClientPlayerNetworkEvent.LoggingIn event) {
            PlayerStamina.init(event.getPlayer());
        }

        @SubscribeEvent
        public static void onClientPlayerLoggingOut(final ClientPlayerNetworkEvent.LoggingOut event) {
            if (event.getPlayer() == null) return;
            PlayerStamina.unlink(event.getPlayer());
        }
    }

    public static final class Cycle {
        private final Runnable onRemove;

        public Cycle(PlayerStamina stamina, final float amount) {
            onRemove = () -> {
                stamina.cycle -= amount;
            };
            stamina.cycle += amount;
        }

        public void remove() {
            onRemove.run();
        }
    }
}
