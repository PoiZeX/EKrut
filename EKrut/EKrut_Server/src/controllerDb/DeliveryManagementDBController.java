package controllerDb;

import java.io.IOException;
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
	protected static void updateDeliveryEntities(ConnectionToClient client, ArrayList<DeliveryEntity> delivaryLst) {
		Statement stmt;
		try {
			if (MySqlClass.getConnection() == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (DeliveryEntity delivery : delivaryLst) {
				// TODO change this to another build method as we learned with question mark
				// (?)...
				stmt.executeUpdate("UPDATE deliveries SET deilvery_status=\"" + delivery.getStatus()
						+ "\", SubscriberNumber=\""+ "\" WHERE id=" + delivery.getOrderId() + ";");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 
	protected static void getTable(ConnectionToClient client) {
		Statement stmt;
		DeliveryEntity deliveryEntity;
		try {
			if (MySqlClass.getConnection() == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM deliveries;");
			while (rs.next()) {
				deliveryEntity = new DeliveryEntity(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5), (DeliveryStatus)(rs.getObject(6)));
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
