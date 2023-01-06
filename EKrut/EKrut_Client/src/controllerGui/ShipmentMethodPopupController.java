package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import common.CommonData;
import common.CommonFunctions;
import common.PopupTypeEnum;
import common.ScreensNames;
import controller.OrderController;
import entity.MachineEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ShipmentMethodPopupController {

	@FXML
	private ComboBox<String> shipmentMethodCmb;

	@FXML
	private ComboBox<MachineEntity> machineCmb;

	@FXML
	private Button cancelBtn;

	@FXML
	private Button confirmBtn;

	private static String selectedShipmentMethod;
	private static ArrayList<MachineEntity> allMachines;
	public static ObservableList<String> shipmentMethod;
	public static MachineEntity selectedMachine;
	String prev;

	private int prevMachine;

	public void initialize() {
		shipmentMethod = FXCollections.observableArrayList(new String[] { "Delivery", "Pickup" });
		ObservableList<MachineEntity> machines = FXCollections.observableArrayList();
		allMachines = CommonData.getMachines();
		shipmentMethodCmb.setItems(shipmentMethod);

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

		for (MachineEntity machine : allMachines) {
			if (NavigationStoreController.connectedUser != null)
				if (machine.getRegionName().equals(NavigationStoreController.connectedUser.getRegion())) {
					machines.add(machine);
				}
		}
		machineCmb.setItems(machines);
		machineCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				selectedMachine = machineCmb.getValue();
				if (selectedMachine.equals(null))
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a machine");
			}

		});
	}

	@FXML
	void cancelOrder(ActionEvent event) {
		selectedShipmentMethod = null;
		selectedMachine = null;
		((Stage) confirmBtn.getScene().getWindow()).close(); // close the popup window
	}

	@FXML
	void confirmMethod(ActionEvent event) {
		if (!selectedShipmentMethod.equals(null)) {
			if (((String) selectedShipmentMethod).equals("Pickup")) {
				OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "Pickup");
				OrderController.currentOrder.setMachine_id(selectedMachine.machineId);
			} else {
				OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "Delivery");
			}
		}
		((Stage) confirmBtn.getScene().getWindow()).close(); // close the popup window	
		NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ViewCatalog);
	}

}
