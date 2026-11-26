package ua.pletinnya.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.Ranks;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.magic.WeavingLaws;

/** Шкала мани, ранг і обране закляття у верхньому лівому куті. */
public final class ManaHud {
	private ManaHud() {}

	public static void render(DrawContext ctx, RenderTickCounter tick) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null || mc.options.hudHidden || mc.getDebugHud().shouldShowDebugHud()) return;
		MageData d = ClientMage.data;
		TextRenderer tr = mc.textRenderer;
		int x = 6, y = 6, w = 104;

		ctx.drawTextWithShadow(tr, Ranks.title(d.rank), x, y, 0xFFD27A);
		y += 11;

		float ratio = d.maxMana > 0 ? Math.min(1f, d.mana / d.maxMana) : 0f;
		ctx.fill(x - 1, y - 1, x + w + 1, y + 7, 0xAA000000);
		ctx.fill(x, y, x + (int) (w * ratio), y + 6, d.channelDamage > 0 ? 0xFF8A4FD8 : 0xFF5A6CFF);
		ctx.fill(x, y, x + (int) (w * ratio), y + 2, 0x66FFFFFF);
		ctx.drawTextWithShadow(tr, Text.literal((int) d.mana + " / " + d.maxMana), x + w + 5, y - 1, 0x9DB4FF);
		y += 11;

		SpellFormula f = d.selectedSpell();
		if (!f.isEmpty()) {
			int cost = (int) Math.ceil(WeavingLaws.cost(mc.player, d, f));
			int color = cost <= d.mana ? f.elementType().color : 0x888888;
			ctx.drawTextWithShadow(tr, Text.literal("[" + (d.selected + 1) + "] " + f.name() + "  ").append(Text.translatable("hud.pletinnya.cost", cost)), x, y, color);
		} else {
			ctx.drawTextWithShadow(tr, Text.literal("[" + (d.selected + 1) + "] —"), x, y, 0x888888);
		}
		y += 11;

		if (d.channelDamage > 0) {
			ctx.drawTextWithShadow(tr, Text.translatable("hud.pletinnya.channels", d.channelDamage / 20), x, y, 0xFF5555);
			y += 11;
		}
		if (d.aura) {
			ctx.drawTextWithShadow(tr, Text.translatable("hud.pletinnya.aura", d.auraTicks / 20), x, y, d.auraTicks > 1800 ? 0xFF7733 : 0xFFAA00);
		}
	}
}
