package ua.pletinnya.magic;

import net.minecraft.text.Text;

/**
 * Умова спрацювання. Затримка й наближення — руни-пастки (з рангу IV).
 * Накопичення — закляття набирає силу, поки тримаєш посох; пульсація — повторюється кілька разів;
 * відповідь — чекає, поки мага вдарять, і б'є кривдника.
 */
public enum SpellTrigger {
	IMMEDIATE("immediate", 1.0f, 0, 1),
	DELAYED("delayed", 1.2f, 3, 4),
	PROXIMITY("proximity", 1.4f, 5, 4),
	CHARGED("charged", 1.1f, 2, 2),
	PULSE("pulse", 1.9f, 5, 3),
	RETALIATE("retaliate", 1.3f, 4, 3);

	public final String id;
	public final float costMul;
	public final int complexity;
	public final int minRank;

	SpellTrigger(String id, float costMul, int complexity, int minRank) {
		this.id = id;
		this.costMul = costMul;
		this.complexity = complexity;
		this.minRank = minRank;
	}

	/** Руна-пастка: закляття відкладається в WeaveManager. */
	public boolean isRune() {
		return this == DELAYED || this == PROXIMITY;
	}

	public Text displayName() {
		return Text.translatable("trigger.pletinnya." + id);
	}

	public static SpellTrigger byIndex(int i) {
		SpellTrigger[] v = values();
		return v[Math.floorMod(i, v.length)];
	}
}
