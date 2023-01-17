package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.Message;
import enums.TaskType;
import entity.PickupEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;
/**
 * Pickup DB controller handles all Reports queries
 * @author Lidor
 *
 */
public class PickupDBController {
	/**
	This variable holds a connection to a MySQL database.
	*/
	private static Connection con = MySqlClass.getConnection();
	
	/**
	 * Instantiates a new pickup DB controller.
	 */
	public PickupDBController() {
	}
	/**
	 * update pickup status to 'done'
	 * 
	 * @param orderId
	 * @param client
	 */
	public static void updatePickupStatus(int orderId, ConnectionToClient client) {
		try {
	
			if (con == null)
				return;
			PreparedStatement ps = con
					.prepareStatement("UPDATE ekrut.pickups SET pickup_status='done' WHERE order_id=?;");
			ps.setInt(1, orderId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * getting the pickup order for specific client from db if exist
	 * 
	 * @param details
	 * @param client
	 */
	public static void isPickupValid(String[] details, ConnectionToClient client) {
		PickupEntity pickup = null;
		try {
	
			if (con == null)
				return;
			PreparedStatement ps = con.prepareStatement("SELECT ekrut.pickups.* , ekrut.orders.machine_id "
					+ "FROM ekrut.pickups, ekrut.orders " + "WHERE orders.user_id=? AND pickups.order_id=? AND"
					+ " pickups.order_id=orders.id AND pickup_status!='done';");
			ps.setString(1, details[0]);
			ps.setString(2, details[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				PickupEntity.Status st = PickupEntity.Status.valueOf(rs.getString(2));
				pickup = new PickupEntity(rs.getInt(1), st, rs.getInt(3));
			}

			client.sendToClient(new Message(TaskType.ValidPickupAnswer, pickup)); // alredy pickup

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * insert new pickup entity
	 * 
	 * @param pickup
	 * @param client
	 */
	public static void insertPickupEntity(PickupEntity pickup, ConnectionToClient client) {
		try {
			if (con == null)
				return;
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO ekrut.pickups (order_id, pickup_status) VALUES (?, ?);");
			ps.setInt(1, pickup.getOrderId());
			ps.setString(2, pickup.getStatus().toString());
			ps.executeUpdate();
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
