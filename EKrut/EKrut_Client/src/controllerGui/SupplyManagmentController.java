package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.TaskType;

import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.UserEntity;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class SupplyManagmentController {

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
	private ComboBox<MachineEntity> machineCmb;

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
	private TableView<ItemInMachineEntity> supplyMangmentTbl;

	@FXML
	private Label titleLbl;

	@FXML
	private ComboBox<UserEntity> workerCmb;

	private static ClientController chat = HostClientController.chat; // define the chat for th
	public static ObservableList<ItemInMachineEntity> itemsInMachineLst = FXCollections.observableArrayList();
	public static ObservableList<MachineEntity> machineLst = FXCollections.observableArrayList();
	private MachineEntity machine;
	private ArrayList<ItemInMachineEntity> toUpdate = new ArrayList<>();;
	ArrayList<TableCell<ItemInMachineEntity, Boolean>> checkboxCellsList = new ArrayList<>();

	/** Setup screen before launching view */
	@FXML
	public void initialize() throws Exception {
		getMachinesInRegion(CommonData.getMachines());
		machineCmb.setItems(machineLst);
		machineCmb.addEventHandler(ComboBox.ON_HIDDEN, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				machine = machineCmb.getValue();
				if (!machine.equals(null)) {
					saveChangesBtn.setDisable(false);
					machineNameLbl.setText(machine.machineName);
					machineNameLbl.setVisible(true);
					minAmountTxtField.setText(machine.getMinamount() + "");

				}
				setupTable(machine.machineId);
			}
		});

	}

	/**
	 * press refresh button to refresh table and item displayed ask from data base
	 * to load updated table
	 */
	@FXML
	void refresh(ActionEvent event) {
		setupTable(machine.machineId);
	}

	/** save changes that was made in the item */
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
			chat.acceptObj(new Message(TaskType.RequestUpdateMachineMinAmount, machine));
			System.out.println("Pop up - the new minimum amount has been updated \n"
					+ "if you want to see an updated list press the refresh button");
		}

	}

	/** send a task to the workers for update the requested items */
	@FXML
	void send(ActionEvent event) {
		if (toUpdate.isEmpty()) // TODO why is empty
			System.out.println("Popup - no new items to open calls for");
		else {
			for (ItemInMachineEntity i : toUpdate) {
				i.setCallStatus(ItemInMachineEntity.Call_Status.Processed);
				System.out.println(i.toString());
			}
			chat.acceptObj(new Message(TaskType.RequestItemsInMachineUpdateFromServer, toUpdate));
		}
		toUpdate.clear();

	}

	/**
	 * get from DB the data for setting the table, and puttin a preview ('eye')
	 * button on the preview col
	 */
	@SuppressWarnings("unchecked")
	void setupTable(int machineId) {
		chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));
		supplyMangmentTbl.setItems(itemsInMachineLst);
		supplyMangmentTbl.setEditable(true);
		// factory
		itemIdCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("itemId"));
		itemNameCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemInMachineEntity, String>("name"));
		currentAmountCol.setCellValueFactory(
				(Callback) new PropertyValueFactory<ItemInMachineEntity, Integer>("currentAmount"));
		callStatusCol.setCellValueFactory(
				(Callback) new PropertyValueFactory<ItemInMachineEntity, ItemInMachineEntity.Call_Status>(
						"callStatus"));
		refillcol.setCellFactory(column -> {
			TableCell<ItemInMachineEntity, Boolean> cell = new CheckBoxTableCell<>();
			checkboxCellsList.add(cell); // save the checkbox
			cell.setOnMouseClicked(event -> {
				if (event.getClickCount() > 0) {
					CheckBox checkBox = (CheckBox) cell.getGraphic();
					ItemInMachineEntity item = (ItemInMachineEntity) cell.getTableRow().getItem();
					if (checkBox != null) {
						if (item.getCallStatus() == ItemInMachineEntity.Call_Status.Processed) {
							cell.setDisable(true);
							checkBox.setVisible(false);
						}
						if (checkBox.isSelected()) {
							checkBox.setSelected(true);
							toUpdate.add(item);
						} else {
							checkBox.setSelected(false);
							toUpdate.remove(item);

						}
					}
				}
			});
			return cell ;
		});
		colorTableRows();
		return;

	}

	/**
	 * get machines and put them in a combo box
	 * 
	 * @param arrayList
	 */
	public void getMachinesInRegion(ArrayList<MachineEntity> arrayList) {
		String region = NavigationStoreController.connectedUser.getRegion();
		for (MachineEntity m : arrayList) {
			if (region.equals(m.regionName))
				machineLst.add(m);
		}
	}

	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		// TODO Auto-generated method stub
		if (!itemsInMachineLst.isEmpty()) {
			itemsInMachineLst.clear();
		}
		itemsInMachineLst.addAll(obj);
	}

	/** color tables rows by the cuurent amount and the status */
	private void colorTableRows() {
		supplyMangmentTbl.setRowFactory(tv -> new TableRow<ItemInMachineEntity>() {
			@Override
			protected void updateItem(ItemInMachineEntity item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null)
					setStyle("");
				else if (item.getCurrentAmount() < machine.getMinamount() && item.isCallOpen() == false)
					setStyle("-fx-background-color: #fa8989;");
				else if (item.getCallStatus().getName().equals("Completed"))
					setStyle("-fx-background-color: #89fa9e;");// TODO to add completed on the quary
				else if (item.getCurrentAmount() < machine.getMinamount() && item.isCallOpen() == true)
					setStyle("-fx-background-color: #faeb89;");
				else
					setStyle("");
			}
		});
	}

}
/*supplyMangmentTbl.getSelectionModel().selectedItemProperty()
.addListener((obs, oldSelection, newSelection) -> {
	if (newSelection != null) {
		CheckBox checkBox = (CheckBox) cell.getGraphic();
		ItemInMachineEntity item = (ItemInMachineEntity) cell.getTableRow().getItem();
		if (checkBox != null) {
			if (item.getCallStatus() == ItemInMachineEntity.Call_Status.Processed) {
				cell.setDisable(true);
				checkBox.setVisible(false);
			}
			if (checkBox.isSelected()) {
				checkBox.setSelected(true);
				toUpdate.add(item);
			} else {
				checkBox.setSelected(false);
				toUpdate.remove(item);

			}
		}
	}
});*/
