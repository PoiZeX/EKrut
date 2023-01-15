package controllerGui;

import java.util.ArrayList;

import Store.DataStore;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.IScreen;
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
	/**
	This method is used to initialize the shipment method and machine comboboxes.
	It sets the shipment method combobox with the options "Delivery" and "Pickup" and sets the machines combobox with the machines that are in the region of the connected user.
	It also adds listeners to the comboboxes that listen to the selected item, if the shipment method is "Pickup" it will show the machines combobox otherwise, it will hide it.
	It also sets the selectedShipmentMethod and selectedMachine variables to null.
	*/
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
	
	/**
	This method is used to cancel an order by setting the 'selectedShipmentMethod' and 'selectedMachine' variables to null.
	It also closes the current window, which is assumed to be a pop-up window.
	@param event The action event that triggered this method
	*/
	@FXML
	void cancelOrder(ActionEvent event) {
		selectedShipmentMethod = null;
		selectedMachine = null;
		((Stage) confirmBtn.getScene().getWindow()).close(); // close the popup window
	}

	/**
	This method is used to confirm the selected shipment method and machine (if the method is "Pickup").
	It first checks if the shipment method is not null, if not it will show an error message.
	If the shipment method is "Pickup" and the selected machine is null, it will also show an error message.
	If the current order is null, it will create a new screen.
	If the selected shipment method is different from the previous method, it will also create a new screen.
	If the previous method is "Pickup" and the selected machine is different from the previous machine,
	it will also create a new screen.
	If all conditions are met, it will close the current window and navigate to the ViewCatalog screen.
	@param event The action event that triggered this method
	*/
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
	/**
	This method is used to create a new screen depending on the selected shipment method and machine.
	It first clears the current order and machine, then it checks the selected shipment method.
	If the method is "Pickup", it sets the current order and machine with the selected machine.
	If the method is "Delivery", it sets the current order without machine.
	It also closes the current window and navigates to the ViewCatalog screen.
	*/
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
