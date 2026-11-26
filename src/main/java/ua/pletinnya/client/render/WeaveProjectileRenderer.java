package ua.pletinnya.client.render;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.entity.WeaveProjectileEntity;

/** Снаряд як світний образ есенції свого напряму з ореолом, що обертається. */
public class WeaveProjectileRenderer extends EntityRenderer<WeaveProjectileEntity> {
	private static final Identifier HALO = Pletinnya.id("textures/particle/ring.png");

	public WeaveProjectileRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(WeaveProjectileEntity entity) {
		return Pletinnya.id("textures/item/essence_" + entity.element().id + ".png");
	}

	@Override
	public void render(WeaveProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light) {
		float t = entity.age + tickDelta;
		int color = entity.element().color;
		matrices.push();
		matrices.translate(0, 0.15, 0);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t * 9f));
		float pulse = 1f + 0.12f * (float) Math.sin(t * 0.5f);
		matrices.scale(1.3f * pulse, 1.3f * pulse, 1.3f * pulse);
		quad(vcp.getBuffer(RenderLayer.getEntityTranslucentEmissive(HALO)), matrices.peek(), 0xAA000000 | color);
		matrices.pop();
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-t * 4f));
		matrices.scale(0.8f, 0.8f, 0.8f);
		quad(vcp.getBuffer(RenderLayer.getEntityTranslucentEmissive(getTexture(entity))), matrices.peek(), 0xFFFFFFFF);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vcp, light);
	}

	private static void quad(VertexConsumer vc, MatrixStack.Entry e, int argb) {
		v(vc, e, -0.5f, -0.5f, 0f, 1f, argb);
		v(vc, e, 0.5f, -0.5f, 1f, 1f, argb);
		v(vc, e, 0.5f, 0.5f, 1f, 0f, argb);
		v(vc, e, -0.5f, 0.5f, 0f, 0f, argb);
	}

	private static void v(VertexConsumer vc, MatrixStack.Entry e, float x, float y, float u, float v, int argb) {
		vc.vertex(e, x, y, 0f).color(argb).texture(u, v).overlay(OverlayTexture.DEFAULT_UV)
			.light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(e, 0f, 1f, 0f);
	}
}
