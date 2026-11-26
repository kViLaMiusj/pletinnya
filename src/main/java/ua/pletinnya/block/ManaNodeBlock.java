package ua.pletinnya.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/** Вузол мани: місце, де нитки Плетіння сходяться. Поруч медитація відновлює ману вчетверо швидше. */
public class ManaNodeBlock extends Block {
	public static final MapCodec<ManaNodeBlock> CODEC = createCodec(ManaNodeBlock::new);

	public ManaNodeBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		for (int i = 0; i < 3; i++) {
			double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 3;
			double y = pos.getY() + 0.5 + random.nextDouble() * 2;
			double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 3;
			world.addParticle(ParticleTypes.ENCHANT, x, y, z, (pos.getX() + 0.5 - x) * 0.5, -0.3, (pos.getZ() + 0.5 - z) * 0.5);
		}
		if (random.nextInt(4) == 0) {
			world.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 0, 0.03, 0);
		}
	}
}
