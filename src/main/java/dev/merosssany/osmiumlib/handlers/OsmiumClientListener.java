package dev.merosssany.osmiumlib.handlers;

import dev.merosssany.osmiumlib.OsmiumLib;
import dev.merosssany.osmiumlib.OsmiumRegistries;
import dev.merosssany.osmiumlib.attachments.Ability;
import dev.merosssany.osmiumlib.attachments.AbilityHolder;
import dev.merosssany.osmiumlib.attachments.OsmiumAttachments;
import dev.merosssany.osmiumlib.events.ClientAbilityUseEvent;
import dev.merosssany.osmiumlib.networking.ToServerAbilityActivationPayload;
import dev.merosssany.osmiumlib.util.AbilityHelper;
import dev.merosssany.osmiumlib.util.IAbility;
import dev.merosssany.osmiumlib.util.IAbilityHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = OsmiumLib.MODID, value = Dist.CLIENT)
public class OsmiumClientListener {
    
    @SubscribeEvent
    public static void onClientAbilityUse(ClientAbilityUseEvent event) {
        Player player = event.getEntity();
        
        if (player.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get())) {
            AbilityHolder main = player.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get());
            
            if (AbilityHelper.canUse(player, event.getAbilityId(), main)) {
                AbilityHelper.startUseAbility(player, event.getAbilityId());
                PacketDistributor.sendToServer(new ToServerAbilityActivationPayload(event.getAbilityId()));
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;
        
        if (entity.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get())) {
            AbilityHolder holder = entity.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get());
            IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
            if (main == null) return;
            
            List<IAbility> iAbilities = main.getAbilities();
            List<Ability> currentAbilitiesData = holder.abilities();
            
            int size = Math.min(iAbilities.size(), currentAbilitiesData.size());
            for (int i = 0; i < size; i++) {
                IAbility ability = iAbilities.get(i);
                Ability data = currentAbilitiesData.get(i);
                
                ability.passiveClientTicks(livingEntity);
                
                int remainingTime = data.remainingTime();
                if (remainingTime > 0) {
                    ability.clientTick(livingEntity, remainingTime);
                    
                    if (!ability.canContinueUsing(livingEntity) || remainingTime == 1) {
                        ability.clientEnded(livingEntity);
                    }
                }
            }
        }
    }
}