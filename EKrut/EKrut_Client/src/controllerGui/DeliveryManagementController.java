package controllerGui;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import client.ClientController;
import common.DeliveryStatus;
import common.MessageType;
import entity.DeliveryEntity;
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

public class DeliveryManagementController {

    @FXML
    private TableColumn<DeliveryEntity, String> actualTimeCol;

    @FXML
    private TableColumn<DeliveryEntity, String> addressCol;

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
    /* need to handle: 
     *   send message to the costumer with the estimated Time
     *   get customer approval and update the actual arrivel time*/
   private static ClientController chat = HostClientController.chat; // define the chat for the controller
	private ArrayList<DeliveryEntity> changedDeliveryItems = new ArrayList<>();
	public static ObservableList<DeliveryEntity> deliveries=FXCollections.observableArrayList();

	@FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
		refresh(null);
		setupTable(); // setup columns connection
		
	}
	
	@FXML
	private void refresh(ActionEvent event) {
		  if (deliveries != null)
			  deliveries.clear();
		  chat.acceptObj(MessageType.LoadDeliveries); // get all entities to ArrayList from DB
		  
		 	}

	@FXML
	private void save(ActionEvent event) {
		  if (changedDeliveryItems.size() > 0) {
			  chat.acceptObj(changedDeliveryItems);
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
		costumerIdCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, Integer>("costumerId"));
		addressCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, String>("address"));
		estimatedTimeCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, String>("estimatedTime"));
		actualTimeCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, String>("actualTime"));
		statusCol.setCellValueFactory((Callback) new PropertyValueFactory<DeliveryEntity, DeliveryStatus>("status"));
	

		// define the editable cells- delivery status
		ObservableList<DeliveryStatus> statusLst = FXCollections.observableArrayList();
		statusLst.addAll(DeliveryStatus.values());
		statusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusLst));
		
		/* Handle delivery status edit:
		 * can change from "pendingApproval" to "outForDelivery"
		 * or from "outForDelivery" to "done".
		 * in other cases, the changes aren't saved*/
		statusCol.setOnEditCommit(new EventHandler<CellEditEvent<DeliveryEntity, DeliveryStatus>>() {
			@Override
			public void handle(CellEditEvent<DeliveryEntity, DeliveryStatus> event) {
				DeliveryEntity deliveryEntity = event.getRowValue();
				DeliveryStatus oldStatus=deliveryEntity.getStatus();
				DeliveryStatus newStatus=event.getNewValue();
				String msg="Hi!\nyour delivery is on the way,\nthe estimated ariivel time is ";
				if(!oldStatus.equals(newStatus)) {
					switch (newStatus){
					case outForDelivery:
						if(oldStatus.equals(DeliveryStatus.pendingApproval)) {
							deliveryEntity.setEstimatedTime(calculateEstimatedTime()); 
							deliveryEntity.setStatus(newStatus);
							msg+=calculateEstimatedTime();
							System.out.println(msg);
							//TODO add: send message to the costumer with the estimated Time
						}	
						break;
					case done:
						if(oldStatus.equals(DeliveryStatus.outForDelivery)) 
							deliveryEntity.setStatus(newStatus);
						break;
					default:
						break;
					}
					if (!changedDeliveryItems.contains(deliveryEntity))
						changedDeliveryItems.add(deliveryEntity);
				}
			}

		});

	}
	
	/*calculae the estimated delivery time
	 * Between 6:00 to 16:00 the estimated arrival time is within 4 hours.
	 * Between 00:00 to 06:00 the estimated arrival time is within 12 hours.
	 * Between 16:00 to 00:00 the estimated arrival time is in the next day (about 18 hours).  */
	
	private String calculateEstimatedTime() {
		 Calendar estimated = Calendar.getInstance();  
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		 
		 Calendar fourPM = Calendar.getInstance();   
		 fourPM.set(Calendar.HOUR_OF_DAY,16);
		 fourPM.set(Calendar.MINUTE,0);
		 
		 
		 Calendar sixAM = Calendar.getInstance();  
		 sixAM.set(Calendar.HOUR_OF_DAY, 6);
		 sixAM.set(Calendar.MINUTE,0);
		 
		
		 if(estimated.after(fourPM)) {
			 estimated.add(Calendar.DATE, 1);
			 estimated.add(Calendar.HOUR, -6);
		 }
		 else if(estimated.before(sixAM)) {
			 estimated.add(Calendar.HOUR, 12);
		 }
		 else {
			 estimated.add(Calendar.HOUR, 4);
		 }
		 
		return formatter.format(estimated.getTime());
	}
	/* adding the deliveryEntity to deliveries list  */
	public static void getDeliveryEntityFromServer(DeliveryEntity deliveryEntity) {
		deliveries.add(deliveryEntity);
	}


}
