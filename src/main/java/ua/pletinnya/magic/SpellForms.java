package ua.pletinnya.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.entity.SpellSpiritEntity;
import ua.pletinnya.entity.WeaveProjectileEntity;
import ua.pletinnya.fx.Vfx;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Доставка заклять усіма формами: снаряд, область, промінь, конус, ланцюг, дощ, елементалі, орбіта,
 * хвиля, щит, ривок... Тут же злиття стихій (друга стихія діє поверх першої) і обереги «відповіді на удар».
 */
public final class SpellForms {
	private SpellForms() {}

	// ====================================================================== злиття

	private static SpellContext secondary(SpellContext ctx) {
		return new SpellContext(ctx.world(), ctx.caster(), ctx.formula().asSecondary(), ctx.power() * Fusion.secondaryMul(ctx.formula()));
	}

	private static SpellContext primary(SpellContext ctx) {
		float m = Fusion.primaryMul(ctx.formula());
		return m == 1f ? ctx : ctx.withPower(ctx.power() * m);
	}

	/** Удар по істоті з урахуванням злиття. */
	public static void hit(SpellContext ctx, LivingEntity t, float mul) {
		if (ctx.isCaster(t)) return;
		SpellEffects.entity(primary(ctx), t, ctx.power() * mul * Fusion.primaryMul(ctx.formula()));
		if (ctx.formula().fused() && t.isAlive()) {
			SpellContext s = secondary(ctx);
			SpellEffects.entity(s, t, s.power() * mul);
		}
	}

	public static void area(SpellContext ctx, Vec3d c, float r) {
		SpellEffects.area(primary(ctx), c, r);
		if (ctx.formula().fused()) SpellEffects.area(secondary(ctx), c, r * 0.85f);
	}

	public static void impact(SpellContext ctx, Vec3d pos, @Nullable Entity hit) {
		SpellEffects.impact(primary(ctx), pos, hit);
		if (ctx.formula().fused()) {
			SpellContext s = secondary(ctx);
			if (hit instanceof LivingEntity le && le.isAlive()) SpellEffects.entity(s, le, s.power());
			else SpellEffects.area(s, pos, Math.max(1.2f, ctx.formula().radius() * 0.7f));
			Vfx.sendP(ctx.world(), Vfx.IMPACT, s.element().ordinal(), pos, Math.max(1f, ctx.formula().radius() * 0.6f), s.power());
		}
	}

	// ====================================================================== доставка

