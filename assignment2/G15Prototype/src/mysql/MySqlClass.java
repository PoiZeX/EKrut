package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlClass {
	static Connection connection;
	public static void connectToDb(String DBAddress, String username, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver success");
		} catch (Exception ex) {
			System.out.println("Driver failure");
		}
		try {
			connection = DriverManager.getConnection(DBAddress, username, password);
			System.out.println("Connected To SQL");
		} catch (SQLException ex) {
			System.out.println("Expection: " + ex.getMessage());
			System.out.println("State: " + ex.getSQLState());
			System.out.println("Error: " + ex.getErrorCode());
			System.out.println("\n");
		}
	}
}
