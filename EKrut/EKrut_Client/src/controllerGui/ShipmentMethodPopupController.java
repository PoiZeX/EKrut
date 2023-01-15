package controllerGui;

import java.util.ArrayList;

import Store.DataStore;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.PopupTypeEnum;
import common.ScreensNamesEnum;
import controller.OrderController;
import entity.MachineEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ShipmentMethodPopupController  implements IScreen {

	@FXML
	private GridPane gridPane;

	@FXML
	private ComboBox<String> shipmentMethodCmb;

	@FXML
	private ComboBox<MachineEntity> machineCmb;

	@FXML
	private Button cancelBtn;

	@FXML
	private Button confirmBtn;

	public static MachineEntity selectedMachine;
	private static String selectedShipmentMethod;
	private static ArrayList<MachineEntity> allMachines;
	public static ObservableList<String> shipmentMethod;

	public void initialize() {
		shipmentMethod = FXCollections.observableArrayList(new String[] { "Delivery", "Pickup" });
		ObservableList<MachineEntity> machines = FXCollections.observableArrayList();
		allMachines = DataStore.getMachines();
		shipmentMethodCmb.setItems(shipmentMethod);

		for (MachineEntity machine : allMachines) {
			if (NavigationStoreController.connectedUser != null)
				if (machine.getRegionName().equals(NavigationStoreController.connectedUser.getRegion())) {
					machines.add(machine);
				}
		}

		shipmentMethodCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				selectedShipmentMethod = shipmentMethodCmb.getValue();
				if (selectedShipmentMethod.equals(null))
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a method");
				else if (newValue.equals("Pickup")) {
					machineCmb.setVisible(true);
				} else
					machineCmb.setVisible(false);
			}
		});

		machineCmb.setItems(machines);
		machineCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				selectedMachine = machineCmb.getValue();
				if (selectedMachine.equals(null))
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a machine");
			}
		});
		selectedMachine = null;
		selectedShipmentMethod = null;
	}

	@FXML
	void cancelOrder(ActionEvent event) {
		selectedShipmentMethod = null;
		selectedMachine = null;
		((Stage) confirmBtn.getScene().getWindow()).close(); // close the popup window
	}

	@FXML
	void confirmMethod(ActionEvent event) {
		String prevMethod = "";
		if ((selectedShipmentMethod == null)) {
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Select Shipmend Method");
			return;
		}
		if ((selectedShipmentMethod.equals("Pickup") && selectedMachine == null)) {
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Select Pickup Machine");
			return;
		}
		if (OrderController.getCurrentOrder() == null) {
			createNewScreen();
			return;
		}
		prevMethod = OrderController.getCurrentOrder().getSupplyMethod();
		if (!selectedShipmentMethod.equals(prevMethod)) {
			createNewScreen();
			return;
		}
		if (prevMethod.equals("Pickup")
				&& OrderController.getCurrentOrder().getMachine_id() != selectedMachine.getMachineId()) {
			createNewScreen();
			return;
		} else {
			((Stage) confirmBtn.getScene().getWindow()).close();
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.ViewCatalog);
		}
	}

	private void createNewScreen() {
		OrderController.clearAll();
		switch (selectedShipmentMethod) {
		case "Pickup":
			OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "Pickup");
			OrderController.getCurrentOrder().setMachine_id(selectedMachine.machineId);
			OrderController.setCurrentMachine(selectedMachine);
			break;
		case "Delivery":
			OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "Delivery");
			OrderController.setCurrentMachine(null);
			break;
		}
		((Stage) gridPane.getScene().getWindow()).close();
		
		NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.ViewCatalog);
	}

}
