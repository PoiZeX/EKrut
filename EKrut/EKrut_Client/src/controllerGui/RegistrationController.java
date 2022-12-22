package controllerGui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class RegistrationController{
	private ToggleGroup radioToggleGroup;
    @FXML
    private Button cancelBtn;

    @FXML
    private TextField creditcardTxtField;

    @FXML
    private TextField emailTxtField;

    @FXML
    private TextField firstnameTxtField;

    @FXML
    private TextField idnumberTxtField;

    @FXML
    private TextField lastnameTxtField;

    @FXML
    private ToggleGroup memberToggleGroup;

    @FXML
    private GridPane membershipGridpaneBox;

    @FXML
    private TextField phonenumberTxtField;

    @FXML
    private RadioButton radioNoMember;

    @FXML
    private RadioButton radioYesMember;

    @FXML
    private Button submitBtn;

    public void initialize() {
    	radioToggleGroup = radioNoMember.getToggleGroup();
    	radioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
  
                RadioButton radioBtn = (RadioButton) radioToggleGroup.getSelectedToggle();
  
                if (radioBtn != null) {
                    String radioBtnValue = radioBtn.getText();
                    switch (radioBtnValue) {
	                	case "Yes":
	                		//membershipGridpaneBox.sh
	                		break;
	                	case "No":
	                		break;
	                	default:
	                		break;
                    }
                }
            }
        });
    }
    
    @FXML
    void cancelBtnAction(ActionEvent event) {

    }

    @FXML
    void submitBtnAction(ActionEvent event) {

    }

}
