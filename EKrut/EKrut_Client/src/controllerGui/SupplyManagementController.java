package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.UserEntity;
import enums.PopupTypeEnum;
import enums.ScreensNamesEnum;
import enums.TaskType;
import interfaces.ICmbANDTableSetUp;
import interfaces.IScreen;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import utils.PopupSetter;

/**
 * Supply Management GUI controller, implements Screen interface and ICmbANDTableSetUp
 * Getting and changing supply information. Can open calls for re-supply
 * @author Lidor
 *
 */
public class SupplyManagementController implements ICmbANDTableSetUp, IScreen {
	@FXML
	private TableColumn<ItemInMachineEntity, ItemInMachineEntity.Call_Status> callStatusCol1;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> currentAmountCol1;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> itemIdCol1;

	@FXML
	private TableColumn<ItemInMachineEntity, String> itemNameCol1;

	@FXML
	private TableView<ItemInMachineEntity> opencallsTbl;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> workerCol1;

	@FXML
	private TableColumn<ItemInMachineEntity, ItemInMachineEntity.Call_Status> callStatusCol;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> currentAmountCol;

	@FXML
	private Label errorInputLbl;

	@FXML
	private GridPane itemDisplayGridPane;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> itemIdCol;

	@FXML
	private ImageView itemImg;

	@FXML
	private TableColumn<ItemInMachineEntity, String> itemNameCol;

	@FXML
	ComboBox<MachineEntity> machineCmb;

	@FXML
	private Label machineNameLbl;

	@FXML
	private TextField minAmountTxtField;

	@FXML
	private TableColumn<ItemInMachineEntity, Boolean> refillcol;

	@FXML
	private Button refreshBtn;

	@FXML
	private Button saveChangesBtn;

	@FXML
	private Button sendCallBtn;

	@FXML
	private Button removeCompletedBtn;

	@FXML
	private TableView<ItemInMachineEntity> supplyMangmentTbl;

	@FXML
	private Label titleLbl;

	@FXML
	private ComboBox<UserEntity> workerCmb;

	private static ClientController chat = HostClientController.getChat(); // define the chat for th

	public static ObservableList<MachineEntity> machineLst = FXCollections.observableArrayList();
	public static ObservableList<UserEntity> supplyWorkers = FXCollections.observableArrayList();
	public static ObservableList<ItemInMachineEntity> openedcalls = FXCollections.observableArrayList();
	public static ObservableList<ItemInMachineEntity> notOpendCalls = FXCollections.observableArrayList();
	private static ArrayList<ItemInMachineEntity> toUpdate = new ArrayList<>();
	private static ArrayList<ItemInMachineEntity> completed = new ArrayList<>();
	ArrayList<TableCell<ItemInMachineEntity, Boolean>> checkboxCellsList;
	private String[] arrStr = new String[2];

