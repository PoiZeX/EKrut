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
/**
 * This class is responsible for providing the functionality for the choose region pop-up window in the GUI.
 * It includes a ComboBox for selecting a region, and buttons for confirming or canceling the selection.
 * 
 * @author Vital
 */
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
    /**
     * Initializes the ComboBox with the available regions from the DataStore.
     */
    @FXML
    public void initialize() {
    	ObservableList<String> regions = FXCollections.observableArrayList(DataStore.getRegions());
    	regionCmb.setItems(regions);
    }
   
    /**
     * Closes the pop-up window without making any changes.
     * @param event the event that triggered this method
     */
    @FXML
    void cancel(ActionEvent event) {
    	((Stage) confirmBtn.getScene().getWindow()).close();
    }
    /**
     * Confirms the selected region and updates the connected user's region.
     * Closes the pop-up window and refreshes the sales management screen.
     * @param event the event that triggered this method
     */
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


}
