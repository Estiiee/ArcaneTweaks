package com.estie.arcanetweaks.handlers;

import com.estie.arcanetweaks.compat.alexscaves.BiomeConfigLoader;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceReloadHandler implements PreparableReloadListener {
    
    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager resourceManager, ProfilerFiller prepProfiler, ProfilerFiller applyProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return barrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
            BiomeConfigLoader.init();
        }, gameExecutor);
    }
}