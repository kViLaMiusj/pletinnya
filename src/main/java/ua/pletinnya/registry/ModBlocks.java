package ua.pletinnya.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.block.ManaNodeBlock;

public final class ModBlocks {
	private ModBlocks() {}

	public static final Block MANA_CRYSTAL_ORE = register("mana_crystal_ore",
		new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), AbstractBlock.Settings.copy(Blocks.IRON_ORE).luminance(s -> 3)));
	public static final Block DEEPSLATE_MANA_CRYSTAL_ORE = register("deepslate_mana_crystal_ore",
		new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), AbstractBlock.Settings.copy(Blocks.DEEPSLATE_IRON_ORE).luminance(s -> 3)));
	public static final Block MANA_CRYSTAL_BLOCK = register("mana_crystal_block",
		new Block(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).luminance(s -> 7)));
	public static final Block RUNE_STONE = register("rune_stone",
		new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS).luminance(s -> 4)));
	public static final Block MANA_NODE = register("mana_node",
		new ManaNodeBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).strength(6f, 1200f).luminance(s -> 13).requiresTool()));
	public static final Block MANA_BARRIER = register("mana_barrier",
		new TransparentBlock(AbstractBlock.Settings.create().strength(3f, 6f).nonOpaque().dropsNothing()
			.luminance(s -> 8).sounds(BlockSoundGroup.AMETHYST_BLOCK).pistonBehavior(PistonBehavior.BLOCK)
			.allowsSpawning((s, w, p, t) -> false).solidBlock((s, w, p) -> false)
			.suffocates((s, w, p) -> false).blockVision((s, w, p) -> false)));
	public static final Block WOVEN_STONE = register("woven_stone",
		new Block(AbstractBlock.Settings.copy(Blocks.STONE).strength(2f, 6f).dropsNothing().pistonBehavior(PistonBehavior.BLOCK)));

	private static Block register(String name, Block block) {
		return Registry.register(Registries.BLOCK, Pletinnya.id(name), block);
	}

	public static void init() {}
}
