package wolforce.net;

import net.minecraftforge.fml.relauncher.Side;
import wolforce.Vaultopic;
import wolforce.net.VTDeliver.VTDeliverHandler;
import wolforce.net.VTRequest.VTRequestHandler;

public class VaultopicClientNet {

	public static void init() {

		Vaultopic.NET.registerMessage(VTRequestHandler.class, VTRequest.class, MessageType.REQUEST.ordinal(),
				/** The fourth parameter is the side that your packet will be *received* on: */
				Side.SERVER);

		Vaultopic.NET.registerMessage(VTDeliverHandler.class, VTDeliver.class, MessageType.DELIVER.ordinal(),
				/** The fourth parameter is the side that your packet will be *received* on: */
				Side.CLIENT);
	}
}
