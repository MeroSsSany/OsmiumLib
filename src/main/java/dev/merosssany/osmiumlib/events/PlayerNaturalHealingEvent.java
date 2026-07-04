package dev.merosssany.osmiumlib.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerNaturalHealingEvent extends PlayerEvent implements ICancellableEvent {
    private final float original;
    private float healthAdded;
    
    public PlayerNaturalHealingEvent(Player player, float healValue) {
        super(player);
        this.original = healValue;
        this.healthAdded = healValue;
    }
    
    public float getOriginal() {
        return original;
    }
    
    public float getHealthAdded() {
        return healthAdded;
    }
    
    public void setHealthAdded(float healthAdded) {
        this.healthAdded = healthAdded;
    }
}
