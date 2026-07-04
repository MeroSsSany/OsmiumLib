package dev.merosssany.osmiumlib.mixin;

import dev.merosssany.osmiumlib.events.MaxAirSupplyEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getMaxAirSupply", at = @At("HEAD"), cancellable = true)
    public void osmiumlib$changeMaxAirSupply(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        
        if (entity instanceof LivingEntity living) {
            MaxAirSupplyEntityEvent event = NeoForge.EVENT_BUS.post(new MaxAirSupplyEntityEvent(living, 300));
            cir.setReturnValue(event.getNewOxygen());
        }
    }
}
