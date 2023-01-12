package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import common.Message;
import common.TaskType;
import entity.ItemInMachineEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class ItemInMachineDBController {
	private static Connection con = MySqlClass.getConnection();

	/**
	 * Update an array of items-in-machine with roll back option
	 * @param itemsInMachine
	 * @param client
	 */
	public static void updateItemsInMachine(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {
		try {
			ItemInMachineEntity[] itemsArray = new ItemInMachineEntity[itemsInMachine.size()];
			// ItemInMachineEntity[itemsInMachine.size()]);
			itemsInMachine.toArray(itemsArray);

			int[] originalAmount = new int[itemsArray.length]; // amount of items

			if (con == null)
				return;

			ItemInMachineEntity item = null;
			for (int i = 0; i < itemsArray.length; i++) {
				originalAmount[i] = getItemInMachineQuantity(itemsArray[i]);
				item = updateSingleItemInMachine(itemsArray[i]);
				if (item != null || originalAmount[i] == -1) {
					RollBack(originalAmount, itemsArray, i);
					client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));
					return;
				}
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Roll back when update failed
	 * 
	 * @param originalAmount
	 * @param itemsInMachine
	 * @param failedIndex
	 */
	private static void RollBack(int[] originalAmount, ItemInMachineEntity[] itemsInMachine, int failedIndex) {
		for (int i = failedIndex; i >= 0; i--) {
			itemsInMachine[i].setCurrentAmount(originalAmount[i]);
			updateSingleItemInMachine(itemsInMachine[i]);
		}
	}

	/**
	 * get quantity of item
	 * 
	 * @param item
	 * @return
	 */
	public static int getItemInMachineQuantity(ItemInMachineEntity item) {
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return -1;

			PreparedStatement ps = con
					.prepareStatement("SELECT current_amount FROM item_in_machine WHERE machine_id=? and item_id=?;");

			ps.setInt(1, item.getMachineId());
			ps.setInt(2, item.getItemId());

			ResultSet res = ps.executeQuery();
			if (res.next())
				return res.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;

	}

	/**
	 * update single item entity
	 * 
	 * @param item
	 * @return
	 */
	public static ItemInMachineEntity updateSingleItemInMachine(ItemInMachineEntity item) {

		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE ekrut.item_in_machine SET current_amount=?, call_status=?, "
							+ "worker_id=?,times_under_min=? WHERE  machine_id=? AND item_id=?;");
			ps.setInt(1, item.getCurrentAmount());
			ps.setString(2, item.getCallStatus().toString());
			ps.setInt(3, item.getWorkerId());
			ps.setInt(4, item.getTimeUnderMin());
			ps.setInt(5, item.getMachineId());
			ps.setInt(6, item.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			return item;
		}
		return null;
	}

	/**
	 * Update items in machine with roll back option if update failed
	 * 
	 * @param itemsInMachine
	 * @param client
	 * @throws IOException
	 */
	public static void decreaseItemsAmountInMachine(Map<ItemInMachineEntity, Integer> itemsInMachine,
			ConnectionToClient client) throws IOException {
		try {
			ArrayList<ItemInMachineEntity> rollBackItems = new ArrayList<ItemInMachineEntity>();

			for (ItemInMachineEntity item : itemsInMachine.keySet()) {
				ItemInMachineEntity updatedItem = null;
				updatedItem = decreaseSingleItemAmount(item, itemsInMachine.get(item));
				if (updatedItem != null) { // has error
					increaseRollBack(itemsInMachine, rollBackItems);
					client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));
					return;
				}
				rollBackItems.add(item);

			}

			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));

		} catch (Exception e) {
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));

			e.printStackTrace();
		}
	}

	/**
	 * decrease amount of single item
	 * 
	 * @param item
	 * @param amountToDecrease
	 * @return
	 */
	public static ItemInMachineEntity decreaseSingleItemAmount(ItemInMachineEntity item, int amountToDecrease) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET current_amount=current_amount-? WHERE machine_id=? AND item_id=?;");
			ps.setInt(1, amountToDecrease);
			ps.setInt(2, item.getMachineId());
			ps.setInt(3, item.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			return item;
		}
		return null;
	}

	/**
	 * increase the amount which decreased earlier. Update is NOT good.
	 * 
	 * @param original
	 * @param successItems
	 */
	private static void increaseRollBack(Map<ItemInMachineEntity, Integer> original,
			ArrayList<ItemInMachineEntity> successItems) {
		for (ItemInMachineEntity item : successItems) {
			increaseSingleItemAmount(item, original.get(item));
		}
	}

	/**
	 * increase amount for single items
	 * 
	 * @param item
	 * @param amountToIncrease
	 */
	public static void increaseSingleItemAmount(ItemInMachineEntity item, int amountToIncrease) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET current_amount=current_amount+? WHERE machine_id=? AND item_id=?;");
			ps.setInt(1, amountToIncrease);
			ps.setInt(2, item.getMachineId());
			ps.setInt(3, item.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Increase the times that item was under the minimum amount
	 * @param machineAnditemsId
	 * @param client
	 */
	public static void increaseItemsUnderMin(ArrayList<int[]> machineAnditemsId, ConnectionToClient client) {
		try {
			if (MySqlClass.getConnection() == null)
				return;
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET times_under_min=times_under_min+1 WHERE machine_id=? AND item_id=?");
			for (int[] machineItemId : machineAnditemsId) {
				ps.setInt(1, machineItemId[0]); // machine
				ps.setInt(2, machineItemId[1]); // item
				ps.executeUpdate();
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
