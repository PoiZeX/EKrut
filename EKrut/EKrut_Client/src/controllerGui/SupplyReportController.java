package controllerGui;

import java.util.ArrayList;
import java.util.Comparator;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import entity.MachineEntity;
import entity.SupplyReportEntity;
import enums.PopupTypeEnum;
import enums.ScreensNamesEnum;
import enums.TaskType;
import interfaces.IScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import utils.PopupSetter;
import utils.TooltipSetter;

/**
 * Supply report GUI controller, implements Screen interface Getting the report
 * and showing it to user
 * 
 * @author Lidor
 *
 */
public class SupplyReportController implements IScreen {

	@FXML
	private BarChart<String, Integer> supplySBC;

	@FXML
	private CategoryAxis xAxisSBC;

	@FXML
	private NumberAxis yAxisSBC;

	@FXML
	private Button prevPageBtn;

	@FXML
	private Button nextPageBtn;

	@FXML
	private Label reportDetailsLabel;

	@FXML
	private Button helpBtn;

	@FXML
	private Button fullViewBtn;

	@FXML
	private Button splitViewBtn;

	@FXML
	private ComboBox<String> machineIdComboBox;

	@FXML
	private PieChart pieChart;

	@FXML
	private Label textConclusionsLbl;

	@FXML
	private Label minAmountLbl;

	protected static ClientController chat;
	protected static SupplyReportEntity reportDetails;
	private SupplyReportEntity currentReport;
	protected static boolean RecievedData = false;
	protected static Object answerFromServer;
	private static ArrayList<MachineEntity> allMachines;
	private static String reportYear, reportMonth, reportRegion;
	private String machineName;
	private int machineID;
	private ArrayList<String[]> itemsArray = new ArrayList<>();
	private ArrayList<String> itemsNames = new ArrayList<>(), startAmount = new ArrayList<>();
	private int start = 0, end = 5;

	public SupplyReportController(ClientController chatService) {
		chat = chatService;
	}

	public SupplyReportController() {
		chat = HostClientController.getChat();
	}

