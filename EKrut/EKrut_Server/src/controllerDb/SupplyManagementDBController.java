package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.OrderReportEntity;
import entity.UserEntity;
import entity.ItemInMachineEntity.Call_Status;
import entity.MachineEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class SupplyManagementDBController {
	private static boolean pros = false;
	private static Connection conn = MySqlClass.getConnection();

	/**
	 * get machine minimum amount
	 * @param machineId
	 * @return
	 */
	public static int getMachineMinAmount(int machineId) {
		int minAmount = -1;

		try {
			if (conn == null)
				return minAmount;
			PreparedStatement ps1 = conn.prepareStatement(
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
	 * @param machineId
	 * @param client
	 */
	public static void getMachineItemsWithMinAmount(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			int minAmount = getMachineMinAmount(machineId);
			ps = conn.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.item_id, ekrut.items.name "
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
				// 1			2			3				4			5				6			7		8	9			10			11			12				
				// machine_id, item_id, current_amount, call_status, times_under_min, worker_id, item_id, name, price, manufacturer, description, item_img_name

				if(res.getString(9) != null) {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6), res.getString(8), res.getDouble(9), res.getString(10), res.getString(11), res.getString(12));
					
				}
				else {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6), "", 0, "", "", "");
					
				}
				itemsInMachine.add(item);
			}

			client.sendToClient(new Message(TaskType.ReceiveItemsInMachine, itemsInMachine)); // finally send the

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * get all machine items by machine id
	 * @param machineId
	 * @param client
	 */
	public static void getMachineItems(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(
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
	 * @param arr
	 * @param client
	 */
	public static void getProcessedMachineItems(int[] arr, ConnectionToClient client) {
		ItemInMachineEntity item;
		int machineId = arr[0];
		int userId = arr[1];
		int minAmount = -1;
		ArrayList<ItemInMachineEntity> itemsInMachine = new ArrayList<ItemInMachineEntity>();
		try {
			minAmount = getMachineMinAmount(machineId);
			
			PreparedStatement ps = conn.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=(?) AND  item_in_machine.call_status=(?) AND item_in_machine.item_id=items.item_id AND item_in_machine.worker_id=(?)  ;");
			ps.setInt(1, machineId);
			ps.setString(2, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(3, userId);
			handleGetItems(ps.executeQuery(), client);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get supply workers from db
	 * 
	 * @param client
	 */
	public static void getSupplyWorkers(ConnectionToClient client) {
		ArrayList<UserEntity> supplyWorkers = new ArrayList<>();
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE role_type=?;");
			ps.setString(1, "supplyWorker");

			ResultSet res = ps.executeQuery();
			while (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));

				supplyWorkers.add(user);
			}
			try {
				client.sendToClient(new Message(TaskType.ReceiveSupplyWorkersFromServer, supplyWorkers)); // finally
																											// send the
																											// entity
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*-----------------------------------UPDATE--------------------------------*/
	/**
	 * 
	 * @param obj
	 * @param client
	 */
	public static void updateMachineMinAmount(MachineEntity obj, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
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
	public static void updateItemsInMachine(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (ItemInMachineEntity item : itemsInMachine) {
				PreparedStatement ps = con
						.prepareStatement("UPDATE ekrut.item_in_machine SET current_amount=(?), call_status=(?), "
								+ "worker_id=(?),times_under_min=(?) WHERE  machine_id=(?) AND item_id=(?) ;");
				ps.setInt(1, item.getCurrentAmount());
				ps.setString(2, item.getCallStatus().toString());
				ps.setInt(3, item.getWorkerId());
				ps.setInt(4, item.getTimeUnderMin());
				ps.setInt(5, item.getMachineId());
				ps.setInt(6, item.getId());

				ps.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
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
					+ "WHERE item_in_machine.call_status=(?) AND worker_id=(?) AND machines.machine_id=item_in_machine.machine_id ;");
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
