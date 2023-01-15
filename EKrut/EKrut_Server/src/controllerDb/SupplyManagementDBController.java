package controllerDb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import common.Message;
import common.TaskType;
import entity.ItemInMachineEntity;
import entity.UserEntity;
import entity.MachineEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class SupplyManagementDBController {
	private static Connection con = MySqlClass.getConnection();

	/*----------------------------------GETDATA----------------------------*/

	/**
	 * get machine items according to minimum amount
	 * 
	 * @param machineId
	 * @param client
	 */
	public static void getMachineItemsWithMinAmount(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			int minAmount = MachineDBController.getMachineMinAmount(machineId);
			ps = con.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=? AND (item_in_machine.current_amount < ? OR  item_in_machine.call_status!=?) AND item_in_machine.item_id=items.item_id ;");
			ps.setInt(1, machineId);
			ps.setInt(2, minAmount);
			ps.setString(3, ItemInMachineEntity.Call_Status.NotOpened.toString());

			ItemInMachineDBController.handleGetItems(ps.executeQuery(), client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * get items which call status = processed and they are opened for the user
	 * 
	 * @param arr
	 * @param client
	 */
	public static void getProcessedMachineItems(int[] arr, ConnectionToClient client) {
		int machineId = arr[0];
		int userId = arr[1];
		int minAmount = -1;
		ArrayList<ItemInMachineEntity> itemsInMachine = new ArrayList<ItemInMachineEntity>();
		try {
			minAmount = MachineDBController.getMachineMinAmount(machineId);

			PreparedStatement ps = con.prepareStatement("SELECT  item_in_machine.*, items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=? AND  item_in_machine.call_status=? AND item_in_machine.worker_id=? AND  item_in_machine.item_id=items.item_id   ;");
			ps.setInt(1, machineId);
			ps.setString(2, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(3, userId);
			ItemInMachineDBController.handleGetItems(ps.executeQuery(), client);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



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
