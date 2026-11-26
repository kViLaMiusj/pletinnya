package ua.pletinnya.magic;

import net.minecraft.text.Text;

/** Дванадцять напрямів Плетіння. */
public enum Element {
	WATER("water", 0x3A7BD5, 3f),
	FIRE("fire", 0xE8562A, 4f),
	WIND("wind", 0xBFEBDD, 3f),
	EARTH("earth", 0x8B6B3E, 4f),
	LIGHTNING("lightning", 0xF5E663, 6f),
	ICE("ice", 0x9EE6F5, 4f),
	NATURE("nature", 0x4CAF50, 4f),
	SOUND("sound", 0xB39DDB, 4f),
	LIGHT("light", 0xFFF3B0, 2f),
	DARKNESS("darkness", 0x4A3466, 3f),
	BLOOD("blood", 0xA3191B, 5f),
	MANA("mana", 0x6A5CFF, 5f);

	public final String id;
	public final int color;
	public final float baseCost;

	Element(String id, int color, float baseCost) {
		this.id = id;
		this.color = color;
		this.baseCost = baseCost;
	}

	public Text displayName() {
		return Text.translatable("element.pletinnya." + id);
	}

	public static Element byIndex(int i) {
		Element[] v = values();
		return v[Math.floorMod(i, v.length)];
	}

	/** Споріднені напрями: сюди може «зісковзнути» закляття при помилці у формулі. */
	public Element[] related() {
		return switch (this) {
			case WATER -> new Element[]{ICE, NATURE};
			case ICE -> new Element[]{WATER, WIND};
			case FIRE -> new Element[]{LIGHT, LIGHTNING};
			case WIND -> new Element[]{SOUND, ICE};
			case SOUND -> new Element[]{WIND, MANA};
			case EARTH -> new Element[]{NATURE, SOUND};
			case LIGHTNING -> new Element[]{FIRE, LIGHT};
			case NATURE -> new Element[]{WATER, BLOOD};
			case LIGHT -> new Element[]{FIRE, DARKNESS};
			case DARKNESS -> new Element[]{LIGHT, BLOOD};
			case BLOOD -> new Element[]{NATURE, DARKNESS};
			case MANA -> new Element[]{LIGHTNING, SOUND};
		};
	}
}
