package ua.pletinnya.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.registry.ModAttachments;

import java.util.List;

/** Кристал мани: зовнішня мана, зібрана й застигла. Можна поглинути. */
public class ManaCrystalItem extends Item {
	public static final int MANA = 30;

	public ManaCrystalItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			MageData d = user.getAttachedOrCreate(ModAttachments.MAGE);
			if (d.mana >= d.maxMana) return TypedActionResult.pass(stack);
			d.mana = Math.min(d.maxMana, d.mana + MANA);
			d.markDirty();
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1f, 1.3f);
			stack.decrementUnlessCreative(1, user);
		}
		return TypedActionResult.success(stack, world.isClient());
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.mana_crystal", MANA).formatted(Formatting.BLUE));
	}
}
