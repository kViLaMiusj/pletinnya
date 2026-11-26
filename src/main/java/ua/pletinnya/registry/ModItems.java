package ua.pletinnya.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.item.ElixirItem;
import ua.pletinnya.item.EssenceItem;
import ua.pletinnya.item.ManaCrystalItem;
import ua.pletinnya.item.StaffItem;
import ua.pletinnya.item.TreatiseItem;
import ua.pletinnya.item.WeaveEyeItem;
import ua.pletinnya.item.WeavingTabletItem;
import ua.pletinnya.magic.Element;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ModItems {
	private ModItems() {}

	private static final List<Item> ALL = new ArrayList<>();
	private static final Map<Element, Item> ESSENCES = new EnumMap<>(Element.class);
	private static final Map<Element, Item> TREATISES = new EnumMap<>(Element.class);

	public static final Item MANA_CRYSTAL = register("mana_crystal", new ManaCrystalItem(new Item.Settings()));
	public static final Item WEAVING_TABLET = register("weaving_tablet", new WeavingTabletItem(new Item.Settings().maxCount(1)));
	public static final Item APPRENTICE_STAFF = register("apprentice_staff", new StaffItem(0.9f, 0, 2, new Item.Settings().maxCount(1)));
	public static final Item WEAVER_STAFF = register("weaver_staff", new StaffItem(0.8f, 15, 5, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
	public static final Item ARCHITECT_STAFF = register("architect_staff", new StaffItem(0.7f, 40, 9, new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
	public static final Item WEAVE_EYE = register("weave_eye", new WeaveEyeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
	public static final Item MANA_ELIXIR = register("mana_elixir", new ElixirItem(ElixirItem.Kind.MANA, new Item.Settings().maxCount(16)));
	public static final Item CHANNEL_ELIXIR = register("channel_elixir", new ElixirItem(ElixirItem.Kind.CHANNELS, new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));
	public static final Item TREATISE_BASICS = register("treatise_basics", new TreatiseItem(null, new Item.Settings().maxCount(16)));

	static {
		for (Element e : Element.values()) {
			ESSENCES.put(e, register("essence_" + e.id, new EssenceItem(e, new Item.Settings())));
		}
		for (Element e : Element.values()) {
			TREATISES.put(e, register("treatise_" + e.id, new TreatiseItem(e, new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON))));
		}
	}

	public static final Item MANA_CRYSTAL_ORE = block(ModBlocks.MANA_CRYSTAL_ORE);
	public static final Item DEEPSLATE_MANA_CRYSTAL_ORE = block(ModBlocks.DEEPSLATE_MANA_CRYSTAL_ORE);
	public static final Item MANA_CRYSTAL_BLOCK = block(ModBlocks.MANA_CRYSTAL_BLOCK);
	public static final Item RUNE_STONE = block(ModBlocks.RUNE_STONE);
	public static final Item MANA_NODE = block(ModBlocks.MANA_NODE);

	public static final ItemGroup GROUP = Registry.register(Registries.ITEM_GROUP, Pletinnya.id("main"),
		FabricItemGroup.builder()
			.icon(() -> new ItemStack(MANA_CRYSTAL))
			.displayName(Text.translatable("itemGroup.pletinnya"))
			.entries((context, entries) -> {
				for (Item item : ALL) entries.add(item);
			})
			.build());

	public static Item essence(Element e) {
		return ESSENCES.get(e);
	}

	public static Item treatise(Element e) {
		return TREATISES.get(e);
	}

	private static Item register(String name, Item item) {
		Item registered = Registry.register(Registries.ITEM, Pletinnya.id(name), item);
		ALL.add(registered);
		return registered;
	}

	private static Item block(Block block) {
		return register(Registries.BLOCK.getId(block).getPath(), new BlockItem(block, new Item.Settings()));
	}

	public static void init() {}
}
