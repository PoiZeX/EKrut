package controllerGui;

import java.util.ArrayList;

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

public class RegistrationFormController{
	private ToggleGroup radioToggleGroup;
	private ArrayList<TextField> textFieldArray;
    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button clearBtn;

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
    private GridPane membershipGridpaneBox;

    @FXML
    private ToggleGroup membershipRadioToggleGroup;

    @FXML
    private TextField phonenumberTxtField;

    @FXML
    private RadioButton radioNoMember;

    @FXML
    private RadioButton radioYesMember;

    @FXML
    private Button submitBtn;


    public void initialize() {
    	textFieldArray = new ArrayList<>();
    	textFieldArray.add(creditcardTxtField);
    	textFieldArray.add(emailTxtField);
    	textFieldArray.add(firstnameTxtField);
    	textFieldArray.add(idnumberTxtField);
    	textFieldArray.add(lastnameTxtField);
    	textFieldArray.add(phonenumberTxtField);
    	
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
	                		membershipGridpaneBox.setVisible(true);
	                		break;
	                	case "No":
	                		membershipGridpaneBox.setVisible(false);
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
    	// Go back to the previous page
    }

    @FXML
    void submitBtnAction(ActionEvent event) {
    	// Submit form and continue
    }
    

    @FXML
    void clearBtnAction(ActionEvent event) {
    	for (TextField textField : textFieldArray) {
    		textField.clear();
    	}
    }

}
