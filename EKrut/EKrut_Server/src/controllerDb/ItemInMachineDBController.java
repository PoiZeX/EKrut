package controllerDb;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
	 * get all machine items by machine id
	 * 
	 * @param machineId
	 * @param client
	 */
	public static void getMachineItems(int machineId, ConnectionToClient client) {
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
	/**
	 * handle the process of items and send to client
	 * 
	 * @param res
	 * @param client
	 */
	protected static void handleGetItems(ResultSet res, ConnectionToClient client) {
		ItemInMachineEntity item;
		ArrayList<ItemInMachineEntity> itemsInMachine = new ArrayList<ItemInMachineEntity>();
		try {
			while (res.next()) {
				// 1 2 3 4 5 6 7 8 9 10
				// machine_id, item_id, current_amount, call_status, times_under_min, worker_id,
				// item_id, name, price, item_img_name
				if (res.getMetaData().getColumnCount() >= 9) {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6),
							res.getString(8), res.getDouble(9), res.getString(10));
					InputStream input = null;
					try {
						 input = ItemDBController.class.getClass().getResourceAsStream("/styles/products/" + item.getItemImg().getImgName());
					} catch (Exception e) {}
					
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
			// Setup
			String missingItems = "";
			ArrayList<ItemInMachineEntity> rollBackItems = new ArrayList<ItemInMachineEntity>();

			// iterate over requested items to decrease
			for (ItemInMachineEntity item : itemsInMachine.keySet()) {
				ItemInMachineEntity updatedItem = null;  // set to null every iteration
				updatedItem = decreaseSingleItemAmount(item, itemsInMachine.get(item));
				
				// if the returned item wasn't null, there is an error
				if (updatedItem != null) { 
					missingItems += String.format("%s -> your order: %d, current amount: %d\n", updatedItem.getName(), itemsInMachine.get(item), item.getCurrentAmount());
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
	 * decrease amount of single item
	 * 
	 * @param item
	 * @param amountToDecrease
	 * @return
	 */
	public static ItemInMachineEntity decreaseSingleItemAmount(ItemInMachineEntity item, int amountToDecrease) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET current_amount=current_amount-? WHERE machine_id=? AND item_id=? AND ? <= current_amount;");
			ps.setInt(1, amountToDecrease);
			ps.setInt(2, item.getMachineId());
			ps.setInt(3, item.getId());
			ps.setInt(4, amountToDecrease);

			if(ps.executeUpdate() == 0)  // update failed
				throw new SQLException();
			
		} catch (SQLException e) {
			item.setCurrentAmount(getItemInMachineQuantity(item)); // get the most updated amount
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
	/**
	 *
	 * @param item an instance of ItemInMachineEntity which needs to be updated
	 *
	 *The method updateCallStatus updates the call status and worker_id of the item in the machine table in the database. 
	 *It uses a prepared statement to update the values in the database.
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
