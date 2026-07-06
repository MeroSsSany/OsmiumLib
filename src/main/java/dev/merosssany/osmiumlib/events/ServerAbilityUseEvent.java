package dev.merosssany.osmiumlib.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class ServerAbilityUseEvent extends LivingEvent implements ICancellableEvent {
    private final ResourceLocation abilityHolder;
    private final int abilityId;
    
    public ServerAbilityUseEvent(LivingEntity entity, ResourceLocation abilityHolder, int abilityId) {
        super(entity);
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
