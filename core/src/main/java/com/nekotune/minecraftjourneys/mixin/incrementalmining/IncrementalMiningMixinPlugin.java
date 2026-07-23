package com.nekotune.minecraftjourneys.mixin.incrementalmining;

import com.nekotune.minecraftjourneys.MinecraftJourneys.Dependency;
import com.nekotune.minecraftjourneys.mixin.DependentMixin;

public class IncrementalMiningMixinPlugin extends DependentMixin {

    @Override
    protected Dependency dependency() {
        return Dependency.INCREMENTAL_MINING;
    }
}
