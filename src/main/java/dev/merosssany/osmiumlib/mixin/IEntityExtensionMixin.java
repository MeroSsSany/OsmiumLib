package dev.merosssany.osmiumlib.mixin;

import dev.merosssany.osmiumlib.events.LivingShouldSwimInLiquidEvent;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IEntityExtension.class)
public interface IEntityExtensionMixin {
    @Inject(
            method = "canSwimInFluidType",
            at = @At("HEAD"),
            cancellable = true
    )
    default void osmiumlib$swimInLiquid(FluidType type, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof LivingEntity entity) {
            LivingShouldSwimInLiquidEvent event = NeoForge.EVENT_BUS.post(new LivingShouldSwimInLiquidEvent(entity, type));
            if (event.isCanceled()) {
                cir.setReturnValue(event.isShouldSwim());
            }
        }
    }
}
