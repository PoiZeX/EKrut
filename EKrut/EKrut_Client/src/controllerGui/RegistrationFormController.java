package controllerGui;

import java.util.ArrayList;
import java.util.regex.Pattern;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.TaskType;
import entity.RegistrationFormEntity;
import entity.SupplyReportEntity;
import entity.UserEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import utils.AppConfig;
import utils.TooltipSetter;

public class RegistrationFormController{
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
    
    @FXML
    private ComboBox<String> regionComboBox;



    ClientController chat = HostClientController.chat;
	protected static UserEntity reportDetails;	
	protected static String messageDetails;
	protected static boolean RecievedData = false;
	private String roleType;
    
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
    	
    	regionComboBox.getItems().addAll("North", "Center", "South");
    	
    	membershipRadioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
                if (radioBtn != null) {
                    String radioBtnValue = radioBtn.getText();
                    switch (radioBtnValue) {
	                	case "Yes":
	                		roleType = "member";
	                		membershipGridpaneBox.setVisible(true);
	                		break;
	                	case "No":
	                		roleType = "registered";
	                		membershipGridpaneBox.setVisible(false);
	                		break;
	                	default:
	                		break;
                    }
                }
            }
        });
//    	passwordTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
//    	    // Handle password validation
//    		String currentPassword = passwordTxtField.getText();
//			if (currentPassword.contains(" ")) {
//				passwordTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				passwordErrorLabel.setText("Can't include spaces");
//			}
//			else if (currentPassword.length() > AppConfig.PASSWORD_MAX_LENGTH) {
//				passwordTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				passwordErrorLabel.setText("Too long");
//			}
//			else if (currentPassword.length() < AppConfig.PASSWORD_MIN_LENGTH && currentPassword.length() > 0) {
//				passwordTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				passwordErrorLabel.setText("Too short");
//			}
//			else if (currentPassword.length() == 0) {
//				passwordTxtField.setStyle("-fx-border-color: none;");
//				passwordErrorLabel.setText("");
//			}
//			else {
//				passwordTxtField.setStyle("-fx-border-color: none;");
//				passwordErrorLabel.setText("");
//			}
//    	});
    	
//    	usernameTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
//    	    // Handle password validation
//    		
//    		String currentUsername = usernameTxtField.getText();
//			if (currentUsername.contains(" ")) {
//				usernameTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				usernameErrorLabel.setText("Can't include spaces");
//			}
//			else if (!Pattern.matches(AppConfig.USERNAME_ALPHA_ALLOWED, currentUsername) && !(currentUsername.length() == 0)) {
//				usernameTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				usernameErrorLabel.setText("Invalid Username");
//			}
//			else if (currentUsername.length() > AppConfig.USERNAME_MAX_LENGTH) {
//				usernameTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				usernameErrorLabel.setText("Too long");
//			}
//			else if (currentUsername.length() < AppConfig.USERNAME_MIN_LENGTH && currentUsername.length() > 0) {
//				usernameTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
//				usernameErrorLabel.setText("Too short");
//			}
//			else if (currentUsername.length() == 0) {
//				usernameTxtField.setStyle("-fx-border-color: none;");
//				usernameErrorLabel.setText("");
//			}
////			else {
////				String[] usernameText = {currentUsername};
////				chat.acceptObj(new Message(TaskType.RequestUserInfoFromServerDB, usernameText));
////	    		while (RecievedData == false) {
////	    			try {
////	    				Thread.sleep(100);
////	    			} catch (InterruptedException e) {
////	    				e.printStackTrace();
////	    			}
////	    		}
////	    		if (reportDetails.getFirst_name().length() > 0) {
////	    			usernameTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
////	    			usernameErrorLabel.setText("User already exists!");
////	    			reportDetails = null;
////    			}
////	    		else {
////					usernameTxtField.setStyle("-fx-border-color: none;");
////					usernameErrorLabel.setText("");
////	    		}
////			}
//			else {
//				usernameTxtField.setStyle("-fx-border-color: none;");
//				usernameErrorLabel.setText("");
//			}
//			
//    	});
    }

    @FXML
    void submitBtnAction(ActionEvent event) {
    	errorMsgLabel.setStyle("-fx-text-fill: #ff1414");
    	int missingTextFields = 0;
    	String[] newRoleType = {idnumberTxtField.getText(), roleType};
    	// Check for empty text fields
    	for (TextField field : dataArray) {
    		if (field.getText().equals("")) {
    			if (field != userSearchTxtField) {
        			missingTextFields++;
            		field.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    			}
    		}
    		else {
    			errorMsgLabel.setText("");
    			field.setStyle("-fx-border-color: none;");
    		}
    	}
    	if (missingTextFields == 0) {
//        	RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
//    		RegistrationFormEntity registrationForm = new RegistrationFormEntity(
//    				formID,
//    				firstnameTxtField.getText(),
//    				lastnameTxtField.getText(),
//    				idnumberTxtField.getText(),
//    				emailTxtField.getText(),
//    				creditcardTxtField.getText(),
//    				radioBtn.getText()
//    		);
//    		if (radioBtn.getText().equals("Yes"))
//    			registrationForm.setClubMemberID(formID + 1);
    		// Update the user's role type in the DB
    		
        	chat.acceptObj(new Message(TaskType.RequestChangeUserRoleTypeInDB, newRoleType));
    		// wait for answer
    		while (RecievedData == false) {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		if (messageDetails.equals("Changed Successfully")) {
    			errorMsgLabel.setStyle("-fx-text-fill: #000000");
    			errorMsgLabel.setText("User Role updated successuflly!");
    		}
    		
    	}
    	else {
			errorMsgLabel.setText("There are missing fields!");
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
	
	public static void recieveMessageFromServer(String message) {
		messageDetails = message;
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
    		
    		// Clear errors if found a user
    		errorMsgLabel.setText("");
           	for (TextField field : dataArray) {
            	passwordErrorLabel.setText("");
            	usernameErrorLabel.setText("");
           		field.clear();
           		field.setDisable(false);
        		if (field.getText().equals("")) 
            		field.setStyle("-fx-border-color: none;");
        	}
    		
    		userSearchMsgLabel.setText(reportDetails.getFirst_name() + " was found!");
    		userSearchMsgLabel.setStyle("-fx-text-fill: #000000");
    		
    		firstnameTxtField.setText(reportDetails.getFirst_name());
    		firstnameTxtField.setDisable(true);
    		
    		lastnameTxtField.setText(reportDetails.getLast_name());
    		lastnameTxtField.setDisable(true);
    		
    		emailTxtField.setText(reportDetails.getEmail());
    		emailTxtField.setDisable(true);
    		
    		idnumberTxtField.setText(reportDetails.getId_num());
    		idnumberTxtField.setDisable(true);
    		
    		creditcardTxtField.setText(reportDetails.getCc_num());
    		
    		phonenumberTxtField.setText(reportDetails.getPhone_number());
    		phonenumberTxtField.setDisable(true);
    		
    		passwordTxtField.setText(reportDetails.getPassword());
    		passwordTxtField.setDisable(true);
    		
    		usernameTxtField.setText(reportDetails.getUsername());
    		usernameTxtField.setDisable(true);
    		
    		regionComboBox.setValue(reportDetails.getRegion());
    		regionComboBox.setDisable(true);
    	}
    }
}
