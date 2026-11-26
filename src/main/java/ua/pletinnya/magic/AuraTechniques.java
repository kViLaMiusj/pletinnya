package ua.pletinnya.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import ua.pletinnya.fx.Vfx;
import ua.pletinnya.item.AuraBladeItem;
import ua.pletinnya.registry.ModAttachments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Техніки аури: удари, посилені маною, і хвиля кожного четвертого удару. */
public final class AuraTechniques {
	private AuraTechniques() {}

	private static final Map<UUID, Integer> COMBO = new HashMap<>();

	public static void onHit(ServerPlayerEntity p, LivingEntity target) {
		MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
		if (!d.aura) return;
		ServerWorld w = p.getServerWorld();
		boolean blade = p.getMainHandStack().getItem() instanceof AuraBladeItem;
		float bonus = (1f + d.rank * 0.8f) * (blade ? 2f : 1f) * (Gear.fullSet(p, MagicPath.AURA) ? 1.3f : 1f);
		target.damage(w.getDamageSources().playerAttack(p), bonus);
		target.timeUntilRegen = 0;
		Vec3d hitPos = target.getPos().add(0, target.getHeight() * 0.6, 0);
		Vfx.send(w, Vfx.AURA_STRIKE, 0, hitPos, p.getRotationVec(1f), 1f);
		int combo = COMBO.merge(p.getUuid(), 1, Integer::sum);
		if (combo >= 4) {
			COMBO.put(p.getUuid(), 0);
			Vfx.send(w, Vfx.AURA_STRIKE, 1, p.getPos().add(0, 1, 0), p.getRotationVec(1f), 3.5f);
			w.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 0.6f);
			for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(3.5), e -> e != p)) {
				Vec3d push = e.getPos().subtract(p.getPos()).normalize().multiply(1.1);
				e.addVelocity(push.x, 0.35, push.z);
				e.velocityModified = true;
				e.damage(w.getDamageSources().playerAttack(p), bonus * 0.8f);
				e.timeUntilRegen = 0;
			}
		}
	}
}
