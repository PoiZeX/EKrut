package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import common.Message;
import common.TaskType;
import entity.MachineEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class CommonDataDBController {
	public static void getAllRegionsFromDB(ConnectionToClient client) {
		// SQL query //
		ArrayList<String> res = getRegionsListFromDB();
		try {
			client.sendToClient(new Message(TaskType.InitRegions, res));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static ArrayList<String> getRegionsListFromDB() {
		ArrayList<String> regions = new ArrayList<String>();
		try {
			if (MySqlClass.getConnection() == null)
				return regions;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.regions;");
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				regions.add(res.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return regions;
	}

	public static void getAllMachinesFromDB(ConnectionToClient client) {
		ArrayList<MachineEntity> res = getMachineListFromDB();
		try {
			client.sendToClient(new Message(TaskType.InitMachines, res));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<MachineEntity> getMachineListFromDB() {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return machines;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.machines;");
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				machines.add(new MachineEntity(res.getInt(1),res.getString(2),res.getInt(3),res.getString(4),res.getInt(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return machines;
	}
	
	public static UserEntity getUserByOrderId(int OrderId, ConnectionToClient client) {
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return new UserEntity();
			Connection conn = MySqlClass.getConnection();
			String query = "SELECT ekrut.users.* FROM ekrut.orders, ekrut.users where orders.id=? And orders.user_id=users.id;";
			PreparedStatement ps = conn.prepareStatement(query);
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
