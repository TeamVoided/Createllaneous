package org.teamvoided.createllaneous.mixin;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BaseSpawner.class)
public interface BaseSpawnerAccessor {
    @Accessor("spawnPotentials")
    SimpleWeightedRandomList<SpawnData> cm_getSpawnPotentials();

    @Accessor("nextSpawnData")
    SpawnData cm_getNextSpawnData();
}
