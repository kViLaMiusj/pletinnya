package ua.pletinnya.magic;

/**
 * Злиття двох стихій. Споріднені стихії підсилюють одна одну (резонанс), протилежні — гасять і
 * роблять плетіння складнішим. Друга стихія діє з половинною силою поверх першої.
 */
public final class Fusion {
	private Fusion() {}

	public static boolean opposed(Element a, Element b) {
		if (a == null || b == null) return false;
		return pair(a, b, Element.FIRE, Element.ICE) || pair(a, b, Element.FIRE, Element.WATER)
			|| pair(a, b, Element.LIGHT, Element.DARKNESS) || pair(a, b, Element.EARTH, Element.WIND)
			|| pair(a, b, Element.NATURE, Element.BLOOD);
	}

	public static boolean resonant(Element a, Element b) {
		if (a == null || b == null) return false;
		return pair(a, b, Element.FIRE, Element.WIND) || pair(a, b, Element.WATER, Element.LIGHTNING)
			|| pair(a, b, Element.ICE, Element.WIND) || pair(a, b, Element.EARTH, Element.NATURE)
			|| pair(a, b, Element.LIGHT, Element.FIRE) || pair(a, b, Element.DARKNESS, Element.BLOOD)
			|| pair(a, b, Element.SOUND, Element.WIND) || pair(a, b, Element.MANA, Element.LIGHTNING)
			|| pair(a, b, Element.WATER, Element.ICE) || pair(a, b, Element.EARTH, Element.FIRE);
	}

	private static boolean pair(Element a, Element b, Element x, Element y) {
		return (a == x && b == y) || (a == y && b == x);
	}

	/** Множник сили другої стихії. */
	public static float secondaryMul(SpellFormula f) {
		if (!f.fused()) return 0f;
		Element a = f.elementType(), b = f.element2Type();
		return resonant(a, b) ? 0.7f : opposed(a, b) ? 0.35f : 0.5f;
	}

	/** Множник сили основної стихії (резонанс трохи підсилює). */
	public static float primaryMul(SpellFormula f) {
		if (!f.fused()) return 1f;
		return resonant(f.elementType(), f.element2Type()) ? 1.15f : 1f;
	}

	/** Множник ціни. */
	public static float costMul(SpellFormula f) {
		if (!f.fused()) return 1f;
		return opposed(f.elementType(), f.element2Type()) ? 1.8f : 1.4f;
	}
}
