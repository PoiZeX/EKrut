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
 * Reports DB controller handles all Reports queries
 * @author Lidor
 *
 */
public class ReportsDBController {
	private static String reportType, month, year, region;
	private static int machineID;
	private static Connection con = MySqlClass.getConnection();

	/**
	 * Parse the string array into reportType region month and year
	 * 
	 * @param array with details
	 * @return true when report is set, else return false
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
	 * Handles getting selected report and sending the entity back to client
	 * 
	 * @param details
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
	 * @return report
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
	This method retrieves a ClientsReportEntity object from the MySQL database.
	@return ClientsReportEntity object containing data from the database
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
	 * Handles the query of getting the report from DB
	 * 
	 * @return SupplyReportEntity object containing data from the database
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
	This method checks if a report of a given type exists in the MySQL database.
	@param reportType a string representing the type of report to check for
	@param month a string representing the month of the report
	@param year a string representing the year of the report
	@param region a string representing the region of the report
	@param machineId an int representing the machine id of the report
	@return boolean true if the report exists, false otherwise
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
