package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import enums.CustomerStatusEnum;
import enums.DeliveryStatusEnum;
import common.Message;
import enums.TaskType;
import entity.DeliveryEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;
/**
*
* Contains delivery DB logic
*
*/
public class DeliveryManagementDBController {
	private static Connection con = MySqlClass.getConnection();
	/**update estimates delivery time, delivery status and customer status in DB*/
	public static void updateDeliveryEntities(ArrayList<DeliveryEntity> deliveryLst, ConnectionToClient client) {
		
		try {
			if (con == null)
				return;

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
	 * insert new delivery to DB
	 * @param deliveryEntity
	 * @param client
	 */
	public static void insertDeliveryEntity(DeliveryEntity deliveryEntity, ConnectionToClient client) {
		try {
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("INSERT INTO ekrut.deliveries (order_id, region,address, estimated_time,deilvery_status,customer_status) "
					+ "VALUES (?, ?, ?, ?, ?, ?);");
			ps.setInt(1, deliveryEntity.getOrderId());
			ps.setString(2, deliveryEntity.getRegion());
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
	 * get deliveries from DB
	 * @param client
	 */
	public static void getTable(String region, ConnectionToClient client) {
		DeliveryEntity deliveryEntity;
		ArrayList<DeliveryEntity> deliveries=new ArrayList<DeliveryEntity>();
		try {
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("SELECT * FROM ekrut.deliveries WHERE deilvery_status!='done' And region=?;");
			ps.setString(1, region);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.valueOf(rs.getString(5));
				CustomerStatusEnum customerStatus= CustomerStatusEnum.valueOf(rs.getString(6));
				deliveryEntity = new DeliveryEntity(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
						 deliveryStatus, customerStatus);
				deliveries.add(deliveryEntity);
			}
			client.sendToClient(new Message(TaskType.ReceiveDeliveriesFromServer, deliveries));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get specific delivery by customer id and order id
	 * @param details
	 * @param client
	 */
	public static void getDelivery(String[] details, ConnectionToClient client) {
		DeliveryEntity deliveryEntity=null;
		try {
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("SELECT ekrut.deliveries.* "
					+ "FROM ekrut.deliveries, ekrut.orders"
					+ " WHERE orders.user_id=? AND deliveries.order_id=? AND "
					+ "deliveries.order_id=orders.id AND deilvery_status!='done';");
			ps.setString(1, details[0]);
			ps.setString(2, details[1]);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.valueOf(rs.getString(5));
				CustomerStatusEnum customerStatus= CustomerStatusEnum.valueOf(rs.getString(6));
				deliveryEntity = new DeliveryEntity(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),
						 deliveryStatus, customerStatus);
					 // finally send the entity
			}
			client.sendToClient(new Message(TaskType.ReceiveDeliveryFromServer, deliveryEntity)); // delivery not exist
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
