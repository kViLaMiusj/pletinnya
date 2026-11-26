package ua.pletinnya.client.fx;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import ua.pletinnya.fx.FxEffects;
import ua.pletinnya.fx.ModParticles;
import ua.pletinnya.fx.Vfx;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.Prayers;

/** Візерунки заклять: магічні кола, вибухи, вихори, стовпи світла — у кожного напряму свій. */
public final class ClientVfx {
	private ClientVfx() {}

	public static void play(ClientWorld w, Vfx.Payload p) {
		Vec3d c = new Vec3d(p.x(), p.y(), p.z());
		Vec3d dir = new Vec3d(p.dx(), p.dy(), p.dz());
		float s = p.size();
		switch (p.kind()) {
			case Vfx.CAST -> castCircle(w, c, Element.byIndex(p.element()), s);
			case Vfx.IMPACT -> impact(w, c, Element.byIndex(p.element()), s);
			case Vfx.SELF -> self(w, c, Element.byIndex(p.element()), s);
			case Vfx.WALL -> wall(w, c, dir, Element.byIndex(p.element()), s);
			case Vfx.RUNE -> rune(w, c, Element.byIndex(p.element()), s);
			case Vfx.RUNE_FIRE -> { rune(w, c, Element.byIndex(p.element()), s * 0.6f); impact(w, c, Element.byIndex(p.element()), Math.max(1f, s * 0.6f)); }
			case Vfx.DISPEL -> dispel(w, c, s);
			case Vfx.MISFIRE -> misfire(w, c);
			case Vfx.OVERLOAD -> overload(w, c);
			case Vfx.PRAYER -> prayer(w, c, p.element());
			case Vfx.DRAGON -> dragon(w, c, dir, Element.byIndex(p.element()), s);
			case Vfx.SPIRIT, Vfx.PACT -> spirit(w, c, Element.byIndex(p.element()), s);
			case Vfx.AURA_STRIKE -> aura(w, c, dir, p.element() == 1, s);
			case Vfx.RANK_UP -> rankUp(w, c, p.element());
			case Vfx.HEAL -> heal(w, c, s);
			default -> {
			}
		}
	}

	private static void add(ClientWorld w, ParticleEffect e, double x, double y, double z, double vx, double vy, double vz) {
		w.addParticle(e, true, x, y, z, vx, vy, vz);
	}

	private static FxEffects.Mote mote(int color, float size, int life) { return new FxEffects.Mote(color, size, life); }

	// ---------------------------------------------------------------- коло плетіння
	static void castCircle(ClientWorld w, Vec3d c, Element e, float r) {
		double y = c.y + 0.06;
		add(w, new FxEffects.Circle(e.color, r * 2.8f), c.x, y, c.z, 0, 0, 0);
		add(w, new FxEffects.FlatGlyph(e.ordinal(), e.color, r * 0.9f), c.x, y + 0.02, c.z, 0, 0, 0);
		for (int i = 0; i < 6; i++) {
			double a = i * Math.PI * 2 / 6;
			add(w, new FxEffects.Glyph(i % 2 == 0 ? e.ordinal() : 11, i % 2 == 0 ? e.color : 0xB8AEFF, 0.22f),
				c.x + Math.cos(a) * r * 1.2, y + 0.2, c.z + Math.sin(a) * r * 1.2, -Math.sin(a) * 0.02, 0.045, Math.cos(a) * 0.02);
		}
		for (int i = 0; i < 32; i++) {
			double a = i * Math.PI * 2 / 32;
			add(w, mote(e.color, 0.1f, 22), c.x + Math.cos(a) * r * 1.35, y, c.z + Math.sin(a) * r * 1.35, 0, 0.03 + (i % 3) * 0.01, 0);
		}
	}

