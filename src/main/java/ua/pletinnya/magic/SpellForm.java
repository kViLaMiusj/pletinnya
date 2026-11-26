package ua.pletinnya.magic;

import net.minecraft.text.Text;

/** Форма закляття: як саме плетіння доставляє ефект. */
public enum SpellForm {
	PROJECTILE("projectile", 1.0f, 1, true, true),
	AREA("area", 1.5f, 3, true, true),
	SELF("self", 0.8f, 0, false, false),
	TOUCH("touch", 0.7f, 0, true, false),
	CONSTRUCT("construct", 1.3f, 4, true, true);

	public final String id;
	public final float costMul;
	public final int complexity;
	public final boolean usesSize;
	public final boolean usesRange;

	SpellForm(String id, float costMul, int complexity, boolean usesSize, boolean usesRange) {
		this.id = id;
		this.costMul = costMul;
		this.complexity = complexity;
		this.usesSize = usesSize;
		this.usesRange = usesRange;
	}

	public Text displayName() {
		return Text.translatable("form.pletinnya." + id);
	}

	public static SpellForm byIndex(int i) {
		SpellForm[] v = values();
		return v[Math.floorMod(i, v.length)];
	}
}
