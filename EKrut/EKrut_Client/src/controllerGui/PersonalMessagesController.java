package controllerGui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

	ObservableList<PersonalMessageEntity> msgsList = FXCollections.observableArrayList();

	public void initialize() {
		 LocalDateTime now = LocalDateTime.now();
	        int year = now.getYear();
	        int month = now.getMonthValue();
	        int day = now.getDayOfMonth();
	        
		msgsList.add(new PersonalMessageEntity(year, month, day, "System message", "Happy Birthday Lidor!\nI have a gift for you...\n Here is a 30$ coupon!\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nand i try long text\n\n\n\nHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsooHere alsoo"));
		setupTable();
	}

	private void setupTable() {
		messageTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
//		if (ChatClient.subscribers == null)
//			return;
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
				// messageTable.getSelectionModel().clearSelection();
			}
		});

	}

	private void askForMessages() {

	}

	private void getAllMessagesFromServer() {

	}

}
