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
     * checking functionality: not 
     * input data: 
     * expected result: 
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
     * checking functionality: 
     * input data: 
     * expected result: 
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
     * checking functionality: 
     * input data: 
     * expected result: 
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
     * checking functionality: 
     * input data: 
     * expected result: 
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
     * checking functionality: 
     * input data: 
     * expected result: 
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
     * checking functionality: 
     * input data: 
     * expected result: 
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

	@Test
	void validateUsernamePasswordSyntaxTestUsernameAndPasswordLengthSuccess() throws Exception {
		LoginController.setUser(new String[] { "abcd", "1234" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare;

		assertTrue(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestUsernameMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "u", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestUsernameMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdefghijklmnopqrstuvwxyz", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestPasswordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdef", "1" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestPasswordMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdef", "abcdefghijklmnopqrstuvwxyz" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

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
	@Test
	void validateUsernamePasswordSyntaxTestUsernameillegalCharsStartWithDigitSuccess() throws Exception {
		LoginController.setUser(new String[] { "ab_cd", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare;

		assertTrue(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestUsernameillegalCharsStartWithDigitFailed() throws Exception {
		LoginController.setUser(new String[] { "1abc", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestUsernameillegalCharsContatinsSpaceFailed() throws Exception {
		LoginController.setUser(new String[] { "abc c", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestUsernameillegalCharsContainsOthersFailed() throws Exception {
		LoginController.setUser(new String[] { "ab@@!", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void validateUsernamePasswordSyntaxTestPasswordillegalCharsContainsSpaceFailed() throws Exception {
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
	
	@Test
	void loginProccessTestPasswordIncorrectInFailed() {
		LoginController.setUser(new String[] { "rmNorth", "not123456" });  // the user 'input'
		user.setUsername("rmNorth");  // empty means user were not found
		user.setPassword("123456");  // empty means user were not found
		
		// prepare behavior
//		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
//		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		// act
		loginEndProcessAssertion("Username or Password are incorrect", null);

	}
	
	
	
	@Test
	void loginProccessTestUserLoggedInFailed() {
		user.setLogged_in(true);
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		loginEndProcessAssertion("User is already logged in", null);
	}
	
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
