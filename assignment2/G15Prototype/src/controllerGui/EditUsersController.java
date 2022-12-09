package controllerGui;

import utils.*;
import java.util.ArrayList;

import client.ChatClient;
import client.ClientController;
import common.MessageType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.util.Callback;
import server.EchoServer;
import entity.ConnectedClientEntity;
import entity.SubscriberEntity;

public class EditUsersController extends WindowControllerBase {

	@FXML
	private Button disconnectBtn;

	@FXML
	private TableView<SubscriberEntity> usersTable;

	@FXML
	private TableColumn<SubscriberEntity, Integer> idCol;

	@FXML
	private TableColumn<SubscriberEntity, String> fnameCol;

	@FXML
	private TableColumn<SubscriberEntity, String> lnameCol;

	@FXML
	private TableColumn<SubscriberEntity, String> phoneCol;
	@FXML
	private TableColumn<SubscriberEntity, String> emailCol;
	@FXML
	private TableColumn<SubscriberEntity, String> creditCol;

	@FXML
	private TableColumn<SubscriberEntity, String> subscriberCol;

	@FXML
	private Button refreshBtn;

	@FXML
	private Button saveBtn;

	ClientController chat = HostClientController.chat; // define the chat for the controller
	private ArrayList<SubscriberEntity> changedSubscriberItems = new ArrayList<>();

	@FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
		refresh(null);
		setupTable(); // setup columns connection
	}
	@FXML
	private void disconnect(ActionEvent event) {
		chat.acceptObj(MessageType.ClientDisconnect);
		System.exit(1);
	}
	@FXML
	private void refresh(ActionEvent event) {
		if(ChatClient.subscribers != null)
			ChatClient.subscribers.clear();
		chat.acceptObj(MessageType.LoadSubscribers); // get all entities to ArrayList from DB
	}

	@FXML
	private void save(ActionEvent event) {
		if (changedSubscriberItems.size() > 0) {
			chat.acceptObj(changedSubscriberItems);
			changedSubscriberItems.clear();
		}

	}

	/*
	 * Making a connection between the ConnectedClient object to the columns
	 * PropertyValueFactory search for a getters like "getIp", "getHost" in entity
	 * object
	 */
	private void setupTable() {
		usersTable.setEditable(true); // make table editable
		usersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		if(ChatClient.subscribers == null) return;
		usersTable.setItems(ChatClient.subscribers);

		// factory
		idCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, Integer>("id"));
		fnameCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("firstName"));
		lnameCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("lastName"));
		phoneCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("phoneNumber"));
		emailCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("email"));
		creditCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("creditCardNumber"));
		subscriberCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("subscriberNumber"));

		// define the editable cells
		creditCol.setCellFactory(TextFieldTableCell.forTableColumn());
		subscriberCol.setCellFactory(TextFieldTableCell.forTableColumn());

		// define the event when submit / commit
		// Handle subscriber credit card number change
		creditCol.setOnEditCommit(new EventHandler<CellEditEvent<SubscriberEntity, String>>() {
			@Override
			public void handle(CellEditEvent<SubscriberEntity, String> event) {
				SubscriberEntity subscriber = event.getRowValue();
				subscriber.setCreditCardNumber(event.getNewValue());
				if (!changedSubscriberItems.contains(subscriber))
					changedSubscriberItems.add(subscriber);
			}

		});
		// Handle subscriber number change
		subscriberCol.setOnEditCommit(new EventHandler<CellEditEvent<SubscriberEntity, String>>() {
			@Override
			public void handle(CellEditEvent<SubscriberEntity, String> event) {
				SubscriberEntity subscriber = event.getRowValue();
				if (event.getNewValue() != null) {
					// TODO check if not exists in the table
					subscriber.setSubscriberNumber(event.getNewValue());

				} else {
					// TODO make null subscriber
					subscriber.setSubscriberNumber(null);
				}
				if (!changedSubscriberItems.contains(subscriber))
					changedSubscriberItems.add(subscriber);
			}
		});

	}

}
