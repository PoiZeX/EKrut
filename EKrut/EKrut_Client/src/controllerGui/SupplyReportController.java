package controllerGui;
import java.util.ArrayList;
import java.util.Arrays;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.ScreensNames;
import common.TaskType;
import entity.MachineEntity;
import entity.SupplyReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class SupplyReportController {

    @FXML
    private TableColumn<SupplyReportEntity, String> currentAmountCol;

    @FXML
    private TableColumn<SupplyReportEntity, String> itemIDCol;

    @FXML
    private TableColumn<SupplyReportEntity, Integer> minAmountCol;

    @FXML
    private TableColumn<SupplyReportEntity, Integer> nameCol;

    @FXML
    private TableColumn<SupplyReportEntity, Integer> startAmountCol;

    @FXML
    private TableView<SupplyReportEntity> supplyMachineTbl;

    @FXML
    private Label titleLabel;
    
    @FXML
    private ComboBox<String> machineIdComboBox;
    
    protected static ClientController chat = HostClientController.chat;
	protected static SupplyReportEntity reportDetails;	
	protected static boolean RecievedData = false;
	private static ArrayList<SupplyReportEntity> supplyReports;
	private static ArrayList<MachineEntity> allMachines;
	private static String reportYear, reportMonth, reportRegion;
	private String machineName;
	private int machineID;
	
	public void initialize() {
		titleLabel.setText("Supply Report : " + reportRegion);
		allMachines = CommonData.getMachines();
		ObservableList<String> machines = FXCollections.observableArrayList();
		for (MachineEntity machine : allMachines) {
			if (NavigationStoreController.connectedUser != null)
				if (machine.getReigonName().equals(reportRegion)) {
					machines.add(machine.getMachineName());
				}
		}
		machineIdComboBox.setItems(machines);
		machineIdComboBox.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				machineName = machineIdComboBox.getValue();
				for (MachineEntity machine : allMachines) {
					if (machine.getMachineName().equals(machineName))
						machineID = machine.getId();
				}
				initTable(machineID);
				checkCurAmount();
			}			
		});
	}
	
	
	public static void setReport(String year, String month, String region) {
		reportYear = year;
		reportMonth = month;
		reportRegion = region;
		return;
	}
	
	public static void recieveDataFromServer(SupplyReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}
	
		
	public void checkCurAmount() {
		supplyMachineTbl.setRowFactory(row -> new TableRow<SupplyReportEntity>(){
		    @Override
		    public void updateItem(SupplyReportEntity report, boolean empty){
		        super.updateItem(report, empty);

		        if (report == null || empty) {
		            setStyle("");
		        } else {
		            if (report.getCur_stock() <= report.getMin_stock()) {
		                //We apply now the changes in all the cells of the row
		                for(int i=0; i<getChildren().size();i++){
		                    ((Labeled) getChildren().get(i)).setTextFill(Color.RED);
		                    //((Labeled) getChildren().get(i)).setStyle("-fx-background-color: yellow");
		                }                        
		            }
		        }
		    }
		});
	}
	
	@SuppressWarnings("unchecked")
	private void initTable(int machineID) {
		// sends the user information to server
		chat.acceptObj(new Message(TaskType.RequestReport, new String[] {"supply", reportRegion, reportMonth, reportYear, String.valueOf(machineID)}));

		// wait for answer
		while (RecievedData == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(Arrays.asList(reportDetails.getReportsList())); 
		if (reportDetails.getReportsList() == null) {
			System.out.println("Empty Report");
			for ( int i = 0; i < supplyMachineTbl.getItems().size(); i++) {
				supplyMachineTbl.getItems().clear();
			}
			return;
		}
			
		
//		supplyReports = new ArrayList<>();
//		SupplyReportEntity newReport = null;
		ArrayList<String[]> itemsArray = reportDetails.getReportsList();
		
//		itemIDCol.setCellValueFactory((Callback) new PropertyValueFactory<SupplyReportEntity, String>("item_id"));
//		nameCol.setCellValueFactory((Callback) new PropertyValueFactory<SupplyReportEntity, String>("item_name"));
//		minAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<SupplyReportEntity, Integer>("min_stock"));
//		startAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<SupplyReportEntity, Integer>("start_stock"));
//		currentAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<SupplyReportEntity, Integer>("cur_stock"));
//		for (String[] item : itemsArray) {
//			newReport = new SupplyReportEntity(reportDetails.getId(), machineID, item[0], item[1], item[2], item[3], item[4], reportDetails.getMonth(), reportDetails.getYear(), machineName, machineName);
//			supplyReports.add(newReport);
//		}
//		ObservableList<SupplyReportEntity> ol = FXCollections.observableArrayList(supplyReports);
//		supplyMachineTbl.setItems(ol);
//		supplyMachineTbl.setEditable(false);
////		supplyMachineTbl.getColumns().forEach(e -> e.setReorderable(false));
//		supplyMachineTbl.getColumns().forEach(e -> e.setSortable(false));
//		RecievedData = false;

	}
}
