package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.Message;
import common.TaskType;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class OrderDBController {


	public static void isMemberFirstPurchase(UserEntity member, ConnectionToClient client) {
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE user_id=?;");
			ps.setInt(1, member.getId());
			ResultSet res = ps.executeQuery();

			if (!res.next()) // empty query = no orders for member
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));
			else 
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
