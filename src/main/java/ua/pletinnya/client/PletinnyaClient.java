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
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
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
	public static KeyBinding CAST, NEXT, PREV, AURA, CANCEL, TABLET;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.WEAVE_PROJECTILE, FlyingItemEntityRenderer::new);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANA_BARRIER, RenderLayer.getTranslucent());

		CAST = key("cast", GLFW.GLFW_KEY_R);
		NEXT = key("next_spell", GLFW.GLFW_KEY_G);
		PREV = key("prev_spell", GLFW.GLFW_KEY_H);
		AURA = key("aura", GLFW.GLFW_KEY_V);
		CANCEL = key("cancel_weaves", GLFW.GLFW_KEY_N);
		TABLET = key("tablet", GLFW.GLFW_KEY_K);

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
