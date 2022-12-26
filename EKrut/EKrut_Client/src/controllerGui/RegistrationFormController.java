package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.Message;
import common.TaskType;
import entity.RegistrationFormEntity;
import entity.SupplyReportEntity;
import entity.UserEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import utils.AppConfig;
import utils.TooltipSetter;

public class RegistrationFormController{
	private ToggleGroup radioToggleGroup;
	private ArrayList<TextField> dataArray;

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
    private TextField usernameTxtField;
    
    @FXML
    private Label usernameErrorLabel;

    @FXML
    private TextField passwordTxtField;
    
    @FXML
    private Label passwordErrorLabel;

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
    
    @FXML
    private Label errorMsgLabel;
    
    @FXML
    private Label userSearchMsgLabel;

    @FXML
    private TextField userSearchTxtField;

    @FXML
    private Button userSeatchBtn;


    ClientController chat = HostClientController.chat;
	protected static UserEntity reportDetails;	
	protected static boolean RecievedData = false;
    private static int numberOfForms;
    
    public void initialize() {
    	dataArray = new ArrayList<>();
    	dataArray.add(firstnameTxtField);
    	dataArray.add(lastnameTxtField);
    	dataArray.add(idnumberTxtField);
    	dataArray.add(emailTxtField);
    	dataArray.add(phonenumberTxtField);
    	dataArray.add(creditcardTxtField);
    	dataArray.add(usernameTxtField);
    	dataArray.add(passwordTxtField);
    	dataArray.add(userSearchTxtField);
    	
    	
    	membershipRadioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
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
    	passwordTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    // Handle password validation
    		String currentPassword = passwordTxtField.getText();
			if (currentPassword.contains(" ")) {
				passwordTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
				passwordErrorLabel.setText("Cannot include spaces");
			}
			else if (currentPassword.length() > AppConfig.PASSWORD_MAX_LENGTH) {
				passwordTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
				passwordErrorLabel.setText("Too long");
			}
			else if (currentPassword.length() < AppConfig.PASSWORD_MIN_LENGTH) {
				passwordTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
				passwordErrorLabel.setText("Too short");
			}
			else {
				passwordTxtField.setStyle("-fx-border-color: none;");
				passwordErrorLabel.setText("");
			}
    	});

    }
    

    @FXML
    void submitBtnAction(ActionEvent event) {
    	int missingTextFields = 0;
    	for (TextField field : dataArray) {
    		if (field.getText().equals("")) {
    			if (field != userSearchTxtField) {
        			missingTextFields++;
            		field.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
        			errorMsgLabel.setText("There are missing fields!");
    			}

    		}
    		else {
    			errorMsgLabel.setText("");
    			field.setStyle("-fx-border-color: none;");
    		}
    	}
    	if (missingTextFields == 0) {
        	int formID = ++numberOfForms;
        	RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
    		RegistrationFormEntity registrationForm = new RegistrationFormEntity(
    				formID,
    				firstnameTxtField.getText(),
    				lastnameTxtField.getText(),
    				idnumberTxtField.getText(),
    				emailTxtField.getText(),
    				creditcardTxtField.getText(),
    				radioBtn.getText()
    		);
    		if (radioBtn.getText().equals("Yes"))
    			registrationForm.setClubMemberID(formID + 1);
    		
    	}
    }
    
    @FXML
    void clearBtnAction(ActionEvent event) {
    	errorMsgLabel.setText("");
    	userSearchMsgLabel.setText("");
       	for (TextField field : dataArray) {
        	passwordErrorLabel.setText("");
        	usernameErrorLabel.setText("");
       		field.clear();
       		field.setDisable(false);
    		if (field.getText().equals("")) 
        		field.setStyle("-fx-border-color: none;");
    	}

    }
    
	public static void recieveDataFromServer(UserEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}
	
    @FXML
    void searchUserAction(ActionEvent event) {
    	if (userSearchTxtField.getText().equals("")) {
    		userSearchTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    		userSearchMsgLabel.setText("User ID is missing");
    		userSearchMsgLabel.setStyle("-fx-text-fill: #ff1414");
    	}
    	else {
    		userSearchTxtField.setStyle("-fx-border-color: none;");
    		userSearchMsgLabel.setText("");
        	String[] userSearchInput = {userSearchTxtField.getText()};
        	// Send a message to server
        	chat.acceptObj(new Message(TaskType.RequestUserInfoFromServerDB, userSearchInput));
    		// wait for answer
    		while (RecievedData == false) {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		if (reportDetails.getId_num().equals("")) {
    			//System.out.println("Empty Report");
    			userSearchMsgLabel.setText("User not found!");
    			userSearchMsgLabel.setStyle("-fx-text-fill: #ff1414");
    			return;
    		}
    		userSearchMsgLabel.setText(reportDetails.getFirst_name() + " was found!");
    		userSearchMsgLabel.setStyle("-fx-text-fill: #000000");
    		
    		firstnameTxtField.setText(reportDetails.getFirst_name());
    		firstnameTxtField.setDisable(true);
    		
    		lastnameTxtField.setText(reportDetails.getLast_name());
    		lastnameTxtField.setDisable(true);
    		
    		emailTxtField.setText(reportDetails.getEmail());
    		
    		idnumberTxtField.setText(reportDetails.getId_num());
    		idnumberTxtField.setDisable(true);
    		
    		creditcardTxtField.setText(reportDetails.getCc_num());
    		
    		phonenumberTxtField.setText(reportDetails.getPhone_number());
    		
    		passwordTxtField.setText(reportDetails.getPassword());
    		
    		usernameTxtField.setText(reportDetails.getUsername());
    	}
    }
}
