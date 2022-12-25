package controllerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

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
			if (CommonFunctions.isNullOrEmpty(tuple[5].strip()) && CommonFunctions.isNullOrEmpty(tuple[6].strip())) {
				// is not a worker
				ps.setString(9, null);
				ps.setString(10, "guest"); // auto role type
			} else {
				// is a worker
				ps.setString(9, tuple[5]); // region (for workers) - can be NULL
				ps.setString(10, tuple[6]); // role type
			}
			ps.setBoolean(11, false); // is logged in (Flag)
			ps.setBoolean(12, true); // is NOT approved (Flag)
			ps.executeUpdate();

		} catch (SQLException e) {
			throw e;
		}

		return true;
	}
}
