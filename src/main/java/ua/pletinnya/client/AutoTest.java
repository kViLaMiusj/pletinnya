package ua.pletinnya.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.MagicPath;
import ua.pletinnya.magic.SpellCaster;
import ua.pletinnya.magic.SpellForm;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.registry.ModAttachments;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Автоматичний огляд моду для CI (лише з -Dpletinnya.autotest=true): вдягає сети, тримає посохи,
 * кастує закляття, ставить структури, відкриває Скрижаль і робить знімки екрана.
 */
public final class AutoTest {
	private AutoTest() {}

	private record Step(int delay, Consumer<MinecraftClient> action) {}

	private static final List<Step> STEPS = new ArrayList<>();
	private static int index = 0, wait = 0, worldTicks = 0;

	public static void init() {
		if (!Boolean.getBoolean("pletinnya.autotest")) return;
		build();
		ClientTickEvents.END_CLIENT_TICK.register(c -> {
			if (c.player == null || c.getServer() == null) return;
			if (++worldTicks < 200) return;
			if (wait > 0) { wait--; return; }
			if (index >= STEPS.size()) { c.scheduleStop(); return; }
			Step s = STEPS.get(index++);
			try { s.action.accept(c); } catch (Throwable t) { ua.pletinnya.Pletinnya.LOGGER.error("AutoTest step failed", t); }
			wait = s.delay;
		});
	}

	private static void cmd(MinecraftClient c, String command) {
		MinecraftServer server = c.getServer();
		String name = c.player.getGameProfile().getName();
		String full = command.replace("@me", name);
		server.execute(() -> server.getCommandManager().executeWithPrefix(server.getCommandSource(), full));
	}

	private static void shot(MinecraftClient c, String name) {
		ScreenshotRecorder.saveScreenshot(c.runDirectory, name + ".png", c.getFramebuffer(), t -> {});
	}

	private static void serverPlayer(MinecraftClient c, Consumer<ServerPlayerEntity> f) {
		MinecraftServer server = c.getServer();
		server.execute(() -> {
			ServerPlayerEntity p = server.getPlayerManager().getPlayer(c.player.getUuid());
			if (p != null) f.accept(p);
		});
	}

	private static void add(int delay, Consumer<MinecraftClient> a) { STEPS.add(new Step(delay, a)); }

