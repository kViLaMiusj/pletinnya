package ua.pletinnya.client.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import ua.pletinnya.client.fx.Geo;
import ua.pletinnya.entity.SpellSpiritEntity;
import ua.pletinnya.magic.Element;

import static ua.pletinnya.client.fx.Geo.*;

/** Елементаль — велике ядро стихії з короною уламків і кільцем; сфера орбіти — менше ядро зі сяйвом. */
public class SpellSpiritRenderer extends EntityRenderer<SpellSpiritEntity> {
	public SpellSpiritRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(SpellSpiritEntity entity) {
		return Geo.WHITE;
	}

	@Override
	public void render(SpellSpiritEntity en, float yaw, float td, MatrixStack m, VertexConsumerProvider vcp, int light) {
		float t = en.age + td;
		Element el = en.element();
		int P = MathHelper.clamp(en.powerStat(), 1, 10);
		boolean orbit = en.mode() == SpellSpiritEntity.ORBIT;
		float S = orbit ? 0.42f + en.sizeStat() * 0.045f : 0.85f + en.sizeStat() * 0.07f;
		m.push();
		m.translate(0, 0.3, 0);
		// сяйво
		m.push();
		m.multiply(this.dispatcher.getRotation());
		flat(vcp.getBuffer(glow()), m.peek(), S * 2.0f * (1f + 0.08f * MathHelper.sin(t * 0.3f)),
			argb(el == Element.DARKNESS ? 70 : 120, el == Element.DARKNESS ? 0x7A4AC0 : el.color));
		m.pop();
		VertexConsumer vc = vcp.getBuffer(solid());
		m.push();
		m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * (orbit ? 8 : 2)));
		if (!orbit) m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));   // у елементаля хвости стихії здіймаються вгору, як полум'я
		WeaveProjectileRenderer.drawElement(m, vc, el, t, S, P, en.getId());
		m.pop();
		if (!orbit) {
			// корона уламків і кільце — щоб елементаль читався як істота
			m.push();
			m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-t * 3));
			int n = 5 + P / 3;
			for (int i = 0; i < n; i++) {
				float a = i * MathHelper.TAU / n;
				m.push();
				m.translate(MathHelper.cos(a) * S * 0.55f, S * 0.45f + 0.06f * MathHelper.sin(t * 0.2f + i), MathHelper.sin(a) * S * 0.55f);
				m.multiply(RotationAxis.POSITIVE_Y.rotation(-a));
				m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20));
				crystal(vc, m.peek(), S * 0.06f, S * 0.28f, S * 0.08f, argb(230, shade(el.color | 0xFF000000, 1.25f) & 0xFFFFFF));
				m.pop();
			}
			m.translate(0, -S * 0.55f, 0);
			ring(vc, m.peek(), S * 0.6f, S * 0.05f, 24, argb(160, el.color));
			m.pop();
			int e2 = en.element2();
			if (e2 >= 0) {
				m.push();
				m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(t * 6));
				m.translate(S * 0.8f, S * 0.2f, 0);
				WeaveProjectileRenderer.drawElement(m, vc, Element.byIndex(e2), t, S * 0.4f, P / 2 + 1, en.getId() + 3);
				m.pop();
			}
		}
		m.pop();
		super.render(en, yaw, td, m, vcp, light);
	}
}
