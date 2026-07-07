package dev.merosssany.osmiumlib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.merosssany.osmiumlib.events.LivingInvertedEffectsEvent;
import dev.merosssany.osmiumlib.events.LivingSensitiveToWaterEvent;
import dev.merosssany.osmiumlib.events.LivingShouldStandOnFluidEvent;
import dev.merosssany.osmiumlib.events.LivingSwimSpeedEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForge;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "aiStep",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isInPowderSnow:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    public boolean osmiumlib_fix$isFreezing(LivingEntity instance, Operation<Boolean> original) {
        return instance.isFreezing();
    }
    
    @Inject(
            method = "isSensitiveToWater",
            at = @At("HEAD"),
            cancellable = true
    )
    public void osmiumlib$sensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        LivingSensitiveToWaterEvent event = NeoForge.EVENT_BUS.post(new LivingSensitiveToWaterEvent((LivingEntity)(Object)this));
        if (event.isCanceled()) {
            cir.setReturnValue(event.isSensitive());
        }
    }
    
    @Inject(
            method = "canStandOnFluid",
            at = @At("HEAD"),
            cancellable = true
    )
    public void osmiumlib$standOnLiquid(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        LivingShouldStandOnFluidEvent event = NeoForge.EVENT_BUS.post(new LivingShouldStandOnFluidEvent((LivingEntity)(Object)this, fluidState));
        if (event.isCanceled()) {
            cir.setReturnValue(event.isShouldStand());
        }
    }
    
    @Inject(
            method = "isInvertedHealAndHarm",
            at = @At("HEAD"),
            cancellable = true
    )
    public void osmiumlib$invertHeal(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        LivingInvertedEffectsEvent event = NeoForge.EVENT_BUS.post(new LivingInvertedEffectsEvent(entity));
        if (event.isCanceled()) {
            cir.setReturnValue(event.isInvert());
        }
    }
    
    @WrapOperation(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"
            )
    )
    public boolean osmiumlib$swimInLava(LivingEntity entity, Operation<Boolean> original) {
        LivingSwimSpeedEvent event = NeoForge.EVENT_BUS.post(new LivingSwimSpeedEvent.WaterPass(entity));
        if (event.isCanceled()) {
            return event.isShouldSwimFaster();
        }
        
        return original.call(entity);
    }
    
    @WrapOperation(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z"
            )
    )
    public boolean osmiumlib$speedupInLava(LivingEntity entity, Operation<Boolean> original) {
        LivingSwimSpeedEvent event = NeoForge.EVENT_BUS.post(new LivingSwimSpeedEvent.LavaPass(entity));
        
        if (event.isCanceled()) {
            return event.isShouldSwimFaster();
        }
        
        return original.call(entity);
    }
}
