package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class MaxAirSupplyEntityEvent extends LivingEvent {
    private final int original;
    private int newOxygen;
    
    public MaxAirSupplyEntityEvent(LivingEntity entity, int original) {
        super(entity);
        this.original = original;
        newOxygen = original;
    }
    
    public int getOriginalOxygen() {
        return original;
    }
    
    public int getNewOxygen() {
        return newOxygen;
    }
    
    public void setNewOxygen(int newOxygen) {
        this.newOxygen = newOxygen;
    }
}
