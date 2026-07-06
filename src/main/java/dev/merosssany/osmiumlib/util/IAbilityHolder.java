package dev.merosssany.osmiumlib.util;

import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface IAbilityHolder {
    /**
     * Checks if the entity can execute the specified ability.
     */
    boolean canUse(LivingEntity caster, int ability);
    
    default void firstClientApply(LivingEntity caster, int ability) {}
    
    default void firstServerApply(LivingEntity caster, int ability) {}
    
    /**
     * Triggered when the ability command/keybind fires.
     */
    void use(LivingEntity caster, int ability);
    
    default void serverEnded(LivingEntity caster, int ability) {}
    
    default void clientEnded(LivingEntity caster, int ability) {}
    
    default boolean cancelIfAlreadyUsing(LivingEntity caster, int ability) {
        return false;
    }
    
    List<IAbility> getAbilities();
    
    default void firstServerApply(LivingEntity entity) {
        getAbilities().forEach(a -> a.firstClientApply(entity));
    }
}