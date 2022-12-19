package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordTxtField;

    @FXML
    private TextField usernameTxtField;
    
    @FXML
    private Button EKTLoginBtn;

    @FXML
    void loginBtnAction(ActionEvent event) {
    	// Go to next screen (controller creates the screen)
		NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.DeliveryManagement);
    	//NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ReportSelection);

    }
    
    @FXML
    void ektLoginAction(ActionEvent event) {
    	//
    }


}
