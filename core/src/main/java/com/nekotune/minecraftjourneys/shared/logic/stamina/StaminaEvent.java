package com.nekotune.minecraftjourneys.shared.logic.stamina;

import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract sealed class StaminaEvent extends Event {
    private final PlayerStamina playerStamina;

    protected StaminaEvent(final PlayerStamina playerStamina) {
        this.playerStamina = playerStamina;
    }
    
    public PlayerStamina getStamina() {
        return playerStamina;
    }
    
    public UUID getPlayerId() {
        return playerStamina.playerId;
    }

    /**
     * Fired when the player's stamina is first initialized.
     * @see PlayerStamina#init
     */
    public static final class InitEvent extends StaminaEvent {
    
        public InitEvent(final PlayerStamina playerStamina) {
            super(playerStamina);
        }
    }

    /**
     * Fired when the player's maximum stamina changes.
     * @see PlayerStamina#setMaxValue
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
     * Fired when the player's stamina is set using setValue.
     * @see PlayerStamina#setValue
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
     * @see PlayerStamina#tick
     */
    public static abstract sealed class TickEvent extends StaminaEvent {
        private final Player player;

        public TickEvent(final PlayerStamina playerStamina, final Player player) {
            super(playerStamina);
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        public static final class Pre extends TickEvent {
            public Pre(final PlayerStamina playerStamina, final Player player) {
                super(playerStamina, player);
            }
        }

        public static final class Post extends TickEvent {
            private final float deltaStamina;
        
            public Post(final PlayerStamina playerStamina, final Player player, final float deltaStamina) {
                super(playerStamina, player);
                this.deltaStamina = deltaStamina;
            }
        
            public float getDeltaStaminaValue() {
                return deltaStamina;
            }
        }

        
        /**
         * Fired when draining or regenerating the player's stamina.
         * @see PlayerStamina#tick
         */
        public static abstract sealed class CycleEvent extends TickEvent {
        
            public CycleEvent(final PlayerStamina playerStamina, final Player player) {
                super(playerStamina, player);
            }
            
            protected abstract float getCycleValue();

            public static final class Pre extends CycleEvent implements ICancellableEvent {
                private final float cycle;
                private float add = 0f;
                private float drainMul = 1f;
                private float regenMul = 1f;

                public Pre(final PlayerStamina playerStamina, final Player player,
                            final float cycle) {
                    super(playerStamina, player);
                    this.cycle = cycle;
                }

                public void addDrain(final float absAmount) {
                    add -= Math.abs(absAmount);
                }

                public void addBasicDrain(final float absMultiplier) {
                    addDrain(this.getStamina().getBaseDrainRate() * absMultiplier);
                }

                public void addBasicDrain() {
                    addBasicDrain(1f);
                }

                public void addRegen(final float absAmount) {
                    add += Math.abs(absAmount);
                }

                public void addBasicRegen(final float absMultiplier) {
                    addRegen(this.getStamina().getBaseRegenRate() * absMultiplier);
                }

                public void addBasicRegen() {
                    addBasicRegen(1f);
                }

                public void multiplyIfDrain(final float absMultiplier) {
                    drainMul *= Math.abs(absMultiplier);
                }

                public void multiplyIfRegen(final float absMultiplier) {
                    regenMul *= Math.abs(absMultiplier);
                }

                @Override
                protected float getCycleValue() {
                    float value = cycle + add;
                    if (value > 0f) {
                        value *= regenMul;
                    } else {
                        value *= drainMul;
                    }
                    return value;
                }
            }

            public static final class Post extends CycleEvent {
                private final float cycle;
                private final float previousStamina;

                public Post(final PlayerStamina playerStamina, final Player player,
                        final float cycle, final float previousStamina) {
                    super(playerStamina, player);
                    this.cycle = cycle;
                    this.previousStamina = previousStamina;
                }
                
                @Override
                public float getCycleValue() {
                    return cycle;
                }

                public float getPreviousStaminaValue() {
                    return this.previousStamina;
                }
            }
        }
    }
}