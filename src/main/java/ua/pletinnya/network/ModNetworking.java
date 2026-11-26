package ua.pletinnya.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.SpellCaster;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.magic.WeaveManager;
import ua.pletinnya.magic.WeavingLaws;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.registry.ModItems;

public final class ModNetworking {
	private ModNetworking() {}

	public static final PacketCodec<ByteBuf, SpellFormula> FORMULA_CODEC = PacketCodecs.codec(SpellFormula.CODEC);

	// ---------------- сервер → клієнт ----------------

	public record SyncMagePayload(NbtCompound nbt) implements CustomPayload {
		public static final Id<SyncMagePayload> ID = new Id<>(Pletinnya.id("sync_mage"));
		public static final PacketCodec<RegistryByteBuf, SyncMagePayload> CODEC =
			PacketCodec.tuple(PacketCodecs.UNLIMITED_NBT_COMPOUND, SyncMagePayload::nbt, SyncMagePayload::new);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public record OpenTabletPayload() implements CustomPayload {
		public static final OpenTabletPayload INSTANCE = new OpenTabletPayload();
		public static final Id<OpenTabletPayload> ID = new Id<>(Pletinnya.id("open_tablet"));
		public static final PacketCodec<RegistryByteBuf, OpenTabletPayload> CODEC = PacketCodec.unit(INSTANCE);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	// ---------------- клієнт → сервер ----------------

	public record CastPayload() implements CustomPayload {
		public static final CastPayload INSTANCE = new CastPayload();
		public static final Id<CastPayload> ID = new Id<>(Pletinnya.id("cast"));
		public static final PacketCodec<RegistryByteBuf, CastPayload> CODEC = PacketCodec.unit(INSTANCE);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public record SelectSpellPayload(int slot) implements CustomPayload {
		public static final Id<SelectSpellPayload> ID = new Id<>(Pletinnya.id("select_spell"));
		public static final PacketCodec<RegistryByteBuf, SelectSpellPayload> CODEC =
			PacketCodec.tuple(PacketCodecs.VAR_INT, SelectSpellPayload::slot, SelectSpellPayload::new);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public record SaveSpellPayload(int slot, SpellFormula formula) implements CustomPayload {
		public static final Id<SaveSpellPayload> ID = new Id<>(Pletinnya.id("save_spell"));
		public static final PacketCodec<RegistryByteBuf, SaveSpellPayload> CODEC =
			PacketCodec.tuple(PacketCodecs.VAR_INT, SaveSpellPayload::slot, FORMULA_CODEC, SaveSpellPayload::formula, SaveSpellPayload::new);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public record ToggleAuraPayload() implements CustomPayload {
		public static final ToggleAuraPayload INSTANCE = new ToggleAuraPayload();
		public static final Id<ToggleAuraPayload> ID = new Id<>(Pletinnya.id("toggle_aura"));
		public static final PacketCodec<RegistryByteBuf, ToggleAuraPayload> CODEC = PacketCodec.unit(INSTANCE);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public record CancelWeavesPayload() implements CustomPayload {
		public static final CancelWeavesPayload INSTANCE = new CancelWeavesPayload();
		public static final Id<CancelWeavesPayload> ID = new Id<>(Pletinnya.id("cancel_weaves"));
		public static final PacketCodec<RegistryByteBuf, CancelWeavesPayload> CODEC = PacketCodec.unit(INSTANCE);
		@Override public Id<? extends CustomPayload> getId() { return ID; }
	}

	public static void init() {
		PayloadTypeRegistry.playS2C().register(SyncMagePayload.ID, SyncMagePayload.CODEC);
		PayloadTypeRegistry.playS2C().register(OpenTabletPayload.ID, OpenTabletPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(CastPayload.ID, CastPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SelectSpellPayload.ID, SelectSpellPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SaveSpellPayload.ID, SaveSpellPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ToggleAuraPayload.ID, ToggleAuraPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(CancelWeavesPayload.ID, CancelWeavesPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(CastPayload.ID, (payload, ctx) -> SpellCaster.castSelected(ctx.player()));

		ServerPlayNetworking.registerGlobalReceiver(SelectSpellPayload.ID, (payload, ctx) -> {
			MageData d = ctx.player().getAttachedOrCreate(ModAttachments.MAGE);
			d.selected = Math.floorMod(payload.slot(), MageData.SPELL_SLOTS);
			d.markDirty();
		});

		ServerPlayNetworking.registerGlobalReceiver(SaveSpellPayload.ID, (payload, ctx) -> saveSpell(ctx.player(), payload.slot(), payload.formula()));

		ServerPlayNetworking.registerGlobalReceiver(ToggleAuraPayload.ID, (payload, ctx) -> {
			ServerPlayerEntity p = ctx.player();
			MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
			if (!d.aura && d.mana < 5) {
				p.sendMessage(Text.translatable("message.pletinnya.aura_no_mana").formatted(Formatting.BLUE), true);
				return;
			}
			d.aura = !d.aura;
			d.markDirty();
			p.sendMessage(Text.translatable(d.aura ? "message.pletinnya.aura_on" : "message.pletinnya.aura_off").formatted(Formatting.GOLD), true);
		});

		ServerPlayNetworking.registerGlobalReceiver(CancelWeavesPayload.ID, (payload, ctx) -> {
			ServerPlayerEntity p = ctx.player();
			MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
			if (d.rank < 2) {
				p.sendMessage(Text.translatable("message.pletinnya.cancel_rank").formatted(Formatting.GRAY), true);
				return;
			}
			int n = WeaveManager.cancelOwn(p);
			p.sendMessage(Text.translatable("message.pletinnya.cancelled", n).formatted(Formatting.AQUA), true);
		});
	}

	private static void saveSpell(ServerPlayerEntity p, int slot, SpellFormula raw) {
		if (slot < 0 || slot >= MageData.SPELL_SLOTS) return;
		MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
		SpellFormula f = raw.sanitized();
		if (f.name().isBlank()) {
			d.spells.set(slot, SpellFormula.empty());
			d.markDirty();
			p.sendMessage(Text.translatable("message.pletinnya.erased", slot + 1).formatted(Formatting.GRAY), true);
			return;
		}
		if (!p.isCreative() && !p.getInventory().contains(s -> s.isOf(ModItems.WEAVING_TABLET))) {
			p.sendMessage(Text.translatable("message.pletinnya.need_tablet").formatted(Formatting.RED), true);
			return;
		}
		Text err = WeavingLaws.weaveError(d, f);
		if (err != null) {
			p.sendMessage(err.copy().formatted(Formatting.RED), true);
			return;
		}
		d.spells.set(slot, f);
		d.markDirty();
		p.sendMessage(Text.translatable("message.pletinnya.woven", f.name(), slot + 1).formatted(Formatting.AQUA), true);
	}

	public static void sync(ServerPlayerEntity p, MageData d) {
		ServerPlayNetworking.send(p, new SyncMagePayload(d.toNbt()));
	}
}
