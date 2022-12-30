package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import common.CustomerStatus;
import common.DeliveryStatus;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.SaleEntity;
import entity.SaleEntity.SaleStatus;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class MarketingManagerDBController {
	public static void insertSaleEntities(SaleEntity saleEntity, ConnectionToClient client) {
		
		try {
			
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			
			PreparedStatement ps=con.prepareStatement("INSERT INTO ekrut.sales (region, sale_type,days, start_time, end_time) "
					+ "VALUES (?, ?, ?, ?, ?);");
			ps.setString(1,saleEntity.getRegion());
			ps.setString(2,saleEntity.getSaleType());
			ps.setString(3,saleEntity.getDays());
			ps.setString(4, saleEntity.getStartTime().toString());
			ps.setString(5, saleEntity.getEndTime().toString());
			
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*update sale status*/
	public static void updateSaleEntities(ArrayList<SaleEntity> saleLst, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (SaleEntity saleEntity : saleLst) {
				PreparedStatement ps=con.prepareStatement("UPDATE ekrut.sales SET sale_sataus=? WHERE id=?;");
				ps.setString(1, saleEntity.getSaleStatus().toString());
				ps.setInt(2, saleEntity.getSaleID());
				ps.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void getSales(ConnectionToClient client) {
		Statement stmt;
		SaleEntity saleEntity;
		SaleStatus saleStatus;
		try {
			if (MySqlClass.getConnection() == null) {
				return;
			}
			stmt = (MySqlClass.getConnection()).createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ekrut.sales;");
			
			while (rs.next()) {
				
				saleStatus=SaleStatus.valueOf(rs.getString(7));
				saleEntity = new SaleEntity(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)
						 , LocalTime.parse( rs.getString(5)), LocalTime.parse(rs.getString(6)), saleStatus);
				try {
					client.sendToClient(new Message(TaskType.ReceiveSalesFromServer, saleEntity)); // finally send the entity
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
