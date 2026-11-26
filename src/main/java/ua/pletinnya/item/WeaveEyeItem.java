package ua.pletinnya.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/** Око Плетіння: лінза, крізь яку видно чужі нитки мани (+40 до сприйняття, поки в руці). */
public class WeaveEyeItem extends Item {
	public WeaveEyeItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.weave_eye").formatted(Formatting.LIGHT_PURPLE));
	}
}
