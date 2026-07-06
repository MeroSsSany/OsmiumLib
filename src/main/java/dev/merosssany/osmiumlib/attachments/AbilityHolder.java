package dev.merosssany.osmiumlib.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record AbilityHolder(
        ResourceLocation abilityId,
        int level,
        List<Ability> abilities
) {
    public static final Codec<AbilityHolder> CODEC = RecordCodecBuilder.create(in -> in.group(
            ResourceLocation.CODEC.fieldOf("abilityId").forGetter(AbilityHolder::abilityId),
            Codec.INT.fieldOf("level").forGetter(AbilityHolder::level),
            Ability.CODEC.listOf().fieldOf("abilities").forGetter(AbilityHolder::abilities)
    ).apply(in, AbilityHolder::new));
    
    public static AbilityHolder empty() {
        return new AbilityHolder(ResourceLocation.withDefaultNamespace("default"), 1, new ArrayList<>());
    }
}
