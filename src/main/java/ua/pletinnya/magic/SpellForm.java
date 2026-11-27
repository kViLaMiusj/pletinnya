package ua.pletinnya.magic;

import net.minecraft.text.Text;

/** Форма закляття: як саме плетіння доставляє ефект. Прапорці кажуть, які параметри форма використовує. */
public enum SpellForm {
	//          id            ціна  скл. розм  даль  швид  к-сть проб  навед ранг
	PROJECTILE("projectile", 1.0f, 1, true, true, true, true, true, true, 1),
	AREA("area", 1.5f, 3, true, true, false, false, false, false, 1),
	SELF("self", 0.8f, 0, false, false, false, false, false, false, 1),
	TOUCH("touch", 0.7f, 0, true, false, false, false, false, false, 1),
	CONSTRUCT("construct", 1.3f, 4, true, true, false, false, false, false, 1),
	BEAM("beam", 1.3f, 3, true, true, false, false, true, false, 2),
	CONE("cone", 1.3f, 2, true, true, false, false, false, false, 1),
	CHAIN("chain", 1.4f, 4, false, true, true, true, false, true, 2),
	RAIN("rain", 1.8f, 5, true, true, true, true, false, false, 3),
	ELEMENTAL("elemental", 2.0f, 6, true, false, true, true, false, true, 3),
	ORBIT("orbit", 1.5f, 4, true, false, true, true, false, false, 2),
	WAVE("wave", 1.5f, 3, true, true, true, false, false, false, 2),
	SHIELD("shield", 1.2f, 2, true, false, false, false, false, false, 1),
	BLINK("blink", 0.9f, 2, false, true, false, false, false, false, 1);

	public final String id;
	public final float costMul;
	public final int complexity;
	public final boolean usesSize, usesRange, usesSpeed, usesCount, usesPierce, usesHoming;
	public final int minRank;

	SpellForm(String id, float costMul, int complexity, boolean usesSize, boolean usesRange, boolean usesSpeed, boolean usesCount,
			  boolean usesPierce, boolean usesHoming, int minRank) {
		this.id = id;
		this.costMul = costMul;
		this.complexity = complexity;
		this.usesSize = usesSize;
		this.usesRange = usesRange;
		this.usesSpeed = usesSpeed;
		this.usesCount = usesCount;
		this.usesPierce = usesPierce;
		this.usesHoming = usesHoming;
		this.minRank = minRank;
	}

	/** Чи вимагає форма тримати плетіння (займає навантаження), поки воно живе. */
	public boolean sustained() {
		return this == CONSTRUCT || this == ELEMENTAL || this == ORBIT || this == SHIELD;
	}

	public Text displayName() {
		return Text.translatable("form.pletinnya." + id);
	}

	public static SpellForm byIndex(int i) {
		SpellForm[] v = values();
		return v[Math.floorMod(i, v.length)];
	}
}
