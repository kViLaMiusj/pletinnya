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
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.Ranks;
import ua.pletinnya.magic.SpellForm;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.magic.SpellTrigger;
import ua.pletinnya.magic.WeavingLaws;
import ua.pletinnya.network.ModNetworking;

import java.util.ArrayList;
import java.util.List;

/**
 * Скрижаль плетіння. Ліворуч — дев'ять завчених формул, праворуч — конструктор:
 * Задум (напрям і форма), Формула (параметри й умова) і розраховане Живлення.
 * Друга сторінка — характеристики мага й наступне випробування.
 */
public class WeavingScreen extends Screen {
	private static final int W = 360, H = 232;

	private int slot;
	private SpellFormula draft;
	private boolean statsPage = false;

	private int left, top;
	private TextFieldWidget nameField;
	private final List<ButtonWidget> slotButtons = new ArrayList<>();
	private ButtonWidget elementBtn, formBtn, triggerBtn;

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

		addDrawableChild(ButtonWidget.builder(Text.translatable(statsPage ? "screen.pletinnya.to_weaving" : "screen.pletinnya.to_stats"), b -> {
			statsPage = !statsPage;
			clearAndInit();
		}).dimensions(left + W - 92, top + 4, 88, 16).build());

		if (statsPage) return;

		// --- Завчені формули ---
		for (int i = 0; i < MageData.SPELL_SLOTS; i++) {
			final int s = i;
			ButtonWidget b = addDrawableChild(ButtonWidget.builder(slotLabel(i), btn -> pickSlot(s))
				.dimensions(left + 6, top + 26 + i * 21, 104, 19).build());
			slotButtons.add(b);
		}

		int x = left + 120;
		int y = top + 26;

		nameField = new TextFieldWidget(textRenderer, x, y, 150, 16, Text.translatable("screen.pletinnya.name"));
		nameField.setMaxLength(24);
		nameField.setPlaceholder(Text.translatable("screen.pletinnya.name").formatted(Formatting.DARK_GRAY));
		nameField.setText(draft.name());
		nameField.setChangedListener(s -> draft = draft.withName(s));
		addDrawableChild(nameField);
		y += 22;

