package ua.pletinnya.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import ua.pletinnya.magic.DragonTongue;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.network.ModNetworking;

/** Голос дракона: маг вимовляє закляття словами первісного Плетіння. */
public class DragonVoiceScreen extends Screen {
	private TextFieldWidget input;

	public DragonVoiceScreen() {
		super(Text.translatable("screen.pletinnya.dragon"));
	}

	@Override
	protected void init() {
		input = new TextFieldWidget(textRenderer, width / 2 - 120, height / 2 + 30, 240, 20, Text.empty());
		input.setMaxLength(80);
		input.setPlaceholder(Text.translatable("screen.pletinnya.dragon.placeholder").formatted(Formatting.DARK_GRAY));
		addDrawableChild(input);
		setInitialFocus(input);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
			String s = input.getText().trim();
			if (!s.isEmpty()) ClientPlayNetworking.send(new ModNetworking.DragonSpeakPayload(s));
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		int cx = width / 2, y = height / 2 - 80;
		GuiDraw.sigil(ctx, 12, cx - 8, y - 22, 0xF2C04A);
		ctx.drawCenteredTextWithShadow(textRenderer, title, cx, y, 0xF2C04A);
		y += 16;
		MageData d = ClientMage.data;
		int n = 0;
		for (int i = 0; i < DragonTongue.WORDS.length; i++) {
			if (!d.paths.knowsWord(i)) continue;
			int col = n % 3, row = n / 3;
			ctx.drawTextWithShadow(textRenderer, Text.literal(DragonTongue.WORDS[i].word() + " ").formatted(Formatting.GOLD)
				.append(DragonTongue.meaning(i).copy().formatted(Formatting.GRAY)), cx - 160 + col * 110, y + row * 11, 0xFFFFFF);
			n++;
		}
		if (n == 0) ctx.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.no_words"), cx, y, 0x9A8A70);
		ctx.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.dragon.hint"), cx, height / 2 + 56, 0x9A8A70);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
