package ua.pletinnya.magic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ua.pletinnya.item.StaffItem;
import ua.pletinnya.item.WeaveEyeItem;
import ua.pletinnya.registry.ModBlocks;

/**
 * П'ять законів Плетіння у вигляді формул. Спільні для сервера й клієнта,
 * щоб Скрижаль показувала ту саму ціну, яку потім стягне сервер.
 */
public final class WeavingLaws {
	private WeavingLaws() {}

	// ---------- Закон ціни ----------

	public static float cost(PlayerEntity p, MageData d, SpellFormula f) {
		SpellForm form = f.formType();
		Element el = f.elementType();
		float c = el.baseCost;
		c *= 0.6f + 0.4f * f.power();
		if (form.usesSize) c *= 0.7f + 0.25f * f.size();
		if (form.usesRange) c *= 0.8f + 0.12f * f.range();
		c *= 0.7f + 0.25f * f.duration();
		c *= form.costMul;
		c *= f.triggerType().costMul;
		c *= environmentFactor(p, el);
		c *= 1f - Math.min(d.precision, 100) * 0.003f;
		c *= staffCostMul(p);
		return Math.max(1f, c);
	}

	/**
	 * Використати наявну речовину дешевше, ніж створити її:
	 * вода поруч здешевлює водні плетіння, гроза — блискавку тощо.
	 */
	public static float environmentFactor(PlayerEntity p, Element el) {
		World w = p.getWorld();
		BlockPos pos = p.getBlockPos();
		boolean nether = w.getRegistryKey() == World.NETHER;
		boolean end = w.getRegistryKey() == World.END;
		return switch (el) {
			case WATER -> p.isTouchingWaterOrRain() || near(w, pos, 3, s -> s.getFluidState().isIn(FluidTags.WATER)) ? 0.6f : nether ? 1.6f : 1f;
			case FIRE -> nether || near(w, pos, 3, s -> s.isOf(Blocks.FIRE) || s.isOf(Blocks.CAMPFIRE) || s.isOf(Blocks.LAVA) || s.isOf(Blocks.MAGMA_BLOCK)) ? 0.6f
				: p.isTouchingWaterOrRain() ? 1.3f : 1f;
			case ICE -> nether ? 1.6f : w.getBiome(pos).value().getTemperature() < 0.15f || near(w, pos, 3, s -> s.isOf(Blocks.ICE) || s.isOf(Blocks.SNOW_BLOCK) || s.isOf(Blocks.PACKED_ICE) || s.isOf(Blocks.SNOW)) ? 0.6f : 1f;
			case EARTH -> end ? 1.3f : (!w.isSkyVisible(pos) && pos.getY() < 50) ? 0.8f : 1f;
			case LIGHTNING -> w.isThundering() ? 0.5f : w.isRaining() ? 0.8f : 1f;
			case NATURE -> (nether || end) ? 1.4f
				: (w.getBiome(pos).isIn(BiomeTags.IS_FOREST) || w.getBiome(pos).isIn(BiomeTags.IS_JUNGLE) || w.getBiome(pos).isIn(BiomeTags.IS_TAIGA)) ? 0.7f : 1f;
			case LIGHT -> w.getLightLevel(pos) <= 4 ? 1.3f : (w.isDay() && w.isSkyVisible(pos)) ? 0.7f : 1f;
			case DARKNESS -> w.getLightLevel(pos) <= 4 ? 0.7f : (w.isDay() && w.isSkyVisible(pos)) ? 1.3f : 1f;
			case WIND -> pos.getY() > 120 ? 0.7f : !w.isSkyVisible(pos) ? 1.2f : 1f;
			case MANA -> near(w, pos, 6, s -> s.isOf(ModBlocks.MANA_NODE)) ? 0.6f : 1f;
			case SOUND, BLOOD -> 1f;
		};
	}

	private static boolean near(World w, BlockPos center, int r, java.util.function.Predicate<BlockState> test) {
		for (BlockPos q : BlockPos.iterate(center.add(-r, -r, -r), center.add(r, r, r))) {
			if (test.test(w.getBlockState(q))) return true;
		}
		return false;
	}

	// ---------- Фокуси (посохи) ----------

	private static StaffItem staff(PlayerEntity p) {
		for (Hand h : Hand.values()) {
			ItemStack s = p.getStackInHand(h);
			if (s.getItem() instanceof StaffItem st) return st;
		}
		return null;
	}

	public static float staffCostMul(PlayerEntity p) {
		StaffItem s = staff(p);
		return s == null ? 1f : s.costMul;
	}

	public static int staffConductivity(PlayerEntity p) {
		StaffItem s = staff(p);
		return s == null ? 0 : s.conductivityBonus;
	}

	public static int staffStability(PlayerEntity p) {
		StaffItem s = staff(p);
		return s == null ? 0 : s.stability;
	}

	// ---------- Характеристики з урахуванням стану ----------

	/** Провідність: скільки мани можна безпечно пропустити через себе за одне плетіння. */
	public static float effectiveConductivity(PlayerEntity p, MageData d) {
		float c = d.conductivity;
		if (d.channelDamage > 0) c *= 0.5f;
		return c + staffConductivity(p);
	}

	public static int effectivePerception(PlayerEntity p, MageData d) {
		int bonus = 0;
		for (Hand h : Hand.values()) {
			if (p.getStackInHand(h).getItem() instanceof WeaveEyeItem) bonus = 40;
		}
		return d.perception + bonus;
	}

	// ---------- Закон розуміння ----------

	public static int understandingLimit(MageData d, Element e) {
		return Ranks.complexityCap(d.rank) + d.mastery(e) / 2;
	}

	/** Повертає причину, з якої маг не може сплести формулу, або null. */
	public static Text weaveError(MageData d, SpellFormula f) {
		if (f.name().isBlank()) return Text.translatable("message.pletinnya.no_name");
		if (f.triggerType().minRank > d.rank)
			return Text.translatable("message.pletinnya.trigger_rank", Ranks.title(f.triggerType().minRank));
		int limit = understandingLimit(d, f.elementType());
		if (f.complexity() > limit)
			return Text.translatable("message.pletinnya.too_complex", f.complexity(), limit);
		return null;
	}

	// ---------- Закон наслідку ----------

	/** Ймовірність збою плетіння (0–0.85). */
	public static float misfireChance(PlayerEntity p, MageData d, SpellFormula f, int load, float cost) {
		float stability = d.precision * 0.4f + 8f + staffStability(p);
		float ch = (f.complexity() + load * 2 - stability) * 0.04f;
		float overload = cost / Math.max(1f, effectiveConductivity(p, d));
		if (overload > 1f) ch += 0.15f * (overload - 1f) + 0.1f;
		if (d.channelDamage > 0) ch += 0.1f;
		return Math.max(0f, Math.min(0.85f, ch));
	}
}
