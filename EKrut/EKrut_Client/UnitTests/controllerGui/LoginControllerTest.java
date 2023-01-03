package controllerGui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Store.NavigationStoreController;
import client.ChatClient;
import common.Message;
import common.TaskType;
import entity.UserEntity;

class LoginControllerTest {

	
	private ChatClient chat;
	private LoginController loginController;
	UserEntity user;

	@BeforeEach
	void setUp() throws Exception {
		chat = mock(ChatClient.class);
		loginController = new LoginController();
		UserEntity user = new UserEntity();
	}

	@Test
	void loginProccessSuccess() {
		user.setUsername("rmNorth"); user.setPassword("123456");
		user.setLogged_in(false); user.setNotApproved(false);
		String[] usernamePassword = new String[] {"rmNorth", "123456"};
		when(chat.handleMessageFromClient(new Message(TaskType.RequestUserFromServerDB, usernamePassword))).thenReturn(true);
		when(chat.handleMessageFromClient(new Message(TaskType.SetUserLoggedIn, user))).thenReturn(true);

		boolean result = loginController.loginProccess(usernamePassword);
		LoginController.validUserFromServer(user);
		assertTrue(result);
		assertEquals(NavigationStoreController.connectedUser, user);

	}

}
