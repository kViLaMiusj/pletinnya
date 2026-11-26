package ua.pletinnya.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * Формула закляття: Задум (напрям + форма), параметри плетіння та умова спрацювання.
 * Живлення (ціну) рахує {@link WeavingLaws}.
 */
public record SpellFormula(String name, int element, int form, int power, int size, int range, int duration, int trigger, int quality) {
	public static final int MIN = 1;
	public static final int MAX = 10;

	public static final Codec<SpellFormula> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.STRING.fieldOf("name").forGetter(SpellFormula::name),
		Codec.INT.fieldOf("element").forGetter(SpellFormula::element),
		Codec.INT.fieldOf("form").forGetter(SpellFormula::form),
		Codec.INT.fieldOf("power").forGetter(SpellFormula::power),
		Codec.INT.fieldOf("size").forGetter(SpellFormula::size),
		Codec.INT.fieldOf("range").forGetter(SpellFormula::range),
		Codec.INT.fieldOf("duration").forGetter(SpellFormula::duration),
		Codec.INT.fieldOf("trigger").forGetter(SpellFormula::trigger),
		Codec.INT.optionalFieldOf("quality", 0).forGetter(SpellFormula::quality)
	).apply(i, SpellFormula::new));

	public SpellFormula(String name, int element, int form, int power, int size, int range, int duration, int trigger) {
		this(name, element, form, power, size, range, duration, trigger, 0);
	}

	public static SpellFormula empty() {
		return new SpellFormula("", 1, 0, 2, 1, 2, 1, 0, 0);
	}

	public boolean isEmpty() {
		return name.isEmpty();
	}

	public SpellFormula sanitized() {
		String n = name.length() > 24 ? name.substring(0, 24) : name;
		return new SpellFormula(n,
			Math.floorMod(element, Element.values().length),
			Math.floorMod(form, SpellForm.values().length),
			MathHelper.clamp(power, MIN, MAX),
			MathHelper.clamp(size, MIN, MAX),
			MathHelper.clamp(range, MIN, MAX),
			MathHelper.clamp(duration, MIN, MAX),
			Math.floorMod(trigger, SpellTrigger.values().length),
			MathHelper.clamp(quality, 0, 100));
	}

	public Element elementType() { return Element.byIndex(element); }
	public SpellForm formType() { return SpellForm.byIndex(form); }
	public SpellTrigger triggerType() { return SpellTrigger.byIndex(trigger); }

	public SpellFormula withElement(int e) { return new SpellFormula(name, e, form, power, size, range, duration, trigger, quality); }
	public SpellFormula withForm(int f) { return new SpellFormula(name, element, f, power, size, range, duration, trigger, quality); }
	public SpellFormula withTrigger(int t) { return new SpellFormula(name, element, form, power, size, range, duration, t, quality); }
	public SpellFormula withPower(int v) { return new SpellFormula(name, element, form, v, size, range, duration, trigger, quality); }
	public SpellFormula withSize(int v) { return new SpellFormula(name, element, form, power, v, range, duration, trigger, quality); }
	public SpellFormula withRange(int v) { return new SpellFormula(name, element, form, power, size, v, duration, trigger, quality); }
	public SpellFormula withDuration(int v) { return new SpellFormula(name, element, form, power, size, range, v, trigger, quality); }
	public SpellFormula withName(String n) { return new SpellFormula(n, element, form, power, size, range, duration, trigger, quality); }

	public SpellFormula withQuality(int q) { return new SpellFormula(name, element, form, power, size, range, duration, trigger, q); }

	/** Ручне плетіння: послідовність вузлів, яку треба повторити на Скрижалі. Залежить лише від Задуму й Формули. */
	public int[] pattern() {
		int len = Math.min(12, 4 + complexity() / 5);
		long seed = element * 31L + form * 131L + power * 977L + size * 3571L + range * 7919L + duration * 104729L + trigger * 1299709L;
		java.util.Random r = new java.util.Random(seed);
		int[] p = new int[len];
		int prev = -1;
		for (int i = 0; i < len; i++) {
			int n;
			do { n = r.nextInt(9); } while (n == prev);
			p[i] = n;
			prev = n;
		}
		return p;
	}

	/** Складність плетіння — сума задіяних параметрів, форми та умови. */
	public int complexity() {
		SpellForm f = formType();
		int c = power + duration + f.complexity + triggerType().complexity;
		if (f.usesSize) c += size;
		if (f.usesRange) c += range;
		return c;
	}

	/** Радіус дії в блоках. */
	public float radius() { return 1.5f + size * 0.75f; }
	/** Дальність у блоках. */
	public float reach() { return 6f + range * 4f; }
	/** Тривалість у тіках. */
	public int durationTicks() { return 40 + duration * 40; }
	/** Затримка для умовних плетінь, у тіках. */
	public int delayTicks() { return 20 + duration * 10; }
}
