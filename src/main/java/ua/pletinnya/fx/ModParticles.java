package ua.pletinnya.fx;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.magic.Element;

/** Власні частинки Плетіння. */
public final class ModParticles {
	private ModParticles() {}

	public static final SimpleParticleType[] ELEMENT = new SimpleParticleType[Element.values().length];
	public static final ParticleType<FxEffects.Glyph> GLYPH = Registry.register(Registries.PARTICLE_TYPE, Pletinnya.id("glyph"),
		FabricParticleTypes.complex(true, FxEffects.Glyph.CODEC, FxEffects.Glyph.PACKET));
	public static final ParticleType<FxEffects.Mote> MOTE = Registry.register(Registries.PARTICLE_TYPE, Pletinnya.id("mote"),
		FabricParticleTypes.complex(true, FxEffects.Mote.CODEC, FxEffects.Mote.PACKET));
	public static final ParticleType<FxEffects.Ring> RING = Registry.register(Registries.PARTICLE_TYPE, Pletinnya.id("ring"),
		FabricParticleTypes.complex(true, FxEffects.Ring.CODEC, FxEffects.Ring.PACKET));

	/** Пласке магічне коло (лежить на землі). */
	public static final ParticleType<FxEffects.Circle> CIRCLE = Registry.register(Registries.PARTICLE_TYPE, Pletinnya.id("circle"),
		FabricParticleTypes.complex(true, FxEffects.Circle.CODEC, FxEffects.Circle.PACKET));
	/** Сигіл, що лежить на землі. */
	public static final ParticleType<FxEffects.FlatGlyph> FLAT_GLYPH = Registry.register(Registries.PARTICLE_TYPE, Pletinnya.id("flat_glyph"),
		FabricParticleTypes.complex(true, FxEffects.FlatGlyph.CODEC, FxEffects.FlatGlyph.PACKET));

	static {
		for (Element e : Element.values()) {
			ELEMENT[e.ordinal()] = Registry.register(Registries.PARTICLE_TYPE, Pletinnya.id("fx_" + e.id), FabricParticleTypes.simple(true));
		}
	}

	public static SimpleParticleType of(Element e) {
		return ELEMENT[e.ordinal()];
	}

	public static void init() {}
}
