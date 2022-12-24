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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ComboBox<String> machineCmb;
    
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
    private TableColumn<?, ?> previewEyeBtnCol;

   
    
    private static ClientController chat = HostClientController.chat; // define the chat for th
    public static ObservableList<ItemInMachineEntity> itemsInMachineLst=FXCollections.observableArrayList();
    private String machineName;
    /** Setup screen before launching view*/
    @FXML
		public void initialize() throws Exception {
	    	ObservableList<String> machinesNames=FXCollections.observableArrayList(getMachines(CommonData.getMachines()));
	    	machineCmb.setItems(machinesNames);
	    	ObservableList<String> statusLst = FXCollections.observableArrayList();
	    	statusLst.addAll(ItemInMachineEntity.call_Status.values());
	    	callStatusCmb.setItems(statusLst);
	    	machineCmb.addEventHandler(ComboBox.ON_SHOWN, new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					machineName = machineCmb.getValue();
					setupTable();
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
    private void setupTable() {
    	chat.acceptObj(new Message(TaskType.RequestItemsInMachine, null));
    	// factory
    			itemIdCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("itemId"));
    			currentAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("currentAmount"));
    			minAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("minAmount"));
    			callStatusCall.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, ItemInMachineEntity.call_Status>("callStatus"));
     }
   
    /**get machines and put them in a combo box*/
    public ArrayList<String> getMachines(ArrayList<MachineEntity> allMachines) {
    	String region =NavigationStoreController.connectedUser.getRegion();
    	ArrayList<String> machinesNames =  new ArrayList<String>();
    	for(MachineEntity m : allMachines) {
    		if(region.equals(m.reigonName))
    			{
    			machinesNames.add(m.getMachineName());
    			}
    	}
		return machinesNames;
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
    
    
   
    
}
