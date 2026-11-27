package ua.pletinnya.magic;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;

/** Відкладені й повторювані дії заклять (пульсація, дощ, хвиля, стрибки ланцюга). Живе лише в пам'яті сервера. */
public final class SpellScheduler {
	private SpellScheduler() {}

	private static final class Task {
		long next;
		final int interval;
		int run = 0;
		final IntPredicate action;   // повертає false — завершити

		Task(long next, int interval, IntPredicate action) { this.next = next; this.interval = interval; this.action = action; }
	}

	private static final List<Task> TASKS = new ArrayList<>();
	private static final List<Task> PENDING = new ArrayList<>();
	private static long now = 0;

	/** Одноразова дія через delay тіків. */
	public static void later(int delay, Runnable r) {
		PENDING.add(new Task(now + Math.max(1, delay), 0, i -> { r.run(); return false; }));
	}

	/** Повторювана дія: перший раз через delay, далі кожні interval тіків, поки action(номер) повертає true. */
	public static void every(int delay, int interval, IntPredicate action) {
		PENDING.add(new Task(now + Math.max(1, delay), Math.max(1, interval), action));
	}

	public static void tick(MinecraftServer server) {
		now++;
		if (!PENDING.isEmpty()) { TASKS.addAll(PENDING); PENDING.clear(); }
		Iterator<Task> it = TASKS.iterator();
		while (it.hasNext()) {
			Task t = it.next();
			if (now < t.next) continue;
			boolean keep;
			try {
				keep = t.action.test(t.run++);
			} catch (Throwable e) {
				ua.pletinnya.Pletinnya.LOGGER.error("Spell task failed", e);
				keep = false;
			}
			if (keep && t.interval > 0) t.next = now + t.interval;
			else it.remove();
		}
	}
}
