package ua.pletinnya.magic;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/** Шістнадцять шляхів: дванадцять напрямів Плетіння та чотири особливі способи (дракони, духи, святе, аура). */
public enum MagicPath {
	WATER(Element.WATER), FIRE(Element.FIRE), WIND(Element.WIND), EARTH(Element.EARTH),
	LIGHTNING(Element.LIGHTNING), ICE(Element.ICE), NATURE(Element.NATURE), SOUND(Element.SOUND),
	LIGHT(Element.LIGHT), DARKNESS(Element.DARKNESS), BLOOD(Element.BLOOD), MANA(Element.MANA),
	DRAGON("dragon", 0xC8962E), SPIRIT("spirit", 0x7FE7E0), HOLY("holy", 0xF2D27A), AURA("aura", 0xFF9A3C);

	public final String id;
	public final int color;
	@Nullable public final Element element;

	MagicPath(Element e) {
		this.id = e.id;
		this.color = e.color;
		this.element = e;
	}

	MagicPath(String id, int color) {
		this.id = id;
		this.color = color;
		this.element = null;
	}

	public Text displayName() {
		return element != null ? element.displayName() : Text.translatable("path.pletinnya." + id);
	}

	public static MagicPath of(Element e) {
		return values()[e.ordinal()];
	}
}
