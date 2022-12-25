package controllerGui;

import Store.NavigationStoreController;
import client.ClientController;
import common.TaskType;
import common.CommonData;
import common.Message;
import common.RolesEnum;
import common.ScreensNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class ReportSelectionController {
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

	ClientController chat = HostClientController.chat; // define the chat for the controller
	
	private String year, month, region;

	@FXML
	void viewReport(ActionEvent event) {
		String error = validateFields();
		errorMsgLabel.setText(error);
		if (error.equals("")) {
			month = monthItemsCmb.getSelectionModel().getSelectedItem();
			year = yearItemsCmb.getSelectionModel().getSelectedItem().toString();
			switch (getSelectedReport()) {
			case "supplyReport":
				chat.acceptObj(new Message(TaskType.RequestReport, new String[] {"supply", region, month, year }));
				checkReportData(SupplyReportController.RecievedData, ScreensNames.SupplyReport);
				break;
			case "ordersReport":
				chat.acceptObj(new Message(TaskType.RequestReport, new String[] {"orders", region, month, year }));
				checkReportData(OrdersReportController.RecievedData, ScreensNames.OrdersReport);
				break;
			case "clientsReport":
				chat.acceptObj(new Message(TaskType.RequestReport, new String[] {"clients", region, month, year }));
				checkReportData(ClientsReportController.RecievedData, ScreensNames.ClientsReport);
				break;
			}
		}
	}

	private void checkReportData(boolean RecievedData, ScreensNames screen) {
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
				NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.OrdersReport);
			break;
		case SupplyReport:
			SupplyReportController.setReport(year, month, region);
			break;
		case ClientsReport:
			if (ClientsReportController.reportDetails.getDescription().equals("noreport")
					|| ClientsReportController.reportDetails.getTotalSalesArr() == null
					|| ClientsReportController.reportDetails.getSupplyMethodsArr() == null)
				errorMsgLabel.setText("No Report Found");
			else
				NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ClientsReport);
		default:
			break;

		}
	}

	public void initialize() {
		ObservableList<Integer> years = FXCollections.observableArrayList();
		for (int i = 2016; i <= 2023; i++) {
			years.add(i);
		}
		ObservableList<String> months = FXCollections.observableArrayList();
		months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
				"November", "December");
		ObservableList<String> regions = FXCollections.observableArrayList(CommonData.getRegions());
		setReportButtons();
		regionCmb.setItems(regions);
		monthItemsCmb.setItems(months);
		yearItemsCmb.setItems(years);

		regionCmb.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				region = regionCmb.getValue();
			}
		});

		if (NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.CEO)) {
			regionLabel.setDisable(false);
			regionCmb.setDisable(false);
		} else {
			region = NavigationStoreController.connectedUser.getRegion();
			regionCmb.getSelectionModel().select(region);
		}

	}

	String validateFields() {
		String errorMsg = "";
		if (monthItemsCmb.getSelectionModel().isEmpty() && yearItemsCmb.getSelectionModel().isEmpty()) {
			errorMsg = "Please Select Month and Year";
		} else if (monthItemsCmb.getSelectionModel().isEmpty()) {
			errorMsg = "Please Select Month";
		} else if (yearItemsCmb.getSelectionModel().isEmpty()) {
			errorMsg = "Please Select Year";
		}
		if (errorMsg != "" && selectedReport == "") {
			errorMsg += " and Report Type";
		}
		if (errorMsg == "" && selectedReport == "") {
			errorMsg = "Please Select Report Type";
		}
		return errorMsg;
	}

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

	private String getSelectedReport() {
		return this.selectedReport;
	}
}