	/**
	 * 
	 * The initialize method is used to set up the machine selection combo box and
	 * associated functionality. It sets the supplySBC to not animated,
	 * textConclusionsLbl to not visible, and loads all machines from the datastore.
	 * It populates the machine selection combo box with machines that match the
	 * reportRegion of the current user and sets up a listener to update data when a
	 * new machine is selected. It also sets the prevPageBtn and nextPageBtn to not
	 * visible.
	 */
	@Override
	public void initialize() {

		supplySBC.setAnimated(false);
		textConclusionsLbl.setVisible(false);
		minAmountLbl.setVisible(false);
		reportDetailsLabel.setText(
				String.format("%s - %s/%s", reportRegion, CommonFunctions.getNumericMonth(reportMonth), reportYear));
		allMachines = DataStore.getMachines();
		ObservableList<String> machines = FXCollections.observableArrayList();
		for (MachineEntity machine : allMachines) {
			if (NavigationStoreController.connectedUser != null)
				if (machine.getRegionName().equals(reportRegion)) {
					machines.add(machine.getMachineName());
				}
		}
		machineIdComboBox.setItems(machines);
		machineIdComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			clearAllData();
			machineName = machineIdComboBox.getValue();
			for (MachineEntity machine : allMachines) {
				if (machine.getMachineName().equals(machineName))
					machineID = machine.getId();
			}
			initDetails(machineID);
			setupFullBarChart();

		});
		prevPageBtn.setVisible(false);
		nextPageBtn.setVisible(false);
	}

	/**
	 * 
	 * The setReport method is used to set the report year, month and region for the
	 * current report.
	 * 
	 * @param year   the year of the report
	 * @param month  the month of the report
	 * @param region the region of the report
	 */
	public static void setReport(String year, String month, String region) {
		reportYear = year;
		reportMonth = month;
		reportRegion = region;
		return;
	}

	/**
	 * 
	 * This method receives data from a server and updates the reportDetails and
	 * RecievedData fields.
	 * 
	 * @param report The SupplyReportEntity object containing the report data
	 *               received from the server.
	 */
	public static void recieveDataFromServer(SupplyReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}

	/**
	 * 
	 * This method receives an answer from a server and updates the answerFromServer
	 * field and RecievedData fields.
	 * 
	 * @param obj The object containing the answer received from the server.
	 */
	public static void getAnswerFromServer(Object obj) {
		answerFromServer = obj;
		RecievedData = true;
		return;
	}

	/**
	 * Initialize details of table
	 * 
	 * @param machineID
	 */
	private void initDetails(int machineID) {
		ObservableList<PieChart.Data> list = FXCollections.observableArrayList();

		if (pieChart != null)
			pieChart.getData().clear();
		if (textConclusionsLbl != null)
			textConclusionsLbl.setText("");

		currentReport = getSupplyReportFromDB(machineID);

		if (currentReport.getReportsList() == null) {
			PopupSetter.createPopup(PopupTypeEnum.Error, "No Report Found!");
			clearAllData();
			return;
		}

		// prepare
		itemsArray = currentReport.getReportsList();
		itemsNames = getItemsNamesByID(itemsArray);
		startAmount = intersectItems(machineID);
		if (startAmount.size() != itemsNames.size()) {
			PopupSetter.createPopup(PopupTypeEnum.Warning,
					"Oops... The items start amount for this month may be different from previous month.\n"
							+ "It might happen when new items are added during the month, or a previous report doesn't exist \n"
							+ "Continue with '0' on start amount for those items");
		}
		// initialize piechart
		int i = 0;
		for (String[] item : itemsArray) {
			PieChart.Data dataToInsert = new PieChart.Data(itemsNames.get(i), Integer.parseInt(item[2]));
			list.add(dataToInsert);

			// setup tool tip
			Node node = dataToInsert.getNode();
			Tooltip.install(node, new TooltipSetter(itemsNames.get(i)).getTooltip());
			i++;
		}
		pieChart.setLegendSide(Side.BOTTOM);

		// set pieChart
		pieChart.setData(list);

		// set textual conclusions
		String itemsAmount = "";
		list = list.sorted(new Comparator<PieChart.Data>() {
			@Override
			public int compare(PieChart.Data o1, PieChart.Data o2) {
				return ((Double) o1.getPieValue()).compareTo(o2.getPieValue());
			}
		});

		itemsAmount += list.size() >= 1
				? list.get(list.size() - 1).getName() + " : " + list.get(list.size() - 1).getPieValue() + "\n"
				: "";
		itemsAmount += list.size() >= 2
				? list.get(list.size() - 2).getName() + " : " + list.get(list.size() - 2).getPieValue() + "\n"
				: "";
		itemsAmount += list.size() >= 3
				? list.get(list.size() - 3).getName() + " : " + list.get(list.size() - 3).getPieValue() + "\n"
				: "";
		String conclusions = "";

		conclusions = "Top 3 items that were filled \nthe most this month\n\n" + itemsAmount + "\n";
		conclusions += "The product consumption was\n";
		conclusions += list.get(list.size() - 1).getPieValue() > 10 ? "high"
				: list.get(list.size() - 1).getPieValue() >= 5 ? "medium" : "low";
		textConclusionsLbl.setText(conclusions);
		textConclusionsLbl.setVisible(true);
		minAmountLbl.setText("Min Amount: " + String.valueOf(currentReport.getMin_stock()));
		minAmountLbl.setVisible(true);
	}

	public SupplyReportEntity getSupplyReportFromDB(int machineID) {
		RecievedData = false; // reset each operation
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
		return reportDetails;
	}

	/**
	 * Handle sending list of itemsID and getting their names
	 * 
	 * @param itemsID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getItemsNamesByID(ArrayList<String[]> itemsID) {
		// change itemsID from array of string to list of int
		ArrayList<Integer> listToSend = new ArrayList<>();
		for (String[] item : itemsID)
			listToSend.add(Integer.parseInt(item[0]));

		RecievedData = false;
		chat.acceptObj(new Message(TaskType.RequestAllItemsNameById, listToSend));
		return (ArrayList<String>) answerFromServer;
	}

	/**
	 * match between previous report and current
	 * 
	 * @param machineID
	 * @return
	 */
	private ArrayList<String> intersectItems(int machineID) {
		ArrayList<String> startAmounts = new ArrayList<>();
		ArrayList<String[]> itemsList = currentReport.getReportsList();

		boolean isFound = false, isProblem = false;
		// prepare
		SupplyReportEntity prevSupplyReport = getPrevSupplyReportForMachine(machineID);
		if (prevSupplyReport.getReportsList() == null) {
			for (int i = 0; i < itemsList.size() + 1; i++)
				startAmounts.add("0");
			return startAmounts;
		}
		ArrayList<String[]> prevItemsList = prevSupplyReport.getReportsList();
		for (String[] currentItem : itemsList) // iterate over the current items_id
		{
			isFound = false;
			// search this itemID in the previous report
			for (String[] prevItem : prevItemsList) {
				if (currentItem[0].equals(prevItem[0])) {
					// match! now get the end amount of this id and add to arraylist
					startAmounts.add(prevItem[1]);
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				// got here just if no corresponding item found
				startAmounts.add("0");
				isProblem = true;
			}

		}
		if (isProblem)
			startAmounts.add("0"); // mark there was a problem (another 0 wont hurt someone :))

		return startAmounts;

	}

	/**
	 * return the previous report
	 * 
	 * @param machineID
	 * @return
	 */
	private SupplyReportEntity getPrevSupplyReportForMachine(int machineID) {
		// get supply report of last month for this machine
		String month = reportMonth;
		String year = reportYear;
		if (month.equals("01")) {
			month = "12";
			year = String.valueOf(Integer.parseInt(year) - 1);
		} else {
			month = String.valueOf(Integer.parseInt(CommonFunctions.getNumericMonth(month)) - 1);
		}
		RecievedData = false; // reset each operation
		// sends the user information to server
		chat.acceptObj(new Message(TaskType.RequestReport,
				new String[] { "supply", reportRegion, month, year, String.valueOf(machineID) }));
		// wait for answer
		while (RecievedData == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return reportDetails;

	}

	/**
	 * The main char chart initialization
	 */
	@SuppressWarnings("unchecked")
	private void setupFullBarChart() {
		if (itemsArray == null)
			return;
		supplySBC.getData().clear();
		XYChart.Series<String, Integer> monthStart = new XYChart.Series<>();
		XYChart.Series<String, Integer> monthEnd = new XYChart.Series<>();
		int i = 0;
		for (String[] item : itemsArray) {
			monthStart.getData()
					.add(new XYChart.Data<String, Integer>(itemsNames.get(i), Integer.parseInt(startAmount.get(i))));
			monthEnd.getData().add(new XYChart.Data<String, Integer>(itemsNames.get(i), Integer.parseInt(item[1])));
			i++;
		}
		monthStart.setName("Month Start Amount");
		monthEnd.setName("Month End Amount");
		supplySBC.getData().addAll(monthStart, monthEnd);
	}

	/**
	 * Zoom-in BarChart setup
	 * 
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	private void setupBarChart(int start, int end) {
		supplySBC.getData().clear();
		XYChart.Series<String, Integer> monthStart = new XYChart.Series<>();
		XYChart.Series<String, Integer> monthEnd = new XYChart.Series<>();
		for (int i = start; i < end; i++) {
			String[] item = itemsArray.get(i);
			monthStart.getData()
					.add(new XYChart.Data<String, Integer>(itemsNames.get(i), Integer.parseInt(startAmount.get(i))));
			monthEnd.getData().add(new XYChart.Data<String, Integer>(itemsNames.get(i), Integer.parseInt(item[1])));
		}
		monthStart.setName("Month Start Amount");
		monthEnd.setName("Month End Amount");
		supplySBC.getData().addAll(monthStart, monthEnd);
	}

	/**
	 * 
	 * This method is the event handler for the "next page" button. It updates the
	 * indices for the portion of the itemsArray to be displayed in the bar chart,
	 * and calls the setupBarChart method to update the chart.
	 * 
	 * @param event The ActionEvent object generated when the button is clicked.
	 */
	@FXML
	void nextPageView(ActionEvent event) {
		if (end + 5 > itemsArray.size()) {
			end = itemsArray.size();
			start = end - 5;
		} else {
			start += 5;
			end += 5;
		}
		setupBarChart(start, end);
	}

	/**
	 * 
	 * This method is the event handler for the "previous page" button. It updates
	 * the indices for the portion of the itemsArray to be displayed in the bar
	 * chart, and calls the setupBarChart method to update the chart.
	 * 
	 * @param event The ActionEvent object generated when the button is clicked.
	 */
	@FXML
	void prevPageView(ActionEvent event) {
		if (start - 5 < 0) {
			start = 0;
			end = 5;
		} else {
			start -= 5;
			end -= 5;
		}

		setupBarChart(start, end);
	}

	/**
	 * Full view button pressed
	 * 
	 * @param event
	 */
	@FXML
	void barChartFullView(ActionEvent event) {
		if (itemsArray == null || itemsArray.size() == 0) {
			PopupSetter.createPopup(PopupTypeEnum.Error, "No report for this machine");
			return;
		}
		setupFullBarChart();
		prevPageBtn.setVisible(false);
		nextPageBtn.setVisible(false);

	}

	/**
	 * Split view button pressed
	 * 
	 * @param event
	 */
	@FXML
	void barChartSplitView(ActionEvent event) {
		if (itemsArray == null || itemsArray.size() == 0) {
			PopupSetter.createPopup(PopupTypeEnum.Error, "No report for this machine");
			return;
		}
		start = 0;
		end = 5;
		setupBarChart(start, end);
		prevPageBtn.setVisible(true);
		nextPageBtn.setVisible(true);
	}

	/**
	 * Show description for the page
	 * 
	 * @param event
	 */
	@FXML
	void showDescription(ActionEvent event) {
		PopupSetter.createPopup(PopupTypeEnum.Information, ScreensNamesEnum.SupplyReport.getDescription());
	}

	/**
	 * Clear all data from previous reports upon combobox changes
	 */
	private void clearAllData() {
		if (itemsArray != null)
			itemsArray.clear();
		if (itemsNames != null)
			itemsNames.clear();
		if (startAmount != null)
			startAmount.clear();
		prevPageBtn.setVisible(false);
		nextPageBtn.setVisible(false);
		minAmountLbl.setVisible(false);
	}
}
