package controllerGui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import Store.NavigationStoreController;
import common.CommonData;
import common.CommonFunctions;
import common.PopupTypeEnum;
import controller.OrderController;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ShipmentMethodPopupController {

	@FXML
	private Button cancelBtn;

	@FXML
	private Button confirmBtn;

	@FXML
	private ComboBox<MachineEntity> machineCmb;

	@FXML
	private ComboBox<String> shipmentMethodCmb;
	private static String selectedShipmentMethod;
	private static ArrayList<MachineEntity> allMachines;
	public static ObservableList<String> shipmentMethod;
	public static MachineEntity selectedMachine;
	private String pickup ="Pickup";
	private String delivery ="Delivery";
	private static boolean isConfirmPressed=false;
	public void initialize() {
		shipmentMethod = FXCollections.observableArrayList();
		shipmentMethod.add(pickup);
		shipmentMethod.add(delivery);
		shipmentMethodCmb.setItems(shipmentMethod);
		shipmentMethodCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				selectedShipmentMethod = shipmentMethodCmb.getValue();
				if (selectedShipmentMethod.equals(null))
					CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a method");
				else if (newValue.equals(pickup)) {
					machineCmb.setVisible(true);
				} else
					machineCmb.setVisible(false);
			}

		});
		allMachines = CommonData.getMachines();
		ObservableList<MachineEntity> machines = FXCollections.observableArrayList();
		for (MachineEntity machine : allMachines) {
			if (NavigationStoreController.connectedUser != null)
				if (machine.getRegionName().equals(NavigationStoreController.connectedUser.getRegion())) {
					machines.add(machine);
				}
		}
		if (machineCmb.isVisible()) {
			machineCmb.setItems(machines);
			machineCmb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
				if (newValue != null) {
					selectedMachine = machineCmb.getValue();
					if (selectedMachine.equals(null))
						CommonFunctions.createPopup(PopupTypeEnum.Warning, "You have to choose a machine");
					else if (oldValue != newValue) {
						selectedMachine = machineCmb.getValue();
					}
				}

			});
		}
	}

	@FXML
	void cancelOrder(ActionEvent event) {
		selectedShipmentMethod=null;
		selectedMachine=null;
		((Stage) confirmBtn.getScene().getWindow()).close(); // close the popup window
	}

	@FXML
	void confirmMethod(ActionEvent event) {
		isConfirmPressed = true;
		
		if (!selectedShipmentMethod.equals(null)){
			if (((String)selectedShipmentMethod).equals(pickup)) {
			
				OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), pickup);
				OrderController.currentOrder.setMachine_id(selectedMachine.machineId);
			}
			else OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), delivery);
	
		}
		((Stage) confirmBtn.getScene().getWindow()).close(); // close the popup window
	}
}
