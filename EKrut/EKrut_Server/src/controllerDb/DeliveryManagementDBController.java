package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.CustomerStatus;
import common.DeliveryStatus;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.SaleEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class DeliveryManagementDBController {
	
	/**update estimates delivery time, delivery status and customer status*/
	public static void updateDeliveryEntities(ArrayList<DeliveryEntity> deliveryLst, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (DeliveryEntity delivery : deliveryLst) {
				PreparedStatement ps=con.prepareStatement("UPDATE ekrut.deliveries SET estimated_time=(?), "
						+ "deilvery_status=(?), customer_status=(?) WHERE order_id=(?);");
				ps.setString(1, delivery.getEstimatedTime());
				ps.setString(2, delivery.getDeliveryStatus().toString());
				ps.setString(3, delivery.getCustomerStatus().toString());
				ps.setInt(4, delivery.getOrderId());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	/**
	 * insert new delivery 
	 * @param deliveryEntity
	 * @param client
	 */
	public static void insertDeliveryEntity(DeliveryEntity deliveryEntity, ConnectionToClient client) {
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("INSERT INTO ekrut.deliveries (order_id, region,customer_id, address, estimated_time,deilvery_status,customer_status) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);");
			ps.setInt(1, deliveryEntity.getOrderId());
			ps.setString(1, deliveryEntity.getRegion());
			ps.setString(2,deliveryEntity.getCustomerId());
			ps.setString(3, deliveryEntity.getAddress());
			ps.setString(4, deliveryEntity.getEstimatedTime());
			ps.setString(5, deliveryEntity.getDeliveryStatus().toString());
			ps.setString(6, deliveryEntity.getCustomerStatus().toString());
			
			ps.executeUpdate();
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));  // send success msg
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));
			} catch (IOException e1) {
				e1.printStackTrace();
			}  
			
		}
	}
	
	/**
	 * get deliveries
	 * @param client
	 */
	public static void getTable(ConnectionToClient client) {
		Statement stmt;
		DeliveryEntity deliveryEntity;
		try {
			if (MySqlClass.getConnection() == null) {
				return;
			}
			stmt = (MySqlClass.getConnection()).createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ekrut.deliveries WHERE deilvery_status!='done';");
			
			while (rs.next()) {
				DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(rs.getString(6));
				CustomerStatus customerStatus= CustomerStatus.valueOf(rs.getString(7));
				deliveryEntity = new DeliveryEntity(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						 deliveryStatus, customerStatus);
				try {
					client.sendToClient(new Message(TaskType.ReceiveDeliveriesFromServer, deliveryEntity)); // finally send the entity
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * get specific delivery by customer id and order id
	 * @param details
	 * @param client
	 */
	public static void getDelivery(String[] details, ConnectionToClient client) {
		Statement stmt;
		DeliveryEntity deliveryEntity;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			
			stmt = (MySqlClass.getConnection()).createStatement();
			PreparedStatement ps=con.prepareStatement("SELECT * FROM ekrut.deliveries WHERE customer_id=? AND order_id=? "
					+ "And deilvery_status!='done';");
			ps.setString(1, details[0]);
			ps.setString(2, details[1]);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(rs.getString(6));
				CustomerStatus customerStatus= CustomerStatus.valueOf(rs.getString(7));
				deliveryEntity = new DeliveryEntity(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						 deliveryStatus, customerStatus);
					client.sendToClient(new Message(TaskType.ReceiveDeliveryFromServer, deliveryEntity)); // finally send the entity
			}
			else {
				client.sendToClient(new Message(TaskType.ReceiveDeliveryFromServer, null)); // delivery not exist
				}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
