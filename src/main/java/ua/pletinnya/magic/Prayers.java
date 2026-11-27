package ua.pletinnya.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import ua.pletinnya.fx.Vfx;

/**
 * Свята магія: молитви відкривають встановлені божественні плетіння без розуміння формул.
 * Живлення — не мана, а віра. Можливості залежать від сфери божества.
 */
public final class Prayers {
	private Prayers() {}

	public static final String[] DEITIES = {"", "elias", "lumis", "dragon_god"};
	public static final int[] DEITY_COLOR = {0, 0x9A8CFF, 0x7FCB5A, 0xF2C04A};

	public record Prayer(String id, int deity, int cost) {}

	public static final Prayer[] ALL = {
		new Prayer("elias_eye", 1, 15), new Prayer("elias_flow", 1, 40), new Prayer("elias_seal", 1, 30),
		new Prayer("lumis_touch", 2, 20), new Prayer("lumis_stone", 2, 30), new Prayer("lumis_growth", 2, 15),
		new Prayer("dragon_breath", 3, 30), new Prayer("dragon_scales", 3, 20), new Prayer("dragon_roar", 3, 25),
	};

	public static Text deityName(int deity) {
		return Text.translatable("deity.pletinnya." + DEITIES[Math.max(0, Math.min(3, deity))]);
	}

	public static void pray(ServerPlayerEntity p, int index) {
		if (index < 0 || index >= ALL.length) return;
		Prayer pr = ALL[index];
		MageData d = p.getAttachedOrCreate(ua.pletinnya.registry.ModAttachments.MAGE);
		if (d.paths.deity != pr.deity) {
			p.sendMessage(Text.translatable("message.pletinnya.wrong_deity").formatted(Formatting.GRAY), true);
			return;
		}
		float cost = pr.cost * (Gear.holdingFocus(p, MagicPath.HOLY) ? 0.75f : 1f);
		if (!p.isCreative() && d.paths.faith < cost) {
			p.sendMessage(Text.translatable("message.pletinnya.no_faith", (int) cost, (int) d.paths.faith).formatted(Formatting.YELLOW), true);
			return;
		}
		if (!p.isCreative()) d.paths.faith -= cost;
		d.markDirty();
		ServerWorld w = p.getServerWorld();
		Vec3d pos = p.getPos();
		Vfx.send(w, Vfx.PRAYER, pr.deity, pos, 1.5f);
		ua.pletinnya.fx.Anim.play(p, ua.pletinnya.fx.Anim.PRAY, 1f);
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 0.8f, 1.3f);
		switch (pr.id) {
			case "elias_eye" -> {
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200, 0));
				for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(32), e -> e != p)) {
					e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 300, 0), p);
				}
				d.perception = Math.min(100, d.perception + (p.getRandom().nextFloat() < 0.3f ? 1 : 0));
			}
			case "elias_flow" -> d.mana = Math.min(d.maxMana, d.mana + d.maxMana * 0.5f);
			case "elias_seal" -> WeaveManager.dispel(new SpellContext(w, p, new SpellFormula("seal", Element.MANA.ordinal(), 1, 10, 8, 1, 1, 0), 10f), pos, 8f);
			case "lumis_touch" -> {
				for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(6), e -> !(e instanceof Monster))) {
					e.heal(8f);
					e.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 1), p);
					Vfx.send(w, Vfx.HEAL, Element.NATURE.ordinal(), e.getPos(), e.getHeight());
				}
			}
			case "lumis_stone" -> {
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 1));
				p.setAbsorptionAmount(Math.max(p.getAbsorptionAmount(), 8f));
			}
			case "lumis_growth" -> {
				SpellEffects.area(new SpellContext(w, p, new SpellFormula("growth", Element.NATURE.ordinal(), 1, 10, 8, 1, 1, 0), 10f), pos, 6f);
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 4, 0));
			}
			case "dragon_breath" -> {
				Vec3d look = p.getRotationVec(1f);
				Vfx.send(w, Vfx.DRAGON, Element.FIRE.ordinal(), p.getEyePos(), look, 8f);
				for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(9), e -> e != p)) {
					Vec3d to = e.getPos().add(0, e.getHeight() / 2, 0).subtract(p.getEyePos());
					if (to.length() < 9 && to.normalize().dotProduct(look) > 0.75) {
						e.damage(w.getDamageSources().indirectMagic(p, p), 8f);
						e.setOnFireFor(6f);
					}
				}
			}
			case "dragon_scales" -> {
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0));
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 0));
			}
			case "dragon_roar" -> {
				w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1f, 0.8f);
				for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(6), e -> e != p && !(e instanceof PlayerEntity))) {
					Vec3d push = e.getPos().subtract(pos).normalize().multiply(1.4);
					e.addVelocity(push.x, 0.5, push.z);
					e.velocityModified = true;
					e.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 1), p);
				}
			}
			default -> {
			}
		}
		p.sendMessage(Text.translatable("prayer.pletinnya." + pr.id).formatted(Formatting.GOLD), true);
	}

	/** Щоденна молитва біля вівтаря або пожертва есенції. */
	public static void altar(ServerPlayerEntity p, int deityChoice, boolean offering, float offeringValue) {
		MageData d = p.getAttachedOrCreate(ua.pletinnya.registry.ModAttachments.MAGE);
		ServerWorld w = p.getServerWorld();
		if (d.paths.deity == 0 || deityChoice > 0) {
			d.paths.deity = Math.max(1, deityChoice);
			p.sendMessage(Text.translatable("message.pletinnya.deity_chosen", deityName(d.paths.deity)).formatted(Formatting.GOLD), false);
		}
		float mul = Gear.fullSet(p, MagicPath.HOLY) ? 2f : 1f;
		if (offering) {
			d.paths.faith = Math.min(100f, d.paths.faith + offeringValue * mul);
			d.paths.devotion += 1;
			p.sendMessage(Text.translatable("message.pletinnya.offering", (int) d.paths.faith).formatted(Formatting.YELLOW), true);
		} else {
			long day = w.getTimeOfDay() / 24000L;
			if (day == d.paths.lastPrayerDay) {
				p.sendMessage(Text.translatable("message.pletinnya.prayed_today").formatted(Formatting.GRAY), true);
				return;
			}
			d.paths.lastPrayerDay = day;
			d.paths.faith = Math.min(100f, d.paths.faith + 25f * mul);
			d.paths.devotion += 3;
			p.sendMessage(Text.translatable("message.pletinnya.prayed", deityName(d.paths.deity), (int) d.paths.faith).formatted(Formatting.GOLD), false);
		}
		Vfx.send(w, Vfx.PRAYER, d.paths.deity, p.getPos(), 1.2f);
		ua.pletinnya.fx.Anim.play(p, ua.pletinnya.fx.Anim.PRAY, 1f);
		w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 0.7f, 1.4f);
		d.markDirty();
	}
}
