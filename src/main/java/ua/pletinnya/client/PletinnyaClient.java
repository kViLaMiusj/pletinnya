package ua.pletinnya.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.network.ModNetworking;
import ua.pletinnya.registry.ModBlocks;
import ua.pletinnya.registry.ModEntities;

public class PletinnyaClient implements ClientModInitializer {
	private static final String CATEGORY = "category.pletinnya";
	public static KeyBinding CAST, NEXT, PREV, AURA, CANCEL, TABLET, DRAGON;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.WEAVE_PROJECTILE, ua.pletinnya.client.render.WeaveProjectileRenderer::new);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANA_BARRIER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANA_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANA_CRYSTAL_CLUSTER, RenderLayer.getCutout());
		ua.pletinnya.client.fx.FxFactories.register();
		ua.pletinnya.client.armor.MageArmorRenderer.registerAll();
		ClientPlayNetworking.registerGlobalReceiver(ua.pletinnya.fx.Vfx.Payload.ID, (payload, ctx) -> {
			if (ctx.client().world != null) ua.pletinnya.client.fx.ClientVfx.play(ctx.client().world, payload);
		});

		CAST = key("cast", GLFW.GLFW_KEY_R);
		NEXT = key("next_spell", GLFW.GLFW_KEY_G);
		PREV = key("prev_spell", GLFW.GLFW_KEY_H);
		AURA = key("aura", GLFW.GLFW_KEY_V);
		CANCEL = key("cancel_weaves", GLFW.GLFW_KEY_N);
		TABLET = key("tablet", GLFW.GLFW_KEY_K);
		DRAGON = key("dragon", GLFW.GLFW_KEY_B);

		ClientPlayNetworking.registerGlobalReceiver(ModNetworking.SyncMagePayload.ID, (payload, ctx) ->
			ClientMage.data = MageData.fromNbt(payload.nbt()));
		ClientPlayNetworking.registerGlobalReceiver(ModNetworking.OpenTabletPayload.ID, (payload, ctx) ->
			ctx.client().setScreen(new WeavingScreen()));

		HudRenderCallback.EVENT.register(ManaHud::render);
		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
	}

	private static KeyBinding key(String name, int code) {
		return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.pletinnya." + name, InputUtil.Type.KEYSYM, code, CATEGORY));
	}

	private void onTick(MinecraftClient client) {
		if (client.player == null) return;
		while (CAST.wasPressed()) ClientPlayNetworking.send(ModNetworking.CastPayload.INSTANCE);
		while (NEXT.wasPressed()) select(client, 1);
		while (PREV.wasPressed()) select(client, -1);
		while (AURA.wasPressed()) ClientPlayNetworking.send(ModNetworking.ToggleAuraPayload.INSTANCE);
		while (CANCEL.wasPressed()) ClientPlayNetworking.send(ModNetworking.CancelWeavesPayload.INSTANCE);
		while (TABLET.wasPressed()) client.setScreen(new WeavingScreen());
		while (DRAGON.wasPressed()) client.setScreen(new DragonVoiceScreen());
		companion(client);
	}

	/** Дух-супутник кружляє довкола того, хто уклав угоду. */
	private static void companion(MinecraftClient client) {
		MageData d = ClientMage.data;
		if (d.paths.spirit <= 0 || client.world == null || client.player == null || client.isPaused()) return;
		long t = client.world.getTime();
		double a = t * 0.12;
		var p = client.player;
		int color = ua.pletinnya.magic.Element.byIndex(d.paths.spirit - 1).color;
		double x = p.getX() + Math.cos(a) * 0.9, y = p.getY() + 1.6 + Math.sin(t * 0.07) * 0.25, z = p.getZ() + Math.sin(a) * 0.9;
		client.world.addParticle(new ua.pletinnya.fx.FxEffects.Mote(0xCFFFFA, 0.16f, 8), x, y, z, 0, 0, 0);
		if (t % 3 == 0) client.world.addParticle(new ua.pletinnya.fx.FxEffects.Mote(color, 0.09f, 18), x, y, z, 0, 0.005, 0);
	}

	private static void select(MinecraftClient client, int dir) {
		MageData d = ClientMage.data;
		int slot = Math.floorMod(d.selected + dir, MageData.SPELL_SLOTS);
		d.selected = slot;
		ClientPlayNetworking.send(new ModNetworking.SelectSpellPayload(slot));
		String name = d.spells.get(slot).isEmpty() ? "—" : d.spells.get(slot).name();
		client.player.sendMessage(Text.translatable("message.pletinnya.selected", slot + 1, name).formatted(Formatting.AQUA), true);
	}
}
