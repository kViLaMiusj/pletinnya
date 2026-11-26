package ua.pletinnya.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/** Кластер кристалів мани — прикраса структур (хрестоподібна модель). */
public class CrystalClusterBlock extends Block {
	public static final MapCodec<CrystalClusterBlock> CODEC = createCodec(CrystalClusterBlock::new);
	private static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 3, 13, 12, 13);

	public CrystalClusterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}
