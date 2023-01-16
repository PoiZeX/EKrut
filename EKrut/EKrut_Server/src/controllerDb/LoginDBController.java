package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.Message;
import enums.TaskType;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class LoginDBController {

	private static String username, password;
	private static Connection con = MySqlClass.getConnection();
	/**
	 * Parse the string array into username and password
	 * 
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
	 * 
	 * @param usernamePassword
	 * @param client
	 */
	public static void getUserEntity(String[] usernamePassword, ConnectionToClient client) {
		if (setUser(usernamePassword)) {
			// sql query //
			UserEntity res = getUserFromDB();
			try {
				client.sendToClient(new Message(TaskType.ReceiveUserFromServerDB, res));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Handles the query of getting the user from DB
	 * 
	 * @return
	 */
	protected static UserEntity getUserFromDB() {
		UserEntity user = new UserEntity();
		try {
			if (con == null)
				return user;
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ekrut.users WHERE username=?;");
			ps.setString(1, username);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
				if (res.getString(10) != null) // region column
					user.setRegion(res.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;

	}
	/**
	 * The method setUserLoggedIn sets the user's logged_in status in the database based on the provided UserEntity
	 * @param user an instance of UserEntity class that holds the user information and the updated status of the user
	*/
	public static void setUserLoggedIn(UserEntity user) {
		try {
			if (con == null)
				return;
			PreparedStatement ps = con.prepareStatement("UPDATE ekrut.users SET logged_in=? WHERE username=?;");
			ps.setBoolean(1, user.isLogged_in());
			ps.setString(2, user.getUsername());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return;
	}

}
