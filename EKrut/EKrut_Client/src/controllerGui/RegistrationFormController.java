package controllerGui;

import java.util.ArrayList;
import java.util.regex.Pattern;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.RolesEnum;
import common.TaskType;
import controller.SMSMailHandlerController;
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
	protected static Object recivedData;
	protected static boolean isDataRecived = false;
	private String roleType;
	private boolean creditCardChecker = true;
    
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
    	regionComboBox.setStyle("-fx-background-radius: 15px;");
    	roleType = "registered";
    	membershipRadioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
                if (radioBtn != null) {
                    String radioBtnValue = radioBtn.getText();
                    switch (radioBtnValue) {
	                	case "Yes":
	                		roleType = RolesEnum.member.toString();
	                		membershipGridpaneBox.setVisible(true);
	                		break;
	                	case "No":
	                		roleType = RolesEnum.registered.toString();
	                		membershipGridpaneBox.setVisible(false);
	                		break;
	                	default:
	                		break;
                    }
                }
            }
        });
    	creditcardTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    if (newValue.length() != 16) {
    	    	creditCardChecker = false;
    	    	creditcardTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    	    }
    	    else if (newValue.length() == 0) {
    	    	creditcardTxtField.setStyle("-fx-border-color: none;");
    	    }
    	    else {
    	    	creditCardChecker = true;
    			creditcardTxtField.setStyle("-fx-border-color: none;");
    	    }
    	});
    }

    @FXML
    void submitBtnAction(ActionEvent event) {
    	errorMsgLabel.setStyle("-fx-text-fill: #ff1414");
    	int missingTextFields = 0;
    	
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
    	if (creditCardChecker == false) {
    		errorMsgLabel.setText("Invalid Credit Card Number!");
    		creditcardTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    	}
    	else {
        	if (missingTextFields == 0) {
        		updateUser();
        	}
        	else {
    			errorMsgLabel.setText("There are missing fields!");
    			regionComboBox.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 1;");
        	}
    	}

    }

    /**
     * Update the user entity
     */
    private void updateUser() {
    	String[] detailsToUpdate = {idnumberTxtField.getText(), roleType, regionComboBox.getValue(), creditcardTxtField.getText()};
    	chat.acceptObj(new Message(TaskType.RequestUserUpdateInDB, detailsToUpdate));
    	
		// wait for answer
		while (isDataRecived == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (recivedData instanceof UserEntity && ((UserEntity) recivedData).getId_num().equals(detailsToUpdate[0])) {
			errorMsgLabel.setStyle("-fx-text-fill: #000000");
			errorMsgLabel.setText("User updated successuflly!");
			sendMessageToRegionManager(((UserEntity)recivedData).getRegion());
			return;
		}
		else {
			errorMsgLabel.setText("A problem occured, please try again!");
		}
    }
    
    /**
     * Send a message to region manager
     * @param region
     */
    private void sendMessageToRegionManager(String region) {
		UserEntity manager = getRegionManager(region);
		
    	// send user to manager approval
    	SMSMailHandlerController.SendSMSOrMail("System", manager, "Need your action", "New user has been signed up\nplease go to 'Users Approval' to review and approve the request");
    }
    
    /**
     * 
     * @param get the manager of specific region
     * @return
     */
    private UserEntity getRegionManager(String region) {
    	// reset
    	isDataRecived = false;
    	recivedData = null;
    	
    	chat.acceptObj(new Message(TaskType.RequesManagerInfoFromServerDB, region));
    	
    	// wait for answer
		while (isDataRecived == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(recivedData instanceof UserEntity && (UserEntity)recivedData != null) {
			return (UserEntity)recivedData;
		}
		return null;
    }
    
    @FXML
    void clearBtnAction(ActionEvent event) {
    	regionComboBox.setStyle("-fx-border-color: none;");
    	regionComboBox.setPromptText("Region");
    	regionComboBox.getSelectionModel().clearSelection();
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
    
    /** 
     * receive data from server
     * @param user
     */
	public static void receiveDataFromServer(Object obj) {
		recivedData = obj;
		isDataRecived = true;
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
			while (isDataRecived == false) {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    		UserEntity userDetails = ((UserEntity)recivedData);
    		if (userDetails.getId_num().equals("")) {
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
           		field.setDisable(true);
        		if (field.getText().equals("")) 
            		field.setStyle("-fx-border-color: none;");
        	}	
           	
           	// success message
    		userSearchMsgLabel.setText(userDetails.getFirst_name() + " was found!");
    		userSearchMsgLabel.setStyle("-fx-text-fill: #000000");
    		
    		// fill fields
    		firstnameTxtField.setText(userDetails.getFirst_name());
    		lastnameTxtField.setText(userDetails.getLast_name());
    		emailTxtField.setText(userDetails.getEmail());
    		idnumberTxtField.setText(userDetails.getId_num());
    		creditcardTxtField.setText(userDetails.getCc_num());
    		phonenumberTxtField.setText(userDetails.getPhone_number());
    		passwordTxtField.setText(userDetails.getPassword());
    		usernameTxtField.setText(userDetails.getUsername());
    		regionComboBox.setValue(userDetails.getRegion());
       		creditcardTxtField.setDisable(false);  // the only one if need to change
    		
    	}
    }
}
