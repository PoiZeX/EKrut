package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class UsersManagementDBController {

	public static void getUnapprovedUsersEntity(ConnectionToClient client) {
		// sql query //
		ArrayList<UserEntity> res = getUnapprovedUsersFromDB();
		try {
			client.sendToClient(new Message(TaskType.ReceiveUnapprovedUsers, res));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Handles the query of getting the from DB
	 * 
	 * @return
	 */
	protected static ArrayList<UserEntity> getUnapprovedUsersFromDB() {
		ArrayList<UserEntity> unapprovedUsersList = new ArrayList<UserEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return unapprovedUsersList;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE is_not_approved=?;");
			ps.setInt(1, 1);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				UserEntity unapprovedUser = new UserEntity(res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(11),
						res.getString(9), res.getBoolean(12), res.getBoolean(13));
				unapprovedUser.setId(res.getInt(1));
				if (res.getString(10) != null) // region column
					unapprovedUser.setRegion(res.getString(10));
				unapprovedUsersList.add(unapprovedUser);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unapprovedUsersList;
	}

	public static void setUnapprovedUsersEntity(ArrayList<UserEntity> toApprove, ConnectionToClient client) {
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			for (UserEntity user : toApprove) {
				PreparedStatement ps = conn.prepareStatement("UPDATE ekrut.users SET is_not_approved = 0 WHERE username=?;");
				ps.setString(1, user.getUsername());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			client.sendToClient(new Message(TaskType.ReceiveUsersApproval, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	/**
	 * Handles the query of getting the user from DB
	 * 
	 * @return user
	 */
	
	public static UserEntity getUserFromDB(String[] details, ConnectionToClient client) {
		String searchWhat = details[0];
		String query = "SELECT * FROM ekrut.users WHERE id_number=" + searchWhat + " AND role_type='user';";
		try {
			Integer.parseInt(searchWhat);
		} catch (Exception e) {
			searchWhat = "'" + searchWhat + "'";
			query = "SELECT * FROM ekrut.users WHERE username=" + searchWhat + " AND role_type='user';";
		}
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return new UserEntity();
			Connection conn = MySqlClass.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(11),
						res.getString(9), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
				if (res.getString(10) != null) // region column
					user.setRegion(res.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			client.sendToClient(new Message(TaskType.ReceiveUserInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * Handles the query of changing the user's role type in DB
	 * 
	 * @return user
	 */
	
	public static boolean updateUserRoleType(String[] details, ConnectionToClient client) {
		String searchWhat = details[1];
		String searchWho = details[0];
		String query = "UPDATE users SET role_type=? WHERE id_number=?;";

		try {
			if (MySqlClass.getConnection() == null)
				return false;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, searchWhat);
			ps.setString(2, searchWho);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			client.sendToClient(new Message(TaskType.ReceiveChangeUserRoleTypeInDB, "Changed Successfully"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
