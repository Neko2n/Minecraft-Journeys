package com.nekotune.minecraftjourneys.shared.registry.content;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.RegistryHandler.Register;

import net.neoforged.neoforge.registries.DeferredRegister;

public class MJItems {
    /**
     * Deferred register for all items.
     */
    @Register
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MinecraftJourneys.MOD_ID);
    
    /**
     * The items for Cloth Armor.
     */
}
