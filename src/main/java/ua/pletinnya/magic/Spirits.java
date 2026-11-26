package ua.pletinnya.magic;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

/** Дух місця залежить від того, де стоїть святилище. */
public final class Spirits {
	private Spirits() {}

	public static Element spiritOf(World w, BlockPos pos) {
		if (w.getRegistryKey() == World.NETHER) return Element.FIRE;
		if (w.getRegistryKey() == World.END) return Element.MANA;
		RegistryEntry<Biome> b = w.getBiome(pos);
		if (b.matchesKey(BiomeKeys.DEEP_DARK)) return Element.DARKNESS;
		if (b.matchesKey(BiomeKeys.CHERRY_GROVE) || b.matchesKey(BiomeKeys.FLOWER_FOREST) || b.matchesKey(BiomeKeys.SUNFLOWER_PLAINS)) return Element.LIGHT;
		if (b.matchesKey(BiomeKeys.MEADOW) || b.matchesKey(BiomeKeys.LUSH_CAVES)) return Element.SOUND;
		if (b.matchesKey(BiomeKeys.SWAMP) || b.matchesKey(BiomeKeys.MANGROVE_SWAMP)) return Element.BLOOD;
		if (b.isIn(BiomeTags.IS_OCEAN) || b.isIn(BiomeTags.IS_RIVER) || b.isIn(BiomeTags.IS_BEACH)) return Element.WATER;
		if (b.value().getTemperature() < 0.15f) return Element.ICE;
		if (b.isIn(BiomeTags.IS_BADLANDS) || b.matchesKey(BiomeKeys.DESERT) || b.isIn(BiomeTags.IS_SAVANNA)) return Element.FIRE;
		if (b.isIn(BiomeTags.IS_MOUNTAIN) || pos.getY() < 40) return Element.EARTH;
		if (b.isIn(BiomeTags.IS_FOREST) || b.isIn(BiomeTags.IS_JUNGLE) || b.isIn(BiomeTags.IS_TAIGA)) return Element.NATURE;
		if (w.isThundering()) return Element.LIGHTNING;
		return Element.WIND;
	}
}
