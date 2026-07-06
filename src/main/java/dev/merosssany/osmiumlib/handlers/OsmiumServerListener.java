package dev.merosssany.osmiumlib.handlers;

import dev.merosssany.osmiumlib.OsmiumLib;
import dev.merosssany.osmiumlib.OsmiumRegistries;
import dev.merosssany.osmiumlib.attachments.Ability;
import dev.merosssany.osmiumlib.attachments.AbilityHolder;
import dev.merosssany.osmiumlib.attachments.OsmiumAttachments;
import dev.merosssany.osmiumlib.events.ServerAbilityUseEvent;
import dev.merosssany.osmiumlib.networking.ToClientAbilityActivationPayload;
import dev.merosssany.osmiumlib.util.AbilityHelper;
import dev.merosssany.osmiumlib.util.IAbility;
import dev.merosssany.osmiumlib.util.IAbilityHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = OsmiumLib.MODID)
public class OsmiumServerListener {
    
    @SubscribeEvent
    public static void onServerAbilityUse(ServerAbilityUseEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return; // Server enforcement guard
        
        if (entity.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get())) {
            // AbilityHelper handles tracking mutations securely here
            AbilityHelper.startUseAbility(entity, event.getAbilityId());
            
            PacketDistributor.sendToAllPlayers(new ToClientAbilityActivationPayload(event.getAbilityId()));
        }
    }
    
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide() || !(entity instanceof LivingEntity livingEntity)) return;
        
        if (entity.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get())) {
            AbilityHolder holder = entity.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get());
            IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
            if (main == null) return;
            
            List<IAbility> iAbilities = main.getAbilities();
            List<Ability> currentAbilitiesData = holder.abilities();
            List<Ability> updatedAbilities = new ArrayList<>();
            
            int size = Math.min(iAbilities.size(), currentAbilitiesData.size());
            
            for (int i = 0; i < size; i++) {
                IAbility ability = iAbilities.get(i);
                Ability data = currentAbilitiesData.get(i);
                
                ability.passiveServerTicks(livingEntity);
                
                int remainingTime = data.remainingTime();
                int nextRemainingTime = remainingTime;
                int nextCooldown = data.cooldown();
                
                if (remainingTime > 0) {
                    ability.serverTick(livingEntity, remainingTime);
                    
                    if (ability.canContinueUsing(livingEntity)) {
                        nextRemainingTime = remainingTime - 1;
                        if (nextRemainingTime == 0) {
                            // Hit terminal frame naturally, clean up and shift to cooldown
                            ability.serverEnded(livingEntity);
                            nextCooldown = ability.getCooldownTicks(livingEntity);
                        }
                    } else {
                        ability.serverEnded(livingEntity);
                        nextRemainingTime = 0;
                        nextCooldown = ability.getCooldownTicks(livingEntity);
                    }
                } else if (nextCooldown > 0) {
                    nextCooldown--;
                }
                
                updatedAbilities.add(new Ability(nextRemainingTime, nextCooldown, data.id()));
            }
            
            entity.setData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get(), new AbilityHolder(
                    holder.abilityId(), holder.level(), updatedAbilities
            ));
        }
    }
}