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
import ua.pletinnya.magic.DragonTongue;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.registry.ModAttachments;

import java.util.ArrayList;
import java.util.List;

/** Драконячий кодекс: уривки первісного Плетіння. Кожне прочитання відкриває кілька слів драконячої мови. */
public class DragonCodexItem extends Item {
	public DragonCodexItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			MageData d = user.getAttachedOrCreate(ModAttachments.MAGE);
			List<Integer> unknown = new ArrayList<>();
			for (int i = 0; i < DragonTongue.WORDS.length; i++) if (!d.paths.knowsWord(i)) unknown.add(i);
			if (unknown.isEmpty()) {
				user.sendMessage(Text.translatable("message.pletinnya.codex_known").formatted(Formatting.GOLD), true);
				return TypedActionResult.pass(stack);
			}
			if (!d.paths.knowsWord(DragonTongue.POWER_INDEX)) unknown.remove(Integer.valueOf(DragonTongue.POWER_INDEX));
			java.util.Collections.shuffle(unknown, new java.util.Random(user.getRandom().nextLong()));
			List<Integer> learn = new ArrayList<>(unknown.subList(0, Math.min(3, unknown.size())));
			if (!d.paths.knowsWord(DragonTongue.POWER_INDEX)) learn.add(DragonTongue.POWER_INDEX);
			if (d.paths.dragonWords == 0 && !learn.contains(DragonTongue.DEFAULT_FORM)) learn.add(DragonTongue.DEFAULT_FORM);
			user.sendMessage(Text.translatable("message.pletinnya.codex_read").formatted(Formatting.GOLD), false);
			for (int i : learn) {
				d.paths.dragonWords |= 1 << i;
				user.sendMessage(Text.literal("  " + DragonTongue.WORDS[i].word() + " — ").formatted(Formatting.YELLOW)
					.append(DragonTongue.meaning(i).copy().formatted(Formatting.WHITE)), false);
			}
			d.markDirty();
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.35f, 1.4f);
			stack.decrementUnlessCreative(1, user);
		}
		return TypedActionResult.success(stack, world.isClient());
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.pletinnya.dragon_codex").formatted(Formatting.GOLD));
	}
}
