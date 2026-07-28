package com.nekotune.minecraftjourneys.mixin.incrementalmining;

import com.nekotune.minecraftjourneys.MJDependency;
import com.nekotune.minecraftjourneys.mixin.DependentMixin;

public class IncrementalMiningMixinPlugin extends DependentMixin {

    @Override
    protected MJDependency dependency() {
        return MJDependency.INCREMENTAL_MINING;
    }
}
