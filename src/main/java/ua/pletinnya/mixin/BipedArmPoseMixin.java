package ua.pletinnya.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.pletinnya.client.GuiIcons;

/** Поза мага з посохом: рука з посохом винесена вперед, кисть охоплює посох упоперек (посох перпендикулярний руці). */
@Mixin(BipedEntityModel.class)
public abstract class BipedArmPoseMixin {
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart leftArm;
	@Shadow public BipedEntityModel.ArmPose rightArmPose;
	@Shadow public BipedEntityModel.ArmPose leftArmPose;

	private static final float STAFF_RAISE = -0.62F;

	private static boolean pletinnya$isStaff(ItemStack s) {
		return !s.isEmpty() && GuiIcons.HAS.contains(Registries.ITEM.getId(s.getItem()));
	}

	private static ItemStack pletinnya$stackFor(LivingEntity e, Arm arm) {
		return e.getMainArm() == arm ? e.getMainHandStack() : e.getOffHandStack();
	}

	@Inject(method = "positionRightArm", at = @At("TAIL"))
	private void pletinnya$staffRight(LivingEntity entity, CallbackInfo ci) {
		if (rightArmPose == BipedEntityModel.ArmPose.ITEM && pletinnya$isStaff(pletinnya$stackFor(entity, Arm.RIGHT))) {
			rightArm.pitch += STAFF_RAISE;
			rightArm.yaw = -0.12F;
		}
	}

	@Inject(method = "positionLeftArm", at = @At("TAIL"))
	private void pletinnya$staffLeft(LivingEntity entity, CallbackInfo ci) {
		if (leftArmPose == BipedEntityModel.ArmPose.ITEM && pletinnya$isStaff(pletinnya$stackFor(entity, Arm.LEFT))) {
			leftArm.pitch += STAFF_RAISE;
			leftArm.yaw = 0.12F;
		}
	}
}
