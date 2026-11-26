package ua.pletinnya.client.fx;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import ua.pletinnya.fx.ModParticles;
import ua.pletinnya.magic.Element;

/** Поведінка частинок кожного напряму. */
public final class FxFactories {
	private FxFactories() {}

	public static void register() {
		ParticleFactoryRegistry reg = ParticleFactoryRegistry.getInstance();
		for (Element e : Element.values()) {
			final Element el = e;
			reg.register(ModParticles.of(e), sprites -> (params, world, x, y, z, vx, vy, vz) -> switch (el) {
				case FIRE -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 16, 0.22f, 0.04f, -0.02f, 0.92f, 0.05f, true, 0xFFFFFF);
				case WATER -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 22, 0.16f, 0.1f, 0.06f, 0.96f, 0f, true, 0xFFFFFF);
				case WIND -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 18, 0.2f, 0.3f, 0f, 0.9f, 0.2f, true, 0xFFFFFF);
				case EARTH -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 26, 0.16f, 0.12f, 0.08f, 0.96f, 0.15f, true, 0xFFFFFF);
				case LIGHTNING -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 5, 0.2f, 0.12f, 0f, 0.7f, 0f, true, 0xFFFFFF);
				case ICE -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 30, 0.14f, 0.06f, 0.01f, 0.94f, 0.08f, true, 0xFFFFFF);
				case NATURE -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 40, 0.15f, 0.12f, 0.01f, 0.95f, 0.12f, true, 0xFFFFFF);
				case SOUND -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 24, 0.18f, 0.12f, -0.005f, 0.95f, 0f, true, 0xFFFFFF);
				case LIGHT -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 14, 0.24f, 0.02f, 0f, 0.9f, 0.1f, true, 0xFFFFFF);
				case DARKNESS -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 26, 0.25f, 0.4f, -0.005f, 0.93f, 0.03f, true, 0xFFFFFF);
				case BLOOD -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 24, 0.13f, 0.1f, 0.07f, 0.97f, 0f, true, 0xFFFFFF);
				case MANA -> new FxParticle(world, x, y, z, vx, vy, vz, sprites, 22, 0.16f, 0.04f, 0f, 0.92f, 0.1f, true, 0xFFFFFF);
			});
		}
		reg.register(ModParticles.GLYPH, sprites -> (p, world, x, y, z, vx, vy, vz) ->
			new FxParticle(world, x, y, z, vx, vy, vz, sprites, 28, p.size(), p.size() * 0.85f, 0f, 0.9f, 0.02f, false, p.color()).withSprite(p.index(), 16));
		reg.register(ModParticles.FLAT_GLYPH, sprites -> (p, world, x, y, z, vx, vy, vz) ->
			new FxParticle(world, x, y, z, 0, 0, 0, sprites, 34, p.size() * 0.4f, p.size() * 0.5f, 0f, 1f, -0.03f, false, p.color()).withSprite(p.index(), 16).flat());
		reg.register(ModParticles.MOTE, sprites -> (p, world, x, y, z, vx, vy, vz) ->
			new FxParticle(world, x, y, z, vx, vy, vz, sprites, p.life(), p.size(), p.size() * 0.2f, 0f, 0.94f, 0f, false, p.color()));
		reg.register(ModParticles.RING, sprites -> (p, world, x, y, z, vx, vy, vz) ->
			new FxParticle(world, x, y, z, vx, vy, vz, sprites, 14, p.size() * 0.15f, p.size() * 0.5f, 0f, 0.9f, 0f, false, p.color()).flat());
		reg.register(ModParticles.CIRCLE, sprites -> (p, world, x, y, z, vx, vy, vz) ->
			new FxParticle(world, x, y, z, 0, 0, 0, sprites, 34, p.size() * 0.42f, p.size() * 0.5f, 0f, 1f, 0.045f, false, p.color()).flat());
	}
}
