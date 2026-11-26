package ua.pletinnya.magic;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/** Сім рангів і випробування для переходу між ними. */
public final class Ranks {
	private Ranks() {}

	public static final String[] NUMERALS = {"I", "II", "III", "IV", "V", "VI", "VII"};
	/** Закон розуміння: найбільша складність формули для рангу. */
	private static final int[] COMPLEXITY_CAP = {10, 16, 22, 28, 34, 40, 50};
	/** Закон навантаження: скільки незалежних плетінь маг утримує одночасно. */
	private static final int[] LOAD_LIMIT = {1, 1, 3, 4, 5, 7, 10};
	private static final int[] MANA_CAP = {150, 300, 600, 1200, 2500, 5000, 10000};
	private static final int[] CONDUCTIVITY_CAP = {40, 80, 160, 300, 600, 1200, 3000};

	public static int complexityCap(int rank) { return COMPLEXITY_CAP[idx(rank)]; }
	public static int loadLimit(int rank) { return LOAD_LIMIT[idx(rank)]; }
	public static int manaCap(int rank) { return MANA_CAP[idx(rank)]; }
	public static int conductivityCap(int rank) { return CONDUCTIVITY_CAP[idx(rank)]; }

	private static int idx(int rank) { return Math.max(0, Math.min(6, rank - 1)); }

	public static MutableText name(int rank) {
		return Text.translatable("rank.pletinnya." + Math.max(1, Math.min(7, rank)));
	}

	public static MutableText title(int rank) {
		return Text.literal(NUMERALS[idx(rank)] + " · ").append(name(rank));
	}

	/** Одна вимога випробування. */
	public record Requirement(Text text, boolean met) {}

	/** Вимоги випробування для переходу з рангу {@code rank} на наступний. */
	public static List<Requirement> trial(MageData d) {
		List<Requirement> r = new ArrayList<>();
		switch (d.rank) {
			case 1 -> {
				r.add(count("trial.pletinnya.casts", d.casts, 20));
				r.add(count("trial.pletinnya.xp", d.totalXp(), 300));
			}
			case 2 -> {
				r.add(count("trial.pletinnya.casts", d.casts, 60));
				r.add(count("trial.pletinnya.xp", d.totalXp(), 1500));
				r.add(count("trial.pletinnya.max_mana", d.maxMana, 100));
			}
			case 3 -> {
				r.add(count("trial.pletinnya.xp", d.totalXp(), 5000));
				r.add(feat("trial.pletinnya.feat_sustained", d.hasFeat(MageData.FEAT_SUSTAINED)));
				r.add(count("trial.pletinnya.elements3", masteredCount(d, 3), 3));
				r.add(count("trial.pletinnya.max_mana", d.maxMana, 200));
			}
			case 4 -> {
				r.add(count("trial.pletinnya.xp", d.totalXp(), 15000));
				r.add(feat("trial.pletinnya.feat_trigger", d.hasFeat(MageData.FEAT_TRIGGER)));
				r.add(feat("trial.pletinnya.feat_dispel", d.hasFeat(MageData.FEAT_DISPEL)));
				r.add(count("trial.pletinnya.elements3", masteredCount(d, 3), 5));
				r.add(count("trial.pletinnya.precision", d.precision, 30));
			}
			case 5 -> {
				r.add(count("trial.pletinnya.xp", d.totalXp(), 40000));
				r.add(feat("trial.pletinnya.feat_complex", d.hasFeat(MageData.FEAT_COMPLEX)));
				r.add(count("trial.pletinnya.max_mana", d.maxMana, 1000));
				r.add(count("trial.pletinnya.precision", d.precision, 50));
			}
			case 6 -> {
				r.add(count("trial.pletinnya.xp", d.totalXp(), 100000));
				r.add(feat("trial.pletinnya.feat_grand", d.hasFeat(MageData.FEAT_GRAND)));
				r.add(count("trial.pletinnya.elements6", masteredCount(d, 6), 10));
				r.add(count("trial.pletinnya.perception", d.perception, 70));
			}
			default -> {
			}
		}
		return r;
	}

	private static Requirement count(String key, int have, int need) {
		return new Requirement(Text.translatable(key, Math.min(have, need), need), have >= need);
	}

	private static Requirement feat(String key, boolean met) {
		return new Requirement(Text.translatable(key), met);
	}

	public static int masteredCount(MageData d, int level) {
		int n = 0;
		for (Element e : Element.values()) if (d.mastery(e) >= level) n++;
		return n;
	}

	/** Перевіряє, чи маг склав випробування, і підвищує ранг. */
	public static void checkTrial(ServerPlayerEntity player, MageData d) {
		if (d.rank >= 7) return;
		List<Requirement> req = trial(d);
		if (req.isEmpty()) return;
		for (Requirement q : req) if (!q.met()) return;
		d.rank++;
		d.markDirty();
		player.sendMessage(Text.translatable("message.pletinnya.rank_up", title(d.rank)).formatted(Formatting.GOLD), false);
		player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
			SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
	}
}
