package controllerDb;

import java.util.ArrayList;
import entity.ItemInMachineEntity;
import ocsf.server.ConnectionToClient;

/**
 * The Class SupplyManagementDBController.
 */
public class SupplyManagementDBController {


	/*-----------------------------------UPDATE--------------------------------*/

	/**
	 * Restock items in machine.
	 *
	 * @param itemsInMachine the items in machine
	 * @param client the client
	 */
	public static void restockItemsInMachine(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {

		for (ItemInMachineEntity itemToIncrease : itemsInMachine) {
			ItemInMachineDBController.increaseSingleItemAmount(itemToIncrease, itemToIncrease.getCurrentAmount());
			ItemInMachineDBController.updateCallStatus(itemToIncrease);
		}

	}

	/**
	 * Update calls status.
	 *
	 * @param itemsInMachine the items in machine
	 * @param client the client
	 */
	public static void updateCallsStatus(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {
		for (ItemInMachineEntity item : itemsInMachine)
			ItemInMachineDBController.updateCallStatus(item);

	}


}
