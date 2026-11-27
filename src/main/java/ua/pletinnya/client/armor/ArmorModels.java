package ua.pletinnya.client.armor;

import net.minecraft.client.model.*;

import java.util.ArrayList;
import java.util.List;

/** Згенеровано tools/art/armor_models12.py — вбрання магів за мотивами Iron's Spells. Не редагувати вручну. */
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
			case "lightning" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_cone"}, {"head", "h_brim"}, {"body", "c_collar"}};
			case "ice" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_peak"}, {"body", "c_mantle"}, {"head", "h_ice0"}, {"head", "h_ice1"}, {"head", "h_ice2"}, {"head", "h_ice3"}, {"head", "h_ice_c"}, {"right_arm", "c_shard"}, {"left_arm", "c_shard"}};
			case "darkness" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_crown"}, {"head", "h_brim"}, {"body", "c_vial0"}, {"body", "c_vial1"}, {"body", "c_cape"}};
			case "blood" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_peak"}, {"head", "h_horn_l"}, {"head", "h_horn_r"}, {"right_arm", "c_bone"}, {"left_arm", "c_bone"}, {"right_arm", "c_bspike"}, {"left_arm", "c_bspike"}, {"body", "c_cape"}};
			case "holy" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_mitre"}, {"head", "h_halo0"}, {"head", "h_halo1"}, {"head", "h_halo2"}, {"head", "h_halo3"}, {"right_arm", "c_shoulder"}, {"left_arm", "c_shoulder"}};
			case "light" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_peak"}, {"head", "h_sun"}, {"right_arm", "c_pauldron"}, {"left_arm", "c_pauldron"}};
			case "nature" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_peak"}, {"head", "h_ant_l"}, {"head", "h_ant_r"}, {"head", "h_leaf0"}, {"head", "h_leaf1"}, {"right_arm", "c_mush"}, {"left_arm", "c_leafpad"}, {"body", "c_cape"}};
			case "aura" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_crest"}, {"head", "h_crest_b"}, {"right_arm", "c_pauldron"}, {"left_arm", "c_pauldron"}, {"right_arm", "c_pauldron2"}, {"left_arm", "c_pauldron2"}, {"body", "c_cape"}};
			case "earth" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_peak"}, {"right_arm", "c_rock"}, {"left_arm", "c_rock"}, {"right_arm", "c_moss"}, {"left_arm", "c_moss"}, {"body", "c_float"}, {"body", "c_float2"}, {"body", "c_float3"}};
			case "mana" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_cone"}, {"head", "h_brim"}, {"body", "c_orb"}, {"body", "c_collar"}};
			case "dragon" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_peak"}, {"head", "h_horn_l"}, {"head", "h_horn_r"}, {"right_arm", "c_scale"}, {"left_arm", "c_scale"}, {"body", "c_wing_l"}, {"body", "c_wing_r"}};
			case "spirit" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_kasa"}, {"head", "h_kasa2"}, {"head", "h_kasa3"}, {"head", "h_ofuda"}, {"body", "c_lantern"}, {"body", "c_lantern_t"}, {"body", "c_lantern_b"}, {"right_arm", "c_ofuda"}, {"left_arm", "c_ofuda"}};
			case "sound" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_crown"}, {"head", "h_brim"}, {"head", "h_feather"}, {"body", "c_bell0"}, {"body", "c_bell1"}, {"body", "c_bell2"}, {"body", "c_cape"}};
			case "wind" -> new String[][]{{"head", "h_helm"}, {"body", "c_robe"}, {"right_arm", "c_arm"}, {"left_arm", "c_arm"}, {"right_arm", "c_cuff"}, {"left_arm", "c_cuff"}, {"body", "l_waist"}, {"right_leg", "l_leg"}, {"left_leg", "l_leg"}, {"right_leg", "f_boot"}, {"left_leg", "f_boot"}, {"head", "h_flap_l"}, {"head", "h_flap_r"}, {"body", "c_tail1"}, {"body", "c_tail2"}, {"right_arm", "c_feather"}, {"left_arm", "c_feather"}, {"right_arm", "c_feather2"}, {"left_arm", "c_feather2"}};
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
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_cone", ModelPartBuilder.create().uv(72, 15).cuboid(-3.5F, -12.5F, -3.5F, 7.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = x0.addChild("h_cone1", ModelPartBuilder.create().uv(100, 15).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.2F, 0.3F, -0.272F, 0.0F, 0.05F));
		ModelPartData x2 = x1.addChild("h_cone2", ModelPartBuilder.create().uv(72, 26).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.7F, 0.0F, -0.476F, 0.0F, 0.05F));
		ModelPartData x3 = x2.addChild("h_cone3", ModelPartBuilder.create().uv(84, 26).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.6120000000000001F, 0.0F, 0.0F));
		ModelPartData x4 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(72, 0).cuboid(-7.0F, -5.4F, -7.0F, 14.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_collar", ModelPartBuilder.create().uv(92, 26).cuboid(-4.5F, -3.5F, 1.6F, 9.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData ice() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_peak", ModelPartBuilder.create().uv(104, 0).cuboid(-4.0F, -1.0F, -2.6F, 8.0F, 2.0F, 3.0F, new Dilation(0.75F)), ModelTransform.of(0.0F, -8.4F, -3.4F, -0.34F, 0.0F, 0.0F));
		ModelPartData x1 = b[2].addChild("c_mantle", ModelPartBuilder.create().uv(72, 0).cuboid(-5.0F, -0.8F, -3.0F, 10.0F, 2.0F, 6.0F, new Dilation(0.55F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_ice0", ModelPartBuilder.create().uv(92, 8).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.6F, -8.9F, -3.9F, -0.25F, 0.0F, -0.3F));
		ModelPartData x3 = b[0].addChild("h_ice1", ModelPartBuilder.create().uv(76, 8).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -8.9F, -3.9F, -0.25F, 0.0F, -0.1F));
		ModelPartData x4 = b[0].addChild("h_ice2", ModelPartBuilder.create().uv(80, 8).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -8.9F, -3.9F, -0.25F, 0.0F, 0.1F));
		ModelPartData x5 = b[0].addChild("h_ice3", ModelPartBuilder.create().uv(96, 8).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.6F, -8.9F, -3.9F, -0.25F, 0.0F, 0.3F));
		ModelPartData x6 = b[0].addChild("h_ice_c", ModelPartBuilder.create().uv(72, 8).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.9F, -3.9F, -0.25F, 0.0F, 0.0F));
		ModelPartData x7 = b[3].addChild("c_shard", ModelPartBuilder.create().uv(84, 8).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.2F, -4.3F, 0.0F, 0.0F, 0.0F, 0.3F));
		ModelPartData x8 = b[4].addChild("c_shard", ModelPartBuilder.create().uv(88, 8).mirrored().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.2F, -4.3F, 0.0F, 0.0F, 0.0F, -0.3F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData darkness() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_crown", ModelPartBuilder.create().uv(72, 19).cuboid(-3.5F, -12.2F, -3.5F, 7.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(0, 52).cuboid(-8.0F, -9.2F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[2].addChild("c_vial0", ModelPartBuilder.create().uv(100, 19).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 6.0F, -3.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[2].addChild("c_vial1", ModelPartBuilder.create().uv(104, 19).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.8F, 6.0F, -3.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(72, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 18.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.1F, 3.5F, 0.1F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData blood() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_peak", ModelPartBuilder.create().uv(96, 19).cuboid(-4.0F, -1.0F, -2.6F, 8.0F, 2.0F, 3.0F, new Dilation(0.75F)), ModelTransform.of(0.0F, -8.4F, -3.4F, -0.34F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_horn_l", ModelPartBuilder.create().uv(118, 19).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.4F, -8.6F, 0.5F, 0.25F, 0.0F, 0.45F));
		ModelPartData x2 = x1.addChild("h_horn_l2", ModelPartBuilder.create().uv(72, 27).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, 0.1F));
		ModelPartData x3 = x2.addChild("h_horn_l3", ModelPartBuilder.create().uv(104, 27).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, 0.1F));
		ModelPartData x4 = b[0].addChild("h_horn_r", ModelPartBuilder.create().uv(80, 27).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.4F, -8.6F, 0.5F, 0.25F, 0.0F, -0.45F));
		ModelPartData x5 = x4.addChild("h_horn_r2", ModelPartBuilder.create().uv(88, 27).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, -0.1F));
		ModelPartData x6 = x5.addChild("h_horn_r3", ModelPartBuilder.create().uv(108, 27).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.6F, 0.0F, -0.1F));
		ModelPartData x7 = b[3].addChild("c_bone", ModelPartBuilder.create().uv(94, 0).cuboid(-4.2F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[4].addChild("c_bone", ModelPartBuilder.create().uv(72, 19).mirrored().cuboid(-1.7999999999999998F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x9 = b[3].addChild("c_bspike", ModelPartBuilder.create().uv(96, 27).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.4F, -5.2F, 0.0F, 0.0F, 0.0F, 0.35F));
		ModelPartData x10 = b[4].addChild("c_bspike", ModelPartBuilder.create().uv(100, 27).mirrored().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.4F, -5.2F, 0.0F, 0.0F, 0.0F, -0.35F));
		ModelPartData x11 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(72, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 18.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.1F, 3.5F, 0.1F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData holy() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_mitre", ModelPartBuilder.create().uv(72, 0).cuboid(-3.5F, -6.0F, -3.0F, 7.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = x0.addChild("h_mitre2", ModelPartBuilder.create().uv(72, 19).cuboid(-2.5F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_halo0", ModelPartBuilder.create().uv(90, 19).cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.5F, 1.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[0].addChild("h_halo1", ModelPartBuilder.create().uv(108, 19).cuboid(-4.0F, 0.0F, 3.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.5F, 1.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[0].addChild("h_halo2", ModelPartBuilder.create().uv(98, 0).cuboid(-4.0F, 0.0F, -3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.5F, 1.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[0].addChild("h_halo3", ModelPartBuilder.create().uv(112, 0).cuboid(3.0F, 0.0F, -3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.5F, 1.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[3].addChild("c_shoulder", ModelPartBuilder.create().uv(72, 12).cuboid(-4.2F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[4].addChild("c_shoulder", ModelPartBuilder.create().uv(96, 12).mirrored().cuboid(-1.7999999999999998F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData light() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_peak", ModelPartBuilder.create().uv(96, 13).cuboid(-4.0F, -1.0F, -2.6F, 8.0F, 2.0F, 3.0F, new Dilation(0.75F)), ModelTransform.of(0.0F, -8.4F, -3.4F, -0.34F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_sun", ModelPartBuilder.create().uv(72, 0).cuboid(-6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.5F, 5.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[3].addChild("c_pauldron", ModelPartBuilder.create().uv(98, 0).cuboid(-4.2F, -4.2F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[4].addChild("c_pauldron", ModelPartBuilder.create().uv(72, 13).mirrored().cuboid(-1.7999999999999998F, -4.2F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData nature() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_peak", ModelPartBuilder.create().uv(100, 16).cuboid(-4.0F, -1.0F, -2.6F, 8.0F, 2.0F, 3.0F, new Dilation(0.75F)), ModelTransform.of(0.0F, -8.4F, -3.4F, -0.34F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_ant_l", ModelPartBuilder.create().uv(118, 0).cuboid(-0.75F, -5.0F, -0.75F, 1.5F, 5.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(3.2F, -8.6F, -0.5F, 0.0F, 0.0F, 0.35F));
		ModelPartData x2 = x1.addChild("h_ant_l2", ModelPartBuilder.create().uv(122, 16).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.6F, 0.0F, 0.0F, 0.0F, 0.45F));
		ModelPartData x3 = x1.addChild("h_ant_l3", ModelPartBuilder.create().uv(76, 23).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.0F, 0.0F, -0.75F));
		ModelPartData x4 = b[0].addChild("h_ant_r", ModelPartBuilder.create().uv(72, 16).cuboid(-0.75F, -5.0F, -0.75F, 1.5F, 5.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(-3.2F, -8.6F, -0.5F, 0.0F, 0.0F, -0.35F));
		ModelPartData x5 = x4.addChild("h_ant_r2", ModelPartBuilder.create().uv(72, 23).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.6F, 0.0F, 0.0F, 0.0F, -0.45F));
		ModelPartData x6 = x4.addChild("h_ant_r3", ModelPartBuilder.create().uv(80, 23).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.0F, 0.0F, 0.75F));
		ModelPartData x7 = b[0].addChild("h_leaf0", ModelPartBuilder.create().uv(84, 23).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, -9.0F, -2.6F, 0.3F, 0.4F, -0.3F));
		ModelPartData x8 = b[0].addChild("h_leaf1", ModelPartBuilder.create().uv(94, 23).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.2F, -9.1F, 1.6F, -0.3F, -0.5F, 0.3F));
		ModelPartData x9 = b[3].addChild("c_mush", ModelPartBuilder.create().uv(80, 16).cuboid(-2.5F, -1.2F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -3.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x10 = x9.addChild("c_mush_s", ModelPartBuilder.create().uv(104, 23).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.2F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x11 = b[4].addChild("c_leafpad", ModelPartBuilder.create().uv(94, 0).cuboid(-1.8F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x12 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(72, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.1F, 3.5F, 0.1F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData aura() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_crest", ModelPartBuilder.create().uv(94, 0).cuboid(-0.5F, -3.0F, -4.5F, 1.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.8F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_crest_b", ModelPartBuilder.create().uv(72, 14).cuboid(-1.0F, -1.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[3].addChild("c_pauldron", ModelPartBuilder.create().uv(94, 14).cuboid(-4.2F, -4.2F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[4].addChild("c_pauldron", ModelPartBuilder.create().uv(72, 24).mirrored().cuboid(-1.7999999999999998F, -4.2F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[3].addChild("c_pauldron2", ModelPartBuilder.create().uv(96, 24).cuboid(-4.2F, -1.4F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.45F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[4].addChild("c_pauldron2", ModelPartBuilder.create().uv(72, 33).mirrored().cuboid(-1.7999999999999998F, -1.4F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.45F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(72, 0).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.1F, 3.5F, 0.1F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData earth() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_peak", ModelPartBuilder.create().uv(72, 16).cuboid(-4.0F, -1.0F, -2.6F, 8.0F, 2.0F, 3.0F, new Dilation(0.75F)), ModelTransform.of(0.0F, -8.4F, -3.4F, -0.34F, 0.0F, 0.0F));
		ModelPartData x1 = b[3].addChild("c_rock", ModelPartBuilder.create().uv(72, 0).cuboid(-4.2F, -4.4F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.35F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[4].addChild("c_rock", ModelPartBuilder.create().uv(96, 0).mirrored().cuboid(-1.7999999999999998F, -4.4F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.35F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[3].addChild("c_moss", ModelPartBuilder.create().uv(72, 9).cuboid(-4.2F, -5.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.4F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[4].addChild("c_moss", ModelPartBuilder.create().uv(96, 9).mirrored().cuboid(-1.7999999999999998F, -5.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.4F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_float", ModelPartBuilder.create().uv(94, 16).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-7.2F, -4.0F, 2.5F, 0.4F, 0.6F, 0.3F));
		ModelPartData x6 = b[2].addChild("c_float2", ModelPartBuilder.create().uv(110, 16).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(7.4F, -2.5F, 1.5F, 0.3F, 0.8F, 0.5F));
		ModelPartData x7 = b[2].addChild("c_float3", ModelPartBuilder.create().uv(102, 16).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(6.8F, -6.5F, -1.0F, 0.7F, 0.2F, 0.6F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData mana() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_cone", ModelPartBuilder.create().uv(72, 0).cuboid(-4.0F, -12.5F, -4.0F, 8.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = x0.addChild("h_cone1", ModelPartBuilder.create().uv(104, 0).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.2F, 0.3F, -0.336F, 0.0F, 0.05F));
		ModelPartData x2 = x1.addChild("h_cone2", ModelPartBuilder.create().uv(72, 12).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.7F, 0.0F, -0.588F, 0.0F, 0.05F));
		ModelPartData x3 = x2.addChild("h_cone3", ModelPartBuilder.create().uv(84, 12).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.756F, 0.0F, 0.0F));
		ModelPartData x4 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(0, 52).cuboid(-8.0F, -5.4F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_orb", ModelPartBuilder.create().uv(92, 12).cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(7.4F, -6.0F, 0.5F, 0.2F, 0.785F, 0.3F));
		ModelPartData x6 = b[2].addChild("c_collar", ModelPartBuilder.create().uv(100, 12).cuboid(-4.5F, -3.5F, 1.6F, 9.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData dragon() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_peak", ModelPartBuilder.create().uv(96, 16).cuboid(-4.0F, -1.0F, -2.6F, 8.0F, 2.0F, 3.0F, new Dilation(0.75F)), ModelTransform.of(0.0F, -8.4F, -3.4F, -0.34F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_horn_l", ModelPartBuilder.create().uv(118, 16).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.2F, -8.4F, 0.5F, -0.35F, 0.0F, 0.45F));
		ModelPartData x2 = x1.addChild("h_horn_l2", ModelPartBuilder.create().uv(72, 24).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, 0.1F));
		ModelPartData x3 = x2.addChild("h_horn_l3", ModelPartBuilder.create().uv(96, 24).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, 0.1F));
		ModelPartData x4 = b[0].addChild("h_horn_r", ModelPartBuilder.create().uv(80, 24).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.2F, -8.4F, 0.5F, -0.35F, 0.0F, -0.45F));
		ModelPartData x5 = x4.addChild("h_horn_r2", ModelPartBuilder.create().uv(88, 24).cuboid(-0.75F, -3.0F, -0.75F, 1.5F, 3.0F, 1.5F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, -0.1F));
		ModelPartData x6 = x5.addChild("h_horn_r3", ModelPartBuilder.create().uv(100, 24).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.6F, 0.0F, 0.5F, 0.0F, -0.1F));
		ModelPartData x7 = b[3].addChild("c_scale", ModelPartBuilder.create().uv(96, 0).cuboid(-4.2F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[4].addChild("c_scale", ModelPartBuilder.create().uv(72, 16).mirrored().cuboid(-1.7999999999999998F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x9 = b[2].addChild("c_wing_l", ModelPartBuilder.create().uv(72, 0).cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.4F, 0.4F, 3.4F, 0.18F, 0.42F, 0.12F));
		ModelPartData x10 = b[2].addChild("c_wing_r", ModelPartBuilder.create().uv(84, 0).cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.4F, 0.4F, 3.4F, 0.18F, -0.42F, -0.12F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData spirit() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_kasa", ModelPartBuilder.create().uv(0, 52).cuboid(-8.0F, -9.4F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_kasa2", ModelPartBuilder.create().uv(72, 0).cuboid(-5.0F, -10.6F, -5.0F, 10.0F, 1.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_kasa3", ModelPartBuilder.create().uv(72, 11).cuboid(-2.5F, -11.6F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x3 = b[0].addChild("h_ofuda", ModelPartBuilder.create().uv(92, 11).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -8.6F, -7.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x4 = b[2].addChild("c_lantern", ModelPartBuilder.create().uv(98, 11).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(5.8F, 10.2F, -1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_lantern_t", ModelPartBuilder.create().uv(72, 17).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(5.8F, 10.2F, -1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[2].addChild("c_lantern_b", ModelPartBuilder.create().uv(84, 17).cuboid(-1.5F, 3.0F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(5.8F, 10.2F, -1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[3].addChild("c_ofuda", ModelPartBuilder.create().uv(106, 11).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-2.0F, -1.6F, -3.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData x8 = b[4].addChild("c_ofuda", ModelPartBuilder.create().uv(112, 11).mirrored().cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(2.0F, -1.6F, -3.4F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData sound() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_crown", ModelPartBuilder.create().uv(94, 15).cuboid(-4.0F, -11.6F, -4.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x1 = b[0].addChild("h_brim", ModelPartBuilder.create().uv(72, 0).cuboid(-7.0F, -9.0F, -7.0F, 14.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x2 = b[0].addChild("h_feather", ModelPartBuilder.create().uv(72, 27).cuboid(0.0F, -7.0F, 0.0F, 1.0F, 7.0F, 3.0F, new Dilation(-0.3F)), ModelTransform.of(4.4F, -10.0F, 1.0F, -0.55F, 0.0F, 0.3F));
		ModelPartData x3 = x2.addChild("h_feather2", ModelPartBuilder.create().uv(80, 27).cuboid(0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -6.6F, 0.5F, -0.4F, 0.0F, 0.0F));
		ModelPartData x4 = b[2].addChild("c_bell0", ModelPartBuilder.create().uv(86, 27).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-3.0F, 10.4F, -3.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[2].addChild("c_bell1", ModelPartBuilder.create().uv(90, 27).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-1.6F, 10.4F, -3.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[2].addChild("c_bell2", ModelPartBuilder.create().uv(94, 27).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(2.8F, 10.4F, -3.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData x7 = b[2].addChild("c_cape", ModelPartBuilder.create().uv(72, 15).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.1F, 3.5F, 0.1F, 0.0F, 0.0F));
		return TexturedModelData.of(md, 128, 128);
	}

	private static TexturedModelData wind() {
		ModelData md = new ModelData();
		ModelPartData[] b = bones(md);
		b[0].addChild("h_helm", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[2].addChild("c_robe", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new Dilation(0.95F)), ModelTransform.NONE);
		b[3].addChild("c_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[4].addChild("c_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.85F)), ModelTransform.NONE);
		b[3].addChild("c_cuff", ModelPartBuilder.create().uv(56, 16).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[4].addChild("c_cuff", ModelPartBuilder.create().uv(56, 23).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(1.2F)), ModelTransform.NONE);
		b[2].addChild("l_waist", ModelPartBuilder.create().uv(48, 36).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.4F)), ModelTransform.NONE);
		b[5].addChild("l_leg", ModelPartBuilder.create().uv(0, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[6].addChild("l_leg", ModelPartBuilder.create().uv(16, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.NONE);
		b[5].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		b[6].addChild("f_boot", ModelPartBuilder.create().uv(32, 36).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.8F)), ModelTransform.NONE);
		ModelPartData x0 = b[0].addChild("h_flap_l", ModelPartBuilder.create().uv(120, 12).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(4.9F, -4.2F, 0.3F, 0.0F, 0.0F, -0.12F));
		ModelPartData x1 = b[0].addChild("h_flap_r", ModelPartBuilder.create().uv(72, 20).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-4.9F, -4.2F, 0.3F, 0.0F, 0.0F, 0.12F));
		ModelPartData x2 = b[2].addChild("c_tail1", ModelPartBuilder.create().uv(72, 0).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 11.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(1.6F, -0.6F, 3.4F, 0.85F, 0.2F, 0.12F));
		ModelPartData x3 = b[2].addChild("c_tail2", ModelPartBuilder.create().uv(78, 0).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 9.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(-1.4F, -0.6F, 3.4F, 1.05F, -0.25F, -0.12F));
		ModelPartData x4 = b[3].addChild("c_feather", ModelPartBuilder.create().uv(84, 0).cuboid(-4.2F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x5 = b[4].addChild("c_feather", ModelPartBuilder.create().uv(72, 12).mirrored().cuboid(-1.7999999999999998F, -3.6F, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData x6 = b[3].addChild("c_feather2", ModelPartBuilder.create().uv(96, 12).cuboid(-0.5F, -4.2F, -2.5F, 1.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-2.6F, -2.0F, 0.0F, 0.0F, 0.0F, 0.5F));
		ModelPartData x7 = b[4].addChild("c_feather2", ModelPartBuilder.create().uv(108, 12).mirrored().cuboid(-0.5F, -4.2F, -2.5F, 1.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(2.6F, -2.0F, 0.0F, 0.0F, 0.0F, -0.5F));
		return TexturedModelData.of(md, 128, 128);
	}

}
