package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.LongSupplier;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import controllerDb.CommonDataDBController;
import controllerDb.ReportsDBController;
import entity.SupplyReportEntity;
import mysql.MySqlClass;

public class ReportsGenerator {

	public static void generateReportsDB(String reportType, String month, String year) {
		switch (reportType) {
		case "clients":
			generateClientsReport(month, year);
			break;
		case "orders":
			generateOrdersReport(month, year);
			break;
		case "supply":
			break;
		}
	}

	private static void generateSupplyReport(String month, String year) {
		try {
			// iterate on regions
			ArrayList<String> allRegion = CommonDataDBController.getRegionsListFromDB();
			for (String region : allRegion) {
				ArrayList<singleSupplyReportInfo> supplyReportsForOneRegion = new ArrayList<>();
				ArrayList<ArrayList<singleSupplyReportInfo>> reportsForMachineID = new ArrayList<>();

				int machine_id, current_amount, times_under_min, min_amount;
				String name;
				// get all machines in region
				String query = "SELECT i.machine_id, i.item_id, i.current_amount, i.times_under_min, machines.min_amount, items.name "
						+ "FROM machines " + "JOIN item_in_machine as i ON " + "region_name='?' " + "JOIN items ON "
						+ "items.item_id = i.item_id " + "ORDER BY i.machine_id;";

				// the query will return a list of ALL machines in the desired region, and the
				// selected fields
				Connection con = MySqlClass.getConnection();
				if (con == null)
					return;
				PreparedStatement psGet = con.prepareStatement(query);
				psGet.setString(1, region);
				// psGet.setString(2, String.format("%s-%s-31 23:59:59", year, month));
				ResultSet res = psGet.executeQuery();
				int prev_machine_id = -1;
				
				// for each tuple
				while (res.next()) {
					machine_id = res.getInt(1);
					// item_id = res.getInt(2);  // remove later ?
					current_amount = res.getInt(3);
					times_under_min = res.getInt(4);
					min_amount = res.getInt(5);
					name = res.getString(6);

					// if this is not the first time (-1) and other machine detected 
					// -> insert all previous 
					if (prev_machine_id != -1 && prev_machine_id != machine_id) {
						reportsForMachineID.put(region, supplyReportsForOneRegion);
						supplyReportsForOneRegion.clear();
					}
					
					prev_machine_id = machine_id; // set for next time
					singleSupplyReportInfo report = new singleSupplyReportInfo(machine_id, name, current_amount,
							times_under_min, min_amount);

					// add the report details we know
					supplyReportsForOneRegion.add(report);

				}
				
				// got here means we finish to get ALL data we need for single region, now process it
				
				

				// TODO: !!!!! reset times_under_min !!!!!

			}

		} catch (Exception e) {	}

	}

	
	private String processData() {
		
	}
	
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
		// North -> <5,7,100,10
		// 10,3,12,4,15,1,3,5 // got it
		// 10 12 15 3 // separate
		// 3 10 12 15 // sort
		// 3,5 , 10,3 , 12,4 , 15, 1 // sort with values
		// 0-3,5,3-10,3, 12-15,5 // wanted
		// 0-3,5 , 3-10,3 , 10-12,4 , 12-15,1, // Actual
	}

	// inner class represents a pair for orders report
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

}
