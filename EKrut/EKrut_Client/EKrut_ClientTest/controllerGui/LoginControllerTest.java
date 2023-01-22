package controllerGui;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Store.NavigationStoreController;
import client.ClientController;
import common.Message;
import entity.UserEntity;
import enums.TaskType;

class LoginControllerTest {

	private ClientController chat;
	private LoginController loginController;
	private UserEntity user;
	private String errorCompare;
	private Method validateSyntaxMethod;
	private Field errorMsgField, isEKTpressedField;
	private String[] usernamePassword;
	private boolean invokeResult; // need to be downcasting
	private String result, excepted;

	@BeforeEach
	void setUp() throws Exception {
		// define mock and inject
		chat = mock(ClientController.class);
		loginController = new LoginController(chat);

		// basic objects to work with
		// set the default user entity 
		user = new UserEntity();
		user.setUsername("rmNorth");
		user.setPassword("123456");
		usernamePassword = new String[] { user.getUsername(), user.getPassword() };

		// error message starts with
		errorCompare = "Error in:\n";

		// --- define access to method and field ---
		// syntax validation method
		validateSyntaxMethod = LoginController.class.getDeclaredMethod("validateUsernamePasswordSyntax");
		errorMsgField = LoginController.class.getDeclaredField("errorMsg");
		isEKTpressedField = LoginController.class.getDeclaredField("isEKTpressed");
		validateSyntaxMethod.setAccessible(true);
		errorMsgField.setAccessible(true);
		isEKTpressedField.setAccessible(true);
		
		// clear fields every test
		result = "";
		excepted = "";
		NavigationStoreController.connectedUser = null;  // static so need to be cleared every test (like logout)
		isEKTpressedField.set(loginController, false); // default
		
	}

	/**
	 * method for shortcut the invoke call and get
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean invokeValidateSyntaxMethod() throws Exception {
		return (boolean) validateSyntaxMethod.invoke(loginController);
	}

	/**
	 * method for shortcut the return and casting the error msg
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getErrorMsgField() throws Exception {
		return ((StringBuilder) errorMsgField.get(loginController)).toString();
	}
	
	/**
	 * helper method to assertEquals for a message and for connected user
	 * @param expectedMsg the message that set after login validation end
	 * @param expectedConnectedUser check which user is connected 
	 */
	private void loginEndProcessAssertion(String expectedMsg, UserEntity expectedConnectedUser) {
		// get actual information
		String actucalMsg = LoginController.returnedMsg;
		UserEntity actucalConnectedUser = NavigationStoreController.connectedUser;
		boolean booleanResult = loginController.loginProccess(usernamePassword);
		
		assertEquals(actucalMsg, expectedMsg);
		assertEquals(actucalConnectedUser, expectedConnectedUser);
		if(expectedMsg.contains("Success"))  // actually its equals, but its better to be safe
			assertTrue(booleanResult);
		else
			assertFalse(booleanResult);
	}

	
	// ------------------------------------------
	// --------------NULL--Empty-----------------
	// ------------------------------------------

