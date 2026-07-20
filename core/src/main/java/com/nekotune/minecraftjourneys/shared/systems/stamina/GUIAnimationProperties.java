package com.nekotune.minecraftjourneys.shared.systems.stamina;

public final record GUIAnimationProperties(float tweenTime) {

    public static final class Builder {
        private float tweenTime = 0f;

        /**
         * Sets how long this animation takes to finish.
         * @param length The length of time, in seconds.
         * @return Builder
         */
        public Builder length(final float seconds) {
            this.tweenTime = seconds;
            return this;
        }

        /**
         * Sets how long this animation takes to finish.
         * @param length The length of time, in ticks.
         * @return Builder
         */
        public Builder length(final int ticks) {
            this.tweenTime = ticks/20;
            return this;
        }

        public GUIAnimationProperties build() {
            return new GUIAnimationProperties(tweenTime);
        }
    }
}