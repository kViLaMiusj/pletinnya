package ua.pletinnya.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ua.pletinnya.client.GuiIcons;

/** Як у ванільного тризуба: в інвентарі, на землі й у рамці посох показується пласкою іконкою, у руці — 3D-моделлю. */
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
		at = @At("HEAD"), argsOnly = true)
	private BakedModel pletinnya$flatIcon(BakedModel model, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ModelTransformationMode mode) {
		if (mode != ModelTransformationMode.GUI && mode != ModelTransformationMode.GROUND && mode != ModelTransformationMode.FIXED) return model;
		Identifier id = Registries.ITEM.getId(stack.getItem());
		if (!GuiIcons.HAS.contains(id)) return model;
		BakedModel flat = MinecraftClient.getInstance().getBakedModelManager().getModel(GuiIcons.model(id));
		return flat != null ? flat : model;
	}
}