	// ---------------------------------------------------------------- влучання й області
	static void impact(ClientWorld w, Vec3d c, Element e, float r) {
		Random rnd = w.getRandom();
		var fx = ModParticles.of(e);
		int n = (int) Math.min(90, 14 + r * r * 6);
		switch (e) {
			case FIRE -> {
				for (int i = 0; i < n; i++) {
					Vec3d v = sphere(rnd).multiply(0.12 + r * 0.06);
					add(w, fx, c.x, c.y, c.z, v.x, v.y, v.z);
				}
				for (int i = 0; i < n / 2; i++) add(w, mote(0xFFB040, 0.1f, 30), c.x + rnd.nextGaussian() * r * 0.4, c.y, c.z + rnd.nextGaussian() * r * 0.4, 0, 0.06 + rnd.nextDouble() * 0.05, 0);
				for (int i = 0; i < n / 3; i++) add(w, mote(0x3A2A2A, 0.35f, 40), c.x + rnd.nextGaussian() * r * 0.3, c.y + 0.3, c.z + rnd.nextGaussian() * r * 0.3, 0, 0.03, 0);
				add(w, new FxEffects.Ring(0xFF8A2A, r * 2.6f), c.x, c.y + 0.1, c.z, 0, 0, 0);
				add(w, ParticleTypes.FLASH, c.x, c.y, c.z, 0, 0, 0);
			}
			case WATER -> {
				for (int i = 0; i < n; i++) {
					double a = rnd.nextDouble() * Math.PI * 2, sp = 0.08 + rnd.nextDouble() * 0.12 * r;
					add(w, fx, c.x + Math.cos(a) * r * 0.3, c.y, c.z + Math.sin(a) * r * 0.3, Math.cos(a) * sp * 0.6, 0.25 + rnd.nextDouble() * 0.2, Math.sin(a) * sp * 0.6);
				}
				for (int k = 0; k < 3; k++) add(w, new FxEffects.Ring(0x6AB0FF, r * (1.2f + k * 0.8f)), c.x, c.y + 0.05 * k, c.z, 0, 0, 0);
				add(w, ParticleTypes.SPLASH, c.x, c.y + 0.2, c.z, 0, 0, 0);
			}
			case WIND -> {
				for (int i = 0; i < n; i++) {
					double t = i / (double) n, a = t * Math.PI * 8, rad = r * (0.3 + t * 0.8);
					add(w, fx, c.x + Math.cos(a) * rad, c.y + t * r * 1.5, c.z + Math.sin(a) * rad, -Math.sin(a) * 0.15, 0.04, Math.cos(a) * 0.15);
				}
				add(w, new FxEffects.Ring(0xDFF6EE, r * 2.2f), c.x, c.y + 0.1, c.z, 0, 0, 0);
			}
			case EARTH -> {
				for (int i = 0; i < n; i++) {
					double a = rnd.nextDouble() * Math.PI * 2, sp = 0.1 + rnd.nextDouble() * 0.15;
					add(w, fx, c.x + Math.cos(a) * r * 0.4, c.y + 0.1, c.z + Math.sin(a) * r * 0.4, Math.cos(a) * sp, 0.3 + rnd.nextDouble() * 0.25, Math.sin(a) * sp);
				}
				for (int i = 0; i < 24; i++) {
					double a = i * Math.PI / 12;
					add(w, mote(0x8B6B3E, 0.3f, 22), c.x + Math.cos(a) * r, c.y + 0.1, c.z + Math.sin(a) * r, 0, 0.02, 0);
				}
				add(w, new FxEffects.Ring(0x9A7A4A, r * 2.4f), c.x, c.y + 0.1, c.z, 0, 0, 0);
			}
			case LIGHTNING -> {
				double x = c.x, y = c.y + 8, z = c.z;
				while (y > c.y) {
					double nx = x + rnd.nextGaussian() * 0.35, ny = y - 0.5 - rnd.nextDouble() * 0.5, nz = z + rnd.nextGaussian() * 0.35;
					for (int k = 0; k < 4; k++) {
						double t = k / 4.0;
						add(w, fx, x + (nx - x) * t, y + (ny - y) * t, z + (nz - z) * t, 0, 0, 0);
					}
					if (rnd.nextFloat() < 0.25f) {
						double bx = nx, by = ny, bz = nz;
						for (int b = 0; b < 4; b++) { bx += rnd.nextGaussian() * 0.4; by -= 0.3; bz += rnd.nextGaussian() * 0.4; add(w, fx, bx, by, bz, 0, 0, 0); }
					}
					x = nx; y = ny; z = nz;
				}
				for (int i = 0; i < n; i++) { Vec3d v = sphere(rnd).multiply(0.3); add(w, fx, c.x, c.y + 0.2, c.z, v.x, Math.abs(v.y), v.z); }
				add(w, ParticleTypes.FLASH, c.x, c.y + 0.5, c.z, 0, 0, 0);
				add(w, new FxEffects.Ring(0xFFF27A, r * 2.5f), c.x, c.y + 0.1, c.z, 0, 0, 0);
			}
			case ICE -> {
				for (int i = 0; i < n; i++) { Vec3d v = sphere(rnd).multiply(0.15 + r * 0.04); add(w, fx, c.x, c.y + 0.3, c.z, v.x, v.y * 0.6 + 0.05, v.z); }
				for (int i = 0; i < 36; i++) {
					double a = i * Math.PI / 18;
					add(w, mote(0xDFF8FF, 0.16f, 30), c.x + Math.cos(a) * r, c.y + 0.05, c.z + Math.sin(a) * r, 0, 0.005, 0);
				}
				add(w, new FxEffects.Glyph(Element.ICE.ordinal(), 0xBFEFFF, r * 0.6f), c.x, c.y + 0.1, c.z, 0, 0, 0);
				add(w, new FxEffects.Ring(0xBFEFFF, r * 2.3f), c.x, c.y + 0.1, c.z, 0, 0, 0);
			}
			case NATURE -> {
				for (int i = 0; i < n; i++) {
					double t = i / (double) n, a = t * Math.PI * 6;
					add(w, fx, c.x + Math.cos(a) * r * 0.7, c.y + 0.2 + t * 1.6, c.z + Math.sin(a) * r * 0.7, -Math.sin(a) * 0.05, 0.03, Math.cos(a) * 0.05);
				}
				for (int i = 0; i < n / 2; i++) add(w, mote(rnd.nextBoolean() ? 0xFF9ACD : 0xB8F07A, 0.12f, 40), c.x + rnd.nextGaussian() * r * 0.5, c.y + 0.2, c.z + rnd.nextGaussian() * r * 0.5, 0, 0.02, 0);
				add(w, new FxEffects.Ring(0x7FCB5A, r * 2.2f), c.x, c.y + 0.1, c.z, 0, 0, 0);
			}
			case SOUND -> {
				for (int k = 0; k < 4; k++) add(w, new FxEffects.Ring(0xC9B6F0, r * (0.8f + k * 0.9f)), c.x, c.y + 0.5 + k * 0.15, c.z, 0, 0, 0);
				for (int i = 0; i < n / 2; i++) { Vec3d v = sphere(rnd).multiply(0.12); add(w, fx, c.x, c.y + 0.6, c.z, v.x, Math.abs(v.y) * 0.5, v.z); }
				add(w, ParticleTypes.SONIC_BOOM, c.x, c.y + 0.6, c.z, 0, 0, 0);
			}
			case LIGHT -> {
				for (int ray = 0; ray < 12; ray++) {
					double a = ray * Math.PI / 6;
					for (int k = 0; k < 6; k++) add(w, mote(0xFFF1B0, 0.2f - k * 0.02f, 12 + k), c.x, c.y + 0.4, c.z, Math.cos(a) * (0.08 + k * 0.05) * r * 0.5, 0.02, Math.sin(a) * (0.08 + k * 0.05) * r * 0.5);
				}
				for (int i = 0; i < n / 2; i++) { Vec3d v = sphere(rnd).multiply(0.1); add(w, fx, c.x, c.y + 0.4, c.z, v.x, v.y, v.z); }
				add(w, ParticleTypes.FLASH, c.x, c.y + 0.4, c.z, 0, 0, 0);
				add(w, new FxEffects.Glyph(Element.LIGHT.ordinal(), 0xFFF1B0, r * 0.7f), c.x, c.y + 0.5, c.z, 0, 0.01, 0);
			}
			case DARKNESS -> {
				for (int i = 0; i < n; i++) {
					Vec3d off = sphere(rnd).multiply(r * 1.1);
					add(w, fx, c.x + off.x, c.y + 0.5 + Math.abs(off.y) * 0.6, c.z + off.z, -off.x * 0.06, -off.y * 0.03, -off.z * 0.06);
				}
				add(w, new FxEffects.Glyph(Element.DARKNESS.ordinal(), 0x9A5AE0, r * 0.6f), c.x, c.y + 0.6, c.z, 0, 0, 0);
				add(w, mote(0x14081E, r * 1.2f, 20), c.x, c.y + 0.6, c.z, 0, 0, 0);
			}
			case BLOOD -> {
				for (int i = 0; i < 20; i++) {
					double a = -Math.PI * 0.4 + i * Math.PI * 0.8 / 20;
					add(w, mote(0xB0141A, 0.22f, 14), c.x + Math.cos(a) * r * 0.9, c.y + 0.6 + Math.sin(a) * 0.4, c.z + Math.sin(a) * r * 0.9, 0, 0, 0);
				}
				for (int i = 0; i < n; i++) { Vec3d v = sphere(rnd).multiply(0.15); add(w, fx, c.x, c.y + 0.6, c.z, v.x, Math.abs(v.y) + 0.05, v.z); }
			}
			case MANA -> {
				for (int i = 0; i < 18; i++) {
					double a = i * Math.PI * 2 / 18;
					for (int k = 1; k <= 3; k++) {
						double el = k * Math.PI / 8;
						add(w, new FxEffects.Glyph(11, 0x9A8CFF, 0.2f), c.x + Math.cos(a) * Math.cos(el) * r, c.y + Math.sin(el) * r, c.z + Math.sin(a) * Math.cos(el) * r, 0, 0, 0);
					}
				}
				for (int i = 0; i < n / 2; i++) { Vec3d v = sphere(rnd).multiply(0.12); add(w, fx, c.x, c.y + 0.3, c.z, v.x, v.y, v.z); }
				add(w, new FxEffects.Ring(0x9A8CFF, r * 2.4f), c.x, c.y + 0.1, c.z, 0, 0, 0);
			}
		}
	}

