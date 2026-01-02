package org.teamvoided.xaero_api.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.teamvoided.createllaneous.Createllaneous;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

public interface CustomMapNameRegistry {
    @ApiStatus.Internal
    HashMap<ResourceLocation, MapNameProvider> MAP_NAME_PROVIDERS = new HashMap<>(16);

    /**
     * @param id       ResourceLocation should be unique unless for overrides.
     * @param provider MapName provider
     */
    @ParametersAreNonnullByDefault
    static void registerMapNameProvider(ResourceLocation id, MapNameProvider provider) {
        if (MAP_NAME_PROVIDERS.get(id) == null) {
            Createllaneous.LOGGER.warn("ID: {}, is being registered twice. Overriding!", id);
        }
        MAP_NAME_PROVIDERS.put(id, provider);
    }

    @FunctionalInterface
    interface MapNameProvider {
        /**
         *
         * @param original The original name fetched by Xaero's. Can be nullable.
         * @param entity   The entity for which the name is for.
         * @return Return the new name of the entity. If returns null the entity name will be disabled. <b>Must</b> return original if name is not modified.
         */
        @Nullable
        Component getMapName(@Nullable Component original, @NotNull Entity entity);
    }
}
