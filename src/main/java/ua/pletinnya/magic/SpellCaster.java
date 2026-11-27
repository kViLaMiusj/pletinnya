package ua.pletinnya.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.entity.WeaveProjectileEntity;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.fx.Vfx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Сплітає закляття: перевіряє закони, стягує ману, кидає жереб наслідку й вивільняє ефект. */
public final class SpellCaster {
	private SpellCaster() {}

	private static final Map<UUID, Long> LAST_CAST = new HashMap<>();
	private static final int CAST_COOLDOWN = 10;

	public static void castSelected(ServerPlayerEntity p) {
		MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
		SpellFormula f = d.selectedSpell();
		if (f.isEmpty()) {
			p.sendMessage(Text.translatable("message.pletinnya.empty_slot").formatted(Formatting.GRAY), true);
			return;
		}
		cast(p, d, f);
	}

	/** Драконяча мова: без розрахунків і без помилок, але тіло платить за навантаження. */
	public static void castDragon(ServerPlayerEntity p, MageData d, SpellFormula f) {
		if (p.isSpectator() || !p.isAlive()) return;
		long now = WeaveManager.now();
		Long last = LAST_CAST.get(p.getUuid());
		if (last != null && now - last < CAST_COOLDOWN * 2) return;
		float cost = WeavingLaws.cost(p, d, f) * (Gear.fullSet(p, MagicPath.DRAGON) ? 0.9f : 1.3f);
		if (!p.isCreative() && d.mana < cost) {
			p.sendMessage(Text.translatable("message.pletinnya.no_mana", (int) Math.ceil(cost), (int) d.mana).formatted(Formatting.BLUE), true);
			return;
		}
		LAST_CAST.put(p.getUuid(), now);
		if (!p.isCreative()) d.mana -= cost;
		ServerWorld w = p.getServerWorld();
		float strain = 1f + f.power() * 0.6f;
		float cond = WeavingLaws.effectiveConductivity(p, d);
		if (cost > cond) strain += (cost / cond - 1f) * 3f;
		if (Gear.holdingFocus(p, MagicPath.DRAGON)) strain *= 0.5f;
		if (d.paths.deity == 3) strain *= 0.5f;
		if (Gear.fullSet(p, MagicPath.DRAGON)) strain *= 0.6f;
		if (!p.isCreative()) {
			p.damage(w.getDamageSources().magic(), strain);
			p.addExhaustion(2f);
		}
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.6f, 1.5f);
		Vfx.send(w, Vfx.DRAGON, f.element(), p.getEyePos(), p.getRotationVec(1f), f.power(), f.power());
		ua.pletinnya.fx.Anim.play(p, ua.pletinnya.fx.Anim.ROAR, f.power());
		for (ServerPlayerEntity o : w.getPlayers()) {
			if (o.squaredDistanceTo(p) < 48 * 48) {
				o.sendMessage(Text.literal("«" + f.name() + "»").formatted(Formatting.GOLD, Formatting.BOLD), true);
			}
		}
		execute(p, f, f.power());
		d.addXp(f.elementType(), f.complexity() / 2);
		d.markDirty();
	}

	public static void cast(ServerPlayerEntity p, MageData d, SpellFormula f) {
		if (p.isSpectator() || !p.isAlive()) return;
		long now = WeaveManager.now();
		Long last = LAST_CAST.get(p.getUuid());
		if (last != null && now - last < CAST_COOLDOWN) return;

		Text err = WeavingLaws.weaveError(d, f);
		if (err != null) {
			p.sendMessage(err.copy().formatted(Formatting.RED), true);
			return;
		}

		float cost = WeavingLaws.cost(p, d, f);
		if (!p.isCreative() && d.mana < cost) {
			p.sendMessage(Text.translatable("message.pletinnya.no_mana", (int) Math.ceil(cost), (int) d.mana).formatted(Formatting.BLUE), true);
			return;
		}

		// Закон навантаження
		boolean sustained = f.formType() == SpellForm.CONSTRUCT
			|| (f.triggerType() != SpellTrigger.IMMEDIATE && f.formType() != SpellForm.PROJECTILE);
		int load = WeaveManager.load(p.getUuid());
		if (sustained && load >= Ranks.loadLimit(d.rank)) {
			p.sendMessage(Text.translatable("message.pletinnya.overloaded", load, Ranks.loadLimit(d.rank)).formatted(Formatting.GOLD), true);
			return;
		}

		LAST_CAST.put(p.getUuid(), now);
		if (!p.isCreative()) d.mana -= cost;
		d.markDirty();

		Random rnd = p.getRandom();
		ServerWorld w = p.getServerWorld();

		// Закон наслідку: перевищення провідності
		float cond = WeavingLaws.effectiveConductivity(p, d);
		float overload = cost / Math.max(1f, cond);
		if (overload > 1f) {
			p.damage(w.getDamageSources().magic(), (overload - 1f) * 3f);
			d.channelDamage += (int) ((overload - 1f) * 400) + 100;
			p.sendMessage(Text.translatable("message.pletinnya.overload").formatted(Formatting.DARK_RED), false);
			Vfx.send(w, Vfx.OVERLOAD, f.element(), p.getPos(), 1f);
			if (overload > 3f) {
				w.spawnParticles(ParticleTypes.LARGE_SMOKE, p.getX(), p.getBodyY(0.6), p.getZ(), 20, 0.3, 0.4, 0.3, 0.02);
				p.sendMessage(Text.translatable("message.pletinnya.overload_fail").formatted(Formatting.DARK_RED), true);
				return;
			}
		}

		// Закон наслідку: помилка у формулі
		float chance = WeavingLaws.misfireChance(p, d, f, load, cost);
		float power = f.power();
		SpellFormula effective = f;
		boolean misfired = false;
		if (rnd.nextFloat() < chance) {
			misfired = true;
			Vfx.send(w, Vfx.MISFIRE, f.element(), p.getPos().add(0, 1.2, 0), 1f);
			ua.pletinnya.fx.Anim.play(p, ua.pletinnya.fx.Anim.RECOIL, 1f);
			float roll = rnd.nextFloat();
			if (roll < 0.4f) {
				w.spawnParticles(ParticleTypes.SMOKE, p.getX(), p.getBodyY(0.7), p.getZ(), 15, 0.3, 0.3, 0.3, 0.02);
				w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.6f, 1.4f);
				p.sendMessage(Text.translatable("message.pletinnya.misfire_dissipate").formatted(Formatting.GRAY), true);
				return;
			} else if (roll < 0.7f) {
				power *= 0.5f;
				p.sendMessage(Text.translatable("message.pletinnya.misfire_partial").formatted(Formatting.YELLOW), true);
			} else if (roll < 0.9f) {
				Element[] rel = f.elementType().related();
				Element changed = rel[rnd.nextInt(rel.length)];
				effective = f.withElement(changed.ordinal());
				p.sendMessage(Text.translatable("message.pletinnya.misfire_altered", changed.displayName()).formatted(Formatting.LIGHT_PURPLE), true);
			} else {
				p.damage(w.getDamageSources().magic(), 2f + f.complexity() * 0.2f);
				d.channelDamage += 300;
				p.sendMessage(Text.translatable("message.pletinnya.misfire_backlash").formatted(Formatting.DARK_RED), false);
				return;
			}
		}

		PathData pd = d.paths;
		if (pd.spirit > 0 && pd.spiritElement() == effective.elementType()) {
			pd.spiritBond = Math.min(100, pd.spiritBond + 1);
			float will = 0.15f - pd.spiritBond * 0.001f;
			if (rnd.nextFloat() < will) {
				SpellForm nf = SpellForm.byIndex(rnd.nextInt(SpellForm.values().length));
				effective = effective.withForm(nf.ordinal());
				power *= 1.2f;
				p.sendMessage(Text.translatable("message.pletinnya.spirit_will", nf.displayName()).formatted(Formatting.AQUA), true);
			}
		}
		execute(p, effective, power);
		grow(p, d, f, cost, cond, chance, misfired);
		Ranks.checkTrial(p, d);
	}

	private static void execute(ServerPlayerEntity p, SpellFormula f, float power) {
		ServerWorld w = p.getServerWorld();
		SpellContext ctx = new SpellContext(w, p, f, power);
		Direction facing = p.getHorizontalFacing();
		boolean now = f.triggerType() == SpellTrigger.IMMEDIATE;
		Vfx.sendP(w, Vfx.CAST, f.element(), p.getPos(), 1.3f + f.complexity() * 0.04f, power);
		ua.pletinnya.fx.Anim.play(p, switch (f.formType()) {
			case PROJECTILE, TOUCH -> ua.pletinnya.fx.Anim.THRUST;
			case AREA, CONSTRUCT -> ua.pletinnya.fx.Anim.RAISE;
			case SELF -> ua.pletinnya.fx.Anim.SELF;
		}, power);

		switch (f.formType()) {
			case PROJECTILE -> {
				WeaveProjectileEntity proj = new WeaveProjectileEntity(w, p, f, power);
				proj.setVelocity(p, p.getPitch(), p.getYaw(), 0f, 1.2f + f.range() * 0.08f, 0.5f);
				w.spawnEntity(proj);
				w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.7f, 1.2f);
			}
			case SELF -> {
				if (now) SpellEffects.self(ctx, p);
				else WeaveManager.rune(ctx, p.getPos(), p, facing);
			}
			case TOUCH -> {
				Target t = find(p, 4.5);
				if (now) {
					if (t.entity() instanceof LivingEntity le) SpellEffects.entity(ctx, le, power * 1.25f);
					else SpellEffects.area(ctx, t.pos(), 1.5f);
				} else {
					WeaveManager.rune(ctx, t.pos(), t.entity() instanceof LivingEntity ? t.entity() : null, facing);
				}
			}
			case AREA -> {
				Target t = find(p, f.reach());
				if (now) SpellEffects.area(ctx, t.pos(), f.radius());
				else WeaveManager.rune(ctx, t.pos(), null, facing);
			}
			case CONSTRUCT -> {
				Target t = find(p, f.reach());
				if (now) SpellEffects.construct(ctx, t.placePos(), facing);
				else WeaveManager.rune(ctx, Vec3d.ofBottomCenter(t.placePos()), null, facing);
			}
		}
		p.swingHand(Hand.MAIN_HAND, true);
	}

	/** Маг росте, коли користується магією. */
	private static void grow(ServerPlayerEntity p, MageData d, SpellFormula f, float cost, float cond, float chance, boolean misfired) {
		Random r = p.getRandom();
		int cx = f.complexity();
		d.casts++;
		d.addXp(f.elementType(), Math.max(1, cx));
		if (d.maxMana < Ranks.manaCap(d.rank) && r.nextFloat() < cost / Math.max(10f, d.maxMana * 0.5f)) {
			d.maxMana = Math.min(Ranks.manaCap(d.rank), d.maxMana + 1 + d.maxMana / 100);
		}
		if (d.conductivity < Ranks.conductivityCap(d.rank) && cost > 0.7f * cond && r.nextFloat() < 0.3f) {
			d.conductivity = Math.min(Ranks.conductivityCap(d.rank), d.conductivity + 1 + d.conductivity / 50);
		}
		if (d.precision < 100 && chance > 0.05f && chance < 0.6f && r.nextFloat() < 0.25f) d.precision++;
		if (f.triggerType() != SpellTrigger.IMMEDIATE) d.addFeat(MageData.FEAT_TRIGGER);
		if (cx >= 36) d.addFeat(MageData.FEAT_COMPLEX);
		if (cost >= 400f && !misfired) d.addFeat(MageData.FEAT_GRAND);
		d.markDirty();
	}

	public record Target(Vec3d pos, @Nullable Entity entity, BlockPos placePos) {}

	public static Target find(ServerPlayerEntity p, double reach) {
		Vec3d eye = p.getEyePos();
		Vec3d look = p.getRotationVec(1f);
		Vec3d end = eye.add(look.multiply(reach));
		HitResult block = p.raycast(reach, 1f, false);
		double maxSq = block.getType() != HitResult.Type.MISS ? block.getPos().squaredDistanceTo(eye) : reach * reach;
		Box box = p.getBoundingBox().stretch(look.multiply(reach)).expand(1.0);
		EntityHitResult eh = ProjectileUtil.raycast(p, eye, end, box, e -> !e.isSpectator() && e.canHit(), maxSq);
		if (eh != null) {
			return new Target(eh.getPos(), eh.getEntity(), eh.getEntity().getBlockPos());
		}
		if (block instanceof BlockHitResult bh && bh.getType() == HitResult.Type.BLOCK) {
			return new Target(bh.getPos(), null, bh.getBlockPos().offset(bh.getSide()));
		}
		return new Target(end, null, BlockPos.ofFloored(end));
	}

	public static void forget(UUID id) {
		LAST_CAST.remove(id);
	}
}
