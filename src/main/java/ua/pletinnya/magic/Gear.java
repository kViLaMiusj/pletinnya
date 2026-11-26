package ua.pletinnya.magic;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.item.MageArmorItem;
import ua.pletinnya.item.StaffItem;

/** Що маг має на собі: одяг шляхів і фокус у руках. */
public final class Gear {
	private Gear() {}

	private static final EquipmentSlot[] ARMOR = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	public static int pieces(PlayerEntity p, MagicPath path) {
		int n = 0;
		for (EquipmentSlot s : ARMOR) {
			ItemStack st = p.getEquippedStack(s);
			if (st.getItem() instanceof MageArmorItem a && a.path == path) n++;
		}
		return n;
	}

	public static boolean fullSet(PlayerEntity p, MagicPath path) {
		return pieces(p, path) == 4;
	}

	/** Шлях повного сету, якщо він є. */
	@Nullable
	public static MagicPath fullSet(PlayerEntity p) {
		ItemStack chest = p.getEquippedStack(EquipmentSlot.CHEST);
		if (chest.getItem() instanceof MageArmorItem a && fullSet(p, a.path)) return a.path;
		return null;
	}

	@Nullable
	public static StaffItem staff(PlayerEntity p) {
		for (Hand h : Hand.values()) {
			if (p.getStackInHand(h).getItem() instanceof StaffItem st) return st;
		}
		return null;
	}

	public static boolean holdingFocus(PlayerEntity p, MagicPath path) {
		StaffItem s = staff(p);
		return s != null && s.path == path;
	}

	/** Множник ціни від одягу й посоха для конкретного напряму. */
	public static float costMul(PlayerEntity p, Element e) {
		MagicPath path = MagicPath.of(e);
		float m = 1f - pieces(p, path) * 0.05f;
		if (fullSet(p, path)) m -= 0.1f;
		StaffItem s = staff(p);
		if (s != null) {
			m *= s.costMul;
			if (s.path == path) m *= 0.8f;
		}
		return Math.max(0.3f, m);
	}

	public static int conductivityBonus(PlayerEntity p) {
		int b = 0;
		StaffItem s = staff(p);
		if (s != null) b += s.conductivityBonus;
		MagicPath set = fullSet(p);
		if (set == MagicPath.DRAGON) b += 40;
		else if (set != null) b += 10;
		return b;
	}

	public static int stability(PlayerEntity p) {
		int b = 0;
		StaffItem s = staff(p);
		if (s != null) b += s.stability;
		if (fullSet(p) == MagicPath.SPIRIT) b += 6;
		return b;
	}
}
