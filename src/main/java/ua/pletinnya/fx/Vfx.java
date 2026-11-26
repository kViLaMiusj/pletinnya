package ua.pletinnya.fx;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import ua.pletinnya.Pletinnya;

/** Сервер повідомляє клієнтам, який візерунок намалювати й де. */
public final class Vfx {
	private Vfx() {}

	public static final int CAST = 0, IMPACT = 1, SELF = 2, WALL = 3, RUNE = 4, RUNE_FIRE = 5, DISPEL = 6, MISFIRE = 7,
		PRAYER = 8, DRAGON = 9, SPIRIT = 10, AURA_STRIKE = 11, RANK_UP = 12, OVERLOAD = 13, PACT = 14, HEAL = 15;

	public record Payload(int kind, int element, double x, double y, double z, float dx, float dy, float dz, float size) implements CustomPayload {
		public static final Id<Payload> ID = new Id<>(Pletinnya.id("vfx"));
		public static final PacketCodec<RegistryByteBuf, Payload> CODEC = PacketCodec.of((p, buf) -> {
			buf.writeVarInt(p.kind); buf.writeVarInt(p.element);
			buf.writeDouble(p.x); buf.writeDouble(p.y); buf.writeDouble(p.z);
			buf.writeFloat(p.dx); buf.writeFloat(p.dy); buf.writeFloat(p.dz); buf.writeFloat(p.size);
		}, buf -> new Payload(buf.readVarInt(), buf.readVarInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(),
			buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public static void send(ServerWorld w, int kind, int element, Vec3d pos, float size) {
		send(w, kind, element, pos, Vec3d.ZERO, size);
	}

	public static void send(ServerWorld w, int kind, int element, Vec3d pos, Vec3d dir, float size) {
		Payload p = new Payload(kind, element, pos.x, pos.y, pos.z, (float) dir.x, (float) dir.y, (float) dir.z, size);
		for (ServerPlayerEntity pl : PlayerLookup.around(w, pos, 96)) ServerPlayNetworking.send(pl, p);
	}
}
