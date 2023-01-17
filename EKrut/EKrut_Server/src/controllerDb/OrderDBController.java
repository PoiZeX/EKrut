package controllerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.Message;
import enums.TaskType;
import entity.OrderEntity;
import entity.UserEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;
import utils.ScheduledTasksController;
/**
 * Handles the order DB queries
 *
 */
public class OrderDBController {
	private static Connection con = MySqlClass.getConnection();

	/**
	 * Checks and sends an answer if this is the first purchase of a member
	 * 
	 * @param member
	 * @param client
	 */
	public static void isMemberFirstPurchase(UserEntity member, ConnectionToClient client) {
		try {
			if (con == null)
				return;
			PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE user_id=?;");
			ps.setInt(1, member.getId());
			ResultSet res = ps.executeQuery();

			if (!res.next()) // empty query = no orders for member
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));
			else
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets an order entity and insert it. Return success (or not) message to client
	 * 
	 * @param entity
	 * @param client
	 */
	public static void insertOrderEntity(OrderEntity entity, ConnectionToClient client) {

		try {
			if (con == null)
				return;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date date = new Date();
			PreparedStatement ps = con.prepareStatement("INSERT INTO orders "
					+ "(machine_id, total_sum, user_id, buytime, products_amount, payment_status, supply_method) VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, entity.getMachine_id());
			ps.setDouble(2, entity.getTotal_sum());
			ps.setInt(3, entity.getUser_id());
			ps.setString(4, formatter.format(date));
			ps.setDouble(5, entity.getProductsAmount());
			ps.setString(6, entity.getPaymentStatus());
			ps.setString(7, entity.getSupplyMethod());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			int generatedKey = -1;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, generatedKey)); // send success msg
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, -1)); // send not success msg
				return;
			} catch (Exception ex) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Return all orders of members which not paid yet, in time range
	 * 
	 * @param startMonth
	 * @param StartYear
	 * @param endMonth
	 * @param endYear
	 * @return
	 */
	public static ArrayList<int[]> getNotPaidOrdersInTimeRange(String year, String month) {
		ArrayList<int[]> ordersToReturn = new ArrayList<int[]>();

		try {
			if (con == null)
				return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ordersToReturn;
	}

	/**
	 * Manage the task of taking money from members of specific month and year
	 * 
	 * @param year
	 * @param month
	 */
	public static void takeMonthlyMoneyScheduledManager(String month, String year) {
		try {
			if (ScheduledTasksController.isFirstDayOfMonth()) // validate
				if (takeMoneyOnOrders(month, year))
					updatePaymentStatus(month, year);
		} catch (Exception e) {

		}
	}

	/**
	 * Handle the query of calculating and taking the money for orders in range of
	 * time
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	private static boolean takeMoneyOnOrders( String month, String year) throws SQLException {
		boolean isAtLeastOneFound = false;
		if (con == null)
			return false;
		PreparedStatement ps = con.prepareStatement("SELECT *, SUM(t.total_sum) AS final_sum FROM("
				+ "SELECT orders.* from orders "
				+ "join users ON users.id=orders.user_id where orders.payment_status='later' AND "
				+ "STR_TO_DATE(orders.buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ?) t GROUP BY user_id;");
		
		ps.setString(1, String.format("%s-%s-01 00:00:00", year, month));
		ps.setString(2, String.format("%s-%s-31 23:59:59", year, month));
		
		ResultSet rs = ps.executeQuery();

		// every tuple is a sum for user id at this range of time
		while (rs.next()) {
			isAtLeastOneFound = true;
			externalPaymentService(rs.getInt(4), rs.getDouble(9));
		}

		rs.close();
		return isAtLeastOneFound;

	}

	/**
	 * For simulation we get the user id and sum of money to take, and refer it to
	 * external payment process (simulation)
	 * 
	 * @param userId
	 * @param sum
	 */
	@SuppressWarnings("unused")
	private static void externalPaymentService(int userId, double sum) {
		UserEntity user = UsersManagementDBController.getUserByID(userId);
		if (user == null)
			return;
		else {
			String cc_number = user.getCc_num();
			// External payment should be inserted here With details <cc_number, sum>
		}
	}

	/**
	 * Update payment status to 'later' for orders in time range of whole month
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private static void updatePaymentStatus(String month, String year) throws SQLException {
		if (con == null)
			return;
		// get all orders
		PreparedStatement ps = con.prepareStatement("SELECT * from orders  where orders.payment_status='later' AND "
				+ "STR_TO_DATE(orders.buytime, '%d/%m/%Y %H:%i') BETWEEN ? AND ?;");
		
		
		ps.setString(1, String.format("%s-%s-01 00:00:00", year, month));
		ps.setString(2, String.format("%s-%s-31 23:59:59", year, month));

		ResultSet rs = ps.executeQuery();

		// update each order
		while (rs.next()) {
			ps = con.prepareStatement("UPDATE orders SET payment_status='paid' WHERE id=?");
			ps.setInt(1, rs.getInt(1));
			ps.executeUpdate();

		}

	}

}