	private MachineEntity machine;
	private UserEntity supplyworker;
	private static String region;
	public static boolean recievedData = false;
	/**

	This method initializes the GUI by setting the region to that of the connected user, initializing the array arrStr, and setting the recievedData flag to false.
	It then sends a message to the chat object with a request for a list of machines in the user's region, and sets up the combo box with the data received.
	It also sends a message to the chat object with a request for a list of supply workers and sets up the combo box with the data received.
	@throws Exception if an error occurs while initializing the GUI.
	*/
	@Override
	public void initialize() {
		try { 

			region = NavigationStoreController.connectedUser.getRegion();
			arrStr[0] = "0";
			arrStr[1] = region;
			recievedData = false;
			chat.acceptObj(new Message(TaskType.InitMachinesInRegions, arrStr));
			while (!recievedData)
				Thread.sleep(100);
			setUpComboBox();
			chat.acceptObj(new Message(TaskType.RequestSupplyWorkers));
			while (!recievedData)
				Thread.sleep(100);
			setUpSupplyWorkersComboBox();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//----------------------------------------------------------------------------   Combobox setup
	/***
	 * Insert machines list to Combo box handles the choos
	 */
	public void setUpComboBox() {
		machineCmb.setItems(machineLst);
		machineCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				machine = machineCmb.getValue();
				if (machine.equals(null))
					PopupSetter.createPopup(PopupTypeEnum.Warning, "You have to choose a machine");
				else if (oldValue != newValue) {
					saveChangesBtn.setDisable(false);
					machineNameLbl.setText(machine.machineName);
					machineNameLbl.setVisible(true);
					minAmountTxtField.setText(machine.getMinamount() + "");
					toUpdate = new ArrayList<ItemInMachineEntity>();
					setupTable(machine.machineId);
				}
			}

		});
		recievedData = true;
	}

	/***
	 * Insert Supply workers list to Combo box
	 */
	public void setUpSupplyWorkersComboBox() {
		workerCmb.setItems(supplyWorkers);
		sendCallBtn.setDisable(true);
		workerCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				supplyworker = workerCmb.getValue();
				if (!supplyworker.equals(null))
					sendCallBtn.setDisable(false);
				else
					PopupSetter.createPopup(PopupTypeEnum.Warning, "You have to choose a Worker");
			}
		});
	}

