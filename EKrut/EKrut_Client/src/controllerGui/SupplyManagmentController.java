package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.TaskType;

import entity.ItemInMachineEntity;
import entity.MachineEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class SupplyManagmentController {
	
	

    @FXML
    private Label callStatusLbl;

    @FXML
    private Label callStatusTitleLbl;
    
    @FXML
    private Label workerLbl;
 
    @FXML
    private Label titleLbl;
   
   /**items info get from items in machine in data base*/
    @FXML
    private GridPane itemDisplayGridPane;
   
    @FXML
    private Label itemNameLbl;
    
    @FXML
    private Label currentAmountLbl;

    @FXML
    private TextField minAmountTxtField;

    @FXML
    private ImageView itemImg;
    /** Call status selection*/
    @FXML
	private ComboBox<String> chooseWorkerCmb;

	@FXML
    private ComboBox<ItemInMachineEntity.call_Status> callStatusCmb;

    @FXML
    private Button saveItemChangesBtn;

   

    @FXML
    private Button refreshBtn;

    @FXML
    private Button sendCallBtn;
    
  /** machine to display her items in machine supply status table*/
    @FXML
    private ComboBox<MachineEntity> machineCmb;
    
    @FXML
    private TableView<ItemInMachineEntity> supplyMangmentTbl;
    
    @FXML
    private TableColumn<ItemInMachineEntity, Integer> itemIdCol;
   
    @FXML
    private TableColumn<ItemInMachineEntity, Integer> minAmountCol;
    
    @FXML
    private TableColumn<ItemInMachineEntity, Integer> currentAmountCol;

    @FXML
    private TableColumn<ItemInMachineEntity, ItemInMachineEntity.call_Status> callStatusCall;

    @FXML
    private TableColumn<ItemInMachineEntity, Void> previewEyeBtnCol;;

   
    
    private static ClientController chat = HostClientController.chat; // define the chat for th
    public static ObservableList<ItemInMachineEntity> itemsInMachineLst=FXCollections.observableArrayList();
    public static ObservableList<MachineEntity> machineLst=FXCollections.observableArrayList(CommonData.getMachines());
    private String machineName;
    private int machineId;
    private MachineEntity machine;
    /** Setup screen before launching view*/
    @FXML
		public void initialize() throws Exception {
    		getMachinesInRegion(machineLst);
	    	machineCmb.setItems(machineLst);
	    	ObservableList<ItemInMachineEntity.call_Status> statusLst = FXCollections.observableArrayList();
	    	statusLst.addAll(ItemInMachineEntity.call_Status.values());
	    	callStatusCmb.setItems(statusLst);
	    	machineCmb.addEventHandler(ComboBox.ON_HIDDEN, new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					
					machine=machineCmb.getValue();
					setupTable(machine.machineID);
				}
			});
	    	
    }
    /** press refresh button to refresh table and item displayed ask from data base to load updated table*/
    @FXML
    void refresh(ActionEvent event) {

    }
    /** save changes that was made in the item */
    @FXML
    void saveItemChanges(ActionEvent event) {

    }
    /** send a task to the workers for update the requested items*/
    @FXML
    void send(ActionEvent event) {

    }
    /**get from DB the data for setting the table, and puttin a preview ('eye') button on the preview col*/
    private void setupTable(int machineId) {
    	chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));//errorfix
    	// factory
    			itemIdCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("itemId"));
    			currentAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("currentAmount"));
    			minAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("minAmount"));
    			callStatusCall.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, ItemInMachineEntity.call_Status>("callStatus"));
    			previewEyeBtnCol.setCellFactory(column -> {
    			    return new TableCell<ItemInMachineEntity, Void>() {
    			        private final Button button = new Button();

    			        {
    			            ImageView imageView = new ImageView(new Image("../EKrut_Client/src/styles/icons/eye.png"));
    			            button.setGraphic(imageView);
    			            button.setOnAction(event -> {
    			                // Handle button click event
    			            	//TODO load item on the side with his image
    			            });
    			        }

    			        @Override
    			        public void updateItem(Void item, boolean empty) {
    			            super.updateItem(item, empty);
    			            if (empty) {
    			                setGraphic(null);
    			            } else {
    			                setGraphic(button);
    			            }
    			        }
    			    };
    			});
    			    
     }
   
    /**get machines and put them in a combo box*/
    public void getMachinesInRegion(ObservableList<MachineEntity> machineLst2) {
    	String region =NavigationStoreController.connectedUser.getRegion();
    	for(MachineEntity m : machineLst2) {
    		if(!region.equals(m.reigonName))
    			{
    			machineLst.remove(m);
    			}
    	}

    }
    /*TODO - 
     * filter by region the machines
     * get the workers from DB and put them in CMB 
     * TABLE
     * load items in the table
     * add in the last col a button of an eye 
     * eye button - 
     * 	need to upllod the product preview on the side 
     * add css to whats under the minimum amount 
     * - colors
     * 		- red- under min and the call is not opened yet
     * 		-green- completed
     * 		-yellow - under min and the call is on process
     * 		-orange - under min and the call is opend
     * 
     * Machine CMB - 
     * 	add listener to the selection and link it to the setup table
     * 
     *  */
	//errorfix
	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		// TODO Auto-generated method stub
		itemsInMachineLst.addAll(obj);
	}
    
    
   
    
}
