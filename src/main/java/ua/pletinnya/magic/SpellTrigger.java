package ua.pletinnya.magic;

import net.minecraft.text.Text;

/** Умова спрацювання. Умовні плетіння доступні з рангу IV (Магістр). */
public enum SpellTrigger {
	IMMEDIATE("immediate", 1.0f, 0, 1),
	DELAYED("delayed", 1.2f, 3, 4),
	PROXIMITY("proximity", 1.4f, 5, 4);

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

	public Text displayName() {
		return Text.translatable("trigger.pletinnya." + id);
	}

	public static SpellTrigger byIndex(int i) {
		SpellTrigger[] v = values();
		return v[Math.floorMod(i, v.length)];
	}
}
