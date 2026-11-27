package ua.pletinnya.client;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.AnimationFormat;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.fx.Anim;

/**
 * Анімації тіла мага через playerAnimator: випад посохом, здіймання над головою, посох до грудей,
 * молитва, драконячий рик, віддача від збою. Видно й іншим гравцям, і від першої особи.
 */
public final class ClientAnims {
	private ClientAnims() {}

	private static final Identifier LAYER = Pletinnya.id("cast");

	public static void init() {
		PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(LAYER, 42, player -> new ModifierLayer<>());
	}

	public static void onPayload(Anim.Payload p) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.world == null) return;
		Entity e = mc.world.getEntityById(p.entityId());
		if (e instanceof AbstractClientPlayerEntity pl) play(pl, p.kind(), p.power());
	}

	@SuppressWarnings("unchecked")
	public static void play(AbstractClientPlayerEntity player, int kind, float power) {
		ModifierLayer<IAnimation> layer;
		try {
			layer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(LAYER);
		} catch (Throwable t) {
			return;
		}
		if (layer == null) return;
		KeyframeAnimation anim = build(kind, holdsStaff(player), player.getMainArm() == Arm.LEFT, power);
		KeyframeAnimationPlayer kp = new KeyframeAnimationPlayer(anim).setFirstPersonMode(FirstPersonMode.DISABLED);
		if (player == MinecraftClient.getInstance().player) {
			fpKind = kind;
			fpStart = player.age;
		}
		layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, Ease.INOUTSINE), kp, true);
	}

	// ------------------------------------------------------------------ перша особа: власний рух посоха
	private static int fpKind = -1;
	private static int fpStart = 0;

	private static final int[] FP_LEN = {12, 18, 16, 26, 18, 10};

	/** Зсув посоха від першої особи: {x, y, z, нахил°} або null, коли руху немає. */
	public static float[] firstPersonOffset(AbstractClientPlayerEntity p, float td) {
		if (fpKind < 0 || p != MinecraftClient.getInstance().player) return null;
		float t = p.age - fpStart + td;
		int len = FP_LEN[Math.min(fpKind, FP_LEN.length - 1)];
		if (t < 0 || t > len) { fpKind = -1; return null; }
		float f = t / len;
		float bell = (float) Math.sin(Math.PI * f);
		switch (fpKind) {
			case Anim.THRUST -> {
				float up = f < 0.25f ? f / 0.25f : 1f - (f - 0.25f) / 0.75f;
				float push = f < 0.25f ? 0f : (float) Math.sin(Math.PI * (f - 0.25f) / 0.75f);
				return new float[]{-0.12f * push, 0.12f * up - 0.04f * push, -0.45f * push, -18f * push + 10f * up};
			}
			case Anim.RAISE -> {
				float up = f < 0.45f ? (float) Math.sin(Math.PI / 2 * f / 0.45f) : 1f - (f - 0.45f) / 0.25f;
				up = Math.max(-0.4f, up);
				if (f > 0.7f) up = -0.4f * (1f - (f - 0.7f) / 0.3f);
				return new float[]{-0.1f * bell, 0.35f * up, -0.1f * bell, 14f * up};
			}
			case Anim.SELF -> { return new float[]{-0.3f * bell, 0.05f * bell, 0.05f * bell, -8f * bell}; }
			case Anim.PRAY -> { return new float[]{-0.28f * bell, -0.12f * bell, 0f, -6f * bell}; }
			case Anim.ROAR -> { return new float[]{0.12f * bell, -0.22f * bell, 0f, 12f * bell}; }
			default -> { return new float[]{0.05f * bell, 0.05f * bell, 0.15f * bell, 15f * bell}; }
		}
	}

	private static boolean holdsStaff(AbstractClientPlayerEntity p) {
		ItemStack s = p.getMainHandStack();
		return !s.isEmpty() && GuiIcons.HAS.contains(Registries.ITEM.getId(s.getItem()));
	}

	// ------------------------------------------------------------------ побудова кадрів
	private static final class B {
		final KeyframeAnimation.AnimationBuilder b = new KeyframeAnimation.AnimationBuilder(AnimationFormat.JSON_EMOTECRAFT);
		final boolean left;

		B(boolean left, int end) {
			this.left = left;
			b.beginTick = 0;
			b.endTick = end;
			b.stopTick = end;
			b.isLooped = false;
		}

		KeyframeAnimation.StateCollection main() { return left ? b.leftArm : b.rightArm; }
		KeyframeAnimation.StateCollection off() { return left ? b.rightArm : b.leftArm; }
		float side(float v) { return left ? -v : v; }

		/** Рука з посохом: нахил уперед (pitch), поворот (yaw), відведення вбік (roll; + — назовні). */
		B main(int t, float pitch, float yaw, float roll) {
			KeyframeAnimation.StateCollection a = main();
			a.pitch.addKeyFrame(t, pitch, Ease.INOUTSINE);
			a.yaw.addKeyFrame(t, side(yaw), Ease.INOUTSINE);
			a.roll.addKeyFrame(t, side(roll), Ease.INOUTSINE);
			return this;
		}

		B off(int t, float pitch, float yaw, float roll) {
			KeyframeAnimation.StateCollection a = off();
			a.pitch.addKeyFrame(t, pitch, Ease.INOUTSINE);
			a.yaw.addKeyFrame(t, side(-yaw), Ease.INOUTSINE);
			a.roll.addKeyFrame(t, side(-roll), Ease.INOUTSINE);
			return this;
		}

		B body(int t, float pitch, float yaw) {
			b.body.pitch.addKeyFrame(t, pitch, Ease.INOUTSINE);
			b.body.yaw.addKeyFrame(t, side(yaw), Ease.INOUTSINE);
			return this;
		}

		B head(int t, float pitch) {
			b.head.pitch.addKeyFrame(t, pitch, Ease.INOUTSINE);
			return this;
		}

		KeyframeAnimation build() { return b.build(); }
	}

	static KeyframeAnimation build(int kind, boolean staff, boolean left, float power) {
		float rest = staff ? -0.93f : 0f;
		float ry = staff ? -0.12f : 0f;
		switch (kind) {
			case Anim.THRUST -> {
				// посох злітає вгору, потім різкий випад уперед — у бік цілі
				return new B(left, 15)
					.main(3, -2.25f, ry, 0.05f).main(6, -1.62f, 0.08f, 0f).main(10, -1.55f, 0.05f, 0f).main(15, rest, ry, 0f)
					.off(3, -0.25f, 0f, 0.1f).off(6, -0.55f, 0.1f, 0.25f).off(15, 0f, 0f, 0f)
					.body(3, 0f, 0.18f).body(6, 0.06f, -0.28f).body(15, 0f, 0f)
					.build();
			}
			case Anim.RAISE -> {
				// обома руками над головою, удар посохом донизу
				return new B(left, 21)
					.main(5, -1.95f, ry, 0.1f).main(9, -2.05f, ry, 0.12f).main(12, -0.8f, ry, 0f).main(21, rest, ry, 0f)
					.off(5, -1.85f, 0.5f, 0f).off(9, -1.95f, 0.5f, 0f).off(12, -0.6f, 0.3f, 0f).off(21, 0f, 0f, 0f)
					.body(5, -0.12f, 0f).body(9, -0.15f, 0f).body(12, 0.35f, 0f).body(21, 0f, 0f)
					.build();
			}
			case Anim.SELF -> {
				// посох навскіс до грудей, погляд угору
				return new B(left, 19)
					.main(5, -1.35f, -0.6f, 0f).main(13, -1.4f, -0.62f, 0f).main(19, rest, ry, 0f)
					.off(5, -1.25f, 0.7f, 0f).off(13, -1.3f, 0.72f, 0f).off(19, 0f, 0f, 0f)
					.head(7, -0.4f).head(13, -0.42f).head(19, 0f)
					.build();
			}
			case Anim.PRAY -> {
				return new B(left, 30)
					.main(6, -1.35f, -0.45f, 0f).main(22, -1.38f, -0.45f, 0f).main(30, rest, ry, 0f)
					.off(6, -1.35f, 0.45f, 0f).off(22, -1.38f, 0.45f, 0f).off(30, 0f, 0f, 0f)
					.head(6, 0.45f).head(22, 0.48f).head(30, 0f)
					.body(6, 0.12f, 0f).body(22, 0.12f, 0f).body(30, 0f, 0f)
					.build();
			}
			case Anim.ROAR -> {
				return new B(left, 22)
					.main(5, -0.55f, 0f, 1.15f).main(16, -0.6f, 0f, 1.2f).main(22, rest, ry, 0f)
					.off(5, -0.55f, 0f, 1.15f).off(16, -0.6f, 0f, 1.2f).off(22, 0f, 0f, 0f)
					.head(5, -0.45f).head(16, -0.5f).head(22, 0f)
					.body(5, -0.15f, 0f).body(16, -0.15f, 0f).body(22, 0f, 0f)
					.build();
			}
			default -> {
				// віддача: тіло відкидає назад
				return new B(left, 12)
					.main(2, -0.35f, ry, 0.3f).main(12, rest, ry, 0f)
					.off(2, -0.2f, 0f, 0.35f).off(12, 0f, 0f, 0f)
					.body(2, -0.25f, 0f).body(12, 0f, 0f)
					.head(2, -0.3f).head(12, 0f)
					.build();
			}
		}
	}
}
