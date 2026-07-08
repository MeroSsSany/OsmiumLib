package dev.merosssany.osmiumlib.attachments;

import dev.merosssany.osmiumlib.OsmiumLib;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class OsmiumAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, OsmiumLib.MODID);
    
    public static final Supplier<AttachmentType<AbilityHolder>> ENTITY_ABILITY_TYPES = ATTACHMENT_TYPE.register(
            "ability_holder_type",
            () -> AttachmentType.builder(AbilityHolder::empty)
                            .serialize(AbilityHolder.CODEC)
                            .sync(ByteBufCodecs.fromCodec(AbilityHolder.CODEC))
                            .copyOnDeath()
                            .build()
    );
    
    public static void register(IEventBus bus) {
        ATTACHMENT_TYPE.register(bus);
    }
}
