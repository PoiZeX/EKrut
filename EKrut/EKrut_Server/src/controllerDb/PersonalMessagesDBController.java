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
	public static void getClientReportEntity(UserEntity user, ConnectionToClient client) {
		if (user != null) {
			// SQL query //
			ArrayList<PersonalMessageEntity> res = getClientsReportFromDB(user);
			try {
				client.sendToClient(new Message(TaskType.RecievePersonalMessages, res));
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
	protected static ArrayList<PersonalMessageEntity> getClientsReportFromDB(UserEntity user) {
		ArrayList<PersonalMessageEntity> messages = new ArrayList<PersonalMessageEntity>();
		PersonalMessageEntity entity = null;
		
		try {
			if (MySqlClass.getConnection() == null)
				return messages;
			
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.personal_messages WHERE user_id=? ;");
			ps.setInt(1, user.getId());
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				String[] yearMonthDay = res.getString(3).split("-");

				entity = new PersonalMessageEntity(res.getInt(1), 
						Integer.parseInt(yearMonthDay[0]), Integer.parseInt(yearMonthDay[1]),
						Integer.parseInt(yearMonthDay[2]),
						res.getString(4), res.getString(5));
				messages.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;

	}
}
