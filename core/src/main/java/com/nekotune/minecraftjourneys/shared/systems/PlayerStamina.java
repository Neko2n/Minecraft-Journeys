package com.nekotune.minecraftjourneys.shared.systems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.joml.Math;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.attachment.PlayerStaminaStored;
import com.nekotune.minecraftjourneys.shared.registries.MJAttachmentTypes;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlayerStamina {
    
    public static final Supplier<AttachmentType<PlayerStaminaStored>> ATTACHMENT_TYPE = MJAttachmentTypes.PLAYER_STAMINA_DATA;
    private static final Map<Player, PlayerStamina> MAP = new HashMap<>();

    public final Player player;
    private float stamina;
    private int maxStamina;

    protected PlayerStamina(Player player) {
        this.player = player;
        this.maxStamina = player.getData(ATTACHMENT_TYPE.get()).maxStamina();
        this.stamina = this.maxStamina;
    }

    public void set(float stamina) {
        this.stamina = Math.clamp(0, getMax(), stamina);
        if (stamina != this.stamina) {
            MinecraftJourneys.LOGGER.warn("Clamped value of parameter stamina; was " + stamina + ", clamped to " + this.stamina);
        }
    }

    public void setMax(int maxStamina) {
        this.maxStamina = maxStamina;
        final PlayerStaminaStored stored = new PlayerStaminaStored(maxStamina);
        player.setData(ATTACHMENT_TYPE.get(), stored);

        // If on server; update client
        if (player instanceof final ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, stored);
        }
    }

    public float get() {
        return stamina;
    }

    public int getMax() {
        return maxStamina;
    }

    /**
     * Returns the PlayerStamina object attached to the given player.
     * @param player The player owner of the returned PlayerStamina object
     * @return PlayerStamina object
     */
    public static PlayerStamina get(Player player) {
        return MAP.get(player);
    }

    /**
     * Links a new PlayerStamina object to the given player.
     * @param player The player to initialize a new PlayerStamina object for
     */
    public static void init(Player player) {
        MAP.put(player, new PlayerStamina(player));
    }

    /**
     * Unlinks an existing PlayerStamina object from its player.
     * @param player The player whose PlayerStamina should be unlinked
     */
    public static void unlink(Player player) {
        MAP.remove(player);
    }

    public static class ClientPayloadHandler {

        public static void acceptPayload(final PlayerStaminaStored data, final IPayloadContext context) {
            final Player player = context.player();
            final PlayerStamina stamina = PlayerStamina.get(player);
            stamina.setMax(data.maxStamina());
        }
    }
}
