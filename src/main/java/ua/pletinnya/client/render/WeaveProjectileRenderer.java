package ua.pletinnya.client.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.client.fx.Geo;
import ua.pletinnya.entity.WeaveProjectileEntity;
import ua.pletinnya.magic.Element;

import static ua.pletinnya.client.fx.Geo.*;

/**
 * Снаряд — об'ємний образ свого напряму. Розмір закляття збільшує модель, сила — кількість деталей,
 * яскравість і швидкість руху, якість плетіння — спокій образу (погане плетіння тремтить).
 */
public class WeaveProjectileRenderer extends EntityRenderer<WeaveProjectileEntity> {
	public WeaveProjectileRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(WeaveProjectileEntity entity) {
		return Pletinnya.id("textures/item/essence_" + entity.element().id + ".png");
	}

	@Override
	public void render(WeaveProjectileEntity en, float yaw, float tickDelta, MatrixStack m, VertexConsumerProvider vcp, int light) {
		float t = en.age + tickDelta;
		int size = en.sizeStat(), power = en.powerStat(), quality = en.qualityStat();
		float S = 0.38f + size * 0.09f;
		int P = MathHelper.clamp(power, 1, 10);
		Element el = en.element();
		int id = en.getId();

		m.push();
		m.translate(0, 0.15, 0);
		// неспокій поганого плетіння
		float jit = (100 - quality) / 100f * 0.07f * S;
		if (jit > 0.002f) {
			int k = (int) (t * 2) * 7 + id;
			m.translate((hash(k) - 0.5f) * jit, (hash(k + 1) - 0.5f) * jit, (hash(k + 2) - 0.5f) * jit);
		}
		// сяйво — завжди до камери
		m.push();
		m.multiply(this.dispatcher.getRotation());
		float pulse = 1f + 0.08f * MathHelper.sin(t * 0.45f);
		int glowA = el == Element.DARKNESS ? 70 : el == Element.EARTH ? 40 : 90 + P * 12;
		flat(vcp.getBuffer(glow()), m.peek(), S * (1.7f + P * 0.09f) * pulse, argb(glowA, el == Element.DARKNESS ? 0x7A4AC0 : el.color));
		m.pop();

		// орієнтація вздовж польоту: локальна +Z — уперед
		Vec3d v = en.getVelocity();
		if (v.lengthSquared() > 1e-5) {
			float yw = (float) MathHelper.atan2(v.x, v.z);
			float pt = (float) MathHelper.atan2(v.y, Math.sqrt(v.x * v.x + v.z * v.z));
			m.multiply(RotationAxis.POSITIVE_Y.rotation(yw));
			m.multiply(RotationAxis.POSITIVE_X.rotation(-pt));
		}
		VertexConsumer vc = vcp.getBuffer(solid());
		drawElement(m, vc, el, t, S, P, id);
		int e2 = en.element2Stat();
		if (e2 >= 0) {
			// друга стихія злиття — менший образ кружляє довкола першого
			m.push();
			m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t * 12));
			m.translate(S * 0.62f, 0, 0);
			drawElement(m, vc, Element.byIndex(e2), t + 7, S * 0.5f, Math.max(1, P / 2), id + 1);
			m.pop();
		}
		m.pop();
		super.render(en, yaw, tickDelta, m, vcp, light);
	}

	// ====================================================================== образи

	/** Об'ємний образ стихії з центром у (0,0,0), уперед — локальна +Z. */
	public static void drawElement(MatrixStack m, VertexConsumer vc, Element el, float t, float S, int P, int id) {
		switch (el) {
			case FIRE -> fire(m, vc, t, S, P);
			case WATER -> water(m, vc, t, S, P);
			case ICE -> ice(m, vc, t, S, P);
			case LIGHTNING -> lightning(m, vc, t, S, P, id);
			case EARTH -> earth(m, vc, t, S, P, id);
			case WIND -> wind(m, vc, t, S, P);
			case NATURE -> nature(m, vc, t, S, P);
			case SOUND -> sound(m, vc, t, S, P);
			case LIGHT -> light(m, vc, t, S, P);
			case DARKNESS -> darkness(m, vc, t, S, P);
			case BLOOD -> blood(m, vc, t, S, P);
			case MANA -> mana(m, vc, t, S, P);
		}
	}

	static void fire(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 9));
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(t * 6));
		cube(vc, m.peek(), S * 0.3f, S * 0.3f, S * 0.3f, argb(255, 0xFFE27A));
		m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
		cube(vc, m.peek(), S * 0.46f, S * 0.46f, S * 0.46f, argb(170, 0xFF8A2A));
		m.pop();
		int n = 4 + P / 2;
		for (int i = 0; i < n; i++) {
			float a = (float) (i * Math.PI * 2 / n) + t * 0.18f;
			float r = S * 0.17f;
			float len = S * (0.55f + 0.3f * MathHelper.sin(t * 0.7f + i * 1.9f)) * (0.85f + P * 0.05f);
			float w = S * (0.13f - 0.006f * i);
			float x = MathHelper.cos(a) * r, y = MathHelper.sin(a) * r;
			box(vc, m.peek(), x - w / 2, y - w / 2, -len, x + w / 2, y + w / 2, S * 0.1f, argb(185, mix(0xFFB040, 0xC8301A, i / (float) n)));
		}
	}

	static void water(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t * 4));
		cube(vc, m.peek(), S * 0.28f, S * 0.28f, S * 0.28f, argb(235, 0xCFEAFF));
		cube(vc, m.peek(), S * 0.5f, S * 0.42f, S * 0.5f, argb(150, 0x3A86E0));
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45));
		cube(vc, m.peek(), S * 0.42f, S * 0.5f, S * 0.42f, argb(120, 0x5AA0F0));
		m.pop();
		int n = 3 + P / 2;
		for (int i = 0; i < n; i++) {
			float a = (float) (i * Math.PI * 2 / n) + t * 0.28f;
			float r = S * 0.46f;
			m.push();
			m.translate(MathHelper.cos(a) * r, MathHelper.sin(a) * r, -S * 0.1f * (i % 2));
			cube(vc, m.peek(), S * 0.1f, S * 0.1f, S * 0.14f, argb(220, 0x8CC8FF));
			m.pop();
		}
		m.push();
		m.translate(0, 0, -S * 0.15f);
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
		ring(vc, m.peek(), S * (0.55f + 0.05f * MathHelper.sin(t * 0.5f)), S * 0.05f, 20, argb(110, 0x8CC8FF));
		m.pop();
	}

	static void ice(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 3));
		crystal(vc, m.peek(), S * 0.22f, S * 0.75f, S * 0.38f, argb(225, 0xBFEFFF));
		crystal(vc, m.peek(), S * 0.11f, S * 0.55f, S * 0.25f, argb(255, 0xF4FDFF));
		m.pop();
		int n = 3 + P / 3;
		for (int i = 0; i < n; i++) {
			float a = (float) (i * Math.PI * 2 / n) + 0.4f;
			m.push();
			m.translate(MathHelper.cos(a) * S * 0.2f, MathHelper.sin(a) * S * 0.2f, -S * 0.12f);
			m.multiply(RotationAxis.POSITIVE_Z.rotation(a - (float) Math.PI / 2));
			m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-120));
			crystal(vc, m.peek(), S * 0.07f, S * (0.32f + 0.03f * (i % 3)), S * 0.08f, argb(210, 0x9EE6F5));
			m.pop();
		}
	}

	static void lightning(MatrixStack m, VertexConsumer vc, float t, float S, int P, int id) {
		cube(vc, m.peek(), S * 0.22f, S * 0.22f, S * 0.22f, argb(255, 0xFFFFF0));
		int seed = (int) (t / 2) * 131 + id * 17;
		int n = 3 + P / 2;
		for (int i = 0; i < n; i++) {
			float dx = hash(seed + i * 7) - 0.5f, dy = hash(seed + i * 7 + 1) - 0.5f, dz = hash(seed + i * 7 + 2) - 0.8f;
			float l = MathHelper.sqrt(dx * dx + dy * dy + dz * dz) + 1e-3f;
			float len = S * (0.55f + 0.45f * hash(seed + i * 7 + 3));
			float[][] pts = new float[5][];
			for (int k = 0; k < 5; k++) {
				float f = k / 4f;
				float jx = k == 0 ? 0 : (hash(seed + i * 31 + k) - 0.5f) * S * 0.25f;
				float jy = k == 0 ? 0 : (hash(seed + i * 37 + k) - 0.5f) * S * 0.25f;
				pts[k] = new float[]{dx / l * len * f + jx, dy / l * len * f + jy, dz / l * len * f};
			}
			bolt(vc, m.peek(), pts, S * 0.07f, argb(220, 0xF5E663));
			bolt(vc, m.peek(), pts, S * 0.025f, argb(255, 0xFFFFFF));
		}
	}

	static void earth(MatrixStack m, VertexConsumer vc, float t, float S, int P, int id) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(t * 7));
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 4));
		int n = 4 + P / 3;
		int[] cols = {0x8B6B3E, 0x6E6A62, 0xA08060, 0x5A5048};
		for (int i = 0; i < n; i++) {
			float ox = (hash(id + i * 11) - 0.5f) * S * 0.35f, oy = (hash(id + i * 13) - 0.5f) * S * 0.35f, oz = (hash(id + i * 17) - 0.5f) * S * 0.35f;
			float s = S * (0.2f + 0.14f * hash(id + i * 19));
			m.push();
			m.translate(ox, oy, oz);
			m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(hash(id + i * 23) * 90));
			cube(vc, m.peek(), s, s * 0.85f, s, argb(255, cols[i % cols.length]));
			m.pop();
		}
		m.pop();
		for (int i = 0; i < 2 + P / 4; i++) {
			float a = t * 0.3f + i * 2.4f;
			m.push();
			m.translate(MathHelper.cos(a) * S * 0.5f, MathHelper.sin(a * 1.3f) * S * 0.2f, MathHelper.sin(a) * S * 0.5f);
			cube(vc, m.peek(), S * 0.08f, S * 0.08f, S * 0.08f, argb(255, 0x7A6A55));
			m.pop();
		}
	}

	static void wind(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		int n = 3 + P / 3;
		for (int i = 0; i < n; i++) {
			float base = t * 0.45f + (float) (i * Math.PI * 2 / n);
			float[][] pts = new float[6][];
			for (int k = 0; k < 6; k++) {
				float f = k / 5f;
				float a = base + f * 1.6f;
				float r = S * (0.12f + f * 0.32f);
				pts[k] = new float[]{MathHelper.cos(a) * r, MathHelper.sin(a) * r, -f * S * 0.7f + S * 0.15f};
			}
			bolt(vc, m.peek(), pts, S * 0.09f, argb(150, 0xDFF7EE));
		}
		for (int k = 0; k < 2; k++) {
			m.push();
			m.translate(0, 0, -S * (0.25f + k * 0.3f));
			m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
			m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 20));
			ring(vc, m.peek(), S * (0.42f - k * 0.1f), S * 0.04f, 16, argb(90, 0xBFEBDD));
			m.pop();
		}
		cube(vc, m.peek(), S * 0.14f, S * 0.14f, S * 0.14f, argb(200, 0xF0FFF8));
	}

	static void nature(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 4));
		crystal(vc, m.peek(), S * 0.17f, S * 0.5f, S * 0.28f, argb(255, 0x5FAF3A));
		crystal(vc, m.peek(), S * 0.08f, S * 0.62f, S * 0.1f, argb(255, 0x8A6A42));
		m.pop();
		int n = 3 + P / 3;
		for (int i = 0; i < n; i++) {
			float a = t * 0.22f + (float) (i * Math.PI * 2 / n);
			m.push();
			m.translate(MathHelper.cos(a) * S * 0.38f, MathHelper.sin(a) * S * 0.38f, -S * 0.08f);
			m.multiply(RotationAxis.POSITIVE_Z.rotation(a));
			m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30 + 20 * MathHelper.sin(t * 0.3f + i)));
			cube(vc, m.peek(), S * 0.26f, S * 0.03f, S * 0.13f, argb(240, i == 0 ? 0xFF9ACD : (i % 2 == 0 ? 0x6FCF4A : 0x4C9A30)));
			m.pop();
		}
	}

	static void sound(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		cube(vc, m.peek(), S * 0.18f, S * 0.18f, S * 0.18f, argb(255, 0xE2D6FF));
		int n = 2 + P / 3;
		for (int k = 0; k < n; k++) {
			float ph = (t * 0.07f + k / (float) n) % 1f;
			m.push();
			m.translate(0, 0, S * (0.2f - ph * 0.6f));
			m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
			ring(vc, m.peek(), S * (0.15f + ph * 0.65f), S * 0.05f, 22, argb((int) ((1 - ph) * 210), 0xC9B6F0));
			m.pop();
		}
	}

	static void light(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t * 4));
		float L = S * (0.9f + P * 0.03f);
		cube(vc, m.peek(), L, S * 0.08f, S * 0.08f, argb(235, 0xFFF6C8));
		cube(vc, m.peek(), S * 0.08f, L, S * 0.08f, argb(235, 0xFFF6C8));
		cube(vc, m.peek(), S * 0.08f, S * 0.08f, L * 0.7f, argb(235, 0xFFF6C8));
		m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
		cube(vc, m.peek(), L * 0.6f, S * 0.06f, S * 0.06f, argb(200, 0xFFE89A));
		cube(vc, m.peek(), S * 0.06f, L * 0.6f, S * 0.06f, argb(200, 0xFFE89A));
		m.pop();
		cube(vc, m.peek(), S * 0.24f, S * 0.24f, S * 0.24f, argb(255, 0xFFFFFF));
	}

	static void darkness(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		cube(vc, m.peek(), S * 0.32f, S * 0.32f, S * 0.32f, argb(255, 0x120A1C));
		m.push();
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 5));
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(t * 3));
		cube(vc, m.peek(), S * 0.5f, S * 0.5f, S * 0.5f, argb(100, 0x6A3AA8));
		m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
		cube(vc, m.peek(), S * 0.44f, S * 0.44f, S * 0.44f, argb(80, 0x4A2A6E));
		m.pop();
		int n = 3 + P / 2;
		for (int i = 0; i < n; i++) {
			float a = (float) (i * Math.PI * 2 / n);
			float[][] pts = new float[5][];
			for (int k = 0; k < 5; k++) {
				float f = k / 4f;
				float wob = MathHelper.sin(t * 0.35f + i * 1.7f + f * 3f) * S * 0.12f * f;
				float r = S * (0.15f + f * 0.2f);
				pts[k] = new float[]{MathHelper.cos(a) * r + wob, MathHelper.sin(a) * r - wob, -f * S * (0.8f + P * 0.03f)};
			}
			bolt(vc, m.peek(), pts, S * 0.07f, argb(170, 0x4A2A6E));
		}
	}

	static void blood(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		float beat = MathHelper.sin(t * 0.5f);
		float hb = 1f + 0.16f * (beat > 0 ? beat * beat * beat * beat : 0);
		cube(vc, m.peek(), S * 0.32f * hb, S * 0.32f * hb, S * 0.36f * hb, argb(250, 0xA3191B));
		box(vc, m.peek(), -S * 0.1f, -S * 0.1f, -S * 0.6f, S * 0.1f, S * 0.1f, -S * 0.12f, argb(230, 0x7A0A10));
		box(vc, m.peek(), -S * 0.05f, -S * 0.05f, -S * 0.85f, S * 0.05f, S * 0.05f, -S * 0.55f, argb(200, 0x5A060A));
		int n = 2 + P / 3;
		for (int i = 0; i < n; i++) {
			float a = t * 0.3f + (float) (i * Math.PI * 2 / n);
			m.push();
			m.translate(MathHelper.cos(a) * S * 0.36f, MathHelper.sin(a) * S * 0.36f, 0);
			cube(vc, m.peek(), S * 0.08f, S * 0.08f, S * 0.08f, argb(240, 0xD03030));
			m.pop();
		}
	}

	static void mana(MatrixStack m, VertexConsumer vc, float t, float S, int P) {
		m.push();
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 5));
		crystal(vc, m.peek(), S * 0.24f, S * 0.45f, S * 0.45f, argb(230, 0x8A7CFF));
		crystal(vc, m.peek(), S * 0.1f, S * 0.3f, S * 0.3f, argb(255, 0xE8E4FF));
		m.pop();
		int n = 2 + P / 4;
		for (int k = 0; k < n; k++) {
			m.push();
			m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(k * 180f / n + t * 2));
			m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(60));
			m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 6));
			ring(vc, m.peek(), S * (0.5f + k * 0.06f), S * 0.04f, 24, argb(170, 0xB8AEFF));
			m.pop();
		}
	}
}
