package ua.pletinnya.client.armor;

import net.minecraft.client.model.*;

import java.util.ArrayList;
import java.util.List;

/** Згенеровано tools/art/armor_models10.py — вбрання магів за мотивами Iron's Spells. Не редагувати вручну. */
public final class ArmorModels {
	private ArmorModels() {}

	public static TexturedModelData create(String set) {
		return switch (set) {
			case "water" -> water();
			case "fire" -> fire();
			case "lightning" -> lightning();
			case "ice" -> ice();
			case "darkness" -> darkness();
			case "blood" -> blood();
			case "holy" -> holy();
			case "light" -> light();
			case "nature" -> nature();
			case "aura" -> aura();
			case "earth" -> earth();
			case "mana" -> mana();
			case "dragon" -> dragon();
			case "spirit" -> spirit();
			case "sound" -> sound();
			case "wind" -> wind();
			default -> throw new IllegalArgumentException(set);
		};
	}

	public static String[][] children(String set) {
		return switch (set) {
			case "water" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"body", "c_cape"}};
			case "fire" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"body", "c_cape"}};
			case "lightning" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_cone"}, {"head", "h_brim"}, {"body", "c_collar"}, {"right_arm", "c_coil"}, {"left_arm", "c_coil"}, {"right_arm", "c_coil2"}, {"left_arm", "c_coil2"}, {"right_arm", "c_glove"}, {"left_arm", "c_glove"}};
			case "ice" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_horn_l"}, {"head", "h_horn_r"}, {"head", "h_spike"}, {"body", "c_mantle"}, {"right_arm", "c_shard"}, {"left_arm", "c_shard"}, {"right_arm", "c_spk"}, {"left_arm", "c_spk"}, {"body", "c_cape"}};
			case "darkness" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_crown"}, {"head", "h_brim"}, {"body", "c_vial0"}, {"body", "c_vial1"}, {"body", "c_vial2"}, {"body", "c_cape"}};
			case "blood" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_horn_l"}, {"head", "h_horn_r"}, {"right_arm", "c_bone"}, {"left_arm", "c_bone"}, {"right_arm", "c_bspike"}, {"left_arm", "c_bspike"}, {"body", "c_cape"}};
			case "holy" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_cap"}, {"head", "h_brim"}, {"head", "h_halo0"}, {"head", "h_halo1"}, {"head", "h_halo2"}, {"head", "h_halo3"}, {"right_arm", "c_shoulder"}, {"left_arm", "c_shoulder"}};
			case "light" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_sun"}, {"right_arm", "c_pauldron"}, {"left_arm", "c_pauldron"}, {"body", "c_cape"}};
			case "nature" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_beak"}, {"head", "h_leaf0"}, {"head", "h_leaf1"}, {"head", "h_leaf2"}, {"right_arm", "c_mush"}, {"left_arm", "c_leafpad"}, {"body", "c_cape"}};
			case "aura" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_crest"}, {"head", "h_crest2"}, {"right_arm", "c_pauldron"}, {"left_arm", "c_pauldron"}, {"right_arm", "c_pauldron2"}, {"left_arm", "c_pauldron2"}, {"body", "c_cape"}};
			case "earth" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"right_arm", "c_rock"}, {"left_arm", "c_rock"}, {"right_arm", "c_moss"}, {"left_arm", "c_moss"}, {"body", "c_float"}, {"body", "c_float2"}, {"body", "c_cape"}};
			case "mana" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_cone"}, {"head", "h_brim"}, {"body", "c_orb"}};
			case "dragon" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_horn_l"}, {"head", "h_horn_r"}, {"right_arm", "c_scale"}, {"left_arm", "c_scale"}, {"body", "c_wing_l"}, {"body", "c_wing_r"}};
			case "spirit" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_ofuda"}, {"body", "c_lantern"}, {"body", "c_lantern_t"}, {"body", "c_lantern_b"}, {"right_arm", "c_ofuda"}, {"left_arm", "c_ofuda"}, {"body", "c_cape"}};
			case "sound" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_crown"}, {"head", "h_brim"}, {"head", "h_feather"}, {"body", "c_bell0"}, {"body", "c_bell1"}, {"body", "c_bell2"}, {"body", "c_cape"}};
			case "wind" -> new String[][]{{"head", "h_helm"}, {"body", "c_body"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"body", "c_tail1"}, {"body", "c_tail2"}, {"right_arm", "c_feather"}, {"left_arm", "c_feather"}, {"right_arm", "c_feather2"}, {"left_arm", "c_feather2"}};
			default -> new String[0][];
		};
	}

	private static ModelPartData[] bones(ModelData md) {
		ModelPartData root = md.getRoot();
		ModelPartData head = root.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData hat = root.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData body = root.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData right_arm = root.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
		ModelPartData left_arm = root.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		return new ModelPartData[]{head, hat, body, right_arm, left_arm, right_leg, left_leg};
	}

	private static TexturedModelData water() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(0, 64).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 64, 96);
	}

	private static TexturedModelData fire() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(0, 64).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 64, 96);
	}

	private static TexturedModelData lightning() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_cone", ModelPartBuilder.create().uv(64, 17).cuboid(-3.5F, -12.5F, -3.5F, 7.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = x0.addChild("h_cone1", ModelPartBuilder.create().uv(92, 17).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.2F, 0.3F, -0.256F, 0.0F, 0.05F));
		ModelPartData x2 = x1.addChild("h_cone2", ModelPartBuilder.create().uv(104, 28).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.7F, 0.0F, -0.44799999999999995F, 0.0F, 0.05F));
		ModelPartData x3 = x2.addChild("h_cone3", ModelPartBuilder.create().uv(84, 41).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.5760000000000001F, 0.0F, 0.0F));
		ModelPartData x4 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(64, 0).cuboid(-8.0F, -5.2F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_collar", ModelPartBuilder.create().uv(92, 41).cuboid(-4.5F, -3.5F, 1.6F, 9.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[3].addChild("c_coil", ModelPartBuilder.create().uv(64, 35).cuboid(-3.5F, 3.0F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[4].addChild("c_coil", ModelPartBuilder.create().uv(84, 35).mirrored().cuboid(-1.5F, 3.0F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[3].addChild("c_coil2", ModelPartBuilder.create().uv(104, 35).cuboid(-3.5F, 5.0F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x9 = b[4].addChild("c_coil2", ModelPartBuilder.create().uv(64, 41).mirrored().cuboid(-1.5F, 5.0F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x10 = b[3].addChild("c_glove", ModelPartBuilder.create().uv(64, 28).cuboid(-3.5F, 7.5F, -2.5F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x11 = b[4].addChild("c_glove", ModelPartBuilder.create().uv(84, 28).mirrored().cuboid(-1.5F, 7.5F, -2.5F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData ice() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_horn_l", ModelPartBuilder.create().uv(104, 20).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -8.6F, 0.5F, -0.1F, 0.0F, 0.45F));
		ModelPartData x1 = x0.addChild("h_horn_l2", ModelPartBuilder.create().uv(112, 20).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, -0.35F, 0.0F, 0.1F));
		ModelPartData x2 = x1.addChild("h_horn_l3", ModelPartBuilder.create().uv(84, 27).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, -0.35F, 0.0F, 0.1F));
		ModelPartData x3 = b[0].addChild("h_horn_r", ModelPartBuilder.create().uv(120, 20).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -8.6F, 0.5F, -0.1F, 0.0F, -0.45F));
		ModelPartData x4 = x3.addChild("h_horn_r2", ModelPartBuilder.create().uv(64, 27).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, -0.35F, 0.0F, -0.1F));
		ModelPartData x5 = x4.addChild("h_horn_r3", ModelPartBuilder.create().uv(88, 27).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, -0.35F, 0.0F, -0.1F));
		ModelPartData x6 = b[0].addChild("h_spike", ModelPartBuilder.create().uv(72, 27).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.8F, -4.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[2].addChild("c_mantle", ModelPartBuilder.create().uv(86, 0).cuboid(-5.0F, -0.6F, -3.0F, 10.0F, 3.0F, 6.0F, new Dilation(0.15F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[3].addChild("c_shard", ModelPartBuilder.create().uv(64, 20).cuboid(-3.6F, -3.2F, -2.6F, 5.0F, 2.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x9 = b[4].addChild("c_shard", ModelPartBuilder.create().uv(84, 20).mirrored().cuboid(-1.4F, -3.2F, -2.6F, 5.0F, 2.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x10 = b[3].addChild("c_spk", ModelPartBuilder.create().uv(76, 27).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -3.0F, 0.0F, 0.0F, 0.0F, 0.35F));
		ModelPartData x11 = b[4].addChild("c_spk", ModelPartBuilder.create().uv(80, 27).mirrored().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -3.0F, 0.0F, 0.0F, 0.0F, -0.35F));
		ModelPartData x12 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData darkness() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_crown", ModelPartBuilder.create().uv(64, 37).cuboid(-3.5F, -11.5F, -3.5F, 7.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(64, 20).cuboid(-8.0F, -9.0F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[2].addChild("c_vial0", ModelPartBuilder.create().uv(92, 37).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.6F, 2.2F, -3.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[2].addChild("c_vial1", ModelPartBuilder.create().uv(96, 37).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.1F, 3.6F, -3.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[2].addChild("c_vial2", ModelPartBuilder.create().uv(100, 37).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.4F, 5.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData blood() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_horn_l", ModelPartBuilder.create().uv(64, 20).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.4F, -8.4F, 0.5F, 0.25F, 0.0F, 0.45F));
		ModelPartData x1 = x0.addChild("h_horn_l2", ModelPartBuilder.create().uv(72, 20).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, 0.1F));
		ModelPartData x2 = x1.addChild("h_horn_l3", ModelPartBuilder.create().uv(104, 20).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, 0.1F));
		ModelPartData x3 = b[0].addChild("h_horn_r", ModelPartBuilder.create().uv(80, 20).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.4F, -8.4F, 0.5F, 0.25F, 0.0F, -0.45F));
		ModelPartData x4 = x3.addChild("h_horn_r2", ModelPartBuilder.create().uv(88, 20).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, -0.1F));
		ModelPartData x5 = x4.addChild("h_horn_r3", ModelPartBuilder.create().uv(108, 20).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, -0.1F));
		ModelPartData x6 = b[3].addChild("c_bone", ModelPartBuilder.create().uv(86, 0).cuboid(-3.6F, -3.2F, -2.6F, 5.0F, 2.0F, 5.0F, new Dilation(0.05F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[4].addChild("c_bone", ModelPartBuilder.create().uv(106, 0).mirrored().cuboid(-1.4F, -3.2F, -2.6F, 5.0F, 2.0F, 5.0F, new Dilation(0.05F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[3].addChild("c_bspike", ModelPartBuilder.create().uv(96, 20).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.6F, -3.0F, 0.0F, 0.0F, 0.0F, 0.4F));
		ModelPartData x9 = b[4].addChild("c_bspike", ModelPartBuilder.create().uv(100, 20).mirrored().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.6F, -3.0F, 0.0F, 0.0F, 0.0F, -0.4F));
		ModelPartData x10 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData holy() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_cap", ModelPartBuilder.create().uv(64, 15).cuboid(-4.5F, -12.5F, -4.5F, 9.0F, 4.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(64, 0).cuboid(-7.0F, -6.0F, -7.0F, 14.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_halo0", ModelPartBuilder.create().uv(104, 28).cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[0].addChild("h_halo1", ModelPartBuilder.create().uv(64, 34).cuboid(-4.0F, 0.0F, 3.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[0].addChild("h_halo2", ModelPartBuilder.create().uv(100, 15).cuboid(-4.0F, 0.0F, -3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[0].addChild("h_halo3", ModelPartBuilder.create().uv(114, 15).cuboid(3.0F, 0.0F, -3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[3].addChild("c_shoulder", ModelPartBuilder.create().uv(64, 28).cuboid(-3.6F, -2.6F, -2.6F, 5.0F, 1.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[4].addChild("c_shoulder", ModelPartBuilder.create().uv(84, 28).mirrored().cuboid(-1.4F, -2.6F, -2.6F, 5.0F, 1.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData light() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_sun", ModelPartBuilder.create().uv(86, 0).cuboid(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.5F, 5.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[3].addChild("c_pauldron", ModelPartBuilder.create().uv(64, 20).cuboid(-3.8F, -3.2F, -2.8F, 5.0F, 3.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[4].addChild("c_pauldron", ModelPartBuilder.create().uv(86, 20).mirrored().cuboid(-1.2000000000000002F, -3.2F, -2.8F, 5.0F, 3.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData nature() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_beak", ModelPartBuilder.create().uv(86, 0).cuboid(-1.5F, 0.0F, -6.0F, 3.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.2F, -4.6F, 0.3F, 0.0F, 0.0F));
		ModelPartData x1 = x0.addChild("h_beak2", ModelPartBuilder.create().uv(84, 17).cuboid(-1.0F, 0.0F, -4.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.6F, -5.6F, 0.35F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_leaf0", ModelPartBuilder.create().uv(108, 17).cuboid(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, -9.4F, -2.0F, 0.3F, 0.4F, -0.5F));
		ModelPartData x3 = b[0].addChild("h_leaf1", ModelPartBuilder.create().uv(118, 17).cuboid(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -9.6F, 1.5F, -0.4F, -0.6F, 0.5F));
		ModelPartData x4 = b[0].addChild("h_leaf2", ModelPartBuilder.create().uv(64, 24).cuboid(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -9.8F, -0.5F, 0.2F, 1.2F, 0.3F));
		ModelPartData x5 = b[3].addChild("c_mush", ModelPartBuilder.create().uv(104, 0).cuboid(-3.5F, -4.2F, -2.5F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = x5.addChild("c_mush2", ModelPartBuilder.create().uv(96, 17).cuboid(-1.5F, -1.5F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -4.6F, 0.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[4].addChild("c_leafpad", ModelPartBuilder.create().uv(64, 17).cuboid(-1.4F, -3.2F, -2.6F, 5.0F, 2.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData aura() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_crest", ModelPartBuilder.create().uv(86, 0).cuboid(-0.5F, -3.0F, -4.5F, 1.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_crest2", ModelPartBuilder.create().uv(106, 0).cuboid(-0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.8F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[3].addChild("c_pauldron", ModelPartBuilder.create().uv(64, 13).cuboid(-4.0F, -3.4F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[4].addChild("c_pauldron", ModelPartBuilder.create().uv(88, 13).mirrored().cuboid(-2.0F, -3.4F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[3].addChild("c_pauldron2", ModelPartBuilder.create().uv(64, 22).cuboid(-4.2F, -0.6F, -3.2F, 6.0F, 1.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[4].addChild("c_pauldron2", ModelPartBuilder.create().uv(88, 22).mirrored().cuboid(-1.7999999999999998F, -0.6F, -3.2F, 6.0F, 1.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData earth() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[3].addChild("c_rock", ModelPartBuilder.create().uv(86, 0).cuboid(-4.0F, -3.6F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[4].addChild("c_rock", ModelPartBuilder.create().uv(64, 13).mirrored().cuboid(-2.0F, -3.6F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[3].addChild("c_moss", ModelPartBuilder.create().uv(88, 13).cuboid(-4.0F, -4.2F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[4].addChild("c_moss", ModelPartBuilder.create().uv(64, 22).mirrored().cuboid(-2.0F, -4.2F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[2].addChild("c_float", ModelPartBuilder.create().uv(88, 22).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -5.0F, 2.5F, 0.4F, 0.6F, 0.3F));
		ModelPartData x5 = b[2].addChild("c_float2", ModelPartBuilder.create().uv(96, 22).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, -3.0F, 1.5F, 0.3F, 0.8F, 0.5F));
		ModelPartData x6 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData mana() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_cone", ModelPartBuilder.create().uv(64, 17).cuboid(-3.5F, -12.5F, -3.5F, 7.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = x0.addChild("h_cone1", ModelPartBuilder.create().uv(92, 17).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.2F, 0.3F, -0.30400000000000005F, 0.0F, 0.05F));
		ModelPartData x2 = x1.addChild("h_cone2", ModelPartBuilder.create().uv(112, 17).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.7F, 0.0F, -0.5319999999999999F, 0.0F, 0.05F));
		ModelPartData x3 = x2.addChild("h_cone3", ModelPartBuilder.create().uv(64, 28).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.684F, 0.0F, 0.0F));
		ModelPartData x4 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(64, 0).cuboid(-8.0F, -5.0F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_orb", ModelPartBuilder.create().uv(72, 28).cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(6.8F, -5.5F, 0.5F, 0.2F, 0.785F, 0.3F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData dragon() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_horn_l", ModelPartBuilder.create().uv(86, 16).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.2F, -8.2F, 0.5F, -0.35F, 0.0F, 0.45F));
		ModelPartData x1 = x0.addChild("h_horn_l2", ModelPartBuilder.create().uv(94, 16).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, 0.1F));
		ModelPartData x2 = x1.addChild("h_horn_l3", ModelPartBuilder.create().uv(118, 16).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, 0.1F));
		ModelPartData x3 = b[0].addChild("h_horn_r", ModelPartBuilder.create().uv(102, 16).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.2F, -8.2F, 0.5F, -0.35F, 0.0F, -0.45F));
		ModelPartData x4 = x3.addChild("h_horn_r2", ModelPartBuilder.create().uv(110, 16).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, -0.1F));
		ModelPartData x5 = x4.addChild("h_horn_r3", ModelPartBuilder.create().uv(122, 16).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, -0.1F));
		ModelPartData x6 = b[3].addChild("c_scale", ModelPartBuilder.create().uv(88, 0).cuboid(-3.9F, -3.4F, -3.0F, 5.0F, 2.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[4].addChild("c_scale", ModelPartBuilder.create().uv(64, 16).mirrored().cuboid(-1.1F, -3.4F, -3.0F, 5.0F, 2.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[2].addChild("c_wing_l", ModelPartBuilder.create().uv(64, 0).cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.4F, 0.4F, 2.6F, 0.18F, 0.42F, 0.12F));
		ModelPartData x9 = b[2].addChild("c_wing_r", ModelPartBuilder.create().uv(76, 0).cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.4F, 0.4F, 2.6F, 0.18F, -0.42F, -0.12F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData spirit() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_ofuda", ModelPartBuilder.create().uv(86, 0).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -6.2F, -5.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[2].addChild("c_lantern", ModelPartBuilder.create().uv(92, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.7F, 9.6F, -1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[2].addChild("c_lantern_t", ModelPartBuilder.create().uv(112, 0).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(4.7F, 9.6F, -1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[2].addChild("c_lantern_b", ModelPartBuilder.create().uv(64, 20).cuboid(-1.5F, 3.0F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(4.7F, 9.6F, -1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[3].addChild("c_ofuda", ModelPartBuilder.create().uv(100, 0).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-1.5F, -2.5F, -2.9F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[4].addChild("c_ofuda", ModelPartBuilder.create().uv(106, 0).mirrored().cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(1.5F, -2.5F, -2.9F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 19.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData sound() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_crown", ModelPartBuilder.create().uv(86, 15).cuboid(-4.0F, -11.0F, -4.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.2F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(64, 0).cuboid(-7.0F, -6.0F, -7.0F, 14.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_feather", ModelPartBuilder.create().uv(118, 15).cuboid(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(-0.3F)), ModelTransform.of(4.6F, -9.6F, 0.5F, -0.5F, 0.0F, 0.35F));
		ModelPartData x3 = x2.addChild("h_feather2", ModelPartBuilder.create().uv(64, 26).cuboid(0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -5.6F, 0.5F, -0.4F, 0.0F, 0.0F));
		ModelPartData x4 = b[2].addChild("c_bell0", ModelPartBuilder.create().uv(70, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-3.0F, 11.2F, -3.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_bell1", ModelPartBuilder.create().uv(74, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-1.6F, 11.2F, -3.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[2].addChild("c_bell2", ModelPartBuilder.create().uv(78, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(2.8F, 11.2F, -3.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(64, 15).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 2.7F, 0.14F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData wind() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("c_body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.5F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(40, 32).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)), ModelTransform.NONE);
		ModelPartData x0 = b[2].addChild("c_tail1", ModelPartBuilder.create().uv(64, 0).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 11.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(1.6F, -0.6F, 2.6F, 0.85F, 0.2F, 0.12F));
		ModelPartData x1 = b[2].addChild("c_tail2", ModelPartBuilder.create().uv(70, 0).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 9.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(-1.4F, -0.6F, 2.6F, 1.05F, -0.25F, -0.12F));
		ModelPartData x2 = b[3].addChild("c_feather", ModelPartBuilder.create().uv(76, 0).cuboid(-3.8F, -3.2F, -2.8F, 5.0F, 2.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[4].addChild("c_feather", ModelPartBuilder.create().uv(98, 0).mirrored().cuboid(-1.2000000000000002F, -3.2F, -2.8F, 5.0F, 2.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[3].addChild("c_feather2", ModelPartBuilder.create().uv(64, 12).cuboid(-0.5F, -4.2F, -2.5F, 1.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -1.5F, 0.0F, 0.0F, 0.0F, 0.5F));
		ModelPartData x5 = b[4].addChild("c_feather2", ModelPartBuilder.create().uv(76, 12).mirrored().cuboid(-0.5F, -4.2F, -2.5F, 1.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -1.5F, 0.0F, 0.0F, 0.0F, -0.5F));
		return TexturedModelData.of(md, 128, 128);
	}

}
