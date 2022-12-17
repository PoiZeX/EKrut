package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.SubscriberEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class LoginDbController {

	private static String username, password;

	public static boolean setUser(String[] usernamePassword) {
		if (usernamePassword.length == 2) {
			username = usernamePassword[0];
			password = usernamePassword[1];
			return true;
		}
		return false;
	}

	public static void getUserEntity(String[] usernamePassword, ConnectionToClient client) {
		if (setUser(usernamePassword)) {
			// sql query //
			UserEntity res = getUserFromDB();
			try {
				client.sendToClient(res);
				System.out.println("Server: success");
			} catch (IOException e) {
				e.printStackTrace();
			} 

		}
	}
	
	protected static UserEntity getUserFromDB() {
		Statement stmt;
		UserEntity user = new UserEntity("","","","","","","");
		try {
			if (MySqlClass.getConnection() == null)
				return user;
			Connection conn = MySqlClass.getConnection();
			//stmt = MySqlClass.getConnection().createStatement();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE username=?;");
			ps.setString(1, username);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				user = new UserEntity(res.getString(2),res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7),res.getString(8));
				user.setId(res.getInt(1));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;

	}
}
