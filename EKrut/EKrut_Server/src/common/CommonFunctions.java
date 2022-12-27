package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import mysql.MySqlClass;

public class CommonFunctions {

	private static Timer timerTimeLimit;

	/**
	 * checks whether the string is null or empty
	 * @param txt
	 * @return
	 */
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.isEmpty());
	}

	public static String getNumericMonth(String month) {
		int index, numOfMonths = 12;
		String[] months = { "january", "february", "march", "april", "may", "june", "july", "august", "september",
				"october", "november", "december" };
		for (index = 1; index <= numOfMonths; index++) {
			if (month.toLowerCase().equals(months[index - 1]))
				if (index < 10)
					return ("0" + index);
				else if (index >= 10)
					return ("" + index);
		}
		return "Invalid month";
	}

	/**
	 * General function to invoke in <num> seconds without sleeping
	 * @param num
	 * @param callback
	 */
	public static void SleepFor(long num, Runnable callback) {
		timerTimeLimit = new Timer();
		timerTimeLimit.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					try {
						callback.run();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

		}, num);
	}

	
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
		String query = "";
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement psGet = conn.prepareStatement(query);
			psGet.setString(1, String.format("%s-%s-01 00:00:00", year, month));
			psGet.setString(2, String.format("%s-%s-31 23:59:59", year, month));
			ResultSet res = psGet.executeQuery();
			while (res.next()) 
			{
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
