package ua.pletinnya.magic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.entity.WeaveProjectileEntity;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.fx.Vfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Тривалі плетіння: тимчасові стіни, поля та руни з умовою спрацювання.
 * Саме вони займають «увагу» мага за Законом навантаження.
 */
public final class WeaveManager {
	private WeaveManager() {}

	public enum Kind { BLOCKS, RUNE, FIELD }

	public static final class Weave {
		@Nullable final UUID owner;
		final RegistryKey<World> world;
		final Kind kind;
		final SpellFormula formula;
		final float power;
		final int complexity;
		final float ownerPrecision;
		final boolean countsLoad;
		Vec3d pos;
		@Nullable UUID anchor;
		Direction facing = Direction.NORTH;
		final List<BlockPos> blocks = new ArrayList<>();
		final List<Block> placed = new ArrayList<>();
		long fireAt;
		long expireAt;
		long nextPulse;

		Weave(SpellContext ctx, Kind kind, Vec3d pos, boolean countsLoad) {
			this.owner = ctx.caster() != null ? ctx.caster().getUuid() : null;
			this.world = ctx.world().getRegistryKey();
			this.kind = kind;
			this.formula = ctx.formula();
			this.power = ctx.power();
			this.complexity = ctx.formula().complexity();
			this.ownerPrecision = ctx.caster() != null ? ctx.caster().getAttachedOrCreate(ModAttachments.MAGE).precision : 0;
			this.countsLoad = countsLoad;
			this.pos = pos;
		}

		Vec3d center() {
			return kind == Kind.BLOCKS && !blocks.isEmpty() ? Vec3d.ofCenter(blocks.get(blocks.size() / 2)) : pos;
		}
	}

	private static final List<Weave> WEAVES = new ArrayList<>();
	private static long now = 0;

	public static long now() { return now; }

	// ------------------------------------------------------------------
	// Створення
	// ------------------------------------------------------------------

	public static Weave temporary(SpellContext ctx, int life, boolean countsLoad) {
		Weave w = new Weave(ctx, Kind.BLOCKS, Vec3d.ZERO, countsLoad);
		w.expireAt = now + life;
		return w;
	}

	public static boolean placeTemp(ServerWorld world, BlockPos pos, BlockState state, Weave weave) {
		BlockState cur = world.getBlockState(pos);
		boolean free = cur.isAir() || (cur.isReplaceable() && cur.getFluidState().isEmpty());
		if (!free) return false;
		world.setBlockState(pos, state);
		weave.blocks.add(pos.toImmutable());
		weave.placed.add(state.getBlock());
		return true;
	}

	public static void commit(Weave weave) {
		if (weave.blocks.isEmpty()) return;
		weave.pos = weave.center();
		add(weave);
	}

	public static void field(SpellContext ctx, Vec3d center, int life) {
		Weave w = new Weave(ctx, Kind.FIELD, center, true);
		w.expireAt = now + life;
		w.nextPulse = now;
		add(w);
	}

	public static void rune(SpellContext ctx, Vec3d pos, @Nullable Entity anchor, Direction facing) {
		Weave w = new Weave(ctx, Kind.RUNE, pos, true);
		w.anchor = anchor != null ? anchor.getUuid() : null;
		w.facing = facing;
		SpellFormula f = ctx.formula();
		if (f.triggerType() == SpellTrigger.DELAYED) {
			w.fireAt = now + f.delayTicks();
			w.expireAt = w.fireAt + 1;
		} else {
			w.fireAt = now + 10;
			w.expireAt = now + Math.max(1200, f.durationTicks() * 6L);
		}
		add(w);
		Vfx.send(ctx.world(), Vfx.RUNE, ctx.element().ordinal(), pos, Math.max(1f, f.radius() * 0.6f));
	}

	private static void add(Weave w) {
		WEAVES.add(w);
		if (w.owner != null && w.countsLoad && load(w.owner) >= 2) {
			// Утримувати два плетіння одночасно — вимога випробування рангу III.
			pendingSustainedFeat.add(w.owner);
		}
	}

	private static final List<UUID> pendingSustainedFeat = new ArrayList<>();

	public static int load(UUID owner) {
		int n = 0;
		for (Weave w : WEAVES) if (w.countsLoad && owner.equals(w.owner)) n++;
		return n;
	}

	// ------------------------------------------------------------------
	// Щотіку
	// ------------------------------------------------------------------

