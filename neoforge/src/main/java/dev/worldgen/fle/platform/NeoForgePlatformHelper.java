package dev.worldgen.fle.platform;

import dev.worldgen.fle.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public Path getInstanceDirectory() {
        return FMLPaths.GAMEDIR.get();
    }
}