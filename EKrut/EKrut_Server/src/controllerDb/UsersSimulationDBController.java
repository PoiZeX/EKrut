package controllerDb;

import java.util.ArrayList;


public class UsersSimulationDBController {
	/**
	 * Add all tuples to users table
	 * @param tuplesToAdd
	 * @return
	 */
	public static boolean insertTuples(ArrayList<String[]> tuplesToAdd) {
		/* each tuple contains:
		 * firstname, lastname, user_id, email, phone, (for workers: region, role_type) 
		 */
		for(String[] tuple : tuplesToAdd)
		{
			if(!insertSingleTuple(tuple))
				return false;
		}
		return true;
	}

	/**
	 * Insert single tuple to users table
	 * @param tuple
	 * @return
	 */
	private static boolean insertSingleTuple(String[] tuple) {
		return true;
	}
}