	static void self(ClientWorld w, Vec3d c, Element e, float h) {
		var fx = ModParticles.of(e);
		for (int i = 0; i < 40; i++) {
			double t = i / 40.0, a = t * Math.PI * 6;
			add(w, fx, c.x + Math.cos(a) * 0.8, c.y + t * h * 1.2, c.z + Math.sin(a) * 0.8, -Math.sin(a) * 0.04, 0.02, Math.cos(a) * 0.04);
		}
		castCircle(w, c, e, 0.9f);
	}

	static void wall(ClientWorld w, Vec3d c, Vec3d side, Element e, float width) {
		var fx = ModParticles.of(e);
		Random rnd = w.getRandom();
		for (int i = 0; i < width * 12; i++) {
			double off = (rnd.nextDouble() - 0.5) * width;
			add(w, fx, c.x + side.x * off, c.y + rnd.nextDouble() * 0.5, c.z + side.z * off, 0, 0.15 + rnd.nextDouble() * 0.15, 0);
		}
		add(w, new FxEffects.Ring(e.color, width * 1.6f), c.x, c.y + 0.1, c.z, 0, 0, 0);
	}

	static void rune(ClientWorld w, Vec3d c, Element e, float r) {
		add(w, new FxEffects.Circle(e.color, Math.max(1.6f, r * 2f)), c.x, c.y + 0.08, c.z, 0, 0, 0);
		add(w, new FxEffects.FlatGlyph(e.ordinal(), e.color, Math.max(0.8f, r * 0.7f)), c.x, c.y + 0.1, c.z, 0, 0, 0);
		for (int i = 0; i < 16; i++) {
			double a = i * Math.PI / 8;
			add(w, mote(e.color, 0.1f, 24), c.x + Math.cos(a) * r, c.y + 0.1, c.z + Math.sin(a) * r, 0, 0.01, 0);
		}
	}

