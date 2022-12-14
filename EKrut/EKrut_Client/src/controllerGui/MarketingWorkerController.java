package controllerGui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CustomerStatus;
import common.DeliveryStatus;
import common.Message;
import common.SaleType;
import common.TaskType;
import entity.DeliveryEntity;
import entity.SaleEntity;
import entity.SaleEntity.SaleStatus;
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
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import utils.TooltipSetter;

public class MarketingWorkerController {


    @FXML
    private TableColumn<SaleEntity, LocalTime> endTimeCol;
 
    @FXML
    private TableColumn<SaleEntity, LocalTime> startTimeCol;

    @FXML
    private TableColumn<SaleEntity, SaleStatus> statusCol;

    @FXML
    private TableView<SaleEntity> salesTable;

    @FXML
    private TableColumn<SaleEntity, SaleType> saleTypeCol;

    @FXML
    private TableColumn<SaleEntity, String> daysCol;
    
    @FXML
    private Button refreshBtn;

    @FXML
    private Button returnBtn;
    
    @FXML
    private Button saveBtn;

    private static ClientController chat = HostClientController.chat; // define the chat for the controller
   	private ArrayList<SaleEntity> salesToUpdate = new ArrayList<>();
   	public static ObservableList<SaleEntity> sales=FXCollections.observableArrayList();
   	private TooltipSetter tooltip;
   	
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
    void refresh(ActionEvent event) {
    	if (sales != null)
    		sales.clear();
		chat.acceptObj(new Message(TaskType.RequestSalesFromServer, null));
    }

    @FXML
    void save(ActionEvent event) {
    	if (salesToUpdate.size() > 0) {
			chat.acceptObj(new Message(TaskType.RequestUpdateSales, salesToUpdate));
			salesToUpdate.clear();
		}
    }
	private void setupTable() {
		salesTable.setEditable(true); // make table editable
		salesTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		salesTable.setItems(sales);

		// factory
		saleTypeCol.setCellValueFactory((Callback) new PropertyValueFactory<SaleEntity, String>("saleType"));
		daysCol.setCellValueFactory((Callback) new PropertyValueFactory<SaleEntity, String>("days"));
		startTimeCol.setCellValueFactory((Callback) new PropertyValueFactory<SaleEntity, LocalTime>("startTime"));
		endTimeCol.setCellValueFactory((Callback) new PropertyValueFactory<SaleEntity, LocalTime>("endTime"));
		statusCol.setCellValueFactory((Callback) new PropertyValueFactory<SaleEntity, SaleStatus>("saleStatus"));
		
		
		// define the editable cells- delivery status
		ObservableList<SaleStatus> statusLst = FXCollections.observableArrayList();
		statusLst.addAll(SaleEntity.SaleStatus.values());
		statusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusLst));
		
		statusCol.setOnEditCommit(new EventHandler<CellEditEvent<SaleEntity, SaleStatus>>() {
			@Override
			public void handle(CellEditEvent<SaleEntity, SaleStatus> event) {
				SaleEntity saleEntity = event.getRowValue();
				SaleStatus oldStatus=saleEntity.getSaleStatus();
				SaleStatus newStatus=event.getNewValue();
				if(!oldStatus.equals(newStatus)) {
					/***
					 * case Active: start the sale
					 * case NotActive: stop the sale
					 */
				}
			}
			
		});
	}
	/* adding the saleEntity to sales list */
	public static void getSalesEntityFromServer(SaleEntity saleEntity) {
		String region =NavigationStoreController.connectedUser.getRegion();
		if(saleEntity.getRegion().equals(region))
			sales.add(saleEntity);
	}
	
}
