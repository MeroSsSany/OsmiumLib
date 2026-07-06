package dev.merosssany.osmiumlib.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Ability(
        int remainingTime,
        int cooldown,
        int id
) {
    public static final Codec<Ability> CODEC = RecordCodecBuilder.create(in -> in
            .group(
                    Codec.INT.fieldOf("remainingTime").forGetter(Ability::remainingTime),
                    Codec.INT.fieldOf("cooldown").forGetter(Ability::cooldown),
                    Codec.INT.fieldOf("id").forGetter(Ability::id)
            ).apply(in, Ability::new)
    );
}
