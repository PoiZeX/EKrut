package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.Message;
import common.TaskType;
import entity.PersonalMessageEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class PersonalMessagesDBController {

	/**
	 * Handles getting selected report and sending the entity back to client
	 * 
	 * @param usernamePassword
	 * @param client
	 */
	public static void getAllPersonalMessages(UserEntity user, ConnectionToClient client) {
		if (user != null) {
			// SQL query //
			ArrayList<PersonalMessageEntity> res = getPersonalMessagesFromDB(user);
			try {
				client.sendToClient(new Message(TaskType.ReceivePersonalMessages, res));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Handles the query of getting all messages of specific user from DB
	 * 
	 * @return
	 */
	protected static ArrayList<PersonalMessageEntity> getPersonalMessagesFromDB(UserEntity user) {
		ArrayList<PersonalMessageEntity> messages = new ArrayList<PersonalMessageEntity>();
		PersonalMessageEntity entity = null;

		try {
			if (MySqlClass.getConnection() == null)
				return messages;

			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.personal_messages WHERE user_id=? ;");
			ps.setInt(1, user.getId());

			ResultSet res = ps.executeQuery();
			while (res.next()) {
				entity = new PersonalMessageEntity(res.getInt(2), res.getString(3), res.getString(4), res.getString(5));
				entity.setId(res.getInt(1));
				messages.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;

	}

	/**
	 * Handle inserting new personal message to table
	 * 
	 * @param user
	 * @return
	 */
	public static boolean setPersonalMessagesInDB(PersonalMessageEntity entity) {

		int res = 0;
		if (entity == null)
			return false;
		
		try {
			if (MySqlClass.getConnection() == null)
				return false;

			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO ekrut.personal_messages (user_id, date, type, message) VALUES (?, ?, ?, ?);");
			ps.setInt(1, entity.getUserId());
			ps.setString(2, entity.getDate());
			ps.setString(3, entity.getTitle());
			ps.setString(4, entity.getMessage());
			
			res = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res == 0 ? false : true;
	}

}
