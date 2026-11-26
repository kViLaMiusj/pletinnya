package ua.pletinnya.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MagicPath;

import java.util.List;

/** Есенція шляху — згусток мани, налаштований на одне явище. */
public class EssenceItem extends Item {
	public final MagicPath path;
	public final Element element;

	public EssenceItem(MagicPath path, Settings settings) {
		super(settings);
		this.path = path;
		this.element = path.element != null ? path.element : Element.MANA;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.essence." + path.id).withColor(path.color));
	}
}
