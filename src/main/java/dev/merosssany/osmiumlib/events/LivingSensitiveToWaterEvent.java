package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingSensitiveToWaterEvent extends LivingEvent implements ICancellableEvent {
    private boolean sensitive = false;
    
    public LivingSensitiveToWaterEvent(LivingEntity entity) {
        super(entity);
    }
    
    public boolean isSensitive() {
        return sensitive;
    }
    
    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }
}
