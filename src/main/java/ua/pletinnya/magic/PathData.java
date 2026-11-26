package ua.pletinnya.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** Стан особливих шляхів: вивчені драконячі слова, угода з духом, віра в божество. */
public class PathData {
	public static final Codec<PathData> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.INT.fieldOf("dragon_words").forGetter(d -> d.dragonWords),
		Codec.INT.fieldOf("spirit").forGetter(d -> d.spirit),
		Codec.INT.fieldOf("spirit_bond").forGetter(d -> d.spiritBond),
		Codec.INT.fieldOf("deity").forGetter(d -> d.deity),
		Codec.FLOAT.fieldOf("faith").forGetter(d -> d.faith),
		Codec.INT.fieldOf("devotion").forGetter(d -> d.devotion),
		Codec.LONG.fieldOf("last_prayer_day").forGetter(d -> d.lastPrayerDay),
		Codec.LONG.fieldOf("spirit_gift").forGetter(d -> d.spiritGift)
	).apply(i, PathData::new));

	public int dragonWords;
	/** 0 — немає угоди, інакше порядковий номер напряму духа + 1. */
	public int spirit;
	public int spiritBond;
	/** 0 — немає, 1 — Еліас, 2 — Луміс, 3 — Золотий Бог Драконів. */
	public int deity;
	public float faith;
	public int devotion;
	public long lastPrayerDay;
	public long spiritGift;

	public PathData() {
		this(0, 0, 0, 0, 0f, 0, -1L, 0L);
	}

	public PathData(int dragonWords, int spirit, int spiritBond, int deity, float faith, int devotion, long lastPrayerDay, long spiritGift) {
		this.dragonWords = dragonWords;
		this.spirit = spirit;
		this.spiritBond = spiritBond;
		this.deity = deity;
		this.faith = faith;
		this.devotion = devotion;
		this.lastPrayerDay = lastPrayerDay;
		this.spiritGift = spiritGift;
	}

	public Element spiritElement() {
		return spirit > 0 ? Element.byIndex(spirit - 1) : null;
	}

	public boolean knowsWord(int index) {
		return (dragonWords & (1 << index)) != 0;
	}
}
