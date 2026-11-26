package ua.pletinnya.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import ua.pletinnya.item.EssenceItem;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.MagicPath;
import ua.pletinnya.magic.Prayers;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.registry.ModItems;

/**
 * Вівтар святості. Зі святим символом у руці — обрати божество (Shift+ПКМ перебирає).
 * Порожньою рукою — щоденна молитва. З есенцією — пожертва.
 */
public class HolyAltarBlock extends Block {
	public static final MapCodec<HolyAltarBlock> CODEC = createCodec(HolyAltarBlock::new);

	public HolyAltarBlock(Settings settings) {
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
		MageData d = sp.getAttachedOrCreate(ModAttachments.MAGE);
		ItemStack held = sp.getMainHandStack();
		if (held.isOf(ModItems.HOLY_SYMBOL)) {
			int next = sp.isSneaking() ? (d.paths.deity % 3) + 1 : Math.max(1, d.paths.deity);
			Prayers.altar(sp, next, false, 0f);
			return ActionResult.SUCCESS;
		}
		if (d.paths.deity == 0) {
			sp.sendMessage(Text.translatable("message.pletinnya.need_symbol").formatted(Formatting.GRAY), true);
			return ActionResult.SUCCESS;
		}
		if (held.getItem() instanceof EssenceItem e) {
			Prayers.altar(sp, 0, true, e.path == MagicPath.HOLY ? 25f : 10f);
			held.decrementUnlessCreative(1, sp);
			return ActionResult.SUCCESS;
		}
		Prayers.altar(sp, 0, false, 0f);
		return ActionResult.SUCCESS;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(3) == 0) {
			world.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6, pos.getY() + 1.1, pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6, 0, 0.03, 0);
		}
	}
}
