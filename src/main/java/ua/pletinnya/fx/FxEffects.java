package ua.pletinnya.fx;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

/** Параметризовані частинки: колір, розмір і (для гліфа) номер сигіла. */
public final class FxEffects {
	private FxEffects() {}

	/** Сигіл напряму або шляху. */
	public record Glyph(int index, int color, float size) implements ParticleEffect {
		public static final MapCodec<Glyph> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("index").forGetter(Glyph::index),
			Codec.INT.fieldOf("color").forGetter(Glyph::color),
			Codec.FLOAT.fieldOf("size").forGetter(Glyph::size)).apply(i, Glyph::new));
		public static final PacketCodec<RegistryByteBuf, Glyph> PACKET = PacketCodec.tuple(
			PacketCodecs.VAR_INT, Glyph::index, PacketCodecs.INTEGER, Glyph::color, PacketCodecs.FLOAT, Glyph::size, Glyph::new);
		@Override public ParticleType<?> getType() { return ModParticles.GLYPH; }
	}

	/** М'яка світна цятка. */
	public record Mote(int color, float size, int life) implements ParticleEffect {
		public static final MapCodec<Mote> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("color").forGetter(Mote::color),
			Codec.FLOAT.fieldOf("size").forGetter(Mote::size),
			Codec.INT.fieldOf("life").forGetter(Mote::life)).apply(i, Mote::new));
		public static final PacketCodec<RegistryByteBuf, Mote> PACKET = PacketCodec.tuple(
			PacketCodecs.INTEGER, Mote::color, PacketCodecs.FLOAT, Mote::size, PacketCodecs.VAR_INT, Mote::life, Mote::new);
		@Override public ParticleType<?> getType() { return ModParticles.MOTE; }
	}

	/** Пласке магічне коло на землі. */
	public record Circle(int color, float size) implements ParticleEffect {
		public static final MapCodec<Circle> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("color").forGetter(Circle::color),
			Codec.FLOAT.fieldOf("size").forGetter(Circle::size)).apply(i, Circle::new));
		public static final PacketCodec<RegistryByteBuf, Circle> PACKET = PacketCodec.tuple(
			PacketCodecs.INTEGER, Circle::color, PacketCodecs.FLOAT, Circle::size, Circle::new);
		@Override public ParticleType<?> getType() { return ModParticles.CIRCLE; }
	}

	/** Сигіл, що лежить на землі. */
	public record FlatGlyph(int index, int color, float size) implements ParticleEffect {
		public static final MapCodec<FlatGlyph> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("index").forGetter(FlatGlyph::index),
			Codec.INT.fieldOf("color").forGetter(FlatGlyph::color),
			Codec.FLOAT.fieldOf("size").forGetter(FlatGlyph::size)).apply(i, FlatGlyph::new));
		public static final PacketCodec<RegistryByteBuf, FlatGlyph> PACKET = PacketCodec.tuple(
			PacketCodecs.VAR_INT, FlatGlyph::index, PacketCodecs.INTEGER, FlatGlyph::color, PacketCodecs.FLOAT, FlatGlyph::size, FlatGlyph::new);
		@Override public ParticleType<?> getType() { return ModParticles.FLAT_GLYPH; }
	}

	/** Кільце, що розширюється. */
	public record Ring(int color, float size) implements ParticleEffect {
		public static final MapCodec<Ring> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("color").forGetter(Ring::color),
			Codec.FLOAT.fieldOf("size").forGetter(Ring::size)).apply(i, Ring::new));
		public static final PacketCodec<RegistryByteBuf, Ring> PACKET = PacketCodec.tuple(
			PacketCodecs.INTEGER, Ring::color, PacketCodecs.FLOAT, Ring::size, Ring::new);
		@Override public ParticleType<?> getType() { return ModParticles.RING; }
	}
}
