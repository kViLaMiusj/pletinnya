package ua.pletinnya.client.fx;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import ua.pletinnya.fx.Vfx;
import ua.pletinnya.magic.Element;

import static ua.pletinnya.client.fx.Geo.*;

/**
 * Об'ємний образ закляття в світі: вибухи, крижані шпилі, кам'яні уламки, стовпи світла, вихори, руни.
 * Радіус (розмір закляття) задає розмах, сила — кількість уламків, висоту й яскравість.
 * Малюється як «власна» частинка, тож живе й зникає сама.
 */
public class SpellModelParticle extends Particle {
	private final int kind;
	private final Element el;
	private final float r, power;
	private final Vec3d dir;
	private final int seed;

	protected SpellModelParticle(ClientWorld world, int kind, Element el, Vec3d c, Vec3d dir, float r, float power, int life) {
		super(world, c.x, c.y, c.z);
		this.kind = kind;
		this.el = el;
		this.r = Math.max(0.4f, r);
		this.power = MathHelper.clamp(power, 1f, 12f);
		this.dir = dir;
		this.maxAge = life;
		this.collidesWithWorld = false;
		this.gravityStrength = 0f;
		this.velocityX = this.velocityY = this.velocityZ = 0;
		this.seed = world.random.nextInt(100000);
	}

	public static void spawn(ClientWorld w, Vfx.Payload p) {
		Element e = Element.byIndex(p.element());
		Vec3d c = new Vec3d(p.x(), p.y(), p.z());
		Vec3d d = new Vec3d(p.dx(), p.dy(), p.dz());
		float s = p.size(), pw = p.power();
		int life;
		switch (p.kind()) {
			case Vfx.IMPACT, Vfx.RUNE_FIRE -> life = (int) MathHelper.clamp(18 + s * 2.5f, 18, 42);
			case Vfx.CAST -> life = 22;
			case Vfx.RUNE -> life = 40;
			case Vfx.SELF -> life = 26;
			case Vfx.DRAGON -> life = 22;
			case Vfx.MISFIRE, Vfx.OVERLOAD -> life = 16;
			default -> { return; }
		}
		int kind = p.kind() == Vfx.RUNE_FIRE ? Vfx.IMPACT : p.kind();
		MinecraftClient.getInstance().particleManager.addParticle(new SpellModelParticle(w, kind, e, c, d, s, pw, life));
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer ignored, Camera camera, float td) {
		Vec3d cp = camera.getPos();
		MatrixStack m = new MatrixStack();
		m.translate(x - cp.x, y - cp.y, z - cp.z);
		VertexConsumerProvider.Immediate imm = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		float t = age + td;
		float f = MathHelper.clamp(t / maxAge, 0f, 1f);
		VertexConsumer vc = imm.getBuffer(solid());
		switch (kind) {
			case Vfx.IMPACT -> impact(m, vc, t, f);
			case Vfx.CAST -> cast(m, vc, t, f);
			case Vfx.RUNE -> rune(m, vc, t, f);
			case Vfx.SELF -> self(m, vc, t, f);
			case Vfx.DRAGON -> dragon(m, vc, t, f);
			case Vfx.MISFIRE, Vfx.OVERLOAD -> misfire(m, vc, t, f);
			default -> {}
		}
		// спалах-сяйво в центрі, обернене до камери
		if (kind == Vfx.IMPACT && f < 0.45f) {
			m.push();
			m.multiply(camera.getRotation());
			float k = 1f - f / 0.45f;
			flat(imm.getBuffer(glow()), m.peek(), r * (1.4f + power * 0.08f) * (0.6f + 0.6f * (1 - k)),
				argb((int) (k * (el == Element.DARKNESS ? 120 : 200)), el == Element.DARKNESS ? 0x7A4AC0 : el.color));
			m.pop();
		}
		imm.draw();
	}

	private static float easeOut(float f) { return 1f - (1f - f) * (1f - f); }

	private float h(int i) { return hash(seed * 31 + i); }

	private int fade(float f, int a) { return (int) (a * (f < 0.75f ? 1f : (1f - f) / 0.25f)); }

