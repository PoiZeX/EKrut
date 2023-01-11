package controllerGui;

import java.util.ArrayList;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNamesEnum;
import common.TaskType;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.UserEntity;
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

public class SupplyManagementController {
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

	private static ClientController chat = HostClientController.chat; // define the chat for th

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

	@FXML
	public void initialize() throws Exception {
		region = NavigationStoreController.connectedUser.getRegion();
		arrStr[0] = "0";
		arrStr[1] = region;
		recievedData = false;
		chat.acceptObj(new Message(TaskType.InitMachinesInRegions, arrStr));
		while (!recievedData)
			Thread.sleep(100);
		setUpMachineComboBox();
		chat.acceptObj(new Message(TaskType.RequestSupplyWorkers));
		while (!recievedData)
			Thread.sleep(100);
		setUpSupplyWorkersComboBox();
	}

//----------------------------------------------------------------------------   Combobox setup
	/***
	 * Insert machines list to Combo box handles the choos
	 */
	public void setUpMachineComboBox() {
		machineCmb.setItems(machineLst);
		machineCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				machine = machineCmb.getValue();
				if (machine.equals(null))
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a machine");
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
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a Worker");
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
			errorMsg = " you can't leave me empty!";

		int oldMinAmount = machine.getMinamount();
		int newMinAmount = machine.getMinamount();
		try {
			newMinAmount = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			errorMsg = "you have to fill in numbers only";
		}

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
		removeCompleted(null);
		chat.acceptObj(new Message(TaskType.RequestUpdateMachineMinAmount, machine));
		CommonFunctions.createPopup(PopupTypeEnum.Success, "The new minimum amount has been updated sucssesfully \n"
				+ "If you want to see an updated the items list press the refresh button");
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
		chat.acceptObj(new Message(TaskType.RequestItemsInMachineUpdateFromServer, completed));
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
			CommonFunctions.createPopup(PopupTypeEnum.Information, "No new items to open calls for");

		else {
			for (ItemInMachineEntity i : toUpdate) {
				i.setCallStatus(ItemInMachineEntity.Call_Status.Processed);
				i.setWorkerId(supplyworker.getId());
			}
			chat.acceptObj(new Message(TaskType.RequestItemsInMachineUpdateFromServer, toUpdate));

			CommonFunctions.createPopup(PopupTypeEnum.Success, "The calls had been sent for now");
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
	void setupTable(int machineId) {
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

	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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

	private class BooleanCheckBox extends TableCell<ItemInMachineEntity, Boolean> {
		private CheckBox checkBox;

		public BooleanCheckBox() {
			checkBox = new CheckBox();
			checkBox.setOnAction((evt) -> {
				if (checkBox.isSelected())
					toUpdate.add(getTableView().getItems().get(getIndex()));
				else
					toUpdate.remove(getTableView().getItems().get(getIndex()));
				System.out.println(toUpdate);
			});
			checkBox.setId("myCb");
			this.setGraphic(checkBox);
			this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			this.setEditable(true);
		}

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
//refillcol.setCellFactory(column -> {
//TableCell<ItemInMachineEntity, Boolean> cell = new CheckBoxTableCell<>();
//
//return new CheckBoxTableCell<ItemInMachineEntity, Boolean>() {
//
//
//
//};
//});
//@Override
//public void updateItem(Boolean item, boolean empty) {
//super.updateItem(item, empty);
//TableRow<ItemInMachineEntity> currentRow = getTableRow();
//ItemInMachineEntity currentItem = currentRow.getItem();
//
//CheckBox checkBox = (CheckBox) this.getGraphic();
//
//if (currentRow != null && !empty ) {
//	
//	if(this.selectedProperty().get())
//		toUpdate.add(currentItem);
//	if(!this.selectedProperty().get())
//		toUpdate.add(currentItem);
//}
//
//}		
//
//refillcol.setCellFactory(column -> {
//TableCell<ItemInMachineEntity, Boolean> cell = new CheckBoxTableCell<>();
//cell.setOnMouseClicked(event -> {
//	if (event.getClickCount() > 0) {
//		CheckBox checkBox = (CheckBox) cell.getGraphic();
//		ItemInMachineEntity item = (ItemInMachineEntity) cell.getTableRow().getItem();
//		if (checkBox != null) {
//			if (checkBox.isSelected()) {
//				checkBox.setSelected(false);
//				toUpdate.remove(item);
//		
//
//			} else {
//				checkBox.setSelected(true);
//				toUpdate.add(item);
//			}
//		}
//	}
//});
//return cell;
//});
//return;
//
//refillcol.setCellFactory(column -> {
//return new CheckBoxTableCell<ItemInMachineEntity, Boolean>() {
//    @Override
//    public void updateItem(Boolean item, boolean empty) {
//        super.updateItem(item, empty);
//        if (item == null || empty ) {
//            setVisible(false);
//        } else {
//        	ItemInMachineEntity obj = getTableView().getItems().get(getIndex());
//        	if(obj.isCallOpen())
//        		setVisible(false);
//        	else {
//        		setVisible(true);
//        	
//        	}
//        }
//    }
//
//
//    
//};
//
//});
