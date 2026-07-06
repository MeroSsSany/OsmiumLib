package dev.merosssany.osmiumlib.util;

import dev.merosssany.osmiumlib.OsmiumLib;
import dev.merosssany.osmiumlib.OsmiumRegistries;
import dev.merosssany.osmiumlib.attachments.Ability;
import dev.merosssany.osmiumlib.attachments.AbilityHolder;
import dev.merosssany.osmiumlib.attachments.OsmiumAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.*;

@EventBusSubscriber(modid = OsmiumLib.MODID)
public class AbilityHelper {
    private static final Map<ResourceLocation, ResourceLocation> ENTITY_ABILITY = new HashMap<>();
    
    public static void endUseAbility(LivingEntity caster, int ability) {
        if (!caster.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get())) return;
        
        AbilityHolder holder = caster.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get());
        IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
        if (main == null) {
            OsmiumLib.LOGGER.error("End use failure: Resource {} is not found in the registry", holder.abilityId());
            return;
        }
        
        if (caster.level().isClientSide()) {
            main.clientEnded(caster, ability);
        } else {
            main.serverEnded(caster, ability);
        }
        
        caster.setData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get(), new AbilityHolder(
                holder.abilityId(),
                holder.level(),
                create(main.getAbilities(), caster)
        ));
    }
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        ResourceLocation registryKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (!(entity instanceof LivingEntity living)) return;
        
        if (ENTITY_ABILITY.containsKey(registryKey)) {
            entity.setData(OsmiumAttachments.ENTITY_ABILITY_TYPES, create(ENTITY_ABILITY.get(registryKey), 1, living));
        }
    }
    
    public static void register(ResourceLocation entity, ResourceLocation holder) {
        ENTITY_ABILITY.put(entity, holder);
    }
    
    public static List<Ability> create(List<IAbility> list, LivingEntity caster) {
        List<Ability> result = new ArrayList<>();
        
        int i = 0;
        for (IAbility ability : list) {
            result.add(new Ability(ability.getExecutionTicks(caster),ability.getCooldownTicks(caster),i));
            i++;
        }
        
        return result;
    }
    
    public static AbilityHolder create(ResourceLocation id, int level, LivingEntity entity) {
        return new AbilityHolder(id, level, create(Objects.requireNonNull(OsmiumRegistries.getAbilityHolder().get(id), "Registry \""+id+"\" does not exists.").getAbilities(), entity));
    }
    
    public static void startUseAbility(LivingEntity caster, int abilityId) {
        if (!caster.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get())) return;
        
        AbilityHolder holder = caster.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get());
        IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
        if (main == null) {
            OsmiumLib.LOGGER.error("Start use failure: Resource {} is not found in the registry", holder.abilityId());
            return;
        }
        
        if (holder.abilities().get(abilityId).remainingTime() > 0 && main.cancelIfAlreadyUsing(caster, abilityId)) {
            endUseAbility(caster, abilityId);
            return;
        }
        
        if (caster.level().isClientSide()) {
            main.firstClientApply(caster, abilityId);
            return; // Client predictive feedback finishes here
        }
        
        main.use(caster, abilityId);
        
        caster.setData(OsmiumAttachments.ENTITY_ABILITY_TYPES.get(), new AbilityHolder(
                holder.abilityId(),
                holder.level(),
                create(main.getAbilities(), caster)
        ));
    }
    
    public static boolean canUse(LivingEntity entity, int ability, AbilityHolder holder) {
        IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
        if (main == null) return false;
        Ability a = holder.abilities().get(ability);
        return a.cooldown() <= 0 && ((a.remainingTime() > 0 && main.cancelIfAlreadyUsing(entity, ability)));
    }
    
    public static void apply(LivingEntity entity, AbilityHolder ability) {
        entity.setData(OsmiumAttachments.ENTITY_ABILITY_TYPES, ability);
        
        if (entity.level().isClientSide()) Objects.requireNonNull(OsmiumRegistries.getAbilityHolder().get(ability.abilityId())).firstServerApply(entity);
    }
}