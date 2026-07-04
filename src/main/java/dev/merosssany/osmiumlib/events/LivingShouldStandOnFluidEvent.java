package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingShouldStandOnFluidEvent extends LivingEvent implements ICancellableEvent {
    private boolean shouldStand;
    
    public LivingShouldStandOnFluidEvent(LivingEntity entity) {
        super(entity);
    }
    
    public boolean isShouldStand() {
        return shouldStand;
    }
    
    public void setShouldStand(boolean shouldStand) {
        this.shouldStand = shouldStand;
    }
}
