package ua.pletinnya.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.SpellCaster;
import ua.pletinnya.registry.ModAttachments;

import java.util.List;

/**
 * Посох-фокус. Не дає магії сам по собі, а лише допомагає плести:
 * зменшує ціну, розширює провідність і стабілізує складні формули.
 */
public class StaffItem extends Item {
	@org.jetbrains.annotations.Nullable public final ua.pletinnya.magic.MagicPath path;
	public final float costMul;
	public final int conductivityBonus;
	public final int stability;

	public StaffItem(float costMul, int conductivityBonus, int stability, Settings settings) {
		this(null, costMul, conductivityBonus, stability, settings);
	}

	public StaffItem(@org.jetbrains.annotations.Nullable ua.pletinnya.magic.MagicPath path, float costMul, int conductivityBonus, int stability, Settings settings) {
		super(settings);
		this.path = path;
		this.costMul = costMul;
		this.conductivityBonus = conductivityBonus;
		this.stability = stability;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient && user instanceof ServerPlayerEntity sp) {
			if (sp.isSneaking()) {
				MageData d = sp.getAttachedOrCreate(ModAttachments.MAGE);
				d.selected = Math.floorMod(d.selected + 1, MageData.SPELL_SLOTS);
				d.markDirty();
				String name = d.selectedSpell().isEmpty() ? "—" : d.selectedSpell().name();
				sp.sendMessage(Text.translatable("message.pletinnya.selected", d.selected + 1, name).formatted(Formatting.AQUA), true);
			} else {
				SpellCaster.castSelected(sp);
			}
		}
		return TypedActionResult.success(stack, world.isClient());
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (path != null) {
			tooltip.add(Text.translatable("tooltip.pletinnya.staff_path", path.displayName()).withColor(path.color));
			if (path.element != null) tooltip.add(Text.translatable("tooltip.pletinnya.staff_affinity").formatted(Formatting.GRAY));
			else tooltip.add(Text.translatable("tooltip.pletinnya.focus_" + path.id).formatted(Formatting.GRAY));
		}
		tooltip.add(Text.translatable("tooltip.pletinnya.staff_cost", Math.round((1f - costMul) * 100)).formatted(Formatting.BLUE));
		if (conductivityBonus > 0) tooltip.add(Text.translatable("tooltip.pletinnya.staff_conductivity", conductivityBonus).formatted(Formatting.BLUE));
		if (stability > 0) tooltip.add(Text.translatable("tooltip.pletinnya.staff_stability", stability).formatted(Formatting.BLUE));
		tooltip.add(Text.translatable("tooltip.pletinnya.staff_use").formatted(Formatting.DARK_GRAY));
	}
}
