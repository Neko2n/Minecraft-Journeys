package com.nekotune.minecraftjourneys.mixin.reliablegliders;

import com.nekotune.minecraftjourneys.MinecraftJourneys.Dependency;
import com.nekotune.minecraftjourneys.mixin.DependentMixin;

public class ReliableGlidersMixinPlugin extends DependentMixin {

    @Override
    protected Dependency dependency() {
        return Dependency.RELIABLE_GLIDERS;
    }
}
