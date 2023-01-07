package controllerGui;

import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ShipmentMethodPopupController {

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
		allMachines = CommonData.getMachines();
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

// Delivery -> Pickup (V) ` | Delivery -> Delivery (X) | Pickup -> Delivery (V) | Pickup -> Pickup (?) `
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
		CommonFunctions.SleepFor(1500, () -> {
			((Stage) gridPane.getScene().getWindow()).close();
		});

		setupLoading();

		if (OrderController.getCurrentOrder() == null) {
			createNewScreen();
			return;
		}
		prevMethod = OrderController.getCurrentOrder().getSupplyMethod();
		if (!selectedShipmentMethod.equals(prevMethod)) {
			createNewScreen();
			return;
		}
		if (selectedShipmentMethod.equals("Delivery") && prevMethod.equals("Delivery")) {
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ViewCatalog);
			return;
		}
		if (prevMethod.equals("Pickup")
				&& OrderController.getCurrentOrder().getMachine_id() != selectedMachine.getMachineId()) {
			createNewScreen();
			return;
		} else {
			NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ViewCatalog);
		}
	}

	private void setupLoading() {
		gridPane.getChildren().clear();
		gridPane.setMaxSize(150, 150);
		gridPane.getColumnConstraints().clear();
		gridPane.getRowConstraints().clear();
		Label loadingLabel = new Label("Loading, Please Wait...");
		loadingLabel.getStyleClass().add("LabelRoleTitle");
		ImageView img = new ImageView(new Image("/styles/images/loadingGif.gif"));
		img.setFitHeight(200);
		img.setFitWidth(200);
		gridPane.add(loadingLabel, 1, 1);
		gridPane.add(img, 1, 2);
		loadingLabel.setPadding(new Insets(10, 0, 0, 0));
		GridPane.setHalignment(loadingLabel, HPos.CENTER);
		GridPane.setHalignment(img, HPos.CENTER);
		GridPane.setValignment(loadingLabel, VPos.TOP);

	}

	private void createNewScreen() {

		switch (selectedShipmentMethod) {
		case "Pickup":
			OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "Pickup");
			OrderController.getCurrentOrder().setMachine_id(selectedMachine.machineId);
			break;
		case "Delivery":
			OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "Delivery");
			break;
		}
		NavigationStoreController.getInstance().refreshStage(ScreensNames.ViewCatalog);
	}

}
