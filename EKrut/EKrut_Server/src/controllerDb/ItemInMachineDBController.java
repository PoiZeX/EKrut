package controllerDb;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import common.Message;
import enums.TaskType;
import entity.ItemInMachineEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class ItemInMachineDBController.
 */
public class ItemInMachineDBController {
	/**
	This variable holds a connection to a MySQL database.
	*/
	private static Connection con = MySqlClass.getConnection();

	/**
	 * Gets the machine items.
	 *
	 * @param machineId the machine id
	 * @param client the client
	 * @return the machine items
	 */
	public void getMachineItems(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(
					"SELECT  ekrut.item_in_machine.*, ekrut.items.* " + "FROM  ekrut.item_in_machine, ekrut.items"
							+ " WHERE item_in_machine.machine_id=?  AND item_in_machine.item_id=items.item_id;");
			ps.setInt(1, machineId);
			handleGetItems(ps.executeQuery(), client);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*----------------------------------GETDATA----------------------------*/

	/**
	 * Gets the machine items with min amount.
	 *
	 * @param machineId the machine id
	 * @param client the client
	 * @return the machine items with min amount
	 */
	public void getMachineItemsWithMinAmount(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			int minAmount = MachineDBController.getMachineMinAmount(machineId);
			ps = con.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=? AND (item_in_machine.current_amount < ? OR  item_in_machine.call_status!=?) AND item_in_machine.item_id=items.item_id ;");
			ps.setInt(1, machineId);
			ps.setInt(2, minAmount);
			ps.setString(3, ItemInMachineEntity.Call_Status.NotOpened.toString());

			handleGetItems(ps.executeQuery(), client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the processed machine items.
	 *
	 * @param arr the arr
	 * @param client the client
	 * @return the processed machine items
	 */
	public void getProcessedMachineItems(int[] arr, ConnectionToClient client) {
		int machineId = arr[0];
		int userId = arr[1];
		try {	
			PreparedStatement ps = con.prepareStatement("SELECT  item_in_machine.*, items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=? AND  item_in_machine.call_status=? AND item_in_machine.worker_id=? AND  item_in_machine.item_id=items.item_id   ;");
			ps.setInt(1, machineId);
			ps.setString(2, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(3, userId);
			handleGetItems(ps.executeQuery(), client);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle get items.
	 *
	 * @param res the res
	 * @param client the client
	 */
	protected void handleGetItems(ResultSet res, ConnectionToClient client) {
		ItemInMachineEntity item;

		ArrayList<ItemInMachineEntity> itemsInMachine = new ArrayList<ItemInMachineEntity>();
		try {
			while (res.next()) {
				if (res.getMetaData().getColumnCount() >= 9) {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6),
							res.getString(8), res.getDouble(9), res.getString(10));
					InputStream input = null;
					try {
						input = this.getClass()
								.getResourceAsStream("/products/" + item.getItemImg().getImgName());
					} catch (Exception e) {
					}
					byte[] mybytearray = new byte[(int) input.available()];
					BufferedInputStream bis = new BufferedInputStream(input);
					item.getItemImg().initArray(mybytearray.length);
					item.getItemImg().setSize(mybytearray.length);
					bis.read(item.getItemImg().getMybytearray(), 0, mybytearray.length);
					bis.close();
				} else {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6),
							res.getString(7), 0.00, "");
				}

				itemsInMachine.add(item);
			}

			client.sendToClient(new Message(TaskType.ReceiveItemsInMachine, itemsInMachine)); // finally send the

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * Gets the item in machine quantity.
	 *
	 * @param item the item
	 * @return the item in machine quantity
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
	 * Update single item in machine.
	 *
	 * @param item the item
	 * @return the item in machine entity
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
	 * Decrease items amount in machine.
	 *
	 * @param itemsInMachine the items in machine
	 * @param client the client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void decreaseItemsAmountInMachine(Map<ItemInMachineEntity, Integer> itemsInMachine,
			ConnectionToClient client) throws IOException {
		try {
			// Setup
			String missingItems = "";
			ArrayList<ItemInMachineEntity> rollBackItems = new ArrayList<ItemInMachineEntity>();

			// iterate over requested items to decrease
			for (ItemInMachineEntity item : itemsInMachine.keySet()) {
				ItemInMachineEntity updatedItem = null; // set to null every iteration
				updatedItem = decreaseSingleItemAmount(item, itemsInMachine.get(item));

				// if the returned item wasn't null, there is an error
				if (updatedItem != null) {
					missingItems += String.format("%s -> your order: %d, current amount: %d\n", updatedItem.getName(),
							itemsInMachine.get(item), item.getCurrentAmount());
					increaseRollBack(itemsInMachine, rollBackItems);
					client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, missingItems));
					return;
				}
				// successful update, save it for roll back
				rollBackItems.add(item);
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, missingItems));
		} catch (Exception e) {
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, e.getStackTrace()));
		}
	}

	/**
	 * Decrease single item amount.
	 *
	 * @param item the item
	 * @param amountToDecrease the amount to decrease
	 * @return the item in machine entity
	 */
	public static ItemInMachineEntity decreaseSingleItemAmount(ItemInMachineEntity item, int amountToDecrease) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET current_amount=current_amount-? WHERE machine_id=? AND item_id=? AND ? <= current_amount;");
			ps.setInt(1, amountToDecrease);
			ps.setInt(2, item.getMachineId());
			ps.setInt(3, item.getId());
			ps.setInt(4, amountToDecrease);

			if (ps.executeUpdate() == 0) // update failed
				throw new SQLException();

		} catch (SQLException e) {
			item.setCurrentAmount(getItemInMachineQuantity(item)); // get the most updated amount
			return item;
		}
		return null;
	}

	/**
	 * Increase roll back.
	 *
	 * @param original the original
	 * @param successItems the success items
	 */
	private static void increaseRollBack(Map<ItemInMachineEntity, Integer> original,
			ArrayList<ItemInMachineEntity> successItems) {
		for (ItemInMachineEntity item : successItems) {
			increaseSingleItemAmount(item, original.get(item));
		}
	}

	/**
	 * Increase single item amount.
	 *
	 * @param item the item
	 * @param amountToIncrease the amount to increase
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
	 * Increase items under min.
	 *
	 * @param machineAnditemsId the machine anditems id
	 * @param client the client
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

	/**
	 * Update call status.
	 *
	 * @param item the item
	 */
	public static void updateCallStatus(ItemInMachineEntity item) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE ekrut.item_in_machine SET call_status=?, "
					+ "worker_id=? WHERE machine_id=? AND item_id=?;");
			ps.setString(1, item.getCallStatus().toString());
			ps.setInt(2, item.getWorkerId());
			ps.setInt(3, item.getMachineId());
			ps.setInt(4, item.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
