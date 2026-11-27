package ua.pletinnya.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * Формула закляття: Задум (напрям + форма, за бажання — друга стихія злиття), параметри плетіння та умова спрацювання.
 * Живлення (ціну) рахує {@link WeavingLaws}.
 *
 * <p>Параметри: сила, розмір, дальність, тривалість (1..10), швидкість (1..10), кількість (1..8),
 * пробивання (0..8), наведення (0..10). element2 = -1 — без злиття.
 */
public record SpellFormula(String name, int element, int form, int power, int size, int range, int duration, int trigger, int quality,
						   int speed, int count, int pierce, int homing, int element2) {
	public static final int MIN = 1;
	public static final int MAX = 10;
	public static final int MAX_COUNT = 8;
	public static final int MAX_PIERCE = 8;

	public static final Codec<SpellFormula> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.STRING.fieldOf("name").forGetter(SpellFormula::name),
		Codec.INT.fieldOf("element").forGetter(SpellFormula::element),
		Codec.INT.fieldOf("form").forGetter(SpellFormula::form),
		Codec.INT.fieldOf("power").forGetter(SpellFormula::power),
		Codec.INT.fieldOf("size").forGetter(SpellFormula::size),
		Codec.INT.fieldOf("range").forGetter(SpellFormula::range),
		Codec.INT.fieldOf("duration").forGetter(SpellFormula::duration),
		Codec.INT.fieldOf("trigger").forGetter(SpellFormula::trigger),
		Codec.INT.optionalFieldOf("quality", 0).forGetter(SpellFormula::quality),
		Codec.INT.optionalFieldOf("speed", 3).forGetter(SpellFormula::speed),
		Codec.INT.optionalFieldOf("count", 1).forGetter(SpellFormula::count),
		Codec.INT.optionalFieldOf("pierce", 0).forGetter(SpellFormula::pierce),
		Codec.INT.optionalFieldOf("homing", 0).forGetter(SpellFormula::homing),
		Codec.INT.optionalFieldOf("element2", -1).forGetter(SpellFormula::element2)
	).apply(i, SpellFormula::new));

	public SpellFormula(String name, int element, int form, int power, int size, int range, int duration, int trigger, int quality) {
		this(name, element, form, power, size, range, duration, trigger, quality, 3, 1, 0, 0, -1);
	}

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
		int el = Math.floorMod(element, Element.values().length);
		int e2 = element2 < 0 ? -1 : Math.floorMod(element2, Element.values().length);
		if (e2 == el) e2 = -1;
		return new SpellFormula(n, el,
			Math.floorMod(form, SpellForm.values().length),
			MathHelper.clamp(power, MIN, MAX),
			MathHelper.clamp(size, MIN, MAX),
			MathHelper.clamp(range, MIN, MAX),
			MathHelper.clamp(duration, MIN, MAX),
			Math.floorMod(trigger, SpellTrigger.values().length),
			MathHelper.clamp(quality, 0, 100),
			MathHelper.clamp(speed, MIN, MAX),
			MathHelper.clamp(count, 1, MAX_COUNT),
			MathHelper.clamp(pierce, 0, MAX_PIERCE),
			MathHelper.clamp(homing, 0, MAX),
			e2);
	}

	public Element elementType() { return Element.byIndex(element); }
	public SpellForm formType() { return SpellForm.byIndex(form); }
	public SpellTrigger triggerType() { return SpellTrigger.byIndex(trigger); }
	public boolean fused() { return element2 >= 0 && element2 != element; }
	public Element element2Type() { return fused() ? Element.byIndex(element2) : null; }

	private SpellFormula copy(String n, int e, int f, int p, int s, int r, int d, int t, int q, int sp, int c, int pi, int h, int e2) {
		return new SpellFormula(n, e, f, p, s, r, d, t, q, sp, c, pi, h, e2);
	}

	public SpellFormula withElement(int e) { return copy(name, e, form, power, size, range, duration, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withForm(int f) { return copy(name, element, f, power, size, range, duration, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withTrigger(int t) { return copy(name, element, form, power, size, range, duration, t, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withPower(int v) { return copy(name, element, form, v, size, range, duration, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withSize(int v) { return copy(name, element, form, power, v, range, duration, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withRange(int v) { return copy(name, element, form, power, size, v, duration, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withDuration(int v) { return copy(name, element, form, power, size, range, v, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withName(String n) { return copy(n, element, form, power, size, range, duration, trigger, quality, speed, count, pierce, homing, element2); }
	public SpellFormula withQuality(int q) { return copy(name, element, form, power, size, range, duration, trigger, q, speed, count, pierce, homing, element2); }
	public SpellFormula withSpeed(int v) { return copy(name, element, form, power, size, range, duration, trigger, quality, v, count, pierce, homing, element2); }
	public SpellFormula withCount(int v) { return copy(name, element, form, power, size, range, duration, trigger, quality, speed, v, pierce, homing, element2); }
	public SpellFormula withPierce(int v) { return copy(name, element, form, power, size, range, duration, trigger, quality, speed, count, v, homing, element2); }
	public SpellFormula withHoming(int v) { return copy(name, element, form, power, size, range, duration, trigger, quality, speed, count, pierce, v, element2); }
	public SpellFormula withElement2(int v) { return copy(name, element, form, power, size, range, duration, trigger, quality, speed, count, pierce, homing, v); }
	/** Та сама формула, але від імені другої стихії (для злиття). */
	public SpellFormula asSecondary() { return fused() ? copy(name, element2, form, power, size, range, duration, trigger, quality, speed, count, pierce, homing, -1) : this; }

	/** Ручне плетіння: послідовність вузлів, яку треба повторити на Скрижалі. Залежить лише від Задуму й Формули. */
	public int[] pattern() {
		int len = Math.min(14, 4 + complexity() / 5);
		long seed = element * 31L + form * 131L + power * 977L + size * 3571L + range * 7919L + duration * 104729L + trigger * 1299709L
			+ speed * 15485863L + count * 32452843L + pierce * 49979687L + homing * 67867967L + (element2 + 1) * 86028121L;
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

	/** Складність плетіння — сума задіяних параметрів, форми, умови та злиття. */
	public int complexity() {
		SpellForm f = formType();
		int c = power + duration + f.complexity + triggerType().complexity;
		if (f.usesSize) c += size;
		if (f.usesRange) c += range;
		if (f.usesSpeed) c += Math.max(0, speed - 3);
		if (f.usesCount) c += (count - 1) * 2;
		if (f.usesPierce) c += pierce;
		if (f.usesHoming) c += homing;
		if (fused()) c += 6 + (Fusion.opposed(elementType(), element2Type()) ? 4 : 0);
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
	/** Множник швидкості (снаряди, хвилі, темп ударів): 0.55 .. 1.9. */
	public float speedMul() { return 0.4f + speed * 0.15f; }
}
