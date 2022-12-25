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
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class DeliveryManagementDBController {
	
	/*update delivery status*/
	public static void updateDeliveryEntities(ArrayList<DeliveryEntity> deliveryLst, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (DeliveryEntity delivery : deliveryLst) {
				PreparedStatement ps=con.prepareStatement("UPDATE ekrut.deliveries SET estimated_time=(?), actual_time=(?), "
						+ "deilvery_status=(?), customer_status=(?) WHERE order_id=(?);");
				ps.setString(1, delivery.getEstimatedTime());
				ps.setString(2, delivery.getActualTime());
				ps.setString(3, delivery.getDeliveryStatus().toString());
				ps.setString(4, delivery.getCustomerStatus().toString());
				ps.setInt(5, delivery.getOrderId());
				ps.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 
	public static void getTable(ConnectionToClient client) {
		Statement stmt;
		DeliveryEntity deliveryEntity;
		try {
			if (MySqlClass.getConnection() == null) {
				System.out.println("conn null/n");
				return;
			}
			stmt = (MySqlClass.getConnection()).createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ekrut.deliveries WHERE deilvery_status!='done';");
			
			while (rs.next()) {
				DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(rs.getString(7));
				CustomerStatus customerStatus= CustomerStatus.valueOf(rs.getString(8));
				deliveryEntity = new DeliveryEntity(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5),rs.getString(6), deliveryStatus, customerStatus);
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
}
