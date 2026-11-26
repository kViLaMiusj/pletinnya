package ua.pletinnya.client;

import ua.pletinnya.magic.MageData;

/** Копія даних мага на клієнті, яку надсилає сервер. */
public final class ClientMage {
	private ClientMage() {}

	public static MageData data = new MageData();
}
