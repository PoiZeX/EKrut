package controllerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import common.CommonFunctions;
import mysql.MySqlClass;

public class UsersSimulationDBController {
	/**
	 * Add all tuples to users table
	 * 
	 * @param tuplesToAdd
	 * @return
	 * @throws SQLException
	 */
	public static boolean insertTuples(ArrayList<String[]> tuplesToAdd) throws SQLException {
		/*
		 * each tuple contains: firstname, lastname, user_id, email, phone, (for
		 * workers: region, role_type)
		 */
		for (String[] tuple : tuplesToAdd) {
			if (!insertSingleTuple(tuple))
				return false;
		}
		return true;
	}

	/**
	 * Insert single tuple to users table
	 * 
	 * @param tuple
	 * @return
	 * @throws SQLException
	 */
	private static boolean insertSingleTuple(String[] tuple) throws SQLException {
		try {
			if (MySqlClass.getConnection() == null)
				return false;
			Connection conn = MySqlClass.getConnection();

			// validate data
			String validateResult = isValidTuple(tuple);
			if (!validateResult.equals(""))
				throw new SQLException(validateResult);

			PreparedStatement ps = conn.prepareStatement("insert INTO ekrut.users "
					+ "(`id_number`, `username`, `password`, `first_name`, `last_name`, `email`, `phone_number`, `cc_number`, `region`, `role_type`, `logged_in`, `is_not_approved`) "
					+ "VALUES" + "(?,?,?,?,?,?,?,?,?,?,?,?);");
			// ps.setBoolean(1, Boolean.parseBoolean(tuple[0])); // id AutoInc
			ps.setString(1, tuple[0]); // id_number
			ps.setString(2, ""); // username
			ps.setString(3, ""); // password
			ps.setString(4, tuple[1]); // first name
			ps.setString(5, tuple[2]); // last name
			ps.setString(6, tuple[3]); // email
			ps.setString(7, tuple[4]); // phone number
			ps.setString(8, null); // cc number - can be NULL
			if (CommonFunctions.isNullOrEmpty(tuple[5].trim()) && CommonFunctions.isNullOrEmpty(tuple[6].trim())) {
				// is not a worker
				ps.setString(9, null);
				ps.setString(10, "user"); // auto role type
			} else {
				// is a worker
				ps.setString(9, tuple[5]); // region (for workers) - can be NULL
				ps.setString(10, tuple[6]); // role type
			}
			ps.setBoolean(11, false); // is logged in (Flag)
			ps.setBoolean(12, true); // is NOT approved (Flag)
			ps.executeUpdate();

		} catch (SQLException e) {
			throw e; // re-throw to remove trace
		}

		return true;
	}

	/**
	 * Checks validation of params before inserting to table
	 * 
	 * @param tuple
	 * @return
	 */
	private static String isValidTuple(String[] tuple) {
		StringBuilder res = new StringBuilder("");

		// validate ID number
		if(!isValidID(tuple[0])) 
			res.append("* ID number {"+tuple[0]+"} is not valid in Israel\n");
		
		// validate name
		if(!Pattern.matches("^[a-zA-Z]{2,}[a-zA-Z ]{2,}$", tuple[1]) || !Pattern.matches("^[a-zA-Z]{2,}[a-zA-Z ]{2,}$", tuple[2]))
				res.append("* first name {"+tuple[1]+"} / last name {"+tuple[2]+"} can contain letters and spaces only\n");

		// validate email
		if(!Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]{2,}$", tuple[3]))
				res.append("* email {"+tuple[3]+"} is not in the right format\n");

		// validate phone number
		if(!Pattern.matches("^05[0-9]{8}$", tuple[4]))
			res.append("* phone number {"+tuple[4]+"} is not in the right format\n");

		return res.toString();
	}

	/**
	 * Algorithm to validate the ID is legal in Israel
	 * 
	 * @param ID
	 * @return
	 */
	private static boolean isValidID(String ID) {
		int id = Integer.parseInt(ID);
		String.format("%09d", id); // some IDs can be lower than 9 digits, so lets normalize that with zeros
		int[] factors = new int[] { 1, 2, 1, 2, 1, 2, 1, 2, 1 };

		for (int i = 0; i < 9; i++) {
			factors[i] *= id % 10; id /= 10;
			if (factors[i] > 9) {
				factors[i] = factors[i] / 10 + factors[i] % 10;
			}
		}
		if (Arrays.stream(factors).sum() % 10 == 0)
			return true;

		return false;
	}
}