	public static void tick(MinecraftServer server) {
		now++;
		for (UUID id : pendingSustainedFeat) {
			ServerPlayerEntity p = server.getPlayerManager().getPlayer(id);
			if (p != null) p.getAttachedOrCreate(ModAttachments.MAGE).addFeat(MageData.FEAT_SUSTAINED);
		}
		pendingSustainedFeat.clear();

		List<Weave> snapshot = new ArrayList<>(WEAVES);
		for (Weave w : snapshot) {
			ServerWorld world = server.getWorld(w.world);
			if (world == null) {
				WEAVES.remove(w);
				continue;
			}
			ServerPlayerEntity caster = w.owner != null ? server.getPlayerManager().getPlayer(w.owner) : null;
			if (caster != null && caster.getServerWorld() != world) caster = null;
			SpellContext ctx = new SpellContext(world, caster, w.formula, w.power);

			Entity anchor = null;
			if (w.anchor != null) {
				anchor = world.getEntity(w.anchor);
				if (anchor == null || !anchor.isAlive()) {
					fizzle(world, w);
					continue;
				}
				w.pos = anchor.getPos().add(0, anchor.getHeight() * 0.5, 0);
			}

			switch (w.kind) {
				case BLOCKS -> {
					if (now >= w.expireAt) remove(world, w);
				}
				case FIELD -> {
					if (now >= w.nextPulse) {
						SpellEffects.area(ctx.withPower(w.power * 0.5f), w.pos, w.formula.radius());
						w.nextPulse = now + 20;
					}
					if (now >= w.expireAt) remove(world, w);
				}
				case RUNE -> {
					boolean fire = false;
					if (w.formula.triggerType() == SpellTrigger.DELAYED) {
						fire = now >= w.fireAt;
					} else if (now >= w.fireAt && now % 5 == 0) {
						float r = Math.max(2f, w.formula.radius());
						final Entity anc = anchor;
						final UUID owner = w.owner;
						fire = !world.getEntitiesByClass(LivingEntity.class, new Box(w.pos, w.pos).expand(r),
							e -> e.isAlive() && !e.isSpectator() && e != anc && (owner == null || !owner.equals(e.getUuid()))
								&& e.squaredDistanceTo(w.pos) <= r * r).isEmpty();
					}
					if (fire) {
						WEAVES.remove(w);
						world.playSound(null, w.pos.x, w.pos.y, w.pos.z, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 0.6f, 1.6f);
						Vfx.send(world, Vfx.RUNE_FIRE, w.formula.element(), w.pos, w.formula.radius());
						Entity target = anchor;
						if (target == null && w.formula.formType() == SpellForm.SELF) target = caster;
						SpellEffects.release(ctx, w.pos, target, w.facing);
					} else if (now >= w.expireAt) {
						fizzle(world, w);
					}
				}
			}

			if (now % 10 == 0 && WEAVES.contains(w)) showToPerceptive(world, w);
		}
	}

	/** Закон сприйняття: чуже плетіння видно лише тому, хто здатен його відчути. */
	private static void showToPerceptive(ServerWorld world, Weave w) {
		Vec3d c = w.center();
		for (ServerPlayerEntity p : world.getPlayers()) {
			if (p.squaredDistanceTo(c) > 32 * 32) continue;
			boolean own = w.owner != null && w.owner.equals(p.getUuid());
			int perception = WeavingLaws.effectivePerception(p, p.getAttachedOrCreate(ModAttachments.MAGE));
			if (own || perception >= w.complexity * 1.5f) {
				world.spawnParticles(p, new ua.pletinnya.fx.FxEffects.Glyph(w.formula.element(), w.formula.elementType().color, 0.35f), true, c.x, c.y + 0.15, c.z, 1, 0.05, 0.02, 0.05, 0.0);
				world.spawnParticles(p, ParticleTypes.ENCHANT, true, c.x, c.y + 0.5, c.z, 3, 0.3, 0.3, 0.3, 0.3);
			}
		}
	}

	// ------------------------------------------------------------------
	// Протидія: придушення та розплутування
	// ------------------------------------------------------------------

