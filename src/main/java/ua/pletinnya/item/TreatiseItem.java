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
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.Ranks;
import ua.pletinnya.registry.ModAttachments;

import java.util.List;

/**
 * Трактат. Закон розуміння: щоб змінювати формули, треба розуміти процес.
 * Прочитаний трактат поглиблює розуміння напряму.
 */
public class TreatiseItem extends Item {
	public static final int XP = 250;
	@Nullable public final Element element;

	public TreatiseItem(@Nullable Element element, Settings settings) {
		super(settings);
		this.element = element;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			MageData d = user.getAttachedOrCreate(ModAttachments.MAGE);
			if (element != null) {
				d.addXp(element, XP);
				user.sendMessage(Text.translatable("message.pletinnya.treatise_read", element.displayName(), d.mastery(element)).formatted(Formatting.AQUA), false);
			} else {
				d.precision = Math.min(100, d.precision + 2);
				d.perception = Math.min(100, d.perception + 2);
				d.maxMana = Math.min(Ranks.manaCap(d.rank), d.maxMana + 10);
				user.sendMessage(Text.translatable("message.pletinnya.treatise_basics").formatted(Formatting.AQUA), false);
			}
			d.markDirty();
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1f, 1f);
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.6f, 1.2f);
			stack.decrementUnlessCreative(1, user);
		}
		return TypedActionResult.success(stack, world.isClient());
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (element != null) {
			tooltip.add(Text.translatable("tooltip.pletinnya.treatise", element.displayName()).withColor(element.color));
		} else {
			tooltip.add(Text.translatable("tooltip.pletinnya.treatise_basics").formatted(Formatting.GRAY));
		}
	}
}
