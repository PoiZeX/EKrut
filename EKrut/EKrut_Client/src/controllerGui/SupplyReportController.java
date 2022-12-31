package controllerGui;

import java.util.ArrayList;
import java.util.Arrays;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNames;
import common.TaskType;
import entity.MachineEntity;
import entity.SupplyReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
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
	private BarChart<String, Integer> supplySBC;

	@FXML
	private CategoryAxis xAxisSBC;

	@FXML
	private NumberAxis yAxisSBC;

	@FXML
	private Label titleLabel;

	@FXML
	private ComboBox<String> machineIdComboBox;

	@FXML
	private PieChart pieChart;

	@FXML
	private Label textConclusionsLbl;

	protected static ClientController chat = HostClientController.chat;
	protected static SupplyReportEntity reportDetails;
	protected static boolean RecievedData = false;
	private static ArrayList<MachineEntity> allMachines;
	private static String reportYear, reportMonth, reportRegion;
	private String machineName;
	private int machineID;

	public void initialize() {
		titleLabel.setText("Supply Report : " + reportRegion);
		supplySBC.setAnimated(false);
		textConclusionsLbl.setVisible(false);

		allMachines = CommonData.getMachines();
		ObservableList<String> machines = FXCollections.observableArrayList();
		for (MachineEntity machine : allMachines) {
			if (NavigationStoreController.connectedUser != null)
				if (machine.getRegionName().equals(reportRegion)) {
					machines.add(machine.getMachineName());
				}
		}
		machineIdComboBox.setItems(machines);
		machineIdComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			machineName = machineIdComboBox.getValue();
			for (MachineEntity machine : allMachines) {
				if (machine.getMachineName().equals(machineName))
					machineID = machine.getId();
			}
			initBarChart(machineID);

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

//	public void checkCurAmount() {
//		supplyMachineTbl.setRowFactory(row -> new TableRow<SupplyReportEntity>(){
//		    @Override
//		    public void updateItem(SupplyReportEntity report, boolean empty){
//		        super.updateItem(report, empty);
//
//		        if (report == null || empty) {
//		            setStyle("");
//		        } else {
//		            if (report.getCur_stock() <= report.getMin_stock()) {
//		                //We apply now the changes in all the cells of the row
//		                for(int i=0; i<getChildren().size();i++){
//		                    ((Labeled) getChildren().get(i)).setTextFill(Color.RED);
//		                    //((Labeled) getChildren().get(i)).setStyle("-fx-background-color: yellow");
//		                }                        
//		            }
//		        }
//		    }
//		});
//	}

	/**
	 * @param machineID
	 */
	@SuppressWarnings("unchecked")
	private void initBarChart(int machineID) {

		RecievedData = false; // reset each operation
		supplySBC.getData().clear(); // clear previous if exists

		// sends the user information to server
		chat.acceptObj(new Message(TaskType.RequestReport,
				new String[] { "supply", reportRegion, reportMonth, reportYear, String.valueOf(machineID) }));

		// wait for answer
		while (RecievedData == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ArrayList<String[]> itemsArray = reportDetails.getReportsList();
		// [name,min,cur,start,severity]
		if (itemsArray == null) {
			CommonFunctions.createPopup(PopupTypeEnum.Error, "No Report Found!");
			return;
		}

		XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
		yAxisSBC.setLowerBound(15);
		ObservableList<javafx.scene.chart.PieChart.Data> list = FXCollections.observableArrayList();

		for (String[] item : itemsArray) {
			series1.getData().add(new XYChart.Data<String, Integer>(item[0], Integer.parseInt(item[2])));
			series2.getData().add(new XYChart.Data<String, Integer>(item[0], Integer.parseInt(item[3])));
			list.add(new PieChart.Data(item[0], Integer.parseInt(item[4])));

		}
		series1.setName("Month Start Amount");
		series2.setName("Month End Amount");
		supplySBC.getData().addAll(series1, series2);
		pieChart.setData(list);
		String itemsAmount = "";
		itemsAmount += list.sorted().get(0).getName() + " : " + list.sorted().get(0).getPieValue() + "\n";
		itemsAmount += list.sorted().get(1).getName() + " : " + list.sorted().get(1).getPieValue() + "\n";
		itemsAmount += list.sorted().get(2).getName() + " : " + list.sorted().get(2).getPieValue() + "\n";
		textConclusionsLbl.setText("Top 3 items were filled this month\n" + itemsAmount);
		textConclusionsLbl.setVisible(true);
	}
}
