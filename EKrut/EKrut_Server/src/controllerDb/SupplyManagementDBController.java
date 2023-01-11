package controllerDb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	/**
	 * get machine minimum amount
	 * 
	 * @param machineId
	 * @return
	 */
	public static int getMachineMinAmount(int machineId) {
		int minAmount = -1;

		try {
			if (con == null)
				return minAmount;
			PreparedStatement ps1 = con.prepareStatement(
					"SELECT ekrut.machines.min_amount FROM ekrut.machines WHERE machines.machine_id=(?);");
			ps1.setInt(1, machineId);
			ResultSet res1;

			res1 = ps1.executeQuery();

			if (res1.next())
				minAmount = (res1.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return minAmount;
	}

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
			int minAmount = getMachineMinAmount(machineId);
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
	 * get items which call status = processed
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
			minAmount = getMachineMinAmount(machineId);

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
	 * handle the process of items and send to client
	 * 
	 * @param res
	 * @param client
	 */
	private static void handleGetItems(ResultSet res, ConnectionToClient client) {
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

					String LocalfilePath = "../EKrut_Server/src/styles/products/" + item.getItemImg().getImgName();
					File newFile = new File(LocalfilePath);
					byte[] mybytearray = new byte[(int) newFile.length()];
					FileInputStream fis = new FileInputStream(newFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					item.getItemImg().initArray(mybytearray.length);
					item.getItemImg().setSize(mybytearray.length);
					bis.read(item.getItemImg().getMybytearray(), 0, mybytearray.length);
				}
				// (machineId, item_id, currentAmount, callStatus, timeUnderMin, workerId, name,
				// price, item_img_nam)
				else {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6),
							res.getString(7), 0.00, "");
					item.setWorkerId(res.getInt(6));
				}

				itemsInMachine.add(item);
			}

			client.sendToClient(new Message(TaskType.ReceiveItemsInMachine, itemsInMachine)); // finally send the

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*-----------------------------------UPDATE--------------------------------*/
	/**
	 * Update minimum amount
	 * 
	 * @param obj
	 * @param client
	 */
	public static void updateMachineMinAmount(MachineEntity obj, ConnectionToClient client) {
		try {
			if (con == null)
				return;
			MachineEntity machine = obj;
			PreparedStatement ps = con
					.prepareStatement("UPDATE ekrut.machines SET min_amount=(?) WHERE machine_id=(?);");
			{
				ps.setInt(1, machine.getMinamount());
				ps.setInt(2, machine.machineId);
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * updateItemsInMachineUpdate method : input - array list of ItemInMachineEntity
	 * can update the fields of : int currentAmount,Call_Status callStatus,int
	 * timeUnderMin
	 * 
	 * @param itemsInMachine
	 * @param client
	 */

	/**
	 * Restock items in machine for supply upddate
	 * 
	 * @param itemsInMachine
	 * @param client
	 */
	public static void restockItemsInMachine(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {

		for (ItemInMachineEntity itemToIncrease : itemsInMachine) {
			ItemInMachineDBController.increaseSingleItemAmount(itemToIncrease, itemToIncrease.getCurrentAmount());
			updateCallStatus(itemToIncrease);
		}

	}

	/**
	 * increase amount for single items
	 * 
	 * @param item
	 * @param amountToIncrease
	 */
	private static void updateCallStatus(ItemInMachineEntity item) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE ekrut.item_in_machine SET call_status=?, "
					+ " WHERE worker_id=? AND machine_id=? AND item_id=?;");
			ps.setString(1, item.getCallStatus().toString());
			ps.setInt(2, item.getWorkerId());
			ps.setInt(3, item.getMachineId());
			ps.setInt(4, item.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get machines for user by there need for exemple : region manager - gets by
	 * region
	 * 
	 * @param arr
	 * @param client
	 */
	public static void getMachinesFromDB(String[] arr, ConnectionToClient client) {
		try {
			ArrayList<MachineEntity> res = new ArrayList<>();

			if (arr[0].equals("0")) {
				res = getMachineListForSupplyRegionMFromDB(arr[1]);
				client.sendToClient(new Message(TaskType.InitMachinesInRegions, res));
			} else {
				res = getMachineListForSupplyUpdateFromDB(Integer.parseInt(arr[1]));
				client.sendToClient(new Message(TaskType.InitMachinesSupplyUpdate, res));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get machines list for worker where therse a call open for him
	 * 
	 * @param workerId
	 * @return
	 */
	private static ArrayList<MachineEntity> getMachineListForSupplyUpdateFromDB(int workerId) {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return machines;
			Connection conn = MySqlClass.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT  ekrut.machines.* "
					+ "FROM ekrut.machines, ekrut.item_in_machine "
					+ "WHERE item_in_machine.call_status=(?) AND worker_id=(?) AND machines.machine_id=item_in_machine.machine_id "
					+ " GROUP BY machine_id;");
			ps.setString(1, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(2, workerId);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				machines.add(new MachineEntity(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4),
						res.getInt(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return machines;
	}

	/**
	 * get machines list for worker where there is a call open for him
	 * 
	 * @param regionName
	 * @return
	 */
	private static ArrayList<MachineEntity> getMachineListForSupplyRegionMFromDB(String regionName) {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return machines;
			Connection conn = MySqlClass.getConnection();

			PreparedStatement ps = conn.prepareStatement(
					"SELECT  ekrut.machines.* " + "FROM ekrut.machines " + "WHERE machines.region_name=(?) ;");
			ps.setString(1, regionName);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				machines.add(new MachineEntity(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4),
						res.getInt(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return machines;
	}

}
