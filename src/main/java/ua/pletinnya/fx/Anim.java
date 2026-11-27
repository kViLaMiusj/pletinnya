package ua.pletinnya.fx;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import ua.pletinnya.Pletinnya;

/** Сервер просить клієнтів програти анімацію тіла мага (випад посохом, здіймання, молитва...). */
public final class Anim {
	private Anim() {}

	public static final int THRUST = 0, RAISE = 1, SELF = 2, PRAY = 3, ROAR = 4, RECOIL = 5;

	public record Payload(int entityId, int kind, float power) implements CustomPayload {
		public static final Id<Payload> ID = new Id<>(Pletinnya.id("anim"));
		public static final PacketCodec<RegistryByteBuf, Payload> CODEC = PacketCodec.of(
			(p, buf) -> { buf.writeVarInt(p.entityId); buf.writeVarInt(p.kind); buf.writeFloat(p.power); },
			buf -> new Payload(buf.readVarInt(), buf.readVarInt(), buf.readFloat()));
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public static void play(ServerPlayerEntity p, int kind, float power) {
		Payload pl = new Payload(p.getId(), kind, power);
		ServerPlayNetworking.send(p, pl);
		for (ServerPlayerEntity o : PlayerLookup.tracking(p)) if (o != p) ServerPlayNetworking.send(o, pl);
	}
}