	private static void build() {
		add(20, c -> {
			c.options.pauseOnLostFocus = false;
			cmd(c, "gamemode creative @me");
			cmd(c, "time set 6000"); cmd(c, "weather clear"); cmd(c, "gamerule doDaylightCycle false"); cmd(c, "gamerule doMobSpawning false");
			cmd(c, "effect give @me night_vision 99999 0 true");
			cmd(c, "tp @me ~ 120 ~");
		});
		add(40, c -> {
			cmd(c, "execute at @me run fill ~-12 ~-1 ~-12 ~12 ~-1 ~12 grass_block");
			cmd(c, "execute at @me run fill ~-12 ~ ~-12 ~12 ~12 ~12 air");
			cmd(c, "execute at @me run tp @me ~ ~ ~ 0 0");
		});
		// одяг: вид спереду
		for (MagicPath path : MagicPath.values()) {
			add(30, c -> {
				c.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
				c.options.hudHidden = true;
				for (String[] s : new String[][]{{"head", "helmet"}, {"chest", "chestplate"}, {"legs", "leggings"}, {"feet", "boots"}}) {
					cmd(c, "item replace entity @me armor." + s[0] + " with pletinnya:" + path.id + "_" + s[1]);
				}
			});
			add(4, c -> shot(c, "armor_" + path.id));
			add(14, c -> c.player.setYaw(c.player.getYaw() + 150f));
			add(4, c -> shot(c, "armor_back_" + path.id));
			add(1, c -> c.player.setYaw(0f));
		}
		// посохи в руці
		String[] staffs = {"staff_fire", "staff_ice", "staff_nature", "staff_mana", "dragon_sceptre", "spirit_lantern", "holy_sceptre", "aura_blade", "architect_staff"};
		for (String st : staffs) {
			add(20, c -> {
				c.options.setPerspective(Perspective.FIRST_PERSON);
				c.options.hudHidden = false;
				cmd(c, "item replace entity @me weapon.mainhand with pletinnya:" + st);
			});
			add(2, c -> shot(c, "hand_" + st));
		}
		add(20, c -> {
			c.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
			c.options.hudHidden = true;
			cmd(c, "item replace entity @me weapon.mainhand with pletinnya:staff_mana");
		});
		add(4, c -> shot(c, "hold_third"));
		add(4, c -> c.options.setPerspective(Perspective.THIRD_PERSON_BACK));
		add(6, c -> shot(c, "hold_back"));
		// інвентар з іконками
		String[][] pages = {
			{"essence_water", "essence_fire", "essence_wind", "essence_earth", "essence_lightning", "essence_ice", "essence_nature", "essence_sound", "essence_light",
			 "essence_darkness", "essence_blood", "essence_mana", "essence_dragon", "essence_spirit", "essence_holy", "essence_aura", "treatise_water", "treatise_fire",
			 "treatise_mana", "treatise_basics", "mana_crystal", "weaving_tablet", "weave_eye", "mana_elixir", "channel_elixir", "faith_elixir", "dragon_codex",
			 "holy_symbol", "spirit_seal", "staff_water", "staff_fire", "staff_lightning", "staff_darkness", "staff_blood", "apprentice_staff", "weaver_staff"},
			{"water_helmet", "water_chestplate", "water_leggings", "water_boots", "fire_helmet", "fire_chestplate", "mana_helmet", "mana_chestplate", "holy_helmet",
			 "dragon_helmet", "aura_helmet", "ice_helmet", "light_helmet", "nature_helmet", "darkness_helmet", "spirit_chestplate", "blood_chestplate", "earth_chestplate",
			 "mana_crystal_ore", "deepslate_mana_crystal_ore", "mana_crystal_block", "rune_stone", "rune_stone_bricks", "chiseled_rune_stone", "mana_glass",
			 "crystal_lamp", "mana_node", "holy_altar", "spirit_shrine", "mana_crystal_cluster", "staff_sound", "staff_light", "staff_wind", "staff_earth", "staff_nature", "staff_ice"}};
		for (int pg = 0; pg < pages.length; pg++) {
			final String[] page = pages[pg];
			final int n = pg;
			add(10, c -> {
				cmd(c, "gamemode survival @me");
				cmd(c, "clear @me");
				for (int i = 0; i < page.length; i++) cmd(c, "item replace entity @me container." + i + " with pletinnya:" + page[i]);
				cmd(c, "item replace entity @me armor.chest with pletinnya:mana_chestplate");
				cmd(c, "item replace entity @me armor.head with pletinnya:mana_helmet");
			});
			add(15, c -> { c.options.hudHidden = false; c.setScreen(new InventoryScreen(c.player)); });
			add(3, c -> shot(c, "inventory_" + n));
			add(2, c -> c.setScreen(null));
		}
		add(10, c -> { cmd(c, "gamemode creative @me"); cmd(c, "clear @me"); });
		// закляття: снаряд і область кожного напряму
		for (Element e : Element.values()) {
			for (SpellForm f : new SpellForm[]{SpellForm.PROJECTILE, SpellForm.AREA}) {
				add(10, c -> {
					c.options.setPerspective(Perspective.THIRD_PERSON_BACK);
					c.options.hudHidden = true;
					c.player.setPitch(f == SpellForm.AREA ? 35f : 5f);
					c.player.setYaw(0f);
				});
				add(f == SpellForm.PROJECTILE ? 7 : 4, c -> serverPlayer(c, p -> {
					p.setPitch(f == SpellForm.AREA ? 35f : 5f);
					p.setYaw(0f);
					MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
					d.rank = 7; d.mana = 10000; d.maxMana = 10000; d.conductivity = 3000; d.precision = 100;
					SpellFormula sf = new SpellFormula("test", e.ordinal(), f.ordinal(), 6, 5, 3, 3, 0);
					SpellCaster.forget(p.getUuid());
					SpellCaster.cast(p, d, sf);
				}));
				add(f == SpellForm.AREA ? 2 : 3, c -> shot(c, "spell_" + e.id + "_" + f.id));
				add(25, c -> { });
			}
		}
		// конструкти
		for (Element e : new Element[]{Element.EARTH, Element.ICE, Element.MANA, Element.NATURE}) {
			add(4, c -> serverPlayer(c, p -> {
				p.setPitch(30f);
				MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
				SpellCaster.forget(p.getUuid());
				SpellCaster.cast(p, d, new SpellFormula("wall", e.ordinal(), SpellForm.CONSTRUCT.ordinal(), 7, 7, 2, 4, 0));
			}));
			add(20, c -> shot(c, "construct_" + e.id));
		}
		// інтерфейс
		add(10, c -> serverPlayer(c, p -> {
			MageData d = p.getAttachedOrCreate(ModAttachments.MAGE);
			d.rank = 4; d.paths.dragonWords = (1 << 18) - 1; d.paths.spirit = 7; d.paths.spiritBond = 55; d.paths.deity = 1; d.paths.faith = 70f;
			d.spells.set(2, new SpellFormula("Вогняна комета", 1, 0, 6, 4, 6, 2, 0, 80));
			d.markDirty();
		}));
		for (int t = 0; t < 4; t++) {
			final int tab = t;
			add(15, c -> { c.options.hudHidden = false; WeavingScreen.setTab(tab); c.setScreen(new WeavingScreen()); });
			add(3, c -> shot(c, "tablet_" + tab));
		}
		add(10, c -> c.setScreen(new DragonVoiceScreen()));
		add(3, c -> shot(c, "dragon_voice"));
		add(5, c -> c.setScreen(null));
		add(10, c -> shot(c, "hud"));
		// структури: шаблон на рівному майданчику в небі, камера під кутом
		String[] structs = {"mage_tower", "ley_circle", "shrine", "aurora_hut", "academy_ruins", "dragon_sanctuary", "spirit_grove", "weaver_vault"};
		for (int i = 0; i < structs.length; i++) {
			final String s = structs[i];
			final int bx = 600 + i * 64;
			add(40, c -> {
				c.options.hudHidden = true;
				c.options.setPerspective(Perspective.FIRST_PERSON);
				cmd(c, "gamemode spectator @me");
				cmd(c, "tp @me " + (bx - 14) + " 230 -14");
			});
			add(40, c -> serverPlayer(c, p -> {
				net.minecraft.server.world.ServerWorld w = p.getServerWorld();
				var tm = w.getStructureTemplateManager().getTemplate(ua.pletinnya.Pletinnya.id(s));
				if (tm.isEmpty()) return;
				var t = tm.get();
				net.minecraft.util.math.Vec3i size = t.getSize();
				net.minecraft.util.math.BlockPos base = new net.minecraft.util.math.BlockPos(bx, 200, 0);
				for (int x = -6; x < size.getX() + 6; x++)
					for (int z = -6; z < size.getZ() + 6; z++)
						w.setBlockState(base.add(x, -1, z), (x + z) % 7 == 0 ? net.minecraft.block.Blocks.MOSS_BLOCK.getDefaultState() : net.minecraft.block.Blocks.GRASS_BLOCK.getDefaultState());
				t.place(w, base, base, new net.minecraft.structure.StructurePlacementData(), w.getRandom(), 2);
				double cx = bx + size.getX() / 2.0, cz = size.getZ() / 2.0;
				double dist = Math.max(size.getX(), size.getZ()) * 1.05 + 6;
				p.requestTeleport(cx - dist * 0.75, 200 + size.getY() * 0.75 + dist * 0.35, cz - dist * 0.75);
				p.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES, new net.minecraft.util.math.Vec3d(cx, 200 + size.getY() * 0.35, cz));
			}));
			add(120, c -> { });
			add(3, c -> shot(c, "structure_" + s));
		}
		add(5, c -> { cmd(c, "gamemode creative @me"); c.options.hudHidden = false; });
	}
}
