package controllerGui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import Store.NavigationStoreController;
import client.ChatClient;
import common.Message;
import common.TaskType;
import entity.PersonalMessageEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PersonalMessagesController {
	@FXML
	private TableView<PersonalMessageEntity> messageTable;
	@FXML
	private TableColumn<PersonalMessageEntity, String> dateCol;

	@FXML
	private TableColumn<PersonalMessageEntity, String> typeCol;

	@FXML
	private TableColumn<PersonalMessageEntity, String> msgprevCol;

	@FXML
	private Label messageLabel;

	public static ObservableList<PersonalMessageEntity> msgsList = FXCollections.observableArrayList();

	public void initialize() {
		requestPersonalMessages();
//		msgsList.add(new PersonalMessageEntity(0, year, month, day, "System message",
//				"Happy Birthday Lidor!\nI have a gift for you...\n Here is a 30$ coupon!\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nand i try long text\n\n\n\nHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsoo"));
		setupTable();
	}

	private void setupTable() {
		messageTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		messageTable.setItems(msgsList);

		// factory
		dateCol.setCellValueFactory(new PropertyValueFactory<PersonalMessageEntity, String>("date"));
		typeCol.setCellValueFactory(new PropertyValueFactory<PersonalMessageEntity, String>("type"));
		msgprevCol.setCellValueFactory(new PropertyValueFactory<PersonalMessageEntity, String>("msgprev"));
		// add listner
		messageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
//				String[] stringArray;
//				String finalMsg = "";
//				stringArray = newSelection.getMsgprev().toString().split("\n");
//				for (String str : stringArray)
//					finalMsg += str + "\n";
				messageLabel.setText(newSelection.getMsgprev().toString()); // sets the new message
				String originalMsg = newSelection.getMsgprev().toString();
				String str[] = newSelection.getMsgprev().toString().split("\n");
				if (str.length > 1)
					newSelection.setMsgprev(str[0] + "...");
				else {
					if (originalMsg.length() > 256)
						newSelection.setMsgprev(originalMsg.substring(0, 256));
					else
						newSelection.setMsgprev(str[0]);
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
