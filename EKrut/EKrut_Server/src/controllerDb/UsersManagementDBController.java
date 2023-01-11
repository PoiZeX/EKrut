package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.SealedObject;

import common.CommonFunctions;
import common.Message;
import common.RolesEnum;
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
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE is_not_approved=? AND cc_number!=null;");
			ps.setInt(1, 1);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				UserEntity unapprovedUser = new UserEntity(res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9),
						res.getString(10), res.getString(11), res.getBoolean(12), res.getBoolean(13));
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

	/**
	 * Change users status from not-approved to approved
	 * 
	 * @param toApprove
	 * @param client
	 */
	public static void setUnapprovedUsersEntity(ArrayList<UserEntity> toApprove, ConnectionToClient client) {
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			for (UserEntity user : toApprove) {
				PreparedStatement ps = conn
						.prepareStatement("UPDATE ekrut.users SET is_not_approved = 0 WHERE username=?;");
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
	public static UserEntity getUserByUsernameOrIDFromDB(String[] details, ConnectionToClient client) {
		// prepare
		String idORusername = details[0];
		UserEntity user = new UserEntity();
		
		try {
			if (MySqlClass.getConnection() == null)
				return new UserEntity();
			Connection conn = MySqlClass.getConnection();
			String searchBy = "username";
			try {
				Integer.parseInt(idORusername);  // is ID
				searchBy = "id_number";
			} catch (Exception e) { }
			
			String query = "SELECT * FROM ekrut.users WHERE "+ searchBy + "=? AND cc_number is NULL;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, idORusername); 
			ResultSet res = ps.executeQuery();
			
			// create the entity
			while (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(client != null)  // will be null for internal use
				client.sendToClient(new Message(TaskType.ReceiveUserInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * Get a user by id number
	 * @param details
	 * @param client
	 * @return
	 */
	public static UserEntity getUserByIDNumberFromDB(String[] details, ConnectionToClient client) {
		String id_number = details[0];
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return new UserEntity();
			Connection conn = MySqlClass.getConnection();
			String query = "SELECT * FROM ekrut.users WHERE id_number=?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id_number);
			ResultSet res = ps.executeQuery();
			// create the entity
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
			if(client != null)  // will be null for internal use
				client.sendToClient(new Message(TaskType.ReceiveUserInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
		
	}
	
	
	public static UserEntity getUserByID(int userId) {
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return null;
			
			Connection conn = MySqlClass.getConnection();
			String query = "SELECT * FROM ekrut.users WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet res = ps.executeQuery();
			
			// create the entity
			if (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	/**
	 * Handles the query of changing the user's role type in DB
	 * 
	 * @return user
	 */
	public static boolean updateUserInDB(String[] details, ConnectionToClient client) {
		if(details.length < 1 && CommonFunctions.isNullOrEmpty(details[1])) 
			return false;
		String idNumber = details[0];
		String roleType = details[1];
		String region = details[2]; // Member OR Registered
		String creditCardNumber = details[3];
		String query = "UPDATE users SET role_type=? ,region=? ,cc_number=? WHERE id_number=?;";
		try {
			if (MySqlClass.getConnection() == null)
				return false;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, roleType);
			ps.setString(2, region);
			ps.setString(3, creditCardNumber);
			ps.setString(4, idNumber);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			
			UserEntity userToReturn = getUserByIDNumberFromDB(details, null);  // will return a single user
			client.sendToClient(new Message(TaskType.ReceiveUserUpdateInDB, userToReturn));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Handles the query of getting the region manager from DB
	 * 
	 * @return user
	 */

	public static UserEntity getRegionManagerFromDB(String region, ConnectionToClient client) {
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return new UserEntity();
			Connection conn = MySqlClass.getConnection();
			String query = "SELECT * FROM ekrut.users WHERE region=? AND role_type='regionManager';";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, region);
			ResultSet res = ps.executeQuery();

			while (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			client.sendToClient(new Message(TaskType.ReceiveManagerInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}
}
