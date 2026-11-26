package ua.pletinnya.magic;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import ua.pletinnya.network.ModNetworking;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.registry.ModBlocks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Відновлення мани, медитація (мана довкілля), загоєння каналів, аура та синхронізація з клієнтом. */
public final class ManaTicker {
	private ManaTicker() {}

	private static final Map<UUID, Integer> MEDITATION = new HashMap<>();

	public static void tick(MinecraftServer server) {
		for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
			MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
			if (p.age % 20 == 0) second(p, d);
			if (d.consumeDirty()) ModNetworking.sync(p, d);
		}
	}

	public static void forget(UUID id) {
		MEDITATION.remove(id);
	}

	private static void second(ServerPlayerEntity p, MageData d) {
		ServerWorld w = p.getServerWorld();
		float regen = 0.5f + d.maxMana * 0.01f;

		// Медитація: присісти й не рухатися. Збирання мани довкілля потребує часу й залежить від місцевості.
		boolean still = p.isSneaking() && p.isOnGround() && p.getVelocity().horizontalLengthSquared() < 0.001;
		if (still) {
			int med = MEDITATION.merge(p.getUuid(), 20, Integer::sum);
			if (med >= 60) {
				float ambient = ambientFactor(w, p.getBlockPos());
				regen += d.maxMana * 0.03f * ambient;
				w.spawnParticles(ParticleTypes.ENCHANT, p.getX(), p.getY() + 1.8, p.getZ(), (int) (6 * ambient), 0.8, 0.4, 0.8, 0.6);
				if (med == 60) p.sendMessage(Text.translatable("message.pletinnya.meditating", String.format("%.1f", ambient)).formatted(Formatting.DARK_AQUA), true);
				if (p.getRandom().nextFloat() < 0.02f && d.perception < 100) {
					d.perception++;
					d.markDirty();
				}
			}
		} else {
			MEDITATION.remove(p.getUuid());
		}

		if (d.mana < d.maxMana) {
			d.mana = Math.min(d.maxMana, d.mana + regen);
			d.markDirty();
		} else if (d.mana > d.maxMana) {
			d.mana = d.maxMana;
			d.markDirty();
		}

		if (d.channelDamage > 0) {
			d.channelDamage = Math.max(0, d.channelDamage - 20);
			if (d.channelDamage == 0) p.sendMessage(Text.translatable("message.pletinnya.channels_recovered").formatted(Formatting.GREEN), true);
			d.markDirty();
		}

		aura(p, d);
	}

	/** Аура — внутрішнє Плетіння: мана йде крізь тіло й зброю. Швидка й стійка, але виснажує. */
	private static void aura(ServerPlayerEntity p, MageData d) {
		if (!d.aura) {
			if (d.auraTicks > 0) {
				d.auraTicks = Math.max(0, d.auraTicks - 40);
			}
			return;
		}
		float cost = 1f + d.rank * 0.5f;
		if (d.mana < cost) {
			d.aura = false;
			d.auraTicks = 0;
			p.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 0));
			p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 0));
			p.sendMessage(Text.translatable("message.pletinnya.aura_exhausted").formatted(Formatting.RED), true);
			d.markDirty();
			return;
		}
		d.mana -= cost;
		d.auraTicks += 20;
		p.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, d.rank >= 4 ? 1 : 0, true, false));
		p.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 40, 0, true, false));
		if (d.rank >= 5) p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 0, true, false));
		p.addExhaustion(0.4f);
		if (d.auraTicks > 20 * 90 && d.auraTicks % 100 == 0) {
			p.damage(p.getServerWorld().getDamageSources().generic(), 1f);
			p.sendMessage(Text.translatable("message.pletinnya.aura_strain").formatted(Formatting.DARK_RED), true);
		}
		p.getServerWorld().spawnParticles(ParticleTypes.CRIT, p.getX(), p.getBodyY(0.5), p.getZ(), 6, 0.3, 0.5, 0.3, 0.1);
		d.markDirty();
	}

	/** Наскільки щедре довкілля на ману. */
	public static float ambientFactor(World w, BlockPos pos) {
		float f = 1f;
		if (w.getRegistryKey() == World.NETHER) f = 1.2f;
		else if (w.getRegistryKey() == World.END) f = 2f;
		else {
			RegistryEntry<Biome> b = w.getBiome(pos);
			if (b.matchesKey(BiomeKeys.DEEP_DARK)) f = 2f;
			else if (b.isIn(BiomeTags.IS_FOREST) || b.isIn(BiomeTags.IS_JUNGLE)) f = 1.5f;
			else if (b.isIn(BiomeTags.IS_MOUNTAIN)) f = 1.3f;
			else if (b.isIn(BiomeTags.IS_BADLANDS) || b.matchesKey(BiomeKeys.DESERT)) f = 0.7f;
		}
		for (BlockPos q : BlockPos.iterate(pos.add(-8, -4, -8), pos.add(8, 4, 8))) {
			if (w.getBlockState(q).isOf(ModBlocks.MANA_NODE)) {
				f *= 4f;
				break;
			}
		}
		return f;
	}
}
