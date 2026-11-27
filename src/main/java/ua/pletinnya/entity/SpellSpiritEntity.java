package ua.pletinnya.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Monster;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.SpellContext;
import ua.pletinnya.magic.SpellForm;
import ua.pletinnya.magic.SpellForms;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.registry.ModEntities;

import java.util.HashMap;
import java.util.Map;

/**
 * Сплетена істота: елементаль (ширяє за плечем мага й стріляє в ворогів) або сфера орбіти
 * (кружляє довкола мага й обпікає тих, хто підійшов). Не зберігається у світі — розплітається.
 */
public class SpellSpiritEntity extends Entity {
	public static final int ELEMENTAL = 0, ORBIT = 1;

	private static final TrackedData<Integer> ELEMENT = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> ELEMENT2 = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> SIZE = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> POWER = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> MODE = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> OWNER = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> INDEX = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> TOTAL = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> SPEED = DataTracker.registerData(SpellSpiritEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private SpellFormula formula = SpellFormula.empty();
	private float power = 1f;
	private int life = 0;
	private int cooldown = 20;
	private final Map<Integer, Integer> touched = new HashMap<>();

	public SpellSpiritEntity(EntityType<? extends SpellSpiritEntity> type, World world) {
		super(type, world);
		this.noClip = true;
		this.setNoGravity(true);
	}

	public static SpellSpiritEntity create(ServerWorld w, ServerPlayerEntity owner, SpellFormula f, float power, int mode, int index, int total) {
		SpellSpiritEntity e = new SpellSpiritEntity(ModEntities.SPELL_SPIRIT, w);
		e.formula = f;
		e.power = power;
		e.life = mode == ORBIT ? f.durationTicks() * 2 : f.durationTicks() * 3;
		e.cooldown = 15 + index * 6;
		e.dataTracker.set(ELEMENT, f.element());
		e.dataTracker.set(ELEMENT2, f.element2());
		e.dataTracker.set(SIZE, f.size());
		e.dataTracker.set(POWER, Math.max(1, Math.round(power)));
		e.dataTracker.set(MODE, mode);
		e.dataTracker.set(OWNER, owner.getId());
		e.dataTracker.set(INDEX, index);
		e.dataTracker.set(TOTAL, total);
		e.dataTracker.set(SPEED, f.speed());
		Vec3d p = e.targetPos(owner, 0f);
		e.refreshPositionAndAngles(p.x, p.y, p.z, 0f, 0f);
		return e;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder b) {
		b.add(ELEMENT, 0); b.add(ELEMENT2, -1); b.add(SIZE, 3); b.add(POWER, 3); b.add(MODE, ELEMENTAL);
		b.add(OWNER, -1); b.add(INDEX, 0); b.add(TOTAL, 1); b.add(SPEED, 3);
	}

	public Element element() { return Element.byIndex(dataTracker.get(ELEMENT)); }
	public int element2() { return dataTracker.get(ELEMENT2); }
	public int sizeStat() { return dataTracker.get(SIZE); }
	public int powerStat() { return dataTracker.get(POWER); }
	public int mode() { return dataTracker.get(MODE); }

	private float speedMul() { return 0.4f + dataTracker.get(SPEED) * 0.15f; }

	/** Де має бути істота відносно господаря. */
	private Vec3d targetPos(Entity owner, float td) {
		int idx = dataTracker.get(INDEX), total = Math.max(1, dataTracker.get(TOTAL));
		float t = age + td;
		if (mode() == ORBIT) {
			float ang = t * 0.09f * speedMul() + idx * MathHelper.TAU / total;
			float r = 1.5f + sizeStat() * 0.22f;
			return owner.getPos().add(MathHelper.cos(ang) * r, 1.0 + 0.15 * MathHelper.sin(t * 0.1f + idx), MathHelper.sin(ang) * r);
		}
		float yaw = (owner instanceof LivingEntity le ? le.bodyYaw : owner.getYaw()) * MathHelper.RADIANS_PER_DEGREE;
		float spread = (idx - (total - 1) / 2f) * 0.9f;
		float a = yaw + MathHelper.PI + spread * 0.6f;
		double ox = -MathHelper.sin(a) * 1.2, oz = MathHelper.cos(a) * 1.2;
		return owner.getPos().add(ox, 2.1 + 0.18 * MathHelper.sin(t * 0.12f + idx * 1.7f), oz);
	}

	@Override
	public void tick() {
		super.tick();
		Entity owner = getWorld().getEntityById(dataTracker.get(OWNER));
		if (owner == null || !owner.isAlive()) {
			if (!getWorld().isClient && age > 5) discard();
			return;
		}
		Vec3d tp = targetPos(owner, 0f);
		if (mode() == ORBIT) setPosition(tp);
		else setPosition(getPos().lerp(tp, 0.3));
		if (getWorld().isClient) return;
		if (--life <= 0) { discard(); return; }
		if (!(owner instanceof ServerPlayerEntity sp) || !(getWorld() instanceof ServerWorld sw)) return;
		SpellContext ctx = new SpellContext(sw, sp, formula, power);
		if (mode() == ORBIT) {
			touched.replaceAll((k, v) -> v - 1);
			touched.values().removeIf(v -> v <= 0);
			float r = 0.8f + sizeStat() * 0.05f;
			for (LivingEntity le : sw.getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(r),
				e -> e != sp && e.isAlive() && !e.isSpectator() && !touched.containsKey(e.getId()))) {
				SpellForms.hit(ctx, le, 0.5f);
				touched.put(le.getId(), 12);
				sw.playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.PLAYERS, 0.5f, 1.6f);
			}
			return;
		}
		if (--cooldown > 0) return;
		double range = 9 + formula.homing() * 1.2;
		LivingEntity target = sp.getAttacker() != null && sp.getAttacker().isAlive() && sp.getAttacker().squaredDistanceTo(this) < range * range ? sp.getAttacker() : null;
		if (target == null) {
			double best = range * range;
			for (LivingEntity le : sw.getEntitiesByClass(LivingEntity.class, new Box(getPos(), getPos()).expand(range),
				e -> e instanceof Monster && e.isAlive())) {
				double d = le.squaredDistanceTo(this);
				if (d < best) { best = d; target = le; }
			}
		}
		if (target == null) { cooldown = 10; return; }
		SpellFormula mini = formula.withForm(SpellForm.PROJECTILE.ordinal()).withTrigger(0).withCount(1).withPierce(0)
			.withSize(Math.max(1, formula.size() / 2)).withRange(Math.max(2, formula.range()));
		WeaveProjectileEntity pr = new WeaveProjectileEntity(sw, sp, mini, power * 0.5f);
		pr.setPosition(getX(), getY(), getZ());
		Vec3d d = target.getEyePos().subtract(getPos());
		pr.setVelocity(d.x, d.y, d.z, 0.9f + formula.speedMul() * 0.5f, 1f);
		sw.spawnEntity(pr);
		sw.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.4f, 1.6f);
		cooldown = Math.max(8, (int) (36 / formula.speedMul()));
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {}
}
