package controllerGui;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import enums.PopupTypeEnum;
import enums.ScreensNamesEnum;
import enums.TaskType;
import interfaces.IScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.AppConfig;
import utils.PopupSetter;
/**
 * Host Client GUI controller, implements Screen interface
 * Handler for first screen of client-server connection
 * @author Lidor
 *
 */
public class HostClientController implements IScreen {

	@FXML
	private BorderPane borderPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private TextField hostTxt;

	@FXML
	private TextField portTxt;

	@FXML
	private Button connectBtnclient;

	@FXML
	private Label gridLabel;

	@FXML
	private Label headLine;
	private static ClientController chat = null; // only one instance

	/**
	 * Handles the action of sending the entered host and port to the server in
	 * order to establish a connection. Validates that the input is not empty and
	 * that the port is an integer. Navigates to the login screen if the connection
	 * is successful, otherwise displays an error message.
	 *
	 * @param event the event that triggered this method
	 */
	@FXML
	private void SendPort(ActionEvent event) {
		String host = hostTxt.getText(), port = portTxt.getText();

		// Validate
		if (CommonFunctions.isNullOrEmpty(port) || CommonFunctions.isNullOrEmpty(host)) {
			PopupSetter.createPopup(PopupTypeEnum.Warning, "Please fill host & port");
			return;
		}
		try {
			Integer.parseInt(port);
		} catch (Exception ex) {
			PopupSetter.createPopup(PopupTypeEnum.Warning, "Please insert digits only");
			return;
		}

		// Establish connection
		chat = new ClientController(host, Integer.parseInt(port));
		if (chat.acceptObj(new Message(TaskType.ClientConnect, null))) // send server that client connected
		{
			// Go to next screen (controller creates the screen)
			DataStore.InitAllUsers();
			while (!DataStore.recievedData)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.Login);

		}

		else {
			PopupSetter.createPopup(PopupTypeEnum.Error, "Could not connect to server.");
		}

	}

	/**
	 * 
	 * The start method is responsible for setting the current screen in the
	 * Navigation Store to HostClient and checking if a machine id has been
	 * provided. If no machine id has been provided, a warning message is displayed
	 * to the user.
	 * 
	 * @param primaryStage The primary stage of the application.
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.HostClient);
		if (AppConfig.MACHINE_ID <= 0)
			PopupSetter.createPopup(PopupTypeEnum.Warning, "You must provide a machine id\nThe syntax should be:\n\n"
					+ "java -jar EKrut_Client.jar arg <machine_id>");
	}

	@Override
	public void initialize() {

	}

	/**
	 * 
	 * Returns the current instance of the ClientController.
	 * 
	 * @return The current instance of the ClientController.
	 */
	public static ClientController getChat() {
		return chat;
	}

}
