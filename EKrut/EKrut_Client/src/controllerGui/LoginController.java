package controllerGui;

import java.io.IOException;
import java.util.regex.Pattern;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.TaskType;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNamesEnum;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.AppConfig;

public class LoginController  implements IScreen {

	protected static ClientController chat;
	private static String username, password;
	// those fields for server response
	private static Boolean isValidDetails = null;
	private static String returnedMsg = "";
	private boolean isServiceEnable = false;
	private static StringBuilder errorMsg;

	@FXML
	private Button loginBtn;

	@FXML
	private PasswordField passwordTxtField;

	@FXML
	private TextField usernameTxtField;

	@FXML
	private Button EKTLoginBtn;

	@Override
	public void initialize() {
		
	}
	
	public LoginController() {
		chat = HostClientController.getChat(); // one instance
	}

	/**
	 * dependency injection
	 * 
	 * @param chatService
	 */
	public LoginController(ClientController chatService) {
		chat = chatService;
		isServiceEnable = true;
	}

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

		// call login proccess
		loginProccess(new String[] { username, password });
	}

	/**
	 * Handle the login and validation of user; extracted to use in sub-class as
	 * well
	 * 
	 * @return
	 */
	protected boolean loginProccess(String[] usernamePassword) {
		// sends the user information to server
		chat.acceptObj(new Message(TaskType.RequestUserFromServerDB, usernamePassword));

		// wait for answer
		while (isValidDetails == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (isValidDetails) {
			if (!isServiceEnable)
				// Go to next screen (controller creates the screen)
				NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.HomePage);
		}

		else {
			CommonFunctions.createPopup(PopupTypeEnum.Error, returnedMsg);

			return false;
		}
		return true;
	}

	/**
	 * Control user to validate (usually will be called from registration form)
	 * 
	 * @param user
	 */
	public static void setUser(String[] user) {
		username = user[0];
		password = user[1];
		// validateUsernamePasswordSyntax();
	}

	/**
	 * return true if username and password are valid syntax and length
	 * 
	 * @return
	 */
	private boolean validateUsernamePasswordSyntax() {
		// not null
		errorMsg = new StringBuilder();
		errorMsg.append("Error in:\n");
		// username check
		if (CommonFunctions.isNullOrEmpty(username)) {
			errorMsg.append("* username cannot be empty\n");
		} else {
			// validate username
			if (username.length() > AppConfig.USERNAME_MAX_LENGTH || username.length() < AppConfig.USERNAME_MIN_LENGTH)
				errorMsg.append("* username length must be between " + AppConfig.USERNAME_MIN_LENGTH + "-"
						+ AppConfig.USERNAME_MAX_LENGTH + "\n");

			if (!Pattern.matches(AppConfig.USERNAME_ALPHA_ALLOWED, username)) // allow digits, alpha and underscore. and
																				// must starts
				// with a char
				errorMsg.append("* username can contain just alphabet, digits and underscore\n");
		}

		// password checks
		if (CommonFunctions.isNullOrEmpty(password)) {
			errorMsg.append("* password cannot be empty\n");
		} else {
			if (password.contains(" ")) // spaces are not allowed
				errorMsg.append("* password cannot contain any spaces\n");
			if (password.length() > AppConfig.PASSWORD_MAX_LENGTH || password.length() < AppConfig.PASSWORD_MIN_LENGTH)
				errorMsg.append("* password length must be between " + AppConfig.PASSWORD_MIN_LENGTH + "-"
						+ AppConfig.PASSWORD_MAX_LENGTH + "\n");
		}

		if (errorMsg.toString().equals("Error in:\n"))
			return true;

		// show popup message for error
		CommonFunctions.createPopup(PopupTypeEnum.Error, errorMsg.toString());
		return false;

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
			returnedMsg = "Username or Password are incorrect"; // technically username doesn't exist but we don't want
																// to
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
//
//		if(user.getRole_type().equals(RolesEnum.user))
//		{
//			isValidDetails = false;
//			returnedMsg = "User is not registered yet.\nPlease contact customer service for registreation";
//			return;
//		}
//		
		// got here - everything was good
		isValidDetails = true;
		returnedMsg = "Success";

		user.setLogged_in(true); // change in entity and send the entity for update in DB
		CommonFunctions.SleepFor(700, () -> {
			chat.acceptObj(new Message(TaskType.SetUserLoggedIn, user));
		});
		NavigationStoreController.connectedUser = user; // set the current connected user to system
		return;
	}

	/**
	 * Handles the login via EKT with popup
	 * 
	 * @param event
	 */
	@FXML
	void ektLoginAction(ActionEvent event) {
		Stage primaryStage = new Stage();

		Parent root;
		try {

			String path = "/boundary/EKTPopupBoundary.fxml";
			root = FXMLLoader.load(getClass().getResource(path));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Login with EKT");
			// set actions
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if (EKTPopupController.timerSuccess != null)
						EKTPopupController.timerSuccess.cancel();
					if (EKTPopupController.timerTimeLimit != null)
						EKTPopupController.timerTimeLimit.cancel();
					setLoginBtnDisable(false);
					NavigationStoreController.getInstance().getPrimaryStage().show();
				}
			});
			setLoginBtnDisable(true);
			
			// freeze current screen until got popup close
			
			NavigationStoreController.getInstance().getPrimaryStage().hide();
//			primaryStage.initModality(Modality.APPLICATION_MODAL);
//			primaryStage.showAndWait();
			
			 primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	protected void setLoginBtnDisable(boolean disable) {
		loginBtn.setDisable(disable);
		EKTLoginBtn.setDisable(disable);
	}


}