	// ====================================================================== влучання
	private void impact(MatrixStack m, VertexConsumer vc, float t, float f) {
		int col = el.color;
		// ударна хвиля по землі
		m.push();
		m.translate(0, 0.06, 0);
		ring(vc, m.peek(), r * (0.25f + 0.95f * easeOut(f)), 0.12f + r * 0.05f, 28, argb((int) ((1 - f) * 190), col));
		if (f > 0.12f) ring(vc, m.peek(), r * (0.15f + 0.7f * easeOut((f - 0.12f) / 0.88f)), 0.08f + r * 0.03f, 24, argb((int) ((1 - f) * 140), shade(col, 1.25f)));
		m.pop();
		int n = (int) Math.min(34, 6 + power * 2.2f);
		float up = 1f + power * 0.14f;
		switch (el) {
			case FIRE -> {
				float core = f < 0.4f ? easeOut(f / 0.4f) : 1f;
				int ca = (int) (f < 0.4f ? 230 : 230 * (1 - (f - 0.4f) / 0.6f));
				if (ca > 4) {
					m.push();
					m.translate(0, r * 0.25f, 0);
					m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 6));
					cube(vc, m.peek(), r * 0.7f * core, r * 0.6f * core, r * 0.7f * core, argb(ca, 0xFFB040));
					m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));
					cube(vc, m.peek(), r * 0.5f * core, r * 0.5f * core, r * 0.5f * core, argb(ca, 0xFFE27A));
					m.pop();
				}
				for (int i = 0; i < n; i++) {
					float a = h(i) * 6.283f, sp = 0.4f + h(i + 50) * 0.6f;
					float px = MathHelper.cos(a) * r * 0.8f * sp * easeOut(f), pz = MathHelper.sin(a) * r * 0.8f * sp * easeOut(f);
					float py = f * up * (1.2f + h(i + 90) * 1.6f);
					float s = (0.18f + 0.18f * h(i + 7)) * (1f - f * 0.8f) * (0.7f + r * 0.12f);
					int c = f < 0.35f ? mix(0xFFE27A, 0xFF7A20, f / 0.35f) : mix(0xFF7A20, 0x3A2420, (f - 0.35f) / 0.65f);
					m.push();
					m.translate(px, py, pz);
					m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 10 + i * 40));
					cube(vc, m.peek(), s, s * 1.3f, s, argb(fade(f, 220), c));
					m.pop();
				}
			}
			case WATER -> {
				float hb = MathHelper.sin(f * MathHelper.PI) * up * 1.1f;
				m.push();
				band(vc, m.peek(), r * (0.35f + 0.5f * easeOut(f)), hb, 26, argb(fade(f, 150), 0x3A86E0), argb(0, 0x8CC8FF));
				band(vc, m.peek(), r * (0.2f + 0.3f * easeOut(f)), hb * 1.5f, 20, argb(fade(f, 110), 0x8CC8FF), argb(0, 0xCFEAFF));
				m.pop();
				for (int i = 0; i < n; i++) {
					float a = h(i) * 6.283f, sp = 0.5f + h(i + 30) * 0.7f;
					float d = r * 0.9f * sp * f;
					float py = (2.6f * f - 3.2f * f * f) * up * (0.6f + h(i + 60) * 0.6f);
					if (py < -0.1f) continue;
					m.push();
					m.translate(MathHelper.cos(a) * d, py, MathHelper.sin(a) * d);
					cube(vc, m.peek(), 0.12f, 0.16f, 0.12f, argb(fade(f, 220), 0x8CC8FF));
					m.pop();
				}
			}
			case ICE, BLOOD, NATURE -> {
				int main = el == Element.ICE ? 0xCFF4FF : el == Element.BLOOD ? 0xB0202A : 0x6A8A3A;
				int tip = el == Element.ICE ? 0xF4FDFF : el == Element.BLOOD ? 0xE04040 : 0x9ACD5A;
				float grow = Math.min(1f, f * 5f) * (f > 0.78f ? (1f - f) / 0.22f : 1f);
				int spikes = Math.min(18, 4 + (int) (power * 1.2f));
				for (int i = 0; i < spikes; i++) {
					float a = (i + h(i) * 0.6f) * 6.283f / spikes;
					float d = r * (0.3f + 0.55f * h(i + 11));
					float hh = (0.6f + h(i + 22) * 0.8f) * (0.7f + power * 0.08f) * (0.6f + r * 0.12f) * grow;
					m.push();
					m.translate(MathHelper.cos(a) * d, 0, MathHelper.sin(a) * d);
					m.multiply(RotationAxis.POSITIVE_Y.rotation(-a));
					m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-18 - 14 * h(i + 33)));
					crystal(vc, m.peek(), 0.14f + r * 0.035f, hh, 0.1f, argb(235, main));
					crystal(vc, m.peek(), 0.06f + r * 0.015f, hh * 1.08f, 0.05f, argb(255, tip));
					m.pop();
				}
				if (el == Element.NATURE) for (int i = 0; i < n / 2; i++) {
					float a = h(i + 70) * 6.283f + t * 0.12f, d = r * 0.6f * (0.4f + f);
					m.push();
					m.translate(MathHelper.cos(a) * d, f * up * 1.5f * h(i + 80), MathHelper.sin(a) * d);
					m.multiply(RotationAxis.POSITIVE_Y.rotation(a + t * 0.2f));
					m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35));
					cube(vc, m.peek(), 0.24f, 0.03f, 0.13f, argb(fade(f, 230), i % 3 == 0 ? 0xFF9ACD : 0x6FCF4A));
					m.pop();
				}
				if (el == Element.BLOOD) for (int i = 0; i < n / 2; i++) {
					float a = h(i + 40) * 6.283f, d = r * 0.7f * f;
					float py = (2.2f * f - 3f * f * f) * up;
					if (py < 0) continue;
					m.push();
					m.translate(MathHelper.cos(a) * d, py, MathHelper.sin(a) * d);
					cube(vc, m.peek(), 0.1f, 0.14f, 0.1f, argb(230, 0x8A0A10));
					m.pop();
				}
			}
			case EARTH -> {
				float grow = Math.min(1f, f * 5f) * (f > 0.8f ? (1f - f) / 0.2f : 1f);
				for (int i = 0; i < Math.min(12, 3 + (int) power); i++) {
					float a = (i + h(i)) * 6.283f / Math.min(12, 3 + (int) power), d = r * (0.25f + 0.5f * h(i + 5));
					float hh = (0.5f + h(i + 9) * 0.7f) * (0.7f + power * 0.07f) * grow;
					m.push();
					m.translate(MathHelper.cos(a) * d, 0, MathHelper.sin(a) * d);
					m.multiply(RotationAxis.POSITIVE_Y.rotation(-a));
					m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-12));
					box(vc, m.peek(), -0.2f, 0, -0.2f, 0.2f, hh, 0.2f, argb(255, i % 2 == 0 ? 0x7A6248 : 0x6E6A62));
					m.pop();
				}
				for (int i = 0; i < n; i++) {
					float a = h(i) * 6.283f, sp = 0.5f + h(i + 20) * 0.6f, d = r * sp * f;
					float py = (3.0f * f - 3.6f * f * f) * up * (0.5f + h(i + 40) * 0.6f);
					if (py < 0) continue;
					float s = 0.14f + 0.16f * h(i + 3);
					m.push();
					m.translate(MathHelper.cos(a) * d, py, MathHelper.sin(a) * d);
					m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(t * 14 + i * 50));
					m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t * 9));
					cube(vc, m.peek(), s, s * 0.8f, s, argb(255, i % 3 == 0 ? 0x8B6B3E : 0x6E6052));
					m.pop();
				}
			}
			case LIGHTNING -> {
				if (f < 0.55f) {
					int sd = seed + (int) (t / 2) * 97;
					float top = 6f + power * 0.8f;
					float[][] pts = new float[9][];
					for (int k = 0; k < 9; k++) {
						float g = k / 8f;
						float j = k == 0 || k == 8 ? 0 : (hash(sd + k) - 0.5f) * 0.9f;
						float jz = k == 0 || k == 8 ? 0 : (hash(sd + k + 20) - 0.5f) * 0.9f;
						pts[k] = new float[]{j, top * (1 - g), jz};
					}
					float w = 0.22f + power * 0.02f;
					bolt(vc, m.peek(), pts, w, argb(230, 0xF5E663));
					bolt(vc, m.peek(), pts, w * 0.35f, argb(255, 0xFFFFFF));
					for (int b = 0; b < 2 + (int) (power / 3); b++) {
						int k0 = 2 + (int) (hash(sd + b * 5) * 5);
						float[] st = pts[k0];
						float a = hash(sd + b * 9) * 6.283f;
						float[][] br = {st, {st[0] + MathHelper.cos(a) * 0.8f, st[1] - 0.7f, st[2] + MathHelper.sin(a) * 0.8f},
							{st[0] + MathHelper.cos(a) * 1.5f, st[1] - 1.6f, st[2] + MathHelper.sin(a) * 1.4f}};
						bolt(vc, m.peek(), br, w * 0.5f, argb(200, 0xF5E663));
					}
				}
				for (int i = 0; i < n; i++) {
					float a = h(i) * 6.283f, d = r * (0.3f + 0.8f * h(i + 2)) * easeOut(f);
					float py = 0.2f + h(i + 4) * up * (1 - f);
					float[] s0 = {MathHelper.cos(a) * d, py, MathHelper.sin(a) * d};
					float[] s1 = {s0[0] + (h(i + 6) - 0.5f) * 0.5f, s0[1] + (h(i + 8) - 0.5f) * 0.5f, s0[2] + (h(i + 10) - 0.5f) * 0.5f};
					segment(vc, m.peek(), s0, s1, 0.06f, argb(fade(f, 240), 0xFFF6A0));
				}
			}
			case WIND -> {
				int rings = 4 + (int) (power / 2);
				for (int k = 0; k < rings; k++) {
					float yy = k * 0.38f * up * Math.min(1f, f * 3f);
					m.push();
					m.translate(0, yy, 0);
					m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * (18 + k * 4)));
					ring(vc, m.peek(), r * (0.25f + k * 0.11f) * (0.6f + 0.5f * f), 0.08f, 18, argb(fade(f, 130 - k * 8), 0xDFF7EE));
					for (int b = 0; b < 3; b++) {
						float a = b * 2.094f;
						float rr = r * (0.25f + k * 0.11f) * (0.6f + 0.5f * f);
						float[] p0 = {MathHelper.cos(a) * rr, 0, MathHelper.sin(a) * rr};
						float[] p1 = {MathHelper.cos(a + 0.9f) * rr, 0.25f, MathHelper.sin(a + 0.9f) * rr};
						segment(vc, m.peek(), p0, p1, 0.07f, argb(fade(f, 150), 0xFFFFFF));
					}
					m.pop();
				}
			}
			case SOUND -> {
				for (int k = 0; k < 3 + (int) (power / 3); k++) {
					float ph = f * 1.4f - k * 0.18f;
					if (ph <= 0 || ph >= 1) continue;
					m.push();
					m.translate(0, 0.1f + k * 0.05f, 0);
					ring(vc, m.peek(), r * (0.2f + 1.0f * ph), 0.14f, 30, argb((int) ((1 - ph) * 200), 0xC9B6F0));
					band(vc, m.peek(), r * (0.2f + 1.0f * ph), 0.4f + power * 0.05f, 30, argb((int) ((1 - ph) * 90), 0xB39DDB), argb(0, 0xB39DDB));
					m.pop();
				}
			}
			case LIGHT -> {
				float hb = (7f + power) * Math.min(1f, f * 6f);
				float w = (0.35f + r * 0.12f) * (1f - f * 0.7f);
				box(vc, m.peek(), -w / 2, 0, -w / 2, w / 2, hb, w / 2, argb(fade(f, 170), 0xFFF6C8));
				box(vc, m.peek(), -w / 4, 0, -w / 4, w / 4, hb, w / 4, argb(fade(f, 240), 0xFFFFFF));
				m.push();
				m.translate(0, 0.08, 0);
				m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 3));
				for (int k = 0; k < 4; k++) {
					m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45));
					box(vc, m.peek(), -r * 0.9f, 0, -0.05f, r * 0.9f, 0.04f, 0.05f, argb(fade(f, 200), 0xFFE89A));
				}
				m.pop();
			}
			case DARKNESS -> {
				float k = 1f - easeOut(f);
				m.push();
				m.translate(0, r * 0.4f, 0);
				m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 8));
				cube(vc, m.peek(), r * 0.9f * k, r * 0.9f * k, r * 0.9f * k, argb(200, 0x120A1C));
				m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));
				cube(vc, m.peek(), r * 1.1f * k, r * 1.1f * k, r * 1.1f * k, argb(90, 0x6A3AA8));
				m.pop();
				for (int i = 0; i < n; i++) {
					float a = h(i) * 6.283f;
					float[][] pts = new float[5][];
					for (int q = 0; q < 5; q++) {
						float g = q / 4f;
						float rr = r * 1.3f * (1 - g) * (1 - f * 0.6f);
						float wob = MathHelper.sin(t * 0.4f + i + g * 4f) * 0.25f * (1 - g);
						pts[q] = new float[]{MathHelper.cos(a + wob) * rr, r * 0.4f * g + h(i + 3) * 0.4f * (1 - g), MathHelper.sin(a + wob) * rr};
					}
					bolt(vc, m.peek(), pts, 0.08f, argb(fade(f, 170), 0x4A2A6E));
				}
			}
			case MANA -> {
				for (int i = 0; i < n; i++) {
					float a = h(i) * 6.283f, el2 = (h(i + 9) - 0.3f) * 1.2f;
					float d = r * 1.1f * easeOut(f) * (0.5f + h(i + 4) * 0.6f);
					m.push();
					m.translate(MathHelper.cos(a) * MathHelper.cos(el2) * d, 0.4f + MathHelper.sin(el2) * d, MathHelper.sin(a) * MathHelper.cos(el2) * d);
					m.multiply(RotationAxis.POSITIVE_Y.rotation(-a));
					m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90 + el2 * 57f));
					crystal(vc, m.peek(), 0.07f, 0.3f, 0.12f, argb(fade(f, 230), i % 3 == 0 ? 0xE8E4FF : 0x8A7CFF));
					m.pop();
				}
				for (int k = 0; k < 2; k++) {
					m.push();
					m.translate(0, 0.6f, 0);
					m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 4 * (k == 0 ? 1 : -1)));
					m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(k == 0 ? 70 : -70));
					ring(vc, m.peek(), r * (0.4f + 0.5f * easeOut(f)), 0.07f, 26, argb(fade(f, 170), 0xB8AEFF));
					m.pop();
				}
			}
		}
	}

	// ====================================================================== коло плетіння біля ніг
	private void cast(MatrixStack m, VertexConsumer vc, float t, float f) {
		int col = el.color;
		float rr = r * 0.85f;
		int a = (int) (f < 0.15f ? f / 0.15f * 210 : 210 * (1 - Math.max(0, f - 0.6f) / 0.4f));
		m.push();
		m.translate(0, 0.05, 0);
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 5));
		ring(vc, m.peek(), rr, 0.07f, 32, argb(a, col));
		ring(vc, m.peek(), rr * 0.72f, 0.05f, 28, argb(a, 0xB8AEFF));
		int n = 6 + (int) (power / 2);
		for (int i = 0; i < n; i++) {
			float ang = (float) (i * Math.PI * 2 / n);
			m.push();
			m.translate(MathHelper.cos(ang) * rr * 1.05f, 0.1f + f * (0.6f + 0.1f * (i % 3)), MathHelper.sin(ang) * rr * 1.05f);
			m.multiply(RotationAxis.POSITIVE_Y.rotation(-ang));
			box(vc, m.peek(), -0.025f, 0, -0.09f, 0.025f, 0.26f, 0.09f, argb(a, i % 2 == 0 ? col : 0xE8E4FF));
			m.pop();
		}
		m.pop();
		m.push();
		m.translate(0, 0.06, 0);
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-t * 7));
		for (int k = 0; k < 3; k++) {
			float a0 = k * 2.094f, a1 = a0 + 2.094f;
			float[] p0 = {MathHelper.cos(a0) * rr * 0.72f, 0, MathHelper.sin(a0) * rr * 0.72f};
			float[] p1 = {MathHelper.cos(a1 + 1.047f) * rr * 0.72f, 0, MathHelper.sin(a1 + 1.047f) * rr * 0.72f};
			segment(vc, m.peek(), p0, p1, 0.045f, argb(a, col));
		}
		m.pop();
	}

	// ====================================================================== руна-пастка
	private void rune(MatrixStack m, VertexConsumer vc, float t, float f) {
		int col = el.color;
		float rr = Math.max(0.9f, r);
		int a = (int) (f < 0.1f ? f / 0.1f * 220 : 220 * (1 - Math.max(0, f - 0.7f) / 0.3f));
		m.push();
		m.translate(0, 0.07, 0);
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 1.5f));
		ring(vc, m.peek(), rr, 0.09f, 36, argb(a, col));
		ring(vc, m.peek(), rr * 0.78f, 0.05f, 32, argb(a, col));
		float[][] star = new float[5][];
		for (int i = 0; i < 5; i++) {
			double ang = i * Math.PI * 2 / 5;
			star[i] = new float[]{(float) Math.cos(ang) * rr * 0.78f, 0, (float) Math.sin(ang) * rr * 0.78f};
		}
		for (int i = 0; i < 5; i++) segment(vc, m.peek(), star[i], star[(i + 2) % 5], 0.05f, argb(a, shade(col, 1.2f)));
		for (int i = 0; i < 4; i++) {
			float ang = i * 1.571f;
			m.push();
			m.translate(MathHelper.cos(ang) * rr, 0.35f + 0.12f * MathHelper.sin(t * 0.2f + i), MathHelper.sin(ang) * rr);
			m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 4));
			crystal(vc, m.peek(), 0.07f, 0.18f, 0.18f, argb(a, col));
			m.pop();
		}
		m.pop();
	}

	// ====================================================================== на себе: кільця вгору по тілу
	private void self(MatrixStack m, VertexConsumer vc, float t, float f) {
		int col = el.color;
		for (int k = 0; k < 3; k++) {
			float ph = f * 1.3f - k * 0.15f;
			if (ph <= 0 || ph >= 1) continue;
			m.push();
			m.translate(0, ph * r * 1.15f, 0);
			m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 6 + k * 40));
			ring(vc, m.peek(), 0.75f - ph * 0.15f, 0.07f, 26, argb((int) (Math.sin(ph * Math.PI) * 210), col));
			for (int i = 0; i < 4; i++) {
				m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
				m.push();
				m.translate(0.75f - ph * 0.15f, 0, 0);
				crystal(vc, m.peek(), 0.04f, 0.12f, 0.12f, argb((int) (Math.sin(ph * Math.PI) * 230), 0xFFFFFF));
				m.pop();
			}
			m.pop();
		}
	}

	// ====================================================================== драконячий подих: конус уламків уперед
	private void dragon(MatrixStack m, VertexConsumer vc, float t, float f) {
		Vec3d d = dir.lengthSquared() < 1e-4 ? new Vec3d(0, 0, 1) : dir.normalize();
		int col = el.color;
		int n = 14 + (int) (power * 2);
		for (int i = 0; i < n; i++) {
			float g = (f * 1.6f - h(i) * 0.6f);
			if (g <= 0 || g >= 1) continue;
			float dist = g * (5f + power * 0.6f);
			float spread = g * (0.4f + power * 0.08f);
			Vec3d p = d.multiply(dist).add((h(i + 3) - 0.5f) * spread * 2, (h(i + 5) - 0.5f) * spread * 2, (h(i + 7) - 0.5f) * spread * 2);
			float s = 0.18f + 0.3f * g;
			m.push();
			m.translate(p.x, p.y, p.z);
			m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 12 + i * 30));
			cube(vc, m.peek(), s, s, s, argb((int) ((1 - g) * 220), mix(0xFFE27A, col, g)));
			m.pop();
		}
	}

	// ====================================================================== збій плетіння: тріщини-уламки
	private void misfire(MatrixStack m, VertexConsumer vc, float t, float f) {
		for (int i = 0; i < 10; i++) {
			float a = h(i) * 6.283f, d = 0.3f + f * 1.2f * h(i + 2);
			m.push();
			m.translate(MathHelper.cos(a) * d, 0.3f + h(i + 4) * 1.2f, MathHelper.sin(a) * d);
			m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t * 20 + i * 36));
			box(vc, m.peek(), -0.02f, -0.12f, -0.02f, 0.02f, 0.12f, 0.02f, argb((int) ((1 - f) * 230), i % 2 == 0 ? 0x6A3AA8 : 0x2A1A3A));
			m.pop();
		}
	}
}
