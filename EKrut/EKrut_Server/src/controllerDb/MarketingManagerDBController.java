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
	/**
	 * get sales by region
	 * @param region
	 * @param client
	 */
	public static void getSales(String region ,ConnectionToClient client) {
		ArrayList<SaleEntity> sales=new ArrayList<SaleEntity>();
		SaleEntity saleEntity;
		SaleStatus saleStatus;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("SELECT * FROM ekrut.sales WHERE region=?;");
			ps.setString(1,region);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				saleStatus=SaleStatus.valueOf(rs.getString(7));
				saleEntity = new SaleEntity(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)
						 , LocalTime.parse( rs.getString(5)), LocalTime.parse(rs.getString(6)), saleStatus);
				sales.add(saleEntity);
			} 
			client.sendToClient(new Message(TaskType.ReceiveSalesFromServer, sales));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getActiveSalesByRegion(String region ,ConnectionToClient client) {
		ArrayList<SaleEntity> sales=new ArrayList<SaleEntity>();
		SaleEntity saleEntity;
		SaleStatus saleStatus;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement ps=con.prepareStatement("SELECT * FROM ekrut.sales WHERE region=? AND sale_status='Active';");
			ps.setString(1,region);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				saleStatus=SaleStatus.valueOf(rs.getString(7));
				saleEntity = new SaleEntity(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)
						 , LocalTime.parse( rs.getString(5)), LocalTime.parse(rs.getString(6)), saleStatus);
				sales.add(saleEntity);
			} 
			client.sendToClient(new Message(TaskType.ReceiveActiveSales, sales));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
