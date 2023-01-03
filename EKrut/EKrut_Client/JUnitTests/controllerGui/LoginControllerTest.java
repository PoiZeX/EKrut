package controllerGui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Store.NavigationStoreController;
import client.ChatClient;
import client.ClientController;
import common.Message;
import common.TaskType;
import entity.UserEntity;


class LoginControllerTest {

	
	private ClientController chat;
	private LoginController loginController;
	private UserEntity user;
	private String errorCompare;
	private Method validateSyntaxMethod;
	private Field errorMsgField;
	
	
	@BeforeEach
	void setUp() throws Exception {
		// define mock and inject
		chat = mock(ClientController.class);
		loginController = new LoginController(chat);
		
		// basic objects to work with
		user = new UserEntity();
		errorCompare = "Error in:\n";
		
		// define access to method and field
	    validateSyntaxMethod = LoginController.class.getDeclaredMethod("validateUsernamePasswordSyntax");
	    errorMsgField = LoginController.class.getDeclaredField("errorMsg");
		validateSyntaxMethod.setAccessible(true);	
	    errorMsgField.setAccessible(true);

	}

	@Test
	void loginProccessSuccess() {
		user.setUsername("rmNorth"); user.setPassword("123456");
		user.setLogged_in(false); user.setNotApproved(false);
		String[] usernamePassword = new String[] {"rmNorth", "123456"};
		when(chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);
		LoginController.validUserFromServer(user);
		boolean result = loginController.loginProccess(usernamePassword);
	
		assertTrue(result);
		assertEquals(NavigationStoreController.connectedUser, user);
	}
	
	// ------------------------------------------
	// ------------Syntax--Length----------------
	// ------------------------------------------
	
	@Test
	void usernameMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] {"u","123456"});
		validateSyntaxMethod.invoke(loginController);
	    String result = ((StringBuilder)errorMsgField.get(loginController)).toString();
	    String excepted = errorCompare + "* username length must be between 4-16\n";
	    
	    assertEquals(result, excepted);
	}
	
	@Test
	void usernameMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] {"abcdefghijklmnopqrstuvwxyz","123456"});
		validateSyntaxMethod.invoke(loginController);
	    String result = ((StringBuilder)errorMsgField.get(loginController)).toString();
	    String excepted = errorCompare + "* username length must be between 4-16\n";
	    
	    assertEquals(result, excepted);
	}
	

	@Test
	void passwordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] {"abcdef","1"});
		validateSyntaxMethod.invoke(loginController);
	    String result = ((StringBuilder)errorMsgField.get(loginController)).toString();
	    String excepted = errorCompare + "* password length must be between 4-16\n";
	    
	    assertEquals(result, excepted);
	}
	
	@Test
	void passwordMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] {"abcdef", "abcdefghijklmnopqrstuvwxyz"});
		validateSyntaxMethod.invoke(loginController);
	    String result = ((StringBuilder)errorMsgField.get(loginController)).toString();
	    String excepted = errorCompare + "* password length must be between 4-16\n";
	    
	    assertEquals(result, excepted);
	}
	
	@Test
	void usernameAndPasswordMinCharsFailed() throws Exception {
		LoginController.setUser(new String[] {"ab", "ab"});
		validateSyntaxMethod.invoke(loginController);
	    String result = ((StringBuilder)errorMsgField.get(loginController)).toString();
	    String excepted = errorCompare + "* username length must be between 4-16\n" + "* password length must be between 4-16\n";
	    
	    assertEquals(result, excepted);
	}
	
	@Test
	void usernameAndPasswordMaxCharsFailed() throws Exception {
		LoginController.setUser(new String[] {"abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"});
		validateSyntaxMethod.invoke(loginController);
	    String result = ((StringBuilder)errorMsgField.get(loginController)).toString();
	    String excepted = errorCompare + "* username length must be between 4-16\n" + "* password length must be between 4-16\n";
	    
	    assertEquals(result, excepted);
	}
	
	// ------------------------------------------
	// ---------Syntax--Illegal--Chars-----------
	// ------------------------------------------
	
	@Test
	void usernameillegalCharsFailed() throws Exception {

	}
	
}

