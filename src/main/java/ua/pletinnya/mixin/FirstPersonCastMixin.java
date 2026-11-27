package ua.pletinnya.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.pletinnya.client.ClientAnims;

/** Від першої особи посох робить рух закляття: випад до цілі, здіймання, удар донизу. */
@Mixin(HeldItemRenderer.class)
public class FirstPersonCastMixin {
	private float[] pletinnya$off;

	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
	private void pletinnya$castHead(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swing, ItemStack item,
									float equip, MatrixStack m, VertexConsumerProvider vcp, int light, CallbackInfo ci) {
		pletinnya$off = hand == Hand.MAIN_HAND ? ClientAnims.firstPersonOffset(player, tickDelta) : null;
		if (pletinnya$off == null) return;
		m.translate(pletinnya$off[0], pletinnya$off[1], pletinnya$off[2]);
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pletinnya$off[3]));
	}

	@Inject(method = "renderFirstPersonItem", at = @At("RETURN"))
	private void pletinnya$castReturn(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swing, ItemStack item,
									  float equip, MatrixStack m, VertexConsumerProvider vcp, int light, CallbackInfo ci) {
		if (pletinnya$off == null) return;
		m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-pletinnya$off[3]));
		m.translate(-pletinnya$off[0], -pletinnya$off[1], -pletinnya$off[2]);
		pletinnya$off = null;
	}
}
