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

/**
 * The Class LoginDBController.
 */
public class LoginDBController {

	private static String username, password;
	private static Connection con = MySqlClass.getConnection();
	
	/**
	 * Sets the user.
	 *
	 * @param usernamePassword the username password
	 * @return true, if successful
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
	 * Gets the user entity.
	 *
	 * @param usernamePassword the username password
	 * @param client the client
	 * return the user entity
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
	 * Gets the user from DB.
	 *
	 * @return the user from DB
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
	 * Sets the user logged in.
	 *
	 * @param user the new user logged in
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
