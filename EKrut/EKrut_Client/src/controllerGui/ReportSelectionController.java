package controllerGui;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import enums.RolesEnum;
import enums.ScreensNamesEnum;
import enums.TaskType;
import interfaces.IScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import utils.IValidateFields;

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
	ObservableList<String> months, regions, allowedTypes;
	ObservableList<Integer> years;
	private String year, month, region;
	private Button lastPressed = null;
	private IValidateFields fieldsValidator = new FieldsValidator();

	public ReportSelectionController() {
		years = FXCollections.observableArrayList();
		for (int i = 2015; i <= 2023; i++)
			years.add(i);

		months = FXCollections.observableArrayList();
		months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
				"November", "December");

		allowedTypes = FXCollections.observableArrayList();
		allowedTypes.addAll("clientsReport", "ordersReport", "supplyReport");

		regions = FXCollections.observableArrayList();
		regions.addAll("North", "UAE", "South");
	}

	/**
	 * This method is responsible for handling the event of the 'View Report' button
	 * being clicked. It validates the user input, and based on the selected report
	 * type, it sets the report data and opens the relevant report screen.
	 * 
	 * @param event the event of the 'View Report' button being clicked
	 */
	@FXML
	void viewReport(ActionEvent event) {
		month = monthItemsCmb.getSelectionModel().getSelectedItem();
		year = yearItemsCmb.getSelectionModel().getSelectedItem().toString();
		region = regionCmb.getSelectionModel().getSelectedItem();
		String error = validateFields();
		String dateError = validateDate();
		if (error != "")
			errorMsgLabel.setText(error);
		else if (dateError != "")
			errorMsgLabel.setText(dateError);
		///
		error = "";
		///
		if (dateError.equals("") && error.equals("")) {
			switch (getSelectedReport()) {
			case "supplyReport":
				SupplyReportController.setReport(year, month, region);
				NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.SupplyReport);
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

	public String validateDate() {
		String errorMsg = "";
		try {
			Format monthFormat = new SimpleDateFormat("MM");
			Format yearFormat = new SimpleDateFormat("yyyy");
			Date selectedDate = new SimpleDateFormat("MM/yyyy")
					.parse(String.format("%s/%s", CommonFunctions.getNumericMonth(month), year));
			Date todaysDate = new SimpleDateFormat("MM/yyyy")
					.parse(String.format("%s/%s", monthFormat.format(Calendar.getInstance().getTime()),
							yearFormat.format(Calendar.getInstance().getTime())));
			if (selectedDate.compareTo(todaysDate) > 0)
				errorMsg = "Cannot select date greater than today.";
		} catch (ParseException e) {
		}
		return errorMsg;

	}

	/**
	 * This method is used to check if the report data has been recieved from the
	 * server and to display the relevant report screen if the data is available.
	 * 
	 * @param RecievedData a boolean variable indicating whether the report data has
	 *                     been recieved from the server or not
	 * @param screen       the screen that represents the report type that the user
	 *                     selected
	 */
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
				NavigationStoreController.getInstance().refreshStage(screen);
			break;
		case ClientsReport:
			if (ClientsReportController.reportDetails.getDescription().equals("noreport")
					|| ClientsReportController.reportDetails.getTotalSalesArr() == null
					|| ClientsReportController.reportDetails.getSupplyMethodsArr() == null)
				errorMsgLabel.setText("No Report Found");
			else
				NavigationStoreController.getInstance().refreshStage(screen);
		default:
			break;

		}
	}

	/**
	 * 
	 * Initialize method is used to set the values for the different fields in the
	 * UI. It creates an ObservableList of years from 2016 to 2023. It creates an
	 * ObservableList of months from January to December. It creates an
	 * ObservableList of regions from DataStore. It calls setReportButtons method to
	 * set the action for each report button. It sets the items for the
	 * yearItemsCmb, monthItemsCmb and regionCmb with the created ObservableLists.
	 * If the connected user is a CEO, it enables the region selection otherwise it
	 * sets the region to the user's region.
	 */
	@Override
	public void initialize() {
		years = FXCollections.observableArrayList();
		for (int i = 2016; i <= 2023; i++) {
			years.add(i);
		}
		months = FXCollections.observableArrayList();
		months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
				"November", "December");
		regions = FXCollections.observableArrayList(DataStore.getRegions());
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
	 * 
	 * @return String errorMsg
	 */
	public String validateFields() {
		String errorMsg = "";
		fieldsValidator.styleSetter(monthItemsCmb, false);
		fieldsValidator.styleSetter(yearItemsCmb, false);
		if (!fieldsValidator.isMonthValid() && !fieldsValidator.isYearValid()) {
			errorMsg = "Please Select Valid Month and Year";
			fieldsValidator.styleSetter(monthItemsCmb, true);
			fieldsValidator.styleSetter(yearItemsCmb, true);
		} else if (!fieldsValidator.isMonthValid()) {
			errorMsg = "Please Select Valid Month";
			fieldsValidator.styleSetter(monthItemsCmb, true);
		} else if (!fieldsValidator.isYearValid()) {
			errorMsg = "Please Select Valid Year";
			fieldsValidator.styleSetter(yearItemsCmb, true);
		}
		if (errorMsg == "" && !fieldsValidator.isSelectedReportValid()) {
			errorMsg = "Please Select Valid Report Type";
		}
		if (errorMsg != "" && !errorMsg.contains("Region") && !fieldsValidator.isRegionValid()) {
			errorMsg += " and Valid Region";
		}
		if (errorMsg == "" && !fieldsValidator.isRegionValid()) {
			errorMsg = "Please Select Valid Region";
		}
		if (errorMsg != "" && !errorMsg.contains("Report Type") && !fieldsValidator.isSelectedReportValid()) {
			errorMsg += " and Valid Report Type";
		}
		return errorMsg;
	}

	/**
	 * 
	 * This method is used to set the action for each report button. It assigns a
	 * different selectedReport variable for each button. supplyReportBtn sets
	 * "supplyReport", ordersReportBtn sets "ordersReport", clientReportBtn sets
	 * "clientsReport"
	 */
	private void setReportButtons() {
		supplyReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				if (lastPressed == null) {
					lastPressed = supplyReportBtn;
				} else {
					lastPressed.getStyleClass().remove("pressed");
					lastPressed = supplyReportBtn;

				}
				supplyReportBtn.getStyleClass().add("pressed");
				selectedReport = "supplyReport";

			}
		});
		ordersReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				if (lastPressed == null) {
					lastPressed = ordersReportBtn;
				} else {
					lastPressed.getStyleClass().remove("pressed");
					lastPressed = ordersReportBtn;
				}
				ordersReportBtn.getStyleClass().add("pressed");
				selectedReport = "ordersReport";
			}
		});
		clientReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				if (lastPressed == null) {
					lastPressed = clientReportBtn;
				} else {
					lastPressed.getStyleClass().remove("pressed");
					lastPressed = clientReportBtn;
				}
				clientReportBtn.getStyleClass().add("pressed");
				selectedReport = "clientsReport";
			}
		});
	}

	// for unit testing

	/**
	 * @param reportType sets the report type from one of the allowed types
	 * @param region     sets the region from one of the allowed region
	 * @param month      sets the months from one of the allowed months
	 * @param year       sets the year from one of the allowed years
	 */
	public void setDetails(String reportType, String region, String month, String year) {
		this.selectedReport = reportType;
		this.region = region;
		this.month = month;
		this.year = year;
	}

	/**
	 * @return allowed month values
	 */
	public ObservableList<String> getMonths() {
		return months;
	}

	/**
	 * @return allowed year values
	 */
	public ObservableList<Integer> getYears() {
		return years;
	}

	/**
	 * @return allowed region values
	 */
	public ObservableList<String> getRegions() {
		return regions;
	}

	/**
	 * @return allowed report type values
	 */
	public ObservableList<String> getReportTypes() {
		return allowedTypes;
	}

	/**
	 * @return selected report year
	 */
	public String getSelectedYear() {
		return year;
	}

	/**
	 * @return selected report region
	 */
	public String getSelectedRegion() {
		return region;
	}

	/**
	 * @return selected report month
	 */
	public String getSelectedMonth() {
		return month;
	}

	/**
	 * 
	 * This method is used to get the selected report. It returns the selectedReport
	 * variable.
	 * 
	 * @return the selected report as a string
	 */
	public String getSelectedReport() {
		return selectedReport;
	}

	/**
	 * Sets the fieldsValidator object to a given IValidateFields object
	 * 
	 * @param fieldsValidator The IValidateFields object to set as the validator
	 */
	public void setValidaions(IValidateFields fieldsValidator) {
		this.fieldsValidator = fieldsValidator;
	}

	/**
	 * Returns the current fieldsValidator object
	 * 
	 * @return The current IValidateFields object
	 */
	public IValidateFields getValidations() {
		return fieldsValidator;
	}

	private class FieldsValidator implements IValidateFields {

		/**
		 * Checks if the year has been selected
		 * 
		 * @return true if year has been selected, false otherwise
		 */
		@Override
		public boolean isYearValid() {
			return !yearItemsCmb.getSelectionModel().isEmpty();
		}

		/**
		 * Checks if the month has been selected
		 * 
		 * @return true if month has been selected, false otherwise
		 */
		@Override
		public boolean isMonthValid() {
			return !monthItemsCmb.getSelectionModel().isEmpty();
		}

		/**
		 * Checks if the region has been selected
		 * 
		 * @return true if region has been selected, false otherwise
		 */
		@Override
		public boolean isRegionValid() {
			return !regionCmb.getSelectionModel().isEmpty();
		}

		/**
		 * Sets the style of a Node depending on the value of a flag
		 * 
		 * @param n    The Node to set the style of
		 * @param flag true to set red border, false to remove border
		 */
		@Override
		public void styleSetter(Node n, boolean flag) {
			if (flag)
				n.setStyle("-fx-border-color: #ff1414;");
			else
				n.setStyle("-fx-border-color: none;");
		}

		/**
		 * Checks if the selected report is valid
		 * 
		 * @return true if a report has been selected, false otherwise
		 */
		@Override
		public boolean isSelectedReportValid() {
			return selectedReport != "";
		}
	}
}
