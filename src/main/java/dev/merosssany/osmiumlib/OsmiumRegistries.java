package dev.merosssany.osmiumlib;

import dev.merosssany.osmiumlib.util.IAbilityHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import java.util.Objects;

public class OsmiumRegistries {
    
    public static final ResourceKey<Registry<IAbilityHolder>> ABILITY_HOLDER_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath(OsmiumLib.MODID, "ability_holders")
    );
    
    // Backing field hidden from direct manipulation
    private static Registry<IAbilityHolder> abilityHolders;
    
    /**
     * Internal framework setter used during NewRegistryEvent.
     */
    static void setAbilityHolders(Registry<IAbilityHolder> registry) {
        abilityHolders = registry;
    }
    
    /**
     * Public access gateway for DeferredRegisters and custom lookup operations.
     */
    public static Registry<IAbilityHolder> getAbilityHolder() {
        return Objects.requireNonNull(abilityHolders, "OsmiumLib: Attempted to access ABILITY_HOLDER registry before initialization lifecycle completed!");
    }
}