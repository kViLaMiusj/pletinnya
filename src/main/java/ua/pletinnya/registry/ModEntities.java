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

	public static final EntityType<ua.pletinnya.entity.SpellSpiritEntity> SPELL_SPIRIT = Registry.register(Registries.ENTITY_TYPE,
		Pletinnya.id("spell_spirit"),
		EntityType.Builder.<ua.pletinnya.entity.SpellSpiritEntity>create(ua.pletinnya.entity.SpellSpiritEntity::new, SpawnGroup.MISC)
			.dimensions(0.6f, 0.6f)
			.maxTrackingRange(6)
			.trackingTickInterval(2)
			.build("spell_spirit"));

	public static void init() {}
}
