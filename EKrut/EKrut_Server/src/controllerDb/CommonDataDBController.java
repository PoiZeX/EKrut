package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import common.Message;
import common.TaskType;
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
	

}
