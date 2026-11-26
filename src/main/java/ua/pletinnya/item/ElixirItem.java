package ua.pletinnya.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.registry.ModAttachments;

import java.util.List;

/** Еліксири: відновлення мани або загоєння пошкоджених каналів. */
public class ElixirItem extends Item {
	public enum Kind { MANA, CHANNELS, FAITH }

	private final Kind kind;

	public ElixirItem(Kind kind, Settings settings) {
		super(settings);
		this.kind = kind;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 32;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient && user instanceof PlayerEntity player) {
			MageData d = player.getAttachedOrCreate(ModAttachments.MAGE);
			if (kind == Kind.MANA) {
				d.mana = Math.min(d.maxMana, d.mana + 100);
			} else if (kind == Kind.FAITH) {
				d.paths.faith = Math.min(100f, d.paths.faith + 40f);
			} else {
				d.channelDamage = 0;
				player.heal(4f);
				player.sendMessage(Text.translatable("message.pletinnya.channels_healed").formatted(Formatting.GREEN), true);
			}
			d.markDirty();
		}
		boolean creative = user instanceof PlayerEntity pl && pl.isCreative();
		stack.decrementUnlessCreative(1, user);
		if (creative) return stack;
		if (stack.isEmpty()) return new ItemStack(Items.GLASS_BOTTLE);
		if (user instanceof PlayerEntity pl) pl.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
		return stack;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.elixir_" + kind.name().toLowerCase()).formatted(Formatting.GRAY));
	}
}
