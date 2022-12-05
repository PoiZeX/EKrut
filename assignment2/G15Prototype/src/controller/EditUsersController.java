package controller;

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
import entity.ConnectedClient;
import entity.Subscriber;

public class EditUsersController extends WindowControllerBase {

	@FXML
	private Button backBtn;

	@FXML
	private TableView<Subscriber> usersTable;

	@FXML
	private TableColumn<Subscriber, Integer> idCol;

	@FXML
	private TableColumn<Subscriber, String> fnameCol;

	@FXML
	private TableColumn<Subscriber, String> lnameCol;

	@FXML
	private TableColumn<Subscriber, String> phoneCol;
	@FXML
	private TableColumn<Subscriber, String> emailCol;
	@FXML
	private TableColumn<Subscriber, String> creditCol;

	@FXML
	private TableColumn<Subscriber, String> subscriberCol;

	@FXML
	private Button refreshBtn;

	@FXML
	private Button saveBtn;

	ClientController chat = HostClientUIController.chat; // define the chat for the controller
	private ArrayList<Subscriber> changedSubscriberItems = new ArrayList<>();

	@FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
		refresh(null);
		setupTable(); // setup columns connection
	}

	@FXML
	private void back(ActionEvent event) {

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
		idCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, Integer>("id"));
		fnameCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, String>("firstName"));
		lnameCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, String>("lastName"));
		phoneCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, String>("phoneNumber"));
		emailCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, String>("email"));
		creditCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, String>("creditCardNumber"));
		subscriberCol.setCellValueFactory((Callback) new PropertyValueFactory<Subscriber, String>("subscriberNumber"));

		// define the editable cells
		creditCol.setCellFactory(TextFieldTableCell.forTableColumn());
		subscriberCol.setCellFactory(TextFieldTableCell.forTableColumn());

		// define the event when submit / commit
		// Handle subscriber credit card number change
		creditCol.setOnEditCommit(new EventHandler<CellEditEvent<Subscriber, String>>() {
			@Override
			public void handle(CellEditEvent<Subscriber, String> event) {
				Subscriber subscriber = event.getRowValue();
				subscriber.setCreditCardNumber(event.getNewValue());
				if (!changedSubscriberItems.contains(subscriber))
					changedSubscriberItems.add(subscriber);
			}

		});
		// Handle subscriber number change
		subscriberCol.setOnEditCommit(new EventHandler<CellEditEvent<Subscriber, String>>() {
			@Override
			public void handle(CellEditEvent<Subscriber, String> event) {
				Subscriber subscriber = event.getRowValue();
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
