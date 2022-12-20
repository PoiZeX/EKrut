package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.Message;
import common.TaskType;
import entity.SubscriberEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class LoginDbController {

	private static String username, password;

	/**
	 * Parse the string array into username and password
	 * @param usernamePassword
	 * @return
	 */
	public static boolean setUser(String[] usernamePassword) {
		if (usernamePassword.length == 2) {
			username = usernamePassword[0];
			password = usernamePassword[1];
			return true;
		}
		return false;
	}

	/**
	 * Handles getting selected user and sending the entity back to client
	 * @param usernamePassword
	 * @param client
	 */
	public static void getUserEntity(String[] usernamePassword, ConnectionToClient client) {
		if (setUser(usernamePassword)) {
			// sql query //
			UserEntity res = getUserFromDB();
			try {
				client.sendToClient(new Message(TaskType.UserFromServerDB, res));
				System.out.println("Server: success");
			} catch (IOException e) {
				e.printStackTrace();
			} 

		}
	}
	
	/**
	 * Handles the query of getting the user from DB
	 * @return
	 */
	protected static UserEntity getUserFromDB() {
		UserEntity user = new UserEntity("","","","","","","", false, false);
		try {
			if (MySqlClass.getConnection() == null)
				return user;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE username=?;");
			ps.setString(1, username);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				user = new UserEntity(res.getString(2),res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7),res.getString(8), res.getBoolean(10), res.getBoolean(11));
				user.setId(res.getInt(1));
				if(res.getString(8) != null)  // region column
					user.setRegion(res.getString(8));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;

	}
}
