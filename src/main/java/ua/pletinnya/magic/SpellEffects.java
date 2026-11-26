package ua.pletinnya.magic;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.registry.ModBlocks;

import java.util.List;

/** Що робить кожен із дванадцяти напрямів у кожній формі. */
public final class SpellEffects {
	private SpellEffects() {}

	public static final DustParticleEffect BLOOD_DUST = new DustParticleEffect(new Vector3f(0.65f, 0.05f, 0.05f), 1.3f);
	public static final DustParticleEffect MANA_DUST = new DustParticleEffect(new Vector3f(0.42f, 0.36f, 1f), 1.1f);
	public static final DustParticleEffect DARK_DUST = new DustParticleEffect(new Vector3f(0.12f, 0.05f, 0.2f), 1.6f);

	// ======================================================================
	// Вивільнення відкладеного плетіння (руна, снаряд, поле)
	// ======================================================================

	public static void release(SpellContext ctx, Vec3d pos, @Nullable Entity anchor, Direction facing) {
		switch (ctx.formula().formType()) {
			case PROJECTILE -> impact(ctx, pos, null);
			case AREA -> area(ctx, pos, ctx.formula().radius());
			case SELF -> {
				if (anchor instanceof LivingEntity le) self(ctx, le);
			}
			case TOUCH -> {
				if (anchor instanceof LivingEntity le && le.isAlive()) entity(ctx, le, ctx.power() * 1.25f);
				else area(ctx, pos, 1.5f);
			}
			case CONSTRUCT -> construct(ctx, BlockPos.ofFloored(pos), facing);
		}
	}

	/** Влучання снаряда. */
	public static void impact(SpellContext ctx, Vec3d pos, @Nullable Entity hit) {
		float r = ctx.formula().radius();
		if (hit instanceof LivingEntity le) {
			entity(ctx, le, ctx.power());
			if (ctx.formula().size() >= 3) area(ctx.withPower(ctx.power() * 0.6f), pos, r, le);
		} else {
			area(ctx, pos, Math.max(1.5f, r));
		}
		burst(ctx.world(), ctx.element(), pos, 20 + ctx.formula().size() * 6, r * 0.4);
	}

	// ======================================================================
	// Закон опору
	// ======================================================================

	/** Наскільки плетіння істоти опирається втручанню (1 — не опирається). */
	public static float resistance(SpellContext ctx, LivingEntity target) {
		if (ctx.isCaster(target)) return 1f;
		Element el = ctx.element();
		boolean intrusive = el == Element.BLOOD || el == Element.ICE || el == Element.DARKNESS || el == Element.SOUND || el == Element.NATURE;
		float r = 1f;
		if (intrusive) r = MathHelper.clamp(1f - target.getMaxHealth() / 150f, 0.35f, 1f);
		if (el == Element.BLOOD) r *= 0.8f;
		if (target instanceof WitherEntity || target instanceof EnderDragonEntity) r *= 0.25f;
		if (target instanceof PlayerEntity pl) {
			MageData td = pl.getAttachedOrCreate(ModAttachments.MAGE);
			r *= 1f - Math.min(td.precision, 100) * 0.004f;
		}
		return r;
	}

	// ======================================================================
	// Ефект на істоту
	// ======================================================================

