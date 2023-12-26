package dev.worldgen.fle.platform;

import dev.worldgen.fle.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public Path getInstanceDirectory() {
        return FMLPaths.GAMEDIR.get();
    }
}