package ua.pletinnya.fx;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import ua.pletinnya.magic.Element;

/** Шлейф снаряда — у кожного напряму свій характер руху частинок. */
public final class ProjectileTrails {
	private ProjectileTrails() {}

	public static void trail(World w, Vec3d pos, Vec3d vel, Element e, int age) {
		Random r = w.getRandom();
		var fx = ModParticles.of(e);
		double x = pos.x, y = pos.y + 0.15, z = pos.z;
		Vec3d back = vel.multiply(-0.05);
		switch (e) {
			case FIRE -> {
				for (int i = 0; i < 3; i++) w.addParticle(fx, x + r.nextGaussian() * 0.08, y + r.nextGaussian() * 0.08, z + r.nextGaussian() * 0.08, back.x, back.y + 0.02, back.z);
				w.addParticle(new FxEffects.Mote(0xFFB040, 0.12f, 18), x, y, z, r.nextGaussian() * 0.02, 0.04, r.nextGaussian() * 0.02);
				if (age % 2 == 0) w.addParticle(new FxEffects.Mote(0x3A2A2A, 0.25f, 26), x, y, z, 0, 0.03, 0);
			}
			case ICE -> {
				w.addParticle(fx, x + r.nextGaussian() * 0.1, y + r.nextGaussian() * 0.1, z + r.nextGaussian() * 0.1, 0, -0.02, 0);
				w.addParticle(new FxEffects.Mote(0xDFF8FF, 0.08f, 20), x, y, z, r.nextGaussian() * 0.03, r.nextGaussian() * 0.03, r.nextGaussian() * 0.03);
			}
			case LIGHTNING -> {
				for (int i = 0; i < 2; i++) {
					double ox = r.nextGaussian() * 0.25, oy = r.nextGaussian() * 0.25, oz = r.nextGaussian() * 0.25;
					w.addParticle(fx, x + ox, y + oy, z + oz, 0, 0, 0);
					w.addParticle(fx, x + ox * 0.5, y + oy * 0.5, z + oz * 0.5, 0, 0, 0);
				}
			}
			case WATER -> {
				w.addParticle(fx, x, y, z, r.nextGaussian() * 0.02, -0.05, r.nextGaussian() * 0.02);
				if (age % 3 == 0) w.addParticle(new FxEffects.Ring(0x6AB0FF, 0.35f), x, y, z, 0, 0, 0);
			}
			case WIND -> {
				double a = age * 0.9;
				Vec3d side = vel.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(0.3);
				for (int k = 0; k < 2; k++) {
					double ang = a + k * Math.PI;
					w.addParticle(fx, x + side.x * Math.cos(ang), y + 0.3 * Math.sin(ang), z + side.z * Math.cos(ang), back.x, back.y, back.z);
				}
			}
			case EARTH -> {
				if (age % 2 == 0) w.addParticle(fx, x, y, z, r.nextGaussian() * 0.03, 0.05, r.nextGaussian() * 0.03);
				w.addParticle(new FxEffects.Mote(0x8B6B3E, 0.1f, 14), x, y, z, 0, -0.04, 0);
			}
			case NATURE -> {
				double a = age * 0.6;
				w.addParticle(fx, x + Math.cos(a) * 0.35, y + Math.sin(a * 1.3) * 0.2, z + Math.sin(a) * 0.35, 0, -0.01, 0);
				if (age % 3 == 0) w.addParticle(new FxEffects.Mote(0xFF9ACD, 0.1f, 24), x, y, z, 0, 0.01, 0);
			}
			case SOUND -> {
				if (age % 2 == 0) w.addParticle(new FxEffects.Ring(0xC9B6F0, 0.5f), x, y, z, 0, 0, 0);
				if (age % 4 == 0) w.addParticle(fx, x, y + 0.2, z, 0, 0.04, 0);
			}
			case LIGHT -> {
				w.addParticle(fx, x, y, z, 0, 0, 0);
				w.addParticle(new FxEffects.Mote(0xFFF6D0, 0.35f, 10), x, y, z, 0, 0, 0);
			}
			case DARKNESS -> {
				for (int i = 0; i < 2; i++) w.addParticle(fx, x + r.nextGaussian() * 0.15, y + r.nextGaussian() * 0.15, z + r.nextGaussian() * 0.15, back.x * 2, back.y * 2, back.z * 2);
			}
			case BLOOD -> {
				w.addParticle(fx, x + r.nextGaussian() * 0.1, y, z + r.nextGaussian() * 0.1, 0, -0.08, 0);
				if (age % 2 == 0) w.addParticle(new FxEffects.Mote(0x8A0A10, 0.14f, 16), x, y, z, 0, 0, 0);
			}
			case MANA -> {
				if (age % 3 == 0) w.addParticle(new FxEffects.Glyph(11, 0x9A8CFF, 0.25f), x, y, z, 0, 0, 0);
				w.addParticle(fx, x + r.nextGaussian() * 0.1, y + r.nextGaussian() * 0.1, z + r.nextGaussian() * 0.1, back.x, back.y, back.z);
			}
		}
	}
}
