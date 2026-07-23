package com.nekotune.minecraftjourneys.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.MinecraftJourneys.Dependency;

import net.neoforged.fml.loading.LoadingModList;

public abstract class DependentMixin implements IMixinConfigPlugin {

    protected abstract Dependency dependency();

    @Override
    public final boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        final String id = MinecraftJourneys.DEPENDENCIES.get(dependency()).MOD_ID;
        return LoadingModList.get().getModFileById(id) != null;
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
