package dev.merosssany.osmiumlib;

import com.mojang.logging.LogUtils;
import dev.merosssany.osmiumlib.attachments.OsmiumAttachments;
import dev.merosssany.osmiumlib.networking.OsmiumNetworkingManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

@Mod(OsmiumLib.MODID)
public class OsmiumLib {
    public static final String MODID = "osmiumlib";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public OsmiumLib(IEventBus bus) {
        bus.addListener(this::registerRegistries);
        OsmiumNetworkingManager.register(bus);
        OsmiumAttachments.register(bus);
    }
    
    private void registerRegistries(NewRegistryEvent e) {
        OsmiumRegistries.setAbilityHolders(e.create(
                new RegistryBuilder<>(OsmiumRegistries.ABILITY_HOLDER_KEY)
                        .sync(true)
                        .maxId(11000)
        ));
    }
}
