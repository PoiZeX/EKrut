package controllerGui;

import Store.DataStore;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.ScreensNamesEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChooseRegionPopUpController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private GridPane gridPane;
    
    @FXML
    private Label errLbl;

    @FXML
    private ComboBox<String> regionCmb;

    @FXML
    void cancel(ActionEvent event) {
    	((Stage) confirmBtn.getScene().getWindow()).close();
    }

    @FXML
    void confirm(ActionEvent event) {
    	String region = regionCmb.getValue();
    	if(CommonFunctions.isNullOrEmpty(region))
    		errLbl.setText("Please select region");
    	else {
    		NavigationStoreController.connectedUser.setRegion(region);
    		((Stage) confirmBtn.getScene().getWindow()).close();
    		NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.SalesManagement);
    	}
    }
    @FXML
    public void initialize() {
    	ObservableList<String> regions = FXCollections.observableArrayList(DataStore.getRegions());
    	regionCmb.setItems(regions);
    }
   

}