//----------------------------------------------------------------------------   buttons
	/***
	 * press refresh button to refresh table and item displayed ask from data base
	 * to load updated table
	 * 
	 * @param event
	 */
	@FXML
	void refresh(ActionEvent event) {
		MachineEntity tempMachine = machineCmb.getValue();
		Platform.runLater(() -> {
			try {
				NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.SupplyManagement);
				CommonFunctions.SleepFor(300, () -> {
					SupplyManagementController sc = (SupplyManagementController) NavigationStoreController.getInstance()
							.getController();
					sc.machineCmb.getSelectionModel().select(tempMachine);
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/***
	 * save changes that was made in the item
	 * 
	 * @param event
	 */
	@FXML
	void saveMachineMinAmount(ActionEvent event) {
		String errorMsg = "";
		String strNum = minAmountTxtField.getText();

		if (strNum.isEmpty())
			errorMsg = "You can't leave me empty!";

		int oldMinAmount = machine.getMinamount();
		int newMinAmount = machine.getMinamount();
		try {
			newMinAmount = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			errorMsg = "Numbers only";
			errorInputLbl.setText(errorMsg);
			return;
		}
		errorInputLbl.setText(errorMsg);
		filterUpdateCallStatus(oldMinAmount, newMinAmount);
		removeCompleted(null);
		chat.acceptObj(new Message(TaskType.RequestUpdateMachineMinAmount, machine));
		PopupSetter.createPopup(PopupTypeEnum.Success, "The new minimum amount has been updated sucssesfully \n"
				+ "If you want to see an updated the items list press the refresh button");
	}

	/***
	 * get the old minimum amount and the new one and classify the items by the
	 * suitable status call
	 * 
	 * @param oldMinAmount
	 * @param newMinAmount
	 */
	void filterUpdateCallStatus(int oldMinAmount, int newMinAmount) {
		if (oldMinAmount != newMinAmount) {
			machine.setMinamount(newMinAmount);
			for (MachineEntity m : machineLst) {
				if (m.machineId == machine.machineId) {
					m.setMinamount(newMinAmount);
				}
			}
			for (ItemInMachineEntity i : openedcalls) {
				if (i.getCurrentAmount() < machine.getMinamount()) {
					if (i.getCallStatus() == ItemInMachineEntity.Call_Status.Complete)
						completed.add(i);
				}
				if (i.getCurrentAmount() >= machine.getMinamount()) {
					if (i.getCallStatus() == ItemInMachineEntity.Call_Status.Processed) {
						completed.add(i);
					}
				}
			}
		}
	}

	/***
	 * remove the completed actions from the table and request update from server
	 * 
	 * @param event
	 */
	@FXML
	void removeCompleted(ActionEvent event) {
		for (ItemInMachineEntity i : completed) {
			i.setCallStatus(ItemInMachineEntity.Call_Status.NotOpened);
			i.setWorkerId(0);
		}
		chat.acceptObj(new Message(TaskType.RequestItemsInMachineCallStatusUpdate, completed));
		completed.clear();
		refresh(null);
	}

	/***
	 * send a task to the workers for update the requested items
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	@FXML
	void send(ActionEvent event) throws InterruptedException {

		if (toUpdate.isEmpty()) // TODO why is empty
			PopupSetter.createPopup(PopupTypeEnum.Information, "No new items to open calls for");

		else {
			for (ItemInMachineEntity i : toUpdate) {
				i.setCallStatus(ItemInMachineEntity.Call_Status.Processed);
				i.setWorkerId(supplyworker.getId());
			}
			chat.acceptObj(new Message(TaskType.RequestItemsInMachineCallStatusUpdate, toUpdate));

			PopupSetter.createPopup(PopupTypeEnum.Success, "The calls had been sent for now");
			toUpdate.clear();
			refresh(null);
		}

	}

//---------------------------------------------------------------------------  Tables
	/***
	 * set up table by machine id
	 * 
	 * @param machineId
	 */
	public void setupTable(int machineId) {
		recievedData = false;
		chat.acceptObj(new Message(TaskType.RequestItemsWithMinAmount, machineId));
		while (!recievedData) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		supplyMangmentTbl.setEditable(true);
		setFactoryColsForNotOpened();
		supplyMangmentTbl.setItems(notOpendCalls);
		colorTableRows(supplyMangmentTbl);

		opencallsTbl.setEditable(true);
		setFactoryColsForOpened();
		opencallsTbl.setItems(openedcalls);
		colorTableRows(opencallsTbl);
		return;

	}
	/**

	This method sets the cell value factories for the columns of the table displaying ItemInMachineEntity objects.
	@param idCol The TableColumn for the item ID.
	@param nameCol The TableColumn for the item name.
	@param currentAmountCol The TableColumn for the current amount of the item in the machine.
	@param stsCol The TableColumn for the call status of the item in the machine.
	*/
	private void setFactoryCols(TableColumn<ItemInMachineEntity, Integer> idCol,
			TableColumn<ItemInMachineEntity, String> nameCol,
			TableColumn<ItemInMachineEntity, Integer> currentAmountCol,
			TableColumn<ItemInMachineEntity, ItemInMachineEntity.Call_Status> stsCol) {

		idCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("itemId"));
		nameCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, String>("name"));
		currentAmountCol.setCellValueFactory(
				(Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("currentAmount"));
		stsCol.setCellValueFactory(
				(Callback) new PropertyValueFactory<ItemInMachineEntity, ItemInMachineEntity.Call_Status>(
						"callStatus"));
	}

	/***
	 * set columns for table for the opened calls table
	 */
	private void setFactoryColsForOpened() {
		setFactoryCols(itemIdCol1, itemNameCol1, currentAmountCol1, callStatusCol1);
		workerCol1.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("workerId"));
	}

	/***
	 * set columns for table the not opened calls table
	 */
	private void setFactoryColsForNotOpened() {
		// factory
		setFactoryCols(itemIdCol, itemNameCol, currentAmountCol, callStatusCol);
		Callback<TableColumn<ItemInMachineEntity, Boolean>, TableCell<ItemInMachineEntity, Boolean>> booleanCellFactory = new Callback<TableColumn<ItemInMachineEntity, Boolean>, TableCell<ItemInMachineEntity, Boolean>>() {
			@Override
			public TableCell<ItemInMachineEntity, Boolean> call(TableColumn<ItemInMachineEntity, Boolean> p) {
				return new BooleanCheckBox();
			}
		};
		refillcol.setCellFactory(booleanCellFactory);

	}

	/***
	 * color tables rows by the cuurent amount and the status
	 */
	private void colorTableRows(TableView<ItemInMachineEntity> table) {
		table.setRowFactory(tv -> new TableRow<ItemInMachineEntity>() {
			@Override
			protected void updateItem(ItemInMachineEntity item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null)
					setStyle("");
				else if (item.getCurrentAmount() < machine.getMinamount() && item.isCallOpen() == false)
					setStyle("-fx-background-color: #fa8989;");
				else if (item.getCallStatus() == ItemInMachineEntity.Call_Status.Complete)
					setStyle("-fx-background-color: #7cf28f;");// TODO to add completed on the quary
				else if (item.getCurrentAmount() < machine.getMinamount() && item.isCallOpen() == true)
					setStyle("-fx-background-color: #faeb89;");
				else
					setStyle("");
			}
		});
	}

//-------------------------------------------------------------------------------- Receive Data
	/**
	 * get machines and put them in a combo box
	 * 
	 * @param arrayList
	 */
	public static void getMachinesInRegion(ArrayList<MachineEntity> arrayList) {
		// String region = NavigationStoreController.connectedUser.getRegion();
		Platform.runLater(() -> {
			if (!machineLst.isEmpty())
				machineLst.clear();

			machineLst.addAll(arrayList);
		});
		recievedData = true;

	}

	/***
	 * recevie Items InMachine
	 * 
	 * @param obj
	 */
	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		if (!notOpendCalls.equals(null) && !openedcalls.equals(null)) {
			if (!notOpendCalls.isEmpty()) {
				notOpendCalls.clear();
			}

			if (!openedcalls.isEmpty()) {
				openedcalls.clear();
			}
			for (ItemInMachineEntity item : obj) {
				if (item.getCallStatus() == ItemInMachineEntity.Call_Status.NotOpened)
					notOpendCalls.add(item);

				if (item.getCallStatus() == ItemInMachineEntity.Call_Status.Complete) {
					openedcalls.add(item);
					completed.add(item);
				}
				if (item.getCallStatus() == ItemInMachineEntity.Call_Status.Processed)
					openedcalls.add(item);

			}
			recievedData = true;
		}
	}

	/***
	 * Receive supply workers
	 * 
	 * @param obj
	 */
	public static void recevieSupplyWorkers(ArrayList<UserEntity> obj) {
		Platform.runLater(() -> {
			if (!supplyWorkers.isEmpty()) {
				supplyWorkers.clear();
			}
			supplyWorkers.addAll(obj);
		});
		recievedData = true;

	}

	/***
	 * checkbox class that handles the checkbox field on the table cell
	 * 
	 * @author User
	 *
	 */
	private class BooleanCheckBox extends TableCell<ItemInMachineEntity, Boolean> {
		private CheckBox checkBox;

		public BooleanCheckBox() {
			checkBox = new CheckBox();
			checkBox.setOnAction((evt) -> {
				if (checkBox.isSelected())
					toUpdate.add(getTableView().getItems().get(getIndex()));
				else
					toUpdate.remove(getTableView().getItems().get(getIndex()));
			});
			checkBox.setId("myCb");
			this.setGraphic(checkBox);
			this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			this.setEditable(true);
		}
		/**

		This method updates the item in a table cell for a checkbox.
		It sets the checkbox's alignment to center, selects the checkbox if the item is true, sets the cell's alignment to center, and sets the cell's graphic to the checkbox.
		@param item the Boolean value of the checkbox, whether it is selected or not
		@param empty a boolean indicating if the cell is empty or not
		*/
		@Override
		public void updateItem(Boolean item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setGraphic(null);
			} else {
				if (item != null) {
					checkBox.setAlignment(Pos.CENTER);
					checkBox.setSelected(item);
				}
				setAlignment(Pos.CENTER);
				setGraphic(checkBox);
			}
		}
	}

}
