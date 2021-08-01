package com.gestankbratwurst.core.mmcore.util.common;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

public final class NMSUtils {

  // TODO insentients
  private static final Map<EntityTypes<?>, Object> positionMap = null;

  public static <T extends Entity> EntityTypes<T> register(final EntityTypes<T> type, final String vanillaKey,
      final String key,
      final EntityTypes.b<T> supplier, final int baseID) {
    // Creating a minecraft key with the name
    final MinecraftKey baseKey = MinecraftKey.a(vanillaKey);
    final MinecraftKey minecraftKey = MinecraftKey.a(key);
    Validate.notNull(minecraftKey, "Using an invalid name for registering a custom entity. Name: " + key);
    Validate.notNull(baseKey, "Using an invalid name for registering a custom entity. BaseKey: " + baseKey);
    // FIXME IRegistry.ENTITY
    if (IRegistry.Y.getOptional(minecraftKey).isPresent()) {
      throw new IllegalArgumentException("Entity with key " + key + " already exists !");
    }

    //Getting the data converter type for the default entity and adding that to the custom mob.
    final Map<Object, Type<?>> typeMap = (Map<Object, Type<?>>) DataConverterRegistry.a()
        .getSchema(DataFixUtils.makeKey(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion())))
        // FIXME DataConverterTypes.ENTITY
        .findChoiceType(DataConverterTypes.q).types();

    if (!typeMap.containsKey(baseKey.toString())) {
      Bukkit.getLogger().warning("Cannot find vanilla mob named " + baseKey);
    }

    typeMap.put(minecraftKey.toString(), typeMap.get(baseKey.toString()));

    final EntityTypes<T> customEntityNMSEntityType = EntityTypes.Builder
        // FIXME type.getEnumCreatureType()
        .<T>a(supplier, type.f())
        .a(key);

    // FIXME IRegistry.ENTITY_TYPE
    IRegistry.a(IRegistry.Y, baseID, key, customEntityNMSEntityType);

    //Is an insentient entity? Also copy the EntityPositionTypes value.
    if (type.getClass().isAssignableFrom(EntityInsentient.class)) {
      final Object entityInformation = positionMap.get(customEntityNMSEntityType);
      positionMap.put(customEntityNMSEntityType, entityInformation);
    }
    return customEntityNMSEntityType;
  }

}
