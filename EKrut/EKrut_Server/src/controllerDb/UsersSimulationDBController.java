package controllerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import enums.RolesEnum;
import mysql.MySqlClass;

/**
 * The Class UsersSimulationDBController.
 */
public class UsersSimulationDBController {
	private static Connection con = MySqlClass.getConnection();
	
	/**
	 * Insert tuples.
	 *
	 * @param tuplesToAdd the tuples to add
	 * @throws SQLException the SQL exception
	 */
	public static void insertTuples(ArrayList<String[]> tuplesToAdd) throws SQLException {
		/*
		 * each tuple contains: firstname, lastname, user_id, email, phone, (for
		 * workers: region, role_type)
		 */
		ArrayList<String[]> imported = new ArrayList<>();
		try {
			for (String[] tuple : tuplesToAdd) {
				insertSingleTuple(tuple);
				imported.add(tuple);
			}
		} catch (SQLException e) {
			UsersSimulationDBController.rollBackImport(imported);
			throw e;
		}

	}

	/**
	 * Insert single tuple.
	 *
	 * @param tuple the tuple
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	private static boolean insertSingleTuple(String[] tuple) throws SQLException {
		try {
			if (con == null)
				return false;

			boolean isNotWorker = true;
			// validate data
			String validateResult = isValidTuple(tuple);
			if (!validateResult.equals(""))
				throw new SQLException(validateResult);

			if (!tuple[8].toLowerCase().equals("user"))
				isNotWorker = false;
			PreparedStatement ps = con.prepareStatement("insert INTO ekrut.users "
					+ "(`id_number`, `username`, `password`, `first_name`, `last_name`, `email`, `phone_number`, `cc_number`, `region`, `role_type`, `logged_in`, `is_not_approved`) "
					+ "VALUES" + "(?,?,?,?,?,?,?,?,?,?,?,?);");
			// ps.setBoolean(1, Boolean.parseBoolean(tuple[0])); // id AutoInc
			ps.setString(1, tuple[0]); // id_number
			ps.setString(2, tuple[1]); // username
			ps.setString(3, tuple[2]); // password
			ps.setString(4, tuple[3]); // first name
			ps.setString(5, tuple[4]); // last name
			ps.setString(6, tuple[5]); // email
			ps.setString(7, tuple[6]); // phone number
			ps.setString(8, null); // cc number - can be NULL
			ps.setString(9, tuple[7]);
			ps.setString(10, tuple[8]); // auto role type
			ps.setBoolean(11, false); // is logged in (Flag)
			ps.setBoolean(12, isNotWorker); // is NOT approved (Flag)
			ps.executeUpdate();

		} catch (SQLException e) {
			throw e; // re-throw to remove trace
		}

		return true;
	}

	/**
	 * Checks if is valid tuple.
	 *
	 * @param tuple the tuple
	 * @return the string
	 */
	private static String isValidTuple(String[] tuple) {
		StringBuilder res = new StringBuilder("");

		// validate ID number
		if (!isValidID(tuple[0]))
			res.append("* ID number {" + tuple[0] + "} is not valid in Israel\n");

		// validate name
		if (!Pattern.matches("^[a-zA-Z]{2,}[a-zA-Z ]{0,}$", tuple[3])
				|| !Pattern.matches("^[a-zA-Z]{2,}[a-zA-Z ]{0,}$", tuple[4]))
			res.append("* first name {" + tuple[3] + "} / last name {" + tuple[4]
					+ "} can contain letters and spaces only\n");

		// validate email
		if (!Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]{2,}$", tuple[5]))
			res.append("* email {" + tuple[5] + "} is not in the right format\n");

		// validate phone number
		if (!Pattern.matches("^05[0-9]{8}$", tuple[6]))
			res.append("* phone number {" + tuple[6] + "} is not in the right format\n");

		// validate role
		if (!tuple[8].equals(RolesEnum.user.toString()))
			res.append("* role type {" + tuple[8] + "} is not valid, can be user or valid employee role.\n");
		return res.toString();
	}

	/**
	 * Checks if is valid ID.
	 *
	 * @param ID the id
	 * @return true, if is valid ID
	 */
	private static boolean isValidID(String ID) {
		int id = Integer.parseInt(ID);
		String.format("%09d", id); // some IDs can be lower than 9 digits, so lets normalize that with zeros
		int[] factors = new int[] { 1, 2, 1, 2, 1, 2, 1, 2, 1 };

		for (int i = 0; i < 9; i++) {
			factors[i] *= id % 10;
			id /= 10;
			if (factors[i] > 9) {
				factors[i] = factors[i] / 10 + factors[i] % 10;
			}
		}
		if (Arrays.stream(factors).sum() % 10 == 0)
			return true;

		return false;
	}
	
	/**
	 * Roll back import.
	 *
	 * @param res the res
	 */
	public static void rollBackImport(ArrayList<String[]> res) {
		try {
			if (con == null)
				return;
			Connection con = MySqlClass.getConnection();
			for (String[] user : res) {
				PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id_number = ?");
				ps.setString(1, user[0]); // id_number
				ps.execute();
			}
		} catch (Exception e) {}
	}
}