		// --- Задум ---
		addArrow(x, y, -1, () -> draft = draft.withElement(Math.floorMod(draft.element() - 1, Element.values().length)));
		elementBtn = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
			draft = draft.withElement(Math.floorMod(draft.element() + 1, Element.values().length));
			refresh();
		}).dimensions(x + 18, y, 196, 16).build());
		addArrow(x + 216, y, 1, () -> draft = draft.withElement(Math.floorMod(draft.element() + 1, Element.values().length)));
		y += 19;

		addArrow(x, y, -1, () -> draft = draft.withForm(Math.floorMod(draft.form() - 1, SpellForm.values().length)));
		formBtn = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
			draft = draft.withForm(Math.floorMod(draft.form() + 1, SpellForm.values().length));
			refresh();
		}).dimensions(x + 18, y, 196, 16).build());
		addArrow(x + 216, y, 1, () -> draft = draft.withForm(Math.floorMod(draft.form() + 1, SpellForm.values().length)));
		y += 19;

		addArrow(x, y, -1, () -> draft = draft.withTrigger(Math.floorMod(draft.trigger() - 1, SpellTrigger.values().length)));
		triggerBtn = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
			draft = draft.withTrigger(Math.floorMod(draft.trigger() + 1, SpellTrigger.values().length));
			refresh();
		}).dimensions(x + 18, y, 196, 16).build());
		addArrow(x + 216, y, 1, () -> draft = draft.withTrigger(Math.floorMod(draft.trigger() + 1, SpellTrigger.values().length)));
		y += 22;

		// --- Формула: параметри ---
		paramRow(x, y, 0);
		paramRow(x + 118, y, 1);
		y += 19;
		paramRow(x, y, 2);
		paramRow(x + 118, y, 3);

		// --- Дії ---
		int by = top + H - 24;
		addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.weave"), b -> {
			ClientPlayNetworking.send(new ModNetworking.SaveSpellPayload(slot, draft));
		}).dimensions(x, by, 76, 18).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.select"), b -> {
			ClientMage.data.selected = slot;
			ClientPlayNetworking.send(new ModNetworking.SelectSpellPayload(slot));
			refresh();
		}).dimensions(x + 80, by, 76, 18).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("screen.pletinnya.erase"), b -> {
			ClientPlayNetworking.send(new ModNetworking.SaveSpellPayload(slot, SpellFormula.empty()));
			draft = SpellFormula.empty();
			nameField.setText("");
		}).dimensions(x + 160, by, 72, 18).build());

		refresh();
	}

	private void addArrow(int x, int y, int dir, Runnable change) {
		addDrawableChild(ButtonWidget.builder(Text.literal(dir < 0 ? "<" : ">"), b -> {
			change.run();
			refresh();
		}).dimensions(x, y, 16, 16).build());
	}

	private void paramRow(int x, int y, int param) {
		addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
			setParam(param, getParam(param) - (hasShiftDown() ? 3 : 1));
			refresh();
		}).dimensions(x + 70, y, 16, 16).build());
		addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
			setParam(param, getParam(param) + (hasShiftDown() ? 3 : 1));
			refresh();
		}).dimensions(x + 98, y, 16, 16).build());
	}

	private static final String[] PARAMS = {"power", "size", "range", "duration"};

	private int getParam(int i) {
		return switch (i) {
			case 0 -> draft.power();
			case 1 -> draft.size();
			case 2 -> draft.range();
			default -> draft.duration();
		};
	}

	private void setParam(int i, int v) {
		v = MathHelper.clamp(v, SpellFormula.MIN, SpellFormula.MAX);
		draft = switch (i) {
			case 0 -> draft.withPower(v);
			case 1 -> draft.withSize(v);
			case 2 -> draft.withRange(v);
			default -> draft.withDuration(v);
		};
	}

	private boolean paramUsed(int i) {
		SpellForm f = draft.formType();
		return switch (i) {
			case 1 -> f.usesSize;
			case 2 -> f.usesRange;
			default -> true;
		};
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

	@Override
	public void tick() {
		super.tick();
		// Сервер міг оновити дані (наприклад, після збереження) — підтягнути назви слотів.
		if (!statsPage && client != null && client.world != null && client.world.getTime() % 10 == 0) refresh();
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		ctx.fill(left, top, left + W, top + 22, 0x66000000);
		ctx.drawTextWithShadow(textRenderer, title, left + 8, top + 7, 0xE6D3FF);
		if (statsPage) renderStats(ctx);
		else renderWeaving(ctx);
	}

	private void renderWeaving(DrawContext ctx) {
		MageData d = ClientMage.data;
		int x = left + 120;
		int y = top + 26 + 22 + 19 * 3 + 3;
		for (int i = 0; i < 4; i++) {
			int px = x + (i % 2) * 118;
			int py = y + (i / 2) * 19;
			int color = paramUsed(i) ? 0xFFFFFF : 0x666666;
			ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.param." + PARAMS[i]), px, py + 4, color);
			String v = String.valueOf(getParam(i));
			ctx.drawTextWithShadow(textRenderer, Text.literal(v), px + 92 - textRenderer.getWidth(v) / 2, py + 4, color);
		}

		int iy = y + 19 * 2 + 6;
		ctx.fill(x - 2, iy - 3, x + 236, iy + 56, 0x55000000);
		int cx = draft.complexity();
		int limit = WeavingLaws.understandingLimit(d, draft.elementType());
		float cost = client != null && client.player != null ? WeavingLaws.cost(client.player, d, draft) : 0f;
		float cond = client != null && client.player != null ? WeavingLaws.effectiveConductivity(client.player, d) : d.conductivity;
		float risk = client != null && client.player != null ? WeavingLaws.misfireChance(client.player, d, draft, 0, cost) : 0f;

		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.complexity", cx, limit), x, iy, cx <= limit ? 0xB0FFB0 : 0xFF7070);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.cost", (int) Math.ceil(cost), (int) d.mana), x + 118, iy, cost <= d.mana ? 0x9DB4FF : 0xFF7070);
		iy += 11;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.conductivity", (int) cond), x, iy, cost <= cond ? 0xC8C8C8 : 0xFF9040);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.risk", Math.round(risk * 100)), x + 118, iy, risk < 0.15f ? 0xC8C8C8 : risk < 0.4f ? 0xFFD060 : 0xFF7070);
		iy += 11;
		Text hint = draft.triggerType().minRank > d.rank
			? Text.translatable("message.pletinnya.trigger_rank", Ranks.title(draft.triggerType().minRank))
			: cost > cond ? Text.translatable("screen.pletinnya.warn_overload")
			: Text.translatable("screen.pletinnya.desc." + draft.formType().id);
		ctx.drawTextWrapped(textRenderer, hint, x, iy, 234, 0xA0A0A0);
	}

	private void renderStats(DrawContext ctx) {
		MageData d = ClientMage.data;
		int x = left + 10;
		int y = top + 28;
		ctx.drawTextWithShadow(textRenderer, Ranks.title(d.rank), x, y, 0xFFD27A);
		y += 13;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.mana", (int) d.mana, d.maxMana), x, y, 0x9DB4FF);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.conductivity", d.conductivity), x + 170, y, 0xFFB070);
		y += 11;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.precision", d.precision), x, y, 0xB0FFB0);
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.perception", d.perception), x + 170, y, 0xE0B0FF);
		y += 11;
		ctx.drawTextWithShadow(textRenderer, Text.translatable("screen.pletinnya.stat.load", Ranks.loadLimit(d.rank), Ranks.complexityCap(d.rank)), x, y, 0xC8C8C8);
		y += 15;

		// Розуміння напрямів
		Element[] els = Element.values();
		for (int i = 0; i < els.length; i++) {
			Element e = els[i];
			int px = x + (i % 3) * 114;
			int py = y + (i / 3) * 12;
			ctx.drawTextWithShadow(textRenderer, e.displayName(), px, py, e.color);
			int bx = px + 62;
			ctx.fill(bx, py + 2, bx + 42, py + 7, 0x88000000);
			ctx.fill(bx, py + 2, bx + (int) (42 * d.mastery(e) / 10f), py + 7, 0xFF000000 | e.color);
		}
		y += 12 * 4 + 6;

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

	@Override
	public boolean shouldPause() {
		return false;
	}
}
