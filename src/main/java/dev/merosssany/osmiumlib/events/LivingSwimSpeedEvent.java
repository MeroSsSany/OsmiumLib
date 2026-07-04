package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingSwimSpeedEvent extends LivingEvent implements ICancellableEvent {
    private boolean shouldSwimFaster;
    
    public LivingSwimSpeedEvent(LivingEntity entity) {
        super(entity);
    }
    
    public boolean isShouldSwimFaster() {
        return shouldSwimFaster;
    }
    
    public void setShouldSwimFaster(boolean shouldSwimFaster) {
        this.shouldSwimFaster = shouldSwimFaster;
    }
    
    public static class WaterPass extends LivingSwimSpeedEvent {
        public WaterPass(LivingEntity entity) {
            super(entity);
        }
    }
    
    public static class LavaPass extends LivingSwimSpeedEvent {
        public LavaPass(LivingEntity entity) {
            super(entity);
        }
    }
}