	static void dispel(ClientWorld w, Vec3d c, float r) {
		Random rnd = w.getRandom();
		for (int i = 0; i < 24; i++) {
			Vec3d v = sphere(rnd).multiply(0.2);
			add(w, new FxEffects.Glyph(rnd.nextInt(16), 0xE8E4FF, 0.22f), c.x, c.y + 1, c.z, v.x, v.y, v.z);
		}
		add(w, new FxEffects.Ring(0xE8E4FF, r * 2.5f), c.x, c.y + 0.2, c.z, 0, 0, 0);
		add(w, new FxEffects.Circle(0xE8E4FF, r * 2.2f), c.x, c.y + 0.06, c.z, 0, 0, 0);
	}

	static void misfire(ClientWorld w, Vec3d c) {
		Random rnd = w.getRandom();
		for (int i = 0; i < 16; i++) add(w, ParticleTypes.SMOKE, c.x + rnd.nextGaussian() * 0.3, c.y, c.z + rnd.nextGaussian() * 0.3, 0, 0.04, 0);
		for (int i = 0; i < 10; i++) { Vec3d v = sphere(rnd).multiply(0.15); add(w, mote(0xFF5A4A, 0.1f, 12), c.x, c.y, c.z, v.x, v.y, v.z); }
	}

	static void overload(ClientWorld w, Vec3d c) {
		Random rnd = w.getRandom();
		for (int i = 0; i < 30; i++) {
			double a = rnd.nextDouble() * Math.PI * 2;
			add(w, ModParticles.of(Element.LIGHTNING), c.x + Math.cos(a) * 0.4, c.y + rnd.nextDouble() * 1.8, c.z + Math.sin(a) * 0.4, 0, 0, 0);
		}
		add(w, mote(0xFF3A2A, 1.4f, 10), c.x, c.y + 1, c.z, 0, 0, 0);
	}

