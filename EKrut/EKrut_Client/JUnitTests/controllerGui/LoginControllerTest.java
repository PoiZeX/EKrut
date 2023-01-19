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
	private Method validateSyntaxMethod, validateUserEntityMethod;
	private Field errorMsgField, validateUserMessageField;
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
		validateSyntaxMethod.setAccessible(true);
		errorMsgField.setAccessible(true);
		
		// clear fields every test
		result = "";
		excepted = "";
		NavigationStoreController.connectedUser = null;  // static so need to be cleared every test (like logout)
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
	
	
	// ------------------------------------------
	// --------------NULL--Empty-----------------
	// ------------------------------------------

	@Test
	void usernameEmptyFailed() throws Exception {
		LoginController.setUser(new String[] { "", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameNullFailed() throws Exception {
		LoginController.setUser(new String[] { null, "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void passwordEmptyFailed() throws Exception {
		LoginController.setUser(new String[] { "abcd", "" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void passwordNullFailed() throws Exception {
		LoginController.setUser(new String[] { "abcd", null });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameAndPasswordEmptyFailed() throws Exception {
		LoginController.setUser(new String[] { "", "" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username cannot be empty\n" + "* password cannot be empty\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameAndPasswordNullFailed() throws Exception {
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
	void usernameAndPasswordLengthSuccess() throws Exception {
		LoginController.setUser(new String[] { "abcd", "1234" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare;

		assertTrue(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "u", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdefghijklmnopqrstuvwxyz", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void passwordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdef", "1" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void passwordMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "abcdef", "abcdefghijklmnopqrstuvwxyz" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameAndPasswordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] { "ab", "ab" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username length must be between 4-16\n"
				+ "* password length must be between 4-16\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameAndPasswordMaxCharsFailed() throws Exception {
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
	void usernameillegalCharsStartWithDigitSuccess() throws Exception {
		LoginController.setUser(new String[] { "ab_cd", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare;

		assertTrue(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameillegalCharsStartWithDigitFailed() throws Exception {
		LoginController.setUser(new String[] { "1abc", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameillegalCharsContatinsSpaceFailed() throws Exception {
		LoginController.setUser(new String[] { "abc c", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void usernameillegalCharsContainsOthersFailed() throws Exception {
		LoginController.setUser(new String[] { "ab@@!", "123456" });
		invokeResult = invokeValidateSyntaxMethod();
		result = getErrorMsgField();
		excepted = errorCompare + "* username can contain just alphabet, digits and underscore\n";

		assertFalse(invokeResult);
		assertEquals(result, excepted);
	}

	@Test
	void passwordillegalCharsContainsSpaceFailed() throws Exception {
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

	// need to check this again. Im not sure ------------------
	@Test
	void loginProccessSuccess() {
		user.setLogged_in(false);
		user.setNotApproved(false);
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		//when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(false);
		LoginController.validUserFromServer(user);
		boolean booleanResult = loginController.loginProccess(usernamePassword);

		assertTrue(booleanResult);
		assertEquals(NavigationStoreController.connectedUser, user);
	}


	@Test
	void loginProccessUserNotExistInFailed() {
		LoginController.setUser(new String[] { "imnotexisting", "123456" });  // the user 'input'
		user.setUsername("");  // empty means user were not found
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		boolean booleanResult = loginController.loginProccess(usernamePassword);
		assertEquals(LoginController.returnedMsg, "Username or Password are incorrect");

		assertFalse(booleanResult);
		assertEquals(NavigationStoreController.connectedUser, null); 
	}
	
	@Test
	void loginProccessPasswordIncorrectInFailed() {
		LoginController.setUser(new String[] { "rmNorth", "not123456" });  // the user 'input'
		user.setUsername("rmNorth");  // empty means user were not found
		user.setPassword("123456");  // empty means user were not found
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		// act
		boolean booleanResult = loginController.loginProccess(usernamePassword);
		assertEquals(LoginController.returnedMsg, "Username or Password are incorrect");
		assertFalse(booleanResult);
		assertEquals(NavigationStoreController.connectedUser, null);
	}
	
	
	
	@Test
	void loginProccessUserLoggedInFailed() {
		user.setLogged_in(true);
		
		// prepare behavior
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		
		boolean booleanResult = loginController.loginProccess(usernamePassword);
		assertEquals(LoginController.returnedMsg, "User is already logged in");
		assertFalse(booleanResult);
		assertEquals(NavigationStoreController.connectedUser, null);
	}

	

}
