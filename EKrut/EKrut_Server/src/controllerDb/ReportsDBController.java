package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.ClientsReportEntity;
import entity.OrderReportEntity;
import entity.SupplyReportEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class ReportsDBController {
	private static String reportType, month, year, region;
	private static int machineID;

	/**
	 * Parse the string array into reportType region month and year
	 * 
	 * @param array with details
	 * @return
	 */
	public static boolean setReport(String[] details) {
		switch (details.length) {
		case 4:
			reportType = details[0];
			region = details[1];
			month = details[2];
			year = details[3];
			return true;
		case 5:
			reportType = details[0];
			region = details[1];
			month = details[2];
			year = details[3];
			machineID = Integer.parseInt(details[4]);
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
	public static void getReportEntity(String[] details, ConnectionToClient client) {
		Object res;
		try {
			if (setReport(details)) {
				// SQL query //
				switch (reportType) {
				case "orders":
					res = (OrderReportEntity) getOrderReportFromDB();
					client.sendToClient(new Message(TaskType.ReceiveOrderReport, res));
					break;
				case "clients":
					res = (ClientsReportEntity) getClientsReportFromDB();
					client.sendToClient(new Message(TaskType.ReceiveClientsReport, res));
					break;
				case "supply":
					res = (SupplyReportEntity) getSupplyReportFromDB();
					client.sendToClient(new Message(TaskType.ReceiveSupplyReport, res));
					break;
				default:
					res = null;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM ekrut.orders_report WHERE month=? AND year=? AND region=?;");
			ps.setString(1, CommonFunctions.getNumericMonth(month));
			ps.setString(2, year);
			ps.setString(3, region);
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

	protected static ClientsReportEntity getClientsReportFromDB() {
		ClientsReportEntity report = new ClientsReportEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return report;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM ekrut.clients_report WHERE month=? AND year=? AND region=?;");
			ps.setString(1, CommonFunctions.getNumericMonth(month));
			ps.setString(2, year);
			ps.setString(3, region);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				report = new ClientsReportEntity(res.getInt(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return report;

	}

	/**
	 * Handles the query of getting the report from DB
	 * 
	 * @return
	 */
	protected static SupplyReportEntity getSupplyReportFromDB() {
		SupplyReportEntity report = new SupplyReportEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return report;
			Connection conn = MySqlClass.getConnection();
			String query = "SELECT * FROM ekrut.supply_report "
					+ "JOIN machines ON supply_report.machine_id = machines.machine_id "
					+ "WHERE year=? AND month=? AND region=? AND machines.machine_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, year);
			ps.setString(2, CommonFunctions.getNumericMonth(month));
			ps.setString(3, region);
			ps.setInt(4, machineID);

			ResultSet res = ps.executeQuery();

			if (res.next()) {
				report = new SupplyReportEntity(res.getInt(1), res.getInt(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9),
						res.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return report;

	}
}
