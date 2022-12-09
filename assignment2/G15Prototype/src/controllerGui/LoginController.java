package controllerGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.ChangeScreen;

public class LoginController {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordTxtField;

    @FXML
    private TextField usernameTxtField;

    @FXML
    void loginBtnAction(ActionEvent event) {
    	// Go to next screen (controller creates the screen)
		Stage primaryStage = new Stage();
		ChangeScreen screenChanger = new ChangeScreen();
		screenChanger.changeScreen(primaryStage, "/boundary/HomePageBoundary.fxml", event);
    }

}
