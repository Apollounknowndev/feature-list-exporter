package dev.worldgen.fle.platform;

import dev.worldgen.fle.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Path getInstanceDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }
}
