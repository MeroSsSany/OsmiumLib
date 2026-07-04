package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.fluids.FluidType;

public class LivingShouldSwimInLiquidEvent extends LivingEvent implements ICancellableEvent {
    private final FluidType fluidTYpe;
    private boolean shouldSwim;
    
    public LivingShouldSwimInLiquidEvent(LivingEntity entity, FluidType fluidType) {
        super(entity);
        this.fluidTYpe = fluidType;
    }
    
    public FluidType getFluidTYpe() {
        return fluidTYpe;
    }
    
    public boolean isShouldSwim() {
        return shouldSwim;
    }
    
    public void setShouldSwim(boolean shouldSwim) {
        this.shouldSwim = shouldSwim;
    }
}
