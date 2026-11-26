package ua.pletinnya.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import ua.pletinnya.Pletinnya;

/** Допоміжне малювання для інтерфейсів Плетіння. */
public final class GuiDraw {
	private GuiDraw() {}

	public static final Identifier SIGILS = Pletinnya.id("textures/gui/sigils.png");
	public static final Identifier PANEL = Pletinnya.id("textures/gui/tablet.png");

	public static void sigil(DrawContext ctx, int index, int x, int y, int color) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f, 1f);
		ctx.drawTexture(SIGILS, x, y, index * 16, 0, 16, 16, 256, 16);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}

	public static void line(DrawContext ctx, int x0, int y0, int x1, int y1, int color, int thick) {
		int steps = Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0));
		for (int i = 0; i <= steps; i++) {
			int x = x0 + (x1 - x0) * i / Math.max(1, steps);
			int y = y0 + (y1 - y0) * i / Math.max(1, steps);
			ctx.fill(x, y, x + thick, y + thick, color);
		}
	}

	public static void node(DrawContext ctx, int cx, int cy, int r, int color) {
		for (int y = -r; y <= r; y++) {
			int w = (int) Math.sqrt(r * r - y * y);
			ctx.fill(cx - w, cy + y, cx + w + 1, cy + y + 1, color);
		}
	}

	public static void bar(DrawContext ctx, int x, int y, int w, int h, float ratio, int color) {
		ctx.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xAA000000);
		ctx.fill(x, y, x + (int) (w * Math.max(0, Math.min(1, ratio))), y + h, color);
		ctx.fill(x, y, x + (int) (w * Math.max(0, Math.min(1, ratio))), y + 1, 0x55FFFFFF);
	}
}
