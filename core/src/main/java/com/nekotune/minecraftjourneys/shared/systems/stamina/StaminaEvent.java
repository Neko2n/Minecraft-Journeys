package com.nekotune.minecraftjourneys.shared.systems.stamina;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public abstract sealed class StaminaEvent extends Event {
    private final PlayerStamina playerStamina;

    protected StaminaEvent(final PlayerStamina playerStamina) {
        this.playerStamina = playerStamina;
    }
    
    public PlayerStamina getStamina() {
        return playerStamina;
    }
    
    public Optional<Player> getPlayer() {
        return playerStamina.player();
    }
    
    public UUID getPlayerId() {
        return playerStamina.playerId;
    }

    /**
     * Fired when the player's maximum stamina changes.
     * @see PlayerStamina
     */
    public static final class MaxChangedEvent extends StaminaEvent {
        private final int previousValue;
    
        public MaxChangedEvent(final PlayerStamina playerStamina, final int previousValue) {
            super(playerStamina);
            this.previousValue = previousValue;
        }
    
        public int getPreviousValue() {
            return previousValue;
        }
    }

    /**
     * Fired when the player's stamina is set using setValueEffect.
     * @see PlayerStamina
     */
    public static final class ChangedEvent extends StaminaEvent {
        private final float previousValue;
        private final GUIAnimationProperties guiAnimation;
    
        public ChangedEvent(final PlayerStamina playerStamina,
                final float previousValue, final GUIAnimationProperties guiAnimation) {
            super(playerStamina);
            this.previousValue = previousValue;
            this.guiAnimation = guiAnimation;
        }
    
        public float getPreviousValue() {
            return previousValue;
        }
    
        public GUIAnimationProperties getGuiAnimation() {
            return guiAnimation;
        }
    }
    
    /**
     * Fired before and after PlayerStamina::tick.
     * @see PlayerStamina
     */
    public static abstract sealed class TickEvent extends StaminaEvent {
        public TickEvent(final PlayerStamina playerStamina) {
            super(playerStamina);
        }

        public static final class Pre extends TickEvent {
            public Pre(final PlayerStamina playerStamina) {
                super(playerStamina);
            }
        }

        public static final class Post extends TickEvent {
            private final float deltaStamina;
        
            public Post(final PlayerStamina playerStamina, final float deltaStamina) {
                super(playerStamina);
                this.deltaStamina = deltaStamina;
            }
        
            public float getDeltaStaminaValue() {
                return deltaStamina;
            }
        }
    }
}