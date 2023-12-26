package dev.worldgen.fle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record FeatureList(List<List<ResourceLocation>> ids) {
    public static final Codec<FeatureList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.listOf().listOf().fieldOf("features").forGetter(FeatureList::ids)
    ).apply(instance, FeatureList::new));
}
