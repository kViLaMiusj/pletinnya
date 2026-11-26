package ua.pletinnya.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/** Клинок аури: меч, крізь який боєць спрямовує внутрішнє Плетіння. Подвоює силу ударів аури. */
public class AuraBladeItem extends SwordItem {
	public AuraBladeItem(ToolMaterial material, Settings settings) {
		super(material, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.aura_blade").formatted(Formatting.GOLD));
		tooltip.add(Text.translatable("tooltip.pletinnya.aura_blade2").formatted(Formatting.GRAY));
	}
}
