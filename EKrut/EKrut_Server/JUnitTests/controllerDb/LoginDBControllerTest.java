package controllerDb;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.CommonFunctions;
import entity.UserEntity;
import mysql.MySqlClass;

class LoginDBControllerTest {

	private static boolean isConnected = false;
	private static Connection con;
	
	@BeforeEach
	void setUp() throws Exception {
		if(isConnected) return;
		// setup connection to mysql
		MySqlClass.connectToDb("jdbc:mysql://localhost/ekrut?serverTimezone=IST", "root", "root");
		con = MySqlClass.getConnection();
		isConnected = true;
	}

	// ------------------------------------------
	// ---------------Get--User------------------
	// ------------------------------------------

	/**
	 * compare 2 users fields (without ID)
	 * 
	 * @param expected
	 * @param actual
	 */
	private void isUserInformationValid(UserEntity expected, UserEntity actual) {
		assertEquals(expected.getId_num(), actual.getId_num());
		assertEquals(expected.getFirst_name(), actual.getFirst_name());
		assertEquals(expected.getLast_name(), actual.getLast_name());
		assertEquals(expected.getUsername(), actual.getUsername());
		assertEquals(expected.getPassword(), actual.getPassword());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getPhone_number(), actual.getPhone_number());
		assertEquals(expected.getCc_num(), actual.getCc_num());
		if (!CommonFunctions.isNullOrEmpty(actual.getUsername()))
			assertEquals(expected.getRole_type(), actual.getRole_type());
		assertEquals(expected.getRegion(), actual.getRegion());
		assertEquals(expected.isLogged_in(), actual.isLogged_in());
		assertEquals(expected.isNotApproved(), actual.isNotApproved());
	}

	/*
     * checking functionality: retrieve user from the database
     * input data: "rgsN" and "123456" as username and password
     * expected result: a user object with the correct fields and values
     */
	@Test
	void getUserRegisteredSuccess() {
		// Arrange
		LoginDBController.setUser(new String[] { "rgsN", "123456" });
		UserEntity expected = new UserEntity("403818859", "rgsN", "123456", "Ravid", "Plotnik", "nechi@gmail.com",
				"0538503033", "1234123412341234", "North", "registered", false, false);
		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}

	/*
     * checking functionality: retrieve user from the database
     * input data: "usrN" and "123456" as username and password
     * expected result: a user object with the correct fields and values and no credit card number
     */
	@Test
	void getUserNotRegisteredSuccess() {
		// Arrange
		LoginDBController.setUser(new String[] { "usrN", "123456" });
		UserEntity expected = new UserEntity("007818859", "usrN", "123456", "Anna", "Zak", "anna@gmail.com",
				"0538503033", null, "North", "user", false, true); // cc_number null

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}

	/*
     * checking functionality: retrieve user from the database
     * input data: "usrN" and "123456" as username and password
     * expected result: a user object with the correct fields and values and no credit card number
     */
	@Test
	void getUserIncorrectPasswordSuccess() {
		// Arrange
		LoginDBController.setUser(new String[] { "usrN", "badpassword" });
		UserEntity expected = new UserEntity("007818859", "usrN", "123456", "Anna", "Zak", "anna@gmail.com",
				"0538503033", null, "North", "user", false, true); // cc_number null

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}
	
	 /*
     * checking functionality: retrieve user from the database who is not approved and logged in
     * input data: "usrS" and "123456" as username and password
     * expected result: a user object with the correct fields and values and no credit card number
     */
	@Test
	void getUserIsNotApprovedAndLoggedInSuccess() {
		// Arrange
		LoginDBController.setUser(new String[] { "usrS", "123456" });
		UserEntity expected = new UserEntity("205818859", "usrS", "123456", "Omer", "Adam", "omer@gmail.com",
				"0538503033", null, "South", "user", true, true); // cc_number null

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}

	/**
	* checking functionality: test the ability to retrieve user from DB with null region field
	* input data: UserEntity with username "spw4" and password "123456"
	* expected result: UserEntity with all fields match the expected values, including a null value for region field
	*/
	@Test
	void getUserNullRegionSuccess() {
		// Arrange
		LoginDBController.setUser(new String[] { "spw4", "123456" });

		UserEntity expected = new UserEntity("106878259", "spw4", "123456", "Rivka", "Asulin", "rivka@EKrut.com",
				"0538503033", null, null, "supplyWorker", true, false); // cc_number null

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}
	// ------------------------------------------------------------
	/**
	* checking functionality: if the getUserFromDB() method returns the expected UserEntity object when searching for 
	* a user that does not exist in the database.
	* input data: username "imnotexist", password "123456"
	* expected result: an empty UserEntity object
	*/
	
	@Test
	void getUserNotExistFailed() {
		// Arrange
		LoginDBController.setUser(new String[] { "imnotexist", "123456" });
		UserEntity expected = new UserEntity();

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}
	/**

	* checking functionality: if the getUserFromDB() method returns the expected UserEntity object when searching for 
	* a user with an empty username.
	* input data: username "", password "123456"
	* expected result: an empty UserEntity object
	*/
	@Test
	void getUserEmptyUsernameFailed() {
		// Arrange
		LoginDBController.setUser(new String[] { "", "123456" }); // search the following user
		UserEntity expected = new UserEntity();

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}

	/**

	* checking functionality: if the getUserFromDB() method returns the expected UserEntity object when searching for 
	* a user with a null username.
	* input data: username null, password "123456"
	* expected result: an empty UserEntity object
	*/
	@Test
	void getUserNullUsernameFailed() {
		// Arrange
		LoginDBController.setUser(new String[] { null, "123456" }); // search the following user
		UserEntity expected = new UserEntity();

		// Act
		UserEntity actual = LoginDBController.getUserFromDB();

		// Assert
		isUserInformationValid(expected, actual);
	}

	// ------------------------------------------
	// ---------Set--User--Logged--In------------
	// ------------------------------------------

	/**
	 * Set the logged in flag to false again after each test (if needed)
	 * 
	 * @param user
	 */
	private void returnToOriginalState(UserEntity user) {
		user.setLogged_in(false);
		LoginDBController.setUserLoggedIn(user);
	}
	
	/*
	checking functionality: setting the user logged in status
	input data: rgsN , 123456
	expected result: UserEntity with the logged_in flag set to true in the database
	*/

	@Test
	void setUserLoggedInSuccess() {
		// Arrange
		String[] usernamePasswordToCheck = new String[] { "rgsN", "123456" };
		LoginDBController.setUser(usernamePasswordToCheck);

		// Arrange - get the user entity and change it state locally
		UserEntity expected = LoginDBController.getUserFromDB(); // get the current tuple
		assertFalse(expected.isLogged_in()); // validate it was false
		expected.setLogged_in(true);

		// Act
		LoginDBController.setUserLoggedIn(expected);
		UserEntity actual = LoginDBController.getUserFromDB(); // get the current tuple

		// Assert
		isUserInformationValid(expected, actual);
		returnToOriginalState(actual);
	}

	/*
	Set the user logged in even if it was already logged in
	Input data: "rgsN", "123456"
	Expected Result: Check that user is logged in and it's fields are the same before and after logging in again
	*/
	@Test
	void setUserAlreadyLoggedInLoggedInSuccess() {
		// Arrange
		String[] usernamePasswordToCheck = new String[] { "rgsN", "123456" };
		LoginDBController.setUser(usernamePasswordToCheck);

		// -- first time
		
		// Arrange - get the user entity and change it state locally
		UserEntity expected = LoginDBController.getUserFromDB(); // get the current tuple
		assertFalse(expected.isLogged_in()); // validate it was false
		expected.setLogged_in(true);

		// Act
		LoginDBController.setUserLoggedIn(expected);
		UserEntity actual = LoginDBController.getUserFromDB(); // get the current tuple

		// Assert
		isUserInformationValid(expected, actual);
		
		// -- second time
		// Act
		actual.setLogged_in(true);
		LoginDBController.setUserLoggedIn(actual);
		actual = LoginDBController.getUserFromDB(); // get the current tuple
		
		// Assert
		isUserInformationValid(expected, actual);

		returnToOriginalState(actual);
	}
	
	// ------------------------------------------------------------

	/**
	* checking functionality: set the logged in flag to true for a user that not exist, will not affect on DB
	* input data: not existing username and password 123456
	* expected result: user is not logged in
	*/
	@Test
	void setUserEmptyLoggedInFailed() {
		// Arrange
		String[] usernamePasswordToCheck = new String[] { "", "123456" }; // not valid user
		LoginDBController.setUser(usernamePasswordToCheck);
	
		// Arrange - get the user entity and change it state locally
		UserEntity user = LoginDBController.getUserFromDB(); // get the current tuple
		assertFalse(user.isLogged_in()); // validate it was false
		user.setLogged_in(true);

		// Act
		LoginDBController.setUserLoggedIn(user);
		user = LoginDBController.getUserFromDB(); // get the current tuple

		// Assert
		assertFalse(user.isLogged_in());
		returnToOriginalState(user);

	}

	/**
	* checking functionality: test the correctness of logged in method with null username
	* input data: UserEntity with null username and password "123456"
	* expected result: user is not logged in
	*/
	@Test
	void setUsernullLoggedInFailed() {
		// Arrange
		String[] usernamePasswordToCheck = new String[] { null, "123456" }; // not valid user
		LoginDBController.setUser(usernamePasswordToCheck);

		// Arrange - get the user entity and change it state locally
		UserEntity user = LoginDBController.getUserFromDB(); // get the current tuple
		assertFalse(user.isLogged_in()); // validate it was false
		user.setLogged_in(true);

		// Act
		LoginDBController.setUserLoggedIn(user);
		user = LoginDBController.getUserFromDB(); // get the current tuple

		// Assert
		assertFalse(user.isLogged_in());
		returnToOriginalState(user);
	}


}
