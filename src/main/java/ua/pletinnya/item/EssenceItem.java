package ua.pletinnya.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ua.pletinnya.magic.Element;

import java.util.List;

/** Есенція напряму — згусток мани, налаштований на одне явище. */
public class EssenceItem extends Item {
	public final Element element;

	public EssenceItem(Element element, Settings settings) {
		super(settings);
		this.element = element;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.essence." + element.id).withColor(element.color));
	}
}
