package ua.pletinnya;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.pletinnya.command.PletinnyaCommand;
import ua.pletinnya.magic.ManaTicker;
import ua.pletinnya.magic.MageData;
import ua.pletinnya.magic.SpellCaster;
import ua.pletinnya.magic.WeaveManager;
import ua.pletinnya.network.ModNetworking;
import ua.pletinnya.registry.ModAttachments;
import ua.pletinnya.registry.ModBlocks;
import ua.pletinnya.registry.ModEntities;
import ua.pletinnya.registry.ModItems;
import ua.pletinnya.registry.ModWorldgen;

public class Pletinnya implements ModInitializer {
	public static final String MOD_ID = "pletinnya";
	public static final Logger LOGGER = LoggerFactory.getLogger("Pletinnya");

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModAttachments.init();
		ua.pletinnya.fx.ModParticles.init();
		ModBlocks.init();
		ModItems.init();
		ModEntities.init();
		ModWorldgen.init();
		ModNetworking.init();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			WeaveManager.tick(server);
			ua.pletinnya.magic.SpellScheduler.tick(server);
			ManaTicker.tick(server);
		});
		net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, base, taken, blocked) -> {
			if (entity instanceof net.minecraft.server.network.ServerPlayerEntity sp && taken > 0 && sp.isAlive())
				ua.pletinnya.magic.SpellForms.onDamaged(sp, source);
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
			handler.getPlayer().getAttachedOrCreate(ModAttachments.MAGE).markDirty());
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			SpellCaster.forget(handler.getPlayer().getUuid());
			ua.pletinnya.magic.SpellForms.forget(handler.getPlayer().getUuid());
			ManaTicker.forget(handler.getPlayer().getUuid());
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			MageData d = newPlayer.getAttachedOrCreate(ModAttachments.MAGE);
			if (!alive) {
				d.mana = d.maxMana * 0.25f;
				d.aura = false;
				d.auraTicks = 0;
			}
			d.markDirty();
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(WeaveManager::clearAll);
		net.fabricmc.fabric.api.event.player.AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
			if (!world.isClient && player instanceof net.minecraft.server.network.ServerPlayerEntity sp
				&& entity instanceof net.minecraft.entity.LivingEntity le && le.isAlive()) {
				ua.pletinnya.magic.AuraTechniques.onHit(sp, le);
			}
			return net.minecraft.util.ActionResult.PASS;
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PletinnyaCommand.register(dispatcher));

		LOGGER.info("Плетіння пробуджується.");
	}
}
