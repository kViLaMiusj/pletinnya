package ua.pletinnya.magic;

import net.minecraft.text.Text;

import java.util.Locale;

/**
 * Драконяча мова — первісне Плетіння. Те, що маг розраховує, дракон виражає голосом і волею.
 * Слово напряму + слово форми + «РА» для сили. Закон розуміння не діє, помилок у формулі немає,
 * але тіло людини не обов'язково витримує таке навантаження.
 */
public final class DragonTongue {
	private DragonTongue() {}

	public record Word(String word, String latin, int type, int value) {}

	public static final int ELEMENT = 0, FORM = 1, POWER = 2;

	public static final Word[] WORDS = {
		new Word("ЛАГ", "LAG", ELEMENT, 0), new Word("ВАР", "VAR", ELEMENT, 1), new Word("ФЕЙ", "FEY", ELEMENT, 2),
		new Word("ДРУН", "DRUN", ELEMENT, 3), new Word("ЗАР", "ZAR", ELEMENT, 4), new Word("КЕЛ", "KEL", ELEMENT, 5),
		new Word("ВЕРД", "VERD", ELEMENT, 6), new Word("ГОЛ", "GOL", ELEMENT, 7), new Word("ЛУМ", "LUM", ELEMENT, 8),
		new Word("НОКС", "NOKS", ELEMENT, 9), new Word("САНГ", "SANG", ELEMENT, 10), new Word("АРК", "ARK", ELEMENT, 11),
		new Word("ТУН", "TUN", FORM, 0), new Word("РАХ", "RAKH", FORM, 1), new Word("ЗЕН", "ZEN", FORM, 2),
		new Word("КАР", "KAR", FORM, 3), new Word("МУР", "MUR", FORM, 4),
		new Word("РА", "RA", POWER, 2),
	};
	public static final int POWER_INDEX = 17;
	public static final int DEFAULT_FORM = 12;

	public static Text meaning(int i) {
		Word w = WORDS[i];
		return switch (w.type) {
			case ELEMENT -> Element.byIndex(w.value).displayName();
			case FORM -> SpellForm.byIndex(w.value).displayName();
			default -> Text.translatable("dragon.pletinnya.power");
		};
	}

	public record Result(SpellFormula formula, Text error) {}

	public static Result parse(String phrase, PathData known) {
		int element = -1, form = -1, power = 3;
		String[] parts = phrase.toUpperCase(Locale.ROOT).replaceAll("[^\\p{L}]+", " ").trim().split(" ");
		StringBuilder spoken = new StringBuilder();
		for (String raw : parts) {
			if (raw.isEmpty()) continue;
			int idx = -1;
			for (int i = 0; i < WORDS.length; i++) {
				if (WORDS[i].word.equals(raw) || WORDS[i].latin.equals(raw)) { idx = i; break; }
			}
			if (idx < 0) return new Result(null, Text.translatable("message.pletinnya.dragon_unknown", raw));
			if (!known.knowsWord(idx)) return new Result(null, Text.translatable("message.pletinnya.dragon_unlearned", raw));
			Word w = WORDS[idx];
			if (w.type == ELEMENT) element = w.value;
			else if (w.type == FORM) form = w.value;
			else power = Math.min(9, power + w.value);
			spoken.append(w.word).append(' ');
		}
		if (element < 0) return new Result(null, Text.translatable("message.pletinnya.dragon_no_element"));
		if (form < 0) form = 0;
		SpellFormula f = new SpellFormula(spoken.toString().trim(), element, form, power, 4, 4, 4, 0, 0);
		return new Result(f, null);
	}
}
