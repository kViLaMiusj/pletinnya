package ua.pletinnya.client.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.magic.MagicPath;
import ua.pletinnya.registry.ModArmor;

/** Малює магічний одяг власними 3D-моделями: каптури, капелюхи, мантії, табарди, плащі. */
public class MageArmorRenderer implements ArmorRenderer {
	private final String set;
	private final EntityModelLayer layer;
	private final Identifier texture;
	private BipedEntityModel<LivingEntity> model;

	public MageArmorRenderer(String set) {
		this.set = set;
		this.layer = new EntityModelLayer(Pletinnya.id("robes_" + set), "main");
		this.texture = Pletinnya.id("textures/entity/armor/" + set + ".png");
	}

	public static void registerAll() {
		for (MagicPath path : MagicPath.values()) {
			MageArmorRenderer r = new MageArmorRenderer(path.id);
			EntityModelLayerRegistry.registerModelLayer(r.layer, () -> ArmorModels.create(path.id));
			ArmorRenderer.register(r, ModArmor.SETS.get(path).toArray(new Item[0]));
		}
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity,
					   EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		if (model == null) model = new BipedEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer));
		contextModel.copyBipedStateTo(model);
		model.setVisible(true);
		String prefix = switch (slot) {
			case HEAD -> "h_";
			case CHEST -> "c_";
			case LEGS -> "l_";
			default -> "f_";
		};
		float td = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
		float age = entity.age + td;
		float spd = Math.min(1f, entity.limbAnimator.getSpeed(td));
		for (String[] bc : ArmorModels.children(set)) {
			ModelPart bone = switch (bc[0]) {
				case "head" -> model.head;
				case "body" -> model.body;
				case "right_arm" -> model.rightArm;
				case "left_arm" -> model.leftArm;
				case "right_leg" -> model.rightLeg;
				case "left_leg" -> model.leftLeg;
				default -> model.hat;
			};
			ModelPart part = bone.getChild(bc[1]);
			part.visible = bc[1].startsWith(prefix);
			if (part.visible) animate(bc[1], part, age, spd, entity.isInSneakingPose());
		}
		ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, texture);
	}

	/** Жива броня: плащі й шарфи відлітають від руху, камені й кристали ширяють, ліхтарі й дзвіночки гойдаються. */
	private static void animate(String name, ModelPart p, float age, float spd, boolean sneak) {
		p.resetTransform();
		int k = name.hashCode() & 7;
		if (name.equals("c_cape")) {
			p.pitch += spd * 0.85f + 0.045f * MathHelper.sin(age * 0.11f) + (sneak ? 0.35f : 0f);
			p.roll += 0.03f * MathHelper.sin(age * 0.08f);
		} else if (name.startsWith("c_tail")) {
			p.pitch += spd * 0.45f + 0.14f * MathHelper.sin(age * 0.33f + k);
			p.yaw += 0.12f * MathHelper.sin(age * 0.26f + k * 1.3f);
		} else if (name.startsWith("c_wing")) {
			float flap = 0.07f * MathHelper.sin(age * 0.09f) + spd * 0.12f;
			p.yaw += name.endsWith("_l") ? flap : -flap;
		} else if (name.startsWith("c_float") || name.equals("c_orb")) {
			p.pivotY += MathHelper.sin(age * 0.08f + k) * 1.1f;
			p.yaw += age * 0.04f;
			p.pitch += age * 0.023f;
		} else if (name.startsWith("c_lantern")) {
			p.pitch += MathHelper.sin(age * 0.14f) * 0.1f + spd * 0.35f * MathHelper.sin(age * 0.6f);
		} else if (name.startsWith("c_bell") || name.startsWith("c_vial")) {
			p.roll += MathHelper.sin(age * 0.3f + k) * 0.12f * (0.3f + spd);
		} else if (name.endsWith("ofuda")) {
			p.pitch += -spd * 0.35f + 0.05f * MathHelper.sin(age * 0.1f + k);
		} else if (name.equals("h_feather")) {
			p.roll += 0.06f * MathHelper.sin(age * 0.17f);
		} else if (name.startsWith("h_flap")) {
			p.roll += (name.endsWith("_l") ? 1 : -1) * 0.08f * MathHelper.sin(age * 0.5f) * spd;
		}
	}
}
