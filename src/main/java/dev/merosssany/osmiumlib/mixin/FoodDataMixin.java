package dev.merosssany.osmiumlib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.merosssany.osmiumlib.events.PlayerNaturalHealingEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;heal(F)V")
    )
    private void osmiumlib$redirectHeal(Player player, float heal, Operation<Void> original) {
        PlayerNaturalHealingEvent event = NeoForge.EVENT_BUS.post(new PlayerNaturalHealingEvent(player, heal));
        if (event.isCanceled()) return;
        player.heal(event.getHealthAdded());
    }
}
