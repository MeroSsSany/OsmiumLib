package dev.merosssany.osmiumlib.networking;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class OsmiumNetworkingManager {
    public static void register(IEventBus bus) {
        bus.addListener(OsmiumNetworkingManager::registerPackets);
    }
    
    private static void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        
        registrar.playToServer(
                ToServerAbilityActivationPayload.TYPE,
                ToServerAbilityActivationPayload.STREAM_CODEC,
                ToServerAbilityActivationPayload::exec
        );
        
        registrar.playToClient(
                ToClientAbilityActivationPayload.TYPE,
                ToClientAbilityActivationPayload.STREAM_CODEC,
                ToClientAbilityActivationPayload::exec
        );
    }
}