	static void prayer(ClientWorld w, Vec3d c, int deity) {
		int col = Prayers.DEITY_COLOR[Math.max(0, Math.min(3, deity))];
		for (int i = 0; i < 60; i++) {
			double a = i * 0.7, rr = 0.35 + (i % 3) * 0.15;
			add(w, mote(i % 2 == 0 ? col : 0xFFF1C0, 0.18f, 30), c.x + Math.cos(a) * rr, c.y + 6 - i * 0.1, c.z + Math.sin(a) * rr, 0, -0.12, 0);
		}
		add(w, new FxEffects.Circle(col, 4.5f), c.x, c.y + 0.06, c.z, 0, 0, 0);
		add(w, new FxEffects.FlatGlyph(14, col, 1.3f), c.x, c.y + 0.08, c.z, 0, 0, 0);
	}

	static void dragon(ClientWorld w, Vec3d c, Vec3d dir, Element e, float power) {
		Random rnd = w.getRandom();
		var fx = ModParticles.of(e);
		Vec3d d = dir.lengthSquared() < 1e-4 ? new Vec3d(0, 0, 1) : dir.normalize();
		for (int i = 0; i < 40 + power * 6; i++) {
			Vec3d v = d.add(rnd.nextGaussian() * 0.18, rnd.nextGaussian() * 0.12, rnd.nextGaussian() * 0.18).normalize().multiply(0.4 + rnd.nextDouble() * 0.5);
			add(w, i % 3 == 0 ? mote(0xFFC84A, 0.25f, 18) : fx, c.x + d.x * 0.6, c.y - 0.1, c.z + d.z * 0.6, v.x, v.y, v.z);
		}
		add(w, new FxEffects.Glyph(12, 0xFFC84A, 1.0f), c.x + d.x, c.y, c.z + d.z, d.x * 0.05, 0, d.z * 0.05);
	}

