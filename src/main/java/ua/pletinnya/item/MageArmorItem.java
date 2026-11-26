package ua.pletinnya.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ua.pletinnya.magic.MagicPath;

import java.util.List;

/** Магічний одяг шляху. Кожна частина здешевлює плетіння свого напряму, повний сет дає особливу властивість. */
public class MageArmorItem extends ArmorItem {
	public final MagicPath path;

	public MageArmorItem(MagicPath path, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
		super(material, type, settings);
		this.path = path;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.set", path.displayName()).withColor(path.color));
		if (path.element != null) tooltip.add(Text.translatable("tooltip.pletinnya.set_piece", path.displayName()).formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("tooltip.pletinnya.set_full").formatted(Formatting.DARK_AQUA));
		tooltip.add(Text.literal("  ").append(Text.translatable("set.pletinnya." + path.id)).formatted(Formatting.AQUA));
	}
}
