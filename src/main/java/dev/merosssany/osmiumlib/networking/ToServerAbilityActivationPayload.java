package dev.merosssany.osmiumlib.networking;

import dev.merosssany.osmiumlib.OsmiumLib;
import dev.merosssany.osmiumlib.OsmiumRegistries;
import dev.merosssany.osmiumlib.attachments.AbilityHolder;
import dev.merosssany.osmiumlib.attachments.OsmiumAttachments;
import dev.merosssany.osmiumlib.util.AbilityHelper;
import dev.merosssany.osmiumlib.util.IAbilityHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ToServerAbilityActivationPayload(int abilityId) implements CustomPacketPayload {
    public static final Type<ToServerAbilityActivationPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(OsmiumLib.MODID, "ability_activation")
    );

    public static final StreamCodec<FriendlyByteBuf, ToServerAbilityActivationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ToServerAbilityActivationPayload::abilityId,
            ToServerAbilityActivationPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public void exec(final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            OsmiumLib.LOGGER.info("Executing ability with id: {}, from player {}", abilityId, player.getGameProfile().getName());
            
            if (player.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES)) {
                AbilityHolder holder = player.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES);
                IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
                
                if (main == null) {
                    OsmiumLib.LOGGER.error("Failed to get ability holder for player {}", player.getGameProfile().getName());
                    return;
                }
                
                if (holder.abilities().get(abilityId).cooldown() > 0) return;
                AbilityHelper.startUseAbility(player, abilityId);
            }
        });
    }
}