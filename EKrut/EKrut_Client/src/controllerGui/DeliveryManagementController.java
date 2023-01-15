package controllerGui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.CustomerStatusEnum;
import common.DeliveryStatusEnum;
import common.IScreen;
import common.Message;
import common.PopupTypeEnum;
import common.TaskType;
import controller.SMSMailHandlerController;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import utils.TooltipSetter;
import javafx.scene.control.Label;

public class DeliveryManagementController  implements IScreen {


    @FXML
    private TableColumn<DeliveryEntity, String> addressCol;

	@FXML
	private TableView<DeliveryEntity> deliveryTable;

    @FXML
    private TableColumn<DeliveryEntity, String> estimatedTimeCol;
    
    @FXML
    private TableColumn<DeliveryEntity, Integer> orderIdCol;
    
    @FXML
    private TableColumn<DeliveryEntity, DeliveryStatusEnum> deliveryStatusCol;
    
    @FXML
    private TableColumn<DeliveryEntity, CustomerStatusEnum> customerStatusCol;
    
    @FXML
    private Button refreshBtn;

    @FXML
    private Button saveBtn;
    
    @FXML
    private Label errorLbl;
    

   private static ClientController chat = HostClientController.getChat(); // define the chat for the controller
	private ArrayList<DeliveryEntity> changedDeliveryItems = new ArrayList<>();
	public static ObservableList<DeliveryEntity> deliveries=FXCollections.observableArrayList();
	private TooltipSetter tooltip;
	private static UserEntity userToSend=null;
	private static final int loadingTime=2; //constant for the delivery loading time
	private static final int distance=2; //constant for distance of the destination in km
	private  final int fourAm=4;  //constant for the start time of the delivery activity
	private  final int eightPm=20; //constant for the end time of the delivery activity
	
