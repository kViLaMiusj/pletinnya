package ua.pletinnya.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.item.AuraBladeItem;
import ua.pletinnya.item.DragonCodexItem;
import ua.pletinnya.item.ElixirItem;
import ua.pletinnya.item.EssenceItem;
import ua.pletinnya.item.ManaCrystalItem;
import ua.pletinnya.item.SimpleTooltipItem;
import ua.pletinnya.item.StaffItem;
import ua.pletinnya.item.TreatiseItem;
import ua.pletinnya.item.WeaveEyeItem;
import ua.pletinnya.item.WeavingTabletItem;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MagicPath;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ModItems {
	private ModItems() {}

	private static final List<Item> ALL = new ArrayList<>();
	private static final Map<MagicPath, Item> ESSENCES = new EnumMap<>(MagicPath.class);
	private static final Map<Element, Item> TREATISES = new EnumMap<>(Element.class);
	public static final Map<MagicPath, Item> FOCI = new EnumMap<>(MagicPath.class);

	public static final Item MANA_CRYSTAL = register("mana_crystal", new ManaCrystalItem(new Item.Settings()));
	public static final Item WEAVING_TABLET = register("weaving_tablet", new WeavingTabletItem(new Item.Settings().maxCount(1)));
	public static final Item APPRENTICE_STAFF = register("apprentice_staff", new StaffItem(0.9f, 0, 2, new Item.Settings().maxCount(1)));
	public static final Item WEAVER_STAFF = register("weaver_staff", new StaffItem(0.8f, 15, 5, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
	public static final Item ARCHITECT_STAFF = register("architect_staff", new StaffItem(0.7f, 40, 9, new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
	public static final Item WEAVE_EYE = register("weave_eye", new WeaveEyeItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
	public static final Item MANA_ELIXIR = register("mana_elixir", new ElixirItem(ElixirItem.Kind.MANA, new Item.Settings().maxCount(16)));
	public static final Item CHANNEL_ELIXIR = register("channel_elixir", new ElixirItem(ElixirItem.Kind.CHANNELS, new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));
	public static final Item FAITH_ELIXIR = register("faith_elixir", new ElixirItem(ElixirItem.Kind.FAITH, new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));
	public static final Item TREATISE_BASICS = register("treatise_basics", new TreatiseItem(null, new Item.Settings().maxCount(16)));
	public static final Item DRAGON_CODEX = register("dragon_codex", new DragonCodexItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE)));
	public static final Item HOLY_SYMBOL = register("holy_symbol", new SimpleTooltipItem("holy_symbol", false, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
	public static final Item SPIRIT_SEAL = register("spirit_seal", new SimpleTooltipItem("spirit_seal", true, new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));

	static {
		for (MagicPath p : MagicPath.values()) {
			ESSENCES.put(p, register("essence_" + p.id, new EssenceItem(p, new Item.Settings())));
		}
		for (Element e : Element.values()) {
			TREATISES.put(e, register("treatise_" + e.id, new TreatiseItem(e, new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON))));
		}
		for (MagicPath p : MagicPath.values()) {
			if (p.element == null) continue;
			FOCI.put(p, register("staff_" + p.id, new StaffItem(p, 0.85f, 20, 4, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON))));
		}
		FOCI.put(MagicPath.DRAGON, register("dragon_sceptre", new StaffItem(MagicPath.DRAGON, 0.85f, 60, 2, new Item.Settings().maxCount(1).rarity(Rarity.EPIC))));
		FOCI.put(MagicPath.SPIRIT, register("spirit_lantern", new StaffItem(MagicPath.SPIRIT, 0.8f, 20, 10, new Item.Settings().maxCount(1).rarity(Rarity.RARE))));
		FOCI.put(MagicPath.HOLY, register("holy_sceptre", new StaffItem(MagicPath.HOLY, 0.85f, 20, 6, new Item.Settings().maxCount(1).rarity(Rarity.RARE))));
	}

	public static final Item AURA_BLADE = register("aura_blade", new AuraBladeItem(ToolMaterials.DIAMOND,
		new Item.Settings().rarity(Rarity.RARE).attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.2f))));

	public static final Item MANA_CRYSTAL_ORE = block(ModBlocks.MANA_CRYSTAL_ORE);
	public static final Item DEEPSLATE_MANA_CRYSTAL_ORE = block(ModBlocks.DEEPSLATE_MANA_CRYSTAL_ORE);
	public static final Item MANA_CRYSTAL_BLOCK = block(ModBlocks.MANA_CRYSTAL_BLOCK);
	public static final Item MANA_CRYSTAL_CLUSTER = block(ModBlocks.MANA_CRYSTAL_CLUSTER);
	public static final Item RUNE_STONE = block(ModBlocks.RUNE_STONE);
	public static final Item RUNE_STONE_BRICKS = block(ModBlocks.RUNE_STONE_BRICKS);
	public static final Item CHISELED_RUNE_STONE = block(ModBlocks.CHISELED_RUNE_STONE);
	public static final Item MANA_GLASS = block(ModBlocks.MANA_GLASS);
	public static final Item CRYSTAL_LAMP = block(ModBlocks.CRYSTAL_LAMP);
	public static final Item MANA_NODE = block(ModBlocks.MANA_NODE);
	public static final Item HOLY_ALTAR = block(ModBlocks.HOLY_ALTAR);
	public static final Item SPIRIT_SHRINE = block(ModBlocks.SPIRIT_SHRINE);

	public static ItemGroup GROUP;

	public static Item essence(Element e) {
		return ESSENCES.get(MagicPath.of(e));
	}

	public static Item essence(MagicPath p) {
		return ESSENCES.get(p);
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

	public static void init() {
		ModArmor.init();
		GROUP = Registry.register(Registries.ITEM_GROUP, Pletinnya.id("main"),
			FabricItemGroup.builder()
				.icon(() -> new ItemStack(FOCI.get(MagicPath.MANA)))
				.displayName(Text.translatable("itemGroup.pletinnya"))
				.entries((context, entries) -> {
					for (Item item : ALL) entries.add(item);
				})
				.build());
		Registry.register(Registries.ITEM_GROUP, Pletinnya.id("robes"),
			FabricItemGroup.builder()
				.icon(() -> new ItemStack(ModArmor.SETS.get(MagicPath.MANA).get(0)))
				.displayName(Text.translatable("itemGroup.pletinnya.robes"))
				.entries((context, entries) -> {
					for (Item item : ModArmor.ALL) entries.add(item);
				})
				.build());
	}
}
