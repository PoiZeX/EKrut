package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import common.CommonFunctions;
import common.Message;
import enums.TaskType;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class UsersManagementDBController.
 */
public class UsersManagementDBController {
	
	/**
	 * Gets the unapproved users entity.
	 *
	 * @param client the client
	 * return the unapproved users entity
	 */
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
	 * Gets the unapproved users from DB.
	 *
	 * @return the unapproved users from DB
	 */
	protected static ArrayList<UserEntity> getUnapprovedUsersFromDB() {
		ArrayList<UserEntity> unapprovedUsersList = new ArrayList<UserEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return unapprovedUsersList;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM ekrut.users WHERE is_not_approved=? AND cc_number is not null;");
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
	 * Sets the unapproved users entity.
	 *
	 * @param toApprove the to approve
	 * @param client the client
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
	 * Gets the user by username or ID from DB.
	 *
	 * @param details the details
	 * @param client the client
	 * @return the user by username or ID from DB
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
				Integer.parseInt(idORusername); // is ID
				searchBy = "id_number";
			} catch (Exception e) {
			}

			String query = "SELECT * FROM ekrut.users WHERE " + searchBy + "=? AND cc_number is NULL;";
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
			if (client != null) // will be null for internal use
				client.sendToClient(new Message(TaskType.ReceiveUserInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Gets the user by ID number from DB.
	 *
	 * @param details the details
	 * @param client the client
	 * @return the user by ID number from DB
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
			if (client != null) // will be null for internal use
				client.sendToClient(new Message(TaskType.ReceiveUserInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;

	}

	/**
	 * Gets the user by ID.
	 *
	 * @param userId the user id
	 * @return the user by ID
	 */
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
	 * Update user in DB.
	 *
	 * @param details the details
	 * @param client the client
	 * @return true, if successful
	 */
	public static boolean updateUserInDB(String[] details, ConnectionToClient client) {
		if (details.length < 1 && CommonFunctions.isNullOrEmpty(details[1]))
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

			UserEntity userToReturn = getUserByIDNumberFromDB(details, null); // will return a single user
			client.sendToClient(new Message(TaskType.ReceiveUserUpdateInDB, userToReturn));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Gets the region manager from DB.
	 *
	 * @param region the region
	 * @param client the client
	 * @return the region manager from DB
	 */

	public static UserEntity getRegionManagerFromDB(String region, ConnectionToClient client) {
		UserEntity user = getRegionManagerFromDBQuery(region);
		try {
			client.sendToClient(new Message(TaskType.ReceiveManagerInfoFromServerDB, user));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;

	}
	
	/**
	 * Gets the region manager from DB query.
	 *
	 * @param region the region
	 * @return the region manager from DB query
	 */
	public static UserEntity getRegionManagerFromDBQuery(String region) {
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
		return user;
	}

	/**
	 * Gets the supply workers query.
	 *
	 * @return the supply workers query
	 * @throws SQLException the SQL exception
	 */
	private static ArrayList<UserEntity> getSupplyWorkersQuery() throws SQLException {
		ArrayList<UserEntity> supplyWorkers = new ArrayList<>();
		UserEntity user = new UserEntity();
		if (MySqlClass.getConnection() == null)
			return supplyWorkers;
		Connection conn = MySqlClass.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE role_type=?;");
		ps.setString(1, "supplyWorker");

		ResultSet res = ps.executeQuery();
		while (res.next()) {
			user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
					res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
					res.getString(11), res.getBoolean(12), res.getBoolean(13));
			user.setId(res.getInt(1));

			supplyWorkers.add(user);
		}
		return supplyWorkers;
	}

	/**
	 * Gets the supply workers.
	 *
	 * @param client the client
	 * return the supply workers
	 */
	public static void getSupplyWorkers(ConnectionToClient client) {
		try {
			client.sendToClient(new Message(TaskType.ReceiveSupplyWorkersFromServer, getSupplyWorkersQuery())); // finally
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the all users from DB.
	 *
	 * @param client the client
	 * return the all users from DB
	 */
	public static void getAllUsersFromDB(ConnectionToClient client) {
		try {
			client.sendToClient(new Message(TaskType.InitUsers, getAllUsers())); // finally
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the all users.
	 *
	 * @return the all users
	 */
	public static HashMap<String,String> getAllUsers() {
		HashMap<String, String> users = new HashMap<>();
		if (MySqlClass.getConnection() == null)
			return users;
		try {

			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users;");
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				String username = res.getString(3);
				String password = res.getString(4);
				users.put(username,password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

}
