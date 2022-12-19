package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import common.Message;
import common.MessageType;
import entity.OrderReportEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class OrderReportDBController {
	private static String month, year;

	/**
	 * Parse the string array into username and password
	 * 
	 * @param usernamePassword
	 * @return
	 */
	public static boolean setUser(String[] details) {
		if (details.length == 2) {
			month = details[0];
			year = details[1];
			return true;
		}
		return false;
	}

	/**
	 * Handles getting selected user and sending the entity back to client
	 * 
	 * @param usernamePassword
	 * @param client
	 */
	public static void getOrderReportEntity(String[] details, ConnectionToClient client) {
		if (setUser(details)) {
			// sql query //
			OrderReportEntity res = getOrderReportFromDB();
			try {
				client.sendToClient(new Message(MessageType.RequestOrderReport, res));
				System.out.println("Server: success");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Handles the query of getting the user from DB
	 * 
	 * @return
	 */
	protected static OrderReportEntity getOrderReportFromDB() {
		OrderReportEntity report = new OrderReportEntity(0, "", "", "", "");
		try {
			if (MySqlClass.getConnection() == null)
				return report;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.orders_report WHERE month=? AND year=?;");
			ps.setString(1, getMonth(month));
			ps.setString(2, year);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				report = new OrderReportEntity(res.getInt(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return report;

	}
	private static String getMonth(String month) {
	    switch (month.toLowerCase()) {
	        case "january":
	            return "01";
	        case "february":
	            return "02";
	        case "march":
	            return "03";
	        case "april":
	            return "04";
	        case "may":
	            return "05";
	        case "june":
	            return "06";
	        case "july":
	            return "07";
	        case "august":
	            return "08";
	        case "september":
	            return "09";
	        case "october":
	            return "10";
	        case "november":
	            return "11";
	        case "december":
	            return "12";
	        default:
	            return "Invalid month";

	    }
	}
}
