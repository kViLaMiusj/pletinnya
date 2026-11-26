package ua.pletinnya.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.Ranks;
import ua.pletinnya.registry.ModAttachments;

import java.util.Collection;

/**
 * /pletinnya stats [гравець] — характеристики мага й наступне випробування.
 * /pletinnya rank <гравці> <1-7> — присвоїти ранг (оператор).
 * /pletinnya mana <гравці> — наповнити ману (оператор).
 * /pletinnya reset <гравці> — скинути мага до учня (оператор).
 */
public final class PletinnyaCommand {
	private PletinnyaCommand() {}

	public static void register(CommandDispatcher<ServerCommandSource> d) {
		d.register(CommandManager.literal("pletinnya")
			.then(CommandManager.literal("stats")
				.executes(c -> stats(c.getSource(), c.getSource().getPlayerOrThrow()))
				.then(CommandManager.argument("player", EntityArgumentType.player())
					.requires(s -> s.hasPermissionLevel(2))
					.executes(c -> stats(c.getSource(), EntityArgumentType.getPlayer(c, "player")))))
			.then(CommandManager.literal("rank")
				.requires(s -> s.hasPermissionLevel(2))
				.then(CommandManager.argument("players", EntityArgumentType.players())
					.then(CommandManager.argument("rank", IntegerArgumentType.integer(1, 7))
						.executes(c -> setRank(c.getSource(), EntityArgumentType.getPlayers(c, "players"), IntegerArgumentType.getInteger(c, "rank"))))))
			.then(CommandManager.literal("mana")
				.requires(s -> s.hasPermissionLevel(2))
				.then(CommandManager.argument("players", EntityArgumentType.players())
					.executes(c -> fill(c.getSource(), EntityArgumentType.getPlayers(c, "players")))))
			.then(CommandManager.literal("reset")
				.requires(s -> s.hasPermissionLevel(2))
				.then(CommandManager.argument("players", EntityArgumentType.players())
					.executes(c -> reset(c.getSource(), EntityArgumentType.getPlayers(c, "players"))))));
	}

	private static int stats(ServerCommandSource src, ServerPlayerEntity p) throws CommandSyntaxException {
		MageData m = p.getAttachedOrCreate(ModAttachments.MAGE);
		src.sendFeedback(() -> Text.translatable("stats.pletinnya.header", p.getDisplayName(), Ranks.title(m.rank)).formatted(Formatting.GOLD), false);
		src.sendFeedback(() -> Text.translatable("stats.pletinnya.line",
			(int) m.mana, m.maxMana, m.conductivity, m.precision, m.perception).formatted(Formatting.AQUA), false);
		MutableText mastery = Text.translatable("stats.pletinnya.mastery").formatted(Formatting.GRAY);
		for (Element e : Element.values()) {
			mastery.append(Text.literal(" ")).append(e.displayName().copy().withColor(e.color)).append(Text.literal(":" + m.mastery(e)));
		}
		src.sendFeedback(() -> mastery, false);
		if (m.rank < 7) {
			src.sendFeedback(() -> Text.translatable("stats.pletinnya.trial", Ranks.title(m.rank + 1)).formatted(Formatting.YELLOW), false);
			for (Ranks.Requirement r : Ranks.trial(m)) {
				src.sendFeedback(() -> Text.literal(r.met() ? " ✔ " : " ✘ ").formatted(r.met() ? Formatting.GREEN : Formatting.RED).append(r.text().copy().formatted(Formatting.WHITE)), false);
			}
		}
		return 1;
	}

	private static int setRank(ServerCommandSource src, Collection<ServerPlayerEntity> players, int rank) {
		for (ServerPlayerEntity p : players) {
			MageData m = p.getAttachedOrCreate(ModAttachments.MAGE);
			m.rank = rank;
			m.markDirty();
			p.sendMessage(Text.translatable("message.pletinnya.rank_set", Ranks.title(rank)).formatted(Formatting.GOLD), false);
		}
		src.sendFeedback(() -> Text.translatable("command.pletinnya.rank_set", players.size(), Ranks.title(rank)), true);
		return players.size();
	}

	private static int fill(ServerCommandSource src, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity p : players) {
			MageData m = p.getAttachedOrCreate(ModAttachments.MAGE);
			m.mana = m.maxMana;
			m.channelDamage = 0;
			m.markDirty();
		}
		src.sendFeedback(() -> Text.translatable("command.pletinnya.mana_filled", players.size()), true);
		return players.size();
	}

	private static int reset(ServerCommandSource src, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity p : players) {
			MageData fresh = new MageData();
			p.setAttached(ModAttachments.MAGE, fresh);
			fresh.markDirty();
		}
		src.sendFeedback(() -> Text.translatable("command.pletinnya.reset", players.size()), true);
		return players.size();
	}
}
