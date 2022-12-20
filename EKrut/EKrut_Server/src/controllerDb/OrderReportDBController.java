package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import common.CommonFunctions;
import common.Message;
import common.TaskType;
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
	public static boolean setReport(String[] details) {
		if (details.length == 2) {
			month = details[0];
			year = details[1];
			return true;
		}
		return false;
	}

	/**
	 * Handles getting selected report and sending the entity back to client
	 * 
	 * @param usernamePassword
	 * @param client
	 */
	public static void getOrderReportEntity(String[] details, ConnectionToClient client) {
		if (setReport(details)) {
			// SQL query //
			OrderReportEntity res = getOrderReportFromDB();
			try {
				client.sendToClient(new Message(TaskType.RecieveOrderReport, res));
				System.out.println("Server: success");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Handles the query of getting the report from DB
	 * 
	 * @return
	 */
	protected static OrderReportEntity getOrderReportFromDB() {
		OrderReportEntity report = new OrderReportEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return report;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.orders_report WHERE month=? AND year=?;");
			ps.setString(1, CommonFunctions.getNumericMonth(month));
			ps.setString(2, year);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				report = new OrderReportEntity(res.getInt(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5));
				if (CommonFunctions.isNullOrEmpty(report.getDescription()))
					report.setDescription("nosales");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return report;

	}
}
