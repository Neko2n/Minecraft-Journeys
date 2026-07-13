package com.nekotune.minecraftjourneys.data.datagen.maps;

import java.util.Collection;
import java.util.function.Consumer;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

@OnlyIn(value = Dist.DEDICATED_SERVER)
public final class MJDataMapProvider extends DataMapProvider {
    
	public MJDataMapProvider(GatherDataEvent event) {
		super(event.getGenerator().getPackOutput(), event.getLookupProvider());
	}

	@Override
	protected final void gather(Provider provider) {
        final Consumer<SubProvider<?, ?>> build = subProvider -> buildSubProvider(provider, subProvider);
        build.accept(new MJFurnaceFuelsSubProvider());
    }

    private <K, V> void buildSubProvider(Provider provider, SubProvider<K, V> subProvider) {
        subProvider.build(provider, builder(subProvider.mapType()));
    }

    public static abstract class SubProvider<K, V> {
        protected abstract DataMapType<K, V> mapType();
        protected abstract void define(DataMap<K, V> map);
        
        private final void build(Provider provider, Builder<V, K> builder) {
            DataMapType<K, V> mapType = mapType();
            DataMap<K, V> map = new DataMap<>(
                    provider, builder, mapType.registryKey());
            define(map);
            map.builder.build();
        }
    }

    protected static final class DataMap<K, V> {
        private final Provider lookupProvider;
        private final Builder<V, K> builder;
        private final ResourceKey<Registry<K>> registryKey;

        private DataMap(Provider lookupProvider, Builder<V, K> builder, ResourceKey<Registry<K>> registryKey) {
            this.lookupProvider = lookupProvider;
            this.builder = builder;
            this.registryKey = registryKey;
        }

        protected final void add(K key, V value, boolean replace) {
            final Holder<K> holder = lookupProvider.lookupOrThrow(registryKey).listElements()
                .filter(reference -> reference.value() == key)
                .findFirst()
                .orElseThrow();
            builder.add(holder, value, replace);
        }

        protected final void add(Collection<K> keys, V value, boolean replace) {
            keys.forEach(item -> add(item, value, replace));
        }

        protected final void add(TagKey<K> tag, V value, boolean replace) {
            builder.add(tag, value, replace);
        }
    }
}