	static void spirit(ClientWorld w, Vec3d c, Element e, float r) {
		for (int i = 0; i < 40; i++) {
			double t = i / 40.0, a = t * Math.PI * 4;
			add(w, mote(0xBFFFF8, 0.16f, 34), c.x + Math.cos(a) * r, c.y + 0.3 + t * 2, c.z + Math.sin(a) * r, 0, 0.02, 0);
		}
		add(w, new FxEffects.Circle(0x9FFFF5, r * 2.6f), c.x, c.y + 0.06, c.z, 0, 0, 0);
		add(w, new FxEffects.Glyph(13, 0x9FFFF5, 0.9f), c.x, c.y + 1.2, c.z, 0, 0.01, 0);
		add(w, new FxEffects.Glyph(e.ordinal(), e.color, 0.6f), c.x, c.y + 1.9, c.z, 0, 0.01, 0);
	}

	static void aura(ClientWorld w, Vec3d c, Vec3d dir, boolean wave, float s) {
		if (wave) {
			for (int k = 0; k < 2; k++) add(w, new FxEffects.Ring(0xFF9A3C, s * (1.4f + k)), c.x, c.y - 0.8 + k * 0.2, c.z, 0, 0, 0);
			for (int i = 0; i < 30; i++) {
				double a = i * Math.PI / 15;
				add(w, mote(0xFFB050, 0.2f, 14), c.x, c.y - 0.6, c.z, Math.cos(a) * 0.4, 0.02, Math.sin(a) * 0.4);
			}
			return;
		}
		Vec3d d = dir.lengthSquared() < 1e-4 ? new Vec3d(1, 0, 0) : dir.normalize();
		Vec3d side = d.crossProduct(new Vec3d(0, 1, 0)).normalize();
		for (int i = -6; i <= 6; i++) {
			double t = i / 6.0;
			Vec3d p = c.add(side.multiply(t * 0.9)).add(0, -t * t * 0.4 + t * 0.3, 0);
			add(w, mote(0xFFC070, 0.22f - Math.abs(i) * 0.01f, 8), p.x, p.y, p.z, 0, 0, 0);
		}
	}

	static void rankUp(ClientWorld w, Vec3d c, int rank) {
		for (int i = 0; i < 16; i++) {
			double a = i * Math.PI / 8;
			add(w, new FxEffects.Glyph(i, 0xFFD27A, 0.3f), c.x + Math.cos(a) * 1.5, c.y + 0.2, c.z + Math.sin(a) * 1.5, 0, 0.12, 0);
		}
		for (int i = 0; i < 60; i++) add(w, mote(0xFFE9A0, 0.15f, 40), c.x + Math.cos(i) * 0.6, c.y + i * 0.06, c.z + Math.sin(i) * 0.6, 0, 0.05, 0);
		add(w, new FxEffects.Circle(0xFFD27A, 6f), c.x, c.y + 0.06, c.z, 0, 0, 0);
	}

	static void heal(ClientWorld w, Vec3d c, float h) {
		for (int i = 0; i < 14; i++) add(w, mote(0x9CFF8A, 0.14f, 26), c.x + Math.cos(i) * 0.5, c.y + 0.2 + i * h / 14, c.z + Math.sin(i) * 0.5, 0, 0.03, 0);
		add(w, ParticleTypes.HEART, c.x, c.y + h + 0.3, c.z, 0, 0.05, 0);
	}

	private static Vec3d sphere(Random r) {
		Vec3d v = new Vec3d(r.nextGaussian(), r.nextGaussian(), r.nextGaussian());
		return v.lengthSquared() < 1e-6 ? new Vec3d(0, 1, 0) : v.normalize();
	}
}
