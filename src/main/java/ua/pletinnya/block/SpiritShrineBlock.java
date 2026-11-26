package ua.pletinnya.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import ua.pletinnya.fx.Vfx;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.Spirits;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.registry.ModItems;

/** Святилище духів. Із печаткою угоди в руці укладає угоду з духом цього місця. */
public class SpiritShrineBlock extends Block {
	public static final MapCodec<SpiritShrineBlock> CODEC = createCodec(SpiritShrineBlock::new);

	public SpiritShrineBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) return ActionResult.SUCCESS;
		if (!(player instanceof ServerPlayerEntity sp)) return ActionResult.PASS;
		Element spirit = Spirits.spiritOf(world, pos);
		if (!sp.getMainHandStack().isOf(ModItems.SPIRIT_SEAL)) {
			sp.sendMessage(Text.translatable("message.pletinnya.spirit_here", spirit.displayName()).withColor(spirit.color), true);
			return ActionResult.SUCCESS;
		}
		MageData d = sp.getAttachedOrCreate(ModAttachments.MAGE);
		if (d.paths.spirit == spirit.ordinal() + 1) {
			sp.sendMessage(Text.translatable("message.pletinnya.spirit_already", spirit.displayName()).formatted(Formatting.AQUA), true);
			return ActionResult.SUCCESS;
		}
		d.paths.spirit = spirit.ordinal() + 1;
		d.paths.spiritBond = 10;
		d.markDirty();
		sp.getMainHandStack().decrementUnlessCreative(1, sp);
		sp.sendMessage(Text.translatable("message.pletinnya.spirit_pact", spirit.displayName()).withColor(spirit.color), false);
		Vfx.send(sp.getServerWorld(), Vfx.PACT, spirit.ordinal(), pos.toCenterPos(), 2f);
		world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 1f, 0.7f);
		return ActionResult.SUCCESS;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4, pos.getY() + 1.05, pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4, 0, 0.02, 0);
	}
}
