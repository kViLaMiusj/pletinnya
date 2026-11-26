package ua.pletinnya.client.fx;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/** Світна частинка Плетіння: власні спрайти, плавне згасання, обертання, без впливу освітлення. */
public class FxParticle extends SpriteBillboardParticle {
	private final SpriteProvider sprites;
	private final boolean animate;
	private final float startScale;
	private final float endScale;
	private final float spin;
	private boolean flat = false;

	public FxParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider sprites,
					  int life, float scale, float endScale, float gravity, float friction, float spin, boolean animate, int color) {
		super(world, x, y, z);
		this.velocityX = vx;
		this.velocityY = vy;
		this.velocityZ = vz;
		this.sprites = sprites;
		this.animate = animate;
		this.maxAge = Math.max(2, life + random.nextInt(Math.max(1, life / 4)));
		this.startScale = scale;
		this.endScale = endScale;
		this.scale = scale;
		this.gravityStrength = gravity;
		this.velocityMultiplier = friction;
		this.spin = spin;
		this.angle = random.nextFloat() * 6.28f;
		this.prevAngle = angle;
		this.collidesWithWorld = false;
		if (color != 0xFFFFFF) {
			this.setColor(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f);
		}
		if (animate) setSpriteForAge(sprites);
		else setSprite(sprites.getSprite(world.random));
	}

	/** Лежить на землі, а не повертається до камери (магічні кола, ударні хвилі). */
	public FxParticle flat() {
		this.flat = true;
		return this;
	}

	@Override
	public void buildGeometry(VertexConsumer vc, Camera camera, float tickDelta) {
		if (!flat) {
			super.buildGeometry(vc, camera, tickDelta);
			return;
		}
		Vec3d cam = camera.getPos();
		float x = (float) (MathHelper.lerp(tickDelta, prevPosX, this.x) - cam.x);
		float y = (float) (MathHelper.lerp(tickDelta, prevPosY, this.y) - cam.y);
		float z = (float) (MathHelper.lerp(tickDelta, prevPosZ, this.z) - cam.z);
		float s = getSize(tickDelta);
		float a = MathHelper.lerp(tickDelta, prevAngle, angle);
		float c = MathHelper.cos(a) * s, sn = MathHelper.sin(a) * s;
		float u0 = getMinU(), u1 = getMaxU(), v0 = getMinV(), v1 = getMaxV();
		int light = getBrightness(tickDelta);
		float[][] p = {{-c + sn, -sn - c}, {c + sn, sn - c}, {c - sn, sn + c}, {-c - sn, -sn + c}};
		float[][] uv = {{u0, v1}, {u1, v1}, {u1, v0}, {u0, v0}};
		for (int i = 0; i < 4; i++) vc.vertex(x + p[i][0], y, z + p[i][1]).texture(uv[i][0], uv[i][1]).color(red, green, blue, alpha).light(light);
		for (int i = 3; i >= 0; i--) vc.vertex(x + p[i][0], y, z + p[i][1]).texture(uv[i][0], uv[i][1]).color(red, green, blue, alpha).light(light);
	}

	public FxParticle withSprite(int index, int count) {
		setSprite(sprites.getSprite(index, count - 1));
		return this;
	}

	@Override
	public void tick() {
		super.tick();
		if (animate) setSpriteForAge(sprites);
		float t = (float) age / maxAge;
		this.scale = startScale + (endScale - startScale) * t;
		this.alpha = t < 0.7f ? 1f : Math.max(0f, 1f - (t - 0.7f) / 0.3f);
		this.prevAngle = angle;
		this.angle += spin;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public int getBrightness(float tint) {
		return 0xF000F0;
	}
}
