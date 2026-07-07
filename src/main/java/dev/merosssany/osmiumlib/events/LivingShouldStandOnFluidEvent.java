package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingShouldStandOnFluidEvent extends LivingEvent implements ICancellableEvent {
    private boolean shouldStand;
    private final FluidState fluidState;
    
    public LivingShouldStandOnFluidEvent(LivingEntity entity, FluidState fluidState) {
        super(entity);
        this.fluidState = fluidState;
    }
    
    public FluidState getFluidState() {
        return fluidState;
    }
    
    public boolean isShouldStand() {
        return shouldStand;
    }
    
    public void setShouldStand(boolean shouldStand) {
        this.shouldStand = shouldStand;
    }
}