	@FXML
	/**Setup screen before launching view*/
	@Override
	public void initialize(){
		try {
		refresh(null);
		setupTable(); // setup columns connection
		tooltip = new TooltipSetter("Save changes");
		saveBtn.setTooltip(tooltip.getTooltip());
		tooltip = new TooltipSetter("Refresh");
		refreshBtn.setTooltip(tooltip.getTooltip()); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refresh(ActionEvent event) {
		if (deliveries != null)
			deliveries.clear();
		chat.acceptObj(new Message(TaskType.RequestDeliveriesFromServer,NavigationStoreController.connectedUser.getRegion())); // get all entities to ArrayList from
																					// DB
	}
	/**
	 * Send to server the deliveries for update
	 * Send massage (SMS) to the relevant customers with the estimated arrival time
	 * @param event
	 */
	@FXML
	private void save(ActionEvent event) {
		if (changedDeliveryItems.size() > 0) {
			chat.acceptObj(new Message(TaskType.RequestUpdateDeliveries, changedDeliveryItems));
			for(DeliveryEntity de:changedDeliveryItems) {
				if(de.getDeliveryStatus().equals(DeliveryStatusEnum.outForDelivery)) {
					chat.acceptObj(new Message(TaskType.RequestUserByOrderIdFromServer,de.getOrderId()));
					waitToAnswer();
					String msg="Hi!\nOrder number "+de.getOrderId()+ 
							 " is on the way\nThe estimated arrivel time is "+de.getEstimatedTime();
					SMSMailHandlerController.SendSMSOrMail("SMS", userToSend, "Delivery", msg);
					CommonFunctions.createPopup(PopupTypeEnum.Simulation, SMSMailHandlerController.lastMsg);
					userToSend=null;
				}
			}
			changedDeliveryItems.clear();
		}

	}
	/**
	 * Waiting to receive the UserEntity from the server
	 */
	private void waitToAnswer() {
		while (userToSend == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  setUp the delivery table according to the region
	 *  Handle delivery status edit:
		 * can change from "pendingApproval" to "outForDelivery"
		 * or from "outForDelivery" to "done".
		 * in other cases, the changes aren't saved
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		deliveryStatusCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, DeliveryStatusEnum>("deliveryStatus"));
		customerStatusCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, CustomerStatusEnum>("customerStatus"));


		// define the editable cells- delivery status
		ObservableList<DeliveryStatusEnum> statusLst = FXCollections.observableArrayList();
		statusLst.addAll(DeliveryStatusEnum.values());
		deliveryStatusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusLst));
		colorTableRows(deliveryTable);
		// Handle delivery status edit
		deliveryStatusCol.setOnEditCommit(new EventHandler<CellEditEvent<DeliveryEntity, DeliveryStatusEnum>>() {
			@Override
			public void handle(CellEditEvent<DeliveryEntity, DeliveryStatusEnum> event) {
				DeliveryEntity deliveryEntity = event.getRowValue();
				DeliveryEntity deliveryEntityUpdate=new DeliveryEntity(deliveryEntity.getOrderId(),deliveryEntity.getRegion(),deliveryEntity.getAddress(),
						deliveryEntity.getEstimatedTime(),deliveryEntity.getDeliveryStatus(),deliveryEntity.getCustomerStatus());
				DeliveryStatusEnum oldStatus=deliveryEntity.getDeliveryStatus();
				DeliveryStatusEnum newStatus=event.getNewValue();
			
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
						if(oldStatus.equals(DeliveryStatusEnum.pendingApproval)) {
							deliveryEntityUpdate.setEstimatedTime(calculateEstimatedTime());
							deliveryEntityUpdate.setDeliveryStatus(newStatus);
							errorLbl.setText("");
						}
						else {
						errorLbl.setText("Can't change from done status to outForDelivery status ");}
						break;
					case done:
						if(oldStatus.equals(DeliveryStatusEnum.outForDelivery)) {
							if( deliveryEntity.getCustomerStatus().equals(CustomerStatusEnum.APPROVED)) {
								deliveryEntityUpdate.setDeliveryStatus(newStatus);
								errorLbl.setText("");
							}
							else {
							errorLbl.setText("The customer's status is \"not approved\". Unable to change status to \"Done\"");}
						}
						else {
						errorLbl.setText("Can't change from pendingApproval status to done status ");}
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

	/***
	 * color tables rows by the cuurent amount and the status
	 */
	private void colorTableRows(TableView<DeliveryEntity> table) {
		table.setRowFactory(tv -> new TableRow<DeliveryEntity>() {
			@Override
			protected void updateItem(DeliveryEntity delivery, boolean empty) {
				super.updateItem(delivery, empty);
				if (delivery == null)
					setStyle("");
				else if (delivery.getCustomerStatus().equals(CustomerStatusEnum.APPROVED))
					setStyle("-fx-background-color: #7cf28f;");
				else
					setStyle("");
			}
		});
	}

	/**
	 * calculae the estimated delivery time according to the current time:
	 *  Between 4:00 to 20:00 the estimated is within loadingTime+distance hours. 
	 *  Between 00:00 to 04:00 the estimated is within 5+loadingTime+distance hours .
	 *  Between 20:00 to 00:00 the estimated is in the next day at current hour+loadingTime+distance hours.
	 */
	private String calculateEstimatedTime() {
		Calendar estimated = Calendar.getInstance(); //gets now time
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		if (estimated.get(Calendar.HOUR_OF_DAY) < fourAm) {
			
			estimated.add(Calendar.HOUR, 5);
			
		
		} else if (estimated.get(Calendar.HOUR_OF_DAY) > eightPm) {
			estimated.add(Calendar.DATE, 1);
			estimated.add(Calendar.HOUR, -12);
		}	
		
		estimated.add(Calendar.HOUR, loadingTime+distance);
		return formatter.format(estimated.getTime());
	}

	/** adding the deliveryEntity to deliveries list */
	public static void getDeliveryEntityFromServer(ArrayList<DeliveryEntity> deliveriesArr) {
			deliveries.addAll(deliveriesArr);
	}
	/** gets the userEntity for send him message */
	public static void getUserEntityFromServer(UserEntity userEntity) {
		userToSend=userEntity;
}

}
