package ua.pletinnya.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

import java.util.ArrayList;
import java.util.List;

/** Усе, що відомо про мага: чотири характеристики, ранг, досвід напрямів і завчені формули. */
public class MageData {
	public static final int SPELL_SLOTS = 9;

	public static final int FEAT_SUSTAINED = 1;
	public static final int FEAT_TRIGGER = 2;
	public static final int FEAT_DISPEL = 4;
	public static final int FEAT_COMPLEX = 8;
	public static final int FEAT_GRAND = 16;

	public static final Codec<MageData> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.FLOAT.fieldOf("mana").forGetter(d -> d.mana),
		Codec.INT.fieldOf("max_mana").forGetter(d -> d.maxMana),
		Codec.INT.fieldOf("conductivity").forGetter(d -> d.conductivity),
		Codec.INT.fieldOf("precision").forGetter(d -> d.precision),
		Codec.INT.fieldOf("perception").forGetter(d -> d.perception),
		Codec.INT.fieldOf("rank").forGetter(d -> d.rank),
		Codec.INT.fieldOf("channel_damage").forGetter(d -> d.channelDamage),
		Codec.INT.listOf().fieldOf("xp").forGetter(d -> d.xp),
		SpellFormula.CODEC.listOf().fieldOf("spells").forGetter(d -> d.spells),
		Codec.INT.fieldOf("selected").forGetter(d -> d.selected),
		Codec.INT.fieldOf("feats").forGetter(d -> d.feats),
		Codec.INT.fieldOf("casts").forGetter(d -> d.casts),
		Codec.BOOL.fieldOf("aura").forGetter(d -> d.aura),
		Codec.INT.fieldOf("aura_ticks").forGetter(d -> d.auraTicks)
	).apply(i, MageData::new));

	public float mana;
	public int maxMana;
	public int conductivity;
	public int precision;
	public int perception;
	public int rank;
	public int channelDamage;
	public final List<Integer> xp;
	public final List<SpellFormula> spells;
	public int selected;
	public int feats;
	public int casts;
	public boolean aura;
	public int auraTicks;

	private transient boolean dirty = true;

	public MageData() {
		this(50f, 50, 15, 5, 5, 1, 0, List.of(), List.of(), 0, 0, 0, false, 0);
		spells.set(0, new SpellFormula("Іскра", Element.FIRE.ordinal(), SpellForm.PROJECTILE.ordinal(), 2, 1, 2, 1, 0));
		spells.set(1, new SpellFormula("Подих вітру", Element.WIND.ordinal(), SpellForm.TOUCH.ordinal(), 2, 1, 1, 1, 0));
	}

	public MageData(float mana, int maxMana, int conductivity, int precision, int perception, int rank, int channelDamage,
					List<Integer> xp, List<SpellFormula> spells, int selected, int feats, int casts, boolean aura, int auraTicks) {
		this.mana = mana;
		this.maxMana = maxMana;
		this.conductivity = conductivity;
		this.precision = precision;
		this.perception = perception;
		this.rank = Math.max(1, Math.min(7, rank));
		this.channelDamage = channelDamage;
		this.xp = new ArrayList<>(xp);
		while (this.xp.size() < Element.values().length) this.xp.add(0);
		this.spells = new ArrayList<>(spells);
		while (this.spells.size() < SPELL_SLOTS) this.spells.add(SpellFormula.empty());
		this.selected = Math.floorMod(selected, SPELL_SLOTS);
		this.feats = feats;
		this.casts = casts;
		this.aura = aura;
		this.auraTicks = auraTicks;
	}

	public void markDirty() { dirty = true; }
	public boolean consumeDirty() { boolean d = dirty; dirty = false; return d; }

	public int xp(Element e) { return xp.get(e.ordinal()); }

	public void addXp(Element e, int amount) {
		xp.set(e.ordinal(), xp.get(e.ordinal()) + amount);
		markDirty();
	}

	public int totalXp() {
		int t = 0;
		for (int v : xp) t += v;
		return t;
	}

	/** Рівень розуміння напряму (0–10). */
	public int mastery(Element e) {
		return Math.min(10, (int) Math.sqrt(xp(e) / 50.0));
	}

	public boolean hasFeat(int feat) { return (feats & feat) != 0; }

	public void addFeat(int feat) {
		if (!hasFeat(feat)) {
			feats |= feat;
			markDirty();
		}
	}

	public SpellFormula selectedSpell() { return spells.get(selected); }

	public NbtCompound toNbt() {
		NbtElement el = CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElse(new NbtCompound());
		return el instanceof NbtCompound c ? c : new NbtCompound();
	}

	public static MageData fromNbt(NbtCompound nbt) {
		return CODEC.parse(NbtOps.INSTANCE, nbt).result().orElseGet(MageData::new);
	}
}
