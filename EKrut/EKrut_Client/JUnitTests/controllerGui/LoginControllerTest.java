package controllerGui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	UserEntity user;

	@BeforeEach
	void setUp() throws Exception {
		chat = mock(ClientController.class);
		loginController = new LoginController();
		user = new UserEntity();
	}

	@Test
	void loginProccessSuccess() {
		LoginController.chat = chat;
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

}

