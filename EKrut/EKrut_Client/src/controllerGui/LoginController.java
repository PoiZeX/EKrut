package controllerGui;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import entity.MachineEntity;
import entity.UserEntity;
import enums.PopupTypeEnum;
import enums.RolesEnum;
import enums.ScreensNamesEnum;
import enums.TaskType;
import interfaces.IScreen;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.AppConfig;
import utils.PopupSetter;
/**
 * Login GUI controller, implements Screen interface
 * Handle the login boundary, validation, process and navigation
 * @author Lidor
 *
 */
public class LoginController implements IScreen {

	protected static ClientController chat;
	private static String username, password;
	// those fields for server response
	private static Boolean isValidDetails = null;
	protected static String returnedMsg = "";
	private boolean isServiceEnable = false;
	private static StringBuilder errorMsg;

	@FXML
	private Button loginBtn;

	@FXML
	private PasswordField passwordTxtField;

	@FXML
	private TextField usernameTxtField;

	@FXML
	private ComboBox<String> ektUserComboBox;

	@FXML
	private Button EKTLoginBtn;

	private static boolean isEKTpressed = false;
	private HashMap<String, String> allUsers = new HashMap<>();

	public void initialize() {
		if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
			EKTLoginBtn.setVisible(false);
			ektUserComboBox.setVisible(false);
		} else {
			EKTLoginBtn.setDisable(true);
			ObservableList<String> usernames = FXCollections.observableArrayList();
			Platform.runLater(() -> {
				allUsers = DataStore.getUsers();
				for (String username : allUsers.keySet())
					usernames.add(username);
				ektUserComboBox.setItems(usernames);

			});
			ektUserComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
				setUser(new String[] { ektUserComboBox.getSelectionModel().getSelectedItem(),
						allUsers.get(ektUserComboBox.getSelectionModel().getSelectedItem()) });
				EKTLoginBtn.setDisable(false);
			});
		}
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
		isEKTpressed = false;
		username = usernameTxtField.getText();
		password = passwordTxtField.getText();
		// validate
		if (!validateUsernamePasswordSyntax())
			return; // something more

		// call login process
		if (loginProccess(new String[] { username, password }))
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.HomePage);
		else
			showErrorMsg();
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
				return true; // Go to next screen (controller creates the screen)
		} else
			return false;
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

	public static String[] getUser() {
		return new String[] { username, password };
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
		if (!isServiceEnable)
			PopupSetter.createPopup(PopupTypeEnum.Error, errorMsg.toString());
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

		// this condition needs to be checked just if the user tried to connect via EKT
		if (isEKTpressed && !isUserAuthorizedToUseEKT(user)) {
			isValidDetails = false;
			returnedMsg = "You are not a member!\n\nJust members can enjoy the benefit of quick login (And much more things)";
			return;
		}
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
		isEKTpressed = true;
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
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	/**
	 * Sets login btn disable while connect with EKT
	 * 
	 * @param disable
	 */
	protected void setLoginBtnDisable(boolean disable) {
		usernameTxtField.setDisable(disable);
		passwordTxtField.setDisable(disable);
		loginBtn.setDisable(disable);
		EKTLoginBtn.setDisable(disable);
	}

	/**
	 * return true if the user is a member or valid employee
	 * 
	 * @return
	 */
	private static boolean isUserAuthorizedToUseEKT(UserEntity user) {
		if (user.getRole_type().equals(RolesEnum.user) || user.getRole_type().equals(RolesEnum.registered))
			return false;

		// here check for employee (member as well)
		// every registered employee is a member (Our choice because we love our
		// workers)
		// detect it if the worker has credit card number
		if (CommonFunctions.isNullOrEmpty(user.getCc_num()))
			return false;
		return true;
	}

	public void showErrorMsg() {
		PopupSetter.createPopup(PopupTypeEnum.Error, returnedMsg);

	}

}
