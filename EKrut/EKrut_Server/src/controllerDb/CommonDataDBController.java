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
/***
 * CommonDataDBController contains methods that interact with the database to get data
 * for different screens.
 * This class has methods for getting the regions, machines and UserEntity by order id
 * from the database.
 */
public class CommonDataDBController {
	private static Connection con = MySqlClass.getConnection();
	/**
	 * This method retrieves all the regions from the database and sends it to the client
	 * @param client The client that requested the data
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
	 * This method retrieves all regions from the database
	 * @return ArrayList<String>  regions from the database
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
	 * This method retrieves a region by its ID from the database
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
	 * This method retrieves all the machines from the database and sends it to the client
	 * @param client The client that requested the data
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
	 * This method retrieves all machines from the database
	 * @return ArrayList<MachineEntity> machines from the database
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
	* getUserByOrderId method is used to retrive the user information by providing the order id.
	* This method performs a SELECT SQL statement on the ekrut.users and ekrut.orders table.
	* the connection object is getting the connection from MySqlClass class.
	* @param OrderId an integer representing the id of an order
	* @param client an object of ConnectionToClient class that is used to communicate with the client
	* @return UserEntity an object of UserEntity class representing the user.
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
