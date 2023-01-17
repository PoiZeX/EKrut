package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import common.Message;
import enums.TaskType;
import entity.MachineEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class CommonDataDBController.
 */
public class CommonDataDBController {
	private static Connection con = MySqlClass.getConnection();
	
	/**
	 * Gets the all regions from DB.
	 *
	 * @param client the client
	 * the all regions from DB
	 */
	public static void getAllRegionsFromDB(ConnectionToClient client) {
		// SQL query //
		ArrayList<String> res = getRegionsListFromDB();
		try {
			client.sendToClient(new Message(TaskType.InitRegions, res));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Gets the regions list from DB.
	 *
	 * @return the regions list from DB
	 */
	public static ArrayList<String> getRegionsListFromDB() {
		ArrayList<String> regions = new ArrayList<String>();
		try {
			if (con == null)
				return regions;
		
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ekrut.regions;");
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				regions.add(res.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return regions;
	}
	
	/**
	 * Gets the region name by ID.
	 *
	 * @param regionID the region ID
	 * @return the region name by ID
	 */
	public static String getRegionNameByID(int regionID) {
		try {
			if (con == null)
				return "";
		
			PreparedStatement ps = con.prepareStatement("SELECT * FROM regions where id = ?;");
			ps.setInt(1, regionID);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				return res.getString(1);
			}
		} catch (SQLException e) { 	}
		return "";
	}
	
	/**
	 * Gets the all machines from DB.
	 *
	 * @param client the client
	 * return the all machines from DB
	 */
	public static void getAllMachinesFromDB(ConnectionToClient client) {
		ArrayList<MachineEntity> res = getMachineListFromDB();
		try {
			client.sendToClient(new Message(TaskType.InitMachines, res));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the machine list from DB.
	 *
	 * @return the machine list from DB
	 */
	public static ArrayList<MachineEntity> getMachineListFromDB() {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (con == null)
				return machines;
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ekrut.machines;");
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				machines.add(new MachineEntity(res.getInt(1),res.getString(2),res.getInt(3),res.getString(4),res.getInt(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return machines;
	}
	
	/**
	 * Gets the user by order id.
	 *
	 * @param OrderId the order id
	 * @param client the client
	 * @return the user by order id
	 */
	public static UserEntity getUserByOrderId(int OrderId, ConnectionToClient client) {
		UserEntity user = new UserEntity();
		try {
			if (con == null)
				return new UserEntity();

			String query = "SELECT ekrut.users.* FROM ekrut.orders, ekrut.users where orders.id=? And orders.user_id=users.id;";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, OrderId);
			ResultSet res = ps.executeQuery();

			if (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			client.sendToClient(new Message(TaskType.ReceiveUserByOrderIdFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

}
