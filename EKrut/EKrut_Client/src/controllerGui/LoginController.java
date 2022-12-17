package controllerGui;

import java.util.regex.Pattern;

import Store.NavigationStoreController;
import client.ChatClient;
import client.ClientController;
import common.CommonFunctions;
import common.MessageType;
import common.ScreensNames;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

	private final int usernameMaxLength = 16, usernameMinLength = 4;
	private final int passwordMaxLength = 16, passwordMinLength = 4;
	private static ClientController chat = HostClientController.chat;
	private static String username, password;


	@FXML
	private Button loginBtn;

	@FXML
	private PasswordField passwordTxtField;

	@FXML
	private TextField usernameTxtField;

	@FXML
	private Button EKTLoginBtn;

	@FXML
	void loginBtnAction(ActionEvent event) {
		username = usernameTxtField.getText();
		password = passwordTxtField.getText();
		// validate
		if (!validateUsernamePasswordSyntax())
			return; // something more
		
		
		sendServerusernamePassword(new String[] { "username", "password" });
		while (isValidDetails == null) { // wait for answer
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		if (isValidDetails)
			// Go to next screen (controller creates the screen)
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.HomePage);
		else
			System.out.println(returnedMsg);
	}

	/**
	 * return true if username and password are valid
	 * 
	 * @return
	 */
	private boolean validateUsernamePasswordSyntax() {
		// not null

		StringBuilder error = new StringBuilder();
		error.append("Error in:\n");
		// username check
		if (CommonFunctions.isNullOrEmpty(username)) {
			error.append("* username cannot be empty\n");
		} else {
			// validate username
			if (username.length() > usernameMaxLength || username.length() < usernameMinLength)
				error.append("* username length must be between " + usernameMinLength + "-" + usernameMaxLength + "\n");

			if (!Pattern.matches("^[a-zA-Z][\\w]*$", username)) // allow digits, alpha and underscore. and must starts
																// with a char
				error.append("* username can contains just alphabet, digits and underscore\n");
		}

		// password checks
		if (CommonFunctions.isNullOrEmpty(password)) {
			error.append("* password cannot be empty\n");
		} else {
			if (password.contains(" ")) // spaces are not allowed
				error.append("* password cannot contains any spaces\n");
			if (username.length() > passwordMaxLength || username.length() < passwordMinLength)
				error.append("* password length must be between " + passwordMinLength + "-" + passwordMaxLength + "\n");
		}

		if (error.toString().equals("Error in:\n"))
			return true;
		{
			// show popup message for error
			return false;

		}

	}

	private static Boolean isValidDetails = null;
	private static String returnedMsg = "";

	public static void validInformation(UserEntity user) {
		if(CommonFunctions.isNullOrEmpty(user.getUsername()))
		{
			isValidDetails = false;
			returnedMsg = "User name or password are invalid";
		}
		
		if(user.getUsername().equals(username) && user.getPassword().equals(password))
		{
			// good
			isValidDetails = true;
			returnedMsg = "Success";
		}
		
		if(user.isLogged_in())
		{
			isValidDetails = false;
			returnedMsg = "User is already logged in";
		}
		
		// user is approved ?
		if(false) {
			isValidDetails = false;
			returnedMsg = "User is not approved yet";
		}
		
		
	}

	private void sendServerusernamePassword(String[] usernamePassword) {
		// username and password match

		chat.acceptObj((Object) usernamePassword);
		// not logged in

		// is approved in manager

	}

	@FXML
	void ektLoginAction(ActionEvent event) {
		//
	}

}