	/** Вивільнити закляття формою формули від імені мага (з його поточного місця й погляду). */
	public static void deliver(ServerPlayerEntity p, SpellFormula f, float power) {
		ServerWorld w = p.getServerWorld();
		SpellContext ctx = new SpellContext(w, p, f, power);
		Direction facing = p.getHorizontalFacing();
		boolean rune = f.triggerType().isRune();
		switch (f.formType()) {
			case PROJECTILE -> projectiles(p, f, power);
			case SELF -> {
				if (rune) WeaveManager.rune(ctx, p.getPos(), p, facing);
				else {
					SpellEffects.self(primary(ctx), p);
					if (f.fused()) SpellEffects.self(secondary(ctx), p);
				}
			}
			case TOUCH -> {
				SpellCaster.Target t = SpellCaster.find(p, 4.5);
				if (rune) WeaveManager.rune(ctx, t.pos(), t.entity() instanceof LivingEntity ? t.entity() : null, facing);
				else if (t.entity() instanceof LivingEntity le) hit(ctx, le, 1.25f);
				else area(ctx, t.pos(), 1.5f);
			}
			case AREA -> {
				SpellCaster.Target t = SpellCaster.find(p, f.reach());
				if (rune) WeaveManager.rune(ctx, t.pos(), null, facing);
				else area(ctx, t.pos(), f.radius());
			}
			case CONSTRUCT -> {
				SpellCaster.Target t = SpellCaster.find(p, f.reach());
				if (rune) WeaveManager.rune(ctx, Vec3d.ofBottomCenter(t.placePos()), null, facing);
				else SpellEffects.construct(ctx, t.placePos(), facing);
			}
			case BEAM -> {
				if (rune) { WeaveManager.rune(ctx, SpellCaster.find(p, f.reach()).pos(), null, facing); break; }
				int pulses = 1 + f.duration() / 3;
				beam(p, ctx, 1f);
				if (pulses > 1) SpellScheduler.every(5, 5, i -> {
					if (!p.isAlive() || p.isRemoved()) return false;
					beam(p, ctx, 0.6f);
					return i + 2 < pulses;
				});
			}
			case CONE -> {
				if (rune) { WeaveManager.rune(ctx, p.getPos(), null, facing); break; }
				cone(p, ctx);
			}
			case CHAIN -> chain(p, ctx);
			case RAIN -> {
				SpellCaster.Target t = SpellCaster.find(p, f.reach());
				if (rune) WeaveManager.rune(ctx, t.pos(), null, facing);
				else rain(ctx, groundAt(w, t.pos()));
			}
			case ELEMENTAL -> {
				int n = Math.min(4, f.count());
				for (int i = 0; i < n; i++) w.spawnEntity(SpellSpiritEntity.create(w, p, f, power, SpellSpiritEntity.ELEMENTAL, i, n));
				Vfx.send(w, Vfx.ELEMENTAL, f.element(), p.getPos().add(0, 2, 0), Vec3d.ZERO, 1.5f + f.size() * 0.1f, power);
				w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.PLAYERS, 0.7f, 1.3f);
			}
			case ORBIT -> {
				int n = Math.min(8, f.count() + 1);
				for (int i = 0; i < n; i++) w.spawnEntity(SpellSpiritEntity.create(w, p, f, power, SpellSpiritEntity.ORBIT, i, n));
				w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 0.5f, 1.8f);
			}
			case WAVE -> {
				if (rune) { WeaveManager.rune(ctx, p.getPos(), null, facing); break; }
				wave(p, ctx);
			}
			case SHIELD -> shield(p, ctx);
			case BLINK -> blink(p, ctx);
		}
		p.swingHand(Hand.MAIN_HAND, true);
	}

	// ------------------------------------------------------------------ снаряди (залп, швидкість, пробивання, наведення)
	private static void projectiles(ServerPlayerEntity p, SpellFormula f, float power) {
		ServerWorld w = p.getServerWorld();
		int n = f.count();
		float each = n == 1 ? power : power * (0.45f + 0.55f / n) * 1.25f;
		float spread = n == 1 ? 0f : 6f + n * 2.5f;
		float vel = (1.0f + f.range() * 0.07f) * f.speedMul() * 1.25f;
		for (int i = 0; i < n; i++) {
			WeaveProjectileEntity proj = new WeaveProjectileEntity(w, p, f, each);
			float yawOff = n == 1 ? 0f : -spread / 2 + spread * i / (n - 1);
			float pitchOff = n <= 2 ? 0f : ((i % 2) == 0 ? -1.5f : 1.5f);
			proj.setVelocity(p, p.getPitch() + pitchOff, p.getYaw() + yawOff, 0f, vel, 0.3f);
			w.spawnEntity(proj);
		}
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.7f, 1.2f + n * 0.03f);
	}

	// ------------------------------------------------------------------ промінь
	private static void beam(ServerPlayerEntity p, SpellContext ctx, float mul) {
		ServerWorld w = p.getServerWorld();
		SpellFormula f = ctx.formula();
		Vec3d eye = p.getEyePos();
		Vec3d look = p.getRotationVec(1f);
		double reach = f.reach();
		BlockHitResult bh = w.raycast(new RaycastContext(eye, eye.add(look.multiply(reach)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, p));
		double len = bh.getType() == HitResult.Type.MISS ? reach : bh.getPos().distanceTo(eye);
		float width = 0.3f + f.size() * 0.08f;
		Vec3d end = eye.add(look.multiply(len));
		List<LivingEntity> hits = new ArrayList<>();
		for (LivingEntity le : w.getEntitiesByClass(LivingEntity.class, new Box(eye, end).expand(width + 1), e -> e != p && e.isAlive() && !e.isSpectator())) {
			if (le.getBoundingBox().expand(width).raycast(eye, end).isPresent()) hits.add(le);
		}
		hits.sort(Comparator.comparingDouble(e -> e.squaredDistanceTo(eye)));
		int max = 1 + f.pierce();
		float m = mul;
		for (int i = 0; i < hits.size() && i < max; i++) {
			hit(ctx, hits.get(i), m);
			m *= 0.85f;
			if (i == max - 1 && f.pierce() < hits.size()) { len = hits.get(i).getPos().add(0, hits.get(i).getHeight() / 2, 0).distanceTo(eye); }
		}
		end = eye.add(look.multiply(len));
		if (bh.getType() == HitResult.Type.BLOCK && hits.size() <= max) area(ctx.withPower(ctx.power() * 0.4f * mul), end, 1f + f.size() * 0.12f);
		Vec3d from = eye.add(look.multiply(0.8)).add(0, -0.25, 0);
		Vfx.send(w, Vfx.BEAM, f.element(), from, look, width, ctx.power(), (float) len, f.fused() ? f.element2() : -1);
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 0.5f, 1.8f);
	}

	// ------------------------------------------------------------------ конус
	private static void cone(ServerPlayerEntity p, SpellContext ctx) {
		ServerWorld w = p.getServerWorld();
		SpellFormula f = ctx.formula();
		Vec3d eye = p.getEyePos();
		Vec3d look = p.getRotationVec(1f);
		float half = 22f + f.size() * 4f;
		float dist = 4f + f.range() * 1.6f;
		double cos = Math.cos(Math.toRadians(half));
		for (LivingEntity le : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(dist), e -> e != p && e.isAlive() && !e.isSpectator())) {
			Vec3d to = le.getPos().add(0, le.getHeight() / 2, 0).subtract(eye);
			double d = to.length();
			if (d > dist || d < 0.01) continue;
			if (to.normalize().dotProduct(look) < cos) continue;
			hit(ctx, le, (float) (1.0 - 0.45 * d / dist));
		}
		Vfx.send(w, Vfx.CONE, f.element(), eye.add(0, -0.3, 0), look, half, ctx.power(), dist, f.fused() ? f.element2() : -1);
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SpellEffects.soundOf(f.elementType()), SoundCategory.PLAYERS, 0.8f, 1.0f);
	}

	// ------------------------------------------------------------------ ланцюг
	private static void chain(ServerPlayerEntity p, SpellContext ctx) {
		ServerWorld w = p.getServerWorld();
		SpellFormula f = ctx.formula();
		LivingEntity first = null;
		SpellCaster.Target t = SpellCaster.find(p, f.reach());
		if (t.entity() instanceof LivingEntity le && le != p) first = le;
		if (first == null) {
			// наведення: шукаємо ціль у конусі погляду
			Vec3d eye = p.getEyePos(), look = p.getRotationVec(1f);
			double cos = Math.cos(Math.toRadians(8 + f.homing() * 4));
			double best = Double.MAX_VALUE;
			for (LivingEntity le : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(f.reach()), e -> e != p && e.isAlive() && !e.isSpectator())) {
				Vec3d to = le.getEyePos().subtract(eye);
				if (to.length() > f.reach() || to.normalize().dotProduct(look) < cos) continue;
				if (to.lengthSquared() < best) { best = to.lengthSquared(); first = le; }
			}
		}
		Vec3d hand = p.getEyePos().add(p.getRotationVec(1f).multiply(0.8)).add(0, -0.3, 0);
		if (first == null) {
			Vfx.send(w, Vfx.CHAIN, f.element(), hand, t.pos().subtract(hand), 0.15f, ctx.power(), 0f, -1);
			area(ctx.withPower(ctx.power() * 0.5f), t.pos(), 1.2f);
			return;
		}
		int jumps = 1 + f.count();
		double jumpR = 4 + f.range() * 0.6 + f.homing() * 0.5;
		int delay = Math.max(1, (int) (5 / f.speedMul()));
		Set<Integer> done = new HashSet<>();
		LivingEntity[] cur = {first};
		Vec3d[] from = {hand};
		float[] m = {1f};
		SpellScheduler.every(1, delay, i -> {
			LivingEntity c = cur[0];
			if (c == null) return false;
			Vec3d to = c.getPos().add(0, c.getHeight() * 0.6, 0);
			Vfx.send(w, Vfx.CHAIN, f.element(), from[0], to.subtract(from[0]), 0.15f + f.size() * 0.02f, ctx.power(), 0f, f.fused() ? f.element2() : -1);
			hit(ctx, c, m[0]);
			done.add(c.getId());
			w.playSound(null, c.getX(), c.getY(), c.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 0.25f, 1.9f);
			if (i + 1 >= jumps) return false;
			LivingEntity next = null;
			double best = jumpR * jumpR;
			for (LivingEntity le : w.getEntitiesByClass(LivingEntity.class, c.getBoundingBox().expand(jumpR), e -> e != p && e.isAlive() && !e.isSpectator() && !done.contains(e.getId()))) {
				double d = le.squaredDistanceTo(c);
				if (d < best) { best = d; next = le; }
			}
			from[0] = to;
			cur[0] = next;
			m[0] *= 0.82f;
			return next != null;
		});
	}

	// ------------------------------------------------------------------ дощ із неба
	private static Vec3d groundAt(ServerWorld w, Vec3d pos) {
		BlockPos bp = BlockPos.ofFloored(pos);
		for (int i = 0; i < 6 && w.getBlockState(bp.down()).isAir(); i++) bp = bp.down();
		return Vec3d.ofBottomCenter(bp);
	}

	private static void rain(SpellContext ctx, Vec3d center) {
		ServerWorld w = ctx.world();
		SpellFormula f = ctx.formula();
		float r = f.radius();
		int drops = 4 + f.count() * 3 + f.power() / 2;
		int total = (int) (Math.max(20, f.durationTicks() / 2) / f.speedMul());
		int interval = Math.max(1, total / drops);
		Vfx.send(w, Vfx.RAIN_CLOUD, f.element(), center.add(0, 7, 0), Vec3d.ZERO, r, ctx.power(), total + 10, 0f);
		net.minecraft.util.math.random.Random rnd = w.getRandom();
		SpellContext dropCtx = ctx.withPower(ctx.power() * 0.45f);
		SpellScheduler.every(8, interval, i -> {
			double a = rnd.nextDouble() * Math.PI * 2, d = Math.sqrt(rnd.nextDouble()) * r;
			Vec3d pt = groundAt(w, center.add(Math.cos(a) * d, 2, Math.sin(a) * d));
			Vfx.send(w, Vfx.RAIN_DROP, f.element(), pt, Vec3d.ZERO, 0.6f + f.size() * 0.08f, ctx.power(), 0f, f.fused() ? f.element2() : -1);
			SpellScheduler.later(6, () -> area(dropCtx, pt, 0.9f + f.size() * 0.12f));
			return i + 1 < drops;
		});
	}

	// ------------------------------------------------------------------ хвиля
	private static void wave(ServerPlayerEntity p, SpellContext ctx) {
		ServerWorld w = p.getServerWorld();
		SpellFormula f = ctx.formula();
		Vec3d look = p.getRotationVec(1f);
		Vec3d dir = new Vec3d(look.x, 0, look.z);
		if (dir.lengthSquared() < 1e-4) dir = Vec3d.fromPolar(0, p.getYaw());
		dir = dir.normalize();
		Vec3d side = new Vec3d(-dir.z, 0, dir.x);
		Vec3d origin = p.getPos();
		float width = 1.6f + f.size() * 0.55f;
		float length = 5f + f.range() * 2f;
		float step = 0.35f + 0.25f * f.speedMul();
		int ticks = (int) Math.ceil(length / step);
		Vfx.send(w, Vfx.WAVE, f.element(), origin, dir, width, ctx.power(), length, step);
		Set<Integer> done = new HashSet<>();
		Vec3d d0 = dir;
		SpellScheduler.every(1, 1, i -> {
			double front = (i + 1) * step;
			Vec3d c = origin.add(d0.multiply(front));
			for (LivingEntity le : w.getEntitiesByClass(LivingEntity.class, new Box(c, c).expand(width / 2 + 1, 2.5, width / 2 + 1),
				e -> e != p && e.isAlive() && !e.isSpectator() && !done.contains(e.getId()))) {
				Vec3d rel = le.getPos().subtract(origin);
				double along = rel.dotProduct(d0), across = Math.abs(rel.dotProduct(side));
				if (Math.abs(along - front) <= step + 0.7 && across <= width / 2 + 0.4) {
					hit(ctx, le, 0.9f);
					done.add(le.getId());
					le.addVelocity(d0.x * 0.5, 0.25, d0.z * 0.5);
					le.velocityModified = true;
				}
			}
			return i + 1 < ticks;
		});
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SpellEffects.soundOf(f.elementType()), SoundCategory.PLAYERS, 0.9f, 0.8f);
	}

	// ------------------------------------------------------------------ щит і обереги
	private record Ward(SpellContext ctx, long until, int charges, boolean shield) {}

	private static final Map<UUID, Ward> SHIELDS = new HashMap<>();
	private static final Map<UUID, Ward> RETALIATE = new HashMap<>();
	private static final Map<UUID, Long> LAST_RETALIATE = new HashMap<>();

	private static void shield(ServerPlayerEntity p, SpellContext ctx) {
		SpellFormula f = ctx.formula();
		int dur = f.durationTicks() * 2;
		int amp = MathHelper.clamp((int) (ctx.power() / 2.5f) + f.size() / 4, 0, 6);
		p.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, dur, amp, false, false, true));
		if (ctx.power() >= 7) p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, dur, 0, false, false, true));
		SHIELDS.put(p.getUuid(), new Ward(ctx, p.getServerWorld().getTime() + dur, 99, true));
		Vfx.send(p.getServerWorld(), Vfx.SHIELD, f.element(), p.getPos(), Vec3d.ZERO, 1.1f + f.size() * 0.06f, ctx.power(), p.getId(), dur);
		p.getServerWorld().playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 0.6f, 1.5f);
	}

	/** Відповідь на удар: оберіг чекає, поки мага вдарять. */
	public static void armRetaliate(ServerPlayerEntity p, SpellFormula f, float power) {
		SpellContext ctx = new SpellContext(p.getServerWorld(), p, f, power);
		RETALIATE.put(p.getUuid(), new Ward(ctx, p.getServerWorld().getTime() + f.durationTicks() * 6L, 1 + f.duration() / 4, false));
		Vfx.send(p.getServerWorld(), Vfx.SHIELD, f.element(), p.getPos(), Vec3d.ZERO, 0.9f, power * 0.5f, p.getId(), 30);
	}

	/** Викликається, коли гравця поранено. */
	public static void onDamaged(ServerPlayerEntity p, DamageSource src) {
		if (!(src.getAttacker() instanceof LivingEntity att) || att == p) return;
		long now = p.getServerWorld().getTime();
		Ward sh = SHIELDS.get(p.getUuid());
		if (sh != null) {
			if (now > sh.until || sh.ctx.world() != p.getServerWorld()) SHIELDS.remove(p.getUuid());
			else if (att.squaredDistanceTo(p) < 36) hit(sh.ctx, att, 0.35f);   // щит обпікає/морозить кривдника
		}
		Ward r = RETALIATE.get(p.getUuid());
		if (r == null) return;
		if (now > r.until || r.ctx.world() != p.getServerWorld()) { RETALIATE.remove(p.getUuid()); return; }
		Long last = LAST_RETALIATE.get(p.getUuid());
		if (last != null && now - last < 10) return;
		LAST_RETALIATE.put(p.getUuid(), now);
		if (r.charges <= 1) RETALIATE.remove(p.getUuid());
		else RETALIATE.put(p.getUuid(), new Ward(r.ctx, r.until, r.charges - 1, false));
		// розвертаємо мага до кривдника на мить вивільнення
		float yaw = p.getYaw(), pitch = p.getPitch();
		Vec3d to = att.getEyePos().subtract(p.getEyePos());
		p.setYaw((float) (MathHelper.atan2(to.z, to.x) * MathHelper.DEGREES_PER_RADIAN) - 90f);
		p.setPitch((float) -(MathHelper.atan2(to.y, Math.sqrt(to.x * to.x + to.z * to.z)) * MathHelper.DEGREES_PER_RADIAN));
		SpellFormula f = r.ctx.formula().withTrigger(SpellTrigger.IMMEDIATE.ordinal());
		Vfx.sendP(p.getServerWorld(), Vfx.CAST, f.element(), p.getPos(), 1.3f, r.ctx.power());
		ua.pletinnya.fx.Anim.play(p, ua.pletinnya.fx.Anim.THRUST, r.ctx.power());
		deliver(p, f, r.ctx.power());
		p.setYaw(yaw);
		p.setPitch(pitch);
	}

	public static void forget(UUID id) {
		SHIELDS.remove(id);
		RETALIATE.remove(id);
		LAST_RETALIATE.remove(id);
	}

	// ------------------------------------------------------------------ ривок
	private static void blink(ServerPlayerEntity p, SpellContext ctx) {
		ServerWorld w = p.getServerWorld();
		SpellFormula f = ctx.formula();
		Vec3d eye = p.getEyePos();
		Vec3d look = p.getRotationVec(1f);
		double dist = Math.min(f.reach() * 0.5, 4 + f.range() * 1.6);
		BlockHitResult bh = w.raycast(new RaycastContext(eye, eye.add(look.multiply(dist)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, p));
		Vec3d target = bh.getType() == HitResult.Type.MISS ? eye.add(look.multiply(dist)) : bh.getPos().subtract(look.multiply(0.6));
		Vec3d feet = target.subtract(0, p.getStandingEyeHeight(), 0);
		for (int i = 0; i < 3 && !w.isSpaceEmpty(p, p.getBoundingBox().offset(feet.subtract(p.getPos()))); i++) feet = feet.add(0, 1, 0);
		Vec3d from = p.getPos();
		area(ctx.withPower(ctx.power() * 0.5f), from, 1.2f + f.size() * 0.15f);
		p.requestTeleport(feet.x, feet.y, feet.z);
		p.fallDistance = 0;
		Vfx.send(w, Vfx.BLINK, f.element(), from, feet.subtract(from), 1f, ctx.power(), 0f, f.fused() ? f.element2() : -1);
		SpellScheduler.later(2, () -> area(ctx.withPower(ctx.power() * 0.5f), p.getPos(), 1.2f + f.size() * 0.15f));
		w.playSound(null, from.x, from.y, from.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.6f, 1.5f);
	}

	// ------------------------------------------------------------------ пульсація
	public static void pulse(ServerPlayerEntity p, SpellFormula f, float power) {
		int repeats = 2 + f.duration() / 3;
		int interval = Math.max(10, (int) (40 / f.speedMul()));
		SpellFormula once = f.withTrigger(SpellTrigger.IMMEDIATE.ordinal());
		SpellScheduler.every(interval, interval, i -> {
			if (!p.isAlive() || p.isRemoved()) return false;
			Vfx.sendP(p.getServerWorld(), Vfx.CAST, f.element(), p.getPos(), 1.0f, power);
			ua.pletinnya.fx.Anim.play(p, SpellCaster.animFor(f.formType()), power);
			deliver(p, once, power * 0.7f);
			return i + 1 < repeats;
		});
	}
}
