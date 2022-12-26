package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import common.CommonFunctions;
import common.Message;
import common.TaskType;
import controller.SMSMailHandlerController;
import entity.PersonalMessageEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonalMessagesController {
	@FXML
	private TableView<PersonalMessageEntity> messageTable;
	@FXML
	private TableColumn<PersonalMessageEntity, String> dateCol;

	@FXML
	private TableColumn<PersonalMessageEntity, String> titleCol;

	@FXML
	private TableColumn<PersonalMessageEntity, String> messageCol;

	@FXML
	private Label messageLabel;

	public static ObservableList<PersonalMessageEntity> msgsList = FXCollections.observableArrayList();

	public void initialize() {
		
		SMSMailHandlerController.SendSMSOrMail("Mail", NavigationStoreController.connectedUser, "message title", "The whole message is here nigga");
		
		CommonFunctions.SleepFor(3000, () ->
		{
			requestPersonalMessages();
			setupTable();
		});
		
	}

	/**
	 * Setup the columns of table with listeners
	 */
	private void setupTable() {
		messageTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		messageTable.setItems(msgsList);

		// factory
		dateCol.setCellValueFactory(new PropertyValueFactory<PersonalMessageEntity, String>("date"));
		titleCol.setCellValueFactory(new PropertyValueFactory<PersonalMessageEntity, String>("title"));
		messageCol.setCellValueFactory(new PropertyValueFactory<PersonalMessageEntity, String>("message"));
		
		// add listner
		messageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				messageLabel.setText(newSelection.getMessage().toString()); // sets the new message
				String originalMsg = newSelection.getMessage().toString();
				String str[] = newSelection.getMessage().toString().split("\n");
				if (str.length > 1)
					newSelection.setMessage(str[0] + "...");
				else {
					if (originalMsg.length() > 256)
						newSelection.setMessage(originalMsg.substring(0, 256));
					else
						newSelection.setMessage(str[0]);
				}
				// messageTable.getSelectionModel().clearSelection();
			}
		});

	}

	/**
	 * Send a request to receive all personal messages of connected user
	 */
	private void requestPersonalMessages() {
		Message message = new Message(TaskType.RequestPersonalMessages, NavigationStoreController.connectedUser);
		HostClientController.chat.acceptObj(message);
	}

	/**
	 * Handle the receive information back from server
	 */
	public static void getAllMessagesFromServer(ArrayList<PersonalMessageEntity> obj) {
		msgsList.clear(); // if not empty before
		msgsList.addAll(obj);
	}

}
