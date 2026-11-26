package ua.pletinnya.registry;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.item.MageArmorItem;
import ua.pletinnya.magic.MagicPath;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Шістнадцять сетів магічного одягу. */
public final class ModArmor {
	private ModArmor() {}

	public static final Map<MagicPath, RegistryEntry<ArmorMaterial>> MATERIALS = new EnumMap<>(MagicPath.class);
	public static final Map<MagicPath, List<Item>> SETS = new EnumMap<>(MagicPath.class);
	public static final List<Item> ALL = new ArrayList<>();

	static {
		for (MagicPath path : MagicPath.values()) {
			boolean heavy = path == MagicPath.AURA || path == MagicPath.DRAGON;
			Map<ArmorItem.Type, Integer> def = new EnumMap<>(ArmorItem.Type.class);
			def.put(ArmorItem.Type.HELMET, heavy ? 3 : 2);
			def.put(ArmorItem.Type.CHESTPLATE, heavy ? 7 : 5);
			def.put(ArmorItem.Type.LEGGINGS, heavy ? 5 : 4);
			def.put(ArmorItem.Type.BOOTS, heavy ? 3 : 2);
			def.put(ArmorItem.Type.BODY, 5);
			ArmorMaterial mat = new ArmorMaterial(def, 22, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
				() -> Ingredient.ofItems(ModItems.MANA_CRYSTAL),
				List.of(new ArmorMaterial.Layer(Pletinnya.id(path.id + "_robes"))),
				heavy ? 1.5f : 0.5f, heavy ? 0.05f : 0f);
			MATERIALS.put(path, Registry.registerReference(Registries.ARMOR_MATERIAL, Pletinnya.id(path.id + "_robes"), mat));
		}
	}

	public static void init() {
		for (MagicPath path : MagicPath.values()) {
			List<Item> set = new ArrayList<>();
			Rarity rarity = path.element == null ? Rarity.RARE : Rarity.UNCOMMON;
			for (ArmorItem.Type type : new ArmorItem.Type[]{ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS}) {
				String slot = switch (type) {
					case HELMET -> "helmet";
					case CHESTPLATE -> "chestplate";
					case LEGGINGS -> "leggings";
					default -> "boots";
				};
				Item item = Registry.register(Registries.ITEM, Pletinnya.id(path.id + "_" + slot),
					new MageArmorItem(path, MATERIALS.get(path), type, new Item.Settings().maxDamage(type.getMaxDamage(28)).rarity(rarity)));
				set.add(item);
				ALL.add(item);
			}
			SETS.put(path, set);
		}
	}
}
