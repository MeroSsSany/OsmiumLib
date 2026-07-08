package dev.merosssany.osmiumlib.util;

import net.minecraft.world.entity.LivingEntity;

public interface IAbility {
    /**
     * Checks if the entity can execute the specified ability.
     */
    boolean canUse(LivingEntity caster);
    
    default void firstClientApply(LivingEntity caster) {}
    
    default void firstServerApply(LivingEntity caster) {}
    
    /**
     * Triggered when the ability command/keybind fires.
     */
    void use(LivingEntity caster);
    
    /**
     * Optional: Fired every tick while canContinueUsing returns true.
     * Perfect for handling channeled logic like charging, particle updates, or flight drains.
     */
    default void serverTick(LivingEntity caster, int remainingTicks) {}
    
    default void clientTick(LivingEntity caster, int remainingTicks) {}
    
    /**
     * The total duration (in ticks) that the ability remains active after initiation.
     */
    int getExecutionTicks(LivingEntity caster);
    
    /**
     * The mandatory cooldown period (in ticks) before this specific ability can be cast again.
     */
    int getCooldownTicks(LivingEntity caster);
    
    /**
     * Evaluates if a continuous/channeled ability should keep executing or cut short.
     */
    boolean canContinueUsing(LivingEntity caster);
    
    default void serverEnded(LivingEntity caster) {}
    
    default void clientEnded(LivingEntity caster) {}
    
    default void passiveServerTicks(LivingEntity caster) {}
    
    default void passiveClientTicks(LivingEntity caster) {}
    
    default boolean cancelIfAlreadyUsing(LivingEntity caster) {
        return false;
    }
    
    default void clientRemoved(LivingEntity entity) {}
    
    default void serverRemoved(LivingEntity entity) {}
}
