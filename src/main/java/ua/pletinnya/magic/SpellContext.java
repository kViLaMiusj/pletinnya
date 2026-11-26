package ua.pletinnya.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

/** Усе, що потрібно для вивільнення плетіння. Заклинач може бути вже не в мережі. */
public record SpellContext(ServerWorld world, @Nullable ServerPlayerEntity caster, SpellFormula formula, float power) {
	public Element element() { return formula.elementType(); }

	public DamageSource magicDamage() {
		return caster != null ? world.getDamageSources().indirectMagic(caster, caster) : world.getDamageSources().magic();
	}

	public boolean isCaster(Entity e) {
		return caster != null && e == caster;
	}

	public SpellContext withPower(float p) {
		return new SpellContext(world, caster, formula, p);
	}
}
