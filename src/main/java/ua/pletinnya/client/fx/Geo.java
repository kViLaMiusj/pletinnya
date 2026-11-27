package ua.pletinnya.client.fx;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ua.pletinnya.Pletinnya;

/** Прості об'ємні примітиви для 3D-образів заклять: бруски, кристали, кільця, стрічки, сяйво. Усе двобічне й світне. */
public final class Geo {
	private Geo() {}

	public static final Identifier WHITE = Pletinnya.id("textures/misc/fx_white.png");
	public static final Identifier GLOW = Pletinnya.id("textures/misc/fx_glow.png");

	public static RenderLayer solid() { return RenderLayer.getEntityTranslucentEmissive(WHITE); }
	public static RenderLayer glow() { return RenderLayer.getEntityTranslucentEmissive(GLOW); }

	// ------------------------------------------------------------------ кольори
	public static int argb(int a, int rgb) { return (MathHelper.clamp(a, 0, 255) << 24) | (rgb & 0xFFFFFF); }

	public static int shade(int argb, float f) {
		int r = MathHelper.clamp((int) (((argb >> 16) & 255) * f), 0, 255);
		int g = MathHelper.clamp((int) (((argb >> 8) & 255) * f), 0, 255);
		int b = MathHelper.clamp((int) ((argb & 255) * f), 0, 255);
		return (argb & 0xFF000000) | (r << 16) | (g << 8) | b;
	}

	public static int mix(int a, int b, float t) {
		t = MathHelper.clamp(t, 0f, 1f);
		int r = (int) MathHelper.lerp(t, (a >> 16) & 255, (b >> 16) & 255);
		int g = (int) MathHelper.lerp(t, (a >> 8) & 255, (b >> 8) & 255);
		int bl = (int) MathHelper.lerp(t, a & 255, b & 255);
		return (a & 0xFF000000) | (r << 16) | (g << 8) | bl;
	}

	// ------------------------------------------------------------------ вершини
	private static void v(VertexConsumer vc, MatrixStack.Entry e, float x, float y, float z, float u, float w, int argb) {
		vc.vertex(e, x, y, z).color(argb).texture(u, w).overlay(OverlayTexture.DEFAULT_UV)
			.light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(e, 0f, 1f, 0f);
	}

	/** Чотирикутник (двобічний). */
	public static void quad(VertexConsumer vc, MatrixStack.Entry e, float[] a, float[] b, float[] c, float[] d, int argb) {
		v(vc, e, a[0], a[1], a[2], 0, 1, argb); v(vc, e, b[0], b[1], b[2], 1, 1, argb);
		v(vc, e, c[0], c[1], c[2], 1, 0, argb); v(vc, e, d[0], d[1], d[2], 0, 0, argb);
		v(vc, e, d[0], d[1], d[2], 0, 0, argb); v(vc, e, c[0], c[1], c[2], 1, 0, argb);
		v(vc, e, b[0], b[1], b[2], 1, 1, argb); v(vc, e, a[0], a[1], a[2], 0, 1, argb);
	}

	private static float[] p(float x, float y, float z) { return new float[]{x, y, z}; }

	/** Пласкій квадрат у площині XY (для сяйва, повернутого до камери). */
	public static void flat(VertexConsumer vc, MatrixStack.Entry e, float s, int argb) {
		float h = s / 2;
		quad(vc, e, p(-h, -h, 0), p(h, -h, 0), p(h, h, 0), p(-h, h, 0), argb);
	}

	/** Брусок із легким об'ємним затіненням граней. */
	public static void box(VertexConsumer vc, MatrixStack.Entry e, float x0, float y0, float z0, float x1, float y1, float z1, int argb) {
		int top = argb, side = shade(argb, 0.82f), side2 = shade(argb, 0.7f), bot = shade(argb, 0.58f);
		quad(vc, e, p(x0, y1, z0), p(x1, y1, z0), p(x1, y1, z1), p(x0, y1, z1), top);
		quad(vc, e, p(x0, y0, z1), p(x1, y0, z1), p(x1, y0, z0), p(x0, y0, z0), bot);
		quad(vc, e, p(x0, y0, z0), p(x1, y0, z0), p(x1, y1, z0), p(x0, y1, z0), side);
		quad(vc, e, p(x1, y0, z1), p(x0, y0, z1), p(x0, y1, z1), p(x1, y1, z1), side);
		quad(vc, e, p(x0, y0, z1), p(x0, y0, z0), p(x0, y1, z0), p(x0, y1, z1), side2);
		quad(vc, e, p(x1, y0, z0), p(x1, y0, z1), p(x1, y1, z1), p(x1, y1, z0), side2);
	}

	/** Брусок із центром у (0,0,0). */
	public static void cube(VertexConsumer vc, MatrixStack.Entry e, float sx, float sy, float sz, int argb) {
		box(vc, e, -sx / 2, -sy / 2, -sz / 2, sx / 2, sy / 2, sz / 2, argb);
	}

