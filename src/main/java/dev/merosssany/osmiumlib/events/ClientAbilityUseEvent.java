package dev.merosssany.osmiumlib.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ClientAbilityUseEvent extends PlayerEvent implements ICancellableEvent {
    private final ResourceLocation abilityHolder;
    private final int abilityId;
    
    public ClientAbilityUseEvent(Player player, ResourceLocation abilityHolder, int abilityId) {
        super(player);
        this.abilityHolder = abilityHolder;
        this.abilityId = abilityId;
    }
    
    public int getAbilityId() {
        return abilityId;
    }
    
    public ResourceLocation getAbilityHolder() {
        return abilityHolder;
    }
    
}
