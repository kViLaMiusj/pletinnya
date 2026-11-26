package ua.pletinnya.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;
import ua.pletinnya.Pletinnya;

import java.util.HashSet;
import java.util.Set;

/** Пласкі іконки для предметів із 3D-моделями (посохи, клинок аури). */
public final class GuiIcons {
	private GuiIcons() {}

	public static final String[] IDS = {"staff_water", "staff_fire", "staff_wind", "staff_earth", "staff_lightning", "staff_ice", "staff_nature",
		"staff_sound", "staff_light", "staff_darkness", "staff_blood", "staff_mana", "dragon_sceptre", "spirit_lantern", "holy_sceptre", "aura_blade",
		"apprentice_staff", "weaver_staff", "architect_staff"};
	public static final Set<Identifier> HAS = new HashSet<>();

	static {
		for (String s : IDS) HAS.add(Pletinnya.id(s));
	}

	public static Identifier model(Identifier item) {
		return Identifier.of(item.getNamespace(), "item/" + item.getPath() + "_gui");
	}

	public static void register() {
		ModelLoadingPlugin.register(ctx -> {
			for (Identifier id : HAS) ctx.addModels(model(id));
		});
	}
}
