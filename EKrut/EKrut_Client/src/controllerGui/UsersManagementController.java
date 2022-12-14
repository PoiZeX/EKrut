package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utils.TooltipSetter;

public class UsersManagementController {
	
	private TooltipSetter tooltip;
	
    @FXML
    private TableColumn<?, ?> ID;

    @FXML
    private Button approveBtn;

    @FXML
    private TableColumn<?, ?> approveCheckB;

    @FXML
    private TableColumn<?, ?> creditCardNumber;

    @FXML
    private TableColumn<?, ?> customerID;

    @FXML
    private TableColumn<?, ?> email;

    @FXML
    private TableColumn<?, ?> firstName;

    @FXML
    private TableColumn<?, ?> lastName;

    @FXML
    private TableColumn<?, ?> phoneNumber;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button returnBtn;

    @FXML
    private Button selectBtn;

    @FXML
    private TableColumn<?, ?> subscriberID;

    @FXML
    private TableView<?> usersTable;

    public void initialize() {
    	tooltip = new TooltipSetter("Refresh the table");
    	refreshBtn.setTooltip(tooltip.getTooltip());
    	tooltip = new TooltipSetter("Approve all selected rows");
    	approveBtn.setTooltip(tooltip.getTooltip());
    	tooltip = new TooltipSetter("Select all rows");
    	selectBtn.setTooltip(tooltip.getTooltip());
    }
    
    @FXML
    void approveSelected(ActionEvent event) {

    }


    @FXML
    void refresh(ActionEvent event) {
		NavigationStoreController.getInstance().refreshStage(ScreensNames.UsersManagement);

    }

    @FXML
    void selectAll(ActionEvent event) {

    }

}
