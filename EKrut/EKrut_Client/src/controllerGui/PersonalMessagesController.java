package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import common.Message;
import entity.PersonalMessageEntity;
import enums.TaskType;
import interfaces.IScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Personal messages area GUI controller, implements Screen interface
 * Getting and showing user's messages from all times. SMS & Mail also (as simulation)
 * @author Lidor
 *
 */
public class PersonalMessagesController  implements IScreen {
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
	/**
	* This method is used to initialize the components of the view and set up the message table. It requests the personal messages, sets up the table and sets wrapping property of message label to true.
	*/
	@Override
	public void initialize() {
		requestPersonalMessages();
		setupTable();
		messageLabel.setWrapText(true); 
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
			}
		});

	}

	/**
	 * Send a request to receive all personal messages of connected user
	 */
	private void requestPersonalMessages() {
		Message message = new Message(TaskType.RequestPersonalMessages, NavigationStoreController.connectedUser);
		HostClientController.getChat().acceptObj(message);
	}
	/**

	Sets the personal message to be displayed in the chat.
	@param message the message to be displayed
	*/
	public static void setPersonalMessages(Object message) {
		HostClientController.getChat().acceptObj(message);
	}

	/**
	 * Handle the receive information back from server
	 */
	public static void getAllMessagesFromServer(ArrayList<PersonalMessageEntity> obj) {
		msgsList.clear(); // if not empty before
		msgsList.addAll(obj);
	}
	
	/**
	 * Return the message list
	 * @return return the observable list of all messages
	 */
	public static ObservableList<PersonalMessageEntity> getMsgList() {
		return msgsList;
	}

}
