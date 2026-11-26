package ua.pletinnya.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import ua.pletinnya.magic.DragonTongue;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.Prayers;
import ua.pletinnya.magic.Ranks;
import ua.pletinnya.magic.SpellForm;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.magic.SpellTrigger;
import ua.pletinnya.magic.WeavingLaws;
import ua.pletinnya.network.ModNetworking;

import java.util.ArrayList;
import java.util.List;

/**
 * Скрижаль плетіння. Вкладки: Формула (Задум, Формула, Живлення й діаграма ниток),
 * Вручну (ручне плетіння — повторити візерунок вузлів), Маг (характеристики й випробування),
 * Шляхи (драконяча мова, угода з духом, віра й молитви).
 */
public class WeavingScreen extends Screen {
	private static final int W = 360, H = 232;
	private static int tab = 0;

	private int slot;
	private SpellFormula draft;
	private int left, top;
	private TextFieldWidget nameField;
	private final List<ButtonWidget> slotButtons = new ArrayList<>();
	private ButtonWidget elementBtn, formBtn, triggerBtn;

	// ручне плетіння
	private int phase = 0;          // 0 — очікування, 1 — показ, 2 — введення, 3 — результат
	private int showTicks = 0;
	private int[] pattern = new int[0];
	private final List<Integer> input = new ArrayList<>();
	private int lastQuality = -1;

	public static void setTab(int t) {
		tab = t;
	}

	public WeavingScreen() {
		super(Text.translatable("screen.pletinnya.tablet"));
		MageData d = ClientMage.data;
		this.slot = d.selected;
		SpellFormula f = d.spells.get(slot);
		this.draft = f.isEmpty() ? SpellFormula.empty() : f;
	}

	@Override
	protected void init() {
		left = (width - W) / 2;
		top = (height - H) / 2;
		slotButtons.clear();
		elementBtn = formBtn = triggerBtn = null;
		nameField = null;

		String[] tabs = {"formula", "hand", "mage", "paths"};
		for (int i = 0; i < tabs.length; i++) {
			final int t = i;
			ButtonWidget b = ButtonWidget.builder(Text.translatable("screen.pletinnya.tab." + tabs[i]), btn -> {
				tab = t;
				phase = 0;
				clearAndInit();
			}).dimensions(left + 130 + i * 57, top + 5, 55, 16).build();
			b.active = tab != i;
			addDrawableChild(b);
		}

		switch (tab) {
			case 0 -> initFormula();
			case 1 -> initHand();
			case 3 -> initPaths();
			default -> {
			}
		}
	}

