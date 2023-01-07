package controllerGui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.CustomerStatus;
import common.DeliveryStatus;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import utils.TooltipSetter;
import javafx.scene.control.Label;

public class DeliveryManagementController {


    @FXML
    private TableColumn<DeliveryEntity, String> addressCol;

	@FXML
	private TableView<DeliveryEntity> deliveryTable;

    @FXML
    private TableColumn<DeliveryEntity, String> estimatedTimeCol;
    
    @FXML
    private TableColumn<DeliveryEntity, Integer> orderIdCol;
    
    @FXML
    private TableColumn<DeliveryEntity, DeliveryStatus> deliveryStatusCol;
    
    @FXML
    private TableColumn<DeliveryEntity, CustomerStatus> customerStatusCol;
    
    @FXML
    private Button refreshBtn;

    @FXML
    private Button saveBtn;
    
    @FXML
    private Label errorLbl;
    

   private static ClientController chat = HostClientController.chat; // define the chat for the controller
	private ArrayList<DeliveryEntity> changedDeliveryItems = new ArrayList<>();
	public static ObservableList<DeliveryEntity> deliveries=FXCollections.observableArrayList();
	private TooltipSetter tooltip;
	private static UserEntity userToSend=null;
	private String msg="Hi!\nyour delivery is on the way,\nthe estimated arrivel time is ";

	@FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
		refresh(null);
		setupTable(); // setup columns connection
		tooltip = new TooltipSetter("Save changes");
		saveBtn.setTooltip(tooltip.getTooltip());
		tooltip = new TooltipSetter("Refresh");
		refreshBtn.setTooltip(tooltip.getTooltip()); 
		
	}

	@FXML
	private void refresh(ActionEvent event) {
		if (deliveries != null)
			deliveries.clear();
		chat.acceptObj(new Message(TaskType.RequestDeliveriesFromServer,NavigationStoreController.connectedUser.getRegion())); // get all entities to ArrayList from
																					// DB
	}

	@FXML
	private void save(ActionEvent event) {
		if (changedDeliveryItems.size() > 0) {
			chat.acceptObj(new Message(TaskType.RequestUpdateDeliveries, changedDeliveryItems));
			//if(userToSend!=null)
				//SMSMailHandlerController.SendSMSOrMail(SMS, userToSend, "Delivery", msg);
			changedDeliveryItems.clear();
		}

	}

	private void setupTable() {
		deliveryTable.setEditable(true); // make table editable
		deliveryTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		if (deliveries == null)
			return;
		deliveryTable.setItems(deliveries);

		// factory
		orderIdCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, Integer>("orderId"));
		addressCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, String>("address"));
		estimatedTimeCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, String>("estimatedTime"));
		deliveryStatusCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, DeliveryStatus>("deliveryStatus"));
		customerStatusCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, CustomerStatus>("customerStatus"));


		// define the editable cells- delivery status
		ObservableList<DeliveryStatus> statusLst = FXCollections.observableArrayList();
		statusLst.addAll(DeliveryStatus.values());
		deliveryStatusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusLst));
		
		/** Handle delivery status edit:
		 * can change from "pendingApproval" to "outForDelivery"
		 * or from "outForDelivery" to "done".
		 * in other cases, the changes aren't saved*/
		deliveryStatusCol.setOnEditCommit(new EventHandler<CellEditEvent<DeliveryEntity, DeliveryStatus>>() {
			@Override
			public void handle(CellEditEvent<DeliveryEntity, DeliveryStatus> event) {
				DeliveryEntity deliveryEntity = event.getRowValue();
				DeliveryEntity deliveryEntityUpdate=new DeliveryEntity(deliveryEntity.getOrderId(),deliveryEntity.getRegion(),deliveryEntity.getAddress(),
						deliveryEntity.getEstimatedTime(),deliveryEntity.getDeliveryStatus(),deliveryEntity.getCustomerStatus());
				DeliveryStatus oldStatus=deliveryEntity.getDeliveryStatus();
				DeliveryStatus newStatus=event.getNewValue();
				if(!oldStatus.equals(newStatus)) {
					switch (newStatus){
					case pendingApproval:
						if(!CommonFunctions.isNullOrEmpty(deliveryEntity.getEstimatedTime()))
							errorLbl.setText("Can't change to pendingApproval ");
						else {
							deliveryEntityUpdate.setEstimatedTime("");
							deliveryEntityUpdate.setDeliveryStatus(newStatus);
						}
						break;
					case outForDelivery:
						if(oldStatus.equals(DeliveryStatus.pendingApproval)) {
							deliveryEntityUpdate.setEstimatedTime(calculateEstimatedTime());
							deliveryEntityUpdate.setDeliveryStatus(newStatus);
							errorLbl.setText("");
							msg+=calculateEstimatedTime();
							//chat.acceptObj(new Message(TaskType.RequestUserByOrderIdFromServer,deliveryEntity.getOrderId()));
						//	SMSMailHandlerController.SendSMSOrMail(SMS, UserEntity to, "Delivery", msg);
							//TODO add: send message to the costumer with the estimated Time
						}
						else {
						errorLbl.setText("Can't change from done status to outForDelivery status ");}
						break;
					case done:
						if(oldStatus.equals(DeliveryStatus.outForDelivery)) {
							if( deliveryEntity.getCustomerStatus().equals(CustomerStatus.APPROVED)) {
								deliveryEntityUpdate.setDeliveryStatus(newStatus);
								errorLbl.setText("");
							}
							else {
							errorLbl.setText("The customer's status is \"not approved\". Unable to change status to \"Done\"");}
						}
						else {
						errorLbl.setText("Can't change from pendingApproval status to done status ");}
						break;
					default:
						
						break;
					}
					
				}
				//for save the last update
				if (changedDeliveryItems.contains(deliveryEntityUpdate))
					changedDeliveryItems.remove(deliveryEntityUpdate);
				changedDeliveryItems.add(deliveryEntityUpdate);
			}

		});

	}

	/**
	 * calculae the estimated delivery time Between 6:00 to 16:00 the estimated
	 * arrival time is within 4 hours. Between 00:00 to 06:00 the estimated arrival
	 * time is within 12 hours. Between 16:00 to 00:00 the estimated arrival time is
	 * in the next day (about 18 hours).
	 */

	private String calculateEstimatedTime() {
		Calendar estimated = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Calendar fourPM = Calendar.getInstance();
		fourPM.set(Calendar.HOUR_OF_DAY, 16);
		fourPM.set(Calendar.MINUTE, 0);

		Calendar sixAM = Calendar.getInstance();
		sixAM.set(Calendar.HOUR_OF_DAY, 6);
		sixAM.set(Calendar.MINUTE, 0);

		if (estimated.after(fourPM)) {
			estimated.add(Calendar.DATE, 1);
			estimated.add(Calendar.HOUR, -6);
		} else if (estimated.before(sixAM)) {
			estimated.add(Calendar.HOUR, 12);
		} else {
			estimated.add(Calendar.HOUR, 4);
		}

		return formatter.format(estimated.getTime());
	}

	/** adding the deliveryEntity to deliveries list */
	public static void getDeliveryEntityFromServer(ArrayList<DeliveryEntity> deliveriesArr) {
			deliveries.addAll(deliveriesArr);
	}
	
	public static void getUserEntityFromServer(UserEntity userEntity) {
		userToSend=userEntity;
}

}
