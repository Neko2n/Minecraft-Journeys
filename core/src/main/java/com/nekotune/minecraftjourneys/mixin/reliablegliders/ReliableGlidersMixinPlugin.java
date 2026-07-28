package com.nekotune.minecraftjourneys.mixin.reliablegliders;

import com.nekotune.minecraftjourneys.MJDependency;
import com.nekotune.minecraftjourneys.mixin.DependentMixin;

public class ReliableGlidersMixinPlugin extends DependentMixin {

    @Override
    protected MJDependency dependency() {
        return MJDependency.RELIABLE_GLIDERS;
    }
}
