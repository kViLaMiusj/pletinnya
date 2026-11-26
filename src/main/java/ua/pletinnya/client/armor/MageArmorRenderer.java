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
			bone.getChild(bc[1]).visible = bc[1].startsWith(prefix);
		}
		ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, texture);
	}
}
