package com.nekotune.minecraftjourneys.shared.logic.stamina;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

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
                final Player player = level.getPlayerByUUID(playerId);
                if (player != null) {
                    return Optional.of(level.getPlayerByUUID(playerId));
                }
            }
        } else {
            final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(playerId);
                if (serverPlayer != null) {
                    return Optional.of(serverPlayer);
                }
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
     * Returns the player's current stamina.
     */
    public float getValue() {
        return stamina;
    }

    /**
     * Returns the player's maximum stamina.
     */
    public int getMaxValue() {
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
     * Returns whether or not the player's stamina is currently able to regenerate
     * passively.
     */
    public boolean canRegen() {
        return true;
    }

    /**
     * @return True if the player's stamina should be draining.
     */
    public boolean shouldDrain() {
        final var player = player();
        if (player.isEmpty()) return false;
        return player.get().isSprinting();
    }

    /**
     * Temporarily pauses the player's stamina regeneration.
     * @see MJConfig
     */
    public void delayRegen() {
        this.regenCooldownTicks = MJConfig.STAMINA_REGEN_COOLDOWN_TICKS.get();
    }

    /**
     * @see EventHooks
     */
    protected void tick() {
        final var preEvent = new StaminaEvent.TickEvent.Pre(this);
        NeoForge.EVENT_BUS.post(preEvent);
        final float cycle;
        if (shouldDrain()) {
            cycle = -drainRate() / 20.0f;
            delayRegen();
        } else if (canRegen()) {
            if (regenCooldownTicks == 0) {
                cycle = regenRate() / 20.0f;
            } else {
                regenCooldownTicks--;
                cycle = 0f;
            }
        } else {
            delayRegen();
            cycle = 0f;
        }
        stamina = Math.clamp(0f, getMaxValue(), stamina + cycle);
        final var postEvent = new StaminaEvent.TickEvent.Post(this, cycle);
        NeoForge.EVENT_BUS.post(postEvent);
    }

    /**
     * Returns the PlayerStamina object attached to the given player.
     * 
     * @param player The player owner of the returned PlayerStamina object
     * @return PlayerStamina object
     */
    public static PlayerStamina get(final Player player) {
        return MAP.get(player.getUUID());
    }

    /**
     * Returns the PlayerStamina object attached to the given player ID.
     * 
     * @param playerId The UUID of the player owner of the returned PlayerStamina object
     * @return PlayerStamina object
     */
    public static PlayerStamina get(final UUID playerId) {
        return MAP.get(playerId);
    }

    /**
     * @see EventHooks Implementation
     */
    private static void init(final Player player) {
        if (MAP.containsKey(player.getUUID())) return;
        MAP.put(player.getUUID(), new PlayerStamina(player));
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
            PlayerStamina.get(player).tick();
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
}
