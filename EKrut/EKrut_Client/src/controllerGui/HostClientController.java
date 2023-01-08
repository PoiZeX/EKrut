package controllerGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.AppConfig;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.TaskType;
import common.ScreensNames;

public class HostClientController {

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
	public static ClientController chat; // only one instance

	@FXML
	void SendPort(ActionEvent event) {
		String host = hostTxt.getText(), port = portTxt.getText();

		// Validate
		if (CommonFunctions.isNullOrEmpty(port) || CommonFunctions.isNullOrEmpty(host)) {
			CommonFunctions.createPopup(PopupTypeEnum.Warning, "Please fill host & port");
			return;
		}
		try {
			Integer.parseInt(port);
		} catch (Exception ex) {
			CommonFunctions.createPopup(PopupTypeEnum.Warning, "Please insert digits only");
			return;
		}

		// Establish connection
		chat = new ClientController(host, Integer.parseInt(port));
		if (chat.acceptObj(new Message(TaskType.ClientConnect, null))) // send server that client connected
		{
			// Go to next screen (controller creates the screen)
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.Login);
		}

		else {
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Could not connect to server.");
		}

	}

	public void start(Stage primaryStage) throws Exception {
		NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.HostClient);
		if(AppConfig.MACHINE_ID <= 0)
			CommonFunctions.createPopup(PopupTypeEnum.Warning, "You must provide a machine id\nThe syntax should be:\n\n"
					+"java -jar EKrut_Client.jar arg <machine_id>");
	}

}
