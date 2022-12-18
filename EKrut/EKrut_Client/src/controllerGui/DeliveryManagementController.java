package controllerGui;



import java.util.ArrayList;

import client.ChatClient;
import client.ClientController;
import common.DeliveryStatus;
import common.MessageType;
import entity.AddressEntity;
import entity.DeliveryEntity;
import entity.SubscriberEntity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class DeliveryManagementController {

    @FXML
    private TableColumn<DeliveryEntity, String> actualTimeCol;

    @FXML
    private TableColumn<DeliveryEntity, AddressEntity> addressCol;

    @FXML
    private TableColumn<DeliveryEntity, Integer> costumerIdCol;

    @FXML
    private TableView<DeliveryEntity> deliveryTable;

    @FXML
    private TableColumn<DeliveryEntity, String> estimatedTimeCol;
    
    @FXML
    private TableColumn<DeliveryEntity, Integer> orderIdCol;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TableColumn<DeliveryEntity, DeliveryStatus> statusCol;

    ClientController chat = HostClientController.chat; // define the chat for the controller
	private ArrayList<SubscriberEntity> changedSubscriberItems = new ArrayList<>();

	@FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
		refresh(null);
		//setupTable(); // setup columns connection
	}

	@FXML
	private void refresh(ActionEvent event) {
		if (ChatClient.subscribers != null)
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
	/*private void setupTable() {
		usersTable.setEditable(true); // make table editable
		usersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		if (ChatClient.subscribers == null)
			return;
		usersTable.setItems(ChatClient.subscribers);

		// factory
		idCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, Integer>("id"));
		fnameCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("firstName"));
		lnameCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("lastName"));
		phoneCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("phoneNumber"));
		emailCol.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("email"));
		creditCol
				.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("creditCardNumber"));
		subscriberCol
				.setCellValueFactory((Callback) new PropertyValueFactory<SubscriberEntity, String>("subscriberNumber"));

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

	}*/


}
