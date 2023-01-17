package controllerDb;

import java.util.ArrayList;
import entity.ItemInMachineEntity;
import ocsf.server.ConnectionToClient;
/**
 * DB querys for supply management 
 * @author Lidor
 *
 */
public class SupplyManagementDBController {


	/*-----------------------------------UPDATE--------------------------------*/

	/**
	 * Restock items in machine for supply upddate
	 * 
	 * @param itemsInMachine
	 * @param client
	 */
	public static void restockItemsInMachine(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {

		for (ItemInMachineEntity itemToIncrease : itemsInMachine) {
			ItemInMachineDBController.increaseSingleItemAmount(itemToIncrease, itemToIncrease.getCurrentAmount());
			ItemInMachineDBController.updateCallStatus(itemToIncrease);
		}

	}

	/**
	 * @param itemsInMachine ArrayList of ItemInMachineEntity which needs to be updated
	 * @param client an instance of ConnectionToClient used to connect to
	 *  the database The method updateCallsStatus updates the
	 *  call status and worker_id of the items in the machine
	 *  table in the database. 
	 *  It iterates through the list of itemsInMachine and updates 
	 *  the status of each item by calling the updateCallStatus method.
	 */
	public static void updateCallsStatus(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {
		for (ItemInMachineEntity item : itemsInMachine)
			ItemInMachineDBController.updateCallStatus(item);

	}


}
