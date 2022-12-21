package controllerGui;

import java.io.IOException;
import java.util.regex.Pattern;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.TaskType;
import common.Message;
import common.ScreensNames;
import entity.UserEntity;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.PopupWindow;

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

	/**
	 * Handles login with username and password for EKrut clients
	 * 
	 * @param event
	 */
	@FXML
	void loginBtnAction(ActionEvent event) {
		username = usernameTxtField.getText();
		password = passwordTxtField.getText();
		// validate
		if (!validateUsernamePasswordSyntax())
			return; // something more

		// sends the user information to server
		chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, new String[] { username, password }));

		// wait for answer
		while (isValidDetails == null) {
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
		user.setLogged_in(true); // TODO: change in DB
		NavigationStoreController.connectedUser = user; // set the current connected user to system
		return;

	}

	/**
	 * sends a user
	 * 
	 * @param usernamePassword
	 */
	private void sendServerusernamePassword(String[] usernamePassword) {
		chat.acceptObj(new Message(TaskType.RequestUserFromDB, usernamePassword));
	}

	/**
	 * Handles the login via EKT with popup
	 * 
	 * @param event
	 */
	@FXML
	void ektLoginAction(ActionEvent event) {
		PopupWindow pop = new PopupWindow();
		// pop.startEKTPopup(NavigationStoreController.getInstance().getPrimaryStage());
		Stage primaryStage = new Stage();

		Parent root;
		try {

			String path = "/boundary/EKTPopupBoundary.fxml";
			root = FXMLLoader.load(getClass().getResource(path));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Login with EKT");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();

		}
		Task task = new Task<Void>() {
			@Override
			public Void call() {
				while (PopupWindow.isFinishAndSuccess == 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (PopupWindow.isFinishAndSuccess == 1) {
					NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.HomePage);
				}
				primaryStage.close();
				return null;
			}
		};
		new Thread(task).start();



	}

}
