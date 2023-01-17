package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.Message;
import enums.TaskType;
import entity.PersonalMessageEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class PersonalMessagesDBController.
 */
public class PersonalMessagesDBController {
	/**
	This variable holds a connection to a MySQL database.
	*/
	private static Connection con = MySqlClass.getConnection();
	 
	/**
	 * Gets the all personal messages.
	 *
	 * @param user the user
	 * @param client the client
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
	 * Gets the personal messages from DB.
	 *
	 * @param user the user
	 * @return the personal messages from DB
	 */
	protected static ArrayList<PersonalMessageEntity> getPersonalMessagesFromDB(UserEntity user) {
		ArrayList<PersonalMessageEntity> messages = new ArrayList<PersonalMessageEntity>();
		PersonalMessageEntity entity = null;

		try {
			if (con == null)
				return messages;

			PreparedStatement ps = con.prepareStatement("SELECT * FROM ekrut.personal_messages WHERE user_id=? ;");
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
	 * Sets the personal messages in DB.
	 *
	 * @param entity the entity
	 * @return true, if successful
	 */
	public static boolean setPersonalMessagesInDB(PersonalMessageEntity entity) {

		int res = 0;
		if (entity == null || con == null)
			return false;
		
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO ekrut.personal_messages (user_id, date, type, message) VALUES (?, ?, ?, ?);");
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
