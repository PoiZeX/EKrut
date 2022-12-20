package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.DeliveryStatus;
import entity.DeliveryEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class DeliveryManagementDBController {
	
	/*update delivery status*/
	public static void updateDeliveryEntities(ConnectionToClient client, ArrayList<DeliveryEntity> delivaryLst) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (DeliveryEntity delivery : delivaryLst) {
				// TODO change this to another build method as we learned with question mark
				// (?)...
				PreparedStatement ps=con.prepareStatement("UPDATE ekrut.deliveries SET estimated_time=(?), actual_time=(?), "
						+ "deilvery_status=(?) WHERE order_id=(?);");
				ps.setString(1, delivery.getEstimatedTime());
				ps.setString(2, delivery.getActualTime());
				ps.setString(3, delivery.getStatus().toString());
				ps.setInt(4, delivery.getOrderId());
				ps.executeUpdate();
				//stmt.executeUpdate("UPDATE ekrut.deliveries SET deilvery_status=\"" + delivery.getStatus().toString()
						//+ "\" WHERE order_id=" + delivery.getOrderId() + ";");

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
			ResultSet rs = stmt.executeQuery("SELECT * FROM ekrut.deliveries;");
			
			while (rs.next()) {
				DeliveryStatus status = DeliveryStatus.valueOf(rs.getString(6));
				deliveryEntity = new DeliveryEntity(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5), status);
				try {
					client.sendToClient(deliveryEntity); // finally send the entity
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