	/*
     * checking functionality: validation of empty username and good password
     * input data: username "", password '123456'
     * expected result: false with msg "Error in:\n* username cannot be empty\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameEmptyFailed() throws Exception {
		LoginController.setUser(new String[] { "", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of null username and good password
     * input data: username null, password '123456'
     * expected result: false with msg "Error in:\n* username cannot be empty\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameNullFailed() throws Exception {
		LoginController.setUser(new String[] { null, "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of good username and empty password
     * input data: username 'abcd', password ''
     * expected result: false with msg "Error in:\n* password cannot be empty\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestPasswordEmptyFailed() throws Exception {
		LoginController.setUser(new String[] { "abcd", "" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of good username and null password
     * input data: username 'abcd', password null
     * expected result: false with msg "Error in:\n* password cannot be empty\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestPasswordNullFailed() throws Exception {
		LoginController.setUser(new String[] { "abcd", null });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of empty username and empty password
     * input data: username '', password ''
     * expected result: false with msg "Error in:\n* password cannot be empty\n* password cannot be empty\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameAndPasswordEmptyFailed() throws Exception {
		LoginController.setUser(new String[] { "", "" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n" + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of null username and null password
     * input data: username null, password null
     * expected result: false with msg "Error in:\n* password cannot be empty\n* password cannot be empty\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestPsernameAndPasswordNullFailed() throws Exception {
		LoginController.setUser(new String[] { null, null });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n" + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	// ------------------------------------------
	// ------------Syntax--Length----------------
	// ------------------------------------------

	/*
     * checking functionality: validation of good syntax of username & password (length & chars)
     * input data: username 'abcd', password '1234'
     * expected result: true with msg "Success"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameAndPasswordLengthSuccess() throws Exception {
		LoginController.setUser(new String[] { "abcd", "1234" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare;

		assertTrue(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of under minimum chars for username & good password length
     * input data: username 'u', password '123456'
     * expected result: false with msg "Error in:\n* username length must be between 4-16\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "u", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	/*
     * checking functionality: validation of over the maximum chars for username & good password length
     * input data: username 'u', password '123456'
     * expected result: false with msg "Error in:\n* username length must be between 4-16\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdefghijklmnopqrstuvwxyz", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of good username & under over the maximum chars for password length
     * input data: username 'abcdef', password '1'
     * expected result: false with msg "Error in:\n* password length must be between 4-16\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestPasswordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdef", "1" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of good username & over the maximum chars for password length
     * input data: username 'abcdef', password 'abcdefghijklmnopqrstuvwxyz'
     * expected result: false with msg "Error in:\n* password length must be between 4-16\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestPasswordMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdef", "abcdefghijklmnopqrstuvwxyz" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of under min char of username & under min chars of password length
     * input data: username 'ab', password 'ab'
     * expected result: false with msg "Error in:\n* username length must be between 4-16\n* password length must be between 4-16\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameAndPasswordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "ab", "ab" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n"
				+ "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of over the max char of username & over the max chars of password length
     * input data: username 'abcdefghijklmnopqrstuvwxyz', password 'abcdefghijklmnopqrstuvwxyz'
     * expected result: false with msg "Error in:\n* username length must be between 4-16\n* password length must be between 4-16\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameAndPasswordMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n"
				+ "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	// ------------------------------------------
	// ---------Syntax--Illegal--Chars-----------
	// ------------------------------------------
	
	/*
     * checking functionality: validation of legal chars pattern for username & password
     * input data: username 'ab_cd', password '123456'
     * expected result: false with msg "Success"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameIllegalCharsStartWithDigitSuccess() throws Exception {
		LoginController.setUser(new String[] { "ab_cd", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare;

		assertTrue(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of illegal pattern: username starts with a digit
     * input data: username '1abc', password '123456'
     * expected result: false with msg "Error in:\n* username can contain just alphabet, digits and underscore\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameIllegalCharsStartWithDigitFailed() throws Exception {
		LoginController.setUser(new String[] { "1abc", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of illegal pattern: username contains space 
     * input data: username 'abc c', password '123456'
     * expected result: false with msg "Error in:\n* username can contain just alphabet, digits and underscore\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameIllegalCharsContatinsSpaceFailed() throws Exception {
		LoginController.setUser(new String[] { "abc c", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	/*
     * checking functionality: validation of illegal pattern: illegal chars at anyplace for username
     * input data: username 'ab@@!', password '123456'
     * expected result: false with msg "Error in:\n* username can contain just alphabet, digits and underscore\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestUsernameIllegalCharsContainsOthersFailed() throws Exception {
		LoginController.setUser(new String[] { "ab@@!", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	/*
     * checking functionality: validation of illegal pattern: illegal chars for username & password and both min length
     * input data: username '@@', password '!!@'
     * expected result: false with msg "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n* password length must be between 4-16\n"
	*/
	@Test
	void validateUsernamePasswordSyntaxTestFiledsIllegalCharsAndMinLengthUsernamePasswordFailed() throws Exception {
		LoginController.setUser(new String[] { "@@", "!!@" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of illegal pattern: illegal chars for username & password and just password min length
     * input data: username '@@', password '!!@'
     * expected result: false with msg "* username can contain just alphabet, digits and underscore\n* password length must be between 4-16\n"
	*/
	@Test
	void validateUsernamePasswordSyntaxTestFiledsIllegalCharsAndMinLengthPasswordFailed() throws Exception {
		LoginController.setUser(new String[] { "@@@@", "!!@" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of illegal pattern: illegal chars for username and just username min length
     * input data: username '@@', password '!!@!'
     * expected result: false with msg "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n"
	*/
	@Test
	void validateUsernamePasswordSyntaxTestFiledsIllegalCharsAndMinLengthUsernameFailed() throws Exception {
		LoginController.setUser(new String[] { "@@", "!!@!" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of illegal pattern: illegal chars for username and just username max length
     * input data: username '!!!!@!!!!@!!!!@!!!!', password '!!'
     * expected result: false with msg "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n"
	*/
	@Test
	void validateUsernamePasswordSyntaxTestFiledsIllegalCharsAndMaxLengthUsernameFailed() throws Exception {
		LoginController.setUser(new String[] { "!!!!@!!!!@!!!!@!!!!", "!!" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	
	/*
     * checking functionality: validation of illegal pattern: illegal chars for username and just password max length
     * input data: username '!!', password '!!!!@!!!!@!!!!@!!!!'
     * expected result: false with msg "* password length must be between 4-16\n* password can contain just alphabet, digits and underscore\n"
	*/
	@Test
	void validateUsernamePasswordSyntaxTestFiledsIllegalCharsAndMaxLengthPasswordFailed() throws Exception {
		LoginController.setUser(new String[] { "!!", "!!!!@!!!!@!!!!@!!!!" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n* username can contain just alphabet, digits and underscore\n* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}
	
	/*
     * checking functionality: validation of illegal pattern: password contains space
     * input data: username 'abcd', password '123 456'
     * expected result: false with msg "Error in:\n* username can contain just alphabet, digits and underscore\n"
     */
	@Test
	void validateUsernamePasswordSyntaxTestPasswordIllegalCharsContainsSpaceFailed() throws Exception {
		LoginController.setUser(new String[] { "abcd", "123 456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password cannot contain any spaces\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	// ------------------------------------------
	// ---------User--Entity--Validation---------
	// ------------------------------------------

	/*
     * checking functionality: validation of all login process
     * input data: UserEntity with username: 'rmNorth', password: '123456', not logged_in, not not_approved
     * expected result: loginProcess - true , connectedUser - the given user, message: "Success";
     */
	@Test
	void loginProccessTestSuccess() {
		//
		user.setLogged_in(false);
		user.setNotApproved(false);
		
		//
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(false);
		LoginController.validUserFromServer(user);
		
		//

		loginEndProcessAssertion("Success", user);
	}

	/*
     * checking functionality: validation of not existing user 
     * input data: username - 'imnotexisting', password - '123456'
     * expected result: loginProcess - false , connectedUser - null, message: "Username or Password are incorrect";
     */
	@Test
	void loginProccessTestUserNotExistInFailed() {
		LoginController.setUser(new String[] { "imnotexisting", "123456" });  // the user 'input'
		user.setUsername("");  // empty means user were not found
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		//
		loginEndProcessAssertion("Username or Password are incorrect", null);

	}
	
	/*
     * checking functionality: validation of not valid password
     * input data: username - 'imnotexisting', password - 'not123456'
     * expected result: loginProcess - false , connectedUser - null, message: "Username or Password are incorrect";
     */
	@Test
	void loginProccessTestPasswordIncorrectInFailed() {
		LoginController.setUser(new String[] { "rmNorth", "not123456" });  // the user 'input'
		user.setUsername("rmNorth");  // empty means user were not found
		user.setPassword("123456");  // empty means user were not found
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		// act
		loginEndProcessAssertion("Username or Password are incorrect", null);

	}
	
	/*
     * checking functionality: validation of valid user but already logged in 
     * input data: username - 'rmNorth', password - '123456' (by server: logged_in - true)
     * expected result: loginProcess - false , connectedUser - null, message: "User is already logged in";
     */
	@Test
	void loginProccessTestUserLoggedInFailed() {
		user.setLogged_in(true);
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("User is already logged in", null);
	}
	
	/*
     * checking functionality: validation of valid user but is not approved
     * input data: username - 'rmNorth', password - '123456' (by server: notApproved - true)
     * expected result: loginProcess - false , connectedUser - null, message: "User is not approved yet";
     */
	@Test
	void loginProccessTestUserNotApprovedFailed() {
		user.setNotApproved(true);
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("User is not approved yet", null);
	}
	
	// ------------------------------------------
	// ----------Connect--With--EKT--------------
	// ------------------------------------------
	/*
     * checking functionality: validation of login with EKT success
     * input data: username - 'rmNorth', password - '123456' (by server: roleType = member, with valid cc_number)
     * expected result: loginProcess - true , connectedUser - the given user, message: "Success";
     */
	@Test
	void loginProccessTestUserAuthorizedSuccess() throws Exception {
		isEKTpressedField.set(loginController, true);
		user.setRole_type("member");
		user.setCc_num("1234123412341234");
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("Success", user);
	}
	
	/*
     * checking functionality: validation of login with EKT failed because the user  is not authorized
     * input data: username - 'rmNorth', password - '123456' (by server: roleType - user)
     * expected result: loginProcess - false , connectedUser - null, message: "You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)";
     */
	@Test
	void loginProccessTestUserNotAuthorizedUserFailed() throws Exception {
		isEKTpressedField.set(loginController, true);
		user.setRole_type("user");
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)", null);
	}
	
	/*
     * checking functionality: validation of login with EKT failed because the registered is not authorized
     * input data: username - 'rmNorth', password - '123456' (by server: roleType - registered)
     * expected result: loginProcess - false , connectedUser - null, message: "You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)";
     */
	@Test
	void loginProccessTestUserNotAuthorizedRegisterFailed() throws Exception {
		isEKTpressedField.set(loginController, true);
		user.setRole_type("registered");
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)", null);
	}
	
	/*
     * checking functionality: validation of login with EKT failed because the general user is not a member (worker or not - there is not cc_number)
     * input data: username - 'rmNorth', password - '123456' (by server: roleType - CEO)
     * expected result: loginProcess - false , connectedUser - null, message: "You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)";
     */
	@Test
	void loginProccessTestUserNotAuthorizedWorkerFailed() throws Exception {
		isEKTpressedField.set(loginController, true);
		user.setRole_type("CEO"); 	// cc_num is not set - so not a member

		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)", null);
	}
	
	
}
