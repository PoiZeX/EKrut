package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import controllerDb.CommonDataDBController;
import controllerDb.PersonalMessagesDBController;
import controllerDb.ReportsDBController;
import controllerDb.UsersManagementDBController;
import entity.PersonalMessageEntity;
import entity.SupplyReportEntity;
import entity.UserEntity;
import mysql.MySqlClass;

public class ReportsGenerator {

	/**
	 * Manager for report generator for given type, month and year
	 * @param reportType clients / orders / supply
	 * @param month string format 'MM'
	 * @param year string format 'yyyy' 
	 */
	public static void generateReportsDB(String reportType, String month, String year) {
		switch (reportType) {
		case "clients":
			generateClientsReport(month, year);
			break;
		case "orders":
			generateOrdersReport(month, year);
			break;
		case "supply":
			generateSupplyReport();
			break;
		}
	}

	/**
	 * Run a query that generate and insert report for each machine
	 * 
	 */
	private static void generateSupplyReport() {
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			String query = "INSERT INTO supply_report (machine_id, machine_min_amount, item_id,  times_under_min, end_stock, year, month, region)  "
					+ "( " + "	SELECT  " + "		machine_id, min_amount,  "
					+ "		GROUP_CONCAT(item_id SEPARATOR ',') as item_id,  "
					+ "     GROUP_CONCAT(times_under_min SEPARATOR ',') as times_under_min,  "
					+ "		GROUP_CONCAT(current_amount SEPARATOR ',') as current_amount,  "
					+ "     LPAD(MONTH(DATE_SUB(CONCAT(YEAR(CURDATE()),'-',LPAD(MONTH(CURDATE()),2,'0'),'-01'), INTERVAL 1 MONTH)),2,'0') as month, "
					+ "		YEAR(DATE_SUB(CONCAT(YEAR(CURDATE()),'-',LPAD(MONTH(CURDATE()),2,'0'),'-01'), INTERVAL 1 MONTH)) as year, "
					+ "     region_id " + "	FROM ( "
					+ "		  SELECT machines.min_amount as min_amount, item_in_machine.current_amount as current_amount, item_in_machine.item_id as item_id, machines.region_id as region_id, item_in_machine.times_under_min as times_under_min, item_in_machine.machine_id as machine_id "
					+ "		  FROM item_in_machine "
					+ "		  JOIN machines ON machines.machine_id = item_in_machine.machine_id "
					+ "	) as supply_report " + "GROUP BY region_id, machine_id  "
					+ "ORDER BY region_id, machine_id, item_id);";

			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();

		} catch (Exception e) {
			// send message to region manager about failure

			// get current year & month
			LocalDateTime now = LocalDateTime.now();
			String month = now.format(DateTimeFormatter.ofPattern("MM"));
			String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
			if (month.equals("01")) {
				month = "12";
				year = String.valueOf(Integer.parseInt(year) - 1);
			} else {
				month = String.valueOf(Integer.parseInt(month) - 1);
			}

			// iterate over region
			ArrayList<String> allRegion = CommonDataDBController.getRegionsListFromDB();
			for (String region : allRegion) {
				UserEntity manager = UsersManagementDBController.getRegionManagerFromDBQuery(region);

				PersonalMessagesDBController.setPersonalMessagesInDB(new PersonalMessageEntity(manager.getId(),
						String.format("01/%s/%s", month, year), "Error creating report!",
						String.format("Supply report for %s/%s failed. Please contact EKrut team to check whats wrong",
								month, year)));
			}
		}
		resetTimesUnderMin();

	}

	/**
	 * Resets the times under min for ALL machines
	 */
	private static void resetTimesUnderMin() {
		try {
			String query = "UPDATE item_in_machine SET times_under_min=0;";
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();

		} catch (SQLException e) {	}

	}

	/**
	 * return the items end amount from previous report
	 * @param current_report
	 * @return
	 */
	private static String getItemsEndAmountPreviousReport(SupplyReportEntity current_report) {
		String month, year, res = "";

		// initialize dates
		month = String.valueOf(Integer.parseInt(current_report.getMonth()) - 1); // last month
		year = current_report.getYear(); // current year
		if (month.equals("01")) // if the current report is first of the month - change the month & year
		{
			year = String.valueOf(Integer.parseInt(year) - 1);
			month = "12";
		}

		// check if last report is exist
		if (!ReportsDBController.isReportExist("supply", month, year, current_report.getRegion()))
			return "0"; // --------- handle this case more "0,0,0,......" ---------

		// get the report and save the entity
		ReportsDBController.setReport(new String[] { "supply", current_report.getRegion(), month, year });
		SupplyReportEntity prev_report = ReportsDBController.getSupplyReportFromDB();

		// iterate over the the current & find the corresponding items
		// insert to 'res'
		for (String[] current_arr : current_report.getReportsList()) {
			String item_to_find = current_arr[0];
			for (String[] prev_arr : prev_report.getReportsList()) {
				if (prev_arr[0].equals(item_to_find)) {
					res += prev_arr[3] + ",";
					break;
				}
			}
		}

		return res.substring(0, res.length() - 1); // return the string without ','
	}



	/**
	 * orders report Manager, generate and insert
	 * 
	 * @param month
	 * @param year
	 */
	private static void generateOrdersReport(String month, String year) {
		HashMap<String, String> regionDescription = new HashMap<>();
		String query = "SELECT machines.machine_id, machines.machine_name, regions.region_name, orders.buytime, SUM(orders.total_sum) as total_sum, COUNT(*) as num_orders "
				+ "FROM orders " + "JOIN machines ON orders.machine_id = machines.machine_id "
				+ "JOIN regions ON machines.region_id = regions.region_id "
				+ "WHERE orders.supply_method != 'Delivery' AND STR_TO_DATE(buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ? "
				+ "GROUP BY machines.machine_name";
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement psGet = conn.prepareStatement(query);
			psGet.setString(1, String.format("%s-%s-01 00:00:00", year, month));
			psGet.setString(2, String.format("%s-%s-31 23:59:59", year, month));
			ResultSet res = psGet.executeQuery();
			while (res.next()) {
				if (regionDescription.get(res.getString(3)) == null) {
					regionDescription.put(res.getString(3), "");
				}
				String description = regionDescription.get(res.getString(3));
				String toAdd = String.format("%s,%s,%s", res.getString(2), res.getString(6), res.getString(5));
				if (regionDescription.get(res.getString(3)).equals("")) {
					description = toAdd;
				} else {
					description = description + "," + toAdd;
				}
				regionDescription.put(res.getString(3), description);
			}
			for (String reportKey : regionDescription.keySet()) {
				PreparedStatement psInsert = conn.prepareStatement(
						"insert into orders_report(description,month,year,region) " + "values(?,?,?,?)");
				psInsert.setString(1, regionDescription.get(reportKey));
				psInsert.setString(2, month);
				psInsert.setString(3, year);
				psInsert.setString(4, reportKey);
				psInsert.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * clients report Manager, generate and insert
	 * 
	 * @param month
	 * @param year
	 */
	private static void generateClientsReport(String month, String year) {

		HashMap<String, HashMap<String, String>> regionDetails = new HashMap<>();
		// Prepare
		generateSupplyTotal(regionDetails, month, year);
		generateDescription(regionDetails, month, year);
		generateUserStatus(regionDetails, month, year);

		// insert to DB
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			for (String region : regionDetails.keySet()) {
				String description = regionDetails.get(region).get("description");
				String supply_methods = regionDetails.get(region).get("supply_methods");
				String total_sales = regionDetails.get(region).get("total_sales");
				String user_status = regionDetails.get(region).get("user_status");
				PreparedStatement psInsert = con.prepareStatement(
						"insert into clients_report(description,supplymethods,totalorders,user_status,year,month,region) "
								+ "values(?,?,?,?,?,?,?)");
				psInsert.setString(1, description);
				psInsert.setString(2, supply_methods);
				psInsert.setString(3, total_sales);
				psInsert.setString(4, user_status);
				psInsert.setString(5, year);
				psInsert.setString(6, month);
				psInsert.setString(7, region);
				psInsert.executeUpdate();
			}
		} catch (Exception e) {

		}

	}

	/**
	 * generate the user status for clients-report
	 * 
	 * @param regionDetails
	 * @param month
	 * @param year
	 */
	private static void generateUserStatus(HashMap<String, HashMap<String, String>> regionDetails, String month,
			String year) {
		HashMap<String, ArrayList<? super Object>> allQueries = new HashMap<>();
		String query = "SELECT m.region_name, u.role_type, COUNT(*) as amount " + "FROM orders o "
				+ "JOIN users u ON o.user_id = u.id " + "JOIN machines m ON m.machine_id = o.machine_id "
				+ "WHERE STR_TO_DATE(buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ? AND (u.role_type = 'registered' OR u.role_type = 'member') "
				+ "GROUP BY u.role_type, m.region_name";
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement psGet = conn.prepareStatement(query);
			psGet.setString(1, String.format("%s-%s-01 00:00:00", year, month));
			psGet.setString(2, String.format("%s-%s-31 23:59:59", year, month));
			ResultSet res = psGet.executeQuery();
			while (res.next()) {
				String region = res.getString(1);
				if (!allQueries.containsKey(region)) {
					allQueries.put(region, new ArrayList<>());
				}
				allQueries.get(region).add(res.getString(2));
				allQueries.get(region).add(res.getInt(3));
			}
			String userStatus = "0,0";
			for (String region : allQueries.keySet()) {
				while (allQueries.get(region).size() < 4)
					allQueries.get(region).add(0);
				switch ((String) allQueries.get(region).get(0)) {
				case "member":
					userStatus = String.format("%d,%d", (int) allQueries.get(region).get(1),
							(int) allQueries.get(region).get(3));
					break;
				case "registered":
					userStatus = String.format("%d,%d", (int) allQueries.get(region).get(3),
							(int) allQueries.get(region).get(1));
					break;
				default:
					break;
				}
				regionDetails.get(region).put("user_status", userStatus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * generate the supply total column for clients-report
	 * 
	 * @param regionDetails
	 * @param month
	 * @param year
	 */
	private static void generateSupplyTotal(HashMap<String, HashMap<String, String>> regionDetails, String month,
			String year) {
		HashMap<String, ArrayList<? super Object>> allQueries = new HashMap<>();
		String query = "SELECT o.supply_method, COUNT(*) as num_orders, m.region_name "
				+ "FROM orders o JOIN machines m ON o.machine_id = m.machine_id "
				+ "WHERE STR_TO_DATE(buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ? "
				+ "GROUP BY o.supply_method, m.region_name";
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement psGet = conn.prepareStatement(query);
			psGet.setString(1, String.format("%s-%s-01 00:00:00", year, month));
			psGet.setString(2, String.format("%s-%s-31 23:59:59", year, month));
			ResultSet res = psGet.executeQuery();
			while (res.next()) {
				String region = res.getString(3);
				if (!allQueries.containsKey(region)) {
					allQueries.put(region, new ArrayList<>());
				}
				allQueries.get(region).add(res.getString(1));
				allQueries.get(region).add(res.getInt(2));
			}
			for (String region : allQueries.keySet()) {
				regionDetails.put(region, new HashMap<>());
				int pickup = 0, onsite = 0, delivery = 0;
				for (int i = 0; i < allQueries.get(region).size(); i += 2) {
					switch ((String) allQueries.get(region).get(i)) {
					case "Pickup":
						pickup = (int) allQueries.get(region).get(i + 1);
						break;
					case "On-site":
						onsite = (int) allQueries.get(region).get(i + 1);
						break;
					case "Delivery":
						delivery = (int) allQueries.get(region).get(i + 1);
						break;
					}
				}
				regionDetails.get(region).put("supply_methods", String.format("%d,%d", pickup, onsite));
				regionDetails.get(region).put("total_sales", String.format("%d", onsite + pickup + delivery));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate the description column for clients-report
	 * 
	 * @param regionDetails
	 * @param month
	 * @param year
	 */
	private static void generateDescription(HashMap<String, HashMap<String, String>> regionDetails, String month,
			String year) {
		HashMap<String, ArrayList<Integer>> allQueries = new HashMap<>();
		String query = "SELECT t.totalUserMachine as orders,  COUNT(*) as user_amount,  m.region_name " + "FROM (  "
				+ " SELECT machine_id, user_id, COUNT(*) AS totalUserMachine " + "FROM orders "
				+ "WHERE STR_TO_DATE(buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ? " + "GROUP BY user_id, machine_id "
				+ ") t" + " JOIN machines m ON t.machine_id = m.machine_id "
				+ "GROUP BY t.totalUserMachine, m.region_name " + "ORDER BY region_name;";
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;
			PreparedStatement psGet = con.prepareStatement(query);
			psGet.setString(1, String.format("%s-%s-01 00:00:00", year, month));
			psGet.setString(2, String.format("%s-%s-31 23:59:59", year, month));
			ResultSet res = psGet.executeQuery();

			// orders, amount of users, region -> for ex.: 1, 1, North (1 user made 1 order
			// during the last month in North region)
			// the columns: orders, user_amount, region
			while (res.next()) {
				String region = res.getString(3);

				// This query is dynamic for future change in regions
				if (!allQueries.containsKey(region)) {
					allQueries.put(region, new ArrayList<>());
				}

				allQueries.get(region).add(res.getInt(1));
				allQueries.get(region).add(res.getInt(2));
			}
			for (String region : allQueries.keySet()) {
				String stringForRegion = buildStringForRegion(allQueries, region);
				regionDetails.get(region).put("description", String.format("%s", stringForRegion));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Build the string array for given region
	 * 
	 * @param region
	 * @return
	 */
	private static String buildStringForRegion(HashMap<String, ArrayList<Integer>> allQueries, String region) {
		ArrayList<Integer> numbersToProcess = allQueries.get(region);
		ArrayList<Pair> listOfPairs = new ArrayList<>();

		for (int i = 0; i < numbersToProcess.size() - 1; i += 2) {
			listOfPairs.add(new Pair(numbersToProcess.get(i), numbersToProcess.get(i + 1)));
			// sb.append(String.format("%s,%s", String.valueOf(numbersToProcess.get(i)),
			// numbersToProcess.get(i+1)));
		}
		// Sort by ordersAmount
		listOfPairs.sort((o1, o2) -> o1.compareTo(o2));

		String finalRes = "";
		String lastOrderLimit = "0";
		for (Pair p : listOfPairs) {
			finalRes += lastOrderLimit + "-" + p.toString() + ",";
			lastOrderLimit = p.ordersAmountStr();
		}

		finalRes = finalRes.substring(0, finalRes.length() - 1); // remove the last ","
		return finalRes;
	}

	
	
	/**
	 * inner class represents a pair for orders report
	 *
	 */
	protected static class Pair implements Comparable<Pair> {
		int ordersAmount, usersAmount;

		public Pair(int ordersAmount, int usersAmount) {
			this.ordersAmount = ordersAmount;
			this.usersAmount = usersAmount;
		}

		@Override
		public String toString() {
			return this.ordersAmount + "," + this.usersAmount;
		}

		@Override
		public int compareTo(Pair o) {
			return ordersAmount > o.ordersAmount ? 1 : ordersAmount == 0 ? 0 : -1;
		}

		public String ordersAmountStr() {
			return String.valueOf(ordersAmount);
		}

	}
	
	
	/**
	 * A private class to keep a single entity of info-report
	 *
	 */
	private static class singleSupplyReportInfo {
		int machine_id, item_id, current_amount, times_under_min, min_amount;
		String name;

		public singleSupplyReportInfo(int machine_id, String name, int current_amount, int times_under_min,
				int min_amount) {
			super();
			this.machine_id = machine_id;
			this.item_id = item_id;
			this.current_amount = current_amount;
			this.times_under_min = times_under_min;
			this.name = name;
			this.min_amount = min_amount;
		}

	}

}
