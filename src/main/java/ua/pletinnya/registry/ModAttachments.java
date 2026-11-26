package ua.pletinnya.registry;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import ua.pletinnya.Pletinnya;
import ua.pletinnya.magic.MageData;

public final class ModAttachments {
	private ModAttachments() {}

	@SuppressWarnings("UnstableApiUsage")
	public static final AttachmentType<MageData> MAGE = AttachmentRegistry.<MageData>builder()
		.persistent(MageData.CODEC)
		.copyOnDeath()
		.initializer(MageData::new)
		.buildAndRegister(Pletinnya.id("mage"));

	public static void init() {}
}
