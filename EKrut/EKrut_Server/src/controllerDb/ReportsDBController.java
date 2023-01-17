package controllerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import common.CommonFunctions;
import common.Message;
import enums.TaskType;
import entity.ClientsReportEntity;
import entity.OrderReportEntity;
import entity.SupplyReportEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class ReportsDBController.
 */
public class ReportsDBController {
	private static String reportType, month, year, region;
	private static int machineID;
	private static Connection con = MySqlClass.getConnection();

	/**
	 * Sets the report.
	 *
	 * @param details the details
	 * @return true, if successful
	 */
	public static boolean setReport(String[] details) {
		switch (details[0]) {
		case "orders":
		case "clients":
			reportType = details[0];
			region = details[1];
			month = details[2];
			year = details[3];
			return true;
		case "supply":
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
	 * Gets the report entity.
	 *
	 * @param details the details
	 * @param client the client
	 * @return the report entity
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
	 * Gets the order report from DB.
	 *
	 * @return the order report from DB
	 */
	public static OrderReportEntity getOrderReportFromDB() {
		OrderReportEntity report = new OrderReportEntity();
		try {
			if (con == null)
				return report;
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM ekrut.orders_report WHERE month=? AND year=? AND region=?;");
			Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
			if (!pattern.matcher(month).matches())
				month = CommonFunctions.getNumericMonth(month);
			ps.setString(1, month);
			ps.setString(2, year);
			ps.setString(3, region);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				report = new OrderReportEntity(res.getInt(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5));
				if (CommonFunctions.isNullOrEmpty(report.getDescription()))
					report.setDescription("nosales");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;

	}
	
	/**
	 * Gets the clients report from DB.
	 *
	 * @return the clients report from DB
	 */
	public static ClientsReportEntity getClientsReportFromDB() {
		ClientsReportEntity report = new ClientsReportEntity();
		try {
			if (con == null)
				return report;
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM ekrut.clients_report WHERE month=? AND year=? AND region=?;");
			Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
			if (!pattern.matcher(month).matches())
				month = CommonFunctions.getNumericMonth(month);
			ps.setString(1, month);
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
	 * Gets the supply report from DB.
	 *
	 * @return the supply report from DB
	 */
	public static SupplyReportEntity getSupplyReportFromDB() {
		SupplyReportEntity report = new SupplyReportEntity();
		try {
			if (con == null)
				return report;
			String query = "SELECT * FROM ekrut.supply_report where month = ? AND year = ? AND machine_id = ?";

			PreparedStatement ps = con.prepareStatement(query);
			Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
			if (!pattern.matcher(month).matches())
				month = CommonFunctions.getNumericMonth(month);
			ps.setString(1, month);
			ps.setString(2, year);
			ps.setInt(3, machineID);

			ResultSet res = ps.executeQuery();
			if (res.next()) {
				report = new SupplyReportEntity(res.getInt(1), res.getInt(2), res.getInt(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return report;

	}

	/**
	 * Checks if is report exist.
	 *
	 * @param reportType the report type
	 * @param month the month
	 * @param year the year
	 * @param region the region
	 * @param machineId the machine id
	 * @return true, if is report exist
	 */
	public static boolean isReportExist(String reportType, String month, String year, String region, int machineId) {
		if (!setReport(new String[] { reportType, region, month, year, String.valueOf(machineId) }))
			return false;
		switch (reportType) {
		case "clients":
			ClientsReportEntity clientsRes = getClientsReportFromDB();
			if (clientsRes.getDescription().equals("noreport")
					|| clientsRes.getTotalSalesArr() == null
					|| clientsRes.getSupplyMethodsArr() == null)
				return false;
			return true;
		case "orders":
			OrderReportEntity ordersRes =  getOrderReportFromDB();
			if (ordersRes.getDescription().equals("noreport")
					|| ordersRes.getReportsList() == null)
				return false;
			return true;
		case "supply":
			SupplyReportEntity supplyRes = getSupplyReportFromDB();
			if (supplyRes.getReportsList() == null) 
				return false;
			return true;
		}
		return true;
	}
}