	// =============================================================== ФОРМУЛА
	private void initFormula() {
		for (int i = 0; i < MageData.SPELL_SLOTS; i++) {
			final int s = i;
			slotButtons.add(addDrawableChild(ButtonWidget.builder(slotLabel(i), b -> pickSlot(s)).dimensions(left + 8, top + 28 + i * 21, 104, 19).build()));
		}
		int x = left + 120, y = top + 28;
		nameField = new TextFieldWidget(textRenderer, x, y, 150, 16, Text.translatable("screen.pletinnya.name"));
		nameField.setMaxLength(24);
		nameField.setPlaceholder(Text.translatable("screen.pletinnya.name").formatted(Formatting.DARK_GRAY));
		nameField.setText(draft.name());
		nameField.setChangedListener(s -> draft = draft.withName(s));
		addDrawableChild(nameField);
		y += 21;
		elementBtn = cycler(x, y, d -> draft.withElement(Math.floorMod(draft.element() + d, Element.values().length)));
		y += 19;
		formBtn = cycler(x, y, d -> draft.withForm(Math.floorMod(draft.form() + d, SpellForm.values().length)));
		y += 19;
		triggerBtn = cycler(x, y, d -> draft.withTrigger(Math.floorMod(draft.trigger() + d, SpellTrigger.values().length)));
		y += 22;
		paramRow(x, y, 0); paramRow(x + 118, y, 1);
		paramRow(x, y + 19, 2); paramRow(x + 118, y + 19, 3);

		int by = top + H - 24;
		addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.weave"), b ->
			ClientPlayNetworking.send(new ModNetworking.SaveSpellPayload(slot, draft))).dimensions(x, by, 76, 18).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.select"), b -> {
			ClientMage.data.selected = slot;
			ClientPlayNetworking.send(new ModNetworking.SelectSpellPayload(slot));
			refresh();
		}).dimensions(x + 80, by, 76, 18).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.erase"), b -> {
			ClientPlayNetworking.send(new ModNetworking.SaveSpellPayload(slot, SpellFormula.empty()));
			draft = SpellFormula.empty();
			if (nameField != null) nameField.setText("");
		}).dimensions(x + 160, by, 72, 18).build());
		refresh();
	}

	private interface Change { SpellFormula apply(int dir); }

	private ButtonWidget cycler(int x, int y, Change ch) {
		addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> { draft = ch.apply(-1).withQuality(0); refresh(); }).dimensions(x, y, 16, 16).build());
		ButtonWidget mid = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> { draft = ch.apply(1).withQuality(0); refresh(); }).dimensions(x + 18, y, 196, 16).build());
		addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> { draft = ch.apply(1).withQuality(0); refresh(); }).dimensions(x + 216, y, 16, 16).build());
		return mid;
	}

	private static final String[] PARAMS = {"power", "size", "range", "duration"};

	private void paramRow(int x, int y, int param) {
		addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> { setParam(param, getParam(param) - (hasShiftDown() ? 3 : 1)); refresh(); }).dimensions(x + 70, y, 16, 16).build());
		addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> { setParam(param, getParam(param) + (hasShiftDown() ? 3 : 1)); refresh(); }).dimensions(x + 98, y, 16, 16).build());
	}

	private int getParam(int i) {
		return switch (i) { case 0 -> draft.power(); case 1 -> draft.size(); case 2 -> draft.range(); default -> draft.duration(); };
	}

	private void setParam(int i, int v) {
		v = MathHelper.clamp(v, SpellFormula.MIN, SpellFormula.MAX);
		draft = (switch (i) {
			case 0 -> draft.withPower(v); case 1 -> draft.withSize(v); case 2 -> draft.withRange(v); default -> draft.withDuration(v);
		}).withQuality(0);
	}

	private boolean paramUsed(int i) {
		SpellForm f = draft.formType();
		return switch (i) { case 1 -> f.usesSize; case 2 -> f.usesRange; default -> true; };
	}

	private void pickSlot(int s) {
		slot = s;
		SpellFormula f = ClientMage.data.spells.get(s);
		draft = f.isEmpty() ? SpellFormula.empty() : f;
		if (nameField != null) nameField.setText(draft.name());
		refresh();
	}

	private Text slotLabel(int i) {
		SpellFormula f = ClientMage.data.spells.get(i);
		String mark = (i == ClientMage.data.selected ? "» " : "") + (i + 1) + ". ";
		if (f.isEmpty()) return Text.literal(mark + "—").formatted(Formatting.DARK_GRAY);
		return Text.literal(mark + f.name()).withColor(f.elementType().color);
	}

	private void refresh() {
		if (elementBtn == null) return;
		Element e = draft.elementType();
		elementBtn.setMessage(Text.translatable("screen.pletinnya.element").append(": ").append(e.displayName().copy().withColor(e.color)));
		formBtn.setMessage(Text.translatable("screen.pletinnya.form").append(": ").append(draft.formType().displayName()));
		MutableText trig = Text.translatable("screen.pletinnya.trigger").append(": ").append(draft.triggerType().displayName());
		if (draft.triggerType().minRank > ClientMage.data.rank) trig.formatted(Formatting.RED);
		triggerBtn.setMessage(trig);
		for (int i = 0; i < slotButtons.size(); i++) {
			MutableText label = slotLabel(i).copy();
			if (i == slot) label.formatted(Formatting.UNDERLINE);
			slotButtons.get(i).setMessage(label);
		}
	}

	private void renderFormula(DrawContext ctx) {
		MageData d = ClientMage.data;
		int x = left + 120;
		int y = top + 28 + 21 + 19 * 3 + 3;
		for (int i = 0; i < 4; i++) {
			int px = x + (i % 2) * 118, py = y + (i / 2) * 19;
			int color = paramUsed(i) ? 0xF0E6D0 : 0x6A6070;
			ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.param." + PARAMS[i]), px, py + 4, color);
			String v = String.valueOf(getParam(i));
			ctx.drawTextWithShadow(textRenderer, Text.literal(v), px + 92 - textRenderer.getWidth(v) / 2, py + 4, color);
		}
		GuiDraw.sigil(ctx, draft.element(), x + 236, top + 49, draft.elementType().color);

		int iy = y + 19 * 2 + 4;
		ctx.fill(x - 2, iy - 3, x + 236, iy + 58, 0x66000000);
		// діаграма ниток
		int cx = x + 28, cy = iy + 26;
		int[] angles = {-90, 0, 90, 180};
		int[] cols = {0xFFFF8A5A, 0xFF7AD0FF, 0xFFB0FF7A, 0xFFE0B0FF};
		for (int i = 0; i < 4; i++) {
			if (!paramUsed(i)) continue;
			double a = Math.toRadians(angles[i] + 45);
			int len = 6 + getParam(i) * 2;
			int ex = cx + (int) (Math.cos(a) * len), ey = cy + (int) (Math.sin(a) * len);
			GuiDraw.line(ctx, cx, cy, ex, ey, cols[i], 1);
			GuiDraw.node(ctx, ex, ey, 2, cols[i]);
		}
		GuiDraw.node(ctx, cx, cy, 5, 0xFF000000 | draft.elementType().color);
		if (draft.quality() > 0) {
			for (int k = 0; k < 12; k++) {
				double a = k * Math.PI / 6;
				ctx.fill(cx + (int) (Math.cos(a) * 26), cy + (int) (Math.sin(a) * 26), cx + (int) (Math.cos(a) * 26) + 1, cy + (int) (Math.sin(a) * 26) + 1, 0xFFFFD27A);
			}
		}

		int tx = x + 62;
		int cxv = draft.complexity();
		int limit = WeavingLaws.understandingLimit(d, draft.elementType());
		float cost = client != null && client.player != null ? WeavingLaws.cost(client.player, d, draft) : 0f;
		float cond = client != null && client.player != null ? WeavingLaws.effectiveConductivity(client.player, d) : d.conductivity;
		float risk = client != null && client.player != null ? WeavingLaws.misfireChance(client.player, d, draft, 0, cost) : 0f;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.complexity", cxv, limit), tx, iy, cxv <= limit ? 0xB0FFB0 : 0xFF7070);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.cost", (int) Math.ceil(cost), (int) d.mana), tx, iy + 11, cost <= d.mana ? 0x9DB4FF : 0xFF7070);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.conductivity", (int) cond), tx, iy + 22, cost <= cond ? 0xC8C8C8 : 0xFF9040);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.risk", Math.round(risk * 100)), tx + 90, iy + 22, risk < 0.15f ? 0xC8C8C8 : risk < 0.4f ? 0xFFD060 : 0xFF7070);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.quality", draft.quality()), tx + 90, iy + 11, draft.quality() > 0 ? 0xFFD27A : 0x8A8090);
		Text hint = draft.triggerType().minRank > d.rank ? Text.translatable("message.pletinnya.trigger_rank", Ranks.title(draft.triggerType().minRank))
			: cost > cond ? Text.translatable("screen.pletinnya.warn_overload")
			: Text.translatable("screen.pletinnya.desc." + draft.formType().id);
		ctx.drawTextWrapped(textRenderer, hint, tx, iy + 34, 170, 0xA0A0A0);
	}

	// =============================================================== РУЧНЕ ПЛЕТІННЯ
	private void initHand() {
		addDrawableChild(ButtonWidget.builder(Text.translatable(phase == 0 ? "screen.pletinnya.hand.start" : "screen.pletinnya.hand.again"), b -> {
			pattern = draft.pattern();
			input.clear();
			phase = 1;
			showTicks = 30 + pattern.length * 6;
			lastQuality = -1;
			clearAndInit();
		}).dimensions(left + 14, top + 150, 100, 20).build());
		if (lastQuality >= 0) {
			addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.hand.apply"), b -> {
				draft = draft.withQuality(Math.max(draft.quality(), lastQuality));
				ClientPlayNetworking.send(new ModNetworking.SaveSpellPayload(slot, draft));
				tab = 0;
				clearAndInit();
			}).dimensions(left + 14, top + 174, 100, 20).build());
		}
	}

	private int[] nodePos(int i) {
		int gx = left + 200, gy = top + 50;
		return new int[]{gx + (i % 3) * 56, gy + (i / 3) * 56};
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (tab == 1 && phase == 2) {
			for (int i = 0; i < 9; i++) {
				int[] p = nodePos(i);
				if ((mx - p[0]) * (mx - p[0]) + (my - p[1]) * (my - p[1]) < 11 * 11) {
					if (!input.isEmpty() && input.get(input.size() - 1) == i) return true;
					input.add(i);
					if (client != null) client.getSoundManager().play(net.minecraft.client.sound.PositionedSoundInstance.master(
						net.minecraft.sound.SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.8f + input.size() * 0.08f));
					if (input.size() >= pattern.length) finishHand();
					return true;
				}
			}
		}
		return super.mouseClicked(mx, my, button);
	}

	private void finishHand() {
		int ok = 0;
		for (int i = 0; i < pattern.length; i++) if (input.get(i) == pattern[i]) ok++;
		lastQuality = Math.round(ok * 100f / pattern.length);
		if (ok == pattern.length) lastQuality = 100;
		phase = 3;
		clearAndInit();
	}

	private void renderHand(DrawContext ctx) {
		int x = left + 14;
		ctx.drawTextWrapped(textRenderer, Text.translatable("screen.pletinnya.hand.info"), x, top + 30, 150, 0xD8CCE8);
		ctx.drawTextWithShadow(textRenderer, Text.literal(draft.isEmpty() ? "—" : draft.name()).withColor(draft.elementType().color), x, top + 112, 0xFFFFFF);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.quality", draft.quality()), x, top + 124, 0xFFD27A);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.hand.length", pattern.length > 0 ? pattern.length : draft.pattern().length), x, top + 136, 0xA0A0A0);
		if (phase == 1) ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.hand.memorize"), left + 190, top + 28, 0xFFE080);
		if (phase == 2) ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.hand.repeat", input.size(), pattern.length), left + 190, top + 28, 0x9DE0FF);
		if (phase == 3) ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.hand.result", lastQuality), left + 190, top + 28, lastQuality >= 80 ? 0x80FF80 : 0xFFB070);

		int col = 0xFF000000 | draft.elementType().color;
		// візерунок під час показу або після завершення
		if (phase == 1 || phase == 3) {
			int shown = phase == 1 ? Math.min(pattern.length, 1 + (30 + pattern.length * 6 - showTicks) / 6) : pattern.length;
			for (int i = 1; i < shown; i++) {
				int[] a = nodePos(pattern[i - 1]), b = nodePos(pattern[i]);
				GuiDraw.line(ctx, a[0], a[1], b[0], b[1], phase == 1 ? col : 0x66FFFFFF, 2);
			}
		}
		for (int i = 1; i < input.size(); i++) {
			int[] a = nodePos(input.get(i - 1)), b = nodePos(input.get(i));
			boolean good = i < pattern.length && input.get(i).equals(pattern[i]) && input.get(i - 1).equals(pattern[i - 1]);
			GuiDraw.line(ctx, a[0], a[1], b[0], b[1], good || phase == 2 ? 0xFFFFE6A0 : 0xFFFF6060, 2);
		}
		for (int i = 0; i < 9; i++) {
			int[] p = nodePos(i);
			boolean used = input.contains(i);
			GuiDraw.node(ctx, p[0], p[1], 9, 0x88000000);
			GuiDraw.node(ctx, p[0], p[1], 7, used ? 0xFFFFE6A0 : 0xFF5A4A7A);
			GuiDraw.node(ctx, p[0], p[1], 3, used ? 0xFFFFFFFF : col);
		}
	}

	// =============================================================== ШЛЯХИ
	private void initPaths() {
		MageData d = ClientMage.data;
		if (d.paths.deity > 0) {
			int k = 0;
			for (int i = 0; i < Prayers.ALL.length; i++) {
				Prayers.Prayer p = Prayers.ALL[i];
				if (p.deity() != d.paths.deity) continue;
				final int idx = i;
				addDrawableChild(ButtonWidget.builder(Text.translatable("prayer.pletinnya.name." + p.id()).append(" (" + p.cost() + ")"), b ->
					ClientPlayNetworking.send(new ModNetworking.PrayPayload(idx))).dimensions(left + 196, top + 150 + k * 21, 156, 19).build());
				k++;
			}
		}
	}

	private void renderPaths(DrawContext ctx) {
		MageData d = ClientMage.data;
		int x = left + 12, y = top + 30;
		GuiDraw.sigil(ctx, 12, x - 2, y - 3, 0xF2C04A);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.dragon"), x + 16, y, 0xF2C04A);
		y += 14;
		int known = 0;
		for (int i = 0; i < DragonTongue.WORDS.length; i++) {
			if (!d.paths.knowsWord(i)) continue;
			int col = known % 2, row = known / 2;
			ctx.drawTextWithShadow(textRenderer, Text.literal(DragonTongue.WORDS[i].word() + " ").formatted(Formatting.GOLD).append(DragonTongue.meaning(i).copy().formatted(Formatting.GRAY)),
				x + col * 88, y + row * 10, 0xFFFFFF);
			known++;
		}
		if (known == 0) ctx.drawTextWrapped(textRenderer, Text.translatable("screen.pletinnya.paths.no_words"), x, y, 170, 0x8A8090);
		else ctx.drawTextWrapped(textRenderer, Text.translatable("screen.pletinnya.paths.dragon_hint"), x, top + H - 36, 170, 0x8A8090);

		int rx = left + 196, ry = top + 30;
		GuiDraw.sigil(ctx, 13, rx - 2, ry - 3, 0x9FFFF5);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.spirit"), rx + 16, ry, 0x9FFFF5);
		Element sp = d.paths.spiritElement();
		if (sp == null) {
			ctx.drawTextWrapped(textRenderer, Text.translatable("screen.pletinnya.paths.no_spirit"), rx, ry + 14, 156, 0x8A8090);
		} else {
			ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.spirit_of", sp.displayName()).withColor(sp.color), rx, ry + 14, 0xFFFFFF);
			ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.bond", d.paths.spiritBond), rx, ry + 26, 0xC8C8C8);
			GuiDraw.bar(ctx, rx, ry + 38, 150, 4, d.paths.spiritBond / 100f, 0xFF7FE7E0);
		}

		ry = top + 92;
		GuiDraw.sigil(ctx, 14, rx - 2, ry - 3, 0xF2D27A);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.faith"), rx + 16, ry, 0xF2D27A);
		if (d.paths.deity == 0) {
			ctx.drawTextWrapped(textRenderer, Text.translatable("screen.pletinnya.paths.no_deity"), rx, ry + 14, 156, 0x8A8090);
		} else {
			ctx.drawTextWithShadow(textRenderer, Prayers.deityName(d.paths.deity).copy().withColor(Prayers.DEITY_COLOR[d.paths.deity]), rx, ry + 14, 0xFFFFFF);
			ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.paths.faith_value", (int) d.paths.faith), rx, ry + 26, 0xC8C8C8);
			GuiDraw.bar(ctx, rx, ry + 38, 150, 4, d.paths.faith / 100f, 0xFFF2D27A);
		}
	}

	// =============================================================== МАГ
	private void renderMage(DrawContext ctx) {
		MageData d = ClientMage.data;
		int x = left + 12, y = top + 30;
		ctx.drawTextWithShadow(textRenderer, Ranks.title(d.rank), x, y, 0xFFD27A);
		y += 14;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.mana", (int) d.mana, d.maxMana), x, y, 0x9DB4FF);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.conductivity", d.conductivity), x + 170, y, 0xFFB070);
		y += 11;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.precision", d.precision), x, y, 0xB0FFB0);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.perception", d.perception), x + 170, y, 0xE0B0FF);
		y += 11;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.load", Ranks.loadLimit(d.rank), Ranks.complexityCap(d.rank)), x, y, 0xC8C8C8);
		y += 15;
		Element[] els = Element.values();
		for (int i = 0; i < els.length; i++) {
			Element e = els[i];
			int px = x + (i % 3) * 114, py = y + (i / 3) * 13;
			GuiDraw.sigil(ctx, i, px - 3, py - 4, e.color);
			ctx.drawTextWithShadow(textRenderer, e.displayName(), px + 13, py, e.color);
			int bx = px + 66;
			GuiDraw.bar(ctx, bx, py + 2, 38, 4, d.mastery(e) / 10f, 0xFF000000 | e.color);
		}
		y += 13 * 4 + 6;
		if (d.rank < 7) {
			ctx.drawTextWithShadow(textRenderer, Text.translatable("stats.pletinnya.trial", Ranks.title(d.rank + 1)), x, y, 0xFFE080);
			y += 12;
			for (Ranks.Requirement r : Ranks.trial(d)) {
				ctx.drawTextWithShadow(textRenderer, Text.literal(r.met() ? "✔ " : "✘ ").append(r.text()), x + 6, y, r.met() ? 0x80FF80 : 0xFF8080);
				y += 11;
			}
		} else {
			ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.max_rank"), x, y, 0xFFE080);
		}
	}

	// =============================================================== СПІЛЬНЕ
	@Override
	public void tick() {
		super.tick();
		if (tab == 1 && phase == 1 && --showTicks <= 0) {
			phase = 2;
			clearAndInit();
		}
		if (tab == 0 && client != null && client.world != null && client.world.getTime() % 10 == 0) refresh();
	}

	@Override
	public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.renderBackground(ctx, mouseX, mouseY, delta);
		ctx.drawTexture(GuiDraw.PANEL, left, top, 0f, 0f, W, H, 384, 256);
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		GuiDraw.sigil(ctx, 11, left + 8, top + 4, 0xB8AEFF);
		ctx.drawTextWithShadow(textRenderer, title, left + 26, top + 9, 0xF0E0B0);
		switch (tab) {
			case 0 -> renderFormula(ctx);
			case 1 -> renderHand(ctx);
			case 2 -> renderMage(ctx);
			default -> renderPaths(ctx);
		}
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
