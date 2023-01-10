package controllerGui;

import java.util.ArrayList;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.DeliveryStatusEnum;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNamesEnum;
import common.TaskType;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class SupplyUpdateController {

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> currentAmountCol;

	@FXML
	private GridPane itemDisplayGridPane;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> itemIdCol;

	@FXML
	private TableColumn<ItemInMachineEntity, String> itemNameCol;

	@FXML
	private ComboBox<MachineEntity> machineCmb;

	@FXML
	private TableColumn<ItemInMachineEntity, Integer> machineIdCol;

	@FXML
	private Label machineNameLbl;

	@FXML
	private Label minamountLbl;

	@FXML
	private Button refreshBtn;

	@FXML
	private Label regionNameLbl;

	@FXML
	private Label machineIdLbl;

	@FXML
	private TableView<ItemInMachineEntity> supplyMangmentTbl;

	@FXML
	private Label titleLbl;

	@FXML
	private Button updatedBtn;

	private static MachineEntity machine;

	public static ObservableList<ItemInMachineEntity> itemsInMachineLst = FXCollections.observableArrayList();
	public static ObservableList<MachineEntity> machineLst = FXCollections.observableArrayList();
	private static ClientController chat = HostClientController.chat; // define the chat for th
	private static ArrayList<ItemInMachineEntity> toUpdate = new ArrayList<>();;
	private int[] arr = new int[2];
	private String[] arrStr = new String[2];
	public static boolean recievedData = false;

	/** Setup screen before launching view */
	@FXML
	public void initialize() throws Exception {
		arrStr[0] = "1";
		arrStr[1] = NavigationStoreController.connectedUser.getId() + "";
		recievedData = false;
		chat.acceptObj(new Message(TaskType.InitMachinesSupplyUpdate, arrStr));
		while (!recievedData)
			Thread.sleep(100);
		setUpMachineComboBox();
		

	}

	public void setUpMachineComboBox() {
		machineCmb.setItems(machineLst);
		if (machineLst.isEmpty()) {
			setDisableItems();
			CommonFunctions.createPopup(PopupTypeEnum.Warning, "No new calls for items");
		}
		machineCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				machine = machineCmb.getValue();
				if (machine.equals(null))
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a machine");
				else if (oldValue != newValue) {
					regionNameLbl.setText(machine.getRegionName());
					machineNameLbl.setText(machine.machineName);
					machineIdLbl.setText(machine.machineId + "");
					machineNameLbl.setVisible(true);
					minamountLbl.setText(machine.getMinamount() + "");
					try {
						setupTable(machine.machineId);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
	}

	void setDisableItems() {
		machineCmb.setDisable(true);
		updatedBtn.setDisable(true);
	}

	/***
	 * table setup
	 * 
	 * @param machineId
	 * @throws InterruptedException 
	 */
	private void setupTable(int machineId) throws InterruptedException {
		arr[0] = machineId;
		arr[1] = NavigationStoreController.connectedUser.getId();
		recievedData = false;
		chat.acceptObj(new Message(TaskType.RequestProssecedItemsInMachine, arr));
		while (!recievedData) 
			Thread.sleep(100);
		supplyMangmentTbl.setItems(itemsInMachineLst);
		supplyMangmentTbl.setEditable(true);
		// factory
		setFactoryCols();
	}

	/***
	 * set column for table
	 */
	@SuppressWarnings("unchecked")
	private void setFactoryCols() {
		machineIdCol
				.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("machineId"));
		itemIdCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("itemId"));
		itemNameCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, String>("name"));
		currentAmountCol.setCellValueFactory(
				(Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("currentAmount"));
		currentAmountCol.setEditable(true);
		currentAmountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		currentAmountCol.setOnEditCommit(new EventHandler<CellEditEvent<ItemInMachineEntity, Integer>>() {
			@Override
			public void handle(CellEditEvent<ItemInMachineEntity, Integer> event) {
				ItemInMachineEntity item = event.getRowValue();
				if (event.getNewValue() != null) {
					if (event.getNewValue() < event.getOldValue()) {
						CommonFunctions.createPopup(PopupTypeEnum.Error,
								"You can't decrease the amount of current items");
					}

					else {
						item.setCurrentAmount(event.getNewValue());
						if (event.getNewValue() >= machine.getMinamount()) {
							item.setCallStatus(ItemInMachineEntity.Call_Status.Complete);
						} else {
							CommonFunctions.createPopup(PopupTypeEnum.Warning,
									"The item is still under the minimum so the call still open\nPlease fill more if you have");
						}
						toUpdate.add(item);
					}
					supplyMangmentTbl.refresh();
				}
			}
		});
	}

	// ---------------------------------- set buttons event
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
				NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.SupplyUpdate);
				CommonFunctions.SleepFor(300, () -> {
					SupplyUpdateController sc = (SupplyUpdateController) NavigationStoreController.getInstance()
							.getController();
					sc.machineCmb.getSelectionModel().select(tempMachine);
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	void update(ActionEvent event) {
		for (ItemInMachineEntity i : toUpdate) {
			if (i.getCurrentAmount() > machine.getMinamount())
				i.setCallStatus(ItemInMachineEntity.Call_Status.Complete);
		}
		chat.acceptObj(new Message(TaskType.RequestItemsInMachineUpdateFromServer, toUpdate));
		CommonFunctions.createPopup(PopupTypeEnum.Success, "Update success!");
		toUpdate.clear();
		refresh(null);
	}

	// ----------------------------------------------------------------- get data
	/**
	 * get machines and put them in a combo box
	 * 
	 * @param arrayList
	 */
	public static void getAllMachines(ArrayList<MachineEntity> arrayList) {
		//Platform.runLater(() -> {
			if (!machineLst.isEmpty())
				machineLst.clear();
			machineLst.addAll(arrayList);
	//	});

		recievedData = true;
	}

	/***
	 * get items in machine from server in processed status
	 * 
	 * @param obj
	 */
	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		// TODO Auto-generated method stub
		if (!itemsInMachineLst.isEmpty()) {
			itemsInMachineLst.clear();
		}
		itemsInMachineLst.addAll(obj);
		recievedData = true;
	}

}
