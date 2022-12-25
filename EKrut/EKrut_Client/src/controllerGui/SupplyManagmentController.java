package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.TaskType;

import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
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

    @FXML
    private Button saveChangesBtn;

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
    private TableColumn<ItemInMachineEntity, Integer> machineIdCol;

    @FXML
    private TableColumn<ItemInMachineEntity, Integer> itemIdCol;
    
    @FXML
    private TableColumn<ItemInMachineEntity, Integer> currentAmountCol;

    @FXML
    private TableColumn<ItemInMachineEntity, ItemInMachineEntity.Call_Status> callStatusCol;

    @FXML
    private TableColumn<ItemInMachineEntity, Boolean> refillcol;;

   
    
    private static ClientController chat = HostClientController.chat; // define the chat for th
    public static ObservableList<ItemInMachineEntity> itemsInMachineLst=FXCollections.observableArrayList();
    public static ObservableList<MachineEntity> machineLst=FXCollections.observableArrayList();
    private String machineName;
    private int machineId;
    private MachineEntity machine;
	ArrayList<TableCell<ItemInMachineEntity, Boolean>> checkboxCellsList = new ArrayList<>();
    /** Setup screen before launching view*/
    @FXML
		public void initialize() throws Exception {
    		getMachinesInRegion(CommonData.getMachines());
	    	machineCmb.setItems(machineLst);	
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
    @SuppressWarnings("unchecked")
	private void setupTable(int machineId) {
    	chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));//errorfix
    	supplyMangmentTbl.setItems(itemsInMachineLst);
    	// factory
    			machineIdCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("machineId"));
    			itemIdCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("itemId"));
    			currentAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("currentAmount"));
    			callStatusCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, ItemInMachineEntity.Call_Status>("callStatus"));
    			refillcol.setCellFactory(column -> {
    				TableCell<ItemInMachineEntity, Boolean> cell = new CheckBoxTableCell<>();
    				checkboxCellsList.add(cell); // save the checkbox

    				cell.setOnMouseClicked(event -> {
    					if (event.getClickCount() > 0) {
    						CheckBox checkBox = (CheckBox) cell.getGraphic();
    						ItemInMachineEntity item = (ItemInMachineEntity) cell.getTableRow().getItem();
    						if (checkBox != null) {
    							if (checkBox.isSelected()) {
    								checkBox.setSelected(false);
  

    							} else {
    								checkBox.setSelected(true);
    								
    							}
    						}
    					}
    				});
    				return cell;
    			});
    			
    			
    		
     }
   
    /**get machines and put them in a combo box
     * @param arrayList */
    public void getMachinesInRegion(ArrayList<MachineEntity> arrayList) {
    	String region =NavigationStoreController.connectedUser.getRegion();
    	for(MachineEntity m : arrayList) {
    		if(region.equals(m.reigonName))
    			{
    			machineLst.add(m);
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
