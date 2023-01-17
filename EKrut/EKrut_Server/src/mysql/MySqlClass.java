package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Handles the MySQL Connection
 *
 */
public class MySqlClass {
	public static Boolean isConnectionSuccess = null;
	private static Connection connection;  
	
	
	/**
	 * @param DBAddress database address 
	 * @param username database login username 
	 * @param password database login password
	 */
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
			isConnectionSuccess = true;
			return;
		} catch (SQLException ex) {
			System.out.println("Exception: " + ex.getMessage());
			System.out.println("State: " + ex.getSQLState());
			System.out.println("Error: " + ex.getErrorCode());
			System.out.println("\n");
		}
		isConnectionSuccess = false;
	}
	
	/**
	 * @return current mysql connection
	 */
	public static Connection getConnection() {
		return connection;
	}
}
