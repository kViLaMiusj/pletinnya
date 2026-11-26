package ua.pletinnya.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ua.pletinnya.item.EssenceItem;
import ua.pletinnya.magic.Element;
import ua.pletinnya.magic.SpellContext;
import ua.pletinnya.magic.SpellEffects;
import ua.pletinnya.magic.SpellFormula;
import ua.pletinnya.magic.SpellTrigger;
import ua.pletinnya.registry.ModEntities;
import ua.pletinnya.registry.ModItems;

/** Снаряд із мани. Несе формулу й вивільняє її при влучанні або за умовою. */
public class WeaveProjectileEntity extends ThrownItemEntity {
	private SpellFormula formula = SpellFormula.empty();
	private float power = 1f;
	private int life = 100;
	private boolean released = false;

	public WeaveProjectileEntity(EntityType<? extends WeaveProjectileEntity> type, World world) {
		super(type, world);
	}

	public WeaveProjectileEntity(World world, LivingEntity owner, SpellFormula formula, float power) {
		super(ModEntities.WEAVE_PROJECTILE, owner, world);
		this.formula = formula;
		this.power = power;
		this.life = 60 + formula.range() * 16;
		this.setItem(new ItemStack(ModItems.essence(formula.elementType())));
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.essence(Element.MANA);
	}

	@Override
	protected double getGravity() {
		return 0.0;
	}

	public int complexity() {
		return formula.complexity();
	}

	private Element element() {
		return getStack().getItem() instanceof EssenceItem e ? e.element : Element.MANA;
	}

	@Override
	public void tick() {
		super.tick();
		World world = getWorld();
		if (world.isClient) {
			for (int i = 0; i < 2; i++) {
				world.addParticle(SpellEffects.particle(element()), getX(), getY() + 0.1, getZ(),
					(random.nextDouble() - 0.5) * 0.05, (random.nextDouble() - 0.5) * 0.05, (random.nextDouble() - 0.5) * 0.05);
			}
			return;
		}
		if (isRemoved()) return;
		SpellTrigger trigger = formula.triggerType();
		if (trigger == SpellTrigger.DELAYED && age >= formula.delayTicks()) {
			release(getPos(), null);
		} else if (trigger == SpellTrigger.PROXIMITY && age > 5) {
			float r = Math.max(2f, formula.radius());
			Entity owner = getOwner();
			boolean someone = !world.getEntitiesByClass(LivingEntity.class, new Box(getPos(), getPos()).expand(r),
				e -> e.isAlive() && !e.isSpectator() && e != owner && e.squaredDistanceTo(this) <= r * r).isEmpty();
			if (someone) release(getPos(), null);
		}
		if (!isRemoved() && age > life) {
			if (trigger == SpellTrigger.IMMEDIATE) {
				((ServerWorld) world).spawnParticles(net.minecraft.particle.ParticleTypes.SMOKE, getX(), getY(), getZ(), 8, 0.1, 0.1, 0.1, 0.01);
				discard();
			} else {
				release(getPos(), null);
			}
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult hit) {
		if (!getWorld().isClient) release(hit.getPos(), hit.getEntity());
	}

	@Override
	protected void onBlockHit(BlockHitResult hit) {
		super.onBlockHit(hit);
		if (!getWorld().isClient) release(hit.getPos(), null);
	}

	private void release(Vec3d pos, @Nullable Entity hit) {
		if (released || !(getWorld() instanceof ServerWorld sw)) return;
		released = true;
		ServerPlayerEntity caster = getOwner() instanceof ServerPlayerEntity sp ? sp : null;
		SpellEffects.impact(new SpellContext(sw, caster, formula, power), pos, hit);
		discard();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		SpellFormula.CODEC.encodeStart(NbtOps.INSTANCE, formula).result().ifPresent(el -> nbt.put("Formula", el));
		nbt.putFloat("Power", power);
		nbt.putInt("Life", life);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("Formula")) {
			SpellFormula.CODEC.parse(NbtOps.INSTANCE, nbt.get("Formula")).result().ifPresent(f -> this.formula = f);
		}
		power = nbt.getFloat("Power");
		life = nbt.getInt("Life");
	}
}
