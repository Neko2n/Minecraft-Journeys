package com.nekotune.minecraftjourneys.data.datagen.tags;

import com.farcr.nomansland.common.registry.blocks.NMLBlocks;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.tags.MJBlockTags;
import com.nekotune.minecraftjourneys.shared.registries.content.MJBlocks;

import net.hecco.bountifulfares.registry.tags.BFBlockTags;
import net.hibiscus.naturespirit.registration.NSBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@OnlyIn(value = Dist.DEDICATED_SERVER)
public class MJBlockTagsProvider extends BlockTagsProvider {

    public MJBlockTagsProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    protected void addTags(Provider provider) {

        // Blocks that should drop grass
        tag(MJBlockTags.DROPS_GRASS)
            .add(Blocks.SHORT_GRASS)
            .add(NMLBlocks.SHORT_BEACHGRASS.get())
            .add(NMLBlocks.FROSTED_GRASS.get())
            .add(NMLBlocks.OAT_GRASS.get())
            .add(NSBlocks.OAT_GRASS.get())
            .add(NSBlocks.MELIC_GRASS.get())
            .add(NSBlocks.BEACH_GRASS.get())
            .add(NSBlocks.FRIGID_GRASS.get())
            .add(NSBlocks.SEDGE_GRASS.get())
            .add(NSBlocks.SCORCHED_GRASS.get());
        tag(MJBlockTags.DROPS_GRASS_EXTRA)
            .add(Blocks.TALL_GRASS)
            .add(NMLBlocks.TALL_BEACHGRASS.get())
            .add(NSBlocks.TALL_OAT_GRASS.get())
            .add(NSBlocks.TALL_BEACH_GRASS.get())
            .add(NSBlocks.TALL_FRIGID_GRASS.get())
            .add(NSBlocks.TALL_MELIC_GRASS.get())
            .add(NSBlocks.TALL_SEDGE_GRASS.get())
            .add(NSBlocks.TALL_SCORCHED_GRASS.get());

        // Mining type tags
        tag(MJBlockTags.MINEABLE_WITH_MULTITOOL)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_AXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL);
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(MJBlocks.HANGING_PEAR.get())
            .add(MJBlocks.PEAR_BLOCK.get());

        // Wood tags
        tag(BlockTags.LOGS_THAT_BURN)
            .add(MJBlocks.PEAR_LOG.get())
            .add(MJBlocks.PEAR_WOOD.get())
            .add(MJBlocks.STRIPPED_PEAR_LOG.get())
            .add(MJBlocks.STRIPPED_PEAR_WOOD.get());

        // Bountiful fares tags
        tag(BFBlockTags.HANGING_FRUIT)
            .add(MJBlocks.HANGING_PEAR.get());
        tag(MJBlockTags.PEAR_LEAVES)
            .add(MJBlocks.PEAR_LEAVES.get());
            // .add(MJBlocks.FLOWERING_PEAR_LEAVES.get());
        
        /* Block tags which derive from other tags */
        tag(BlockTags.LEAVES).addTag(MJBlockTags.PEAR_LEAVES);
    }
}