	/** Кристал-восьмигранник уздовж осі Y: r — півширина, up/down — висота вершин. */
	public static void crystal(VertexConsumer vc, MatrixStack.Entry e, float r, float up, float down, int argb) {
		float[][] ring = {p(r, 0, 0), p(0, 0, r), p(-r, 0, 0), p(0, 0, -r)};
		float[] t = p(0, up, 0), b = p(0, -down, 0);
		for (int i = 0; i < 4; i++) {
			float[] a = ring[i], c = ring[(i + 1) % 4];
			float f = (i % 2 == 0) ? 1f : 0.78f;
			quad(vc, e, a, c, t, t, shade(argb, f));
			quad(vc, e, c, a, b, b, shade(argb, f * 0.65f));
		}
	}

	/** Пласке кільце в площині XZ. */
	public static void ring(VertexConsumer vc, MatrixStack.Entry e, float r, float w, int segs, int argb) {
		float r0 = Math.max(0, r - w / 2), r1 = r + w / 2;
		for (int i = 0; i < segs; i++) {
			float a0 = (float) (i * Math.PI * 2 / segs), a1 = (float) ((i + 1) * Math.PI * 2 / segs);
			float c0 = MathHelper.cos(a0), s0 = MathHelper.sin(a0), c1 = MathHelper.cos(a1), s1 = MathHelper.sin(a1);
			quad(vc, e, p(c0 * r0, 0, s0 * r0), p(c1 * r0, 0, s1 * r0), p(c1 * r1, 0, s1 * r1), p(c0 * r1, 0, s0 * r1), argb);
		}
	}

	/** Вертикальна стрічка-циліндр (стінка) у площині XZ, висотою h, з прозорішим верхом. */
	public static void band(VertexConsumer vc, MatrixStack.Entry e, float r, float h, int segs, int argb, int argbTop) {
		for (int i = 0; i < segs; i++) {
			float a0 = (float) (i * Math.PI * 2 / segs), a1 = (float) ((i + 1) * Math.PI * 2 / segs);
			float c0 = MathHelper.cos(a0) * r, s0 = MathHelper.sin(a0) * r, c1 = MathHelper.cos(a1) * r, s1 = MathHelper.sin(a1) * r;
			vBand(vc, e, c0, s0, c1, s1, h, argb, argbTop);
		}
	}

	private static void vBand(VertexConsumer vc, MatrixStack.Entry e, float x0, float z0, float x1, float z1, float h, int lo, int hi) {
		v(vc, e, x0, 0, z0, 0, 1, lo); v(vc, e, x1, 0, z1, 1, 1, lo); v(vc, e, x1, h, z1, 1, 0, hi); v(vc, e, x0, h, z0, 0, 0, hi);
		v(vc, e, x0, h, z0, 0, 0, hi); v(vc, e, x1, h, z1, 1, 0, hi); v(vc, e, x1, 0, z1, 1, 1, lo); v(vc, e, x0, 0, z0, 0, 1, lo);
	}

	/** Ламана блискавка між двома точками: тонкі бруски між вузлами. */
	public static void bolt(VertexConsumer vc, MatrixStack.Entry e, float[][] pts, float w, int argb) {
		for (int i = 0; i + 1 < pts.length; i++) segment(vc, e, pts[i], pts[i + 1], w, argb);
	}

	/** Тонкий брусок від точки a до точки b (два перехресні пласкі промені — видно з будь-якого боку). */
	public static void segment(VertexConsumer vc, MatrixStack.Entry e, float[] a, float[] b, float w, int argb) {
		float dx = b[0] - a[0], dy = b[1] - a[1], dz = b[2] - a[2];
		float len = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
		if (len < 1e-4f) return;
		// дві перпендикулярні осі
		float ux, uy, uz;
		if (Math.abs(dy) < len * 0.9f) { ux = -dz; uy = 0; uz = dx; } else { ux = 0; uy = -dz; uz = dy; }
		float ul = MathHelper.sqrt(ux * ux + uy * uy + uz * uz);
		ux = ux / ul * w / 2; uy = uy / ul * w / 2; uz = uz / ul * w / 2;
		float vx = (dy * uz - dz * uy) / len, vy = (dz * ux - dx * uz) / len, vz = (dx * uy - dy * ux) / len;
		quad(vc, e, p(a[0] - ux, a[1] - uy, a[2] - uz), p(a[0] + ux, a[1] + uy, a[2] + uz), p(b[0] + ux, b[1] + uy, b[2] + uz), p(b[0] - ux, b[1] - uy, b[2] - uz), argb);
		quad(vc, e, p(a[0] - vx, a[1] - vy, a[2] - vz), p(a[0] + vx, a[1] + vy, a[2] + vz), p(b[0] + vx, b[1] + vy, b[2] + vz), p(b[0] - vx, b[1] - vy, b[2] - vz), argb);
	}

	/** Псевдовипадкове число 0..1 від цілого — однакове на всіх клієнтах. */
	public static float hash(int n) {
		n = (n << 13) ^ n;
		return ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / (float) 0x7fffffff;
	}
}