	public static void entity(SpellContext ctx, LivingEntity t, float basePower) {
		ServerWorld w = ctx.world();
		SpellFormula f = ctx.formula();
		float p = basePower * resistance(ctx, t);
		int dur = f.durationTicks();
		Vec3d from = ctx.caster() != null ? ctx.caster().getPos() : t.getPos().add(0, 0, 1);
		switch (ctx.element()) {
			case WATER -> {
				t.extinguish();
				push(t, from, 0.4f + p * 0.12f, 0.15f);
				t.damage(ctx.magicDamage(), 0.5f + p * 0.6f);
				w.spawnParticles(ParticleTypes.SPLASH, t.getX(), t.getBodyY(0.5), t.getZ(), 30, 0.4, 0.5, 0.4, 0.2);
			}
			case FIRE -> {
				t.setOnFireFor(2f + p * 0.6f + f.duration() * 0.3f);
				t.damage(ctx.magicDamage(), 1.5f + p * 1.2f);
				w.spawnParticles(ParticleTypes.FLAME, t.getX(), t.getBodyY(0.5), t.getZ(), 25, 0.3, 0.5, 0.3, 0.02);
			}
			case WIND -> {
				push(t, from, 0.6f + p * 0.25f, 0.25f + p * 0.07f);
				w.spawnParticles(ParticleTypes.CLOUD, t.getX(), t.getBodyY(0.3), t.getZ(), 20, 0.4, 0.3, 0.4, 0.05);
			}
			case EARTH -> {
				t.damage(ctx.magicDamage(), 2f + p * 1.3f);
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, dur / 2, 1), ctx.caster());
				w.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()),
					t.getX(), t.getY() + 0.2, t.getZ(), 40, 0.4, 0.3, 0.4, 0.1);
			}
			case LIGHTNING -> {
				if (p >= 5f && w.isSkyVisible(t.getBlockPos())) {
					bolt(ctx, t.getPos());
				} else {
					t.damage(ctx.magicDamage(), 2f + p * 1.5f);
				}
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 2), ctx.caster());
				w.spawnParticles(ParticleTypes.ELECTRIC_SPARK, t.getX(), t.getBodyY(0.5), t.getZ(), 40, 0.4, 0.6, 0.4, 0.3);
			}
			case ICE -> {
				t.setFrozenTicks(Math.min(t.getFrozenTicks() + (int) (p * 40), 300));
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (int) (dur * Math.min(1f, p / 4f)) + 20, Math.min(3, (int) (p / 3))), ctx.caster());
				t.damage(w.getDamageSources().freeze(), 1f + p * 0.8f);
				w.spawnParticles(ParticleTypes.SNOWFLAKE, t.getX(), t.getBodyY(0.5), t.getZ(), 30, 0.4, 0.5, 0.4, 0.02);
			}
			case NATURE -> {
				if (t instanceof Monster) {
					t.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, dur / 2 + 20, 3), ctx.caster());
					t.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, dur / 2, p >= 6 ? 1 : 0), ctx.caster());
				} else {
					t.heal(1f + p);
					t.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, dur / 2, p >= 7 ? 1 : 0), ctx.caster());
				}
				w.spawnParticles(ParticleTypes.HAPPY_VILLAGER, t.getX(), t.getBodyY(0.5), t.getZ(), 20, 0.4, 0.5, 0.4, 0.02);
			}
			case SOUND -> {
				t.damage(w.getDamageSources().sonicBoom(ctx.caster() != null ? ctx.caster() : t), 1f + p * 0.9f);
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, dur, 0), ctx.caster());
				push(t, from, 0.3f + p * 0.08f, 0.1f);
				w.spawnParticles(ParticleTypes.SONIC_BOOM, t.getX(), t.getBodyY(0.5), t.getZ(), 1, 0, 0, 0, 0);
			}
			case LIGHT -> {
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, dur * 2, 0), ctx.caster());
				if (p >= 3f) t.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 + (int) (p * 8), 0), ctx.caster());
				t.damage(ctx.magicDamage(), p * 0.5f);
				w.spawnParticles(ParticleTypes.END_ROD, t.getX(), t.getBodyY(0.5), t.getZ(), 25, 0.4, 0.5, 0.4, 0.05);
			}
			case DARKNESS -> {
				int d = (int) (dur * Math.min(1f, p / Math.max(1f, basePower)));
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, d, 0), ctx.caster());
				t.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, d, 0), ctx.caster());
				if (p >= 5f) t.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, d, 0), ctx.caster());
				w.spawnParticles(DARK_DUST, t.getX(), t.getBodyY(0.5), t.getZ(), 30, 0.4, 0.6, 0.4, 0.01);
			}
			case BLOOD -> {
				if (p < 1.5f) {
					if (ctx.caster() != null) ctx.caster().sendMessage(Text.translatable("message.pletinnya.resisted").formatted(Formatting.DARK_RED), true);
					w.spawnParticles(BLOOD_DUST, t.getX(), t.getBodyY(0.5), t.getZ(), 6, 0.3, 0.3, 0.3, 0);
					return;
				}
				float dmg = 1f + p * 1.3f;
				t.damage(ctx.magicDamage(), dmg);
				if (ctx.caster() != null) ctx.caster().heal(dmg * 0.5f);
				w.spawnParticles(BLOOD_DUST, t.getX(), t.getBodyY(0.5), t.getZ(), 30, 0.3, 0.5, 0.3, 0);
			}
			case MANA -> {
				t.damage(ctx.magicDamage(), 2f + p * 1.1f);
				if (t instanceof PlayerEntity pl) {
					MageData td = pl.getAttachedOrCreate(ModAttachments.MAGE);
					if (td.aura && p * 5 > td.precision) {
						td.aura = false;
						td.markDirty();
						pl.sendMessage(Text.translatable("message.pletinnya.aura_broken").formatted(Formatting.RED), true);
					}
				}
				WeaveManager.dispelAnchored(ctx, t);
				w.spawnParticles(MANA_DUST, t.getX(), t.getBodyY(0.5), t.getZ(), 30, 0.4, 0.5, 0.4, 0);
			}
		}
		sound(w, t.getPos(), ctx.element());
	}

	// ======================================================================
	// Ефект на себе
	// ======================================================================

	public static void self(SpellContext ctx, LivingEntity me) {
		ServerWorld w = ctx.world();
		float p = ctx.power();
		int dur = ctx.formula().durationTicks();
		int amp = Math.min(2, (int) (p / 4f));
		switch (ctx.element()) {
			case WATER -> {
				me.extinguish();
				eff(me, StatusEffects.WATER_BREATHING, dur * 3, 0);
				eff(me, StatusEffects.DOLPHINS_GRACE, dur, amp);
			}
			case FIRE -> {
				me.setFrozenTicks(0);
				eff(me, StatusEffects.FIRE_RESISTANCE, dur * 2, 0);
			}
			case WIND -> {
				Vec3d look = me.getRotationVec(1f);
				double s = 0.8 + p * 0.2;
				me.addVelocity(look.x * s, Math.max(0.3, look.y * s + 0.2), look.z * s);
				me.velocityModified = true;
				me.fallDistance = 0;
				eff(me, StatusEffects.SLOW_FALLING, dur / 2, 0);
				eff(me, StatusEffects.SPEED, dur, amp);
			}
			case EARTH -> {
				eff(me, StatusEffects.RESISTANCE, dur, amp);
				eff(me, StatusEffects.SLOWNESS, dur, 0);
			}
			case LIGHTNING -> {
				eff(me, StatusEffects.SPEED, dur, amp + 1);
				eff(me, StatusEffects.HASTE, dur, amp);
			}
			case ICE -> {
				eff(me, StatusEffects.RESISTANCE, dur, Math.max(0, amp - 1));
				me.extinguish();
				freezeAround(ctx, me.getBlockPos(), 2.5f + ctx.formula().size());
			}
			case NATURE -> {
				me.heal(1f + p * 0.6f);
				eff(me, StatusEffects.REGENERATION, dur, amp);
				eff(me, StatusEffects.SATURATION, 1 + (int) (p / 4), 0);
			}
			case SOUND -> {
				double r = 16 + p * 3;
				for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, me.getBoundingBox().expand(r), e -> e != me)) {
					e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, dur, 0), me);
				}
			}
			case LIGHT -> eff(me, StatusEffects.NIGHT_VISION, dur * 4, 0);
			case DARKNESS -> eff(me, StatusEffects.INVISIBILITY, dur, 0);
			case BLOOD -> {
				me.damage(w.getDamageSources().magic(), 1f + p * 0.5f);
				eff(me, StatusEffects.STRENGTH, dur, amp);
			}
			case MANA -> {
				me.setAbsorptionAmount(Math.max(me.getAbsorptionAmount(), 2f + p * 1.6f));
				WeaveManager.dispelAnchored(ctx, me);
			}
		}
		burst(w, ctx.element(), me.getPos().add(0, 1, 0), 25, 0.6);
		sound(w, me.getPos(), ctx.element());
	}

	private static void eff(LivingEntity e, net.minecraft.registry.entry.RegistryEntry<net.minecraft.entity.effect.StatusEffect> type, int dur, int amp) {
		e.addStatusEffect(new StatusEffectInstance(type, dur, amp));
	}

	// ======================================================================
	// Область
	// ======================================================================

	public static void area(SpellContext ctx, Vec3d c, float r) {
		area(ctx, c, r, null);
	}

	public static void area(SpellContext ctx, Vec3d c, float r, @Nullable Entity skip) {
		ServerWorld w = ctx.world();
		float p = ctx.power();
		Element el = ctx.element();
		BlockPos center = BlockPos.ofFloored(c);
		int ri = MathHelper.ceil(r);

		// --- дія на блоки ---
		switch (el) {
			case WATER -> forBlocks(center, ri, r, q -> {
				BlockState s = w.getBlockState(q);
				if (s.isIn(BlockTags.FIRE)) w.removeBlock(q, false);
			});
			case FIRE -> {
				int budget = 4 + (int) (r * r * 0.6f);
				for (BlockPos q : BlockPos.iterateRandomly(w.getRandom(), budget * 3, center, ri)) {
					if (budget <= 0) break;
					if (w.getBlockState(q).isAir() && w.getBlockState(q.down()).isSideSolidFullSquare(w, q.down(), Direction.UP)) {
						w.setBlockState(q, AbstractFireBlock.getState(w, q));
						budget--;
					}
				}
			}
			case ICE -> freezeAround(ctx, center, r);
			case NATURE -> {
				int budget = 6 + (int) (p * 3);
				ItemStack meal = new ItemStack(Items.BONE_MEAL, 64);
				for (BlockPos q : BlockPos.iterateOutwards(center, ri, 2, ri)) {
					if (budget <= 0) break;
					BlockState s = w.getBlockState(q);
					if (s.getBlock() instanceof Fertilizable && !s.isOf(Blocks.GRASS_BLOCK)) {
						if (BoneMealItem.useOnFertilizable(meal, w, q)) {
							w.spawnParticles(ParticleTypes.HAPPY_VILLAGER, q.getX() + 0.5, q.getY() + 0.5, q.getZ() + 0.5, 5, 0.3, 0.3, 0.3, 0);
							budget--;
						}
					}
				}
			}
			case SOUND -> {
				if (p >= 6f) forBlocks(center, ri, r, q -> {
					if (w.getBlockState(q).isIn(BlockTags.IMPERMEABLE)) w.breakBlock(q, true);
				});
			}
			case LIGHT -> {
				WeaveManager.Weave lights = WeaveManager.temporary(ctx, ctx.formula().durationTicks() * 3, false);
				BlockState light = Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, 15);
				int placed = 0;
				for (BlockPos q : BlockPos.iterateRandomly(w.getRandom(), 40, center, ri)) {
					if (placed >= 3 + (int) (r / 2)) break;
					if (WeaveManager.placeTemp(w, q, light, lights)) placed++;
				}
				WeaveManager.commit(lights);
			}
			case LIGHTNING -> {
				if (p >= 5f) {
					int n = 1 + (int) (p / 4);
					for (int i = 0; i < n; i++) {
						double dx = (w.getRandom().nextDouble() * 2 - 1) * r;
						double dz = (w.getRandom().nextDouble() * 2 - 1) * r;
						BlockPos top = w.getTopPosition(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING, BlockPos.ofFloored(c.x + dx, c.y, c.z + dz));
						bolt(ctx, Vec3d.ofBottomCenter(top));
					}
				}
			}
			case MANA -> WeaveManager.dispel(ctx, c, r);
			default -> {
			}
		}

		// --- дія на істот ---
		boolean includeCaster = el == Element.NATURE || el == Element.LIGHT;
		List<LivingEntity> targets = w.getEntitiesByClass(LivingEntity.class, new Box(c, c).expand(r),
			e -> e.isAlive() && !e.isSpectator() && e != skip && e.squaredDistanceTo(c) <= r * r && (includeCaster || !ctx.isCaster(e)));
		float areaPower = p * 0.75f;
		for (LivingEntity e : targets) {
			switch (el) {
				case WIND, WATER -> {
					push(e, c, 0.5f + p * 0.2f, 0.2f + p * 0.05f);
					if (el == Element.WATER) e.extinguish();
				}
				case LIGHTNING -> {
					if (p < 5f) entity(ctx, e, areaPower);
				}
				case MANA -> e.damage(ctx.magicDamage(), 1f + p * 0.5f);
				case LIGHT -> {
					if (!ctx.isCaster(e)) entity(ctx, e, areaPower);
				}
				default -> entity(ctx, e, areaPower);
			}
		}

		burst(w, el, c, (int) (r * r * 8), r * 0.6);
		sound(w, c, el);
	}

	private static void forBlocks(BlockPos center, int ri, float r, java.util.function.Consumer<BlockPos> action) {
		for (BlockPos q : BlockPos.iterate(center.add(-ri, -ri, -ri), center.add(ri, ri, ri))) {
			if (q.getSquaredDistance(center) <= r * r) action.accept(q);
		}
	}

	/** Відведення тепла: вода → крихкий лід, лава → обсидіан (на високій силі), сніг на землі. */
	private static void freezeAround(SpellContext ctx, BlockPos center, float r) {
		ServerWorld w = ctx.world();
		int ri = MathHelper.ceil(r);
		forBlocks(center, ri, r, q -> {
			BlockState s = w.getBlockState(q);
			if (s.isOf(Blocks.WATER) && s.getFluidState().isStill() && w.getBlockState(q.up()).isAir()) {
				w.setBlockState(q, Blocks.FROSTED_ICE.getDefaultState());
				w.scheduleBlockTick(q, Blocks.FROSTED_ICE, MathHelper.nextInt(w.getRandom(), 60, 120));
			} else if (s.isOf(Blocks.LAVA) && s.getFluidState().isStill() && ctx.power() >= 7f) {
				w.setBlockState(q, Blocks.OBSIDIAN.getDefaultState());
			} else if (s.isIn(BlockTags.FIRE)) {
				w.removeBlock(q, false);
			} else if (s.isAir() && w.getBlockState(q.down()).isSideSolidFullSquare(w, q.down(), Direction.UP)
				&& w.getRandom().nextFloat() < 0.5f) {
				w.setBlockState(q, Blocks.SNOW.getDefaultState());
			}
		});
	}

	// ======================================================================
	// Конструкт: стіна або поле
	// ======================================================================

	public static void construct(SpellContext ctx, BlockPos base, Direction facing) {
		ServerWorld w = ctx.world();
		SpellFormula f = ctx.formula();
		Element el = ctx.element();
		int life = f.durationTicks() * 3;

		BlockState wall = switch (el) {
			case EARTH -> ModBlocks.WOVEN_STONE.getDefaultState();
			case ICE -> Blocks.PACKED_ICE.getDefaultState();
			case MANA -> ModBlocks.MANA_BARRIER.getDefaultState();
			case WATER -> Blocks.WATER.getDefaultState();
			case FIRE -> Blocks.FIRE.getDefaultState();
			case NATURE -> Blocks.OAK_LEAVES.getDefaultState().with(net.minecraft.block.LeavesBlock.PERSISTENT, true);
			default -> null;
		};

		if (wall == null) {
			// Поле: періодично діє на область, поки триває плетіння.
			WeaveManager.field(ctx, Vec3d.ofBottomCenter(base), life);
			burst(w, el, Vec3d.ofCenter(base), 40, f.radius() * 0.5);
			sound(w, Vec3d.ofCenter(base), el);
			return;
		}

		Direction side = facing.rotateYClockwise();
		int half = (f.size() + 1) / 3;
		int height = el == Element.FIRE ? 1 : 1 + (f.power() + 2) / 3;
		WeaveManager.Weave weave = WeaveManager.temporary(ctx, life, true);
		for (int i = -half; i <= half; i++) {
			for (int h = 0; h < height; h++) {
				BlockPos q = base.offset(side, i).up(h);
				BlockState place = el == Element.FIRE ? AbstractFireBlock.getState(w, q) : wall;
				if (el == Element.FIRE && !w.getBlockState(q.down()).isSideSolidFullSquare(w, q.down(), Direction.UP)) continue;
				if (WeaveManager.placeTemp(w, q, place, weave)) {
					w.spawnParticles(particle(el), q.getX() + 0.5, q.getY() + 0.5, q.getZ() + 0.5, 4, 0.3, 0.3, 0.3, 0.01);
				}
			}
		}
		WeaveManager.commit(weave);
		sound(w, Vec3d.ofCenter(base), el);
	}

	// ======================================================================
	// Допоміжне
	// ======================================================================

	private static void push(LivingEntity t, Vec3d from, float strength, float up) {
		Vec3d d = t.getPos().subtract(from);
		d = new Vec3d(d.x, 0, d.z);
		if (d.lengthSquared() < 1.0E-4) d = new Vec3d(0, 0, 1);
		d = d.normalize().multiply(strength);
		t.addVelocity(d.x, up, d.z);
		t.velocityModified = true;
	}

	private static void bolt(SpellContext ctx, Vec3d pos) {
		LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(ctx.world());
		if (bolt == null) return;
		bolt.refreshPositionAfterTeleport(pos);
		if (ctx.caster() != null) bolt.setChanneler(ctx.caster());
		ctx.world().spawnEntity(bolt);
	}

	public static ParticleEffect particle(Element el) {
		return switch (el) {
			case WATER -> ParticleTypes.SPLASH;
			case FIRE -> ParticleTypes.FLAME;
			case WIND -> ParticleTypes.CLOUD;
			case EARTH -> new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState());
			case LIGHTNING -> ParticleTypes.ELECTRIC_SPARK;
			case ICE -> ParticleTypes.SNOWFLAKE;
			case NATURE -> ParticleTypes.HAPPY_VILLAGER;
			case SOUND -> ParticleTypes.NOTE;
			case LIGHT -> ParticleTypes.END_ROD;
			case DARKNESS -> DARK_DUST;
			case BLOOD -> BLOOD_DUST;
			case MANA -> MANA_DUST;
		};
	}

	public static void burst(ServerWorld w, Element el, Vec3d pos, int count, double spread) {
		w.spawnParticles(particle(el), pos.x, pos.y, pos.z, Math.min(count, 300), spread, spread * 0.6, spread, 0.05);
	}

	public static SoundEvent soundOf(Element el) {
		return switch (el) {
			case WATER -> SoundEvents.ENTITY_GENERIC_SPLASH;
			case FIRE -> SoundEvents.ENTITY_BLAZE_SHOOT;
			case WIND -> SoundEvents.ENTITY_ENDER_DRAGON_FLAP;
			case EARTH -> SoundEvents.BLOCK_STONE_BREAK;
			case LIGHTNING -> SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT;
			case ICE -> SoundEvents.BLOCK_GLASS_BREAK;
			case NATURE -> SoundEvents.BLOCK_AZALEA_LEAVES_PLACE;
			case SOUND -> SoundEvents.BLOCK_BELL_RESONATE;
			case LIGHT -> SoundEvents.BLOCK_BEACON_ACTIVATE;
			case DARKNESS -> SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK;
			case BLOOD -> SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH;
			case MANA -> SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE;
		};
	}

	public static void sound(ServerWorld w, Vec3d pos, Element el) {
		w.playSound(null, pos.x, pos.y, pos.z, soundOf(el), SoundCategory.PLAYERS, 0.9f, 0.9f + w.getRandom().nextFloat() * 0.3f);
	}
}
