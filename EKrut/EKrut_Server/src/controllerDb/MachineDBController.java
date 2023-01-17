package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.Message;
import enums.TaskType;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class MachineDBController.
 */
public class MachineDBController {
	/**
	This variable holds a connection to a MySQL database.
	*/
	private static Connection con = MySqlClass.getConnection();
	
	/**
	 * Instantiates a new machine DB controller.
	 */
	public MachineDBController() {
	}
//-------------------------------------------GET
	/**
 * Gets the machine min amount.
 *
 * @param machineId the machine id
 * @return the machine min amount
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
	
	/**
	 * Gets the machines from DB.
	 *
	 * @param arr the arr
	 * @param client the client
	 * return the machines from DB
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
	 * Gets the machine list for supply update from DB.
	 *
	 * @param workerId the worker id
	 * @return the machine list for supply update from DB
	 */
	private static ArrayList<MachineEntity> getMachineListForSupplyUpdateFromDB(int workerId) {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (con == null)
				return machines;

			PreparedStatement ps = con.prepareStatement("SELECT  ekrut.machines.* "
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
	 * Gets the machine list for supply region M from DB.
	 *
	 * @param regionName the region name
	 * @return the machine list for supply region M from DB
	 */
	private static ArrayList<MachineEntity> getMachineListForSupplyRegionMFromDB(String regionName) {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (con == null)
				return machines;

			PreparedStatement ps = con.prepareStatement(
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


//-------------------------------------------UPDATE
	/**
 * Update machine min amount.
 *
 * @param obj the obj
 * @param client the client
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


}
