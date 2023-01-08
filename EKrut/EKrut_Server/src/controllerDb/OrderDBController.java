package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import javax.net.ssl.SSLEngineResult.Status;

import common.CustomerStatus;
import common.DeliveryStatus;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.OrderEntity;
import entity.PersonalMessageEntity;
import entity.PickupEntity;
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

	public static void insertOrderEntity(OrderEntity entity, ConnectionToClient client) {

		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date date = new Date();
			PreparedStatement ps = con.prepareStatement("INSERT INTO orders "
					+ "(machine_id, total_sum, user_id, buytime, products_amount, supply_method) VALUES "
					+ "(?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, entity.getMachine_id());
			ps.setInt(2, entity.getTotal_sum());
			ps.setInt(3, entity.getUser_id());
			ps.setString(4, formatter.format(date));
			ps.setInt(5, entity.getProductsAmount());
			ps.setString(6, entity.getSupplyMethod());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			int generatedKey = -1;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, generatedKey)); // send success msg
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, -1)); // send not success msg
				return;
			} catch (Exception ex) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * update pickup status to 'done'
	 * @param orderId
	 * @param client
	 */
	public static void updatePickupStatus(int orderId, ConnectionToClient client) {
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null) 
				return;
			PreparedStatement ps=con.prepareStatement("UPDATE ekrut.pickups SET pickup_status='done' WHERE order_id=?;");
			ps.setInt(1, orderId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * getting the pickup order for specific client from db if exist
	 * @param details
	 * @param client
	 */
	public static void isPickupValid(String[] details, ConnectionToClient client) {
		PickupEntity pickup=null;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("SELECT ekrut.pickups.* , ekrut.orders.machine_id "
					+ "FROM ekrut.pickups, ekrut.orders "
					+ "WHERE orders.user_id=? AND pickups.order_id=? AND"
					+ " pickups.order_id=orders.id AND pickup_status!='done';");
			ps.setString(1, details[0]);
			ps.setString(2, details[1]);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				PickupEntity.Status st=PickupEntity.Status.valueOf(rs.getString(2));
				pickup=new PickupEntity(rs.getInt(1),st ,rs.getInt(3));
			}
			
			client.sendToClient(new Message(TaskType.ValidPickupAnswer, pickup)); //alredy pickup
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * insert new pickup entity
	 * @param pickup
	 * @param client
	 */
	public static void insertPickupEntity(PickupEntity pickup, ConnectionToClient client) {
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("INSERT INTO ekrut.pickups (order_id, pickup_status) VALUES (?, ?);");
			ps.setInt(1, pickup.getOrderId());
			PickupEntity.Status s=pickup.getStatus();
			ps.setString(2, s.toString());
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
