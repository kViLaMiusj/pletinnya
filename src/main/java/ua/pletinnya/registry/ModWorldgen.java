package ua.pletinnya.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;
import ua.pletinnya.Pletinnya;

/** Руда кристалів мани. Структури описані даними (data/pletinnya/worldgen). */
public final class ModWorldgen {
	private ModWorldgen() {}

	public static void init() {
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
			RegistryKey.of(RegistryKeys.PLACED_FEATURE, Pletinnya.id("ore_mana_crystal")));
	}
}
