package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import controllerDb.ReportsDBController;
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

	private static void generateClientsReport(String month, String year) {
		HashMap<String, HashMap<String, String>> regionDetails = new HashMap<>();
		generateSupplyTotal(regionDetails, month, year);
		generateDescription(regionDetails, month, year);
		generateUserStatus(regionDetails, month, year);

		// iterate insert to db
	}

	private static void generateOrdersReport(String month, String year) {
		HashMap<String, String> regionDescription = new HashMap<>();
		String query = "SELECT machines.machine_id, machines.machine_name, regions.region_name, orders.buytime, SUM(orders.total_sum) as total_sum, COUNT(*) as num_orders "
				+ "FROM orders " + "JOIN machines ON orders.machine_id = machines.machine_id "
				+ "JOIN regions ON machines.region_id = regions.region_id "
				+ "WHERE orders.is_delivery = 0 AND STR_TO_DATE(buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ? "
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

// Helper Functions
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

	private static void generateDescription(HashMap<String, HashMap<String, String>> regionDetails, String month,
			String year) {
		HashMap<String, ArrayList<Integer>> allQueries = new HashMap<>();
		String query = "SELECT o.supply_method, COUNT(*) as num_orders, m.region_name " + "FROM orders o "
				+ "JOIN machines m ON o.machine_id = m.machine_id "
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
				allQueries.get(region).add(res.getInt(1));
				allQueries.get(region).add(res.getInt(2));
			}
			for (String region : allQueries.keySet()) {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
