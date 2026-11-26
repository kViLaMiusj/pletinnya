package ua.pletinnya.client.armor;

import net.minecraft.client.model.*;

/** Згенеровано tools/art/armor_models5.py — одяг за мотивами Iron's Spells. Не редагувати вручну. */
public final class ArmorModels {
	private ArmorModels() {}

	public static final int TEX_W = 64, TEX_H = 64;

	private static boolean hasBrim(String set) {
		return switch (set) { case "lightning", "sound", "mana", "holy" -> true; default -> false; };
	}

	public static TexturedModelData create(String set) {
		ModelData md = new ModelData();
		ModelPartData root = md.getRoot();
		ModelPartData head = root.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData hat = root.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData body = root.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData right_arm = root.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
		ModelPartData left_arm = root.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		head.addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		body.addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		right_arm.addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		left_arm.addChild("c_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		body.addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		right_leg.addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		left_leg.addChild("l_leg", ModelPartBuilder.create().uv(0, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		right_leg.addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		left_leg.addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		if (hasBrim(set)) head.addChild("h_brim", ModelPartBuilder.create().uv(0, 48).cuboid(-7.0F, -6.2F, -7.0F, 14.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.NONE);
		return TexturedModelData.of(md, TEX_W, TEX_H);
	}

	/** Дочірні частини: {кістка, назва}. Префікс назви визначає слот (h_, c_, l_, f_). */
	public static String[][] children(String set) {
		return hasBrim(set) ? new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_brim"}} : new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}};
	}
}
