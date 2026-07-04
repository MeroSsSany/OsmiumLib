package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingInvertedEffectsEvent extends LivingEvent implements ICancellableEvent {
    private boolean invert;
    
    public LivingInvertedEffectsEvent(LivingEntity entity) {
        super(entity);
    }
    
    public boolean isInvert() {
        return invert;
    }
    
    public void setInvert(boolean invert) {
        this.invert = invert;
    }
}
