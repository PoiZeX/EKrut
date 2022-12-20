package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.Message;
import common.TaskType;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class UsersManagementDBController {

	private static ArrayList<UserEntity> resultList;

	public static void getUnapprovedUsersEntity(ConnectionToClient client) {
			// sql query //
			ArrayList<UserEntity> res = getUnapprovedUsersFromDB();
			try {
				client.sendToClient(new Message(TaskType.RecieveUnapprovedUsers, res));
				System.out.println("Server: success");
			} catch (IOException e) {
				e.printStackTrace();
			} 

		}
	
	
	/**
	 * Handles the query of getting the user from DB
	 * @return
	 */
	protected static ArrayList<UserEntity> getUnapprovedUsersFromDB() {
		return resultList;
//		UserEntity user = new UserEntity();
//		try {
//			if (MySqlClass.getConnection() == null)
//				return user;
//			Connection conn = MySqlClass.getConnection();
//			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE username=?;");
//			ps.setString(1, username);
//			ResultSet res = ps.executeQuery();
//			if (res.next()) {
//				user = new UserEntity(res.getString(3),res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8),res.getString(11), res.getString(9), res.getBoolean(12), res.getBoolean(13));
//				user.setId(res.getInt(1));
//				if(res.getString(10) != null)  // region column
//					user.setRegion(res.getString(10));
//			}	
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return user;

	}
}
