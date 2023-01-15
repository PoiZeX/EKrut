package controllerGui;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.TaskType;
import common.IScreen;
import common.Message;
import common.RolesEnum;
import common.ScreensNamesEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

/**
 * Controller for the Orders Report screen. Handles displaying the order data in
 * a pie chart and a bar chart. Allows switching between displaying the data by
 * profit or by quantity.
 *
 * @author David
 */
public class ReportSelectionController implements IScreen {
	private String selectedReport = "";

	@FXML
	private ComboBox<String> monthItemsCmb;

	@FXML
	private ComboBox<Integer> yearItemsCmb;

	@FXML
	private ComboBox<String> regionCmb;

	@FXML
	private Label errorMsgLabel;

	@FXML
	private ToolBar reportsToolBar;

	@FXML
	private Button clientReportBtn;

	@FXML
	private Button supplyReportBtn;

	@FXML
	private Button ordersReportBtn;

	@FXML
	private Button viewReportBtn;

	@FXML
	private Label regionLabel;

	ClientController chat = HostClientController.getChat(); // define the chat for the controller

	private String year, month, region;

	/**
	 * This method is responsible for handling the event of the 'View Report' button
	 * being clicked. It validates the user input, and based on the selected report
	 * type, it sets the report data and opens the relevant report screen.
	 * 
	 * @param event the event of the 'View Report' button being clicked
	 */
	@FXML
	void viewReport(ActionEvent event) {
		String error = validateFields();
		errorMsgLabel.setText(error);
		if (error.equals("")) {
			month = monthItemsCmb.getSelectionModel().getSelectedItem();
			year = yearItemsCmb.getSelectionModel().getSelectedItem().toString();
			region = regionCmb.getSelectionModel().getSelectedItem();
			switch (getSelectedReport()) {
			case "supplyReport":
				SupplyReportController.setReport(year, month, region);
				NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.SupplyReport);
				break;
			case "ordersReport":
				chat.acceptObj(new Message(TaskType.RequestReport, new String[] { "orders", region, month, year }));
				checkReportData(OrdersReportController.RecievedData, ScreensNamesEnum.OrdersReport);
				break;
			case "clientsReport":
				chat.acceptObj(new Message(TaskType.RequestReport, new String[] { "clients", region, month, year }));
				checkReportData(ClientsReportController.RecievedData, ScreensNamesEnum.ClientsReport);
				break;
			}
		}
	}
	/**
	 * This method is used to check if the report data has been recieved from the server and to display the relevant report screen if the data is available.
	 * @param RecievedData a boolean variable indicating whether the report data has been recieved from the server or not
	 * 	@param screen the screen that represents the report type that the user selected*/
	private void checkReportData(boolean RecievedData, ScreensNamesEnum screen) {
		while (!RecievedData) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		switch (screen) {
		case OrdersReport:
			if (OrdersReportController.reportDetails.getDescription().equals("noreport")
					|| OrdersReportController.reportDetails.getReportsList() == null)
				errorMsgLabel.setText("No Report Found");
			else
				NavigationStoreController.getInstance().setCurrentScreen(screen);
			break;
		case ClientsReport:
			if (ClientsReportController.reportDetails.getDescription().equals("noreport")
					|| ClientsReportController.reportDetails.getTotalSalesArr() == null
					|| ClientsReportController.reportDetails.getSupplyMethodsArr() == null)
				errorMsgLabel.setText("No Report Found");
			else
				NavigationStoreController.getInstance().setCurrentScreen(screen);
		default:
			break;

		}
	}
	/**

	Initialize method is used to set the values for the different fields in the UI.
	It creates an ObservableList of years from 2016 to 2023.
	It creates an ObservableList of months from January to December.
	It creates an ObservableList of regions from DataStore.
	It calls setReportButtons method to set the action for each report button.
	It sets the items for the yearItemsCmb, monthItemsCmb and regionCmb with the created ObservableLists.
	If the connected user is a CEO, it enables the region selection otherwise it sets the region to the user's region.
	*/
	@Override
	public void initialize() {
		ObservableList<Integer> years = FXCollections.observableArrayList();
		for (int i = 2016; i <= 2023; i++) {
			years.add(i);
		}
		ObservableList<String> months = FXCollections.observableArrayList();
		months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
				"November", "December");
		ObservableList<String> regions = FXCollections.observableArrayList(DataStore.getRegions());
		setReportButtons();
		regionCmb.setItems(regions);
		monthItemsCmb.setItems(months);
		yearItemsCmb.setItems(years);
		if (NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.CEO)) {
			regionLabel.setDisable(false);
			regionCmb.setDisable(false);
		} else {
			region = NavigationStoreController.connectedUser.getRegion();
			regionCmb.getSelectionModel().select(region);
		}

	}
	/***
	 * validate fileds on selection for the report 
	 * @return Stirng errorMsg
	 */
	String validateFields() {
		String errorMsg = "";
		monthItemsCmb.setStyle("-fx-border-color: none;");
		yearItemsCmb.setStyle("-fx-border-color: none;");
		if (monthItemsCmb.getSelectionModel().isEmpty() && yearItemsCmb.getSelectionModel().isEmpty()) {
			errorMsg = "Please Select Month and Year";
			monthItemsCmb.setStyle("-fx-border-color: #ff1414;");
			yearItemsCmb.setStyle("-fx-border-color: #ff1414;");
		} else if (monthItemsCmb.getSelectionModel().isEmpty()) {
			errorMsg = "Please Select Month";
			monthItemsCmb.setStyle("-fx-border-color: #ff1414;");
		} else if (yearItemsCmb.getSelectionModel().isEmpty()) {
			errorMsg = "Please Select Year";
			yearItemsCmb.setStyle("-fx-border-color: #ff1414;");
		}
		if (errorMsg != "" && selectedReport == "") {
			errorMsg += " and Report Type";
		}
		if (errorMsg == "" && selectedReport == "") {
			errorMsg = "Please Select Report Type";
		}
		return errorMsg;
	}
	/**

	This method is used to set the action for each report button. It assigns a different selectedReport variable for each button.
	supplyReportBtn sets "supplyReport", ordersReportBtn sets "ordersReport", clientReportBtn sets "clientsReport"
	*/
	private void setReportButtons() {
		supplyReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				selectedReport = "supplyReport";
			}
		});
		ordersReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				selectedReport = "ordersReport";
			}
		});
		clientReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				selectedReport = "clientsReport";
			}
		});
	}
	/**

	This method is used to get the selected report. It returns the selectedReport variable.
	@return the selected report as a string
	*/
	private String getSelectedReport() {
		return this.selectedReport;
	}
}
