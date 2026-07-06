package dev.merosssany.osmiumlib.networking;

import dev.merosssany.osmiumlib.OsmiumLib;
import dev.merosssany.osmiumlib.OsmiumRegistries;
import dev.merosssany.osmiumlib.attachments.AbilityHolder;
import dev.merosssany.osmiumlib.attachments.OsmiumAttachments;
import dev.merosssany.osmiumlib.util.AbilityHelper;
import dev.merosssany.osmiumlib.util.IAbilityHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ToClientAbilityActivationPayload(int abilityId) implements CustomPacketPayload {
    public static final Type<ToClientAbilityActivationPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(OsmiumLib.MODID, "ability_activation")
    );

    public static final StreamCodec<FriendlyByteBuf, ToClientAbilityActivationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ToClientAbilityActivationPayload::abilityId,
            ToClientAbilityActivationPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public void exec(final IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            if (player.hasData(OsmiumAttachments.ENTITY_ABILITY_TYPES)) {
                AbilityHolder holder = player.getData(OsmiumAttachments.ENTITY_ABILITY_TYPES);
                IAbilityHolder main = OsmiumRegistries.getAbilityHolder().get(holder.abilityId());
                
                if (main == null) {
                    OsmiumLib.LOGGER.error("Failed to get ability holder for player {}", player.getGameProfile().getName());
                    return;
                }
                
                if (holder.abilities().get(abilityId).cooldown() <= 0) return;
                AbilityHelper.startUseAbility(player, abilityId);
            }
        });
    }
}