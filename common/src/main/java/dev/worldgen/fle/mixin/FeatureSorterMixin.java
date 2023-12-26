package dev.worldgen.fle.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import dev.worldgen.fle.Constants;
import dev.worldgen.fle.FeatureList;
import dev.worldgen.fle.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(FeatureSorter.class)
public class FeatureSorterMixin {
    @Inject(at = @At("HEAD"), method = "buildFeaturesPerStep(Ljava/util/List;Ljava/util/function/Function;Z)Ljava/util/List;")
    private static <T> void fle$exportFeatures(List<T> biomes, Function<T, List<HolderSet<PlacedFeature>>> biomesToPlacedFeatures, boolean $$2, CallbackInfoReturnable<List<FeatureSorter.StepFeatureData>> cir) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (T biome : biomes) {
            List<List<ResourceLocation>> allPlacedFeatures = new ArrayList<>();
            biomesToPlacedFeatures.apply(biome).forEach(holderSet -> {
                List<ResourceLocation> placedFeaturesInStep = new ArrayList<>();
                holderSet.stream().forEach(holder -> {
                    placedFeaturesInStep.add(holder.unwrapKey().orElse(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("unregistered"))).location());
                });
                allPlacedFeatures.add(placedFeaturesInStep);
            });
            FeatureList featureList = new FeatureList(allPlacedFeatures);
            ResourceLocation biomeId = ((Holder<Biome>)biome).unwrapKey().orElse(ResourceKey.create(Registries.BIOME, new ResourceLocation("unknown", "unknown"))).location();
            Path path = Services.PLATFORM.getInstanceDirectory().resolve(String.format("feature_order_export/%1s/%2s.json", biomeId.getNamespace(), biomeId.getPath()));
            try {
                Files.createDirectories(path.getParent());
                try(BufferedWriter writer = Files.newBufferedWriter(path)) {
                    var dataResult = FeatureList.CODEC.encodeStart(JsonOps.INSTANCE, featureList).get();
                    writer.write(gson.toJson(dataResult.left().get()));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}