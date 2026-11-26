package ua.pletinnya.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.entity.WeaveProjectileEntity;

public final class ModEntities {
	private ModEntities() {}

	public static final EntityType<WeaveProjectileEntity> WEAVE_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
		Pletinnya.id("weave_projectile"),
		EntityType.Builder.<WeaveProjectileEntity>create(WeaveProjectileEntity::new, SpawnGroup.MISC)
			.dimensions(0.3f, 0.3f)
			.maxTrackingRange(4)
			.trackingTickInterval(10)
			.build("weave_projectile"));

	public static void init() {}
}
