package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.DeliveryStatus;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
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
	private TableView<ItemInMachineEntity> supplyMangmentTbl;

	@FXML
	private Label titleLbl;

	@FXML
	private Button updatedBtn;

	private MachineEntity machine;

	public static ObservableList<ItemInMachineEntity> itemsInMachineLst = FXCollections.observableArrayList();
	public static ObservableList<MachineEntity> machineLst = FXCollections.observableArrayList();
	private static ClientController chat = HostClientController.chat; // define the chat for th
	private ArrayList<ItemInMachineEntity> toUpdate = new ArrayList<>();;
	private int [] arr = new int[2];
	/** Setup screen before launching view */
	@FXML
	public void initialize() throws Exception {
		getAllMachines(CommonData.getMachines());
		machineCmb.setItems(machineLst);
		machineCmb.addEventHandler(ComboBox.ON_HIDDEN, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				machine = machineCmb.getValue();
				if (!machine.equals(null)) {
					regionNameLbl.setText(machine.getRegionName());

					machineNameLbl.setText(machine.machineName);
					machineNameLbl.setVisible(true);
					minamountLbl.setText(machine.getMinamount() + "");

				}
				setupTable(machine.machineId);
			}
		});

	}

	@SuppressWarnings("unchecked")
	private void setupTable(int machineId) {
		arr[0]=machineId;
		arr[1]= NavigationStoreController.connectedUser.getId();
		chat.acceptObj(new Message(TaskType.RequestProssecedItemsInMachine, arr));
		supplyMangmentTbl.setItems(itemsInMachineLst);
		supplyMangmentTbl.setEditable(true);
		// factory
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
				System.out.println("ho");
				ItemInMachineEntity item = event.getRowValue();
				if (event.getNewValue() != null) {
					if (event.getNewValue() < event.getOldValue()) {
						System.out.println("Pop up -you can dicrease the amount of current items ");
					} 
					
					else {
						item.setCurrentAmount(event.getNewValue());
						if (event.getNewValue() >= machine.getMinamount()) {
							item.setCallStatus(ItemInMachineEntity.Call_Status.Complete);
						} else {
							System.out.println("Pop up -the call will still be opend , you can fill more if you have ");
						}
						toUpdate.add(item);
					}
					supplyMangmentTbl.refresh();
				}
			}
		});

	}

	/**
	 * get machines and put them in a combo box
	 * 
	 * @param arrayList
	 */
	public void getAllMachines(ArrayList<MachineEntity> arrayList) {
		machineLst.addAll(arrayList);
	}

	@FXML
	void refresh(ActionEvent event) {
		setupTable(machine.machineId);
	}

	@FXML
	void update(ActionEvent event) {
		for (ItemInMachineEntity i : toUpdate) {
			if (i.getCurrentAmount() > machine.getMinamount())
				i.setCallStatus(ItemInMachineEntity.Call_Status.Complete);
		}
		chat.acceptObj(new Message(TaskType.RequestItemsInMachineUpdateFromServer, toUpdate));
		toUpdate.clear();
	}

	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		// TODO Auto-generated method stub
		if (!itemsInMachineLst.isEmpty()) {
			itemsInMachineLst.clear();
		}
		itemsInMachineLst.addAll(obj);
	}

}
