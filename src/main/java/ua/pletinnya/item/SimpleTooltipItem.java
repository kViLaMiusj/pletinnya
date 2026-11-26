package ua.pletinnya.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/** Предмет із підказкою (святий символ, печатка угоди тощо). */
public class SimpleTooltipItem extends Item {
	private final String key;
	private final boolean glint;

	public SimpleTooltipItem(String key, boolean glint, Settings settings) {
		super(settings);
		this.key = key;
		this.glint = glint;
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return glint || super.hasGlint(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya." + key).formatted(Formatting.GRAY));
	}
}