	public static void dispel(SpellContext ctx, Vec3d center, float r) {
		ServerPlayerEntity caster = ctx.caster();
		MageData d = caster != null ? caster.getAttachedOrCreate(ModAttachments.MAGE) : null;
		float attack = ctx.power() * 5f + (d != null ? d.precision * 0.5f + WeavingLaws.effectivePerception(caster, d) * 0.3f : 0f);
		UUID me = caster != null ? caster.getUuid() : null;
		int undone = 0, failed = 0;

		Iterator<Weave> it = WEAVES.iterator();
		List<Weave> toRemove = new ArrayList<>();
		while (it.hasNext()) {
			Weave w = it.next();
			if (w.world != ctx.world().getRegistryKey()) continue;
			if (w.center().squaredDistanceTo(center) > (r + 1) * (r + 1)) continue;
			if (me != null && me.equals(w.owner)) {
				toRemove.add(w);
				undone++;
			} else if (attack >= w.complexity * 4f + w.ownerPrecision * 0.3f) {
				toRemove.add(w);
				undone++;
				notifyOwner(ctx.world().getServer(), w);
			} else {
				failed++;
			}
		}
		for (Weave w : toRemove) remove(ctx.world(), w);
		Vfx.send(ctx.world(), Vfx.DISPEL, Element.MANA.ordinal(), center, r);

		for (WeaveProjectileEntity proj : ctx.world().getEntitiesByClass(WeaveProjectileEntity.class, new Box(center, center).expand(r + 1), e -> true)) {
			Entity owner = proj.getOwner();
			if (owner != null && owner == caster) continue;
			if (attack >= proj.complexity() * 4f) {
				SpellEffects.burst(ctx.world(), Element.MANA, proj.getPos(), 15, 0.3);
				proj.discard();
				undone++;
			} else {
				failed++;
			}
		}

		if (caster != null && d != null) {
			if (undone > 0) {
				d.addFeat(MageData.FEAT_DISPEL);
				if (caster.getRandom().nextFloat() < 0.3f && d.perception < 100) {
					d.perception++;
					d.markDirty();
				}
				caster.sendMessage(Text.translatable("message.pletinnya.dispelled", undone).formatted(Formatting.AQUA), true);
			}
			if (failed > 0) caster.sendMessage(Text.translatable("message.pletinnya.dispel_failed", failed).formatted(Formatting.GRAY), false);
		}
	}

	/** Розплутує руни, прив'язані до конкретної істоти (наприклад, мітку на ворогові). */
	public static void dispelAnchored(SpellContext ctx, LivingEntity target) {
		float attack = ctx.power() * 5f;
		List<Weave> toRemove = new ArrayList<>();
		for (Weave w : WEAVES) {
			if (w.anchor == null || !w.anchor.equals(target.getUuid())) continue;
			if (ctx.caster() != null && ctx.caster().getUuid().equals(w.owner)) continue;
			if (attack >= w.complexity * 4f + w.ownerPrecision * 0.3f) toRemove.add(w);
		}
		for (Weave w : toRemove) {
			fizzle(ctx.world(), w);
			notifyOwner(ctx.world().getServer(), w);
		}
	}

	/** Маг припиняє власні плетіння (уміння рангу II). */
	public static int cancelOwn(ServerPlayerEntity p) {
		List<Weave> mine = new ArrayList<>();
		for (Weave w : WEAVES) if (p.getUuid().equals(w.owner)) mine.add(w);
		for (Weave w : mine) {
			ServerWorld world = p.getServer().getWorld(w.world);
			if (world != null) remove(world, w);
			else WEAVES.remove(w);
		}
		return mine.size();
	}

	private static void notifyOwner(MinecraftServer server, Weave w) {
		if (server == null || w.owner == null) return;
		ServerPlayerEntity o = server.getPlayerManager().getPlayer(w.owner);
		if (o != null) o.sendMessage(Text.translatable("message.pletinnya.weave_unravelled", w.formula.name()).formatted(Formatting.RED), false);
	}

	// ------------------------------------------------------------------
	// Прибирання
	// ------------------------------------------------------------------

	private static void fizzle(ServerWorld world, Weave w) {
		Vec3d c = w.center();
		world.spawnParticles(ParticleTypes.SMOKE, c.x, c.y, c.z, 10, 0.2, 0.2, 0.2, 0.01);
		remove(world, w);
	}

	private static void remove(ServerWorld world, Weave w) {
		WEAVES.remove(w);
		for (int i = 0; i < w.blocks.size(); i++) {
			BlockPos q = w.blocks.get(i);
			if (world.getBlockState(q).isOf(w.placed.get(i))) {
				world.setBlockState(q, Blocks.AIR.getDefaultState());
				if (i % 3 == 0) world.spawnParticles(SpellEffects.particle(w.formula.elementType()), q.getX() + 0.5, q.getY() + 0.5, q.getZ() + 0.5, 2, 0.2, 0.2, 0.2, 0.01);
			}
		}
	}

	public static void clearAll(MinecraftServer server) {
		for (Weave w : new ArrayList<>(WEAVES)) {
			ServerWorld world = server.getWorld(w.world);
			if (world != null) remove(world, w);
		}
		WEAVES.clear();
		pendingSustainedFeat.clear();
	}
}
