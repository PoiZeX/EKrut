package controllerGui;

import java.util.regex.Pattern;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
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
	private static ClientController chat = HostClientController.chat; // one instance
	private static String username, password;
	// those fields for server response
	private static Boolean isValidDetails = null;
	private static String returnedMsg = "";

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

		sendServerusernamePassword(new String[] { username, password });
		while (isValidDetails == null) { // wait for answer
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (isValidDetails) {
			// Go to next screen (controller creates the screen)
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.HomePage);
		}

		else
			System.out.println(returnedMsg);
	}

	/**
	 * return true if username and password are valid syntax and length
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
			if (password.length() > passwordMaxLength || password.length() < passwordMinLength)
				error.append("* password length must be between " + passwordMinLength + "-" + passwordMaxLength + "\n");
		}

		if (error.toString().equals("Error in:\n"))
			return true;
		{
			// show popup message for error
			System.out.println(error.toString());
			return false;

		}

	}

	/**
	 * Validated the details of given user from server checks: User is not logged
	 * in, user approved, and the username & password matches
	 * 
	 * @param user
	 */
	public static void validUserFromServer(UserEntity user) {
		if (CommonFunctions.isNullOrEmpty(user.getUsername())
				|| (user.getUsername().equals(username) && !user.getPassword().equals(password))) {
			isValidDetails = false;
			returnedMsg = "Username or Password are invalid"; // technically username doesn't exist but we don't want to
																// show this
			return;
		}

		if (user.isLogged_in()) {
			isValidDetails = false;
			returnedMsg = "User is already logged in";
			return;
		}

		// user is approved ?
		if (user.isNotApproved()) {
			isValidDetails = false;
			returnedMsg = "User is not approved yet";
			return;
		}

		// got here - everything was good
		isValidDetails = true;
		returnedMsg = "Success";
		user.setLogged_in(true);  // TODO: change in DB
		NavigationStoreController.connectedUser = user; // set the current connected user to system
		return;

	}

	private void sendServerusernamePassword(String[] usernamePassword) {
		// username and password match

		chat.acceptObj(usernamePassword);
		// not logged in

		// is approved in manager

	}

	@FXML
	void ektLoginAction(ActionEvent event) {
		//
	}

}
