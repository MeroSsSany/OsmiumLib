package dev.merosssany.osmiumlib.util;

import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompositeAbilityHolder implements IAbilityHolder {
    private final Map<Integer, IAbility> abilities;
    
    public CompositeAbilityHolder(Map<Integer, IAbility> abilities) {
        this.abilities = abilities;
    }
    
    @Override
    public boolean canUse(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        return action != null && action.canUse(caster);
    }
    
    @Override
    public void firstClientApply(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        if (action != null) {
            action.firstClientApply(caster);
        }
    }
    
    @Override
    public void firstServerApply(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        if (action != null) {
            action.firstServerApply(caster);
        }
    }
    
    @Override
    public void use(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        if (action != null) {
            action.use(caster);
        }
    }
    
    @Override
    public void serverEnded(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        if (action != null) {
            action.serverEnded(caster);
        }
    }
    
    @Override
    public void clientEnded(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        if (action != null) {
            action.clientEnded(caster);
        }
    }
    
    @Override
    public boolean cancelIfAlreadyUsing(LivingEntity caster, int ability) {
        IAbility action = abilities.get(ability);
        return action != null && action.cancelIfAlreadyUsing(caster);
    }
    
    @Override
    public List<IAbility> getAbilities() {
        return new ArrayList<>(abilities.values());
    }
    
    public static class Builder {
        private final Map<Integer, IAbility> abilities = new LinkedHashMap<>();
        
        public Builder put(IAbility ability) {
            abilities.put(abilities.size(), ability);
            return this;
        }
        
        public CompositeAbilityHolder build() {
            return new CompositeAbilityHolder(abilities);
        }
    }
}