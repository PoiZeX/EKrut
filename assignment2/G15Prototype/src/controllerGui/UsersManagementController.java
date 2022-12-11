package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UsersManagementController {

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

    @FXML
    void approveSelected(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {
    	NavigationStoreController.getInstance().goBack(event);
    }

    @FXML
    void refresh(ActionEvent event) {
		NavigationStoreController.getInstance().refreshStage(ScreensNames.HostClient);

    }

    @FXML
    void selectAll(ActionEvent event) {

    }

}
