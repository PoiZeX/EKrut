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
			
			PreparedStatement ps=con.prepareStatement("INSERT INTO ekrut.sales (region, sale_type, start_date, end_date, start_time, end_time) "
					+ "VALUES (?, ?, ?, ?, ?, ?);");
			ps.setString(1,saleEntity.getRegion());
			ps.setString(2,saleEntity.getSaleType());
			ps.setString(3, saleEntity.getStartDateToString());
			ps.setString(4, saleEntity.getEndDateToString());
			ps.setString(5, saleEntity.getStartTime().toString());
			ps.setString(6, saleEntity.getEndTime().toString());
			
			ps.executeUpdate();


		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void getSales(ConnectionToClient client) {
		Statement stmt;
		SaleEntity saleEntity;
		Date startD, endD;
		Time startT, endT;
		SaleStatus saleStatus;
		try {
			if (MySqlClass.getConnection() == null) {
				return;
			}
			stmt = (MySqlClass.getConnection()).createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ekrut.sales;");
			
			while (rs.next()) {
				startD= rs.getDate(4);
				endD=rs.getDate(5);
				startT=rs.getTime(6);
				endT=rs.getTime(7);
				saleStatus=SaleStatus.valueOf(rs.getString(8));
				saleEntity = new SaleEntity(rs.getString(2), rs.getString(3),LocalDate.of(startD.getYear(), startD.getMonth(),startD.getDate()) ,
						LocalDate.of(endD.getYear(), endD.getMonth(),endD.getDate()), LocalTime.of(startT.getHours(), startT.getMinutes())
						,LocalTime.of(endT.getHours(), endT.getMinutes()),saleStatus);
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
